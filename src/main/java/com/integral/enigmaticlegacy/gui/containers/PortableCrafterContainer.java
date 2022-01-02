package com.integral.enigmaticlegacy.gui.containers;

import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.util.IWorldPosCallable;

public class PortableCrafterContainer extends WorkbenchContainer {

    public PortableCrafterContainer(int syncid, PlayerInventory playerInv, IWorldPosCallable posCallable) {
        super(syncid, playerInv, posCallable);
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return true;
    }

}
