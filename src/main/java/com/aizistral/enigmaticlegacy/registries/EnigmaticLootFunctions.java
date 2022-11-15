package com.aizistral.enigmaticlegacy.registries;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.handlers.LootFunctionRevelation;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;

public class EnigmaticLootFunctions {
	private static final EnigmaticLootFunctions INSTANCE = new EnigmaticLootFunctions();
	public static final LootItemFunctionType REVELATION = register("revelation", new LootFunctionRevelation.Serializer());

	public EnigmaticLootFunctions() {
		// NO-OP
	}

	private static LootItemFunctionType register(String id, Serializer<? extends LootItemFunction> serializer) {
		return Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(EnigmaticLegacy.MODID, id),
				new LootItemFunctionType(serializer));
	}

}
