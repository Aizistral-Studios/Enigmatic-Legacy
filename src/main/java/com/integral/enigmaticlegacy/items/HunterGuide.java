package com.integral.enigmaticlegacy.items;

import java.util.List;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class HunterGuide extends ItemBase implements IVanishable {

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

	public HunterGuide() {
		super(getDefaultProperties().stacksTo(1).rarity(Rarity.RARE));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "hunter_guide"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.hunterGuide1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.hunterGuide2", TextFormatting.GOLD, effectiveDistance);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.hunterGuide3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.hunterGuide4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.hunterGuide5", TextFormatting.GOLD, synergyDamageReduction + "%");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

	}

}
