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
		// Addons may register recipes during parallel mod-loading events.
		// Protect both the map and its per-ID lists with the same lock.
		synchronized (AbstractBrewingRecipe.recipeMap) {
			List<AbstractBrewingRecipe> list = AbstractBrewingRecipe.recipeMap.computeIfAbsent(registryName, key -> new ArrayList<>());
			list.add(this);
		}
	}

}
