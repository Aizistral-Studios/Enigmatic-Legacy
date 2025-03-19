package com.aizistral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.items.CursedRing;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import net.minecraft.world.level.material.FluidState;

@Mixin(PhantomSpawner.class)
public class MixinPhantomSpawner {
	private int ticksUntilSpawn = 0;

	@Inject(at = @At("RETURN"), method = "tick", cancellable = true)
	private void onHandlePhantomSpawns(ServerLevel world, boolean p_230253_2_, boolean p_230253_3_, CallbackInfoReturnable<Integer> info) {
		if (!p_230253_2_) {
			// NO-OP
		} else if (!world.getGameRules().getBoolean(GameRules.RULE_DOINSOMNIA)) {
			// NO-OP
		} else {
			var random = world.random;
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
									ServerStatsCounter serverstatisticsmanager = player.getStats();
									int ticksSinceRest = Mth.clamp(serverstatisticsmanager.getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST)), 1, Integer.MAX_VALUE);

									if (SuperpositionHandler.hasCurio(player, EnigmaticItems.CURSED_RING) && !CursedRing.disableInsomnia.getValue())
										if (random.nextInt(ticksSinceRest) <= 72000) {
											BlockPos blockpos1 = blockpos.above(20 + random.nextInt(15)).east(-10 + random.nextInt(21)).south(-10 + random.nextInt(21));
											BlockState blockstate = world.getBlockState(blockpos1);
											FluidState fluidstate = world.getFluidState(blockpos1);
											if (NaturalSpawner.isValidEmptySpawnBlock(world, blockpos1, blockstate, fluidstate, EntityType.PHANTOM)) {
												SpawnGroupData ilivingentitydata = null;
												int l = 1 + random.nextInt(difficulty.getDifficulty().getId() + 1);

												for(int i1 = 0; i1 < l; ++i1) {
													Phantom phantomentity = EntityType.PHANTOM.create(world);
													phantomentity.moveTo(blockpos1, 0.0F, 0.0F);
													ilivingentitydata = phantomentity.finalizeSpawn(world, difficulty, MobSpawnType.NATURAL, ilivingentitydata, (CompoundTag)null);
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
