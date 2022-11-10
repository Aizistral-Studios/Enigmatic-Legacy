package com.integral.etherium.items;

import com.integral.etherium.core.EtheriumUtil;
import com.integral.etherium.core.IEtheriumConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class EtheriumNugget extends Item {
	private final IEtheriumConfig config;

	public EtheriumNugget(IEtheriumConfig config) {
		super(EtheriumUtil.defaultProperties(config, EtheriumNugget.class).stacksTo(64));
		this.setRegistryName(new ResourceLocation(config.getOwnerMod(), "etherium_nugget"));
		this.config = config;
	}

}

