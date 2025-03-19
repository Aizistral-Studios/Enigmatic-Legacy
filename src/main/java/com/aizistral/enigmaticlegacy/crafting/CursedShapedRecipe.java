package com.aizistral.enigmaticlegacy.crafting;

import com.aizistral.enigmaticlegacy.api.items.ITaintable;
import com.aizistral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.aizistral.enigmaticlegacy.registries.EnigmaticRecipes;
import com.google.gson.JsonObject;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public class CursedShapedRecipe extends ShapedRecipe {
	// TODO We need one custom recipe handler for all our mod stuff

	public CursedShapedRecipe(ResourceLocation id, String group, CraftingBookCategory category, int width, int height, NonNullList<Ingredient> recipeItems, ItemStack result) {
		super(id, group, category, width, height, recipeItems, result);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return EnigmaticRecipes.CURSED_SHAPED;
	}

	@Override
	public boolean matches(CraftingContainer inv, Level worldIn) {
		boolean isAllTainted = true;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack checkedItemStack = inv.getItem(i);

			if (checkedItemStack.getItem() instanceof ITaintable && !((ITaintable)checkedItemStack.getItem()).isTainted(checkedItemStack)) {
				isAllTainted = false;
			}
		}

		return super.matches(inv, worldIn) && isAllTainted;
	}

	@Override
	public ItemStack assemble(CraftingContainer inv, RegistryAccess access) {
		return super.assemble(inv, access);
	}

	public static class Serializer implements RecipeSerializer<CursedShapedRecipe> {

		private NonNullList<Ingredient> handleTainted(NonNullList<Ingredient> ingredientList) {
			for (Ingredient ing : ingredientList) {
				for (ItemStack stack : ing.getItems()) {
					if (stack.getItem() instanceof ITaintable) {
						ItemNBTHelper.setBoolean(stack, "isTainted", true);
					}
				}
			}

			return ingredientList;
		}

		@Override
		public CursedShapedRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			ShapedRecipe recipe = SHAPED_RECIPE.fromJson(recipeId, json);
			return new CursedShapedRecipe(recipe.getId(), recipe.getGroup(), recipe.category(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), this.handleTainted(recipe.getIngredients()), recipe.getResultItem(null));
		}

		@Override
		public CursedShapedRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			ShapedRecipe recipe = SHAPED_RECIPE.fromNetwork(recipeId, buffer);
			return new CursedShapedRecipe(recipe.getId(), recipe.getGroup(), recipe.category(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), this.handleTainted(recipe.getIngredients()), recipe.getResultItem(null));
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, CursedShapedRecipe recipe) {
			SHAPED_RECIPE.toNetwork(buffer, recipe);
		}

	}

}
