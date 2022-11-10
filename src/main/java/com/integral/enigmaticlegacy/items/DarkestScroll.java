package com.integral.enigmaticlegacy.items;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;

public class DarkestScroll extends ItemBase {

	public DarkestScroll() {
		super(ItemBase.getDefaultProperties().rarity(Rarity.UNCOMMON).stacksTo(1));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "darkest_scroll"));
	}

}
