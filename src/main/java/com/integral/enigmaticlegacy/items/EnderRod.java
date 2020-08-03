package com.integral.enigmaticlegacy.items;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.util.ResourceLocation;

public class EnderRod extends ItemBase {

	public EnderRod() {
		super(ItemBase.getDefaultProperties());
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "ender_rod"));
	}

}
