package com.integral.enigmaticlegacy.handlers;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.items.RevelationTome;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.IRandomRange;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.ILootFunction;

public class LootFunctionRevelation implements ILootFunction {
	private final IRandomRange randomPoints;
	private final IRandomRange randomXP;

	private LootFunctionRevelation(ILootCondition[] conditions, IRandomRange minMaxPoints, IRandomRange minMaxXP) {
		this.randomPoints = minMaxPoints;
		this.randomXP = minMaxXP;
	}

	@Override
	public LootFunctionType func_230425_b_() {
		//return LootFunctionManager.field_237430_c_;
		EnigmaticLegacy.enigmaticLogger.info("Substituting LootFunctionType with null...");

		try {
			return null;
		} catch (NullPointerException ex) {
			EnigmaticLegacy.enigmaticLogger.fatal("I must assume that non-serializable LootFuction thing haven't played out well.");
			throw ex;
		}
	}

	@Override
	public ItemStack apply(ItemStack stack, LootContext context) {
		if (stack.getItem() instanceof RevelationTome)
			stack.setTag(((RevelationTome)stack.getItem()).createTome(this.randomPoints.generateInt(context.getRandom()), this.randomXP.generateInt(context.getRandom())).getTag().copy());

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
		protected LootFunctionRevelation.Builder doCast() {
			return this;
		}

		@Override
		public ILootFunction build() {
			return new LootFunctionRevelation(this.getConditions(), this.randomPoints, this.randomXP);
		}
	}

}
