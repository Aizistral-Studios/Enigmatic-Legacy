package com.aizistral.enigmaticlegacy.triggers;

import javax.annotation.Nonnull;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.google.gson.JsonObject;

import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class RevelationTomeBurntTrigger extends SimpleCriterionTrigger<RevelationTomeBurntTrigger.Instance> {
	public static final ResourceLocation ID = new ResourceLocation(EnigmaticLegacy.MODID, "revelation_tome_burnt");
	public static final RevelationTomeBurntTrigger INSTANCE = new RevelationTomeBurntTrigger();

	private RevelationTomeBurntTrigger() {}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return RevelationTomeBurntTrigger.ID;
	}

	@Nonnull
	@Override
	public RevelationTomeBurntTrigger.Instance createInstance(@Nonnull JsonObject json, @Nonnull ContextAwarePredicate playerPred, DeserializationContext conditions) {
		return new RevelationTomeBurntTrigger.Instance(playerPred);
	}

	public void trigger(ServerPlayer player) {
		this.trigger(player, instance -> instance.test());
	}

	static class Instance extends AbstractCriterionTriggerInstance {
		Instance(ContextAwarePredicate playerPred) {
			super(RevelationTomeBurntTrigger.ID, playerPred);
		}

		@Nonnull
		@Override
		public ResourceLocation getCriterion() {
			return RevelationTomeBurntTrigger.ID;
		}

		boolean test() {
			return true;
		}
	}
}