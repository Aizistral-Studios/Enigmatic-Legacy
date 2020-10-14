package com.integral.enigmaticlegacy.crafting;

import com.google.gson.JsonObject;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

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
	public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		return nonnulllist;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return EnigmaticRecipeSerializers.SHAPELESS_NO_RETURN;
	}

	public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ShapelessNoReturnRecipe> {
		@Override
		public ShapelessNoReturnRecipe read(ResourceLocation recipeId, JsonObject json) {
			ShapelessRecipe recipe = CRAFTING_SHAPELESS.read(recipeId, json);
			return new ShapelessNoReturnRecipe(recipe.getId(), recipe.getGroup(), recipe.getRecipeOutput(), recipe.getIngredients());
		}

		@Override
		public ShapelessNoReturnRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			ShapelessRecipe recipe = CRAFTING_SHAPELESS.read(recipeId, buffer);
			return new ShapelessNoReturnRecipe(recipe.getId(), recipe.getGroup(), recipe.getRecipeOutput(), recipe.getIngredients());
		}

		@Override
		public void write(PacketBuffer buffer, ShapelessNoReturnRecipe recipe) {
			CRAFTING_SHAPELESS.write(buffer, recipe);
		}

	}
}
