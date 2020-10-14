package com.integral.enigmaticlegacy.items.generic;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.world.World;

public abstract class ItemBase extends Item {

	public ItemBase() {
		this(ItemBase.getDefaultProperties());
	}

	public ItemBase(Properties props) {
		super(props);
	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
		// Insert existential void here
	}

	public static Properties getDefaultProperties() {
		Properties props = new Item.Properties();

		props.group(EnigmaticLegacy.enigmaticTab);
		props.maxStackSize(64);
		props.rarity(Rarity.COMMON);

		return props;
	}

}
