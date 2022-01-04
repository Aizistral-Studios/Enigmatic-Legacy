package com.integral.enigmaticlegacy.crafting;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.world.inventory.CraftingInventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.IRecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * Special recipe type for repairing of damageable items with Mending Mixture.
 * @author Integral
 */

public class MendingMixtureRepairRecipe extends ShapelessRecipe {
	public MendingMixtureRepairRecipe(ResourceLocation id, String group, ItemStack output, NonNullList<Ingredient> inputs) {
		super(id, group, output, inputs);
	}

	@Override
	public ItemStack assemble(CraftingInventory inv) {
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack slotStack = inv.getItem(i);
			
			if (!slotStack.isEmpty())
				stackList.add(slotStack);
		}
		
		if (stackList.size() == 2)
			if (stackList.get(0).isDamageableItem() || stackList.get(1).isDamageableItem())
				if (stackList.get(0).getItem() == EnigmaticLegacy.mendingMixture || stackList.get(1).getItem() == EnigmaticLegacy.mendingMixture) {
					ItemStack tool = stackList.get(0).isDamageableItem() ? stackList.get(0).copy() : stackList.get(1).copy();
					
					tool.setDamageValue(0);
					return tool;
				}
					
		return ItemStack.EMPTY;
	}

	@Override
	public boolean matches(CraftingInventory inv, Level world) {
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack slotStack = inv.getItem(i);
			
			if (!slotStack.isEmpty())
				stackList.add(slotStack);
		}
		
		if (stackList.size() == 2)
			if (stackList.get(0).isDamageableItem() || stackList.get(1).isDamageableItem())
				if (stackList.get(0).getItem() == EnigmaticLegacy.mendingMixture || stackList.get(1).getItem() == EnigmaticLegacy.mendingMixture)
					return true;
					
		return false;
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
		return EnigmaticRecipeSerializers.CRAFTING_MENDING_MIXTURE_REPAIR;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<MendingMixtureRepairRecipe> {
		@Override
		public MendingMixtureRepairRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			return new MendingMixtureRepairRecipe(recipeId, "", ItemStack.EMPTY, NonNullList.create());
		}

		@Override
		public MendingMixtureRepairRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			return new MendingMixtureRepairRecipe(recipeId, "", ItemStack.EMPTY, NonNullList.create());
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, MendingMixtureRepairRecipe recipe) {

		}
	}
}
