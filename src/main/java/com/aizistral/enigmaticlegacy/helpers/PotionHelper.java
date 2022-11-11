package com.aizistral.enigmaticlegacy.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.aizistral.enigmaticlegacy.brewing.ComplexBrewingRecipe;
import com.aizistral.enigmaticlegacy.objects.AdvancedPotion;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.aizistral.enigmaticlegacy.registries.EnigmaticPotions;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
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
				return PotionUtils.getColor(PotionHelper.getEffects(stack));

		return PotionUtils.getColor(stack);

	};

	public static boolean isAdvancedPotion(ItemStack stack) {
		if (ItemNBTHelper.verifyExistance(stack, "EnigmaticPotion"))
			return true;


		return false;
	}

	public static List<MobEffectInstance> getEffects(ItemStack stack) {
		if (PotionHelper.isAdvancedPotion(stack)) {
			AdvancedPotion potion = PotionHelper.getAdvancedPotion(ItemNBTHelper.getString(stack, "EnigmaticPotion", "nothing"));
			if (potion != null)
				return potion.getEffects();
		}

		return new ArrayList<MobEffectInstance>();
	};

	public static AdvancedPotion getAdvancedPotion(ItemStack stack) {
		if (PotionHelper.isAdvancedPotion(stack))
			return PotionHelper.getAdvancedPotion(ItemNBTHelper.getString(stack, "EnigmaticPotion", "nothing"));

		return EnigmaticPotions.EMPTY_POTION;
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

		for (AdvancedPotion potion : EnigmaticPotions.ULTIMATE_POTIONS) {
			if (potion.getId().equals(identifier))
				return potion;
		}

		for (AdvancedPotion potion : EnigmaticPotions.COMMON_POTIONS) {
			if (potion.getId().equals(identifier))
				return potion;
		}


		return EnigmaticPotions.EMPTY_POTION;
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
		return PotionUtils.setPotion(new ItemStack(item), potion);
	}

	public static void registerCommonPotions() {

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.AWKWARD)),
								Ingredient.of(Items.QUARTZ)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION, EnigmaticPotions.HASTE)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION, EnigmaticPotions.HASTE)),
								Ingredient.of(Items.REDSTONE)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION, EnigmaticPotions.LONG_HASTE)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION, EnigmaticPotions.HASTE)),
								Ingredient.of(Items.GLOWSTONE_DUST)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION, EnigmaticPotions.STRONG_HASTE)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.AWKWARD)),
								Ingredient.of(Items.QUARTZ),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION, EnigmaticPotions.HASTE)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_SPLASH, EnigmaticPotions.HASTE)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_SPLASH, EnigmaticPotions.HASTE)),
								Ingredient.of(Items.REDSTONE),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION, EnigmaticPotions.LONG_HASTE)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_SPLASH, EnigmaticPotions.LONG_HASTE)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_SPLASH, EnigmaticPotions.HASTE)),
								Ingredient.of(Items.GLOWSTONE_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION, EnigmaticPotions.STRONG_HASTE)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_SPLASH, EnigmaticPotions.STRONG_HASTE)
						));



		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.AWKWARD)),
								Ingredient.of(Items.QUARTZ),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_SPLASH, EnigmaticPotions.HASTE)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_LINGERING, EnigmaticPotions.HASTE)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_LINGERING, EnigmaticPotions.HASTE)),
								Ingredient.of(Items.REDSTONE),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_SPLASH, EnigmaticPotions.LONG_HASTE)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_LINGERING, EnigmaticPotions.LONG_HASTE)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_LINGERING, EnigmaticPotions.HASTE)),
								Ingredient.of(Items.GLOWSTONE_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_SPLASH, EnigmaticPotions.STRONG_HASTE)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_LINGERING, EnigmaticPotions.STRONG_HASTE)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.MUNDANE)),
								Ingredient.of(Items.BLAZE_ROD)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION, EnigmaticPotions.MOLTEN_HEART)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION, EnigmaticPotions.MOLTEN_HEART)),
								Ingredient.of(Items.REDSTONE)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION, EnigmaticPotions.LONG_MOLTEN_HEART)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.MUNDANE)),
								Ingredient.of(Items.BLAZE_ROD),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION, EnigmaticPotions.MOLTEN_HEART)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_SPLASH, EnigmaticPotions.MOLTEN_HEART)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_SPLASH, EnigmaticPotions.MOLTEN_HEART)),
								Ingredient.of(Items.REDSTONE),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION, EnigmaticPotions.LONG_MOLTEN_HEART)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_SPLASH, EnigmaticPotions.LONG_MOLTEN_HEART)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.MUNDANE)),
								Ingredient.of(Items.BLAZE_ROD),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_SPLASH, EnigmaticPotions.MOLTEN_HEART)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_LINGERING, EnigmaticPotions.MOLTEN_HEART)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_LINGERING, EnigmaticPotions.MOLTEN_HEART)),
								Ingredient.of(Items.REDSTONE),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_SPLASH, EnigmaticPotions.LONG_MOLTEN_HEART)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_LINGERING, EnigmaticPotions.LONG_MOLTEN_HEART)
						));
	}


	public static void registerBasicUltimatePotions() {
		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_NIGHT_VISION)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_NIGHT_VISION)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_INVISIBILITY)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_INVISIBILITY)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_FIRE_RESISTANCE)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_FIRE_RESISTANCE)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_HEALING)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_HEALING)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_HARMING)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_HARMING)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_SLOW_FALLING)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTMATE_SLOW_FALLING)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_WEAKNESS)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_WEAKNESS)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_WATER_BREATHING)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_WATER_BREATHING)
						));


		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_STRENGTH)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_STRENGTH)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_STRENGTH)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_LEAPING)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_LEAPING)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_LEAPING)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_SWIFTNESS)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_SWIFTNESS)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_SWIFTNESS)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_SLOWNESS)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_SLOWNESS)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_SLOWNESS)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_TURTLE_MASTER)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_TURTLE_MASTER)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_TURTLE_MASTER)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_POISON)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_POISON)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_POISON)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_REGENERATION)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_REGENERATION)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_REGENERATION)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION, EnigmaticPotions.LONG_HASTE)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION, EnigmaticPotions.STRONG_HASTE)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_HASTE)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION, EnigmaticPotions.LONG_MOLTEN_HEART)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_MOLTEN_HEART)
						));
	}



	public static void registerSplashUltimatePotions() {
		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_NIGHT_VISION)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_NIGHT_VISION)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_NIGHT_VISION)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_INVISIBILITY)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_INVISIBILITY)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_INVISIBILITY)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_FIRE_RESISTANCE)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_FIRE_RESISTANCE)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_FIRE_RESISTANCE)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_HEALING)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_HEALING)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_HEALING)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_HARMING)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_HARMING)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_HARMING)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_SLOW_FALLING)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTMATE_SLOW_FALLING)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTMATE_SLOW_FALLING)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_WEAKNESS)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_WEAKNESS)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_WEAKNESS)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_WATER_BREATHING)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_WATER_BREATHING)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_WATER_BREATHING)
						));




		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_STRENGTH)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_STRENGTH)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_STRENGTH)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_STRENGTH)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_LEAPING)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_LEAPING)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_LEAPING)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_LEAPING)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_SWIFTNESS)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_SWIFTNESS)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_SWIFTNESS)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_SWIFTNESS)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_SLOWNESS)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_SLOWNESS)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_SLOWNESS)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_SLOWNESS)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_TURTLE_MASTER)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_TURTLE_MASTER)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_TURTLE_MASTER)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_TURTLE_MASTER)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_POISON)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_POISON)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_POISON)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_POISON)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_REGENERATION)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_REGENERATION)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_REGENERATION)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_REGENERATION)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_SPLASH, EnigmaticPotions.LONG_HASTE)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_SPLASH, EnigmaticPotions.STRONG_HASTE)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_HASTE)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_HASTE)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_SPLASH, EnigmaticPotions.LONG_MOLTEN_HEART)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION, EnigmaticPotions.ULTIMATE_MOLTEN_HEART)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_MOLTEN_HEART)
						));
	}



	public static void registerLingeringUltimatePotions() {
		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_NIGHT_VISION)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_NIGHT_VISION)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_LINGERING, EnigmaticPotions.ULTIMATE_NIGHT_VISION)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_INVISIBILITY)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_INVISIBILITY)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_LINGERING, EnigmaticPotions.ULTIMATE_INVISIBILITY)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_FIRE_RESISTANCE)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_FIRE_RESISTANCE)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_LINGERING, EnigmaticPotions.ULTIMATE_FIRE_RESISTANCE)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_HEALING)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_HEALING)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_LINGERING, EnigmaticPotions.ULTIMATE_HEALING)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_HARMING)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_HARMING)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_LINGERING, EnigmaticPotions.ULTIMATE_HARMING)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_SLOW_FALLING)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTMATE_SLOW_FALLING)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_LINGERING, EnigmaticPotions.ULTMATE_SLOW_FALLING)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_WEAKNESS)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_WEAKNESS)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_LINGERING, EnigmaticPotions.ULTIMATE_WEAKNESS)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_WATER_BREATHING)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_WATER_BREATHING)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_LINGERING, EnigmaticPotions.ULTIMATE_WATER_BREATHING)
						));




		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_STRENGTH)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_STRENGTH)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_STRENGTH)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_LINGERING, EnigmaticPotions.ULTIMATE_STRENGTH)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_LEAPING)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_LEAPING)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_LEAPING)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_LINGERING, EnigmaticPotions.ULTIMATE_LEAPING)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_SWIFTNESS)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_SWIFTNESS)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_SWIFTNESS)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_LINGERING, EnigmaticPotions.ULTIMATE_SWIFTNESS)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_SLOWNESS)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_SLOWNESS)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_SLOWNESS)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_LINGERING, EnigmaticPotions.ULTIMATE_SLOWNESS)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_TURTLE_MASTER)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_TURTLE_MASTER)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_TURTLE_MASTER)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_LINGERING, EnigmaticPotions.ULTIMATE_TURTLE_MASTER)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_POISON)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_POISON)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_POISON)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_LINGERING, EnigmaticPotions.ULTIMATE_POISON)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_REGENERATION)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_REGENERATION)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_REGENERATION)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_LINGERING, EnigmaticPotions.ULTIMATE_REGENERATION)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_LINGERING, EnigmaticPotions.LONG_HASTE)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_LINGERING, EnigmaticPotions.STRONG_HASTE)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_HASTE)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_LINGERING, EnigmaticPotions.ULTIMATE_HASTE)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.COMMON_POTION_LINGERING, EnigmaticPotions.LONG_MOLTEN_HEART)),
								Ingredient.of(EnigmaticItems.ASTRAL_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_SPLASH, EnigmaticPotions.ULTIMATE_MOLTEN_HEART)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ULTIMATE_POTION_LINGERING, EnigmaticPotions.ULTIMATE_MOLTEN_HEART)
						));
	}

}
