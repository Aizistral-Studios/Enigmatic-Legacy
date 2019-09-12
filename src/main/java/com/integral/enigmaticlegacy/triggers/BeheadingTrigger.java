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
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;

/**
 * Special trigger that activates if player successfully beheads a mob with Axe of Executioner.
 * @author Integral
 */

public class BeheadingTrigger implements ICriterionTrigger<BeheadingTrigger.Instance> {
    public static final ResourceLocation ID = new ResourceLocation(EnigmaticLegacy.MODID, "forbidden_axe_beheading");
    public static final BeheadingTrigger INSTANCE = new BeheadingTrigger();
    private final Map<PlayerAdvancements, PlayerTracker> playerTrackers = new HashMap<>();

    private BeheadingTrigger() {
    	// Insert existential void here
    }

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public void addListener(@Nonnull PlayerAdvancements player, @Nonnull ICriterionTrigger.Listener<BeheadingTrigger.Instance> listener) {
        this.playerTrackers.computeIfAbsent(player, PlayerTracker::new).listeners.add(listener);
    }

    @Override
    public void removeListener(@Nonnull PlayerAdvancements player, @Nonnull ICriterionTrigger.Listener<BeheadingTrigger.Instance> listener) {
        PlayerTracker tracker = this.playerTrackers.get(player);

        if(tracker != null) {
            tracker.listeners.remove(listener);

            if(tracker.listeners.isEmpty()) {
                this.playerTrackers.remove(player);
            }
        }
    }

    @Override
    public void removeAllListeners(@Nonnull PlayerAdvancements player) {
        playerTrackers.remove(player);
    }

    @Nonnull
    @Override
    public Instance deserializeInstance(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
        return new Instance();
    }

    static class PlayerTracker {
        private final PlayerAdvancements playerAdvancements;
        final Set<ICriterionTrigger.Listener<Instance>> listeners = new HashSet<>();

        PlayerTracker(PlayerAdvancements playerAdvancementsIn) {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public void trigger() {
            List<ICriterionTrigger.Listener<Instance>> list = new ArrayList<>();

            for(ICriterionTrigger.Listener<BeheadingTrigger.Instance> listener : this.listeners) {
                if(listener.getCriterionInstance().test()) {
                    list.add(listener);
                }
            }

            for(ICriterionTrigger.Listener<BeheadingTrigger.Instance> listener : list) {
                listener.grantCriterion(this.playerAdvancements);
            }
        }
    }

    public void trigger(ServerPlayerEntity player) {
        PlayerTracker tracker = playerTrackers.get(player.getAdvancements());
        if(tracker != null) {
            tracker.trigger();
        }
    }

    static class Instance implements ICriterionInstance {

        Instance() {
        }

        @Nonnull
        @Override
        public ResourceLocation getId() {
            return ID;
        }

        boolean test() {
            return true;
        }
    }
}
