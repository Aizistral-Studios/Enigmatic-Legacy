package com.integral.etherium.core;

import com.integral.enigmaticlegacy.config.EtheriumConfigHandler;
import com.integral.etherium.blocks.BlockEtherium;
import com.integral.etherium.items.EnderRod;
import com.integral.etherium.items.EtheriumArmor;
import com.integral.etherium.items.EtheriumAxe;
import com.integral.etherium.items.EtheriumIngot;
import com.integral.etherium.items.EtheriumNugget;
import com.integral.etherium.items.EtheriumOre;
import com.integral.etherium.items.EtheriumPickaxe;
import com.integral.etherium.items.EtheriumScraps;
import com.integral.etherium.items.EtheriumScythe;
import com.integral.etherium.items.EtheriumShovel;
import com.integral.etherium.items.EtheriumSword;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Rarity;

public class EtheriumUtil {

	public static Properties defaultProperties(Class<?> itemClass) {
		IEtheriumConfig config = EtheriumConfigHandler.instance();
		Properties props = new Item.Properties();

		props.stacksTo(1);
		props.rarity(Rarity.RARE);

		if (config.isStandalone()) {
			if (isAmong(itemClass, EnderRod.class, EtheriumOre.class, EtheriumIngot.class, EtheriumScraps.class, EtheriumNugget.class)) {
				props.tab(CreativeModeTab.TAB_MATERIALS);
			} else if (isAmong(itemClass, EtheriumAxe.class, EtheriumPickaxe.class, EtheriumScythe.class, EtheriumShovel.class)) {
				props.tab(CreativeModeTab.TAB_TOOLS);
			} else if (isAmong(itemClass, EtheriumSword.class, EtheriumArmor.class)) {
				props.tab(CreativeModeTab.TAB_COMBAT);
			} else if (itemClass == BlockEtherium.class) {
				props.tab(CreativeModeTab.TAB_BUILDING_BLOCKS);
			} else {
				props.tab(CreativeModeTab.TAB_MISC);
			}
		} else {
			props.tab(config.getCreativeTab());
		}

		return props;
	}

	private static boolean isAmong(Class<?> theClass, Class<?>... classList) {
		for (Class<?> someClass : classList) {
			if (theClass == someClass)
				return true;
		}

		return false;
	}

}
