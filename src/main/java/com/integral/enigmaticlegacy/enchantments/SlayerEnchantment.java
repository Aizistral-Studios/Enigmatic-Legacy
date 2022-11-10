package com.integral.enigmaticlegacy.enchantments;

import static com.integral.enigmaticlegacy.objects.RegisteredMeleeAttack.getRegisteredAttackStregth;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SlayerEnchantment extends Enchantment {

	public SlayerEnchantment(EquipmentSlot... slots) {
		super(Enchantment.Rarity.COMMON, EnchantmentCategory.WEAPON, slots);
	}

	@Override
	public int getMinCost(int enchantmentLevel) {
		return 5 + (enchantmentLevel - 1) * 8;
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
		return !(ench instanceof DamageEnchantment) && super.checkCompatibility(ench);
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		return OmniconfigHandler.isItemEnabled(this) && (stack.getItem() instanceof AxeItem ? true : stack.canApplyAtEnchantingTable(this));
	}

	@Override
	public boolean isAllowedOnBooks() {
		return OmniconfigHandler.isItemEnabled(this);
	}

	@Override
	public boolean isDiscoverable() {
		return OmniconfigHandler.isItemEnabled(this);
	}

	public float bonusDamageByCreature(LivingEntity attacker, LivingEntity living, int level) {
		float calculated = living instanceof Monster ? level * 1.5F : 0F;
		calculated*= getRegisteredAttackStregth(attacker);
		return calculated;
	}
}
