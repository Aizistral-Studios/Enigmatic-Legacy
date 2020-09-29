package com.integral.enigmaticlegacy.helpers;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

public class EnigmaticEnchantmentHelper {

	public static boolean hasCustomCrossbowEnchantments(ItemStack crossbowStack) {
		return (EnchantmentHelper.getEnchantmentLevel(EnigmaticLegacy.ceaselessEnchantment, crossbowStack) > 0 || EnchantmentHelper.getEnchantmentLevel(EnigmaticLegacy.sharpshooterEnchantment, crossbowStack) > 0);
	}

	public static boolean hasSharpshooterEnchantment(ItemStack stack) {
		return EnigmaticEnchantmentHelper.getSharpshooterLevel(stack) > 0;
	}

	public static boolean hasCeaselessEnchantment(ItemStack stack) {
		return EnigmaticEnchantmentHelper.getCeaselessLevel(stack) > 0;
	}

	public static boolean hasNemesisCurseEnchantment(ItemStack stack) {
		return EnigmaticEnchantmentHelper.getNemesisCurseLevel(stack) > 0;
	}

	public static int getSharpshooterLevel(ItemStack stack) {
		return EnchantmentHelper.getEnchantmentLevel(EnigmaticLegacy.sharpshooterEnchantment, stack);
	}

	public static int getCeaselessLevel(ItemStack stack) {
		return EnchantmentHelper.getEnchantmentLevel(EnigmaticLegacy.ceaselessEnchantment, stack);
	}

	public static int getNemesisCurseLevel(ItemStack stack) {
		return EnchantmentHelper.getEnchantmentLevel(EnigmaticLegacy.nemesisCurseEnchantment, stack);
	}
}
