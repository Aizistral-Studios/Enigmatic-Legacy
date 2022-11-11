package com.aizistral.etherium.items;

import com.aizistral.etherium.core.EtheriumUtil;
import com.aizistral.etherium.core.IEtheriumConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class EnderRod extends Item {

	public EnderRod() {
		super(EtheriumUtil.defaultProperties(EnderRod.class).stacksTo(64));
	}
}
