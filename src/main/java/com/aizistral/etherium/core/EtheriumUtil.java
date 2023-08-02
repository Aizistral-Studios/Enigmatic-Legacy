package com.aizistral.etherium.core;

import com.aizistral.enigmaticlegacy.config.EtheriumConfigHandler;
import com.aizistral.etherium.blocks.BlockEtherium;
import com.aizistral.etherium.items.EnderRod;
import com.aizistral.etherium.items.EtheriumArmor;
import com.aizistral.etherium.items.EtheriumAxe;
import com.aizistral.etherium.items.EtheriumIngot;
import com.aizistral.etherium.items.EtheriumNugget;
import com.aizistral.etherium.items.EtheriumOre;
import com.aizistral.etherium.items.EtheriumPickaxe;
import com.aizistral.etherium.items.EtheriumScraps;
import com.aizistral.etherium.items.EtheriumScythe;
import com.aizistral.etherium.items.EtheriumShovel;
import com.aizistral.etherium.items.EtheriumSword;

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
