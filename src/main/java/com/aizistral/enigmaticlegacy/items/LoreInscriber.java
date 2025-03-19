package com.aizistral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.gui.containers.LoreInscriberContainerProvider;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class LoreInscriber extends ItemBase implements Vanishable {

	public LoreInscriber() {
		super(ItemBase.getDefaultProperties().rarity(Rarity.EPIC).stacksTo(1));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.loreInscriber1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.loreInscriber2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.loreInscriber3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.loreInscriber4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.loreInscriber5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.loreInscriber6");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.loreInscriber7");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.loreInscriber8");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.loreInscriber9");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.loreInscriber10");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.loreInscriber11");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		playerIn.startUsingItem(handIn);

		if (!worldIn.isClientSide) {
			Component name = Component.translatable("gui.enigmaticlegacy.lore_inscriber");
			playerIn.openMenu(new LoreInscriberContainerProvider(name));
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}

}
