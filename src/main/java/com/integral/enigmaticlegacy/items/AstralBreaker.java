package com.integral.enigmaticlegacy.items;

import java.util.HashSet;
import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.api.items.IMultiblockMiningTool;
import com.integral.enigmaticlegacy.api.materials.EnigmaticMaterials;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.helpers.AOEMiningHelper;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseTool;
import com.integral.enigmaticlegacy.packets.clients.PacketFlameParticles;
import com.integral.omniconfig.Configuration;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.PacketDistributor;

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
		super(4F, -2.8F, EnigmaticMaterials.ETHERIUM, new HashSet<>(),
				ItemBaseTool.getDefaultProperties()
				.addToolType(ToolType.PICKAXE, EnigmaticMaterials.ETHERIUM.getHarvestLevel())
				.addToolType(ToolType.AXE, EnigmaticMaterials.ETHERIUM.getHarvestLevel())
				.addToolType(ToolType.SHOVEL, EnigmaticMaterials.ETHERIUM.getHarvestLevel())
				.defaultMaxDamage(4000)
				.rarity(Rarity.EPIC)
				.isImmuneToFire());
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "astral_breaker"));

		this.effectiveMaterials.addAll(EnigmaticLegacy.etheriumPickaxe.effectiveMaterials);
		this.effectiveMaterials.addAll(EnigmaticLegacy.etheriumAxe.effectiveMaterials);
		this.effectiveMaterials.addAll(EnigmaticLegacy.etheriumShovel.effectiveMaterials);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.astralBreaker1", TextFormatting.GOLD, miningRadius.getValue(), miningDepth.getValue());
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

	public void spawnFlameParticles(World world, BlockPos pos) {
		EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 128, world.getDimensionKey())), new PacketFlameParticles(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 18, true));
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity entityLiving) {

		if (!world.isRemote) {
			this.spawnFlameParticles(world, pos);
		}

		if (entityLiving instanceof PlayerEntity && this.areaEffectsEnabled((PlayerEntity) entityLiving, stack) && this.effectiveMaterials.contains(state.getMaterial()) && !world.isRemote && miningRadius.getValue() != -1) {

			RayTraceResult trace = AOEMiningHelper.calcRayTrace(world, (PlayerEntity) entityLiving, RayTraceContext.FluidMode.ANY);

			if (trace.getType() == RayTraceResult.Type.BLOCK) {
				BlockRayTraceResult blockTrace = (BlockRayTraceResult) trace;
				Direction face = blockTrace.getFace();

				AOEMiningHelper.harvestCube(world, (PlayerEntity) entityLiving, face, pos, this.effectiveMaterials, miningRadius.getValue(), miningDepth.getValue(), true, pos, stack, (objPos, objState) -> {

					stack.damageItem(1, entityLiving, p -> p.sendBreakAnimation(MobEntity.getSlotForItemStack(stack)));
					this.spawnFlameParticles(world, objPos);

				});
			}
		}

		return super.onBlockDestroyed(stack, world, state, pos, entityLiving);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		player.setActiveHand(hand);

		if (player.isCrouching()) {
			this.toggleAreaEffects(player, stack);

			return new ActionResult<>(ActionResultType.SUCCESS, stack);
		} else
			return super.onItemRightClick(world, player, hand);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		if (context.getPlayer().isCrouching())
			return this.onItemRightClick(context.getWorld(), context.getPlayer(), context.getHand()).getType();
		else
			return super.onItemUse(context);
	}

}
