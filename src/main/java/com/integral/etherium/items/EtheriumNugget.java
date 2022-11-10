package com.integral.etherium.items;

import com.integral.etherium.core.EtheriumUtil;
import com.integral.etherium.core.IEtheriumConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class EtheriumNugget extends Item {

	public EtheriumNugget() {
		super(EtheriumUtil.defaultProperties(EtheriumNugget.class).stacksTo(64));
	}

}

