package com.aizistral.enigmaticlegacy.api.materials;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.items.EldritchPan;
import com.aizistral.etherium.core.IEtheriumConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.TierSortingRegistry;

/**
 * Tools material provider.
 * @author Integral
 */

public enum EnigmaticMaterials implements Tier {
	ELDRITCH_PAN(0, 4000, 6.0F, 3.0F, 24, EldritchPan::getRepairMaterial),
	FORBIDDEN_AXE(0, 2000, 6.0F, 3.0F, 16, () -> Ingredient.EMPTY),
	ENDER_SLAYER(0, 2000, 6.0F, 3.0F, 16, () -> Ingredient.of(Blocks.OBSIDIAN)),
	ETHERIUM(5, 3000, 8.0F, 5.0F, 32, () -> getEtheriumConfig().getRepairMaterial()),
	CREATION(6, 10000, 32.0F, 0.0F, 40, () -> Ingredient.EMPTY);

	private static IEtheriumConfig etheriumConfig;

	private final int harvestLevel;
	private final int maxUses;
	private final float efficiency;
	private final float attackDamage;
	private final int enchantability;
	private final Supplier<Ingredient> repairMaterial;

	private EnigmaticMaterials(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
		this.harvestLevel = harvestLevelIn;
		this.maxUses = maxUsesIn;
		this.efficiency = efficiencyIn;
		this.attackDamage = attackDamageIn;
		this.enchantability = enchantabilityIn;
		this.repairMaterial = repairMaterialIn;

		if (harvestLevelIn == 5) {
			TierSortingRegistry.registerTier(this, new ResourceLocation("enigmaticlegacy", "etherium"),
					List.of((Object[]) Tiers.values()), List.of());
		} else if (harvestLevelIn == 6) {
			TierSortingRegistry.registerTier(this, new ResourceLocation("enigmaticlegacy", "creation"),
					List.of((Object[]) Tiers.values()), List.of());
		}
	}

	@Override
	public int getUses() {
		return this.maxUses;
	}

	@Override
	public float getSpeed() {
		return this.efficiency;
	}

	@Override
	public float getAttackDamageBonus() {
		return this.attackDamage;
	}

	@Override
	public int getLevel() {
		return this.harvestLevel;
	}

	@Override
	public int getEnchantmentValue() {
		return this.enchantability;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return this.repairMaterial.get();
	}

	public static IEtheriumConfig getEtheriumConfig() {
		return etheriumConfig;
	}

	public static void setEtheriumConfig(IEtheriumConfig config) {
		etheriumConfig = Objects.requireNonNull(config);
	}

}
