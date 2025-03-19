package com.aizistral.enigmaticlegacy.items;

import java.util.List;

import com.aizistral.enigmaticlegacy.api.items.ICursed;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class SoulDust extends ItemBase implements ICursed {

	public SoulDust() {
		super(getDefaultProperties().rarity(Rarity.RARE).stacksTo(64));
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> list, TooltipFlag flagIn) {
		//		if (Screen.hasShiftDown()) {
		//			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.evilEssence1");
		//			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.evilEssence2");
		//		} else {
		//			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		//		}

		//		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		ItemLoreHelper.indicateCursedOnesOnly(list);
	}

}
