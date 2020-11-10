package com.integral.enigmaticlegacy.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.ImmutableList;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.PiglinMobsSensor;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.world.server.ServerWorld;

@Mixin(PiglinMobsSensor.class)
public class MixinPiglinMobsSensor {

	@Inject(at = @At("RETURN"), method = "update")
	protected void onPiglinSenses(ServerWorld worldIn, LivingEntity entityIn, CallbackInfo info) {
		Brain<?> brain = entityIn.getBrain();
		PlayerEntity player = brain.getMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD).orElse(null);

		if (player != null) {
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.gemRing)) {
				brain.setMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, Optional.empty());

				// Cycle through visible mobs again in order to replace removed player,
				// since there might be other players nearby not wearing gold or ring
				for(LivingEntity livingentity : brain.getMemory(MemoryModuleType.VISIBLE_MOBS).orElse(ImmutableList.of())) {
					if (livingentity instanceof PlayerEntity) {
						PlayerEntity playerentity = (PlayerEntity)livingentity;

						if (EntityPredicates.CAN_HOSTILE_AI_TARGET.test(livingentity) &&
								!PiglinTasks.func_234460_a_(playerentity) &&
								!SuperpositionHandler.hasCurio(playerentity, EnigmaticLegacy.gemRing)) {

							brain.setMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, Optional.of(playerentity));
						}
					}
				}
			}
		}
	}

}
