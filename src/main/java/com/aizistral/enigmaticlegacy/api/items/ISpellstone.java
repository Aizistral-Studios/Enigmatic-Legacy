package com.aizistral.enigmaticlegacy.api.items;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ISpellstone {

	default void triggerActiveAbility(Level world, ServerPlayer player, ItemStack stack) {
		if (SuperpositionHandler.hasSpellstoneCooldown(player))
			return;
	}

}
