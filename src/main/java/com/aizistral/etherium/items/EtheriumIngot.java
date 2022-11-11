package com.aizistral.etherium.items;

import com.aizistral.etherium.core.EtheriumUtil;
import com.aizistral.etherium.core.IEtheriumConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class EtheriumIngot extends Item {

	public EtheriumIngot() {
		super(EtheriumUtil.defaultProperties(EtheriumIngot.class).stacksTo(64).fireResistant());
	}

}
