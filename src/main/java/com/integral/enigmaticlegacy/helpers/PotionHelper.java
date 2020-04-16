package com.integral.enigmaticlegacy.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.brewing.ComplexBrewingRecipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

/**
 * Methods for interacting with advanced potion system.
 * Registering methods for them are also stored here.
 * @author Integral
 */

public class PotionHelper {

	public static int getColor(ItemStack stack) {

		if (PotionHelper.isAdvancedPotion(stack))
		if (PotionHelper.getEffects(stack) != null && PotionHelper.getEffects(stack).size() > 0)
			return PotionUtils.getPotionColorFromEffectList(PotionHelper.getEffects(stack));

			return PotionUtils.getColor(stack);

	};

	public static boolean isAdvancedPotion(ItemStack stack) {
		if (ItemNBTHelper.verifyExistance(stack, "EnigmaticPotion"))
				return true;


			return false;
	}

	public static List<EffectInstance> getEffects(ItemStack stack) {
			if (PotionHelper.isAdvancedPotion(stack)) {
				AdvancedPotion potion = PotionHelper.getAdvancedPotion(ItemNBTHelper.getString(stack, "EnigmaticPotion", "nothing"));
			    if (potion != null) {
			    	return potion.getEffects();
			    }
			}

		return new ArrayList<EffectInstance>();
	};

	public static AdvancedPotion getAdvancedPotion(ItemStack stack) {
		if (PotionHelper.isAdvancedPotion(stack)) {
			return PotionHelper.getAdvancedPotion(ItemNBTHelper.getString(stack, "EnigmaticPotion", "nothing"));
		}

		return EnigmaticLegacy.EMPTY;
	}

	public static ItemStack setAdvancedPotion(ItemStack stack, AdvancedPotion potion) {
		ItemNBTHelper.setString(stack, "EnigmaticPotion", potion.getId());
		return stack;
	}

	public static ItemStack setAdvancedPotion(ItemStack stack, String id) {
		ItemNBTHelper.setString(stack, "EnigmaticPotion", id);
		return stack;
	}

	public static AdvancedPotion getAdvancedPotion(String identifier) {

			for (AdvancedPotion potion : EnigmaticLegacy.ultimatePotionTypes) {
				if (potion.getId().equals(identifier))
					return potion;
			}

			for (AdvancedPotion potion : EnigmaticLegacy.commonPotionTypes) {
				if (potion.getId().equals(identifier))
					return potion;
			}


		return EnigmaticLegacy.EMPTY;
	}

	public static HashMap<Ingredient, Ingredient> constructIngredientMap(Ingredient... ingredients) {
		HashMap<Ingredient, Ingredient> returnMap = new HashMap<Ingredient, Ingredient>();

		if (ingredients.length % 2 != 0)
			throw new IllegalArgumentException("Uneven number of ingredients passed. This must not be!");

		for (int counter = 0; counter < ingredients.length; counter += 2) {
			returnMap.put(ingredients[counter], ingredients[counter+1]);
		}

		return returnMap;
	}

	public static ItemStack createAdvancedPotion(Item item, AdvancedPotion potion) {
		return PotionHelper.setAdvancedPotion(new ItemStack(item), potion);
	}

	public static ItemStack createVanillaPotion(Item item, Potion potion) {
		return PotionUtils.addPotionToItemStack(new ItemStack(item), potion);
	}

