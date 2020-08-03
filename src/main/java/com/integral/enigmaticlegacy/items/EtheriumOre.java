package com.integral.enigmaticlegacy.items;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;

public class EtheriumOre extends ItemBase {

	public EtheriumOre() {
		super(ItemBase.getDefaultProperties().rarity(Rarity.RARE));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "etherium_ore"));
	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.ETHERIUM_ORE_ENABLED.getValue();
	}

}
