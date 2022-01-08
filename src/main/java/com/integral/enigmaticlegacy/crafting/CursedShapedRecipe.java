package com.integral.enigmaticlegacy.crafting;

import com.google.gson.JsonObject;
import com.integral.enigmaticlegacy.api.items.ITaintable;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;

import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class CursedShapedRecipe extends ShapedRecipe {

	// TODO We need one custom recipe handler for all our mod stuff

	public CursedShapedRecipe(ResourceLocation idIn, String groupIn, int recipeWidthIn, int recipeHeightIn, NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn) {
		super(idIn, groupIn, recipeWidthIn, recipeHeightIn, recipeItemsIn, recipeOutputIn);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return EnigmaticRecipeSerializers.CURSED_SHAPED;
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
	public ItemStack assemble(CraftingContainer inv) {
		return super.assemble(inv);
	}

	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>>  implements RecipeSerializer<CursedShapedRecipe> {

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
			return new CursedShapedRecipe(recipe.getId(), recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), this.handleTainted(recipe.getIngredients()), recipe.getResultItem());
		}

		@Override
		public CursedShapedRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			ShapedRecipe recipe = SHAPED_RECIPE.fromNetwork(recipeId, buffer);
			return new CursedShapedRecipe(recipe.getId(), recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), this.handleTainted(recipe.getIngredients()), recipe.getResultItem());
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, CursedShapedRecipe recipe) {
			SHAPED_RECIPE.toNetwork(buffer, recipe);
		}

	}

}
