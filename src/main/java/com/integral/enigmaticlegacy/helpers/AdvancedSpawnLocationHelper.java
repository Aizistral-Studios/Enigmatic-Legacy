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
		return player.func_241141_L_();
	}

	public static BlockPos getRespawnLocation(ServerWorld world, int arg1, int arg2, boolean flag) {
		return func_241092_a_(world, arg1, arg2, flag);
	}

	private static int someNumberLol(int p_205735_1_) {
		return p_205735_1_ <= 16 ? p_205735_1_ - 1 : 17;
	}

	public static void fuckBackToSpawn(ServerWorld worldIn, ServerPlayerEntity playerIn) {
		BlockPos blockpos = worldIn.func_241135_u_();

		playerIn.setPositionAndUpdate(blockpos.getX() + 0.5, blockpos.getY(), blockpos.getZ() + 0.5);

		if (worldIn.func_230315_m_().hasSkyLight() && worldIn.getServer().func_240793_aU_().getGameType() != GameType.ADVENTURE) {
			int i = Math.max(0, worldIn.getServer().getSpawnRadius(worldIn));
			int j = MathHelper.floor(worldIn.getWorldBorder().getClosestDistance(blockpos.getX(), blockpos.getZ()));
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
					playerIn.setPositionAndUpdate(blockpos1.getX() + 0.5, blockpos1.getY(), blockpos1.getZ() + 0.5);
					if (worldIn.hasNoCollisions(playerIn)) {
						break;
					}
				}
			}
		} else {

			while (!worldIn.hasNoCollisions(playerIn) && playerIn.getPosY() < 255.0D) {
				playerIn.setPositionAndUpdate(playerIn.getPosX(), playerIn.getPosY() + 1.0D, playerIn.getPosZ());
			}
		}

		while (!worldIn.hasNoCollisions(playerIn) && playerIn.getPosY() < 256.0D) {
			playerIn.setPositionAndUpdate(playerIn.getPosX(), playerIn.getPosY() + 1.0D, playerIn.getPosZ());
		}

	}

	/**
	 * Retrieves the player respawn location.
	 * @return I don't know anymore.
	 */

	public static Optional<Vector3d> getValidSpawn(final ServerWorld world, final ServerPlayerEntity player) {
		BlockPos blockpos = player.func_241140_K_();
		Optional<Vector3d> optional;
		if (world != null && blockpos != null) {
			optional = PlayerEntity.func_242374_a(world, blockpos, player.func_242109_L(), player.func_241142_M_(), false);
			/*player.func_242374_a(world, blockpos, player.func_242109_L(), player.func_241142_M_(), false)*/
		} else {
			optional = Optional.empty();
		}

		return optional;
	}

	@Nullable
	protected static BlockPos func_241092_a_(ServerWorld p_241092_0_, int p_241092_1_, int p_241092_2_, boolean p_241092_3_) {
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(p_241092_1_, 0, p_241092_2_);
		Biome biome = p_241092_0_.getBiome(blockpos$mutable);
		boolean flag = p_241092_0_.func_230315_m_().func_236037_d_();
		BlockState blockstate = biome.func_242440_e().func_242502_e().getTop();
		if (p_241092_3_ && !blockstate.getBlock().isIn(BlockTags.VALID_SPAWN))
			return null;
		else {
			Chunk chunk = p_241092_0_.getChunk(p_241092_1_ >> 4, p_241092_2_ >> 4);
			int i = flag ? p_241092_0_.getChunkProvider().getChunkGenerator().getGroundHeight() : chunk.getTopBlockY(Heightmap.Type.MOTION_BLOCKING, p_241092_1_ & 15, p_241092_2_ & 15);
			if (i < 0)
				return null;
			else {
				int j = chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE, p_241092_1_ & 15, p_241092_2_ & 15);
				if (j <= i && j > chunk.getTopBlockY(Heightmap.Type.OCEAN_FLOOR, p_241092_1_ & 15, p_241092_2_ & 15))
					return null;
				else {
					for(int k = i + 1; k >= 0; --k) {
						blockpos$mutable.setPos(p_241092_1_, k, p_241092_2_);
						BlockState blockstate1 = p_241092_0_.getBlockState(blockpos$mutable);
						if (!blockstate1.getFluidState().isEmpty()) {
							break;
						}

						if (blockstate1.equals(blockstate))
							return blockpos$mutable.up().toImmutable();
					}

					return null;
				}
			}
		}
	}

	@Nullable
	public static BlockPos func_241094_a_(ServerWorld p_241094_0_, ChunkPos p_241094_1_, boolean p_241094_2_) {
		for(int i = p_241094_1_.getXStart(); i <= p_241094_1_.getXEnd(); ++i) {
			for(int j = p_241094_1_.getZStart(); j <= p_241094_1_.getZEnd(); ++j) {
				BlockPos blockpos = func_241092_a_(p_241094_0_, i, j, p_241094_2_);
				if (blockpos != null)
					return blockpos;
			}
		}

		return null;
	}

}