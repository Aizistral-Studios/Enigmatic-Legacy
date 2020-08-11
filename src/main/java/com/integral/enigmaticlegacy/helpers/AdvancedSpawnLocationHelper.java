package com.integral.enigmaticlegacy.helpers;

import java.util.Optional;
import java.util.Random;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.SpawnLocationHelper;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class AdvancedSpawnLocationHelper extends SpawnLocationHelper {

	public static RegistryKey<World> getPlayerRespawnDimension(ServerPlayerEntity player) {
		return player.func_241141_L_();
	}

	public static BlockPos getRespawnLocation(ServerWorld world, int arg1, int arg2, boolean flag) {
		return SpawnLocationHelper.func_241092_a_(world, arg1, arg2, flag);
	}

	private static int someNumberLol(int p_205735_1_) {
		return p_205735_1_ <= 16 ? p_205735_1_ - 1 : 17;
	}

	public static void fuckBackToSpawn(ServerWorld worldIn, ServerPlayerEntity playerIn) {
		BlockPos blockpos = worldIn.func_241135_u_();

		playerIn.setPositionAndUpdate(blockpos.getX()+0.5, blockpos.getY(), blockpos.getZ()+0.5);

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
					playerIn.setPositionAndUpdate(blockpos1.getX()+0.5, blockpos1.getY(), blockpos1.getZ()+0.5);
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
	         optional = PlayerEntity.func_234567_a_(world, blockpos, player.func_241142_M_(), false);
	      } else {
	         optional = Optional.empty();
	      }

		return optional;
	}

}
