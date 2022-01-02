package com.integral.enigmaticlegacy.enchantments;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;

import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentType;
import net.minecraft.world.item.enchantment.ImpalingEnchantment;
import net.minecraft.world.entity.CreatureAttribute;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class WrathEnchantment extends Enchantment {
	public WrathEnchantment(EquipmentSlotType... slots) {
		super(Enchantment.Rarity.RARE, EnchantmentType.TRIDENT, slots);
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "wrath"));
	}

	@Override
	public int getMinCost(int enchantmentLevel) {
		return 1 + (enchantmentLevel - 1) * 8;
	}

	@Override
	public int getMaxCost(int enchantmentLevel) {
		return this.getMinCost(enchantmentLevel) + 20;
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return this.canEnchant(stack) && super.canApplyAtEnchantingTable(stack);
	}

	@Override
	public boolean checkCompatibility(Enchantment ench) {
		return !(ench instanceof DamageEnchantment) && !(ench instanceof ImpalingEnchantment) && !(ench instanceof TorrentEnchantment) && super.checkCompatibility(ench);
	}

	@Override
	public boolean isAllowedOnBooks() {
		return OmniconfigHandler.isItemEnabled(this);
	}

	@Override
	public boolean isDiscoverable() {
		return OmniconfigHandler.isItemEnabled(this);
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		return OmniconfigHandler.isItemEnabled(this) && stack.canApplyAtEnchantingTable(this);
	}

	public float bonusDamageByCreature(LivingEntity attacker, LivingEntity living, int level) {
		return 0F;
	}

	@Override
	public float getDamageBonus(int level, CreatureAttribute creatureType) {
		return level * 1.25F;
	}
}
