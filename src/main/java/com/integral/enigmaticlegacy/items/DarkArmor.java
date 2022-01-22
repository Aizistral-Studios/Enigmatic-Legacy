package com.integral.enigmaticlegacy.items;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.items.generic.ItemBaseArmor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;

public class DarkArmor extends ItemBaseArmor {
	public final String TEXTURE = EnigmaticLegacy.MODID + ":textures/models/armor/dark_armor.png";

	public DarkArmor(ArmorMaterial materialIn, EquipmentSlot slot) {
		super(materialIn, slot, ItemBaseArmor.getDefaultProperties().rarity(Rarity.RARE).fireResistant().tab(null));
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
