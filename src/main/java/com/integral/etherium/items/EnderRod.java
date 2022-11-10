package com.integral.etherium.items;

import com.integral.etherium.core.EtheriumUtil;
import com.integral.etherium.core.IEtheriumConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class EnderRod extends Item {
	private final IEtheriumConfig config;

	public EnderRod(IEtheriumConfig config) {
		super(EtheriumUtil.defaultProperties(config, EnderRod.class).stacksTo(64));
		this.setRegistryName(new ResourceLocation(config.getOwnerMod(), "ender_rod"));
		this.config = config;
	}
}
