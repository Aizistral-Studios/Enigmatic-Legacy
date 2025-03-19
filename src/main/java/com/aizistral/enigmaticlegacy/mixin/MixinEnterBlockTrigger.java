package com.aizistral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.enigmaticlegacy.api.events.EnterBlockEvent;

import net.minecraft.advancements.critereon.EnterBlockTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;

@Mixin(EnterBlockTrigger.class)
public class MixinEnterBlockTrigger {

	@Inject(method = "trigger", at = @At("HEAD"))
	private void onTrigger(ServerPlayer player, BlockState state, CallbackInfo info) {
		MinecraftForge.EVENT_BUS.post(new EnterBlockEvent(player, state));
	}

}
