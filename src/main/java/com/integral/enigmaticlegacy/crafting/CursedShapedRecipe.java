package com.integral.enigmaticlegacy.crafting;

import com.google.gson.JsonObject;
import com.integral.enigmaticlegacy.api.items.ITaintable;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class CursedShapedRecipe extends ShapedRecipe {

	// TODO We need one custom recipe handler for all our mod stuff

	public CursedShapedRecipe(ResourceLocation idIn, String groupIn, int recipeWidthIn, int recipeHeightIn, NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn) {
		super(idIn, groupIn, recipeWidthIn, recipeHeightIn, recipeItemsIn, recipeOutputIn);
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return EnigmaticRecipeSerializers.CURSED_SHAPED;
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		boolean isAllTainted = true;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack checkedItemStack = inv.getStackInSlot(i);

			if (checkedItemStack.getItem() instanceof ITaintable && !((ITaintable)checkedItemStack.getItem()).isTainted(checkedItemStack)) {
				isAllTainted = false;
			}
		}

		return super.matches(inv, worldIn) && isAllTainted;
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {
		return super.getCraftingResult(inv);
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>  implements IRecipeSerializer<CursedShapedRecipe> {

		private NonNullList<Ingredient> handleTainted(NonNullList<Ingredient> ingredientList) {
			for (Ingredient ing : ingredientList) {
				for (ItemStack stack : ing.getMatchingStacks()) {
					if (stack.getItem() instanceof ITaintable) {
						ItemNBTHelper.setBoolean(stack, "isTainted", true);
					}
				}
			}

			return ingredientList;
		}

		@Override
		public CursedShapedRecipe read(ResourceLocation recipeId, JsonObject json) {
			ShapedRecipe recipe = CRAFTING_SHAPED.read(recipeId, json);
			return new CursedShapedRecipe(recipe.getId(), recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), this.handleTainted(recipe.getIngredients()), recipe.getRecipeOutput());
		}

		@Override
		public CursedShapedRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			ShapedRecipe recipe = CRAFTING_SHAPED.read(recipeId, buffer);
			return new CursedShapedRecipe(recipe.getId(), recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), this.handleTainted(recipe.getIngredients()), recipe.getRecipeOutput());
		}

		@Override
		public void write(PacketBuffer buffer, CursedShapedRecipe recipe) {
			CRAFTING_SHAPED.write(buffer, recipe);
		}

	}

}
