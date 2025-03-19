package com.aizistral.enigmaticlegacy.handlers;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.items.RevelationTome;
import com.aizistral.enigmaticlegacy.registries.EnigmaticLootFunctions;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class LootFunctionRevelation extends LootItemConditionalFunction {
	private final NumberProvider randomPoints;
	private final NumberProvider randomXP;

	private LootFunctionRevelation(LootItemCondition[] conditions, NumberProvider minMaxPoints, NumberProvider minMaxXP) {
		super(conditions);
		this.randomPoints = minMaxPoints;
		this.randomXP = minMaxXP;
	}

	@Override
	public LootItemFunctionType getType() {
		return EnigmaticLootFunctions.REVELATION;
	}

	@Override
	public ItemStack run(ItemStack stack, LootContext context) {
		if (stack.getItem() instanceof RevelationTome tome) {
			stack.setTag(tome.createTome(this.randomPoints.getInt(context), this.randomXP.getInt(context)).getTag().copy());
		}

		return stack;
	}

	public static LootFunctionRevelation.Builder of(NumberProvider minMaxPoints, NumberProvider minMaxXP) {
		return new LootFunctionRevelation.Builder(minMaxPoints, minMaxXP);
	}

	public static class Builder extends LootItemConditionalFunction.Builder<LootFunctionRevelation.Builder> {
		private final NumberProvider randomPoints;
		private final NumberProvider randomXP;

		public Builder(NumberProvider minMaxPoints, NumberProvider minMaxXP) {
			this.randomPoints = minMaxPoints;
			this.randomXP = minMaxXP;
		}

		@Override
		protected LootFunctionRevelation.Builder getThis() {
			return this;
		}

		@Override
		public LootItemFunction build() {
			return new LootFunctionRevelation(this.getConditions(), this.randomPoints, this.randomXP);
		}
	}

	public static class Serializer extends LootItemConditionalFunction.Serializer<LootFunctionRevelation> {
		@Override
		public void serialize(JsonObject json, LootFunctionRevelation value, JsonSerializationContext context) {
			super.serialize(json, value, context);
			json.add("randomPoints", context.serialize(value.randomPoints));
			json.add("randomXP", context.serialize(value.randomXP));
		}

		@Override
		public LootFunctionRevelation deserialize(JsonObject json, JsonDeserializationContext context, LootItemCondition[] conditions) {
			NumberProvider randomPoints = GsonHelper.getAsObject(json, "randomPoints", context, NumberProvider.class);
			NumberProvider randomXP = GsonHelper.getAsObject(json, "randomXP", context, NumberProvider.class);
			return new LootFunctionRevelation(conditions, randomPoints, randomXP);
		}
	}

}
