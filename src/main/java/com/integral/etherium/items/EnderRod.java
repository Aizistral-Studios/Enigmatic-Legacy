package com.integral.etherium.items;

import com.integral.etherium.core.EtheriumUtil;
import com.integral.etherium.core.IEtheriumConfig;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class EnderRod extends Item {
	private final IEtheriumConfig config;

	public EnderRod(IEtheriumConfig config) {
		super(EtheriumUtil.defaultProperties(config, EnderRod.class).stacksTo(64));
		this.setRegistryName(new ResourceLocation(config.getOwnerMod(), "ender_rod"));
		this.config = config;
	}

	@Override
	public String getDescriptionId() {
		return this.config.isStandalone() ? "item.enigmaticlegacy." + this.getRegistryName().getPath() : super.getDescriptionId();
	}
}
