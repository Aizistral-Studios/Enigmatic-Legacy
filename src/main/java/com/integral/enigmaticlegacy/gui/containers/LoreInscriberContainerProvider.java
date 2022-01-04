package com.integral.enigmaticlegacy.gui.containers;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.container.Container;
import net.minecraft.world.inventory.container.INamedContainerProvider;
import net.minecraft.network.chat.Component;

public class LoreInscriberContainerProvider implements INamedContainerProvider {
	private Component name;

	public LoreInscriberContainerProvider(Component name) {
		this.name = name;
	}

	@Override
	public Container createMenu(int syncId, Inventory playerInv, Player player) {
		return new LoreInscriberContainer(syncId, playerInv);
	}

	@Override
	public Component getDisplayName() {
		return this.name;
	}

}
