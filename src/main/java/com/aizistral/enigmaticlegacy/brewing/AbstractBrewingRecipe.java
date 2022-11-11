package com.aizistral.enigmaticlegacy.brewing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public abstract class AbstractBrewingRecipe implements IBrewingRecipe {

	public static HashMap<ResourceLocation, List<AbstractBrewingRecipe>> recipeMap = new HashMap<ResourceLocation, List<AbstractBrewingRecipe>>();
	public static final AbstractBrewingRecipe EMPTY_RECIPE = new SpecialBrewingRecipe(Ingredient.of(ItemStack.EMPTY), Ingredient.of(ItemStack.EMPTY), ItemStack.EMPTY, new ResourceLocation(EnigmaticLegacy.MODID, "empty_recipe"));


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
