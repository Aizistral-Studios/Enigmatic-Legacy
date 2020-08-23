package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseArmor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EtheriumArmor extends ItemBaseArmor {

	public EtheriumArmor(IArmorMaterial materialIn, EquipmentSlotType slot) {
		super(materialIn, slot, ItemBaseArmor.getDefaultProperties().rarity(Rarity.RARE).func_234689_a_());
	}

	public boolean hasShield(@Nonnull PlayerEntity player) {
		if (player != null)
			if (this.hasFullSet(player) && player.getHealth() / player.getMaxHealth() <= ConfigHandler.ETHERIUM_ARMOR_SHIELD_THRESHOLD.getValue().asMultiplier(false))
				return true;

		return false;
	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.ETHERIUM_ARMOR_ENABLED.getValue();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (Screen.func_231173_s_()) {
			if (stack.getItem().equals(EnigmaticLegacy.etheriumHelmet))
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumHelmet1");
			else if (stack.getItem().equals(EnigmaticLegacy.etheriumChestplate))
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumChestplate1");
			else if (stack.getItem().equals(EnigmaticLegacy.etheriumLeggings))
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumLeggings1");
			else if (stack.getItem().equals(EnigmaticLegacy.etheriumBoots))
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumBoots1");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		if (this.hasFullSet(Minecraft.getInstance().player) || (ItemNBTHelper.verifyExistance(stack, "forceDisplaySetBonus") && ItemNBTHelper.getBoolean(stack, "forceDisplaySetBonus", false))) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumArmorSetBonus1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumArmorSetBonus2", TextFormatting.GOLD, ConfigHandler.ETHERIUM_ARMOR_SHIELD_THRESHOLD.getValue().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumArmorSetBonus3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumArmorSetBonus4", TextFormatting.GOLD, ConfigHandler.ETHERIUM_ARMOR_SHIELD_REDUCTION.getValue().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumArmorSetBonus5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumArmorSetBonus6");
		}

		if (stack.isEnchanted())
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

	}

}
