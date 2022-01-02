package com.integral.enigmaticlegacy.gui.containers;

import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;

public class LoreInscriberContainerProvider implements INamedContainerProvider {
	private ITextComponent name;

	public LoreInscriberContainerProvider(ITextComponent name) {
		this.name = name;
	}

	@Override
	public Container createMenu(int syncId, PlayerInventory playerInv, PlayerEntity player) {
		return new LoreInscriberContainer(syncId, playerInv);
	}

	@Override
	public ITextComponent getDisplayName() {
		return this.name;
	}

}
