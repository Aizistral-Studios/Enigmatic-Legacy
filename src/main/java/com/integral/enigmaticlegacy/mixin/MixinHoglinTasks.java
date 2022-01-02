package com.integral.enigmaticlegacy.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.brain.sensor.PiglinMobsSensor;
import net.minecraft.world.entity.monster.HoglinEntity;
import net.minecraft.world.entity.monster.HoglinTasks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.server.ServerWorld;

@Mixin(HoglinTasks.class)
public class MixinHoglinTasks {

	@Inject(at = @At("RETURN"), method = "findNearestValidAttackTarget", cancellable = true)
	private static void onHoglinShallAttack(HoglinEntity hoglin, CallbackInfoReturnable<Optional<? extends LivingEntity>> info) {
		Optional<? extends LivingEntity> returnedTarget = info.getReturnValue();

		if (returnedTarget.isPresent() && returnedTarget.orElse(null) instanceof Player) {
			Player returnedPlayer = (Player) returnedTarget.orElse(null);

			if (SuperpositionHandler.hasItem(returnedPlayer, EnigmaticLegacy.animalGuide)) {
				info.setReturnValue(Optional.empty());
			}
		}
	}
}
