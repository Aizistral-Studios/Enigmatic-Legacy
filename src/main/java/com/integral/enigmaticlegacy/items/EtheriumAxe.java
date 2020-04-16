package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.Set;

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
import net.minecraft.item.AxeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EtheriumAxe extends AxeItem implements IPerhaps {

	public static Properties integratedProperties = new Item.Properties();
	public Set<Material> effectiveMaterials;

	public EtheriumAxe(IItemTier tier, Properties properties, float attackSpeedIn, float attackDamageIn) {
		super(tier, attackDamageIn, attackSpeedIn, properties);

		this.effectiveMaterials = Sets.newHashSet();
		this.effectiveMaterials.add(Material.WOOD);
		this.effectiveMaterials.add(Material.LEAVES);
		this.effectiveMaterials.add(Material.CACTUS);
		this.effectiveMaterials.add(Material.BAMBOO);
	}

	public static Properties setupIntegratedProperties(int harvestLevel) {
		EtheriumAxe.integratedProperties.group(EnigmaticLegacy.enigmaticTab);
		EtheriumAxe.integratedProperties.maxStackSize(1);
		EtheriumAxe.integratedProperties.rarity(Rarity.RARE);

		return EtheriumAxe.integratedProperties;

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (ConfigHandler.ETHERIUM_AXE_VOLUME.getValue() == -1)
			return;

		if (Screen.hasShiftDown()) {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumAxe1", ConfigHandler.ETHERIUM_AXE_VOLUME.getValue());
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumAxe2");
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

		if (entityLiving instanceof PlayerEntity && !entityLiving.isShiftKeyDown() && this.effectiveMaterials.contains(state.getMaterial()) && !world.isRemote && ConfigHandler.ETHERIUM_AXE_VOLUME.getValue() != -1) {
			Direction face = Direction.UP;

			AOEMiningHelper.harvestCube(world, (PlayerEntity) entityLiving, face, pos.add(0, (ConfigHandler.ETHERIUM_AXE_VOLUME.getValue() - 1) / 2, 0), this.effectiveMaterials, ConfigHandler.ETHERIUM_AXE_VOLUME.getValue(), ConfigHandler.ETHERIUM_AXE_VOLUME.getValue(), false, pos, stack, (objPos, objState) -> {
				stack.damageItem(1, entityLiving, p -> p.sendBreakAnimation(MobEntity.getSlotForItemStack(stack)));
			});
		}

		return super.onBlockDestroyed(stack, world, state, pos, entityLiving);
	}

	/*
	 * public ActionResultType onItemUse(ItemUseContext context) { return
	 * Items.DIAMOND_AXE.onItemUse(context); }
	 */

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		Material material = state.getMaterial();
		return !this.effectiveMaterials.contains(material) ? super.getDestroySpeed(stack, state) : this.efficiency;
	}

}
