package com.integral.enigmaticlegacy.api.items;

import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.world.entity.player.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.World;

public interface ISpellstone {

	default void triggerActiveAbility(World world, ServerPlayer player, ItemStack stack) {
		if (SuperpositionHandler.hasSpellstoneCooldown(player))
			return;
	}

}
