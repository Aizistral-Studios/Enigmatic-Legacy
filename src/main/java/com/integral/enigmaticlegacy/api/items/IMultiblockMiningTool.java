package com.integral.enigmaticlegacy.api.items;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

public interface IMultiblockMiningTool {

	default boolean areaEffectsEnabled(PlayerEntity player, ItemStack stack) {
		return this.areaEffectsAllowed(stack) && (!player.isCrouching() || OmniconfigHandler.disableAOEShiftSuppression.getValue());
	}

	default boolean areaEffectsAllowed(ItemStack stack) {
		if (stack.getItem() instanceof IMultiblockMiningTool)
			return ItemNBTHelper.getBoolean(stack, "MultiblockEffectsEnabled", true);

		return false;
	}

	default void enableAreaEffects(PlayerEntity player, ItemStack stack) {

		if (stack.getItem() instanceof IMultiblockMiningTool) {
			ItemNBTHelper.setBoolean(stack, "MultiblockEffectsEnabled", true);

			if (!player.world.isRemote) {
				player.world.playSound(null, player.getPosition(), EnigmaticLegacy.HHON, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2F)));
			}
		}

	}

	default void disableAreaEffects(PlayerEntity player, ItemStack stack) {
		if (stack.getItem() instanceof IMultiblockMiningTool) {
			ItemNBTHelper.setBoolean(stack, "MultiblockEffectsEnabled", false);

			if (!player.world.isRemote) {
				player.world.playSound(null, player.getPosition(), EnigmaticLegacy.HHOFF, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2F)));
			}
		}
	}

	default void toggleAreaEffects(PlayerEntity player, ItemStack stack) {
		if (stack.getItem() instanceof IMultiblockMiningTool) {
			if (this.areaEffectsAllowed(stack)) {
				this.disableAreaEffects(player, stack);
			} else {
				this.enableAreaEffects(player, stack);
			}
		}
	}

}
