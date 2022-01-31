package com.integral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.integral.enigmaticlegacy.items.InfernalShield;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity {

	protected MixinPlayer(EntityType<? extends LivingEntity> type, Level world) {
		super(type, world);
		throw new IllegalStateException("Can't touch this");
	}

	@Inject(method = "disableShield", at = @At("HEAD"), cancellable = true)
	private void onDisableShield(boolean flag, CallbackInfo info) {
		if (this.useItem != null && this.useItem.getItem() instanceof InfernalShield) {
			info.cancel();
		}
	}

}
