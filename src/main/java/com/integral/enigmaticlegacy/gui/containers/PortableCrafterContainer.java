package com.integral.enigmaticlegacy.gui.containers;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;

public class PortableCrafterContainer extends CraftingMenu {

	public PortableCrafterContainer(int syncid, Inventory playerInv, ContainerLevelAccess posCallable) {
		super(syncid, playerInv, posCallable);
	}

	@Override
	public boolean stillValid(Player playerIn) {
		return true;
	}

}
