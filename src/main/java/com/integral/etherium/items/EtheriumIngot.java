package com.integral.etherium.items;

import com.integral.etherium.core.EtheriumUtil;
import com.integral.etherium.core.IEtheriumConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class EtheriumIngot extends Item {
	private final IEtheriumConfig config;

	public EtheriumIngot(IEtheriumConfig config) {
		super(EtheriumUtil.defaultProperties(config, EtheriumIngot.class).stacksTo(64).fireResistant());
		this.setRegistryName(new ResourceLocation(config.getOwnerMod(), "etherium_ingot"));
		this.config = config;
	}

}
