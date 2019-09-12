package com.integral.enigmaticlegacy.crafting;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
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
	public ItemStack getCraftingResult(CraftingInventory inv) {
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack slotStack = inv.getStackInSlot(i);
			
			if (!slotStack.isEmpty())
				stackList.add(slotStack);
		}
		
		if (stackList.size() == 2)
			if (stackList.get(0).isDamageable() || stackList.get(1).isDamageable())
				if (stackList.get(0).getItem() == EnigmaticLegacy.mendingMixture || stackList.get(1).getItem() == EnigmaticLegacy.mendingMixture) {
					ItemStack tool = stackList.get(0).isDamageable() ? stackList.get(0).copy() : stackList.get(1).copy();
					
					tool.setDamage(0);
					return tool;
				}
					
		return ItemStack.EMPTY;
	}

	@Override
	public boolean matches(CraftingInventory inv, World world) {
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack slotStack = inv.getStackInSlot(i);
			
			if (!slotStack.isEmpty())
				stackList.add(slotStack);
		}
		
		if (stackList.size() == 2)
			if (stackList.get(0).isDamageable() || stackList.get(1).isDamageable())
				if (stackList.get(0).getItem() == EnigmaticLegacy.mendingMixture || stackList.get(1).getItem() == EnigmaticLegacy.mendingMixture)
					return true;
					
		return false;
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
		return EnigmaticRecipeSerializers.CRAFTING_MENDING_MIXTURE_REPAIR;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<MendingMixtureRepairRecipe> {
		@Override
		public MendingMixtureRepairRecipe read(ResourceLocation recipeId, JsonObject json) {
			return new MendingMixtureRepairRecipe(recipeId, "", ItemStack.EMPTY, NonNullList.create());
		}

		@Override
		public MendingMixtureRepairRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			return new MendingMixtureRepairRecipe(recipeId, "", ItemStack.EMPTY, NonNullList.create());
		}

		@Override
		public void write(PacketBuffer buffer, MendingMixtureRepairRecipe recipe) {

		}
	}
}
