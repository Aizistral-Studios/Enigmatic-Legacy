package com.integral.enigmaticlegacy.brewing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.helpers.PotionHelper;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractBrewingRecipe implements IBrewingRecipe {

	public static HashMap<ResourceLocation, List<AbstractBrewingRecipe>> recipeMap = new HashMap<ResourceLocation, List<AbstractBrewingRecipe>>();
	public static final AbstractBrewingRecipe EMPTY_RECIPE = new SpecialBrewingRecipe(Ingredient.fromStacks(ItemStack.EMPTY), Ingredient.fromStacks(ItemStack.EMPTY), ItemStack.EMPTY, new ResourceLocation(EnigmaticLegacy.MODID, "empty_recipe"));


	public AbstractBrewingRecipe(ResourceLocation registryName) {
		if (AbstractBrewingRecipe.recipeMap.containsKey(registryName)) {
			List<AbstractBrewingRecipe> list = AbstractBrewingRecipe.recipeMap.get(registryName);
			list.add(this);
			AbstractBrewingRecipe.recipeMap.put(registryName, list);
		} else {
			List<AbstractBrewingRecipe> list = new ArrayList<AbstractBrewingRecipe>();
			list.add(this);
			AbstractBrewingRecipe.recipeMap.put(registryName, list);
		}
	}

}
