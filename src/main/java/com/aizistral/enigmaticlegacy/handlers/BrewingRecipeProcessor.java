package com.aizistral.enigmaticlegacy.handlers;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BrewingRecipeProcessor implements IComponentProcessor {
	private IBrewingRecipe recipe;

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
			variableList.addAll(BrewingRecipeProcessor.wrapStackList(ingredient.getItems()));
		}

		return variableList;
	}

	@Override
	public void setup(Level level, IVariableProvider variables) {
		int index = variables.get("index").asNumber().intValue();
		// TODO: fix
		this.recipe = BrewingRecipeRegistry.getRecipes().get(index);
	}

	@Override
	public @Nonnull IVariable process(Level level, String key) {
		if (this.recipe instanceof BrewingRecipe brewingRecipe) {
			if (key.startsWith("catalyst"))
				return IVariable.wrapList(BrewingRecipeProcessor.wrapStackList(brewingRecipe.getIngredient().getItems()));
			else if (key.startsWith("input"))
				return IVariable.wrapList(BrewingRecipeProcessor.wrapStackList(brewingRecipe.getInput().getItems()));
			else if (key.startsWith("output"))
				return IVariable.from(brewingRecipe.getOutput());
		}

		return IVariable.empty();
	}

}
