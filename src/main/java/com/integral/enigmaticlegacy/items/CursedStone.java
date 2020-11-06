package com.integral.enigmaticlegacy.items;

import java.util.List;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.ICursed;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class CursedStone extends ItemBase implements ICursed {

	public CursedStone() {
		super(getDefaultProperties().rarity(Rarity.EPIC).isBurnable().maxStackSize(1));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "cursed_stone"));
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
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
