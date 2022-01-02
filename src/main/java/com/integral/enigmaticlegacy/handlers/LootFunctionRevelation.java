package com.integral.enigmaticlegacy.handlers;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.items.RevelationTome;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.IRandomRange;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootFunction;
import net.minecraft.world.level.storage.loot.LootFunctionType;
import net.minecraft.world.level.storage.loot.conditions.ILootCondition;
import net.minecraft.world.level.storage.loot.functions.ILootFunction;

public class LootFunctionRevelation implements ILootFunction {
	private final IRandomRange randomPoints;
	private final IRandomRange randomXP;

	private LootFunctionRevelation(ILootCondition[] conditions, IRandomRange minMaxPoints, IRandomRange minMaxXP) {
		this.randomPoints = minMaxPoints;
		this.randomXP = minMaxXP;
	}

	@Override
	public LootFunctionType getType() {
		//return LootFunctionManager.ENCHANT_WITH_LEVELS;
		EnigmaticLegacy.logger.info("Substituting LootFunctionType with null...");

		try {
			return null;
		} catch (NullPointerException ex) {
			EnigmaticLegacy.logger.fatal("I must assume that non-serializable LootFuction thing haven't played out well.");
			throw ex;
		}
	}

	@Override
	public ItemStack apply(ItemStack stack, LootContext context) {
		if (stack.getItem() instanceof RevelationTome)
			stack.setTag(((RevelationTome)stack.getItem()).createTome(this.randomPoints.getInt(context.getRandom()), this.randomXP.getInt(context.getRandom())).getTag().copy());

		return stack;
	}

	public static LootFunctionRevelation.Builder of(IRandomRange minMaxPoints, IRandomRange minMaxXP) {
		return new LootFunctionRevelation.Builder(minMaxPoints, minMaxXP);
	}

	public static class Builder extends LootFunction.Builder<LootFunctionRevelation.Builder> {
		private final IRandomRange randomPoints;
		private final IRandomRange randomXP;

		public Builder(IRandomRange minMaxPoints, IRandomRange minMaxXP) {
			this.randomPoints = minMaxPoints;
			this.randomXP = minMaxXP;
		}

		@Override
		protected LootFunctionRevelation.Builder getThis() {
			return this;
		}

		@Override
		public ILootFunction build() {
			return new LootFunctionRevelation(this.getConditions(), this.randomPoints, this.randomXP);
		}
	}

}
