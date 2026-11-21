package com.aizistral.enigmaticlegacy.registries;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.effects.BlazingStrengthEffect;
import com.aizistral.enigmaticlegacy.effects.GrowingBloodlustEffect;
import com.aizistral.enigmaticlegacy.effects.GrowingHungerEffect;
import com.aizistral.enigmaticlegacy.effects.MoltenHeartEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class EnigmaticEffects {
	private static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, EnigmaticLegacy.MODID);

	public static final RegistryObject<BlazingStrengthEffect> BLAZING_STRENGTH = register("blazing_strength", BlazingStrengthEffect::new);
	public static final RegistryObject<MoltenHeartEffect> MOLTEN_HEART = register("molten_heart", MoltenHeartEffect::new);
	public static final RegistryObject<GrowingHungerEffect> GROWING_HUNGER = register("growing_hunger", GrowingHungerEffect::new);
	public static final RegistryObject<GrowingBloodlustEffect> GROWING_BLOODLUST = register("growing_bloodlust", GrowingBloodlustEffect::new);

	public static void registerEffects(IEventBus modEventBus) {
		MOB_EFFECTS.register(modEventBus);
	}

	private static <T extends MobEffect> RegistryObject<T> register(String name, Supplier<T> effect) {
		return MOB_EFFECTS.register(name, effect);
	}
}
