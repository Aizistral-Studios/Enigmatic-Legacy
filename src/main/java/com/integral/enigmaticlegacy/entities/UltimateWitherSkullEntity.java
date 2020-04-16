package com.integral.enigmaticlegacy.entities;

import java.util.List;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.AOEMiningHelper;
import com.integral.enigmaticlegacy.helpers.Vector3;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;

public class UltimateWitherSkullEntity extends DamagingProjectileEntity {
   private static final DataParameter<Boolean> INVULNERABLE = EntityDataManager.createKey(UltimateWitherSkullEntity.class, DataSerializers.BOOLEAN);

   @ObjectHolder(EnigmaticLegacy.MODID + ":ultimate_wither_skull_entity")
   public static EntityType<UltimateWitherSkullEntity> TYPE;
   
   	   public UltimateWitherSkullEntity(EntityType<? extends UltimateWitherSkullEntity> p_i50147_1_, World p_i50147_2_) {
	      super(p_i50147_1_, p_i50147_2_);
	   }

	   public UltimateWitherSkullEntity(World worldIn, LivingEntity shooter) {
	      super(TYPE, shooter, 0, 0, 0, worldIn);
	      this.accelerationX = 0;
	      this.accelerationY = 0;
	      this.accelerationZ = 0;
	      
	      this.setMotion(new Vec3d(0, 0, 0));
	   }
	   
	   
   public void initMotion(LivingEntity player, double accelX, double accelY, double accelZ, float modifier) {
	      this.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
	      //this.setMotion(Vec3d.ZERO);
	      accelX = accelX + this.rand.nextGaussian() * 0.4D;
	      accelY = accelY + this.rand.nextGaussian() * 0.4D;
	      accelZ = accelZ + this.rand.nextGaussian() * 0.4D;
	      double d0 = (double)MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
	      this.accelerationX = accelX / d0 * 0.1D;
	      this.accelerationY = accelY / d0 * 0.1D;
	      this.accelerationZ = accelZ / d0 * 0.1D;
	      
	      this.accelerationX *= modifier;
	      this.accelerationY *= modifier;
	      this.accelerationZ *= modifier;
   }   
	
   /**
    * Return the motion factor for this projectile. The factor is multiplied by the original motion.
    */
   protected float getMotionFactor() {
      return super.getMotionFactor();
   }

   /**
    * Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
    */
   public boolean isBurning() {
      return false;
   }

   /**
    * Explosion resistance of a block relative to this entity
    */
   public float getExplosionResistance(Explosion explosionIn, IBlockReader worldIn, BlockPos pos, BlockState blockStateIn, IFluidState p_180428_5_, float p_180428_6_) {
      return this.isSkullInvulnerable() && !BlockTags.WITHER_IMMUNE.contains(blockStateIn.getBlock()) ? Math.min(0.8F, p_180428_6_) : p_180428_6_;
   }
   
   @Override
   public void tick() {
	   super.tick();
	   
	   if (this.world.isRemote)
		   return;
	   
	   if (!(this.shootingEntity instanceof PlayerEntity)) {
		   return;
	   }
	   
	   if (this.ticksExisted < 10) {
		   
		   Vector3 res = AOEMiningHelper.calcRayTrace(world, (PlayerEntity) this.shootingEntity, FluidMode.NONE, 128);
		   
		   this.initMotion(this.shootingEntity, res.x - this.getPosX(), res.y - this.getPosY(), res.z - this.getPosZ(), 0.1F);
		   
		   return;
	   }
	   
	   if (this.ticksExisted == 10) {
		   Vector3 res = AOEMiningHelper.calcRayTrace(world, (PlayerEntity) this.shootingEntity, FluidMode.NONE, 128);
		   
		   this.initMotion(this.shootingEntity, res.x - this.getPosX(), res.y - this.getPosY(), res.z - this.getPosZ(), 1.0F);
	   }
	   
	   if (this.ticksExisted >= 400) {
		   this.onImpact(BlockRayTraceResult.createMiss(this.getPositionVector(), Direction.DOWN, this.getPosition()));
		   
	   }
   }

   /**
    * Called when this EntityFireball hits a block or entity.
    */
   protected void onImpact(RayTraceResult result) {
      if (!this.world.isRemote) {
    	  
         if (result.getType() == RayTraceResult.Type.ENTITY) {
            Entity entity = ((EntityRayTraceResult)result).getEntity();
            
            if (entity == this.shootingEntity)
            	return;
            
            if (this.shootingEntity != null) {
               if (entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this.shootingEntity), 8.0F)) {
                  if (entity.isAlive()) {
                     this.applyEnchantments(this.shootingEntity, entity);
                  }
               }
            } else {
               entity.attackEntityFrom(DamageSource.MAGIC, 5.0F);
            }

            if (entity instanceof LivingEntity) {
               
                ((LivingEntity)entity).addPotionEffect(new EffectInstance(Effects.WITHER, 20 * 30, 1));
               
            }
         }
         
         this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), this.isSkullInvulnerable() ? 1.5F : 1.0F, false, Explosion.Mode.DESTROY);
         
         List<ItemEntity> drops = this.world.getEntitiesWithinAABB(ItemEntity.class, SuperpositionHandler.getBoundingBoxAroundEntity(this, 2D));
         for (ItemEntity drop : drops) {
        	 drop.remove();
         }
         
         this.remove();
      }

   }

   /**
    * Returns true if other Entities should be prevented from moving through this Entity.
    */
   public boolean canBeCollidedWith() {
      return false;
   }

   /**
    * Called when the entity is attacked.
    */
   public boolean attackEntityFrom(DamageSource source, float amount) {
      return false;
   }

   protected void registerData() {
      this.dataManager.register(INVULNERABLE, false);
   }

   /**
    * Return whether this skull comes from an invulnerable (aura) wither boss.
    */
   public boolean isSkullInvulnerable() {
      return this.dataManager.get(INVULNERABLE);
   }

   /**
    * Set whether this skull comes from an invulnerable (aura) wither boss.
    */
   public void setSkullInvulnerable(boolean invulnerable) {
      this.dataManager.set(INVULNERABLE, invulnerable);
   }

   protected boolean isFireballFiery() {
      return false;
   }
   
   public IPacket<?> createSpawnPacket() {
	  return NetworkHooks.getEntitySpawningPacket(this);
   }
}