	public static void registerCommonPotions() {

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
				PotionHelper.constructIngredientMap(
						Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.POTION, Potions.AWKWARD)),
						Ingredient.fromItems(Items.QUARTZ)
						),
				PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.HASTE)
				));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
				PotionHelper.constructIngredientMap(
						Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.HASTE)),
						Ingredient.fromItems(Items.REDSTONE)
						),
				PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.LONG_HASTE)
				));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.HASTE)),
								Ingredient.fromItems(Items.GLOWSTONE_DUST)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.STRONG_HASTE)
						));



		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
				PotionHelper.constructIngredientMap(
						Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.AWKWARD)),
						Ingredient.fromItems(Items.QUARTZ),
						Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.HASTE)),
						Ingredient.fromItems(Items.GUNPOWDER)
						),
				PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.HASTE)
				));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
				PotionHelper.constructIngredientMap(
						Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.HASTE)),
						Ingredient.fromItems(Items.REDSTONE),
						Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.LONG_HASTE)),
						Ingredient.fromItems(Items.GUNPOWDER)
						),
				PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.LONG_HASTE)
				));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.HASTE)),
								Ingredient.fromItems(Items.GLOWSTONE_DUST),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.STRONG_HASTE)),
								Ingredient.fromItems(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.STRONG_HASTE)
						));



		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
				PotionHelper.constructIngredientMap(
						Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.AWKWARD)),
						Ingredient.fromItems(Items.QUARTZ),
						Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.HASTE)),
						Ingredient.fromItems(Items.DRAGON_BREATH)
						),
				PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionLingering, EnigmaticLegacy.HASTE)
				));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
				PotionHelper.constructIngredientMap(
						Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionLingering, EnigmaticLegacy.HASTE)),
						Ingredient.fromItems(Items.REDSTONE),
						Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.LONG_HASTE)),
						Ingredient.fromItems(Items.DRAGON_BREATH)
						),
				PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionLingering, EnigmaticLegacy.LONG_HASTE)
				));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionLingering, EnigmaticLegacy.HASTE)),
								Ingredient.fromItems(Items.GLOWSTONE_DUST),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.STRONG_HASTE)),
								Ingredient.fromItems(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionLingering, EnigmaticLegacy.STRONG_HASTE)
						));
	}


	public static void registerBasicUltimatePotions() {
		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
				PotionHelper.constructIngredientMap(
						Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_NIGHT_VISION)),
						Ingredient.fromItems(EnigmaticLegacy.astralDust)
						),
				PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_NIGHT_VISION)
				));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_INVISIBILITY)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_INVISIBILITY)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_FIRE_RESISTANCE)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_FIRE_RESISTANCE)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_HEALING)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_HEALING)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_HARMING)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_HARMING)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_SLOW_FALLING)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_SLOW_FALLING)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_WEAKNESS)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_WEAKNESS)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_WATER_BREATHING)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_WATER_BREATHING)
						));




		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_STRENGTH)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_STRENGTH)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_STRENGTH)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_LEAPING)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_LEAPING)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_LEAPING)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_SWIFTNESS)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_SWIFTNESS)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_SWIFTNESS)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_SLOWNESS)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_SLOWNESS)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_SLOWNESS)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_TURTLE_MASTER)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_TURTLE_MASTER)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_TURTLE_MASTER)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_POISON)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_POISON)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_POISON)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_REGENERATION)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_REGENERATION)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_REGENERATION)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.LONG_HASTE)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.STRONG_HASTE)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_HASTE)
						));
	}



	public static void registerSplashUltimatePotions() {
		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
				PotionHelper.constructIngredientMap(
						Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_NIGHT_VISION)),
						Ingredient.fromItems(EnigmaticLegacy.astralDust),
						Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_NIGHT_VISION)),
						Ingredient.fromItems(Items.GUNPOWDER)
						),
				PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_NIGHT_VISION)
				));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_INVISIBILITY)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_INVISIBILITY)),
								Ingredient.fromItems(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_INVISIBILITY)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_FIRE_RESISTANCE)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_FIRE_RESISTANCE)),
								Ingredient.fromItems(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_FIRE_RESISTANCE)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_HEALING)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_HEALING)),
								Ingredient.fromItems(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_HEALING)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_HARMING)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_HARMING)),
								Ingredient.fromItems(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_HARMING)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_SLOW_FALLING)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_SLOW_FALLING)),
								Ingredient.fromItems(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_SLOW_FALLING)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_WEAKNESS)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_WEAKNESS)),
								Ingredient.fromItems(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_WEAKNESS)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_WATER_BREATHING)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_WATER_BREATHING)),
								Ingredient.fromItems(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_WATER_BREATHING)
						));




		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_STRENGTH)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_STRENGTH)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_STRENGTH)),
								Ingredient.fromItems(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_STRENGTH)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_LEAPING)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_LEAPING)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_LEAPING)),
								Ingredient.fromItems(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_LEAPING)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_SWIFTNESS)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_SWIFTNESS)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_SWIFTNESS)),
								Ingredient.fromItems(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_SWIFTNESS)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_SLOWNESS)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_SLOWNESS)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_SLOWNESS)),
								Ingredient.fromItems(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_SLOWNESS)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_TURTLE_MASTER)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_TURTLE_MASTER)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_TURTLE_MASTER)),
								Ingredient.fromItems(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_TURTLE_MASTER)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_POISON)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_POISON)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_POISON)),
								Ingredient.fromItems(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_POISON)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_REGENERATION)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_REGENERATION)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_REGENERATION)),
								Ingredient.fromItems(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_REGENERATION)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.LONG_HASTE)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.STRONG_HASTE)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ULTIMATE_HASTE)),
								Ingredient.fromItems(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_HASTE)
						));
	}



	public static void registerLingeringUltimatePotions() {
		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
				PotionHelper.constructIngredientMap(
						Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_NIGHT_VISION)),
						Ingredient.fromItems(EnigmaticLegacy.astralDust),
						Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_NIGHT_VISION)),
						Ingredient.fromItems(Items.DRAGON_BREATH)
						),
				PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ULTIMATE_NIGHT_VISION)
				));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_INVISIBILITY)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_INVISIBILITY)),
								Ingredient.fromItems(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ULTIMATE_INVISIBILITY)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_FIRE_RESISTANCE)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_FIRE_RESISTANCE)),
								Ingredient.fromItems(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ULTIMATE_FIRE_RESISTANCE)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_HEALING)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_HEALING)),
								Ingredient.fromItems(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ULTIMATE_HEALING)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_HARMING)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_HARMING)),
								Ingredient.fromItems(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ULTIMATE_HARMING)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_SLOW_FALLING)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_SLOW_FALLING)),
								Ingredient.fromItems(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ULTIMATE_SLOW_FALLING)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_WEAKNESS)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_WEAKNESS)),
								Ingredient.fromItems(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ULTIMATE_WEAKNESS)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_WATER_BREATHING)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_WATER_BREATHING)),
								Ingredient.fromItems(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ULTIMATE_WATER_BREATHING)
						));




		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_STRENGTH)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_STRENGTH)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_STRENGTH)),
								Ingredient.fromItems(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ULTIMATE_STRENGTH)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_LEAPING)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_LEAPING)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_LEAPING)),
								Ingredient.fromItems(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ULTIMATE_LEAPING)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_SWIFTNESS)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_SWIFTNESS)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_SWIFTNESS)),
								Ingredient.fromItems(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ULTIMATE_SWIFTNESS)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_SLOWNESS)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_SLOWNESS)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_SLOWNESS)),
								Ingredient.fromItems(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ULTIMATE_SLOWNESS)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_TURTLE_MASTER)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_TURTLE_MASTER)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_TURTLE_MASTER)),
								Ingredient.fromItems(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ULTIMATE_TURTLE_MASTER)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_POISON)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_POISON)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_POISON)),
								Ingredient.fromItems(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ULTIMATE_POISON)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_REGENERATION)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_REGENERATION)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_REGENERATION)),
								Ingredient.fromItems(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ULTIMATE_REGENERATION)
						));


		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionLingering, EnigmaticLegacy.LONG_HASTE)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionLingering, EnigmaticLegacy.STRONG_HASTE)),
								Ingredient.fromItems(EnigmaticLegacy.astralDust),
								Ingredient.fromStacks(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ULTIMATE_HASTE)),
								Ingredient.fromItems(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ULTIMATE_HASTE)
						));
	}

}
