package com.integral.enigmaticlegacy.brewing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractBrewingRecipe implements IBrewingRecipe {

	public static HashMap<ResourceLocation, List<AbstractBrewingRecipe>> recipeMap = new HashMap<ResourceLocation, List<AbstractBrewingRecipe>>();

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
