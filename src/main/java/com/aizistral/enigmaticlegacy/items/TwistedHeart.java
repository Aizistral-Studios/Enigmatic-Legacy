package com.aizistral.enigmaticlegacy.items;

import java.util.List;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.items.ICursed;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;

public class TwistedHeart extends ItemBase implements ICursed, Vanishable {

	public TwistedHeart() {
		super(ItemBase.getDefaultProperties().rarity(Rarity.EPIC).stacksTo(1));
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		ItemLoreHelper.indicateCursedOnesOnly(tooltip);
	}

}
