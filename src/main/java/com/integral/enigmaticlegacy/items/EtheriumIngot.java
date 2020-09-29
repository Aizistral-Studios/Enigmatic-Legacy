package com.integral.enigmaticlegacy.items;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;

public class EtheriumIngot extends ItemBase {

	public EtheriumIngot() {
		super(ItemBase.getDefaultProperties().rarity(Rarity.RARE).isBurnable());
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "etherium_ingot"));
	}

}
