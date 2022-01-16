package com.integral.enigmaticlegacy.entities;

import java.util.List;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.AOEMiningHelper;
import com.integral.enigmaticlegacy.objects.Vector3;
import com.integral.enigmaticlegacy.objects.WitherExplosion;
import com.integral.enigmaticlegacy.packets.clients.PacketWitherParticles;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SExplosionPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ObjectHolder;

public class UltimateWitherSkullEntity extends DamagingProjectileEntity {
	private static final DataParameter<Boolean> INVULNERABLE = EntityDataManager.defineId(UltimateWitherSkullEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> BOUND_TARGET = EntityDataManager.defineId(UltimateWitherSkullEntity.class, DataSerializers.INT);

	int targetID;
	LivingEntity target;

	@ObjectHolder(EnigmaticLegacy.MODID + ":ultimate_wither_skull_entity")
	public static EntityType<UltimateWitherSkullEntity> TYPE;

	public UltimateWitherSkullEntity(EntityType<? extends UltimateWitherSkullEntity> p_i50147_1_, World p_i50147_2_) {
		super(p_i50147_1_, p_i50147_2_);

		this.targetID = -1;
		this.target = null;
	}

	public UltimateWitherSkullEntity(World worldIn, LivingEntity shooter) {
		super(UltimateWitherSkullEntity.TYPE, shooter, 0, 0, 0, worldIn);
		this.xPower = 0;
		this.yPower = 0;
		this.zPower = 0;

		this.targetID = -1;
		this.target = null;

		this.setDeltaMovement(new Vector3d(0, 0, 0));
	}

	public UltimateWitherSkullEntity(World worldIn, LivingEntity shooter, LivingEntity target) {
		this(worldIn, shooter);

		this.target = target;
		this.targetID = target.getId();
		this.entityData.set(UltimateWitherSkullEntity.BOUND_TARGET, this.targetID);
	}

	public void initMotion(LivingEntity player, double accelX, double accelY, double accelZ, float modifier) {
		this.setPos(this.getX(), this.getY(), this.getZ());
		//this.setMotion(Vec3d.ZERO);
		accelX = accelX + this.random.nextGaussian() * 0.4D;
		accelY = accelY + this.random.nextGaussian() * 0.4D;
		accelZ = accelZ + this.random.nextGaussian() * 0.4D;
		double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
		this.xPower = accelX / d0 * 0.1D;
		this.yPower = accelY / d0 * 0.1D;
		this.zPower = accelZ / d0 * 0.1D;

		this.xPower *= modifier;
		this.yPower *= modifier;
		this.zPower *= modifier;
	}

	/**
	 * Return the motion factor for this projectile. The factor is multiplied by the original motion.
	 */
	@Override
	protected float getInertia() {
		return super.getInertia();
	}

	/**
	 * Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
	 */
	@Override
	public boolean isOnFire() {
		return false;
	}

	/**
	 * Explosion resistance of a block relative to this entity
	 */
	@Override
	public float getBlockExplosionResistance(Explosion explosionIn, IBlockReader worldIn, BlockPos pos, BlockState blockStateIn, FluidState fluidState, float explosionPower) {
		return this.isSkullInvulnerable() && !BlockTags.WITHER_IMMUNE.contains(blockStateIn.getBlock()) ? Math.min(0.8F, explosionPower) : explosionPower;
	}


	@Override
	public void tick() {
		super.tick();

		if (this.tickCount > 10 && this.tickCount < 400) {

			if (this.target == null) {

				int targetId = this.entityData.get(UltimateWitherSkullEntity.BOUND_TARGET);

				if (targetId != -1) {
					try {
						this.target = (LivingEntity) this.level.getEntity(targetId);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

			}

			if (this.target != null && this.target.isAlive()) {

				/*
				final double d = this.getDistanceSq(this.target);
				double dx = this.target.getPosX() - this.getPosX();
				double dy = this.target.getBoundingBox().minY + this.target.getHeight() * 0.6 - this.getPosY();
				double dz = this.target.getPosZ() - this.getPosZ();
				final double d2 = 1.5;
				dx /= d;
				dy /= d;
				dz /= d;

				double motionX = this.getMotion().x + (dx * d2);
				double motionY = this.getMotion().y + (dy * d2);
				double motionZ = this.getMotion().z + (dz * d2);

				motionX = MathHelper.clamp((float) motionX, -0.25f, 0.25f);
				motionY = MathHelper.clamp((float) motionY, -0.25f, 0.25f);
				motionZ = MathHelper.clamp((float) motionZ, -0.25f, 0.25f);

				 */

				final Vector3 thisVec = Vector3.fromEntityCenter(this);
				final Vector3 targetVec = Vector3.fromEntityCenter(this.target);
				final Vector3 diffVec = targetVec.subtract(thisVec);
				final Vector3 motionVec = diffVec.normalize().multiply(0.6)/*.multiply(Math.min(this.getDistance(this.target)/4D, 1.0))*/;

				double distance = this.distanceTo(this.target);
				Vector3 formerMotion = new Vector3(this.getDeltaMovement()).multiply(0.6);

				if (distance < 6D && distance != 0) {
					this.setDeltaMovement(new Vector3d(formerMotion.x + (motionVec.x * 2D / distance), formerMotion.y + (motionVec.y * 2D / distance), formerMotion.z + (motionVec.z * 2D / distance)).normalize().multiply(1.4D, 1.4D, 1.4D));
				}

				//this.setMotion(motionX, motionY, motionZ);
				this.xPower = motionVec.x / 3.0D;
				this.yPower = motionVec.y / 3.0D;
				this.zPower = motionVec.z / 3.0D;
			}

		}

		if (this.level.isClientSide || this.getOwner() == null)
			return;

		if (this.tickCount < 10) {
			Vector3 res = AOEMiningHelper.calcRayTrace(this.level, (PlayerEntity) this.getOwner(), FluidMode.NONE, 128);

			this.initMotion((LivingEntity) this.getOwner(), res.x - this.getX(), res.y - this.getY(), res.z - this.getZ(), 0.1F);

			return;
		} else if (this.tickCount == 10) {
			Vector3 res = AOEMiningHelper.calcRayTrace(this.level, (PlayerEntity) this.getOwner(), FluidMode.NONE, 128);

			this.initMotion((LivingEntity) this.getOwner(), res.x - this.getX(), res.y - this.getY(), res.z - this.getZ(), 1.5F);
		} else if (this.tickCount >= 400) {

			this.onHit(BlockRayTraceResult.miss(this.position(), Direction.DOWN, this.blockPosition()));

		}

	}

	/**
	 * Called when this EntityFireball hits a block or entity.
	 */
	@Override
	protected void onHit(RayTraceResult result) {
		if (!this.level.isClientSide) {

			this.clearInvincibility();

			if (result.getType() == RayTraceResult.Type.ENTITY) {
				Entity entity = ((EntityRayTraceResult) result).getEntity();

				if (entity == this.getOwner())
					return;

				if (this.getOwner() != null) {
					if (entity.hurt(DamageSource.indirectMagic(this, this.getOwner()), this.isSkullInvulnerable() ? 24.0F : 8.0F)) {
						if (entity.isAlive()) {
							this.doEnchantDamageEffects((LivingEntity) this.getOwner(), entity);
						}
					}
				} else {
					entity.hurt(DamageSource.MAGIC, this.isSkullInvulnerable() ? 16.0F : 5.0F);
				}

				if (entity instanceof LivingEntity) {

					((LivingEntity) entity).addEffect(new EffectInstance(Effects.WITHER, 20 * 30, 1));

				}
			}

			float explosionRadius = this.isSkullInvulnerable() ? 1.5F : 1.0F;
			WitherExplosion explosion = new WitherExplosion(this.level, this, this.getX(), this.getY(), this.getZ(), explosionRadius, false, Explosion.Mode.DESTROY);


			if (!net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level, explosion)) {
				explosion.explode();
				explosion.finalizeExplosion(true);

				for(ServerPlayerEntity serverplayerentity : this.level.getServer().getLevel(this.level.dimension()).players()) {
					if (serverplayerentity.distanceToSqr(explosion.getPosition().x, explosion.getPosition().y, explosion.getPosition().z) < 4096.0D) {
						serverplayerentity.connection.send(new SExplosionPacket(explosion.getPosition().x, explosion.getPosition().y, explosion.getPosition().z, explosionRadius, explosion.getToBlow(), explosion.getHitPlayers().get(serverplayerentity)));
					}
				}
			}

			EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 64, this.level.dimension())), new PacketWitherParticles(this.getX(), this.getY() + (this.getBbHeight() / 2), this.getZ(), this.isSkullInvulnerable() ? 20 : 16, false));

			this.remove();
		}

	}

	public void clearInvincibility() {
		List<LivingEntity> entities = this.level.getEntitiesOfClass(LivingEntity.class, SuperpositionHandler.getBoundingBoxAroundEntity(this, 3D));
		for (LivingEntity entity : entities) {
			entity.invulnerableTime = 0;
		}

	}

	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	@Override
	public boolean isPickable() {
		return false;
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean hurt(DamageSource source, float amount) {
		return false;
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(UltimateWitherSkullEntity.INVULNERABLE, false);
		this.entityData.define(UltimateWitherSkullEntity.BOUND_TARGET, -1);
	}

	/**
	 * Return whether this skull comes from an invulnerable (aura) wither boss.
	 */
	public boolean isSkullInvulnerable() {
		return this.entityData.get(UltimateWitherSkullEntity.INVULNERABLE);
	}

	/**
	 * Set whether this skull comes from an invulnerable (aura) wither boss.
	 */
	public void setSkullInvulnerable(boolean invulnerable) {
		this.entityData.set(UltimateWitherSkullEntity.INVULNERABLE, invulnerable);
	}

	@Override
	protected boolean shouldBurn() {
		return false;
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}