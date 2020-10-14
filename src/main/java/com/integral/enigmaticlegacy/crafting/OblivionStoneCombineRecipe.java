package com.integral.enigmaticlegacy.crafting;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.items.OblivionStone;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * Special recipe type for adding items to list of Keystone of The Oblivion.
 * @author Integral
 */

public class OblivionStoneCombineRecipe extends ShapelessRecipe {
	public OblivionStoneCombineRecipe(ResourceLocation id, String group, ItemStack output, NonNullList<Ingredient> inputs) {
		super(id, group, output, inputs);
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		ItemStack voidStone = null;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack checkedItemStack = inv.getStackInSlot(i);

			if (!checkedItemStack.isEmpty()) {
				if (checkedItemStack.getItem() == EnigmaticLegacy.oblivionStone) {
					if (voidStone == null) {
						voidStone = checkedItemStack;
					} else
						return ItemStack.EMPTY;
				} else {
					stackList.add(checkedItemStack);
				}

			}

		}

		if (voidStone != null && stackList.size() == 1) {
			ItemStack savedStack = stackList.get(0).copy();

			CompoundNBT nbt = voidStone.getOrCreateTag();

			ListNBT arr = nbt.getList("SupersolidID", 8);
			int counter = 0;

			if (arr.size() >= OblivionStone.itemHardcap.getValue())
				return null;

			for (INBT s_uncast : arr) {
				counter++;

				String s = ((StringNBT)s_uncast).getString();

				if (s.equals(ForgeRegistries.ITEMS.getKey(savedStack.getItem()).toString()))
					return ItemStack.EMPTY;
			}

			ListNBT arrCopy = arr.copy();
			CompoundNBT nbtCopy = nbt.copy();

			arrCopy.add(StringNBT.valueOf(ForgeRegistries.ITEMS.getKey(savedStack.getItem()).toString()));

			nbtCopy.put("SupersolidID", arrCopy);

			ItemStack returnedStack = voidStone.copy();
			returnedStack.setTag(nbtCopy);

			return returnedStack;

		} else if (voidStone != null && stackList.size() == 0) {
			ItemStack returnedStack = new ItemStack(EnigmaticLegacy.oblivionStone, 1);
			returnedStack.setTag(voidStone.getOrCreateTag().copy());
			returnedStack.removeChildTag("SupersolidID");
			return returnedStack;
		} else
			return ItemStack.EMPTY;
	}

	@Override
	public boolean matches(CraftingInventory inv, World world) {
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		ItemStack voidStone = null;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack checkedItemStack = inv.getStackInSlot(i);

			if (!checkedItemStack.isEmpty()) {
				if (checkedItemStack.getItem() == EnigmaticLegacy.oblivionStone) {
					if (voidStone == null) {
						voidStone = checkedItemStack;
					} else
						return false;
				} else {
					stackList.add(checkedItemStack);
				}

			}

		}

		if (voidStone != null && stackList.size() == 1) {
			ItemStack savedStack = stackList.get(0).copy();

			CompoundNBT nbt = voidStone.getOrCreateTag();

			ListNBT arr = nbt.getList("SupersolidID", 8);
			int counter = 0;

			if (arr.size() >= OblivionStone.itemHardcap.getValue())
				return false;

			for (INBT s_uncast : arr) {
				counter++;

				String s = ((StringNBT)s_uncast).getString();

				if (s.equals(ForgeRegistries.ITEMS.getKey(savedStack.getItem()).toString()))
					return false;
			}

			return true;

		} else if (voidStone != null && stackList.size() == 0)
			return true;
		else
			return false;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		return nonnulllist;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean isDynamic() {
		return true;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return EnigmaticRecipeSerializers.OBLIVION_STONE_COMBINE;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<OblivionStoneCombineRecipe> {
		@Override
		public OblivionStoneCombineRecipe read(ResourceLocation recipeId, JsonObject json) {
			return new OblivionStoneCombineRecipe(recipeId, "", ItemStack.EMPTY, NonNullList.create());
		}

		@Override
		public OblivionStoneCombineRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			return new OblivionStoneCombineRecipe(recipeId, "", ItemStack.EMPTY, NonNullList.create());
		}

		@Override
		public void write(PacketBuffer buffer, OblivionStoneCombineRecipe recipe) {

		}
	}
}
