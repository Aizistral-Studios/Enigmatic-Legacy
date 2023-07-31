package com.aizistral.enigmaticlegacy.api.items;

import com.aizistral.enigmaticlegacy.config.OmniconfigHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.aizistral.enigmaticlegacy.registries.EnigmaticSounds;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IMultiblockMiningTool {

	default boolean areaEffectsEnabled(Player player, ItemStack stack) {
		return this.areaEffectsAllowed(stack) && (!player.isCrouching() || OmniconfigHandler.disableAOEShiftSuppression.getValue());
	}

	default boolean areaEffectsAllowed(ItemStack stack) {
		if (stack.getItem() instanceof IMultiblockMiningTool)
			return ItemNBTHelper.getBoolean(stack, "MultiblockEffectsEnabled", true);

		return false;
	}

	default void enableAreaEffects(Player player, ItemStack stack) {

		if (stack.getItem() instanceof IMultiblockMiningTool) {
			ItemNBTHelper.setBoolean(stack, "MultiblockEffectsEnabled", true);

			if (!player.level().isClientSide) {
				player.level().playSound(null, player.blockPosition(), EnigmaticSounds.CHARGED_ON, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2F)));
			}
		}

	}

	default void disableAreaEffects(Player player, ItemStack stack) {
		if (stack.getItem() instanceof IMultiblockMiningTool) {
			ItemNBTHelper.setBoolean(stack, "MultiblockEffectsEnabled", false);

			if (!player.level().isClientSide) {
				player.level().playSound(null, player.blockPosition(), EnigmaticSounds.CHARGED_OFF, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2F)));
			}
		}
	}

	default void toggleAreaEffects(Player player, ItemStack stack) {
		if (stack.getItem() instanceof IMultiblockMiningTool) {
			if (this.areaEffectsAllowed(stack)) {
				this.disableAreaEffects(player, stack);
			} else {
				this.enableAreaEffects(player, stack);
			}
		}
	}

}
