package com.integral.enigmaticlegacy.items;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.helpers.IPerhaps;

import net.minecraft.item.Item;
import net.minecraft.item.Rarity;

public class EtheriumOre extends Item implements IPerhaps {
	
 public static Properties integratedProperties = new Item.Properties();

 public EtheriumOre(Properties properties) {
		super(properties);
 }
 
 public static Properties setupIntegratedProperties() {
	 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(64);
	 integratedProperties.rarity(Rarity.RARE);
	 
	 return integratedProperties;
 
 }

 @Override
 public boolean isForMortals() {
	return ConfigHandler.ETHERIUM_ORE_ENABLED.getValue();
 }
  
}

