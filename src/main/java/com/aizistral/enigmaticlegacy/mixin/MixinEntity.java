package com.aizistral.enigmaticlegacy.mixin;

import com.aizistral.enigmaticlegacy.registries.EnigmaticEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class MixinEntity {

    @Inject(method = "canSpawnSprintParticle", at = @At("HEAD"), cancellable = true)
    private void canSpawnSprintParticle(CallbackInfoReturnable<Boolean> cir) {
        cancelIfImperceptible(cir, false);
    }

    @Inject(method = "displayFireAnimation", at = @At("HEAD"), cancellable = true)
    private void onDisplayFireAnimation(CallbackInfoReturnable<Boolean> cir) {
        cancelIfImperceptible(cir, false);
    }

    @Unique
    private void cancelIfImperceptible(CallbackInfoReturnable<Boolean> cir, boolean returnValue) {
        Entity entity = (Entity) (Object) this;

        if (!(entity instanceof LivingEntity livingEntity)) return;
        if (!livingEntity.hasEffect(EnigmaticEffects.IMPERCEPTIBLE)) return;

        cir.setReturnValue(returnValue);
    }
}
