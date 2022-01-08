package com.integral.enigmaticlegacy.triggers;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.EntitySelector;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.resources.ResourceLocation;

public class CursedRingEquippedTrigger extends SimpleCriterionTrigger<CursedRingEquippedTrigger.Instance> {
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
	public CursedRingEquippedTrigger.Instance createInstance(@Nonnull JsonObject json, @Nonnull EntitySelector.Composite playerPred, DeserializationContext conditions) {
		return new CursedRingEquippedTrigger.Instance(playerPred);
	}

	public void trigger(ServerPlayer player) {
		this.trigger(player, instance -> instance.test());
	}

	static class Instance extends AbstractCriterionTriggerInstance {
		Instance(EntitySelector.Composite playerPred) {
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
