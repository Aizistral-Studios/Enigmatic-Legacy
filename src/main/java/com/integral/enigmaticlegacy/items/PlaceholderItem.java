package com.integral.enigmaticlegacy.items;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.world.item.Rarity;
import net.minecraft.resources.ResourceLocation;

public class PlaceholderItem extends ItemBase {

	public PlaceholderItem(String name, Rarity rarity) {
		this(name, rarity, 1);
	}

	public PlaceholderItem(String name, Rarity rarity, int maxStackSize) {
		super(ItemBase.getDefaultProperties().tab(null).rarity(rarity).stacksTo(maxStackSize));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, name));
	}

}
