package com.aizistral.enigmaticlegacy.registries;

import com.aizistral.enigmaticlegacy.objects.SpecialLootModifier;
import com.mojang.serialization.Codec;

import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.ForgeRegistries;

public class EnigmaticLootModifiers extends AbstractRegistry<Codec<? extends IGlobalLootModifier>> {
	private static final EnigmaticLootModifiers INSTANCE = new EnigmaticLootModifiers();

	private EnigmaticLootModifiers() {
		super(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS);
		this.register("special_loot_modifier", SpecialLootModifier.CODEC::get);
	}

}