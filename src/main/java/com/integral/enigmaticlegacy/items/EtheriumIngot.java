package com.integral.enigmaticlegacy.items;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.item.Item;
import net.minecraft.item.Rarity;

public class EtheriumIngot extends Item {

	public static Properties integratedProperties = new Item.Properties();

	public EtheriumIngot(Properties properties) {
		super(properties);
	}

	public static Properties setupIntegratedProperties() {
		EtheriumIngot.integratedProperties.group(EnigmaticLegacy.enigmaticTab);
		EtheriumIngot.integratedProperties.maxStackSize(64);
		EtheriumIngot.integratedProperties.rarity(Rarity.RARE);

		return EtheriumIngot.integratedProperties;

	}

}
