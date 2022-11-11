package com.aizistral.enigmaticlegacy.helpers;

import java.util.Optional;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

public class AdvancedSpawnLocationHelper {

	public static ResourceKey<Level> getPlayerRespawnDimension(ServerPlayer player) {
		return player.getRespawnDimension();
	}

	public static BlockPos getRespawnLocation(ServerLevel world, int arg1, int arg2, boolean flag) {
		return getOverworldRespawnPos(world, arg1, arg2, flag);
	}

	private static int someNumberLol(int p_205735_1_) {
		return p_205735_1_ <= 16 ? p_205735_1_ - 1 : 17;
	}

	public static void fuckBackToSpawn(ServerLevel worldIn, ServerPlayer playerIn) {
		BlockPos blockpos = worldIn.getSharedSpawnPos();

		playerIn.teleportTo(blockpos.getX() + 0.5, blockpos.getY(), blockpos.getZ() + 0.5);

		if (worldIn.dimensionType().hasSkyLight() && worldIn.getServer().getWorldData().getGameType() != GameType.ADVENTURE) {
			int i = Math.max(0, worldIn.getServer().getSpawnRadius(worldIn));
			int j = Mth.floor(worldIn.getWorldBorder().getDistanceToBorder(blockpos.getX(), blockpos.getZ()));
			if (j < i) {
				i = j;
			}

			if (j <= 1) {
				i = 1;
			}

			long k = i * 2 + 1;
			long l = k * k;
			int i1 = l > 2147483647L ? Integer.MAX_VALUE : (int) l;
			int j1 = AdvancedSpawnLocationHelper.someNumberLol(i1);
			int k1 = (new Random()).nextInt(i1);

			for (int l1 = 0; l1 < i1; ++l1) {
				int i2 = (k1 + j1 * l1) % i1;
				int j2 = i2 % (i * 2 + 1);
				int k2 = i2 / (i * 2 + 1);
				BlockPos blockpos1 = AdvancedSpawnLocationHelper.getRespawnLocation(worldIn, blockpos.getX() + j2 - i, blockpos.getZ() + k2 - i, false);
				if (blockpos1 != null) {
					playerIn.teleportTo(blockpos1.getX() + 0.5, blockpos1.getY(), blockpos1.getZ() + 0.5);
					if (worldIn.noCollision(playerIn)) {
						break;
					}
				}
			}
		} else {

			while (!worldIn.noCollision(playerIn) && playerIn.getY() < 255.0D) {
				playerIn.teleportTo(playerIn.getX(), playerIn.getY() + 1.0D, playerIn.getZ());
			}
		}

		while (!worldIn.noCollision(playerIn) && playerIn.getY() < 256.0D) {
			playerIn.teleportTo(playerIn.getX(), playerIn.getY() + 1.0D, playerIn.getZ());
		}

	}

	/**
	 * Retrieves the player respawn location.
	 * @return I don't know anymore.
	 */

	public static Optional<Vec3> getValidSpawn(final ServerLevel world, final ServerPlayer player) {
		BlockPos blockpos = player.getRespawnPosition();
		Optional<Vec3> optional;
		if (world != null && blockpos != null) {
			optional = Player.findRespawnPositionAndUseSpawnBlock(world, blockpos, player.getRespawnAngle(), player.isRespawnForced(), false);
			/*player.findRespawnPositionAndUseSpawnBlock(world, blockpos, player.getRespawnAngle(), player.isRespawnForced(), false)*/
		} else {
			optional = Optional.empty();
		}

		return optional;
	}

	@Nullable
	protected static BlockPos getOverworldRespawnPos(ServerLevel p_241092_0_, int p_241092_1_, int p_241092_2_, boolean p_241092_3_) {
		BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos(p_241092_1_, 0, p_241092_2_);
		Biome biome = p_241092_0_.getBiome(blockpos$mutable).value();
		boolean flag = p_241092_0_.dimensionType().hasCeiling();
		LevelChunk chunk = p_241092_0_.getChunk(p_241092_1_ >> 4, p_241092_2_ >> 4);
		int i = flag ? p_241092_0_.getChunkSource().getGenerator().getSpawnHeight(p_241092_0_) : chunk.getHeight(Heightmap.Types.MOTION_BLOCKING, p_241092_1_ & 15, p_241092_2_ & 15);
		if (i < 0)
			return null;
		else {
			int j = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, p_241092_1_ & 15, p_241092_2_ & 15);
			if (j <= i && j > chunk.getHeight(Heightmap.Types.OCEAN_FLOOR, p_241092_1_ & 15, p_241092_2_ & 15))
				return null;
			else {
				for(int k = i + 1; k >= 0; --k) {
					blockpos$mutable.set(p_241092_1_, k, p_241092_2_);
					BlockState blockstate = p_241092_0_.getBlockState(blockpos$mutable);
					if (!blockstate.getFluidState().isEmpty()) {
						break;
					}

					if (Block.isFaceFull(blockstate.getCollisionShape(p_241092_0_, blockpos$mutable), Direction.UP))
						return blockpos$mutable.above().immutable();
				}

				return null;
			}
		}

	}

	@Nullable
	public static BlockPos getSpawnPosInChunk(ServerLevel p_241094_0_, ChunkPos p_241094_1_, boolean p_241094_2_) {
		for(int i = p_241094_1_.getMinBlockX(); i <= p_241094_1_.getMaxBlockX(); ++i) {
			for(int j = p_241094_1_.getMinBlockZ(); j <= p_241094_1_.getMaxBlockZ(); ++j) {
				BlockPos blockpos = getOverworldRespawnPos(p_241094_0_, i, j, p_241094_2_);
				if (blockpos != null)
					return blockpos;
			}
		}

		return null;
	}

}