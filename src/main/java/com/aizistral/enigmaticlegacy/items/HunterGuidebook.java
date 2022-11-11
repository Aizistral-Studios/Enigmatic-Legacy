package com.aizistral.enigmaticlegacy.items;

import java.util.List;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBase;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class HunterGuidebook extends ItemBase implements Vanishable {

	public static Omniconfig.IntParameter effectiveDistance;
	public static Omniconfig.PerhapsParameter synergyDamageReduction;

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("HunterGuide");

		effectiveDistance = builder
				.comment("The range in which Guide to Feral Hunt will redirect damage from pet to it's owner.")
				.getInt("EffectiveDistance", 24);

		synergyDamageReduction = builder
				.comment("The percantage subtracted from damage redirected by Guide to Feral Hunt, if Guide to Animal Companionship is also possessed.")
				.max(100)
				.getPerhaps("SynergyDamageReduction", 50);

		builder.popPrefix();
	}

	public HunterGuidebook() {
		super(getDefaultProperties().stacksTo(1).rarity(Rarity.RARE));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> list, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.hunterGuidebook1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.hunterGuidebook2", ChatFormatting.GOLD, effectiveDistance);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.hunterGuidebook3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.hunterGuidebook4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.hunterGuidebook5", ChatFormatting.GOLD, synergyDamageReduction + "%");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

	}

}
