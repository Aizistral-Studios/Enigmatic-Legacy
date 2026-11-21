package com.aizistral.enigmaticlegacy.effects;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.registries.EnigmaticEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

public class ImperceptibleEffect extends MobEffect {
    public ImperceptibleEffect() {
        super(MobEffectCategory.BENEFICIAL, 10748041);
    }

    @Mod.EventBusSubscriber(modid = EnigmaticLegacy.MODID, value = Dist.CLIENT)
    public static class ClientEvents {

        @SubscribeEvent
        public static void onEntityRenderEvent(RenderLivingEvent.Pre<?, ?> event) {
            if (!isImperceptible(event.getEntity())) return;

            event.setCanceled(true);
        }

        @SubscribeEvent
        public static void onHandRenderEvent(RenderHandEvent event) {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;

            if (player == null) return;

            if (!isImperceptible(player)) return;
            if (!event.getItemStack().isEmpty()) return;

            event.setCanceled(true);
        }
    }

    @Mod.EventBusSubscriber(modid = EnigmaticLegacy.MODID)
    public static class ServerEvents {

        @SubscribeEvent
        public static void onLivingAttackEvent(LivingAttackEvent event) {
            if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) return;
            tryRemoveEffect(attacker);
        }

        @SubscribeEvent
        public static void onPlayerInteractEvent(PlayerInteractEvent event) {
            tryRemoveEffect(event.getEntity());
        }
        
        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void onLivingChangeTargetEvent(LivingChangeTargetEvent event) {
            LivingEntity target = event.getNewTarget();
            
            if (!isImperceptible(target)) return;
            event.setCanceled(true);

            if (event.getEntity() instanceof Mob mob) {
                mob.setTarget(null);
            }
        }
    }

    private static void tryRemoveEffect(@Nullable LivingEntity entity) {
        if (entity == null) return;
        if (entity.level().isClientSide()) return;

        if (!isImperceptible(entity)) return;
        entity.removeEffect(EnigmaticEffects.IMPERCEPTIBLE);
    }

    private static boolean isImperceptible(@Nullable LivingEntity entity) {
        if (entity == null) return false;
        return entity.hasEffect(EnigmaticEffects.IMPERCEPTIBLE);
    }
}
