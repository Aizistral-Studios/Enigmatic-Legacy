package com.integral.enigmaticlegacy.registry;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.integral.enigmaticlegacy.gui.containers.LoreInscriberContainer;
import com.integral.enigmaticlegacy.objects.AdvancedPotion;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

public class EnigmaticPotions extends AbstractRegistry<Potion> {
	private static final EnigmaticPotions INSTANCE = new EnigmaticPotions();

	public static final List<AdvancedPotion> ultimatePotionTypes;
	public static final List<AdvancedPotion> commonPotionTypes;

	public static final AdvancedPotion ultimateNightVision;
	public static final AdvancedPotion ultimateInvisibility;
	public static final AdvancedPotion ultimateFireResistance;
	public static final AdvancedPotion ultimateLeaping;
	public static final AdvancedPotion ultimateSwiftness;
	public static final AdvancedPotion ultimateSlowness;
	public static final AdvancedPotion ultimateTurtleMaster;
	public static final AdvancedPotion ultimateWaterBreathing;
	public static final AdvancedPotion ultimateHealing;
	public static final AdvancedPotion ultimateHarming;
	public static final AdvancedPotion ultimatePoison;
	public static final AdvancedPotion ultimateRegeneration;
	public static final AdvancedPotion ultimateStrength;
	public static final AdvancedPotion ultimateWeakness;
	public static final AdvancedPotion ultimateSlowFalling;
	public static final AdvancedPotion haste;
	public static final AdvancedPotion longHaste;
	public static final AdvancedPotion strongHaste;
	public static final AdvancedPotion ultimateHaste;
	public static final AdvancedPotion moltenHeart;
	public static final AdvancedPotion longMoltenHeart;
	public static final AdvancedPotion ultimateMoltenHeart;
	public static final AdvancedPotion emptyPotion;

	static {
		ultimateNightVision = new AdvancedPotion("ultimate_night_vision", new MobEffectInstance(MobEffects.NIGHT_VISION, 19200));
		ultimateInvisibility = new AdvancedPotion("ultimate_invisibility", new MobEffectInstance(MobEffects.INVISIBILITY, 19200));
		ultimateLeaping = new AdvancedPotion("ultimate_leaping", new MobEffectInstance(MobEffects.JUMP, 9600, 1));
		ultimateFireResistance = new AdvancedPotion("ultimate_fire_resistance", new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 19200));
		ultimateSwiftness = new AdvancedPotion("ultimate_swiftness", new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 9600, 1));
		ultimateSlowness = new AdvancedPotion("ultimate_slowness", new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1200, 3));
		ultimateTurtleMaster = new AdvancedPotion("ultimate_turtle_master", new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 800, 5), new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 800, 3));
		ultimateWaterBreathing = new AdvancedPotion("ultimate_water_breathing", new MobEffectInstance(MobEffects.WATER_BREATHING, 19200));
		ultimateHealing = new AdvancedPotion("ultimate_healing", new MobEffectInstance(MobEffects.HEAL, 1, 2));
		ultimateHarming = new AdvancedPotion("ultimate_harming", new MobEffectInstance(MobEffects.HARM, 1, 2));
		ultimatePoison = new AdvancedPotion("ultimate_poison", new MobEffectInstance(MobEffects.POISON, 1800, 1));
		ultimateRegeneration = new AdvancedPotion("ultimate_regeneration", new MobEffectInstance(MobEffects.REGENERATION, 1800, 1));
		ultimateStrength = new AdvancedPotion("ultimate_strength", new MobEffectInstance(MobEffects.DAMAGE_BOOST, 9600, 1));
		ultimateWeakness = new AdvancedPotion("ultimate_weakness", new MobEffectInstance(MobEffects.WEAKNESS, 9600));
		ultimateSlowFalling = new AdvancedPotion("ultimate_slow_falling", new MobEffectInstance(MobEffects.SLOW_FALLING, 9600));

		haste = new AdvancedPotion("haste", new MobEffectInstance(MobEffects.DIG_SPEED, 3600));
		longHaste = new AdvancedPotion("long_haste", new MobEffectInstance(MobEffects.DIG_SPEED, 9600));
		strongHaste = new AdvancedPotion("strong_haste", new MobEffectInstance(MobEffects.DIG_SPEED, 1800, 1));
		ultimateHaste = new AdvancedPotion("ultimate_haste", new MobEffectInstance(MobEffects.DIG_SPEED, 9600, 1));

		moltenHeart = new AdvancedPotion("molten_heart", new MobEffectInstance(EnigmaticEffects.moltenHeartEffect, 3600));
		longMoltenHeart = new AdvancedPotion("long_molten_heart", new MobEffectInstance(EnigmaticEffects.moltenHeartEffect, 9600));
		ultimateMoltenHeart = new AdvancedPotion("ultimate_molten_heart", new MobEffectInstance(EnigmaticEffects.moltenHeartEffect, 19200));

		emptyPotion = new AdvancedPotion("empty");

		ImmutableList.Builder<AdvancedPotion> ultimatePotions = ImmutableList.builder();
		ImmutableList.Builder<AdvancedPotion> commonPotions = ImmutableList.builder();

		ultimatePotions.add(ultimateNightVision);
		ultimatePotions.add(ultimateInvisibility);
		ultimatePotions.add(ultimateLeaping);
		ultimatePotions.add(ultimateFireResistance);
		ultimatePotions.add(ultimateSlowness);
		ultimatePotions.add(ultimateTurtleMaster);
		ultimatePotions.add(ultimateWaterBreathing);
		ultimatePotions.add(ultimateHealing);
		ultimatePotions.add(ultimateHarming);
		ultimatePotions.add(ultimatePoison);
		ultimatePotions.add(ultimateRegeneration);
		ultimatePotions.add(ultimateStrength);
		ultimatePotions.add(ultimateWeakness);
		ultimatePotions.add(ultimateSlowFalling);

		commonPotions.add(haste);
		commonPotions.add(longHaste);
		commonPotions.add(strongHaste);
		ultimatePotions.add(ultimateHaste);

		commonPotions.add(moltenHeart);
		commonPotions.add(longMoltenHeart);
		ultimatePotions.add(ultimateMoltenHeart);

		ultimatePotionTypes = ultimatePotions.build();
		commonPotionTypes = commonPotions.build();
	}

	private EnigmaticPotions() {
		super(ForgeRegistries.POTIONS);
	}

}
