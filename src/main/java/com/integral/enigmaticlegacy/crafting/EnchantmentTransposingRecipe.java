package com.integral.enigmaticlegacy.crafting;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.world.inventory.CraftingInventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.IRecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * Special recipe type for transposing enchantments.
 * @author Integral
 */

public class EnchantmentTransposingRecipe extends ShapelessRecipe {
	public EnchantmentTransposingRecipe(ResourceLocation id, String group, ItemStack output, NonNullList<Ingredient> inputs) {
		super(id, group, output, inputs);
	}

	@Override
	public ItemStack assemble(CraftingInventory inv) {
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		ItemStack transposer = null;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack checkedItemStack = inv.getItem(i);

			if (!checkedItemStack.isEmpty()) {
				if (checkedItemStack.getItem() == EnigmaticLegacy.enchantmentTransposer) {
					if (transposer == null)
						transposer = checkedItemStack.copy();
					else
						return ItemStack.EMPTY;
				} else {
					stackList.add(checkedItemStack);
				}

			}

		}

		if (transposer != null && stackList.size() == 1 && stackList.get(0).isEnchanted()) {
			ItemStack enchanted = stackList.get(0).copy();
			ListNBT enchantmentNBT = enchanted.getEnchantmentTags();
			
			ItemStack returned = new ItemStack(Items.ENCHANTED_BOOK);
			returned.getOrCreateTag().put("StoredEnchantments", enchantmentNBT);
			
			return returned;
		}
					
		return ItemStack.EMPTY;
	}

	@Override
	public boolean matches(CraftingInventory inv, World world) {
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		ItemStack transposer = null;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack checkedItemStack = inv.getItem(i);

			if (!checkedItemStack.isEmpty()) {
				if (checkedItemStack.getItem() == EnigmaticLegacy.enchantmentTransposer) {
					if (transposer == null)
						transposer = checkedItemStack.copy();
					else
						return false;
				} else {
					stackList.add(checkedItemStack);
				}

			}

		}

		if (transposer != null && stackList.size() == 1 && stackList.get(0).isEnchanted())
			return true;
					
		return false;
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);

	      for(int i = 0; i < nonnulllist.size(); ++i) {
	         ItemStack item = inv.getItem(i);
	         if (item.getItem() != EnigmaticLegacy.enchantmentTransposer && item.isEnchanted()) {
	        	ItemStack returned = item.copy();
	        	CompoundNBT nbt = returned.getOrCreateTag();
	        	
	        	nbt.remove("Enchantments");
	        	//returned.setTag(nbt);
	        	
	            nonnulllist.set(i, returned);
	         }
	      }

	      return nonnulllist;
	}
	
	@Override
	public ItemStack getResultItem() {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return EnigmaticRecipeSerializers.ENCHANTMENT_TRANSPOSING;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<EnchantmentTransposingRecipe> {
		@Override
		public EnchantmentTransposingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			return new EnchantmentTransposingRecipe(recipeId, "", ItemStack.EMPTY, NonNullList.create());
		}

		@Override
		public EnchantmentTransposingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
			return new EnchantmentTransposingRecipe(recipeId, "", ItemStack.EMPTY, NonNullList.create());
		}

		@Override
		public void toNetwork(PacketBuffer buffer, EnchantmentTransposingRecipe recipe) {

		}
	}
}
