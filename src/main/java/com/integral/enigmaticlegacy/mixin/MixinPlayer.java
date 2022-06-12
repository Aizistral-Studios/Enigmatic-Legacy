package com.integral.enigmaticlegacy.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.integral.enigmaticlegacy.blocks.BlockEndAnchor;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.items.EndAnchor;
import com.integral.enigmaticlegacy.items.InfernalShield;
import com.integral.enigmaticlegacy.objects.AnchorSearchResult;
import com.integral.enigmaticlegacy.objects.GlobalState;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
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
