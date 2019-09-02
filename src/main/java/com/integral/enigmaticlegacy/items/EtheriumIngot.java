package com.integral.enigmaticlegacy.items;

import net.minecraft.item.Item;
import net.minecraft.item.Rarity;

public class EtheriumIngot extends Item {
	
 public static Properties integratedProperties = new Item.Properties();

 public EtheriumIngot(Properties properties) {
		super(properties);
 }
 
 public static Properties setupIntegratedProperties() {
	 //integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(64);
	 integratedProperties.rarity(Rarity.RARE);
	 
	 return integratedProperties;
 
 }
 
 public static void initConfigValues() {}
 
  
}

