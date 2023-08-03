package com.aizistral.enigmaticlegacy.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.aizistral.enigmaticlegacy.brewing.AbstractBrewingRecipe;
import com.aizistral.enigmaticlegacy.brewing.ComplexBrewingRecipe;
import com.aizistral.enigmaticlegacy.brewing.SpecialBrewingRecipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
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
			variableList.addAll(AdvancedBrewingRecipeProcessor.wrapStackList(ingredient.getItems()));
		}

		return variableList;
	}

	@Override
	public void setup(Level level, IVariableProvider variables) {
		ResourceLocation recipeId = new ResourceLocation(variables.get("recipe").asString());
		int index = variables.get("index").asNumber().intValue();

		this.recipe = AbstractBrewingRecipe.recipeMap.containsKey(recipeId) ? AbstractBrewingRecipe.recipeMap.get(recipeId).get(index) : AbstractBrewingRecipe.EMPTY_RECIPE;
	}

	@Override
	public IVariable process(Level level, String key) {
		if (this.recipe instanceof SpecialBrewingRecipe) {
			SpecialBrewingRecipe special = (SpecialBrewingRecipe) this.recipe;

			if (key.startsWith("catalyst"))
				return IVariable.wrapList(AdvancedBrewingRecipeProcessor.wrapStackList(special.getIngredient().getItems()));
			else if (key.startsWith("input"))
				return IVariable.wrapList(AdvancedBrewingRecipeProcessor.wrapStackList(special.getInput().getItems()));
			else if (key.startsWith("output"))
				return IVariable.from(special.getOutput());

		} else if (this.recipe instanceof ComplexBrewingRecipe) {
			ComplexBrewingRecipe complex = (ComplexBrewingRecipe) this.recipe;
			HashMap<Ingredient, Ingredient> processingMappings = complex.getProcessingMappings();

			if (key.startsWith("catalyst"))
				return IVariable.wrapList(AdvancedBrewingRecipeProcessor.wrapIngredientSet(processingMappings.values()));
			else if (key.startsWith("input"))
				return IVariable.wrapList(AdvancedBrewingRecipeProcessor.wrapIngredientSet(processingMappings.keySet()));
			else if (key.startsWith("output"))
				return IVariable.from(complex.getOutput());

		}
		return null;
	}

}
