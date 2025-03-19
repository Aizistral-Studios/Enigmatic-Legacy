package com.aizistral.enigmaticlegacy.registries;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

// Used there to be simpler times...
public class EnigmaticDamageTypes {
	public static final ResourceKey<DamageType> DARKNESS = register("darkness");
	public static final ResourceKey<DamageType> NEMESIS_CURSE = register("nemesis_curse");

	private static ResourceKey<DamageType> register(String name) {
		return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(EnigmaticLegacy.MODID, name));
	}
}
