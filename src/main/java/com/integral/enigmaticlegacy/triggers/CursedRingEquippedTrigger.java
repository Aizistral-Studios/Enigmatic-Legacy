package com.integral.enigmaticlegacy.triggers;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.world.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;

public class CursedRingEquippedTrigger extends AbstractCriterionTrigger<CursedRingEquippedTrigger.Instance> {
	public static final ResourceLocation ID = new ResourceLocation(EnigmaticLegacy.MODID, "equip_cursed_ring");
	public static final CursedRingEquippedTrigger INSTANCE = new CursedRingEquippedTrigger();

	private CursedRingEquippedTrigger() {}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return CursedRingEquippedTrigger.ID;
	}

	@Nonnull
	@Override
	public CursedRingEquippedTrigger.Instance createInstance(@Nonnull JsonObject json, @Nonnull EntityPredicate.AndPredicate playerPred, ConditionArrayParser conditions) {
		return new CursedRingEquippedTrigger.Instance(playerPred);
	}

	public void trigger(ServerPlayerEntity player) {
		this.trigger(player, instance -> instance.test());
	}

	static class Instance extends CriterionInstance {
		Instance(EntityPredicate.AndPredicate playerPred) {
			super(CursedRingEquippedTrigger.ID, playerPred);
		}

		@Nonnull
		@Override
		public ResourceLocation getCriterion() {
			return CursedRingEquippedTrigger.ID;
		}

		boolean test() {
			return true;
		}
	}

}
