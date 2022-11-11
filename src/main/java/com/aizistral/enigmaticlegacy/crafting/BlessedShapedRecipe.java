package com.aizistral.enigmaticlegacy.crafting;

import com.aizistral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.aizistral.enigmaticlegacy.items.CosmicHeart;
import com.aizistral.enigmaticlegacy.registry.EnigmaticRecipes;
import com.google.gson.JsonObject;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public class BlessedShapedRecipe extends ShapedRecipe {

	public BlessedShapedRecipe(ResourceLocation idIn, String groupIn, int recipeWidthIn, int recipeHeightIn, NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn) {
		super(idIn, groupIn, recipeWidthIn, recipeHeightIn, recipeItemsIn, recipeOutputIn);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return EnigmaticRecipes.BLESSED_SHAPED;
	}

	@Override
	public boolean matches(CraftingContainer inv, Level worldIn) {
		boolean isAllBlessed = true;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack checkedItemStack = inv.getItem(i);

			if (checkedItemStack.getItem() instanceof CosmicHeart && !ItemNBTHelper.getBoolean(checkedItemStack, "isBelieverBlessed", false)) {
				isAllBlessed = false;
			}
		}

		return isAllBlessed && super.matches(inv, worldIn);
	}

	@Override
	public ItemStack assemble(CraftingContainer inv) {
		return super.assemble(inv);
	}

	public static class Serializer implements RecipeSerializer<BlessedShapedRecipe> {

		private NonNullList<Ingredient> handleBlessed(NonNullList<Ingredient> ingredientList) {
			for (Ingredient ing : ingredientList) {
				for (ItemStack stack : ing.getItems()) {
					if (stack.getItem() instanceof CosmicHeart) {
						ItemNBTHelper.setBoolean(stack, "isBelieverBlessed", true);
					}
				}
			}

			return ingredientList;
		}

		@Override
		public BlessedShapedRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			ShapedRecipe recipe = SHAPED_RECIPE.fromJson(recipeId, json);
			return new BlessedShapedRecipe(recipe.getId(), recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), this.handleBlessed(recipe.getIngredients()), recipe.getResultItem());
		}

		@Override
		public BlessedShapedRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			ShapedRecipe recipe = SHAPED_RECIPE.fromNetwork(recipeId, buffer);
			return new BlessedShapedRecipe(recipe.getId(), recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), this.handleBlessed(recipe.getIngredients()), recipe.getResultItem());
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, BlessedShapedRecipe recipe) {
			SHAPED_RECIPE.toNetwork(buffer, recipe);
		}

	}

}