package com.integral.enigmaticlegacy.enchantments;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;

import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.ImpalingEnchantment;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class WrathEnchantment extends Enchantment {
	public WrathEnchantment(EquipmentSlotType... slots) {
		super(Enchantment.Rarity.RARE, EnchantmentType.TRIDENT, slots);
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "wrath"));
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 1 + (enchantmentLevel - 1) * 8;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return this.getMinEnchantability(enchantmentLevel) + 20;
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return this.canApply(stack) && super.canApplyAtEnchantingTable(stack);
	}

	@Override
	public boolean canApplyTogether(Enchantment ench) {
		return !(ench instanceof DamageEnchantment) && !(ench instanceof ImpalingEnchantment) && !(ench instanceof TorrentEnchantment) && super.canApplyTogether(ench);
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

	public float bonusDamageByCreature(LivingEntity attacker, LivingEntity living, int level) {
		return 0F;
	}

	@Override
	public float calcDamageByCreature(int level, CreatureAttribute creatureType) {
		return level * 1.25F;
	}
}
