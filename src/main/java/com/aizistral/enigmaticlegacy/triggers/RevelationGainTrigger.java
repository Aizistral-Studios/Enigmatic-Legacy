package com.aizistral.enigmaticlegacy.triggers;

import javax.annotation.Nonnull;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.items.RevelationTome;
import com.google.gson.JsonObject;

import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

public class RevelationGainTrigger extends SimpleCriterionTrigger<RevelationGainTrigger.Instance> {
	public static final ResourceLocation ID = new ResourceLocation(EnigmaticLegacy.MODID, "embrace_revelation");
	public static final RevelationGainTrigger INSTANCE = new RevelationGainTrigger();

	private RevelationGainTrigger() {}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return RevelationGainTrigger.ID;
	}

	@Nonnull
	@Override
	public RevelationGainTrigger.Instance createInstance(@Nonnull JsonObject json, @Nonnull ContextAwarePredicate playerPred, DeserializationContext conditions) {
		return new RevelationGainTrigger.Instance(playerPred, GsonHelper.getAsString(json, "point_type"), GsonHelper.getAsInt(json, "point_amount"));
	}

	public void trigger(ServerPlayer player, RevelationTome.TomeType type, int amount) {
		this.trigger(player, instance -> instance.test(type, amount));
	}

	static class Instance extends AbstractCriterionTriggerInstance {
		private final String revelationType;
		private final int requiredAmount;

		Instance(ContextAwarePredicate playerPred, String type, int amount) {
			super(RevelationGainTrigger.ID, playerPred);
			this.revelationType = type;
			this.requiredAmount = amount;
		}

		@Nonnull
		@Override
		public ResourceLocation getCriterion() {
			return RevelationGainTrigger.ID;
		}

		boolean test(RevelationTome.TomeType type, int count) {
			boolean success = RevelationTome.TomeType.resolveType(this.revelationType).equals(type) && count >= this.requiredAmount;
			//System.out.println("Requested amount: " + this.requiredAmount + ", provided amount: " + count + ", requested/provided revelation type: " + this.revelationType + "/" + type.registryName +  ", success: " + success);
			return success;
		}
	}
}

