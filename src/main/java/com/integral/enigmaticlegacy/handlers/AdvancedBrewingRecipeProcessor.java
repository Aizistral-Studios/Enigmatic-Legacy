package com.integral.enigmaticlegacy.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.integral.enigmaticlegacy.brewing.AbstractBrewingRecipe;
import com.integral.enigmaticlegacy.brewing.ComplexBrewingRecipe;
import com.integral.enigmaticlegacy.brewing.SpecialBrewingRecipe;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class AdvancedBrewingRecipeProcessor implements IComponentProcessor {

	private AbstractBrewingRecipe recipe;

	public static List<IVariable> wrapStackList(ItemStack... stackList) {
		List<IVariable> variableList = new ArrayList<IVariable>();

		for (ItemStack catalyst : stackList) {
			variableList.add(IVariable.from(catalyst));
		}

		return variableList;
	}

	public static List<IVariable> wrapIngredientSet(Iterable<Ingredient> ingredientSet) {
		List<IVariable> variableList = new ArrayList<IVariable>();

		for (Ingredient ingredient : ingredientSet) {
			variableList.addAll(AdvancedBrewingRecipeProcessor.wrapStackList(ingredient.getMatchingStacks()));
		}

		return variableList;
	}

	@Override
	public void setup(IVariableProvider variables) {

		String recipeId = variables.get("recipe").asString();
		int index = variables.get("index").asNumber().intValue();

		this.recipe = AbstractBrewingRecipe.recipeMap.get(new ResourceLocation(recipeId)).get(index);
	}

	@Override
	public IVariable process(String key) {

		if (this.recipe instanceof SpecialBrewingRecipe) {
			SpecialBrewingRecipe special = (SpecialBrewingRecipe) this.recipe;

			if (key.startsWith("catalyst")) {
				return IVariable.wrapList(AdvancedBrewingRecipeProcessor.wrapStackList(special.getIngredient().getMatchingStacks()));
			} else if (key.startsWith("input")) {
				return IVariable.wrapList(AdvancedBrewingRecipeProcessor.wrapStackList(special.getInput().getMatchingStacks()));
			} else if (key.startsWith("output")) {
				return IVariable.from(special.getOutput());
			}

		} else if (this.recipe instanceof ComplexBrewingRecipe) {
			ComplexBrewingRecipe complex = (ComplexBrewingRecipe) this.recipe;
			HashMap<Ingredient, Ingredient> processingMappings = complex.getProcessingMappings();

			if (key.startsWith("catalyst")) {
				return IVariable.wrapList(AdvancedBrewingRecipeProcessor.wrapIngredientSet(processingMappings.values()));
			} else if (key.startsWith("input")) {
				return IVariable.wrapList(AdvancedBrewingRecipeProcessor.wrapIngredientSet(processingMappings.keySet()));
			} else if (key.startsWith("output")) {
				return IVariable.from(complex.getOutput());
			}

		}
		return null;
	}

}
