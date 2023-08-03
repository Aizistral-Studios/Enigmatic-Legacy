package com.aizistral.enigmaticlegacy.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.helpers.ItemNBTHelper;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Special recipe type for binding items to player using Gem of Binding.
 * @author Integral
 */

public class BindToPlayerRecipe extends CustomRecipe {
	public static final SimpleCraftingRecipeSerializer<BindToPlayerRecipe> SERIALIZER = new SimpleCraftingRecipeSerializer<>(BindToPlayerRecipe::new);

	public BindToPlayerRecipe(ResourceLocation id, CraftingBookCategory ctg) {
		super(id, ctg);
	}

	@Override
	public ItemStack assemble(CraftingContainer inv, RegistryAccess access) {
		List<ItemStack> stackList = new ArrayList<ItemStack>();

		ItemStack gem = null;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack slotStack = inv.getItem(i);

			if (!slotStack.isEmpty()) {
				if (/*slotStack.getItem() == EnigmaticLegacy.gemOfBinding*/ false) {
					if (gem == null) {
						gem = slotStack;
					} else
						return ItemStack.EMPTY;
				} else {
					stackList.add(slotStack);
				}
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
	public boolean matches(CraftingContainer inv, Level world) {
		List<ItemStack> stackList = new ArrayList<ItemStack>();

		ItemStack gem = null;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack slotStack = inv.getItem(i);

			if (!slotStack.isEmpty()) {
				if (/*slotStack.getItem() == EnigmaticLegacy.gemOfBinding*/ false) {
					if (gem == null) {
						gem = slotStack;
					} else
						return false;
				} else {
					stackList.add(slotStack);
				}
			}
		}

		if (stackList.size() == 1 && stackList.get(0).getItem() instanceof IBound && gem != null && ItemNBTHelper.verifyExistance(gem, "BoundPlayer") && ItemNBTHelper.containsUUID(gem, "BoundUUID"))
			return true;

		return false;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public interface IBound {

		@Nullable
		default Player getBoundPlayer(Level world, ItemStack stack) {
			if (ItemNBTHelper.verifyExistance(stack, "BoundPlayer") && ItemNBTHelper.containsUUID(stack, "BoundUUID")) {
				UUID id = ItemNBTHelper.getUUID(stack, "BoundUUID", null);

				return world.getPlayerByUUID(id);
			}

			return null;
		}
	}

}
