package com.integral.enigmaticlegacy.gui.containers;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.container.Container;
import net.minecraft.world.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;

public class LoreInscriberContainerProvider implements INamedContainerProvider {
	private ITextComponent name;

	public LoreInscriberContainerProvider(ITextComponent name) {
		this.name = name;
	}

	@Override
	public Container createMenu(int syncId, Inventory playerInv, Player player) {
		return new LoreInscriberContainer(syncId, playerInv);
	}

	@Override
	public ITextComponent getDisplayName() {
		return this.name;
	}

}
