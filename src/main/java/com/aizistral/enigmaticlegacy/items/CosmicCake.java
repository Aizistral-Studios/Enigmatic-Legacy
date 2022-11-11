package com.aizistral.enigmaticlegacy.items;

import java.util.List;

import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.GenericBlockItem;
import com.aizistral.enigmaticlegacy.registries.EnigmaticBlocks;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class CosmicCake extends GenericBlockItem {

	public CosmicCake() {
		super(EnigmaticBlocks.COSMIC_CAKE, getDefaultProperties().stacksTo(1).rarity(Rarity.EPIC));
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(tooltip, "tooltip.enigmaticlegacy.cosmicCake1");
			ItemLoreHelper.addLocalizedString(tooltip, "tooltip.enigmaticlegacy.cosmicCake2");
		} else {
			ItemLoreHelper.addLocalizedString(tooltip, "tooltip.enigmaticlegacy.holdShift");
		}
	}

}
