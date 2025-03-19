package com.aizistral.enigmaticlegacy.enchantments;

import java.util.ArrayList;
import java.util.List;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.config.OmniconfigHandler;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SorrowCurse extends Enchantment {
	private final List<MobEffect> debuffList = new ArrayList<>();

	public SorrowCurse(EquipmentSlot... slots) {
		super(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR, slots);

		this.debuffList.add(MobEffects.BLINDNESS);
		this.debuffList.add(MobEffects.CONFUSION);
		this.debuffList.add(MobEffects.WEAKNESS);
		this.debuffList.add(MobEffects.MOVEMENT_SLOWDOWN);
		this.debuffList.add(MobEffects.DIG_SLOWDOWN);
		this.debuffList.add(MobEffects.HUNGER);
	}

	public void maybeApplyDebuff(Player player, float damage) {
		if (player.getRandom().nextDouble() < 0.1) {
			float severity = damage > 4 ? damage / 4 : 1;
			severity *= 0.5F + player.getRandom().nextFloat();

			int amplifier = (int) (severity / 2);

			if (amplifier > 3) {
				amplifier = 3;
			}

			MobEffect debuff = this.debuffList.get(player.getRandom().nextInt(this.debuffList.size()));
			MobEffectInstance instance = new MobEffectInstance(debuff, (int) (300 * severity), amplifier, false, true);

			player.addEffect(instance);
		}
	}

	@Override
	public int getMinCost(int enchantmentLevel) {
		return 25;
	}

	@Override
	public int getMaxCost(int enchantmentLevel) {
		return 50;
	}

	@Override
	public int getMinLevel() {
		return 1;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		return OmniconfigHandler.isItemEnabled(this) && super.canEnchant(stack);
	}

	@Override
	public boolean isTreasureOnly() {
		return true;
	}

	@Override
	public boolean isCurse() {
		return true;
	}

	@Override
	public boolean isAllowedOnBooks() {
		return OmniconfigHandler.isItemEnabled(this);
	}

	@Override
	public boolean isDiscoverable() {
		return true;
	}

	@Override
	protected boolean checkCompatibility(Enchantment ench) {
		return super.checkCompatibility(ench);
	}

}
