package com.integral.enigmaticlegacy.enchantments;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

public class CeaselessEnchantment extends Enchantment {
	public static Omniconfig.BooleanParameter allowNoArrow;

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("CeaselessEnchantment");

		allowNoArrow = builder
				.comment("Whether or not crossbows with Ceaseless should be able to shoot basic arrows even if there are none in player's inventory.")
				.getBoolean("AllowNoArrow", true);

		builder.popPrefix();
	}

	public CeaselessEnchantment(final EquipmentSlot... slots) {
		super(Enchantment.Rarity.RARE, EnchantmentCategory.CROSSBOW, slots);
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "ceaseless"));
	}

	@Override
	public int getMinLevel() {
		return 1;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	protected boolean checkCompatibility(final Enchantment ench) {
		return super.checkCompatibility(ench);
	}

	@Override
	public boolean canApplyAtEnchantingTable(final ItemStack stack) {
		return this.canEnchant(stack) && stack.getItem() instanceof CrossbowItem;
	}

	@Override
	public boolean isTreasureOnly() {
		return false;
	}

	@Override
	public boolean isCurse() {
		return false;
	}

	@Override
	public boolean isAllowedOnBooks() {
		return OmniconfigHandler.isItemEnabled(this);
	}

	@Override
	public boolean isDiscoverable() {
		return OmniconfigHandler.isItemEnabled(this);
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		return OmniconfigHandler.isItemEnabled(this) && stack.canApplyAtEnchantingTable(this);
	}

}

