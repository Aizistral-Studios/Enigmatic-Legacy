package com.aizistral.enigmaticlegacy.items.generic;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.aizistral.enigmaticlegacy.api.items.ICreativeTabMember;
import com.aizistral.enigmaticlegacy.registries.EnigmaticTabs;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;

public class GenericBlockItem extends BlockItem implements ICreativeTabMember {
	private final Supplier<CreativeModeTab> tab; // supplier cuz weird shit happens otherwise

	public GenericBlockItem(Block blockIn) {
		this(blockIn, GenericBlockItem.getDefaultProperties());
	}

	public GenericBlockItem(Block blockIn, Properties props) {
		this(blockIn, props, () -> EnigmaticTabs.MAIN);
	}

	public GenericBlockItem(Block blockIn, Properties props, Supplier<@Nullable CreativeModeTab> tab) {
		super(blockIn, props);
		this.tab = tab;
	}

	@Override
	public CreativeModeTab getCreativeTab() {
		return this.tab.get();
	}

	public static Properties getDefaultProperties() {
		Properties props = new Item.Properties();

		props.stacksTo(64);
		props.rarity(Rarity.COMMON);

		return props;
	}

}
