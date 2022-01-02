package com.integral.enigmaticlegacy.api.materials;

import java.util.Objects;
import java.util.function.Supplier;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.etherium.core.IEtheriumConfig;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.world.item.IArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public enum EnigmaticArmorMaterials implements IArmorMaterial {
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
	private final LazyValue<Ingredient> repairMaterial;
	private final float knockbackResistance;

	private EnigmaticArmorMaterials(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantability, SoundEvent soundEvent, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterial) {
		this.name = name;
		this.maxDamageFactor = maxDamageFactor;
		this.damageReductionAmountArray = damageReductionAmountArray;
		this.enchantability = enchantability;
		this.soundEvent = soundEvent;
		this.toughness = toughness;
		this.repairMaterial = new LazyValue<>(repairMaterial);
		this.knockbackResistance = knockbackResistance;
	}

	@Override
	public int getDurabilityForSlot(EquipmentSlotType slot) {
		int durability = MAX_DAMAGE_ARRAY[slot.getIndex()] * this.maxDamageFactor;
		return durability;
	}

	@Override
	public int getDefenseForSlot(EquipmentSlotType slot) {
		return this.damageReductionAmountArray[slot.getIndex()];
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
