package com.integral.enigmaticlegacy.helpers;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;

public class EnigmaticEnchantmentHelper {

	public static boolean hasCustomCrossbowEnchantments(ItemStack crossbowStack) {
		return (EnchantmentHelper.getItemEnchantmentLevel(EnigmaticLegacy.ceaselessEnchantment, crossbowStack) > 0 || EnchantmentHelper.getItemEnchantmentLevel(EnigmaticLegacy.sharpshooterEnchantment, crossbowStack) > 0);
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
		return EnchantmentHelper.getItemEnchantmentLevel(EnigmaticLegacy.sharpshooterEnchantment, stack);
	}

	public static int getCeaselessLevel(ItemStack stack) {
		return EnchantmentHelper.getItemEnchantmentLevel(EnigmaticLegacy.ceaselessEnchantment, stack);
	}

	public static int getNemesisCurseLevel(ItemStack stack) {
		return EnchantmentHelper.getItemEnchantmentLevel(EnigmaticLegacy.nemesisCurseEnchantment, stack);
	}
}
