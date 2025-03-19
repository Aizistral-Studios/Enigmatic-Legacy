package com.aizistral.enigmaticlegacy.items;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Rarity;

public class PlaceholderItem extends ItemBase {

	public PlaceholderItem(String name, Rarity rarity) {
		this(name, rarity, 1);
	}

	public PlaceholderItem(String name, Rarity rarity, int maxStackSize) {
		super(ItemBase.getDefaultProperties().rarity(rarity).stacksTo(maxStackSize));
	}

	@Override
	public CreativeModeTab getCreativeTab() {
		return null;
	}

}
