package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.api.items.ICursed;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.item.enchantment.IVanishable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "cursed_scroll"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursed_scroll1", TextFormatting.GOLD, damageBoost + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursed_scroll2", TextFormatting.GOLD, miningBoost + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursed_scroll3", TextFormatting.GOLD, regenBoost + "%");

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
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursed_scroll7", TextFormatting.GOLD, (damageBoost.getValue().asPercentage()*curses) + "%");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursed_scroll8", TextFormatting.GOLD, (miningBoost.getValue().asPercentage()*curses) + "%");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursed_scroll9", TextFormatting.GOLD, (regenBoost.getValue().asPercentage()*curses) + "%");

			}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		ItemLoreHelper.indicateCursedOnesOnly(list);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRender(String identifier, int index, LivingEntity living, ItemStack stack) {
		return false;
	}

	@Override
	public boolean canEquip(String identifier, LivingEntity living, ItemStack stack) {
		return super.canEquip(identifier, living, stack) && living instanceof PlayerEntity && SuperpositionHandler.isTheCursedOne((PlayerEntity)living);
	}

}