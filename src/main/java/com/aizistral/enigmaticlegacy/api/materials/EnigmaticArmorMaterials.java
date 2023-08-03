package com.aizistral.enigmaticlegacy.api.materials;

import java.util.Objects;
import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.etherium.core.IEtheriumConfig;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public enum EnigmaticArmorMaterials implements ArmorMaterial {
	ETHERIUM(EnigmaticLegacy.MODID + ":etherium", 132, new int[] { 4, 7, 9, 4 }, 24,
			SoundEvents.ARMOR_EQUIP_IRON, 4F, 0, () -> getEtheriumConfig().getRepairMaterial());

	private static final int[] MAX_DAMAGE_ARRAY = new int[] { 13, 15, 16, 11 };
	private static IEtheriumConfig etheriumConfig;

	private final String name;
	private final int maxDamageFactor;
	private final int[] damageReductionAmountArray;
	private final int enchantability;
	private final SoundEvent soundEvent;
	private final float toughness;
	private final Supplier<Ingredient> repairMaterial;
	private final float knockbackResistance;

	private EnigmaticArmorMaterials(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantability, SoundEvent soundEvent, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterial) {
		this.name = name;
		this.maxDamageFactor = maxDamageFactor;
		this.damageReductionAmountArray = damageReductionAmountArray;
		this.enchantability = enchantability;
		this.soundEvent = soundEvent;
		this.toughness = toughness;
		this.repairMaterial = repairMaterial;
		this.knockbackResistance = knockbackResistance;
	}

	@Override
	public int getDurabilityForType(ArmorItem.Type slot) {
		int durability = MAX_DAMAGE_ARRAY[slot.ordinal()] * this.maxDamageFactor;
		return durability;
	}

	@Override
	public int getDefenseForType(ArmorItem.Type slot) {
		return this.damageReductionAmountArray[slot.ordinal()];
	}

	@Override
	public int getEnchantmentValue() {
		return this.enchantability;
	}

	@Override
	public SoundEvent getEquipSound() {
		return this.soundEvent;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return this.repairMaterial.get();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public float getToughness() {
		return this.toughness;
	}

	@Override
	public float getKnockbackResistance() {
		return this.knockbackResistance;
	}

	public static IEtheriumConfig getEtheriumConfig() {
		return etheriumConfig;
	}

	public static void setEtheriumConfig(IEtheriumConfig config) {
		etheriumConfig = Objects.requireNonNull(config);
	}

}
