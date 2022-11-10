package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBasePotion;
import com.integral.enigmaticlegacy.registry.EnigmaticItems;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RedemptionPotion extends ItemBasePotion {

	public RedemptionPotion() {
		super(getDefaultProperties().stacksTo(1).rarity(Rarity.EPIC));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.antiforbiddenPotion1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.antiforbiddenPotion2");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	public void onConsumed(Level worldIn, Player player, ItemStack potion) {
		EnigmaticItems.forbiddenFruit.defineConsumedFruit(player, false);
	}

	@Override
	public boolean canDrink(Level world, Player player, ItemStack potion) {
		return EnigmaticItems.forbiddenFruit.haveConsumedFruit(player);
	}



}