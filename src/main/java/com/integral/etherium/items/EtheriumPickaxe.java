package com.integral.etherium.items;

import java.util.HashSet;
import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.helpers.AOEMiningHelper;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.etherium.core.EtheriumUtil;
import com.integral.etherium.core.IEtheriumConfig;
import com.integral.etherium.items.generic.ItemEtheriumTool;

import net.minecraft.world.level.block.BlockState;
import net.minecraft.world.level.block.material.Material;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobEntity;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseContext;
import net.minecraft.world.item.Rarity;
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
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (this.config.getPickaxeMiningRadius() == -1)
			return;

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumPickaxe1", TextFormatting.GOLD, this.config.getPickaxeMiningRadius(), this.config.getPickaxeMiningDepth());
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
	public boolean mineBlock(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity entityLiving) {

		if (entityLiving instanceof PlayerEntity && this.areaEffectsEnabled((PlayerEntity) entityLiving, stack) && this.effectiveMaterials.contains(state.getMaterial()) && !world.isClientSide && this.config.getPickaxeMiningRadius() != -1) {

			RayTraceResult trace = AOEMiningHelper.calcRayTrace(world, (PlayerEntity) entityLiving, RayTraceContext.FluidMode.ANY);

			if (trace.getType() == RayTraceResult.Type.BLOCK) {
				BlockRayTraceResult blockTrace = (BlockRayTraceResult) trace;
				Direction face = blockTrace.getDirection();

				AOEMiningHelper.harvestCube(world, (PlayerEntity) entityLiving, face, pos, this.effectiveMaterials, this.config.getPickaxeMiningRadius(), this.config.getPickaxeMiningDepth(), true, pos, stack, (objPos, objState) -> {
					stack.hurtAndBreak(1, entityLiving, p -> p.broadcastBreakEvent(MobEntity.getEquipmentSlotForItem(stack)));
				});
			}
		}

		return super.mineBlock(stack, world, state, pos, entityLiving);
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getItemInHand(hand);
		player.startUsingItem(hand);

		if (player.isCrouching()) {
			this.toggleAreaEffects(player, stack);

			return new ActionResult<>(ActionResultType.SUCCESS, stack);
		} else
			return super.use(world, player, hand);
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		if (context.getPlayer().isCrouching())
			return this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
		else
			return super.useOn(context);
	}

}
