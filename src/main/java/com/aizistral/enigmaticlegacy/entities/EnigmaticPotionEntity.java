package com.aizistral.enigmaticlegacy.entities;

import java.util.List;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.helpers.PotionHelper;
import com.aizistral.enigmaticlegacy.registries.EnigmaticEntities;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;

/**
 * Modified version of Potion Entity designed specifically for Enigmatic
 * Legacy's potions.
 *
 * @author Integral
 */

public class EnigmaticPotionEntity extends ThrowableItemProjectile implements ItemSupplier {
	private static final EntityDataAccessor<ItemStack> ITEM = SynchedEntityData.defineId(
			EnigmaticPotionEntity.class, EntityDataSerializers.ITEM_STACK);

	public EnigmaticPotionEntity(EntityType<EnigmaticPotionEntity> type, Level world) {
		super(type, world);
	}

	public EnigmaticPotionEntity(Level world, LivingEntity entity) {
		super(EnigmaticEntities.ENIGMATIC_POTION, entity, world);
	}

	public EnigmaticPotionEntity(Level world, double x, double y, double z) {
		super(EnigmaticEntities.ENIGMATIC_POTION, x, y, z, world);
	}

	@Override
	protected void defineSynchedData() {
		this.getEntityData().define(EnigmaticPotionEntity.ITEM, ItemStack.EMPTY);
	}

	@Override
	public ItemStack getItem() {
		ItemStack itemstack = this.getEntityData().get(EnigmaticPotionEntity.ITEM);

		if (PotionHelper.isAdvancedPotion(itemstack))
			// if (!itemstack.isEmpty())
			return itemstack;
		else
			return new ItemStack(Items.SPLASH_POTION);
		// else
		// return new ItemStack(Items.SPLASH_POTION);

	}

	@Override
	public void setItem(ItemStack stack) {
		this.getEntityData().set(EnigmaticPotionEntity.ITEM, stack.copy());
	}

	/**
	 * Gets the amount of gravity to apply to the thrown entity with each tick.
	 */
	@Override
	protected float getGravity() {
		return 0.05F;
	}

	@Override
	public void tick() {
		super.tick();
	}

	/**
	 * Called when this EntityThrowable hits a block or entity.
	 */
	@Override
	protected void onHit(HitResult result) {
		if (!this.level().isClientSide) {
			ItemStack itemstack = this.getItem();

			List<MobEffectInstance> list = PotionHelper.getEffects(itemstack);

			int i = 2002;

			if (list != null && !list.isEmpty()) {
				if (this.isLingering()) {
					this.makeAreaOfEffectCloud(itemstack, list);
				} else {
					this.triggerSplash(list,
							result.getType() == HitResult.Type.ENTITY ? ((EntityHitResult) result).getEntity()
									: null);
				}

				for (MobEffectInstance instance : list) {
					if (instance.getEffect().isInstantenous()) {
						i = 2007;
					}
				}
			}

			this.level().levelEvent(i, new BlockPos(this.blockPosition()), PotionHelper.getColor(itemstack));
			this.discard();
		}
	}

	private void triggerSplash(List<MobEffectInstance> p_213888_1_, @Nullable Entity p_213888_2_) {
		AABB axisalignedbb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
		List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, axisalignedbb);
		if (!list.isEmpty()) {
			for (LivingEntity livingentity : list) {
				if (livingentity.isAffectedByPotions()) {
					double d0 = this.distanceToSqr(livingentity);
					if (d0 < 16.0D) {
						double d1 = 1.0D - Math.sqrt(d0) / 4.0D;
						if (livingentity == p_213888_2_) {
							d1 = 1.0D;
						}

						for (MobEffectInstance effectinstance : p_213888_1_) {
							MobEffect effect = effectinstance.getEffect();
							if (effect.isInstantenous()) {
								effect.applyInstantenousEffect(this, this.getOwner(), livingentity,
										effectinstance.getAmplifier(), d1);
							} else {
								int i = (int) (d1 * effectinstance.getDuration() + 0.5D);
								if (i > 20) {
									livingentity.addEffect(
											new MobEffectInstance(effect, i, effectinstance.getAmplifier(),
													effectinstance.isAmbient(), effectinstance.isVisible()));
								}
							}
						}
					}
				}
			}
		}

	}

	private void makeAreaOfEffectCloud(ItemStack stack, List<MobEffectInstance> list) {
		AreaEffectCloud areaeffectcloudentity = new AreaEffectCloud(this.level(), this.getX(),
				this.getY(), this.getZ());
		areaeffectcloudentity.setOwner((LivingEntity) this.getOwner());
		areaeffectcloudentity.setRadius(3.0F);
		areaeffectcloudentity.setRadiusOnUse(-0.5F);
		areaeffectcloudentity.setWaitTime(10);
		areaeffectcloudentity
		.setRadiusPerTick(-areaeffectcloudentity.getRadius() / areaeffectcloudentity.getDuration());

		for (MobEffectInstance effectInstance : list) {
			areaeffectcloudentity.addEffect(new MobEffectInstance(effectInstance.getEffect(),
					effectInstance.getDuration() / 4, effectInstance.getAmplifier(), effectInstance.isAmbient(),
					effectInstance.isVisible()));
		}

		areaeffectcloudentity.setFixedColor(PotionHelper.getColor(stack));

		this.level().addFreshEntity(areaeffectcloudentity);
	}

	private boolean isLingering() {
		return this.getItem().getItem() == EnigmaticItems.ULTIMATE_POTION_LINGERING;
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		ItemStack itemstack = ItemStack.of(compound.getCompound("Potion"));
		if (itemstack.isEmpty()) {
			this.discard();
		} else {
			this.setItem(itemstack);
		}

	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		ItemStack itemstack = this.getItem();
		if (!itemstack.isEmpty()) {
			compound.put("Potion", itemstack.save(new CompoundTag()));
		}

	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected Item getDefaultItem() {
		return Items.SPLASH_POTION;
	}

}