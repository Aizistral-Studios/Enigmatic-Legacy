package com.integral.enigmaticlegacy.items.generic;

import javax.annotation.Nonnull;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.IPerhaps;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;

public abstract class ItemBaseArmor extends ArmorItem implements IPerhaps {

	public ItemBaseArmor(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builder) {
		super(materialIn, slot, builder);
	}

	public ItemBaseArmor(IArmorMaterial materialIn, EquipmentSlotType slot) {
		this(materialIn, slot, ItemBaseArmor.getDefaultProperties());
	}

	@Override
	public boolean isForMortals() {
		return true;
	}

	public boolean hasFullSet(@Nonnull PlayerEntity player) {
		if (player == null)
			return false;

		for (ItemStack stack : player.getArmorInventoryList()) {
			if (!(stack.getItem().getClass() == this.getClass()))
				return false;
		}

		return true;
	}

	public static Properties getDefaultProperties() {
		Properties props = new Item.Properties();

		props.group(EnigmaticLegacy.enigmaticTab);
		props.maxStackSize(1);
		props.rarity(Rarity.COMMON);

		return props;
	}

}
