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
	 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(64);
	 integratedProperties.rarity(Rarity.COMMON);
	 
	 return integratedProperties;
 
 }
  
}

