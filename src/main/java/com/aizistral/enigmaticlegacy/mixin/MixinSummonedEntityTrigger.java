package com.aizistral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.enigmaticlegacy.api.events.SummonedEntityEvent;

import net.minecraft.advancements.critereon.SummonedEntityTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;

@Mixin(SummonedEntityTrigger.class)
public class MixinSummonedEntityTrigger {

	@Inject(method = "trigger", at = @At("HEAD"))
	private void onTrigger(ServerPlayer player, Entity entity, CallbackInfo info) {
		MinecraftForge.EVENT_BUS.post(new SummonedEntityEvent(player, entity));
	}

}
