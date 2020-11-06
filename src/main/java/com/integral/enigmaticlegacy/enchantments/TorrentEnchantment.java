package com.integral.enigmaticlegacy.enchantments;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.objects.RegisteredMeleeAttack;

import static com.integral.enigmaticlegacy.objects.RegisteredMeleeAttack.*;

import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.ImpalingEnchantment;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;

public class TorrentEnchantment extends Enchantment {
	public TorrentEnchantment(EquipmentSlotType... slots) {
		super(Enchantment.Rarity.RARE, EnchantmentType.TRIDENT, slots);

		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "torrent"));
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
	public boolean canApplyTogether(Enchantment ench) {
		return !(ench instanceof DamageEnchantment) && !(ench instanceof ImpalingEnchantment) && !(ench instanceof WrathEnchantment);
	}

	public float bonusDamageByCreature(LivingEntity attacker, LivingEntity living, int level) {
		float calculated = (living.isImmuneToFire() || living.isWaterSensitive() || living instanceof EnderDragonEntity) ? level * 2.5F : 0F;
		calculated*= getRegisteredAttackStregth(attacker);

		return calculated;
	}
}

