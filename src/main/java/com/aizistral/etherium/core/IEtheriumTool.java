package com.aizistral.etherium.core;

import com.aizistral.enigmaticlegacy.config.EtheriumConfigHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemNBTHelper;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IEtheriumTool {

	default IEtheriumConfig getConfig() {
		return EtheriumConfigHandler.instance();
	}

	default boolean areaEffectsEnabled(Player player, ItemStack stack) {
		return this.areaEffectsAllowed(stack) && (!player.isCrouching() || this.getConfig().disableAOEShiftInhibition());
	}

	default boolean areaEffectsAllowed(ItemStack stack) {
		if (stack.getItem() instanceof IEtheriumTool)
			return ItemNBTHelper.getBoolean(stack, "MultiblockEffectsEnabled", true);

		return false;
	}

	default void enableAreaEffects(Player player, ItemStack stack) {

		if (stack.getItem() instanceof IEtheriumTool) {
			ItemNBTHelper.setBoolean(stack, "MultiblockEffectsEnabled", true);

			if (!player.level().isClientSide) {
				player.level().playSound(null, player.blockPosition(), this.getConfig().getAOESoundOn(), SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2F)));
			}
		}

	}

	default void disableAreaEffects(Player player, ItemStack stack) {
		if (stack.getItem() instanceof IEtheriumTool) {
			ItemNBTHelper.setBoolean(stack, "MultiblockEffectsEnabled", false);

			if (!player.level().isClientSide) {
				player.level().playSound(null, player.blockPosition(), this.getConfig().getAOESoundOff(), SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2F)));
			}
		}
	}

	default void toggleAreaEffects(Player player, ItemStack stack) {
		if (stack.getItem() instanceof IEtheriumTool) {
			if (this.areaEffectsAllowed(stack)) {
				this.disableAreaEffects(player, stack);
			} else {
				this.enableAreaEffects(player, stack);
			}
		}
	}

}

