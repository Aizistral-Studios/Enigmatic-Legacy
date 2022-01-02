package com.integral.etherium.items;

import com.integral.etherium.core.EtheriumUtil;
import com.integral.etherium.core.IEtheriumConfig;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.resources.ResourceLocation;

public class EtheriumOre extends Item {
	private final IEtheriumConfig config;

	public EtheriumOre(IEtheriumConfig config) {
		super(EtheriumUtil.defaultProperties(config, EtheriumOre.class).stacksTo(64).fireResistant());
		this.setRegistryName(new ResourceLocation(config.getOwnerMod(), "etherium_ore"));
		this.config = config;
	}

	@Override
	public String getDescriptionId() {
		return this.config.isStandalone() ? "item.enigmaticlegacy." + this.getRegistryName().getPath() : super.getDescriptionId();
	}

}
