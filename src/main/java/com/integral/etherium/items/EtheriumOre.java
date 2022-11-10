package com.integral.etherium.items;

import com.integral.enigmaticlegacy.config.EtheriumConfigHandler;
import com.integral.etherium.core.EtheriumUtil;
import com.integral.etherium.core.IEtheriumConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class EtheriumOre extends Item {

	public EtheriumOre() {
		super(EtheriumUtil.defaultProperties(EtheriumOre.class).stacksTo(64).fireResistant());
	}

}
