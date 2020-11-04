package com.integral.enigmaticlegacy.enchantments;

import static com.integral.enigmaticlegacy.objects.RegisteredMeleeAttack.*;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.objects.RegisteredMeleeAttack;

import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;

public class SlayerEnchantment extends Enchantment {
	public SlayerEnchantment(EquipmentSlotType... slots) {
		super(Enchantment.Rarity.COMMON, EnchantmentType.WEAPON, slots);
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "slayer"));
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 5 + (enchantmentLevel - 1) * 8;
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
		return !(ench instanceof DamageEnchantment);
	}

	@Override
	public boolean canApply(ItemStack stack) {
		return stack.getItem() instanceof AxeItem ? true : super.canApply(stack);
	}

	public float bonusDamageByCreature(LivingEntity attacker, LivingEntity living, int level) {
		float calculated = living instanceof MonsterEntity ? level * 1.5F : 0F;
		calculated*= getRegisteredAttackStregth(attacker);
		return calculated;
	}

	@Override
	public void onEntityDamaged(LivingEntity user, Entity target, int level) {
		if (target instanceof MonsterEntity) {
			MonsterEntity monster = (MonsterEntity)target;
			int i = 20 + user.getRNG().nextInt(10 * level);
			monster.addPotionEffect(new EffectInstance(Effects.SLOWNESS, i, 3));
		}
	}
}
