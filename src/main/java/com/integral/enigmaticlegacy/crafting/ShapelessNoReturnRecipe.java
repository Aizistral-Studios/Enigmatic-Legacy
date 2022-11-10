package com.integral.enigmaticlegacy.crafting;

import com.google.gson.JsonObject;

import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;

/**
 * A copy of regular Shapeless Recipe, but any container items
 * are destroyed instead of remaining in a crafting grid.
 * @author Integral
 */

public class ShapelessNoReturnRecipe extends ShapelessRecipe {

	private final String group;
	private final ItemStack recipeOutput;
	private final NonNullList<Ingredient> recipeItems;

	public ShapelessNoReturnRecipe(ResourceLocation id, String group, ItemStack output, NonNullList<Ingredient> inputs) {
		super(id, group, output, inputs);

		this.group = group;
		this.recipeOutput = output;
		this.recipeItems = inputs;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
		return nonnulllist;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return EnigmaticRecipeSerializers.SHAPELESS_NO_RETURN;
	}

	public static class Serialize implements RecipeSerializer<ShapelessNoReturnRecipe> {
		@Override
		public ShapelessNoReturnRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			ShapelessRecipe recipe = SHAPELESS_RECIPE.fromJson(recipeId, json);
			return new ShapelessNoReturnRecipe(recipe.getId(), recipe.getGroup(), recipe.getResultItem(), recipe.getIngredients());
		}

		@Override
		public ShapelessNoReturnRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			ShapelessRecipe recipe = SHAPELESS_RECIPE.fromNetwork(recipeId, buffer);
			return new ShapelessNoReturnRecipe(recipe.getId(), recipe.getGroup(), recipe.getResultItem(), recipe.getIngredients());
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, ShapelessNoReturnRecipe recipe) {
			SHAPELESS_RECIPE.toNetwork(buffer, recipe);
		}

	}
}
