package com.integral.enigmaticlegacy.items;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.item.Item;
import net.minecraft.item.Rarity;

public class EnderRod extends Item {

	public static Properties integratedProperties = new Item.Properties();

	public EnderRod(Properties properties) {
		super(properties);
	}

	public static Properties setupIntegratedProperties() {
		EnderRod.integratedProperties.group(EnigmaticLegacy.enigmaticTab);
		EnderRod.integratedProperties.maxStackSize(64);
		EnderRod.integratedProperties.rarity(Rarity.COMMON);

		return EnderRod.integratedProperties;

	}

}
