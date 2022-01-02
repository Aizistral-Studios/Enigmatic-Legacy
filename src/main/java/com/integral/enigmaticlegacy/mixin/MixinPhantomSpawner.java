package com.integral.enigmaticlegacy.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ILivingEntityData;
import net.minecraft.world.entity.SpawnReason;
import net.minecraft.world.entity.monster.PhantomEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ServerPlayer;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.ServerStatisticsManager;
import net.minecraft.stats.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.GameRules;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.PhantomSpawner;
import net.minecraft.world.spawner.WorldEntitySpawner;

@Mixin(PhantomSpawner.class)
public class MixinPhantomSpawner {
	private int ticksUntilSpawn = 0;

	@Inject(at = @At("RETURN"), method = "tick", cancellable = true)
	private void onHandlePhantomSpawns(ServerWorld world, boolean p_230253_2_, boolean p_230253_3_, CallbackInfoReturnable<Integer> info) {
		if (!p_230253_2_) {
			// NO-OP
		} else if (!world.getGameRules().getBoolean(GameRules.RULE_DOINSOMNIA)) {
			// NO-OP
		} else {
			Random random = world.random;
			--this.ticksUntilSpawn;
			if (this.ticksUntilSpawn > 0) {
				// NO-OP
			} else {
				this.ticksUntilSpawn += (60 + random.nextInt(60)) * 20;
				if (world.getSkyDarken() < 5 && world.dimensionType().hasSkyLight()) {
					// NO-OP
				} else {
					int i = 0;

					for(ServerPlayer player : world.players()) {
						if (!player.isSpectator() && !player.isCreative()) {
							BlockPos blockpos = player.blockPosition();
							if (!world.dimensionType().hasSkyLight() || blockpos.getY() >= world.getSeaLevel() && world.canSeeSky(blockpos)) {
								DifficultyInstance difficulty = world.getCurrentDifficultyAt(blockpos);
								if (difficulty.isHarderThan(random.nextFloat() * 3.0F)) {
									ServerStatisticsManager serverstatisticsmanager = player.getStats();
									int ticksSinceRest = MathHelper.clamp(serverstatisticsmanager.getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST)), 1, Integer.MAX_VALUE);

									if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.cursedRing))
										if (random.nextInt(ticksSinceRest) <= 72000) {
											BlockPos blockpos1 = blockpos.above(20 + random.nextInt(15)).east(-10 + random.nextInt(21)).south(-10 + random.nextInt(21));
											BlockState blockstate = world.getBlockState(blockpos1);
											FluidState fluidstate = world.getFluidState(blockpos1);
											if (WorldEntitySpawner.isValidEmptySpawnBlock(world, blockpos1, blockstate, fluidstate, EntityType.PHANTOM)) {
												ILivingEntityData ilivingentitydata = null;
												int l = 1 + random.nextInt(difficulty.getDifficulty().getId() + 1);

												for(int i1 = 0; i1 < l; ++i1) {
													PhantomEntity phantomentity = EntityType.PHANTOM.create(world);
													phantomentity.moveTo(blockpos1, 0.0F, 0.0F);
													ilivingentitydata = phantomentity.finalizeSpawn(world, difficulty, SpawnReason.NATURAL, ilivingentitydata, (CompoundNBT)null);
													world.addFreshEntityWithPassengers(phantomentity);
												}

												i += l;
											}
										}
								}
							}
						}
					}

					info.setReturnValue(info.getReturnValue()+i);
				}
			}
		}
	}

}
