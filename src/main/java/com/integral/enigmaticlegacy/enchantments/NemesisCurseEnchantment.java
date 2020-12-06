package com.integral.enigmaticlegacy.enchantments;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class NemesisCurseEnchantment extends Enchantment {
	public NemesisCurseEnchantment(final EquipmentSlotType... slots) {
		super(Enchantment.Rarity.RARE, EnchantmentType.WEAPON, slots);
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "nemesis_curse"));
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 25;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return 50;
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
	public boolean canApply(ItemStack stack) {
		return super.canApply(stack) || Enchantments.SHARPNESS.canApply(stack);
	}

	@Override
	public boolean isTreasureEnchantment() {
		return true;
	}

	@Override
	public boolean isCurse() {
		return true;
	}

	@Override
	public boolean isAllowedOnBooks() {
		return true;
	}

	@Override
	protected boolean canApplyTogether(Enchantment ench) {
		return super.canApplyTogether(ench);
	}
}
