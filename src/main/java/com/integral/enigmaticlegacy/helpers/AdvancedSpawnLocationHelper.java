package com.integral.enigmaticlegacy.helpers;

import java.util.Optional;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.SpawnLocationHelper;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;

public class AdvancedSpawnLocationHelper {

	public static RegistryKey<World> getPlayerRespawnDimension(ServerPlayerEntity player) {
		return player.getRespawnDimension();
	}

	public static BlockPos getRespawnLocation(ServerWorld world, int arg1, int arg2, boolean flag) {
		return getOverworldRespawnPos(world, arg1, arg2, flag);
	}

	private static int someNumberLol(int p_205735_1_) {
		return p_205735_1_ <= 16 ? p_205735_1_ - 1 : 17;
	}

	public static void fuckBackToSpawn(ServerWorld worldIn, ServerPlayerEntity playerIn) {
		BlockPos blockpos = worldIn.getSharedSpawnPos();

		playerIn.teleportTo(blockpos.getX() + 0.5, blockpos.getY(), blockpos.getZ() + 0.5);

		if (worldIn.dimensionType().hasSkyLight() && worldIn.getServer().getWorldData().getGameType() != GameType.ADVENTURE) {
			int i = Math.max(0, worldIn.getServer().getSpawnRadius(worldIn));
			int j = MathHelper.floor(worldIn.getWorldBorder().getDistanceToBorder(blockpos.getX(), blockpos.getZ()));
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

	public static Optional<Vector3d> getValidSpawn(final ServerWorld world, final ServerPlayerEntity player) {
		BlockPos blockpos = player.getRespawnPosition();
		Optional<Vector3d> optional;
		if (world != null && blockpos != null) {
			optional = PlayerEntity.findRespawnPositionAndUseSpawnBlock(world, blockpos, player.getRespawnAngle(), player.isRespawnForced(), false);
			/*player.findRespawnPositionAndUseSpawnBlock(world, blockpos, player.getRespawnAngle(), player.isRespawnForced(), false)*/
		} else {
			optional = Optional.empty();
		}

		return optional;
	}

	@Nullable
	protected static BlockPos getOverworldRespawnPos(ServerWorld p_241092_0_, int p_241092_1_, int p_241092_2_, boolean p_241092_3_) {
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(p_241092_1_, 0, p_241092_2_);
		Biome biome = p_241092_0_.getBiome(blockpos$mutable);
		boolean flag = p_241092_0_.dimensionType().hasCeiling();
		BlockState blockstate = biome.getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial();
		if (p_241092_3_ && !blockstate.getBlock().is(BlockTags.VALID_SPAWN))
			return null;
		else {
			Chunk chunk = p_241092_0_.getChunk(p_241092_1_ >> 4, p_241092_2_ >> 4);
			int i = flag ? p_241092_0_.getChunkSource().getGenerator().getSpawnHeight() : chunk.getHeight(Heightmap.Type.MOTION_BLOCKING, p_241092_1_ & 15, p_241092_2_ & 15);
			if (i < 0)
				return null;
			else {
				int j = chunk.getHeight(Heightmap.Type.WORLD_SURFACE, p_241092_1_ & 15, p_241092_2_ & 15);
				if (j <= i && j > chunk.getHeight(Heightmap.Type.OCEAN_FLOOR, p_241092_1_ & 15, p_241092_2_ & 15))
					return null;
				else {
					for(int k = i + 1; k >= 0; --k) {
						blockpos$mutable.set(p_241092_1_, k, p_241092_2_);
						BlockState blockstate1 = p_241092_0_.getBlockState(blockpos$mutable);
						if (!blockstate1.getFluidState().isEmpty()) {
							break;
						}

						if (blockstate1.equals(blockstate))
							return blockpos$mutable.above().immutable();
					}

					return null;
				}
			}
		}
	}

	@Nullable
	public static BlockPos getSpawnPosInChunk(ServerWorld p_241094_0_, ChunkPos p_241094_1_, boolean p_241094_2_) {
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