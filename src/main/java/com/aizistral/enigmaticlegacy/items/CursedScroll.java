package com.aizistral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.aizistral.enigmaticlegacy.api.items.ICursed;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
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
import top.theillusivec4.curios.api.SlotContext;

public class CursedScroll extends ItemBaseCurio implements ICursed {
	public static Omniconfig.PerhapsParameter damageBoost;
	public static Omniconfig.PerhapsParameter miningBoost;
	public static Omniconfig.PerhapsParameter regenBoost;

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("CursedScroll");

		damageBoost = builder
				.comment("Damage increase provided by Scroll of a Thousand Curses for each curse, as percentage.")
				.getPerhaps("DamageBoost", 4);

		miningBoost = builder
				.comment("Mining speed increase provided by Scroll of a Thousand Curses for each curse, as percentage.")
				.getPerhaps("MiningBoost", 7);

		regenBoost = builder
				.comment("Health regeneration increase provided by Scroll of a Thousand Curses for each curse, as percentage.")
				.getPerhaps("RegenBoost", 4);

		builder.popPrefix();
	}

	public CursedScroll() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.EPIC));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursed_scroll1", ChatFormatting.GOLD, damageBoost + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursed_scroll2", ChatFormatting.GOLD, miningBoost + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursed_scroll3", ChatFormatting.GOLD, regenBoost + "%");

			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursed_scroll4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursed_scroll5");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		if (Minecraft.getInstance().player != null)
			if (SuperpositionHandler.getCurioStack(Minecraft.getInstance().player, this) == stack) {
				int curses = SuperpositionHandler.getCurseAmount(Minecraft.getInstance().player);

				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursed_scroll6");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursed_scroll7", ChatFormatting.GOLD, (damageBoost.getValue().asPercentage()*curses) + "%");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursed_scroll8", ChatFormatting.GOLD, (miningBoost.getValue().asPercentage()*curses) + "%");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursed_scroll9", ChatFormatting.GOLD, (regenBoost.getValue().asPercentage()*curses) + "%");

			}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		ItemLoreHelper.indicateCursedOnesOnly(list);
	}

	@Override
	public boolean canEquip(SlotContext context, ItemStack stack) {
		return super.canEquip(context, stack) && context.entity() instanceof Player player && SuperpositionHandler.isTheCursedOne(player);
	}

}