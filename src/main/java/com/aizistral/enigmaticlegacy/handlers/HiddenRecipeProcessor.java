package com.aizistral.enigmaticlegacy.handlers;

import com.aizistral.enigmaticlegacy.crafting.HiddenRecipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class HiddenRecipeProcessor implements IComponentProcessor {
	private ItemStack[][] grid;
	private ItemStack output;

	@Override
	public void setup(Level level, IVariableProvider variables) {
		ResourceLocation recipeId = new ResourceLocation(variables.get("recipe").asString());
		var recipe = HiddenRecipe.getRecipe(recipeId);

		this.grid = recipe.getKey();
		this.output = recipe.getValue();
	}

	@Override
	public IVariable process(Level level, String key) {
		if (key.startsWith("input1"))
			return IVariable.from(this.grid[0][0]);
		else if (key.startsWith("input2"))
			return IVariable.from(this.grid[0][1]);
		else if (key.startsWith("input3"))
			return IVariable.from(this.grid[0][2]);
		else if (key.startsWith("input4"))
			return IVariable.from(this.grid[1][0]);
		else if (key.startsWith("input5"))
			return IVariable.from(this.grid[1][1]);
		else if (key.startsWith("input6"))
			return IVariable.from(this.grid[1][2]);
		else if (key.startsWith("input7"))
			return IVariable.from(this.grid[2][0]);
		else if (key.startsWith("input8"))
			return IVariable.from(this.grid[2][1]);
		else if (key.startsWith("input9"))
			return IVariable.from(this.grid[2][2]);
		else if (key.startsWith("output"))
			return IVariable.from(this.output);

		return null;
	}

}
