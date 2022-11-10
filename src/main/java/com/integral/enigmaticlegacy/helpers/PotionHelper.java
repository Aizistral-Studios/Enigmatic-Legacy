package com.integral.enigmaticlegacy.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.brewing.ComplexBrewingRecipe;
import com.integral.enigmaticlegacy.objects.AdvancedPotion;

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

		return EnigmaticLegacy.emptyPotion;
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


		return EnigmaticLegacy.emptyPotion;
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
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.haste)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.haste)),
								Ingredient.of(Items.REDSTONE)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.longHaste)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.haste)),
								Ingredient.of(Items.GLOWSTONE_DUST)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.strongHaste)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.AWKWARD)),
								Ingredient.of(Items.QUARTZ),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.haste)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.haste)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.haste)),
								Ingredient.of(Items.REDSTONE),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.longHaste)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.longHaste)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.haste)),
								Ingredient.of(Items.GLOWSTONE_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.strongHaste)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.strongHaste)
						));



		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.AWKWARD)),
								Ingredient.of(Items.QUARTZ),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.haste)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionLingering, EnigmaticLegacy.haste)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionLingering, EnigmaticLegacy.haste)),
								Ingredient.of(Items.REDSTONE),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.longHaste)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionLingering, EnigmaticLegacy.longHaste)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionLingering, EnigmaticLegacy.haste)),
								Ingredient.of(Items.GLOWSTONE_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.strongHaste)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionLingering, EnigmaticLegacy.strongHaste)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.MUNDANE)),
								Ingredient.of(Items.BLAZE_ROD)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.moltenHeart)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.moltenHeart)),
								Ingredient.of(Items.REDSTONE)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.longMoltenHeart)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.MUNDANE)),
								Ingredient.of(Items.BLAZE_ROD),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.moltenHeart)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.moltenHeart)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.moltenHeart)),
								Ingredient.of(Items.REDSTONE),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.longMoltenHeart)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.longMoltenHeart)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.MUNDANE)),
								Ingredient.of(Items.BLAZE_ROD),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.moltenHeart)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionLingering, EnigmaticLegacy.moltenHeart)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionLingering, EnigmaticLegacy.moltenHeart)),
								Ingredient.of(Items.REDSTONE),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.longMoltenHeart)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionLingering, EnigmaticLegacy.longMoltenHeart)
						));
	}


	public static void registerBasicUltimatePotions() {
		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_NIGHT_VISION)),
								Ingredient.of(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateNightVision)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_INVISIBILITY)),
								Ingredient.of(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateInvisibility)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_FIRE_RESISTANCE)),
								Ingredient.of(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateFireResistance)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_HEALING)),
								Ingredient.of(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateHealing)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_HARMING)),
								Ingredient.of(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateHarming)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_SLOW_FALLING)),
								Ingredient.of(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateSlowFalling)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_WEAKNESS)),
								Ingredient.of(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateWeakness)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_WATER_BREATHING)),
								Ingredient.of(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateWaterBreathing)
						));


		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_STRENGTH)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_STRENGTH)),
								Ingredient.of(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateStrength)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_LEAPING)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_LEAPING)),
								Ingredient.of(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateLeaping)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_SWIFTNESS)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_SWIFTNESS)),
								Ingredient.of(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateSwiftness)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_SLOWNESS)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_SLOWNESS)),
								Ingredient.of(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateSlowness)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_TURTLE_MASTER)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_TURTLE_MASTER)),
								Ingredient.of(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateTurtleMaster)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_POISON)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_POISON)),
								Ingredient.of(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimatePoison)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_REGENERATION)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_REGENERATION)),
								Ingredient.of(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateRegeneration)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.longHaste)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.strongHaste)),
								Ingredient.of(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateHaste)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.longMoltenHeart)),
								Ingredient.of(EnigmaticLegacy.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateMoltenHeart)
						));
	}



	public static void registerSplashUltimatePotions() {
		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_NIGHT_VISION)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateNightVision)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateNightVision)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_INVISIBILITY)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateInvisibility)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateInvisibility)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_FIRE_RESISTANCE)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateFireResistance)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateFireResistance)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_HEALING)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateHealing)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateHealing)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_HARMING)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateHarming)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateHarming)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_SLOW_FALLING)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateSlowFalling)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateSlowFalling)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_WEAKNESS)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateWeakness)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateWeakness)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_WATER_BREATHING)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateWaterBreathing)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateWaterBreathing)
						));




		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_STRENGTH)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_STRENGTH)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateStrength)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateStrength)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_LEAPING)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_LEAPING)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateLeaping)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateLeaping)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_SWIFTNESS)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_SWIFTNESS)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateSwiftness)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateSwiftness)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_SLOWNESS)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_SLOWNESS)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateSlowness)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateSlowness)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_TURTLE_MASTER)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_TURTLE_MASTER)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateTurtleMaster)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateTurtleMaster)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_POISON)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_POISON)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimatePoison)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimatePoison)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_REGENERATION)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_REGENERATION)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateRegeneration)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateRegeneration)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.longHaste)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.strongHaste)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateHaste)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateHaste)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.longMoltenHeart)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimateMoltenHeart)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateMoltenHeart)
						));
	}



	public static void registerLingeringUltimatePotions() {
		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_NIGHT_VISION)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateNightVision)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ultimateNightVision)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_INVISIBILITY)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateInvisibility)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ultimateInvisibility)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_FIRE_RESISTANCE)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateFireResistance)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ultimateFireResistance)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_HEALING)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateHealing)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ultimateHealing)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_HARMING)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateHarming)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ultimateHarming)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_SLOW_FALLING)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateSlowFalling)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ultimateSlowFalling)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_WEAKNESS)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateWeakness)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ultimateWeakness)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_WATER_BREATHING)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateWaterBreathing)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ultimateWaterBreathing)
						));




		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_STRENGTH)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_STRENGTH)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateStrength)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ultimateStrength)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_LEAPING)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_LEAPING)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateLeaping)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ultimateLeaping)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_SWIFTNESS)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_SWIFTNESS)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateSwiftness)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ultimateSwiftness)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_SLOWNESS)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_SLOWNESS)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateSlowness)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ultimateSlowness)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_TURTLE_MASTER)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_TURTLE_MASTER)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateTurtleMaster)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ultimateTurtleMaster)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_POISON)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_POISON)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimatePoison)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ultimatePoison)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_REGENERATION)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_REGENERATION)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateRegeneration)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ultimateRegeneration)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionLingering, EnigmaticLegacy.longHaste)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionLingering, EnigmaticLegacy.strongHaste)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateHaste)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ultimateHaste)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionLingering, EnigmaticLegacy.longMoltenHeart)),
								Ingredient.of(EnigmaticLegacy.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimateMoltenHeart)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.ultimateMoltenHeart)
						));
	}

}
