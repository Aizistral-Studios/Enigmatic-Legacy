package com.integral.enigmaticlegacy.items;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.helpers.AOEMiningHelper;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.item.ToolItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

public class EtheriumShovel extends ToolItem implements IPerhaps {

	public static Properties integratedProperties = new Item.Properties();
	public Set<Material> effectiveMaterials;

	public EtheriumShovel(IItemTier tier, Function<Properties, Properties> properties) {
		super(2.5F, -3.0F, tier, new HashSet<>(), properties.apply(new Properties().defaultMaxDamage((int) (tier.getMaxUses() * 1.5)).addToolType(ToolType.SHOVEL, tier.getHarvestLevel())));

		this.effectiveMaterials = Sets.newHashSet();
		this.effectiveMaterials.add(Material.EARTH);
		this.effectiveMaterials.add(Material.ORGANIC);
		this.effectiveMaterials.add(Material.SNOW);
		this.effectiveMaterials.add(Material.SNOW_BLOCK);
		this.effectiveMaterials.add(Material.SAND);
	}

	public static Properties setupIntegratedProperties() {
		EtheriumShovel.integratedProperties.group(EnigmaticLegacy.enigmaticTab);
		EtheriumShovel.integratedProperties.maxStackSize(1);
		EtheriumShovel.integratedProperties.rarity(Rarity.RARE);

		return EtheriumShovel.integratedProperties;

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (ConfigHandler.ETHERIUM_SHOVEL_RADIUS.getValue() == -1)
			return;

		if (Screen.hasShiftDown()) {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumShovel1", ConfigHandler.ETHERIUM_SHOVEL_RADIUS.getValue(), ConfigHandler.ETHERIUM_SHOVEL_DEPTH.getValue());
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumShovel2");
		} else {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.ETHERIUM_TOOLS_ENABLED.getValue();
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity entityLiving) {

		if (entityLiving instanceof PlayerEntity && !entityLiving.isShiftKeyDown() && this.effectiveMaterials.contains(state.getMaterial()) && !world.isRemote && ConfigHandler.ETHERIUM_SHOVEL_RADIUS.getValue() != -1) {

			RayTraceResult trace = AOEMiningHelper.calcRayTrace(world, (PlayerEntity) entityLiving, RayTraceContext.FluidMode.ANY);

			if (trace.getType() == RayTraceResult.Type.BLOCK) {
				BlockRayTraceResult blockTrace = (BlockRayTraceResult) trace;
				Direction face = blockTrace.getFace();

				AOEMiningHelper.harvestCube(world, (PlayerEntity) entityLiving, face, pos, this.effectiveMaterials, ConfigHandler.ETHERIUM_SHOVEL_RADIUS.getValue(), ConfigHandler.ETHERIUM_SHOVEL_DEPTH.getValue(), false, pos, stack, (objPos, objState) -> {
					stack.damageItem(1, entityLiving, p -> p.sendBreakAnimation(MobEntity.getSlotForItemStack(stack)));
				});
			}
		}

		return super.onBlockDestroyed(stack, world, state, pos, entityLiving);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		return Items.DIAMOND_SHOVEL.onItemUse(context);
	}

	@Override
	public boolean canHarvestBlock(BlockState blockIn) {
		return Items.DIAMOND_SHOVEL.canHarvestBlock(blockIn);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		Material material = state.getMaterial();
		return !this.effectiveMaterials.contains(material) ? super.getDestroySpeed(stack, state) : this.efficiency;
	}

}
