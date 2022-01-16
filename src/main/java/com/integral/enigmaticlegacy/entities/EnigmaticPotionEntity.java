package com.integral.enigmaticlegacy.entities;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.helpers.PotionHelper;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;

@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)

/**
 * Modified version of Potion Entity designed specifically for Enigmatic
 * Legacy's potions.
 *
 * @author Integral
 */

public class EnigmaticPotionEntity extends ThrowableEntity implements IRendersAsItem {
	@ObjectHolder(EnigmaticLegacy.MODID + ":enigmatic_potion_entity")
	public static EntityType<EnigmaticPotionEntity> TYPE;

	private static final DataParameter<ItemStack> ITEM = EntityDataManager.defineId(EnigmaticPotionEntity.class,
			DataSerializers.ITEM_STACK);

	public EnigmaticPotionEntity(EntityType<EnigmaticPotionEntity> type, World world) {
		super(type, world);
	}

	public EnigmaticPotionEntity(World world, LivingEntity entity) {
		super(EnigmaticPotionEntity.TYPE, entity, world);
	}

	public EnigmaticPotionEntity(World world, double x, double y, double z) {
		super(EnigmaticPotionEntity.TYPE, x, y, z, world);
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
	protected void onHit(RayTraceResult result) {
		if (!this.level.isClientSide) {
			ItemStack itemstack = this.getItem();

			List<EffectInstance> list = PotionHelper.getEffects(itemstack);

			int i = 2002;

			if (list != null && !list.isEmpty()) {
				if (this.isLingering()) {
					this.makeAreaOfEffectCloud(itemstack, list);
				} else {
					this.triggerSplash(list,
							result.getType() == RayTraceResult.Type.ENTITY ? ((EntityRayTraceResult) result).getEntity()
									: null);
				}

				for (EffectInstance instance : list) {
					if (instance.getEffect().isInstantenous()) {
						i = 2007;
					}
				}
			}

			// TODO Replace getOnPos to blockPosition everywhere
			this.level.levelEvent(i, new BlockPos(this.getOnPos()), PotionHelper.getColor(itemstack));
			this.remove();
		}
	}

	private void triggerSplash(List<EffectInstance> p_213888_1_, @Nullable Entity p_213888_2_) {
		AxisAlignedBB axisalignedbb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
		List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, axisalignedbb);
		if (!list.isEmpty()) {
			for (LivingEntity livingentity : list) {
				if (livingentity.isAffectedByPotions()) {
					double d0 = this.distanceToSqr(livingentity);
					if (d0 < 16.0D) {
						double d1 = 1.0D - Math.sqrt(d0) / 4.0D;
						if (livingentity == p_213888_2_) {
							d1 = 1.0D;
						}

						for (EffectInstance effectinstance : p_213888_1_) {
							Effect effect = effectinstance.getEffect();
							if (effect.isInstantenous()) {
								effect.applyInstantenousEffect(this, this.getOwner(), livingentity,
										effectinstance.getAmplifier(), d1);
							} else {
								int i = (int) (d1 * effectinstance.getDuration() + 0.5D);
								if (i > 20) {
									livingentity.addEffect(
											new EffectInstance(effect, i, effectinstance.getAmplifier(),
													effectinstance.isAmbient(), effectinstance.isVisible()));
								}
							}
						}
					}
				}
			}
		}

	}

	private void makeAreaOfEffectCloud(ItemStack stack, List<EffectInstance> list) {
		AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(this.level, this.getX(),
				this.getY(), this.getZ());
		areaeffectcloudentity.setOwner((LivingEntity) this.getOwner());
		areaeffectcloudentity.setRadius(3.0F);
		areaeffectcloudentity.setRadiusOnUse(-0.5F);
		areaeffectcloudentity.setWaitTime(10);
		areaeffectcloudentity
		.setRadiusPerTick(-areaeffectcloudentity.getRadius() / areaeffectcloudentity.getDuration());

		for (EffectInstance effectInstance : list) {
			areaeffectcloudentity.addEffect(new EffectInstance(effectInstance.getEffect(),
					effectInstance.getDuration() / 4, effectInstance.getAmplifier(), effectInstance.isAmbient(),
					effectInstance.isVisible()));
		}

		areaeffectcloudentity.setFixedColor(PotionHelper.getColor(stack));

		this.level.addFreshEntity(areaeffectcloudentity);
	}

	private boolean isLingering() {
		return this.getItem().getItem() == EnigmaticLegacy.ultimatePotionLingering;
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		ItemStack itemstack = ItemStack.of(compound.getCompound("Potion"));
		if (itemstack.isEmpty()) {
			this.remove();
		} else {
			this.setItem(itemstack);
		}

	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		ItemStack itemstack = this.getItem();
		if (!itemstack.isEmpty()) {
			compound.put("Potion", itemstack.save(new CompoundNBT()));
		}

	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
