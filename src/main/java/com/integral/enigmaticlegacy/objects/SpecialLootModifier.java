package com.integral.enigmaticlegacy.objects;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

public class SpecialLootModifier extends LootModifier {

	protected SpecialLootModifier(LootItemCondition[] conditions) {
		super(conditions);
	}

	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		if ("minecraft:chests/end_city_treasure".equals(String.valueOf(context.getQueriedLootTableId()))) {
			Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);

			if (entity instanceof ServerPlayer player) {
				if (!SuperpositionHandler.hasPersistentTag(player, "LootedFirstEndCityChest")) {
					SuperpositionHandler.setPersistentBoolean(player, "LootedFirstEndCityChest", true);

					if (SuperpositionHandler.isTheCursedOne(player)) {
						generatedLoot.add(new ItemStack(EnigmaticLegacy.astralFruit, 1));
					}
				}
			}
		}

		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<SpecialLootModifier> {
		@Override
		public SpecialLootModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] conditions) {
			return new SpecialLootModifier(conditions);
		}

		@Override
		public JsonObject write(SpecialLootModifier instance) {
			return this.makeConditions(instance.conditions);
		}
	}

}
