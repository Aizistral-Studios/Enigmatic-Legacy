package com.integral.etherium.items;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.etherium.core.EtheriumUtil;
import com.integral.etherium.core.IEtheriumConfig;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EtheriumArmor extends ArmorItem {
	private static IEtheriumConfig config;

	public EtheriumArmor(IEtheriumConfig config, EquipmentSlot slot) {
		super(config.getArmorMaterial(), slot,
				EtheriumUtil.defaultProperties(config, EtheriumArmor.class).fireResistant());
		EtheriumArmor.config = config;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return "enigmaticlegacy:textures/models/armor/unseen_armor.png";
	}

	public static IEtheriumConfig getConfig() {
		return config;
	}

	public static boolean hasFullSet(@Nonnull Player player) {
		if (player == null)
			return false;

		for (ItemStack stack : player.getArmorSlots()) {
			if (!(stack.getItem().getClass() == EtheriumArmor.class))
				return false;
		}

		return true;
	}

	public static boolean hasShield(@Nonnull Player player) {
		if (player != null)
			if (hasFullSet(player) && player.getHealth() / player.getMaxHealth() <= config.getShieldThreshold(player).asMultiplier(false))
				return true;

		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			EtheriumArmor armor = (EtheriumArmor) stack.getItem();
			if (armor.getSlot() == EquipmentSlot.HEAD) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumHelmet1");
			} else if (armor.getSlot() == EquipmentSlot.CHEST) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumChestplate1");
			} else if (armor.getSlot() == EquipmentSlot.LEGS) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumLeggings1");
			} else if (armor.getSlot() == EquipmentSlot.FEET) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumBoots1");
			}
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		if (hasFullSet(Minecraft.getInstance().player) || (ItemNBTHelper.verifyExistance(stack, "forceDisplaySetBonus") && ItemNBTHelper.getBoolean(stack, "forceDisplaySetBonus", false))) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumArmorSetBonus1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumArmorSetBonus2", ChatFormatting.GOLD, config.getShieldThreshold(Minecraft.getInstance().player).asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumArmorSetBonus3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumArmorSetBonus4", ChatFormatting.GOLD, config.getShieldReduction().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumArmorSetBonus5");

			//ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumArmorSetBonus6");
		}

		if (stack.isEnchanted()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		}

	}

}
