package com.integral.etherium.items;

import com.integral.etherium.core.EtheriumUtil;
import com.integral.etherium.core.IEtheriumConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class EtheriumScraps extends Item {

	public EtheriumScraps() {
		super(EtheriumUtil.defaultProperties(EtheriumScraps.class).stacksTo(64));
	}

}
