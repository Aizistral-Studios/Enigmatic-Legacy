package com.aizistral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.aizistral.enigmaticlegacy.handlers.EnigmaticEventHandler;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;

@Mixin(MobEffect.class)
public class MixinMobEffect {

	@Redirect(method = "applyEffectTick", at = @At(value = "INVOKE", target =
			"Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z",
			ordinal = 0))
	private boolean onHurt(LivingEntity entity, DamageSource source, float amount) {
		EnigmaticEventHandler.isPoisonHurt = true;
		boolean result = entity.hurt(source, amount);
		EnigmaticEventHandler.isPoisonHurt = false;
		return result;
	}

}
