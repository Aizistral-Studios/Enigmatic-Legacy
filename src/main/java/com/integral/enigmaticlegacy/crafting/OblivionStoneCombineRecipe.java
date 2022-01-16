package com.integral.enigmaticlegacy.crafting;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.items.OblivionStone;

import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * Special recipe type for adding items to list of Keystone of The Oblivion.
 * @author Integral
 */

public class OblivionStoneCombineRecipe extends CustomRecipe {
	static final SimpleRecipeSerializer<OblivionStoneCombineRecipe> SERIALIZER = new SimpleRecipeSerializer<>(OblivionStoneCombineRecipe::new);

	public OblivionStoneCombineRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public ItemStack assemble(CraftingContainer inv) {
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		ItemStack voidStone = null;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack checkedItemStack = inv.getItem(i);

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

			CompoundTag nbt = voidStone.getOrCreateTag();

			ListTag arr = nbt.getList("SupersolidID", 8);
			int counter = 0;

			if (arr.size() >= OblivionStone.itemHardcap.getValue())
				return null;

			for (Tag s_uncast : arr) {
				counter++;

				String s = ((StringTag)s_uncast).getAsString();

				if (s.equals(ForgeRegistries.ITEMS.getKey(savedStack.getItem()).toString()))
					return ItemStack.EMPTY;
			}

			ListTag arrCopy = arr.copy();
			CompoundTag nbtCopy = nbt.copy();

			arrCopy.add(StringTag.valueOf(ForgeRegistries.ITEMS.getKey(savedStack.getItem()).toString()));

			nbtCopy.put("SupersolidID", arrCopy);

			ItemStack returnedStack = voidStone.copy();
			returnedStack.setTag(nbtCopy);

			return returnedStack;

		} else if (voidStone != null && stackList.size() == 0) {
			ItemStack returnedStack = new ItemStack(EnigmaticLegacy.oblivionStone, 1);
			returnedStack.setTag(voidStone.getOrCreateTag().copy());
			returnedStack.removeTagKey("SupersolidID");
			return returnedStack;
		} else
			return ItemStack.EMPTY;
	}

	@Override
	public boolean matches(CraftingContainer inv, Level world) {
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		ItemStack voidStone = null;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack checkedItemStack = inv.getItem(i);

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

			CompoundTag nbt = voidStone.getOrCreateTag();

			ListTag arr = nbt.getList("SupersolidID", 8);
			int counter = 0;

			if (arr.size() >= OblivionStone.itemHardcap.getValue())
				return false;

			for (Tag s_uncast : arr) {
				counter++;

				String s = ((StringTag)s_uncast).getAsString();

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
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
		return nonnulllist;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

}
