package com.integral.enigmaticlegacy.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingInventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.IRecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * Special recipe type for binding items to player using Gem of Binding.
 * @author Integral
 */

public class BindToPlayerRecipe extends ShapelessRecipe {
	public BindToPlayerRecipe(ResourceLocation id, String group, ItemStack output, NonNullList<Ingredient> inputs) {
		super(id, group, output, inputs);
	}

	@Override
	public ItemStack assemble(CraftingInventory inv) {
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		
		ItemStack gem = null;
		
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack slotStack = inv.getItem(i);
			
			if (!slotStack.isEmpty()) {
				if (slotStack.getItem() == EnigmaticLegacy.gemOfBinding) {
					if (gem == null)
						gem = slotStack;
					else
						return ItemStack.EMPTY;
				} else
					stackList.add(slotStack);
			}
		}
		
		if (stackList.size() == 1 && stackList.get(0).getItem() instanceof IBound && gem != null && ItemNBTHelper.verifyExistance(gem, "BoundPlayer") && ItemNBTHelper.containsUUID(gem, "BoundUUID")) {
			ItemStack returned = stackList.get(0).copy();
			ItemNBTHelper.setString(returned, "BoundPlayer", ItemNBTHelper.getString(gem, "BoundPlayer", "Herobrine"));
			ItemNBTHelper.setUUID(returned, "BoundUUID", ItemNBTHelper.getUUID(gem, "BoundUUID", null));
			
			return returned;
		}
					
		return ItemStack.EMPTY;
	}

	@Override
	public boolean matches(CraftingInventory inv, World world) {
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		
		ItemStack gem = null;
		
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack slotStack = inv.getItem(i);
			
			if (!slotStack.isEmpty()) {
				if (slotStack.getItem() == EnigmaticLegacy.gemOfBinding) {
					if (gem == null)
						gem = slotStack;
					else
						return false;
				} else
					stackList.add(slotStack);
			}
		}
		
		if (stackList.size() == 1 && stackList.get(0).getItem() instanceof IBound && gem != null && ItemNBTHelper.verifyExistance(gem, "BoundPlayer") && ItemNBTHelper.containsUUID(gem, "BoundUUID"))
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
		return EnigmaticRecipeSerializers.BIND_TO_PLAYER;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BindToPlayerRecipe> {
		@Override
		public BindToPlayerRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			return new BindToPlayerRecipe(recipeId, "", ItemStack.EMPTY, NonNullList.create());
		}

		@Override
		public BindToPlayerRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
			return new BindToPlayerRecipe(recipeId, "", ItemStack.EMPTY, NonNullList.create());
		}

		@Override
		public void toNetwork(PacketBuffer buffer, BindToPlayerRecipe recipe) {

		}
	}
	
	public interface IBound {
		
		@Nullable
		default Player getBoundPlayer(World world, ItemStack stack) {
			if (ItemNBTHelper.verifyExistance(stack, "BoundPlayer") && ItemNBTHelper.containsUUID(stack, "BoundUUID")) {
				 UUID id = ItemNBTHelper.getUUID(stack, "BoundUUID", null);
				 
				 return world.getPlayerByUUID(id);
			}
			
			return null;
		}
	}

}
