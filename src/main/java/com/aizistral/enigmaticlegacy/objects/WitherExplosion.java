package com.aizistral.enigmaticlegacy.objects;

import java.util.Collections;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class WitherExplosion extends Explosion {
	private final boolean causesFire;
	private final Explosion.BlockInteraction mode;
	private final Random random = new Random();
	private final Level world;
	private final double x;
	private final double y;
	private final double z;
	@Nullable
	private final Entity exploder;
	private final float size;

	public WitherExplosion(Level worldIn, Entity exploderIn, double xIn, double yIn, double zIn, float sizeIn, boolean causesFireIn, BlockInteraction modeIn) {
		super(worldIn, exploderIn, (DamageSource)null, (ExplosionDamageCalculator)null, xIn, yIn, zIn, sizeIn, causesFireIn, modeIn);

		this.world = worldIn;
		this.exploder = exploderIn;
		this.size = sizeIn;
		this.x = xIn;
		this.y = yIn;
		this.z = zIn;
		this.causesFire = causesFireIn;
		this.mode = modeIn;
		new Vec3(this.x, this.y, this.z);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void finalizeExplosion(boolean spawnParticles) {
		if (this.world.isClientSide) {
			this.world.playLocalSound(this.x, this.y, this.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F) * 0.7F, false);
		}

		boolean flag = this.mode != Explosion.BlockInteraction.KEEP;
		if (spawnParticles) {
			if (!(this.size < 2.0F) && flag) {
				this.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
			} else {
				this.world.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
			}
		}

		if (flag) {
			Collections.shuffle(super.getToBlow());

			for (BlockPos blockpos : super.getToBlow()) {
				BlockState blockstate = this.world.getBlockState(blockpos);
				if (!blockstate.isAir()) {
					this.world.getProfiler().push("explosion_blocks");

					blockstate.onBlockExploded(this.world, blockpos, this);
					this.world.getProfiler().pop();
				}
			}
		}

		if (this.causesFire) {
			for (BlockPos blockpos2 : super.getToBlow()) {
				if (this.random.nextInt(3) == 0 && this.world.getBlockState(blockpos2).isAir() && this.world.getBlockState(blockpos2.below()).isSolidRender(this.world, blockpos2.below())) {
					this.world.setBlockAndUpdate(blockpos2, Blocks.FIRE.defaultBlockState());
				}
			}
		}

	}

}
