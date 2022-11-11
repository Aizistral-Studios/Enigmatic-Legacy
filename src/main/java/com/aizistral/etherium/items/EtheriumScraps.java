package com.aizistral.etherium.items;

import com.aizistral.etherium.core.EtheriumUtil;
import com.aizistral.etherium.core.IEtheriumConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class EtheriumScraps extends Item {

	public EtheriumScraps() {
		super(EtheriumUtil.defaultProperties(EtheriumScraps.class).stacksTo(64));
	}

}
