package com.aizistral.enigmaticlegacy.registries;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class EnigmaticPotions {
	private static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, EnigmaticLegacy.MODID);

	private static final List<Supplier<Potion>> ULTIMATE_POTIONS = new ArrayList<>();
	private static final List<Supplier<Potion>> COMMON_POTIONS =  new ArrayList<>();

	public static final RegistryObject<Potion> ULTIMATE_NIGHT_VISION = register("ultimate_night_vision", PotionType.ULTIMATE, new MobEffectInstance(MobEffects.NIGHT_VISION, 19200));
	public static final RegistryObject<Potion> ULTIMATE_INVISIBILITY = register("ultimate_invisibility", PotionType.ULTIMATE, new MobEffectInstance(MobEffects.INVISIBILITY, 19200));
	public static final RegistryObject<Potion> ULTIMATE_FIRE_RESISTANCE = register("ultimate_fire_resistance", PotionType.ULTIMATE, new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 19200));
	public static final RegistryObject<Potion> ULTIMATE_LEAPING = register("ultimate_leaping", PotionType.ULTIMATE, new MobEffectInstance(MobEffects.JUMP, 9600, 1));
	public static final RegistryObject<Potion> ULTIMATE_SWIFTNESS = register("ultimate_swiftness", PotionType.ULTIMATE, new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 9600, 1));
	public static final RegistryObject<Potion> ULTIMATE_SLOWNESS = register("ultimate_slowness", PotionType.ULTIMATE, new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1200, 3));
	public static final RegistryObject<Potion> ULTIMATE_TURTLE_MASTER = register("ultimate_turtle_master", PotionType.ULTIMATE, new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 800, 5), new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 800, 3));
	public static final RegistryObject<Potion> ULTIMATE_WATER_BREATHING = register("ultimate_water_breathing", PotionType.ULTIMATE, new MobEffectInstance(MobEffects.WATER_BREATHING, 19200));
	public static final RegistryObject<Potion> ULTIMATE_HEALING = register("ultimate_healing", PotionType.ULTIMATE, new MobEffectInstance(MobEffects.HEAL, 1, 2));
	public static final RegistryObject<Potion> ULTIMATE_HARMING = register("ultimate_harming", PotionType.ULTIMATE, new MobEffectInstance(MobEffects.HARM, 1, 2));
	public static final RegistryObject<Potion> ULTIMATE_POISON = register("ultimate_poison", PotionType.ULTIMATE, new MobEffectInstance(MobEffects.POISON, 1800, 1));
	public static final RegistryObject<Potion> ULTIMATE_REGENERATION = register("ultimate_regeneration", PotionType.ULTIMATE, new MobEffectInstance(MobEffects.REGENERATION, 1800, 1));
	public static final RegistryObject<Potion> ULTIMATE_STRENGTH = register("ultimate_strength", PotionType.ULTIMATE, new MobEffectInstance(MobEffects.DAMAGE_BOOST, 9600, 1));
	public static final RegistryObject<Potion> ULTIMATE_WEAKNESS = register("ultimate_weakness", PotionType.ULTIMATE, new MobEffectInstance(MobEffects.WEAKNESS, 9600));
	public static final RegistryObject<Potion> ULTIMATE_SLOW_FALLING = register("ultimate_slow_falling", PotionType.ULTIMATE, new MobEffectInstance(MobEffects.SLOW_FALLING, 9600));
	public static final RegistryObject<Potion> ULTIMATE_MOLTEN_HEART = register("ultimate_molten_heart", PotionType.ULTIMATE, () -> new MobEffectInstance[]{new MobEffectInstance(EnigmaticEffects.MOLTEN_HEART.get(), 19200)});
	public static final RegistryObject<Potion> ULTIMATE_HASTE = register("ultimate_haste", PotionType.ULTIMATE, new MobEffectInstance(MobEffects.DIG_SPEED, 9600, 1));

	public static final RegistryObject<Potion> HASTE = register("haste", new MobEffectInstance(MobEffects.DIG_SPEED, 3600));
	public static final RegistryObject<Potion> LONG_HASTE = register("long_haste", new MobEffectInstance(MobEffects.DIG_SPEED, 9600));
	public static final RegistryObject<Potion> STRONG_HASTE = register("strong_haste", new MobEffectInstance(MobEffects.DIG_SPEED, 1800, 1));
	public static final RegistryObject<Potion> MOLTEN_HEART = register("molten_heart",() -> new MobEffectInstance[]{new MobEffectInstance(EnigmaticEffects.MOLTEN_HEART.get(), 3600)});
	public static final RegistryObject<Potion> LONG_MOLTEN_HEART = register("long_molten_heart", () -> new MobEffectInstance[]{new MobEffectInstance(EnigmaticEffects.MOLTEN_HEART.get(), 9600)});

	// Vanilla Objects
	private static RegistryObject<Potion> register(String name, MobEffectInstance... effects) {
		return register(name, () -> effects);
	}

	private static RegistryObject<Potion> register(String name, PotionType type, MobEffectInstance... effects) {
		return register(name, type, () -> effects);
	}

	// Supplier Objects
	private static RegistryObject<Potion> register(String name, Supplier<MobEffectInstance[]> effects) {
		return register(name, PotionType.COMMON, effects);
	}

	private static RegistryObject<Potion> register(String name, PotionType type, Supplier<MobEffectInstance[]> effects) {
		RegistryObject<Potion> potion = POTIONS.register(name, () -> new Potion(name, effects.get()));

		if (type == PotionType.ULTIMATE) {
			ULTIMATE_POTIONS.add(potion);
		}
		else if (type == PotionType.COMMON) {
			COMMON_POTIONS.add(potion);
		}

		return potion;
	}

	private enum PotionType {
		COMMON,
		ULTIMATE
	}

	public static void registerPotions(IEventBus modEventBus) {
		POTIONS.register(modEventBus);
	}

	public static List<Supplier<Potion>> getUltimatePotions() {
		return Collections.unmodifiableList(ULTIMATE_POTIONS);
	}

	public static List<Supplier<Potion>> getCommonPotions() {
		return Collections.unmodifiableList(COMMON_POTIONS);
	}
}
