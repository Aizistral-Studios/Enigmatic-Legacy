package com.aizistral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.aizistral.enigmaticlegacy.api.items.IMultiblockMiningTool;
import com.aizistral.enigmaticlegacy.api.materials.EnigmaticMaterials;
import com.aizistral.enigmaticlegacy.config.EtheriumConfigHandler;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.AOEMiningHelper;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBaseTool;
import com.aizistral.enigmaticlegacy.packets.clients.PacketFlameParticles;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.aizistral.etherium.core.IEtheriumConfig;
import com.aizistral.etherium.items.EtheriumAxe;
import com.aizistral.etherium.items.EtheriumPickaxe;
import com.aizistral.etherium.items.EtheriumShovel;
import com.aizistral.omniconfig.Configuration;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.network.PacketDistributor;

public class AstralBreaker extends ItemBaseTool implements IMultiblockMiningTool {
	public static Omniconfig.IntParameter miningRadius;
	public static Omniconfig.IntParameter miningDepth;

	public static Omniconfig.BooleanParameter flameParticlesToggle;

	@SubscribeConfig(receiveClient = true)
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("AstralBreaker");

		if (builder.config.getSidedType() != Configuration.SidedConfigType.CLIENT) {
			miningRadius = builder
					.comment("The radius of Astral Breaker AOE mining. Set to -1 to disable the feature.")
					.min(-1)
					.max(128 - 1)
					.getInt("MiningRadius", 3);


			miningDepth = builder
					.comment("The depth of Astral Breaker AOE mining.")
					.max(128 - 1)
					.getInt("MiningDepth", 1);
		} else {
			flameParticlesToggle = builder
					.comment("Whether or not flame particles should appear when the Astral Breaker breaks a block")
					.getBoolean("FlameParticlesToggle", true);
		}

		builder.popPrefix();
	}

	public AstralBreaker() {
		super(4F, -2.8F, EnigmaticMaterials.ETHERIUM, BlockTags.MINEABLE_WITH_PICKAXE,
				ItemBaseTool.getDefaultProperties()
				.defaultDurability(4000)
				.rarity(Rarity.EPIC)
				.fireResistant());

		this.effectiveMaterials.addAll(((EtheriumPickaxe) this.findTool("etherium_pickaxe")).effectiveMaterials);
		this.effectiveMaterials.addAll(((EtheriumAxe) this.findTool("etherium_axe")).effectiveMaterials);
		this.effectiveMaterials.addAll(((EtheriumShovel) this.findTool("etherium_shovel")).effectiveMaterials);
	}

	private Item findTool(String name) {
		return SuperpositionHandler.findItem(EnigmaticLegacy.MODID, name);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.astralBreaker1", ChatFormatting.GOLD, miningRadius.getValue() + EtheriumConfigHandler.instance().getAOEBoost(Minecraft.getInstance().player), miningDepth.getValue());
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.astralBreaker2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.astralBreaker3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.astralBreaker4");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		if (!this.areaEffectsAllowed(stack)) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.aoeDisabled");
		}
	}

	public void spawnFlameParticles(Level world, BlockPos pos) {
		EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 128, world.dimension())), new PacketFlameParticles(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 18, true));
	}

	@Override
	public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entityLiving) {
		if (!world.isClientSide) {
			this.spawnFlameParticles(world, pos);
		}

		if (entityLiving instanceof Player player && this.areaEffectsEnabled(player, stack) && this.effectiveMaterials.contains(state.getMaterial()) && !world.isClientSide && miningRadius.getValue() != -1) {
			HitResult trace = AOEMiningHelper.calcRayTrace(world, player, ClipContext.Fluid.ANY);

			if (trace.getType() == HitResult.Type.BLOCK) {
				BlockHitResult blockTrace = (BlockHitResult) trace;
				Direction face = blockTrace.getDirection();

				AOEMiningHelper.harvestCube(world, player, face, pos, this.effectiveMaterials, miningRadius.getValue() + EtheriumConfigHandler.instance().getAOEBoost(player), miningDepth.getValue(), true, pos, stack, (objPos, objState) -> {
					stack.hurtAndBreak(1, entityLiving, p -> p.broadcastBreakEvent(Mob.getEquipmentSlotForItem(stack)));
					this.spawnFlameParticles(world, objPos);
				});
			}
		}

		return super.mineBlock(stack, world, state, pos, entityLiving);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		player.startUsingItem(hand);

		if (player.isCrouching()) {
			this.toggleAreaEffects(player, stack);

			return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
		} else
			return super.use(world, player, hand);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (context.getPlayer().isCrouching())
			return this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
		else
			return super.useOn(context);
	}

	@Override
	public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
		return toolAction == ToolActions.AXE_DIG || toolAction == ToolActions.PICKAXE_DIG
				|| toolAction == ToolActions.SHOVEL_DIG || toolAction == ToolActions.HOE_DIG
				|| toolAction == ToolActions.SWORD_DIG || toolAction == ToolActions.SHEARS_DIG;
	}

}
