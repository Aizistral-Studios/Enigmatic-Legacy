package com.aizistral.enigmaticlegacy.items;

import java.util.List;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.items.IEldritch;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AbyssalHeart extends ItemBase implements IEldritch {

	public AbyssalHeart() {
		super(ItemBase.getDefaultProperties().rarity(Rarity.EPIC).stacksTo(1));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> list, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.indicateWorthyOnesOnly(list);
		} else {
			//if (SuperpositionHandler.isTheWorthyOne(Minecraft.getInstance().player)) {
			list.add(Component.translatable("tooltip.enigmaticlegacy.abyssalHeart1"));
			list.add(Component.translatable("tooltip.enigmaticlegacy.abyssalHeart2"));
			list.add(Component.translatable("tooltip.enigmaticlegacy.abyssalHeart3"));
			list.add(Component.translatable("tooltip.enigmaticlegacy.abyssalHeart4"));
			//} else {
			//	list.add(Component.translatable("tooltip.enigmaticlegacy.abyssalHeart1_obf"));
			//	list.add(Component.translatable("tooltip.enigmaticlegacy.abyssalHeart2_obf"));
			//	list.add(Component.translatable("tooltip.enigmaticlegacy.abyssalHeart3_obf"));
			//	list.add(Component.translatable("tooltip.enigmaticlegacy.abyssalHeart4_obf"));
			//}

			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.indicateCursedOnesOnly(list);
		}
	}

}
