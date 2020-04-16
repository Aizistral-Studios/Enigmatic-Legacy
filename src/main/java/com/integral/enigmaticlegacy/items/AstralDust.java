package com.integral.enigmaticlegacy.items;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.item.Item;
import net.minecraft.item.Rarity;

public class AstralDust extends Item {

	public static Properties integratedProperties = new Item.Properties();

	public AstralDust(Properties properties) {
		super(properties);
	}

	public static Properties setupIntegratedProperties() {
		AstralDust.integratedProperties.group(EnigmaticLegacy.enigmaticTab);
		AstralDust.integratedProperties.maxStackSize(64);
		AstralDust.integratedProperties.rarity(Rarity.EPIC);

		return AstralDust.integratedProperties;

	}

}
