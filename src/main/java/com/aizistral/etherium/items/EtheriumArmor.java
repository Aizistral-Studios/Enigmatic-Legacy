package com.aizistral.etherium.items;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.api.items.ICreativeTabMember;
import com.aizistral.enigmaticlegacy.api.materials.EnigmaticArmorMaterials;
import com.aizistral.enigmaticlegacy.config.EtheriumConfigHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.aizistral.enigmaticlegacy.registries.EnigmaticTabs;
import com.aizistral.etherium.core.EtheriumUtil;
import com.aizistral.etherium.core.IEtheriumConfig;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EtheriumArmor extends ArmorItem implements ICreativeTabMember {

	public EtheriumArmor(ArmorItem.Type slot) {
		super(EnigmaticArmorMaterials.ETHERIUM, slot,
				EtheriumUtil.defaultProperties(EtheriumArmor.class).fireResistant());
	}

	@Override
	public CreativeModeTab getCreativeTab() {
		return EnigmaticTabs.MAIN;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return "enigmaticlegacy:textures/models/armor/unseen_armor.png";
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
			if (hasFullSet(player) && player.getHealth() / player.getMaxHealth()
					<= EtheriumConfigHandler.instance().getShieldThreshold(player).asMultiplier(false))
				return true;

		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			EtheriumArmor armor = (EtheriumArmor) stack.getItem();
			if (armor.getType() == Type.HELMET) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumHelmet1");
			} else if (armor.getType() == Type.CHESTPLATE) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumChestplate1");
			} else if (armor.getType() == Type.LEGGINGS) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumLeggings1");
			} else if (armor.getType() == Type.BOOTS) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumBoots1");
			}
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		if (hasFullSet(Minecraft.getInstance().player) || (ItemNBTHelper.verifyExistance(stack, "forceDisplaySetBonus") && ItemNBTHelper.getBoolean(stack, "forceDisplaySetBonus", false))) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumArmorSetBonus1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumArmorSetBonus2", ChatFormatting.GOLD, EtheriumConfigHandler.instance().getShieldThreshold(Minecraft.getInstance().player).asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumArmorSetBonus3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumArmorSetBonus4", ChatFormatting.GOLD, EtheriumConfigHandler.instance().getShieldReduction().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumArmorSetBonus5");

			//ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumArmorSetBonus6");
		}

		if (stack.isEnchanted()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		}

	}

}
