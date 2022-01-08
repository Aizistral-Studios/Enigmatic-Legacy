package com.integral.etherium.items;

import java.util.HashSet;
import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.helpers.AOEMiningHelper;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.etherium.core.EtheriumUtil;
import com.integral.etherium.core.IEtheriumConfig;
import com.integral.etherium.items.generic.ItemEtheriumTool;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseContext;
import net.minecraft.world.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.util.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.util.math.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

public class EtheriumPickaxe extends ItemEtheriumTool {

	public EtheriumPickaxe(IEtheriumConfig config) {
		super(1F, -2.8F, config, new HashSet<>(),
				EtheriumUtil.defaultProperties(config, EtheriumPickaxe.class)
				.defaultDurability((int) (config.getToolMaterial().getUses() * 1.5))
				.addToolType(ToolType.PICKAXE, config.getToolMaterial().getLevel())
				.fireResistant());

		this.setRegistryName(new ResourceLocation(config.getOwnerMod(), "etherium_pickaxe"));

		this.effectiveMaterials.add(Material.METAL);
		this.effectiveMaterials.add(Material.STONE);
		this.effectiveMaterials.add(Material.HEAVY_METAL);
		this.effectiveMaterials.add(Material.GLASS);
		this.effectiveMaterials.add(Material.ICE_SOLID);
		this.effectiveMaterials.add(Material.ICE);
		this.effectiveMaterials.add(Material.SHULKER_SHELL);

		this.config.getSorceryMaterial("MARBLE").ifPresent(this.effectiveMaterials::add);
		this.config.getSorceryMaterial("BLACK_MARBLE").ifPresent(this.effectiveMaterials::add);
	}

	@Override
	public String getDescriptionId() {
		return this.config.isStandalone() ? "item.enigmaticlegacy." + this.getRegistryName().getPath() : super.getDescriptionId();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		if (this.config.getPickaxeMiningRadius() == -1)
			return;

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumPickaxe1", ChatFormatting.GOLD, this.config.getPickaxeMiningRadius(), this.config.getPickaxeMiningDepth());
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

			if (!this.config.disableAOEShiftInhibition()) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumPickaxe2");
			}
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumPickaxe3");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		if (!this.areaEffectsAllowed(stack)) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.aoeDisabled");
		}
	}

	@Override
	public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entityLiving) {

		if (entityLiving instanceof Player && this.areaEffectsEnabled((Player) entityLiving, stack) && this.effectiveMaterials.contains(state.getMaterial()) && !world.isClientSide && this.config.getPickaxeMiningRadius() != -1) {

			HitResult trace = AOEMiningHelper.calcRayTrace(world, (Player) entityLiving, ClipContext.Fluid.ANY);

			if (trace.getType() == HitResult.Type.BLOCK) {
				BlockHitResult blockTrace = (BlockHitResult) trace;
				Direction face = blockTrace.getDirection();

				AOEMiningHelper.harvestCube(world, (Player) entityLiving, face, pos, this.effectiveMaterials, this.config.getPickaxeMiningRadius(), this.config.getPickaxeMiningDepth(), true, pos, stack, (objPos, objState) -> {
					stack.hurtAndBreak(1, entityLiving, p -> p.broadcastBreakEvent(Mob.getEquipmentSlotForItem(stack)));
				});
			}
		}

		return super.mineBlock(stack, world, state, pos, entityLiving);
	}

	@Override
	public ActionResult<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		player.startUsingItem(hand);

		if (player.isCrouching()) {
			this.toggleAreaEffects(player, stack);

			return new ActionResult<>(InteractionResult.SUCCESS, stack);
		} else
			return super.use(world, player, hand);
	}

	@Override
	public InteractionResult useOn(ItemUseContext context) {
		if (context.getPlayer().isCrouching())
			return this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
		else
			return super.useOn(context);
	}

}
