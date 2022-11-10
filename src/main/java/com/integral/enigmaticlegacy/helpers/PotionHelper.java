package com.integral.enigmaticlegacy.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.integral.enigmaticlegacy.brewing.ComplexBrewingRecipe;
import com.integral.enigmaticlegacy.objects.AdvancedPotion;
import com.integral.enigmaticlegacy.registry.EnigmaticItems;
import com.integral.enigmaticlegacy.registry.EnigmaticPotions;

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

		return EnigmaticPotions.emptyPotion;
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

		for (AdvancedPotion potion : EnigmaticPotions.ultimatePotionTypes) {
			if (potion.getId().equals(identifier))
				return potion;
		}

		for (AdvancedPotion potion : EnigmaticPotions.commonPotionTypes) {
			if (potion.getId().equals(identifier))
				return potion;
		}


		return EnigmaticPotions.emptyPotion;
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
						PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionBase, EnigmaticPotions.haste)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionBase, EnigmaticPotions.haste)),
								Ingredient.of(Items.REDSTONE)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionBase, EnigmaticPotions.longHaste)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionBase, EnigmaticPotions.haste)),
								Ingredient.of(Items.GLOWSTONE_DUST)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionBase, EnigmaticPotions.strongHaste)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.AWKWARD)),
								Ingredient.of(Items.QUARTZ),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionBase, EnigmaticPotions.haste)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionSplash, EnigmaticPotions.haste)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionSplash, EnigmaticPotions.haste)),
								Ingredient.of(Items.REDSTONE),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionBase, EnigmaticPotions.longHaste)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionSplash, EnigmaticPotions.longHaste)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionSplash, EnigmaticPotions.haste)),
								Ingredient.of(Items.GLOWSTONE_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionBase, EnigmaticPotions.strongHaste)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionSplash, EnigmaticPotions.strongHaste)
						));



		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.AWKWARD)),
								Ingredient.of(Items.QUARTZ),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionSplash, EnigmaticPotions.haste)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionLingering, EnigmaticPotions.haste)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionLingering, EnigmaticPotions.haste)),
								Ingredient.of(Items.REDSTONE),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionSplash, EnigmaticPotions.longHaste)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionLingering, EnigmaticPotions.longHaste)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionLingering, EnigmaticPotions.haste)),
								Ingredient.of(Items.GLOWSTONE_DUST),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionSplash, EnigmaticPotions.strongHaste)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionLingering, EnigmaticPotions.strongHaste)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.MUNDANE)),
								Ingredient.of(Items.BLAZE_ROD)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionBase, EnigmaticPotions.moltenHeart)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionBase, EnigmaticPotions.moltenHeart)),
								Ingredient.of(Items.REDSTONE)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionBase, EnigmaticPotions.longMoltenHeart)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.MUNDANE)),
								Ingredient.of(Items.BLAZE_ROD),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionBase, EnigmaticPotions.moltenHeart)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionSplash, EnigmaticPotions.moltenHeart)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionSplash, EnigmaticPotions.moltenHeart)),
								Ingredient.of(Items.REDSTONE),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionBase, EnigmaticPotions.longMoltenHeart)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionSplash, EnigmaticPotions.longMoltenHeart)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.MUNDANE)),
								Ingredient.of(Items.BLAZE_ROD),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionSplash, EnigmaticPotions.moltenHeart)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionLingering, EnigmaticPotions.moltenHeart)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionLingering, EnigmaticPotions.moltenHeart)),
								Ingredient.of(Items.REDSTONE),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionSplash, EnigmaticPotions.longMoltenHeart)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionLingering, EnigmaticPotions.longMoltenHeart)
						));
	}


	public static void registerBasicUltimatePotions() {
		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_NIGHT_VISION)),
								Ingredient.of(EnigmaticItems.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateNightVision)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_INVISIBILITY)),
								Ingredient.of(EnigmaticItems.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateInvisibility)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_FIRE_RESISTANCE)),
								Ingredient.of(EnigmaticItems.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateFireResistance)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_HEALING)),
								Ingredient.of(EnigmaticItems.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateHealing)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_HARMING)),
								Ingredient.of(EnigmaticItems.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateHarming)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_SLOW_FALLING)),
								Ingredient.of(EnigmaticItems.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateSlowFalling)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_WEAKNESS)),
								Ingredient.of(EnigmaticItems.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateWeakness)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_WATER_BREATHING)),
								Ingredient.of(EnigmaticItems.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateWaterBreathing)
						));


		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_STRENGTH)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_STRENGTH)),
								Ingredient.of(EnigmaticItems.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateStrength)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_LEAPING)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_LEAPING)),
								Ingredient.of(EnigmaticItems.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateLeaping)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_SWIFTNESS)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_SWIFTNESS)),
								Ingredient.of(EnigmaticItems.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateSwiftness)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_SLOWNESS)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_SLOWNESS)),
								Ingredient.of(EnigmaticItems.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateSlowness)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_TURTLE_MASTER)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_TURTLE_MASTER)),
								Ingredient.of(EnigmaticItems.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateTurtleMaster)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_POISON)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_POISON)),
								Ingredient.of(EnigmaticItems.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimatePoison)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.LONG_REGENERATION)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.STRONG_REGENERATION)),
								Ingredient.of(EnigmaticItems.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateRegeneration)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionBase, EnigmaticPotions.longHaste)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionBase, EnigmaticPotions.strongHaste)),
								Ingredient.of(EnigmaticItems.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateHaste)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionBase, EnigmaticPotions.longMoltenHeart)),
								Ingredient.of(EnigmaticItems.astralDust)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateMoltenHeart)
						));
	}



	public static void registerSplashUltimatePotions() {
		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_NIGHT_VISION)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateNightVision)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateNightVision)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_INVISIBILITY)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateInvisibility)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateInvisibility)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_FIRE_RESISTANCE)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateFireResistance)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateFireResistance)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_HEALING)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateHealing)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateHealing)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_HARMING)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateHarming)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateHarming)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_SLOW_FALLING)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateSlowFalling)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateSlowFalling)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_WEAKNESS)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateWeakness)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateWeakness)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_WATER_BREATHING)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateWaterBreathing)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateWaterBreathing)
						));




		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_STRENGTH)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_STRENGTH)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateStrength)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateStrength)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_LEAPING)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_LEAPING)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateLeaping)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateLeaping)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_SWIFTNESS)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_SWIFTNESS)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateSwiftness)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateSwiftness)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_SLOWNESS)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_SLOWNESS)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateSlowness)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateSlowness)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_TURTLE_MASTER)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_TURTLE_MASTER)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateTurtleMaster)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateTurtleMaster)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_POISON)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_POISON)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimatePoison)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimatePoison)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.LONG_REGENERATION)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.SPLASH_POTION, Potions.STRONG_REGENERATION)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateRegeneration)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateRegeneration)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionSplash, EnigmaticPotions.longHaste)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionSplash, EnigmaticPotions.strongHaste)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateHaste)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateHaste)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionSplash, EnigmaticPotions.longMoltenHeart)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionBase, EnigmaticPotions.ultimateMoltenHeart)),
								Ingredient.of(Items.GUNPOWDER)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateMoltenHeart)
						));
	}



	public static void registerLingeringUltimatePotions() {
		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_NIGHT_VISION)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateNightVision)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionLingering, EnigmaticPotions.ultimateNightVision)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_INVISIBILITY)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateInvisibility)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionLingering, EnigmaticPotions.ultimateInvisibility)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_FIRE_RESISTANCE)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateFireResistance)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionLingering, EnigmaticPotions.ultimateFireResistance)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_HEALING)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateHealing)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionLingering, EnigmaticPotions.ultimateHealing)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_HARMING)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateHarming)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionLingering, EnigmaticPotions.ultimateHarming)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_SLOW_FALLING)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateSlowFalling)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionLingering, EnigmaticPotions.ultimateSlowFalling)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_WEAKNESS)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateWeakness)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionLingering, EnigmaticPotions.ultimateWeakness)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_WATER_BREATHING)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateWaterBreathing)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionLingering, EnigmaticPotions.ultimateWaterBreathing)
						));




		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_STRENGTH)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_STRENGTH)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateStrength)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionLingering, EnigmaticPotions.ultimateStrength)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_LEAPING)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_LEAPING)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateLeaping)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionLingering, EnigmaticPotions.ultimateLeaping)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_SWIFTNESS)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_SWIFTNESS)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateSwiftness)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionLingering, EnigmaticPotions.ultimateSwiftness)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_SLOWNESS)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_SLOWNESS)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateSlowness)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionLingering, EnigmaticPotions.ultimateSlowness)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_TURTLE_MASTER)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_TURTLE_MASTER)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateTurtleMaster)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionLingering, EnigmaticPotions.ultimateTurtleMaster)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_POISON)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_POISON)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimatePoison)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionLingering, EnigmaticPotions.ultimatePoison)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.LONG_REGENERATION)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createVanillaPotion(Items.LINGERING_POTION, Potions.STRONG_REGENERATION)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateRegeneration)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionLingering, EnigmaticPotions.ultimateRegeneration)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionLingering, EnigmaticPotions.longHaste)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionLingering, EnigmaticPotions.strongHaste)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateHaste)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionLingering, EnigmaticPotions.ultimateHaste)
						));

		BrewingRecipeRegistry.addRecipe(
				new ComplexBrewingRecipe(
						PotionHelper.constructIngredientMap(
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionLingering, EnigmaticPotions.longMoltenHeart)),
								Ingredient.of(EnigmaticItems.astralDust),
								Ingredient.of(PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionSplash, EnigmaticPotions.ultimateMoltenHeart)),
								Ingredient.of(Items.DRAGON_BREATH)
								),
						PotionHelper.createAdvancedPotion(EnigmaticItems.ultimatePotionLingering, EnigmaticPotions.ultimateMoltenHeart)
						));
	}

}
