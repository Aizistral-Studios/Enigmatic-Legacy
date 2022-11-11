package com.aizistral.enigmaticlegacy.handlers;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.items.RevelationTome;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class LootFunctionRevelation implements LootItemFunction {
	private final NumberProvider randomPoints;
	private final NumberProvider randomXP;

	private LootFunctionRevelation(LootItemCondition[] conditions, NumberProvider minMaxPoints, NumberProvider minMaxXP) {
		this.randomPoints = minMaxPoints;
		this.randomXP = minMaxXP;
	}

	@Override
	public LootItemFunctionType getType() {
		//return LootFunctionManager.ENCHANT_WITH_LEVELS;
		EnigmaticLegacy.LOGGER.info("Substituting LootFunctionType with null...");

		try {
			return null;
		} catch (NullPointerException ex) {
			EnigmaticLegacy.LOGGER.fatal("I must assume that non-serializable LootFuction thing haven't played out well.");
			throw ex;
		}
	}

	@Override
	public ItemStack apply(ItemStack stack, LootContext context) {
		if (stack.getItem() instanceof RevelationTome) {
			stack.setTag(((RevelationTome)stack.getItem()).createTome(this.randomPoints.getInt(context), this.randomXP.getInt(context)).getTag().copy());
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

}
