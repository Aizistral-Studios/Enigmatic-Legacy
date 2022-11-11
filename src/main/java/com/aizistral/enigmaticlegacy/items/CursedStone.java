package com.aizistral.enigmaticlegacy.items;

import java.util.List;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.items.ICursed;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class CursedStone extends ItemBase implements ICursed {

	public CursedStone() {
		super(getDefaultProperties().rarity(Rarity.EPIC).fireResistant().stacksTo(1));
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> list, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedStone1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedStone2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedStone3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedStone4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedStone5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedStone6");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		ItemLoreHelper.indicateCursedOnesOnly(list);
	}

}
