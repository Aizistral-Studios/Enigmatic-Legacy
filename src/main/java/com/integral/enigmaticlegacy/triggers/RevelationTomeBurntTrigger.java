package com.integral.enigmaticlegacy.triggers;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;

public class RevelationTomeBurntTrigger extends AbstractCriterionTrigger<RevelationTomeBurntTrigger.Instance> {
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
	public RevelationTomeBurntTrigger.Instance deserializeTrigger(@Nonnull JsonObject json, @Nonnull EntityPredicate.AndPredicate playerPred, ConditionArrayParser conditions) {
		return new RevelationTomeBurntTrigger.Instance(playerPred);
	}

	public void trigger(ServerPlayerEntity player) {
		this.triggerListeners(player, instance -> instance.test());
	}

	static class Instance extends CriterionInstance {
		Instance(EntityPredicate.AndPredicate playerPred) {
			super(RevelationTomeBurntTrigger.ID, playerPred);
		}

		@Nonnull
		@Override
		public ResourceLocation getId() {
			return RevelationTomeBurntTrigger.ID;
		}

		boolean test() {
			return true;
		}
	}
}