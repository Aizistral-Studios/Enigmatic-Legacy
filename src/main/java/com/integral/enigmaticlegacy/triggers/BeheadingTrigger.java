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

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;

/**
 * Special trigger that activates if player successfully beheads a mob with Axe of Executioner.
 * @author Integral
 */

public class BeheadingTrigger extends AbstractCriterionTrigger<BeheadingTrigger.Instance> {
	public static final ResourceLocation ID = new ResourceLocation(EnigmaticLegacy.MODID, "forbidden_axe_beheading");
	public static final BeheadingTrigger INSTANCE = new BeheadingTrigger();

	private BeheadingTrigger() {}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return BeheadingTrigger.ID;
	}

	@Nonnull
	@Override
	public BeheadingTrigger.Instance func_230241_b_(@Nonnull JsonObject json, @Nonnull EntityPredicate.AndPredicate playerPred, ConditionArrayParser conditions) {
		return new BeheadingTrigger.Instance(playerPred);
	}

	public void trigger(ServerPlayerEntity player) {
		this.func_235959_a_(player, instance -> instance.test());
	}

	static class Instance extends CriterionInstance {
		Instance(EntityPredicate.AndPredicate playerPred) {
			super(BeheadingTrigger.ID, playerPred);
		}

		@Nonnull
		@Override
		public ResourceLocation getId() {
			return BeheadingTrigger.ID;
		}

		boolean test() {
			return true;
		}
	}

}