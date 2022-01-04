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

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.brain.Brain;
import net.minecraft.world.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.brain.sensor.PiglinMobsSensor;
import net.minecraft.world.entity.monster.piglin.PiglinTasks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.EntityPredicates;
import net.minecraft.server.level.ServerLevel;

@Mixin(PiglinMobsSensor.class)
public class MixinPiglinMobsSensor {

	@Inject(at = @At("RETURN"), method = "doTick")
	protected void onPiglinSenses(ServerLevel worldIn, LivingEntity entityIn, CallbackInfo info) {
		Brain<?> brain = entityIn.getBrain();
		Player player = brain.getMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD).orElse(null);

		if (player != null) {
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.gemRing)) {
				brain.setMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, Optional.empty());

				// Cycle through visible mobs again in order to replace removed player,
				// since there might be other players nearby not wearing gold or ring
				for(LivingEntity livingentity : brain.getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).orElse(ImmutableList.of())) {
					if (livingentity instanceof Player) {
						Player Player = (Player)livingentity;

						if (EntityPredicates.ATTACK_ALLOWED.test(livingentity) &&
								!PiglinTasks.isWearingGold(Player) &&
								!SuperpositionHandler.hasCurio(Player, EnigmaticLegacy.gemRing)) {

							brain.setMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, Optional.of(Player));
						}
					}
				}
			}
		}
	}

}
