package com.integral.enigmaticlegacy.helpers;

import com.integral.enigmaticlegacy.registry.EnigmaticEnchantments;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class EnigmaticEnchantmentHelper {

	public static boolean hasCustomCrossbowEnchantments(ItemStack crossbowStack) {
		return (EnchantmentHelper.getItemEnchantmentLevel(EnigmaticEnchantments.ceaselessEnchantment, crossbowStack) > 0 || EnchantmentHelper.getItemEnchantmentLevel(EnigmaticEnchantments.sharpshooterEnchantment, crossbowStack) > 0);
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
		return EnchantmentHelper.getItemEnchantmentLevel(EnigmaticEnchantments.sharpshooterEnchantment, stack);
	}

	public static int getCeaselessLevel(ItemStack stack) {
		return EnchantmentHelper.getItemEnchantmentLevel(EnigmaticEnchantments.ceaselessEnchantment, stack);
	}

	public static int getNemesisCurseLevel(ItemStack stack) {
		return EnchantmentHelper.getItemEnchantmentLevel(EnigmaticEnchantments.nemesisCurse, stack);
	}
}
