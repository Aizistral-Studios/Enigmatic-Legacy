package com.integral.enigmaticlegacy.items.generic;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;

import net.minecraft.item.Item.Properties;

public class GenericBlockItem extends BlockItem {

	public GenericBlockItem(Block blockIn) {
		super(blockIn, GenericBlockItem.getDefaultProperties());
		this.setRegistryName(blockIn.getRegistryName());
	}

	public GenericBlockItem(Block blockIn, Properties props) {
		super(blockIn, props);
	}

	public static Properties getDefaultProperties() {
		Properties props = new Item.Properties();

		props.tab(EnigmaticLegacy.enigmaticTab);
		props.stacksTo(64);
		props.rarity(Rarity.COMMON);

		return props;
	}

}
