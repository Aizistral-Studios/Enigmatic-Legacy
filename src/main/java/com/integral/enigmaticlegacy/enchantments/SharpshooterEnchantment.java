package com.integral.enigmaticlegacy.enchantments;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.EnchantmentContainer;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class SharpshooterEnchantment extends Enchantment {
	public SharpshooterEnchantment(final EquipmentSlotType... slots) {
		super(Enchantment.Rarity.RARE, EnchantmentType.CROSSBOW, slots);
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "sharpshooter"));
	}

	@Override
	public int getMinLevel() {
		return 1;
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

	@Override
	protected boolean canApplyTogether(final Enchantment ench) {
		return ench != Enchantments.MULTISHOT && ench != Enchantments.PIERCING && super.canApplyTogether(ench);
	}

	@Override
	public boolean canApplyAtEnchantingTable(final ItemStack stack) {
		return this.canApply(stack) && stack.getItem() instanceof CrossbowItem;
	}

	@Override
	public boolean isTreasureEnchantment() {
		return false;
	}

	@Override
	public boolean isCurse() {
		return false;
	}

	@Override
	public boolean isAllowedOnBooks() {
		return OmniconfigHandler.isItemEnabled(this);
	}

	@Override
	public boolean canGenerateInLoot() {
		return OmniconfigHandler.isItemEnabled(this);
	}

	@Override
	public boolean canApply(ItemStack stack) {
		return OmniconfigHandler.isItemEnabled(this) && stack.canApplyAtEnchantingTable(this);
	}

}
