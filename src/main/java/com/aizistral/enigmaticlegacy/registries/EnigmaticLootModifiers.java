package com.aizistral.enigmaticlegacy.registries;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.objects.SpecialLootModifier;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class EnigmaticLootModifiers {
	private static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIERS =
			DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, EnigmaticLegacy.MODID);

	public static final RegistryObject<Codec<SpecialLootModifier>> SPECIAL_LOOT_MODIFIER =
			register("special_loot_modifier", SpecialLootModifier.CODEC);

	public static void registerLootModifiers(IEventBus modEventBus) {
		GLOBAL_LOOT_MODIFIERS.register(modEventBus);
	}

	private static <T extends IGlobalLootModifier> RegistryObject<Codec<T>> register(
			String name, Supplier<Codec<T>> codec
	) {
		return GLOBAL_LOOT_MODIFIERS.register(name, codec);
	}
}