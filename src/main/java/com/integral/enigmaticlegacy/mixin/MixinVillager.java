package com.integral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.integral.enigmaticlegacy.handlers.EnigmaticEventHandler;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;

@Mixin(Villager.class)
public class MixinVillager {

	@Inject(method = "trySpawnGolem", at = @At("RETURN"), cancellable = true)
	private void onGolemCreation(ServerLevel level, CallbackInfoReturnable<IronGolem> info) {
		if (info.getReturnValue() != null) {
			IronGolem golem = info.getReturnValue();
			if (EnigmaticEventHandler.desolationBoxes.values().stream().anyMatch(golem.getBoundingBox()::intersects)) {
				info.setReturnValue(null);
			}
		}
	}

}
