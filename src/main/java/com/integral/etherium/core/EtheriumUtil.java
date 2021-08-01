package com.integral.etherium.core;

import com.integral.etherium.items.EnderRod;
import com.integral.etherium.items.EtheriumArmor;
import com.integral.etherium.items.EtheriumAxe;
import com.integral.etherium.items.EtheriumIngot;
import com.integral.etherium.items.EtheriumOre;
import com.integral.etherium.items.EtheriumPickaxe;
import com.integral.etherium.items.EtheriumScythe;
import com.integral.etherium.items.EtheriumShovel;
import com.integral.etherium.items.EtheriumSword;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraft.item.Item.Properties;

public class EtheriumUtil {

	public static Properties defaultProperties(IEtheriumConfig config, Class<?> itemClass) {
		Properties props = new Item.Properties();

		props.maxStackSize(1);
		props.rarity(Rarity.RARE);

		if (config.isStandalone()) {
			if (isAmong(itemClass, EnderRod.class, EtheriumOre.class, EtheriumIngot.class)) {
				props.group(ItemGroup.MATERIALS);
			} else if (isAmong(itemClass, EtheriumAxe.class, EtheriumPickaxe.class, EtheriumScythe.class, EtheriumShovel.class)) {
				props.group(ItemGroup.TOOLS);
			} else if (isAmong(itemClass, EtheriumSword.class, EtheriumArmor.class)) {
				props.group(ItemGroup.COMBAT);
			} else {
				props.group(ItemGroup.MISC);
			}
		} else {
			props.group(config.getCreativeTab());
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
