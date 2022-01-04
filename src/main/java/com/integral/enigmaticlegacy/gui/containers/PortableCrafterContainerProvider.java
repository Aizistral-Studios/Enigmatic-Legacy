package com.integral.enigmaticlegacy.gui.containers;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.container.Container;
import net.minecraft.world.inventory.container.INamedContainerProvider;
import net.minecraft.network.chat.Component;

public class PortableCrafterContainerProvider implements INamedContainerProvider {
    private Component name;

    public PortableCrafterContainerProvider(Component name) {
        this.name = name;
    }

    @Override
    public Container createMenu(int syncId, Inventory playerInv, Player player) {
        return EnigmaticLegacy.PORTABLE_CRAFTER.create(syncId, playerInv);
    }

    @Override
    public Component getDisplayName() {
        return this.name;
    }

}
