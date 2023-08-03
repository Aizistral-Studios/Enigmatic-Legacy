package com.aizistral.enigmaticlegacy.items;

import javax.annotation.Nonnull;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.items.generic.ItemBaseArmor;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class DarkArmor extends ItemBaseArmor {
	public final String TEXTURE = EnigmaticLegacy.MODID + ":textures/models/armor/dark_armor.png";

	public DarkArmor(ArmorMaterial materialIn, ArmorItem.Type slot) {
		super(materialIn, slot, ItemBaseArmor.getDefaultProperties().rarity(Rarity.RARE).fireResistant());
	}

	@Override
	public CreativeModeTab getCreativeTab() {
		return null;
	}

	/*
	@Override
	@OnlyIn(Dist.CLIENT)
	public HumanoidModel<?> provideArmorModelForSlot(EquipmentSlot slot, HumanoidModel<?> original) {
		DarkArmorModel model = new DarkArmorModel(slot);
		return model;
	}
	 */

	@Nonnull
	@Override
	public final String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return this.TEXTURE;
	}

}
