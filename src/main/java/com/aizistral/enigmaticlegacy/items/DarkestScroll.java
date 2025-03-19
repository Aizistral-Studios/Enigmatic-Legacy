package com.aizistral.enigmaticlegacy.items;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;

public class DarkestScroll extends ItemBase {

	public DarkestScroll() {
		super(ItemBase.getDefaultProperties().rarity(Rarity.UNCOMMON).stacksTo(1));
	}

}
