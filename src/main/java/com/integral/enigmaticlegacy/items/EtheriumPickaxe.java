package com.integral.enigmaticlegacy.items;

import java.util.HashSet;
import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.IMultiblockMiningTool;
import com.integral.enigmaticlegacy.api.materials.EnigmaticMaterials;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.helpers.AOEMiningHelper;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseTool;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
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

public class EtheriumPickaxe extends ItemBaseTool implements IMultiblockMiningTool {

	public EtheriumPickaxe() {
		super(1F, -2.8F, EnigmaticMaterials.ETHERIUM, new HashSet<>(),
				ItemBaseTool.getDefaultProperties()
				.defaultMaxDamage((int) (EnigmaticMaterials.ETHERIUM.getMaxUses() * 1.5))
				.addToolType(ToolType.PICKAXE, EnigmaticMaterials.ETHERIUM.getHarvestLevel())
				.rarity(Rarity.RARE)
				.isBurnable());

		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "etherium_pickaxe"));

		this.effectiveMaterials.add(Material.IRON);
		this.effectiveMaterials.add(Material.ROCK);
		this.effectiveMaterials.add(Material.ANVIL);
		this.effectiveMaterials.add(Material.GLASS);
		this.effectiveMaterials.add(Material.PACKED_ICE);
		this.effectiveMaterials.add(Material.ICE);
		this.effectiveMaterials.add(Material.SHULKER);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (ConfigHandler.ETHERIUM_PICKAXE_RADIUS.getValue() == -1)
			return;

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumPickaxe1", TextFormatting.GOLD, ConfigHandler.ETHERIUM_PICKAXE_RADIUS.getValue(), ConfigHandler.ETHERIUM_PICKAXE_DEPTH.getValue());
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

			if (!ConfigHandler.DISABLE_AOE_SHIFT_SUPPRESSION.getValue())
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumPickaxe2");
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
	public boolean isForMortals() {
		return ConfigHandler.ETHERIUM_TOOLS_ENABLED.getValue();
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity entityLiving) {

		if (entityLiving instanceof PlayerEntity && this.areaEffectsEnabled((PlayerEntity) entityLiving, stack) && this.effectiveMaterials.contains(state.getMaterial()) && !world.isRemote && ConfigHandler.ETHERIUM_PICKAXE_RADIUS.getValue() != -1) {

			RayTraceResult trace = AOEMiningHelper.calcRayTrace(world, (PlayerEntity) entityLiving, RayTraceContext.FluidMode.ANY);

			if (trace.getType() == RayTraceResult.Type.BLOCK) {
				BlockRayTraceResult blockTrace = (BlockRayTraceResult) trace;
				Direction face = blockTrace.getFace();

				AOEMiningHelper.harvestCube(world, (PlayerEntity) entityLiving, face, pos, this.effectiveMaterials, ConfigHandler.ETHERIUM_PICKAXE_RADIUS.getValue(), ConfigHandler.ETHERIUM_PICKAXE_DEPTH.getValue(), true, pos, stack, (objPos, objState) -> {
					stack.damageItem(1, entityLiving, p -> p.sendBreakAnimation(MobEntity.getSlotForItemStack(stack)));
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
