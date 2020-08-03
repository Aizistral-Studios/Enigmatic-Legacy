package com.integral.enigmaticlegacy.api.items;

import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ISpellstone {

	default void triggerActiveAbility(World world, ServerPlayerEntity player, ItemStack stack) {
		if (SuperpositionHandler.hasSpellstoneCooldown(player))
			return;
	}

}
