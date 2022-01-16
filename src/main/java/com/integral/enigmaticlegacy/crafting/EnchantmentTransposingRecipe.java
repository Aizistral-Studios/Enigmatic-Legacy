package com.integral.enigmaticlegacy.crafting;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * Special recipe type for transposing enchantments.
 * @author Integral
 */

public class EnchantmentTransposingRecipe extends CustomRecipe {
	static final SimpleRecipeSerializer<EnchantmentTransposingRecipe> SERIALIZER = new SimpleRecipeSerializer<>(EnchantmentTransposingRecipe::new);

	public EnchantmentTransposingRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public ItemStack assemble(CraftingContainer inv) {
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		ItemStack transposer = null;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack checkedItemStack = inv.getItem(i);

			if (!checkedItemStack.isEmpty()) {
				if (checkedItemStack.getItem() == EnigmaticLegacy.enchantmentTransposer) {
					if (transposer == null) {
						transposer = checkedItemStack.copy();
					} else
						return ItemStack.EMPTY;
				} else {
					stackList.add(checkedItemStack);
				}

			}

		}

		if (transposer != null && stackList.size() == 1 && stackList.get(0).isEnchanted()) {
			ItemStack enchanted = stackList.get(0).copy();
			ListTag enchantmentNBT = enchanted.getEnchantmentTags();

			ItemStack returned = new ItemStack(Items.ENCHANTED_BOOK);
			returned.getOrCreateTag().put("StoredEnchantments", enchantmentNBT);

			return returned;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public boolean matches(CraftingContainer inv, Level world) {
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		ItemStack transposer = null;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack checkedItemStack = inv.getItem(i);

			if (!checkedItemStack.isEmpty()) {
				if (checkedItemStack.getItem() == EnigmaticLegacy.enchantmentTransposer) {
					if (transposer == null) {
						transposer = checkedItemStack.copy();
					} else
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
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);

		for(int i = 0; i < nonnulllist.size(); ++i) {
			ItemStack item = inv.getItem(i);
			if (item.getItem() != EnigmaticLegacy.enchantmentTransposer && item.isEnchanted()) {
				ItemStack returned = item.copy();
				CompoundTag nbt = returned.getOrCreateTag();

				nbt.remove("Enchantments");
				//returned.setTag(nbt);

				nonnulllist.set(i, returned);
			}
		}

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
