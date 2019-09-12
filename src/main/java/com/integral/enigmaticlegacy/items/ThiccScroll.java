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
	 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(16);
	 integratedProperties.rarity(Rarity.COMMON);
	 
	 return integratedProperties;
 
 }
 
 public static void initConfigValues() {
	 // Insert existential void here
 }
 
  
}

