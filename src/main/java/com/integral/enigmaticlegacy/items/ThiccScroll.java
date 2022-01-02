package com.integral.enigmaticlegacy.items;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.util.ResourceLocation;

public class ThiccScroll extends ItemBase {

	public ThiccScroll() {
		super(ItemBase.getDefaultProperties().stacksTo(16));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "thicc_scroll"));
	}

}
