package com.aizistral.etherium.items;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.items.ICreativeTabMember;
import com.aizistral.etherium.core.EtheriumUtil;
import com.aizistral.etherium.core.IEtheriumConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class EnderRod extends Item implements ICreativeTabMember {

	public EnderRod() {
		super(EtheriumUtil.defaultProperties(EnderRod.class).stacksTo(64));
	}

	@Override
	public CreativeModeTab getCreativeTab() {
		return EnigmaticLegacy.MAIN_TAB;
	}
}
