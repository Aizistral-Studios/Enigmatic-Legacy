package com.integral.enigmaticlegacy.triggers;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ForbiddenFruitTrigger extends SimpleCriterionTrigger<ForbiddenFruitTrigger.Instance> {
	public static final ResourceLocation ID = new ResourceLocation(EnigmaticLegacy.MODID, "consume_forbidden_fruit");
	public static final ForbiddenFruitTrigger INSTANCE = new ForbiddenFruitTrigger();

	private ForbiddenFruitTrigger() {}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return ForbiddenFruitTrigger.ID;
	}

	@Nonnull
	@Override
	public ForbiddenFruitTrigger.Instance createInstance(@Nonnull JsonObject json, @Nonnull EntityPredicate.Composite playerPred, DeserializationContext conditions) {
		return new ForbiddenFruitTrigger.Instance(playerPred);
	}

	public void trigger(ServerPlayer player) {
		this.trigger(player, instance -> instance.test());
	}

	static class Instance extends AbstractCriterionTriggerInstance {
		Instance(EntityPredicate.Composite playerPred) {
			super(ForbiddenFruitTrigger.ID, playerPred);
		}

		@Nonnull
		@Override
		public ResourceLocation getCriterion() {
			return ForbiddenFruitTrigger.ID;
		}

		boolean test() {
			return true;
		}
	}

}
