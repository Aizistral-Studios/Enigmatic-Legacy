package com.integral.enigmaticlegacy.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

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
		return ModRecipeSerializers.CRAFTING_MENDING_MIXTURE_REPAIR;
	}

	public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ShapelessNoReturnRecipe> {
	      public ShapelessNoReturnRecipe read(ResourceLocation recipeId, JsonObject json) {
	         String s = JSONUtils.getString(json, "group", "");
	         NonNullList<Ingredient> nonnulllist = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
	         if (nonnulllist.isEmpty()) {
	            throw new JsonParseException("No ingredients for shapeless recipe");
	         } else if (nonnulllist.size() > 3 * 3) {
	            throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + (3 * 3));
	         } else {
	            ItemStack itemstack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
	            return new ShapelessNoReturnRecipe(recipeId, s, itemstack, nonnulllist);
	         }
	      }

	      private static NonNullList<Ingredient> readIngredients(JsonArray p_199568_0_) {
	         NonNullList<Ingredient> nonnulllist = NonNullList.create();

	         for(int i = 0; i < p_199568_0_.size(); ++i) {
	            Ingredient ingredient = Ingredient.deserialize(p_199568_0_.get(i));
	            if (!ingredient.hasNoMatchingItems()) {
	               nonnulllist.add(ingredient);
	            }
	         }

	         return nonnulllist;
	      }

	      public ShapelessNoReturnRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
	         String s = buffer.readString(32767);
	         int i = buffer.readVarInt();
	         NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

	         for(int j = 0; j < nonnulllist.size(); ++j) {
	            nonnulllist.set(j, Ingredient.read(buffer));
	         }

	         ItemStack itemstack = buffer.readItemStack();
	         return new ShapelessNoReturnRecipe(recipeId, s, itemstack, nonnulllist);
	      }
	      
	      @Override
	      public void write(PacketBuffer buffer, ShapelessNoReturnRecipe recipe) {
	         buffer.writeString(recipe.group);
	         buffer.writeVarInt(recipe.recipeItems.size());

	         for(Ingredient ingredient : recipe.recipeItems) {
	            ingredient.write(buffer);
	         }

	         buffer.writeItemStack(recipe.recipeOutput);
	      }
	      
	   }
}
