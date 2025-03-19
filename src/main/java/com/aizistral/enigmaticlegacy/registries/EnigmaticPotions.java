package com.aizistral.enigmaticlegacy.registries;

import java.util.ArrayList;
import java.util.List;

import com.aizistral.enigmaticlegacy.gui.containers.LoreInscriberContainer;
import com.aizistral.enigmaticlegacy.objects.AdvancedPotion;
import com.google.common.collect.ImmutableList;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

public class EnigmaticPotions {
	private static final EnigmaticPotions INSTANCE = new EnigmaticPotions();

	public static final List<AdvancedPotion> ULTIMATE_POTIONS;
	public static final List<AdvancedPotion> COMMON_POTIONS;

	public static final AdvancedPotion ULTIMATE_NIGHT_VISION;
	public static final AdvancedPotion ULTIMATE_INVISIBILITY;
	public static final AdvancedPotion ULTIMATE_FIRE_RESISTANCE;
	public static final AdvancedPotion ULTIMATE_LEAPING;
	public static final AdvancedPotion ULTIMATE_SWIFTNESS;
	public static final AdvancedPotion ULTIMATE_SLOWNESS;
	public static final AdvancedPotion ULTIMATE_TURTLE_MASTER;
	public static final AdvancedPotion ULTIMATE_WATER_BREATHING;
	public static final AdvancedPotion ULTIMATE_HEALING;
	public static final AdvancedPotion ULTIMATE_HARMING;
	public static final AdvancedPotion ULTIMATE_POISON;
	public static final AdvancedPotion ULTIMATE_REGENERATION;
	public static final AdvancedPotion ULTIMATE_STRENGTH;
	public static final AdvancedPotion ULTIMATE_WEAKNESS;
	public static final AdvancedPotion ULTMATE_SLOW_FALLING;
	public static final AdvancedPotion HASTE;
	public static final AdvancedPotion LONG_HASTE;
	public static final AdvancedPotion STRONG_HASTE;
	public static final AdvancedPotion ULTIMATE_HASTE;
	public static final AdvancedPotion MOLTEN_HEART;
	public static final AdvancedPotion LONG_MOLTEN_HEART;
	public static final AdvancedPotion ULTIMATE_MOLTEN_HEART;
	public static final AdvancedPotion EMPTY_POTION;

	static {
		ULTIMATE_NIGHT_VISION = new AdvancedPotion("ultimate_night_vision", new MobEffectInstance(MobEffects.NIGHT_VISION, 19200));
		ULTIMATE_INVISIBILITY = new AdvancedPotion("ultimate_invisibility", new MobEffectInstance(MobEffects.INVISIBILITY, 19200));
		ULTIMATE_LEAPING = new AdvancedPotion("ultimate_leaping", new MobEffectInstance(MobEffects.JUMP, 9600, 1));
		ULTIMATE_FIRE_RESISTANCE = new AdvancedPotion("ultimate_fire_resistance", new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 19200));
		ULTIMATE_SWIFTNESS = new AdvancedPotion("ultimate_swiftness", new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 9600, 1));
		ULTIMATE_SLOWNESS = new AdvancedPotion("ultimate_slowness", new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1200, 3));
		ULTIMATE_TURTLE_MASTER = new AdvancedPotion("ultimate_turtle_master", new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 800, 5), new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 800, 3));
		ULTIMATE_WATER_BREATHING = new AdvancedPotion("ultimate_water_breathing", new MobEffectInstance(MobEffects.WATER_BREATHING, 19200));
		ULTIMATE_HEALING = new AdvancedPotion("ultimate_healing", new MobEffectInstance(MobEffects.HEAL, 1, 2));
		ULTIMATE_HARMING = new AdvancedPotion("ultimate_harming", new MobEffectInstance(MobEffects.HARM, 1, 2));
		ULTIMATE_POISON = new AdvancedPotion("ultimate_poison", new MobEffectInstance(MobEffects.POISON, 1800, 1));
		ULTIMATE_REGENERATION = new AdvancedPotion("ultimate_regeneration", new MobEffectInstance(MobEffects.REGENERATION, 1800, 1));
		ULTIMATE_STRENGTH = new AdvancedPotion("ultimate_strength", new MobEffectInstance(MobEffects.DAMAGE_BOOST, 9600, 1));
		ULTIMATE_WEAKNESS = new AdvancedPotion("ultimate_weakness", new MobEffectInstance(MobEffects.WEAKNESS, 9600));
		ULTMATE_SLOW_FALLING = new AdvancedPotion("ultimate_slow_falling", new MobEffectInstance(MobEffects.SLOW_FALLING, 9600));

		HASTE = new AdvancedPotion("haste", new MobEffectInstance(MobEffects.DIG_SPEED, 3600));
		LONG_HASTE = new AdvancedPotion("long_haste", new MobEffectInstance(MobEffects.DIG_SPEED, 9600));
		STRONG_HASTE = new AdvancedPotion("strong_haste", new MobEffectInstance(MobEffects.DIG_SPEED, 1800, 1));
		ULTIMATE_HASTE = new AdvancedPotion("ultimate_haste", new MobEffectInstance(MobEffects.DIG_SPEED, 9600, 1));

		MOLTEN_HEART = new AdvancedPotion("molten_heart", new MobEffectInstance(EnigmaticEffects.MOLTEN_HEART, 3600));
		LONG_MOLTEN_HEART = new AdvancedPotion("long_molten_heart", new MobEffectInstance(EnigmaticEffects.MOLTEN_HEART, 9600));
		ULTIMATE_MOLTEN_HEART = new AdvancedPotion("ultimate_molten_heart", new MobEffectInstance(EnigmaticEffects.MOLTEN_HEART, 19200));

		EMPTY_POTION = new AdvancedPotion("empty");

		ImmutableList.Builder<AdvancedPotion> ultimatePotions = ImmutableList.builder();
		ImmutableList.Builder<AdvancedPotion> commonPotions = ImmutableList.builder();

		ultimatePotions.add(ULTIMATE_NIGHT_VISION);
		ultimatePotions.add(ULTIMATE_INVISIBILITY);
		ultimatePotions.add(ULTIMATE_LEAPING);
		ultimatePotions.add(ULTIMATE_FIRE_RESISTANCE);
		ultimatePotions.add(ULTIMATE_SWIFTNESS);
		ultimatePotions.add(ULTIMATE_SLOWNESS);
		ultimatePotions.add(ULTIMATE_TURTLE_MASTER);
		ultimatePotions.add(ULTIMATE_WATER_BREATHING);
		ultimatePotions.add(ULTIMATE_HEALING);
		ultimatePotions.add(ULTIMATE_HARMING);
		ultimatePotions.add(ULTIMATE_POISON);
		ultimatePotions.add(ULTIMATE_REGENERATION);
		ultimatePotions.add(ULTIMATE_STRENGTH);
		ultimatePotions.add(ULTIMATE_WEAKNESS);
		ultimatePotions.add(ULTMATE_SLOW_FALLING);

		commonPotions.add(HASTE);
		commonPotions.add(LONG_HASTE);
		commonPotions.add(STRONG_HASTE);
		ultimatePotions.add(ULTIMATE_HASTE);

		commonPotions.add(MOLTEN_HEART);
		commonPotions.add(LONG_MOLTEN_HEART);
		ultimatePotions.add(ULTIMATE_MOLTEN_HEART);

		ULTIMATE_POTIONS = ultimatePotions.build();
		COMMON_POTIONS = commonPotions.build();
	}

	private EnigmaticPotions() {
		super();
	}

}
