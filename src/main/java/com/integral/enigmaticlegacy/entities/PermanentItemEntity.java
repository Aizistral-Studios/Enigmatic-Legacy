package com.integral.enigmaticlegacy.entities;

import java.util.UUID;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.packets.clients.PacketHandleItemPickup;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Modified copy of ItemEntity that has special properties
 * and is not recognized as it's instance.
 * @author Integral
 */

public class PermanentItemEntity extends Entity {
   private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(PermanentItemEntity.class, DataSerializers.ITEMSTACK);
   private int age;
   private int pickupDelay;
   private int health = 5;
   private UUID thrower;
   private UUID owner;
   
    @ObjectHolder(EnigmaticLegacy.MODID + ":permanent_item_entity")
	public static EntityType<PermanentItemEntity> TYPE;

   public final float hoverStart = (float)(Math.random() * Math.PI * 2.0D);

   public PermanentItemEntity(EntityType<PermanentItemEntity> type, World world) {
      super(type, world);
   }

   public PermanentItemEntity(World worldIn, double x, double y, double z) {
      this(TYPE, worldIn);
      this.setPosition(x, y, z);
      this.rotationYaw = this.rand.nextFloat() * 360.0F;
      
      this.setNoGravity(true);
   }

   public PermanentItemEntity(World worldIn, double x, double y, double z, ItemStack stack) {
      this(worldIn, x, y, z);
      this.setItem(stack);
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   protected void registerData() {
      this.getDataManager().register(ITEM, ItemStack.EMPTY);
   }

   public void tick() {
      
      if (this.getItem().isEmpty()) {
         this.remove();
      } else {
    	 super.tick();
    	  
         if (this.pickupDelay > 0 && this.pickupDelay != 32767) {
            --this.pickupDelay;
         }

         this.prevPosX = this.getPosX();
         this.prevPosY = this.getPosY();
         this.prevPosZ = this.getPosZ();
         Vec3d vec3d = this.getMotion();
         
         if (!this.hasNoGravity()) {
            this.setMotion(this.getMotion().add(0.0D, -0.04D, 0.0D));
         }

         if (this.world.isRemote) {
            this.noClip = false;
            
            this.world.addParticle(ParticleTypes.PORTAL, this.getPosX(), this.getPosY()+(this.getHeight()/2), this.getPosZ(), ((Math.random()-0.5)*2.0), ((Math.random()-0.5)*2.0), ((Math.random()-0.5)*2.0));
            
         }

         
         ++this.age;
         
         if (!this.world.isRemote) {
            double d0 = this.getMotion().subtract(vec3d).lengthSquared();
            if (d0 > 0.01D) {
               this.isAirBorne = true;
            }
         }

         ItemStack item = this.getItem();

         if (item.isEmpty()) {
            this.remove();
         }

      }
   }


   @Override
   protected void dealFireDamage(int amount) {
      this.attackEntityFrom(DamageSource.IN_FIRE, (float)amount);
   }

   @Override
   public boolean attackEntityFrom(DamageSource source, float amount) {
      if (this.world.isRemote || !this.isAlive()) return false;
      
      if (source.isDamageAbsolute()) {
    	  this.remove();
    	  return false;
      } else
    	  return false;
      
   }

   public void writeAdditional(CompoundNBT compound) {
      compound.putShort("Health", (short)this.health);
      compound.putShort("Age", (short)this.age);
      compound.putShort("PickupDelay", (short)this.pickupDelay);
      if (this.getThrowerId() != null) {
         compound.put("Thrower", NBTUtil.writeUniqueId(this.getThrowerId()));
      }

      if (this.getOwnerId() != null) {
         compound.put("Owner", NBTUtil.writeUniqueId(this.getOwnerId()));
      }

      if (!this.getItem().isEmpty()) {
         compound.put("Item", this.getItem().write(new CompoundNBT()));
      }

   }

   public void readAdditional(CompoundNBT compound) {
      this.health = compound.getShort("Health");
      this.age = compound.getShort("Age");
      if (compound.contains("PickupDelay")) {
         this.pickupDelay = compound.getShort("PickupDelay");
      }

      if (compound.contains("Owner", 10)) {
         this.owner = NBTUtil.readUniqueId(compound.getCompound("Owner"));
      }

      if (compound.contains("Thrower", 10)) {
         this.thrower = NBTUtil.readUniqueId(compound.getCompound("Thrower"));
      }

      CompoundNBT compoundnbt = compound.getCompound("Item");
      this.setItem(ItemStack.read(compoundnbt));
      if (this.getItem().isEmpty()) {
         this.remove();
      }

   }

   public void onCollideWithPlayer(PlayerEntity entityIn) {
      if (!this.world.isRemote) {
         if (this.pickupDelay > 0) return;
         ItemStack itemstack = this.getItem();
         Item item = itemstack.getItem();
         int i = itemstack.getCount();

         ItemStack copy = itemstack.copy();
         if (this.pickupDelay == 0 && (this.owner == null || this.owner.equals(entityIn.getUniqueID())) && (i <= 0 || entityIn.inventory.addItemStackToInventory(itemstack))) {
            copy.setCount(copy.getCount() - getItem().getCount());
            if (itemstack.isEmpty()) {
               entityIn.onItemPickup(this, i);
               
               EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getPosX(), this.getPosY(), this.getPosZ(), 64, this.dimension)), new PacketHandleItemPickup(entityIn.getEntityId(), this.getEntityId()));
               
               this.remove();
               itemstack.setCount(i);
            }

            entityIn.addStat(Stats.ITEM_PICKED_UP.get(item), i);
         }

      }
   }

   public ITextComponent getName() {
      ITextComponent itextcomponent = this.getCustomName();
      return (ITextComponent)(itextcomponent != null ? itextcomponent : new TranslationTextComponent(this.getItem().getTranslationKey()));
   }


   public boolean canBeAttackedWithItem() {
      return false;
   }

   @Nullable
   public Entity changeDimension(DimensionType destination) {
      Entity entity = super.changeDimension(destination);

      return entity;
   }

   public ItemStack getItem() {
      return this.getDataManager().get(ITEM);
   }


   public void setItem(ItemStack stack) {
      this.getDataManager().set(ITEM, stack);
   }

   @Nullable
   public UUID getOwnerId() {
      return this.owner;
   }

   public void setOwnerId(@Nullable UUID p_200217_1_) {
      this.owner = p_200217_1_;
   }

   @Nullable
   public UUID getThrowerId() {
      return this.thrower;
   }

   public void setThrowerId(@Nullable UUID p_200216_1_) {
      this.thrower = p_200216_1_;
   }

   @OnlyIn(Dist.CLIENT)
   public int getAge() {
      return this.age;
   }

   public void setDefaultPickupDelay() {
      this.pickupDelay = 10;
   }

   public void setNoPickupDelay() {
      this.pickupDelay = 0;
   }

   public void setInfinitePickupDelay() {
      this.pickupDelay = 32767;
   }

   public void setPickupDelay(int ticks) {
      this.pickupDelay = ticks;
   }

   public boolean cannotPickup() {
      return this.pickupDelay > 0;
   }

   public void makeFakeItem() {
      this.setInfinitePickupDelay();
   }

   public IPacket<?> createSpawnPacket() {
	      return NetworkHooks.getEntitySpawningPacket(this);
   }
}