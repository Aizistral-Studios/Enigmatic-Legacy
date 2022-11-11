package com.aizistral.etherium.items;

import com.aizistral.enigmaticlegacy.config.EtheriumConfigHandler;
import com.aizistral.etherium.core.EtheriumUtil;
import com.aizistral.etherium.core.IEtheriumConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class EtheriumOre extends Item {

	public EtheriumOre() {
		super(EtheriumUtil.defaultProperties(EtheriumOre.class).stacksTo(64).fireResistant());
	}

}
