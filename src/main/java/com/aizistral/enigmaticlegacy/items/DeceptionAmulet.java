package com.aizistral.enigmaticlegacy.items;

import java.util.List;

import com.aizistral.enigmaticlegacy.api.items.ICursed;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBase;
import com.aizistral.enigmaticlegacy.items.generic.ItemBaseCurio;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DeceptionAmulet extends ItemBaseCurio implements ICursed {

	public DeceptionAmulet() {
		super(ItemBase.getDefaultProperties().rarity(Rarity.EPIC).stacksTo(1).fireResistant().durability(2));
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> list, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.deceptionAmulet1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.deceptionAmulet2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.deceptionAmulet3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.deceptionAmulet4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.deceptionAmulet5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.deceptionAmulet6");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.deceptionAmulet7");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.deceptionAmulet8");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.deceptionAmulet9");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.deceptionAmulet10");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.deceptionAmulet11");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		ItemLoreHelper.indicateCursedOnesOnly(list);
	}

}
