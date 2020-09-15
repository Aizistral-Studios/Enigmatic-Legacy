package com.integral.enigmaticlegacy.gui.containers;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;

public class EnigmaticRepairContainerProvider implements INamedContainerProvider {
    private ITextComponent name;

    public EnigmaticRepairContainerProvider(ITextComponent name) {
        this.name = name;
    }

    @Override
    public Container createMenu(int syncId, PlayerInventory playerInv, PlayerEntity player) {
        return EnigmaticLegacy.LORE_INSCRIBER_CONTAINER.create(syncId, playerInv);
    }

    @Override
    public ITextComponent getDisplayName() {
        return this.name;
    }

}
