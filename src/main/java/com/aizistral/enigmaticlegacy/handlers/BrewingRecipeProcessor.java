package com.aizistral.enigmaticlegacy.handlers;

import com.aizistral.enigmaticlegacy.crafting.AdvancedBrewingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.registries.ForgeRegistries;
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
		if (variables.has("potion")) setupPotion(variables);
		else if (variables.has("item")) setupItem(variables);
	}

	private void setupPotion(IVariableProvider variables) {
		String keyStr = variables.get("potion").asString();
		Potion potion = ForgeRegistries.POTIONS.getValue(new ResourceLocation(keyStr));
		if (potion == null) return;

		for (IBrewingRecipe iBrewingRecipe : BrewingRecipeRegistry.getRecipes()) {
			if (!(iBrewingRecipe instanceof AdvancedBrewingRecipe brewingRecipe)) continue;

			if (brewingRecipe.output().equals(potion)) {
				this.recipe = brewingRecipe;
				break;
			}
		}
	}

	private void setupItem(IVariableProvider variables) {
		ItemStack item = variables.get("item").as(ItemStack.class, ItemStack.EMPTY);
		if (item.isEmpty()) return;

		for (IBrewingRecipe iBrewingRecipe : BrewingRecipeRegistry.getRecipes()) {
			if (!(iBrewingRecipe instanceof BrewingRecipe brewingRecipe)) continue;

			if (ItemStack.isSameItem(brewingRecipe.getOutput(), item)) {
				this.recipe = brewingRecipe;
				break;
			}
		}
	}

	@Override
	public @Nonnull IVariable process(Level level, String key) {
		if (this.recipe instanceof BrewingRecipe brewingRecipe) {
			return processBrewingRecipe(key, brewingRecipe);
		} else if (this.recipe instanceof AdvancedBrewingRecipe brewingRecipe) {
			return processAdvancedBrewingRecipe(key, brewingRecipe);
		}

		return IVariable.empty();
	}

	private static IVariable processBrewingRecipe(String key, BrewingRecipe brewingRecipe) {
		if (key.startsWith("catalyst"))
			return IVariable.wrapList(BrewingRecipeProcessor.wrapStackList(brewingRecipe.getIngredient().getItems()));
		else if (key.startsWith("input"))
			return IVariable.wrapList(BrewingRecipeProcessor.wrapStackList(brewingRecipe.getInput().getItems()));
		else if (key.startsWith("output"))
			return IVariable.from(brewingRecipe.getOutput());

		return IVariable.empty();
	}

	private static IVariable processAdvancedBrewingRecipe(String key, AdvancedBrewingRecipe brewingRecipe) {
		final ItemStack input = PotionUtils.setPotion(new ItemStack(Items.POTION), brewingRecipe.input());
		final ItemStack output = PotionUtils.setPotion(new ItemStack(Items.POTION), brewingRecipe.output());
		final ItemStack ingredient = new ItemStack(brewingRecipe.ingredient());

		if (key.startsWith("catalyst")) {
			return IVariable.from(ingredient);
		}
		else if (key.startsWith("input")) {
			return IVariable.wrapList(wrapStackList(input));
		} else if (key.startsWith("output")) {
			return IVariable.from(output);
		}

		return IVariable.empty();
	}
}
