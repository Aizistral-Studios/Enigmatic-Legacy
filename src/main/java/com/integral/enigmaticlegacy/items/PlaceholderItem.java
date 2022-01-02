package com.integral.enigmaticlegacy.items;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;

public class PlaceholderItem extends ItemBase {

	public PlaceholderItem(String name, Rarity rarity) {
		this(name, rarity, 1);
	}

	public PlaceholderItem(String name, Rarity rarity, int maxStackSize) {
		super(ItemBase.getDefaultProperties().rarity(rarity).stacksTo(maxStackSize));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, name));
	}

}
