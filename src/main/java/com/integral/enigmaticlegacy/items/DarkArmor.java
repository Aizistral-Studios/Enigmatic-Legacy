package com.integral.enigmaticlegacy.items;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.client.models.DarkArmorModel;
import com.integral.enigmaticlegacy.items.generic.ItemBaseArmor;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DarkArmor extends ItemBaseArmor {

	public final String TEXTURE = EnigmaticLegacy.MODID + ":textures/models/armor/dark_armor.png";

	public DarkArmor(IArmorMaterial materialIn, EquipmentSlotType slot) {
		super(materialIn, slot, ItemBaseArmor.getDefaultProperties().rarity(Rarity.RARE).func_234689_a_().group(null));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	@Nullable
	public BipedModel<?> provideArmorModelForSlot(EquipmentSlotType slot) {
		DarkArmorModel model = new DarkArmorModel(slot);
		return model;
	}

	@Nonnull
	@Override
	public final String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		return this.TEXTURE;
	}

}
