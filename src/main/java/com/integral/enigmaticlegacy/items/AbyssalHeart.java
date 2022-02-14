package com.integral.enigmaticlegacy.items;

import java.util.List;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.IBlessable;
import com.integral.enigmaticlegacy.api.items.ICursed;
import com.integral.enigmaticlegacy.api.items.IEldritch;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AbyssalHeart extends ItemBase implements IEldritch {

	public AbyssalHeart() {
		super(ItemBase.getDefaultProperties().rarity(Rarity.EPIC).stacksTo(1));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "abyssal_heart"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> list, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.indicateWorthyOnesOnly(list);
		} else {
			//if (SuperpositionHandler.isTheWorthyOne(Minecraft.getInstance().player)) {
			list.add(new TranslatableComponent("tooltip.enigmaticlegacy.abyssalHeart1"));
			list.add(new TranslatableComponent("tooltip.enigmaticlegacy.abyssalHeart2"));
			list.add(new TranslatableComponent("tooltip.enigmaticlegacy.abyssalHeart3"));
			list.add(new TranslatableComponent("tooltip.enigmaticlegacy.abyssalHeart4"));
			//} else {
			//	list.add(new TranslatableComponent("tooltip.enigmaticlegacy.abyssalHeart1_obf"));
			//	list.add(new TranslatableComponent("tooltip.enigmaticlegacy.abyssalHeart2_obf"));
			//	list.add(new TranslatableComponent("tooltip.enigmaticlegacy.abyssalHeart3_obf"));
			//	list.add(new TranslatableComponent("tooltip.enigmaticlegacy.abyssalHeart4_obf"));
			//}

			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.indicateCursedOnesOnly(list);
		}
	}

}
