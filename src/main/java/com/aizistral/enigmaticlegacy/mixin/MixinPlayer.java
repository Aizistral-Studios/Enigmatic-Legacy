package com.aizistral.enigmaticlegacy.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.enigmaticlegacy.items.EndAnchor;
import com.aizistral.enigmaticlegacy.items.InfernalShield;
import com.aizistral.enigmaticlegacy.objects.AnchorSearchResult;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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

	@Inject(method = "findRespawnPositionAndUseSpawnBlock", at = @At("HEAD"), cancellable = true)
	private static void onFindRespawnPositionAndUseSpawnBlock(ServerLevel level, BlockPos pos,
			float angle, boolean forced, boolean keep, CallbackInfoReturnable<Optional<Vec3>> info) {
		AnchorSearchResult result = EndAnchor.findAndUseEndAnchor(level, pos, angle, forced, keep);

		if (result.found()) {
			info.setReturnValue(result.location());
		}
	}

}
