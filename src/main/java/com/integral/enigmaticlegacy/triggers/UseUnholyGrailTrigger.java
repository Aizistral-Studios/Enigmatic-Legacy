package com.integral.enigmaticlegacy.triggers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.items.RevelationTome;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

/**
 * Special trigger that activates when player drinks from Unholy Grail.
 * @author Integral
 */

public class UseUnholyGrailTrigger extends AbstractCriterionTrigger<UseUnholyGrailTrigger.Instance> {
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
	public UseUnholyGrailTrigger.Instance deserializeTrigger(@Nonnull JsonObject json, @Nonnull EntityPredicate.AndPredicate playerPred, ConditionArrayParser conditions) {
		return new UseUnholyGrailTrigger.Instance(playerPred);
	}

	public void trigger(ServerPlayerEntity player) {
		this.triggerListeners(player, instance -> instance.test());
	}

	static class Instance extends CriterionInstance {
		Instance(EntityPredicate.AndPredicate playerPred) {
			super(UseUnholyGrailTrigger.ID, playerPred);
		}

		@Nonnull
		@Override
		public ResourceLocation getId() {
			return UseUnholyGrailTrigger.ID;
		}

		boolean test() {
			return true;
		}
	}
}