package com.integral.enigmaticlegacy.triggers;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.resources.ResourceLocation;

/**
 * Special trigger that activates when player drinks from Unholy Grail.
 * @author Integral
 */

public class UseUnholyGrailTrigger extends SimpleCriterionTrigger<UseUnholyGrailTrigger.Instance> {
	public static final ResourceLocation ID = new ResourceLocation(EnigmaticLegacy.MODID, "unholy_grail_drink");
	public static final UseUnholyGrailTrigger INSTANCE = new UseUnholyGrailTrigger();

	private UseUnholyGrailTrigger() {}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return UseUnholyGrailTrigger.ID;
	}

	@Nonnull
	@Override
	public UseUnholyGrailTrigger.Instance createInstance(@Nonnull JsonObject json, @Nonnull EntityPredicate.Composite playerPred, DeserializationContext conditions) {
		return new UseUnholyGrailTrigger.Instance(playerPred, GsonHelper.getAsString(json, "is_the_worthy_one", null));
	}

	public void trigger(ServerPlayer player, boolean isTheWorthyOne) {
		this.trigger(player, instance -> instance.test(isTheWorthyOne));
	}

	static class Instance extends AbstractCriterionTriggerInstance {
		private final Boolean isTheWorthyOne;

		Instance(EntityPredicate.Composite playerPred, String isTheWorthyOne) {
			super(UseUnholyGrailTrigger.ID, playerPred);
			this.isTheWorthyOne = isTheWorthyOne != null ? Boolean.parseBoolean(isTheWorthyOne) : null;
		}

		@Nonnull
		@Override
		public ResourceLocation getCriterion() {
			return UseUnholyGrailTrigger.ID;
		}

		boolean test(boolean isTheWorthyOne) {
			return this.isTheWorthyOne != null ? this.isTheWorthyOne.booleanValue() == isTheWorthyOne : true;
		}
	}
}