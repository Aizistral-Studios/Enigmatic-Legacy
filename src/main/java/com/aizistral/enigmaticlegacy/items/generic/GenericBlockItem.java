package com.aizistral.enigmaticlegacy.items.generic;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;

public class GenericBlockItem extends BlockItem {

	public GenericBlockItem(Block blockIn) {
		super(blockIn, GenericBlockItem.getDefaultProperties());
	}

	public GenericBlockItem(Block blockIn, Properties props) {
		super(blockIn, props);
	}

	public static Properties getDefaultProperties() {
		Properties props = new Item.Properties();

		props.tab(EnigmaticLegacy.MAIN_TAB);
		props.stacksTo(64);
		props.rarity(Rarity.COMMON);

		return props;
	}

}
