package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnigmaticAmulet extends ItemBaseCurio {

	public EnigmaticAmulet() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.UNCOMMON).func_234689_a_());
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "enigmatic_amulet"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		String name = ItemNBTHelper.getString(stack, "Inscription", null);

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.func_231173_s_() && this.isVesselEnabled()) {

			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmuletShift1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmuletShift2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmuletShift3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmuletShift4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmuletShift5");

		} else {
			if (this.isVesselEnabled()) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			}

			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet6");

			if (name != null) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmuletInscription", TextFormatting.DARK_RED, name);
			}
		}

	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(String identifier) {

		Multimap<Attribute, AttributeModifier> atts = HashMultimap.create();

		//atts.put(Attributes.field_233826_i_, new AttributeModifier(UUID.fromString("50faf191-bf78-4654-b349-cc1f4f1143bf"), "Armor bonus", 2.0, AttributeModifier.Operation.ADDITION));
		if (ConfigHandler.ENIGMATIC_AMULET_DAMAGE_BONUS.getValue() != 0)
			atts.put(Attributes.field_233823_f_, new AttributeModifier(UUID.fromString("cb7f55d3-685c-4f38-a497-9c13a33db5cf"), "Attack bonus", ConfigHandler.ENIGMATIC_AMULET_DAMAGE_BONUS.getValue(), AttributeModifier.Operation.ADDITION));

		return atts;
	}

	public boolean isVesselEnabled() {
		return ConfigHandler.ENIGMATIC_AMULET_VESSEL_ENABLED.getValue();
	}

	public boolean isVesselOwnerOnly() {
		return ConfigHandler.OWNER_ONLY_VESSEL.getValue();
	}

}
