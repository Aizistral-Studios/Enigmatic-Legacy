package com.integral.enigmaticlegacy.objects;

import java.util.Collections;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class WitherExplosion extends Explosion {

	private final boolean causesFire;
	private final Explosion.Mode mode;
	private final Random random = new Random();
	private final World world;
	private final double x;
	private final double y;
	private final double z;
	@Nullable
	private final Entity exploder;
	private final float size;

	public WitherExplosion(World worldIn, Entity exploderIn, double xIn, double yIn, double zIn, float sizeIn, boolean causesFireIn, Mode modeIn) {
		super(worldIn, exploderIn, xIn, yIn, zIn, sizeIn, causesFireIn, modeIn);

		this.world = worldIn;
	      this.exploder = exploderIn;
	      this.size = sizeIn;
	      this.x = xIn;
	      this.y = yIn;
	      this.z = zIn;
	      this.causesFire = causesFireIn;
	      this.mode = modeIn;
	      DamageSource.causeExplosionDamage(this);
	      new Vector3d(this.x, this.y, this.z);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void doExplosionB(boolean spawnParticles) {
		if (this.world.isRemote) {
			this.world.playSound(this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F, false);
		}

		boolean flag = this.mode != Explosion.Mode.NONE;
		if (spawnParticles) {
			if (!(this.size < 2.0F) && flag) {
				this.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
			} else {
				this.world.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
			}
		}

		if (flag) {
			Collections.shuffle(super.getAffectedBlockPositions(), this.world.rand);

			for (BlockPos blockpos : super.getAffectedBlockPositions()) {
				BlockState blockstate = this.world.getBlockState(blockpos);
				if (!blockstate.isAir(this.world, blockpos)) {
					this.world.getProfiler().startSection("explosion_blocks");

					blockstate.onBlockExploded(this.world, blockpos, this);
					this.world.getProfiler().endSection();
				}
			}
		}

		if (this.causesFire) {
			for (BlockPos blockpos2 : super.getAffectedBlockPositions()) {
				if (this.random.nextInt(3) == 0 && this.world.getBlockState(blockpos2).isAir() && this.world.getBlockState(blockpos2.down()).isOpaqueCube(this.world, blockpos2.down())) {
					this.world.setBlockState(blockpos2, Blocks.FIRE.getDefaultState());
				}
			}
		}

	}

}
