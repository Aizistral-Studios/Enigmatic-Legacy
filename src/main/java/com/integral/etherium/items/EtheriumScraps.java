package com.integral.etherium.items;

import com.integral.etherium.core.EtheriumUtil;
import com.integral.etherium.core.IEtheriumConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class EtheriumScraps extends Item {
	private final IEtheriumConfig config;

	public EtheriumScraps(IEtheriumConfig config) {
		super(EtheriumUtil.defaultProperties(config, EtheriumScraps.class).stacksTo(64));
		this.setRegistryName(new ResourceLocation(config.getOwnerMod(), "etherium_scraps"));
		this.config = config;
	}

	@Override
	public String getDescriptionId() {
		return this.config.isStandalone() ? "item.enigmaticlegacy." + this.getRegistryName().getPath() : super.getDescriptionId();
	}

}
