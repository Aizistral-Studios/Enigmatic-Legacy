package com.aizistral.enigmaticlegacy.items;

import java.awt.TextComponent;
import java.util.List;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.items.IHidden;
import com.aizistral.enigmaticlegacy.client.Quote;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class QuotePlayer extends ItemBase implements IHidden {

	public QuotePlayer() {
		super(getDefaultProperties().rarity(Rarity.EPIC).stacksTo(1).fireResistant());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.quotePlayer1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.quotePlayer2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.quotePlayer3");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.quotePlayerSelection", ChatFormatting.GOLD,
				Quote.getAllQuotes().get(stack.getDamageValue()).getName().toUpperCase());
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);

		if (player.isCrouching()) {
			player.startUsingItem(hand);

			if (itemstack.getDamageValue() < Quote.getAllQuotes().size() - 1) {
				itemstack.setDamageValue(itemstack.getDamageValue() + 1);
			} else {
				itemstack.setDamageValue(0);
			}

			player.swing(hand);

			if (player instanceof ServerPlayer) {
				player.displayClientMessage(Component.literal("Quote: " + Quote.getAllQuotes().get(itemstack.getDamageValue()).getName().toUpperCase()), true);
			}
		} else {
			if (player instanceof ServerPlayer splayer) {
				Quote.getAllQuotes().get(itemstack.getDamageValue()).play(splayer, 10);
			}
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		return this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
	}

}
