package com.integral.etherium.items;

import com.integral.etherium.core.EtheriumUtil;
import com.integral.etherium.core.IEtheriumConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class EtheriumOre extends Item {
	private final IEtheriumConfig config;

	public EtheriumOre(IEtheriumConfig config) {
		super(EtheriumUtil.defaultProperties(config, EtheriumOre.class).stacksTo(64).fireResistant());
		this.setRegistryName(new ResourceLocation(config.getOwnerMod(), "etherium_ore"));
		this.config = config;
	}

}
