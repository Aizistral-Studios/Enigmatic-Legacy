package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
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
	public static Omniconfig.DoubleParameter damageBonus;
	public static Omniconfig.BooleanParameter vesselEnabled;
	public static Omniconfig.BooleanParameter ownerOnlyVessel;

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("EnigmaticAmulet");

		damageBonus = builder
				.comment("The damage bonus stat provided by Enigmatic Amulet.")
				.minMax(32768)
				.getDouble("DamageBonus", 1.5);

		vesselEnabled = builder
				.comment("Whether or not Enigmatic Amulet should be summoning Extradimensional Vessel on owner's death.")
				.getBoolean("enigmaticAmuletVesselEnabled", true);

		ownerOnlyVessel = builder
				.comment("If true, only original owner of Extradimensional Vessel will be able to pick it up.")
				.getBoolean("ownerOnlyVessel", false);

		builder.popPrefix();
	}

	public EnigmaticAmulet() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.UNCOMMON).isBurnable());
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "enigmatic_amulet"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		String name = ItemNBTHelper.getString(stack, "Inscription", null);

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown() && this.isVesselEnabled()) {

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

		//atts.put(Attributes.ARMOR, new AttributeModifier(UUID.fromString("50faf191-bf78-4654-b349-cc1f4f1143bf"), "Armor bonus", 2.0, AttributeModifier.Operation.ADDITION));
		if (damageBonus.getValue() != 0) {
			atts.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("cb7f55d3-685c-4f38-a497-9c13a33db5cf"), "Attack bonus", damageBonus.getValue(), AttributeModifier.Operation.ADDITION));
		}

		return atts;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRender(String identifier, int index, LivingEntity living) {
		return false;
	}

	public boolean isVesselEnabled() {
		return vesselEnabled.getValue();
	}

	public boolean isVesselOwnerOnly() {
		return ownerOnlyVessel.getValue();
	}

}
