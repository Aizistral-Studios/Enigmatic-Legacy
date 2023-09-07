package com.aizistral.enigmaticlegacy.mixin;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.enigmaticlegacy.items.EndAnchor;
import com.aizistral.enigmaticlegacy.objects.AnchorSearchResult;
import com.aizistral.enigmaticlegacy.registries.EnigmaticBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundSetDefaultSpawnPositionPacket;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.Vec3;

@Mixin(PlayerList.class)
public class MixinPlayerList {
	@Shadow @Final
	private MinecraftServer server;
	@Shadow @Final
	private List<ServerPlayer> players;
	@Shadow @Final
	private Map<UUID, ServerPlayer> playersByUUID;

	@Inject(method = "respawn", at = @At("HEAD"), cancellable = true)
	private void respawn(ServerPlayer player, boolean keep, CallbackInfoReturnable<ServerPlayer> info) {
		BlockPos pos = player.getRespawnPosition();
		float angle = player.getRespawnAngle();
		boolean forced = player.isRespawnForced();
		boolean override = false;
		boolean endRespawn = false;
		ServerLevel level = this.server.getLevel(player.getRespawnDimension());
		Optional<Vec3> optional = Optional.empty();

		if (level != null && pos != null) {
			AnchorSearchResult result = EndAnchor.findEndAnchor(level, pos, angle, forced, keep);

			if (result.found()) {
				optional = result.location();
				override = true;

				if (keep && level.dimension() == Level.END && optional.isPresent()) {
					endRespawn = true;
					optional = Optional.empty();
				}
			}
		}

		if (!override)
			return;

		this.players.remove(player);
		player.serverLevel().removePlayerImmediately(player, Entity.RemovalReason.DISCARDED);
		PlayerList list = (PlayerList) (Object) this;

		ServerLevel newLevel = level != null && optional.isPresent() ? level : this.server.overworld();
		ServerPlayer newPlayer = new ServerPlayer(this.server, newLevel, player.getGameProfile());
		newPlayer.connection = player.connection;
		newPlayer.restoreFrom(player, keep);
		newPlayer.setId(player.getId());
		newPlayer.setMainArm(player.getMainArm());

		for (String tag : player.getTags()) {
			newPlayer.addTag(tag);
		}

		boolean usedAnchorCharge = false;
		if (optional.isPresent()) {
			BlockState respawnState = newLevel.getBlockState(pos);
			boolean isAnchor = respawnState.is(Blocks.RESPAWN_ANCHOR) || respawnState.is(EnigmaticBlocks.END_ANCHOR);
			Vec3 vec3 = optional.get();
			float f1;
			if (!respawnState.is(BlockTags.BEDS) && !isAnchor) {
				f1 = angle;
			} else {
				Vec3 vec31 = Vec3.atBottomCenterOf(pos).subtract(vec3).normalize();
				f1 = (float) Mth.wrapDegrees(Mth.atan2(vec31.z, vec31.x) * (180F / (float) Math.PI) - 90.0D);
			}

			newPlayer.moveTo(vec3.x, vec3.y, vec3.z, f1, 0.0F);
			newPlayer.setRespawnPosition(newLevel.dimension(), pos, angle, forced, false);
			usedAnchorCharge = !keep && isAnchor && EndAnchor.useEndAnchor(newLevel, pos, respawnState);
		} else if (pos != null && !endRespawn) {
			newPlayer.connection.send(new ClientboundGameEventPacket(
					ClientboundGameEventPacket.NO_RESPAWN_BLOCK_AVAILABLE, 0.0F));
		} else if (pos != null && endRespawn) {
			newPlayer.setRespawnPosition(Level.END, pos, angle, forced, false);
		}

		while (!newLevel.noCollision(newPlayer) && newPlayer.getY() < newLevel.getMaxBuildHeight()) {
			newPlayer.setPos(newPlayer.getX(), newPlayer.getY() + 1.0D, newPlayer.getZ());
		}

		byte keepByte = (byte) (keep ? 1 : 0);
		LevelData leveldata = newPlayer.level().getLevelData();
		newPlayer.connection.send(new ClientboundRespawnPacket(newPlayer.level().dimensionTypeId(),
				newPlayer.level().dimension(), BiomeManager.obfuscateSeed(newPlayer.serverLevel().getSeed()),
				newPlayer.gameMode.getGameModeForPlayer(), newPlayer.gameMode.getPreviousGameModeForPlayer(),
				newPlayer.level().isDebug(), newPlayer.serverLevel().isFlat(), keepByte, newPlayer.getLastDeathLocation(),
				newPlayer.getPortalCooldown()));
		newPlayer.connection.teleport(newPlayer.getX(), newPlayer.getY(), newPlayer.getZ(), newPlayer.getYRot(),
				newPlayer.getXRot());
		newPlayer.connection.send(new ClientboundSetDefaultSpawnPositionPacket(newLevel.getSharedSpawnPos(),
				newLevel.getSharedSpawnAngle()));
		newPlayer.connection.send(new ClientboundChangeDifficultyPacket(leveldata.getDifficulty(),
				leveldata.isDifficultyLocked()));
		newPlayer.connection.send(new ClientboundSetExperiencePacket(newPlayer.experienceProgress,
				newPlayer.totalExperience, newPlayer.experienceLevel));
		list.sendLevelInfo(newPlayer, newLevel);
		list.sendPlayerPermissionLevel(newPlayer);
		newLevel.addRespawnedPlayer(newPlayer);
		this.players.add(newPlayer);
		this.playersByUUID.put(newPlayer.getUUID(), newPlayer);
		newPlayer.initInventoryMenu();
		newPlayer.setHealth(newPlayer.getHealth());
		net.minecraftforge.event.ForgeEventFactory.firePlayerRespawnEvent(newPlayer, keep);

		if (usedAnchorCharge && pos != null) {
			newPlayer.connection.send(new ClientboundSoundPacket(SoundEvents.RESPAWN_ANCHOR_DEPLETE, SoundSource.BLOCKS, pos.getX(), pos.getY(), pos.getZ(), 1.0F, 1.0F, newLevel.getRandom().nextLong()));
		}

		info.setReturnValue(newPlayer);
	}

}
