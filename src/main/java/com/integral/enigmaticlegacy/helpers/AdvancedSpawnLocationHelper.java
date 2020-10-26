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
		return SpawnLocationHelper.func_241092_a_(world, arg1, arg2, flag);
	}

	public static void fuckBackToSpawn(ServerWorld worldIn, ServerPlayerEntity playerIn) {
		playerIn.func_205734_a(worldIn);
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

}