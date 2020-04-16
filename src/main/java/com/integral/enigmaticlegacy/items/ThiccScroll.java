package com.integral.enigmaticlegacy.items;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.item.Item;
import net.minecraft.item.Rarity;

public class ThiccScroll extends Item {

	public static Properties integratedProperties = new Item.Properties();

	public ThiccScroll(Properties properties) {
		super(properties);
	}

	public static Properties setupIntegratedProperties() {
		ThiccScroll.integratedProperties.group(EnigmaticLegacy.enigmaticTab);
		ThiccScroll.integratedProperties.maxStackSize(16);
		ThiccScroll.integratedProperties.rarity(Rarity.COMMON);

		return ThiccScroll.integratedProperties;

	}

}
