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

	private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(EnigmaticPotionEntity.class,
			DataSerializers.ITEMSTACK);

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
	protected void registerData() {
		this.getDataManager().register(EnigmaticPotionEntity.ITEM, ItemStack.EMPTY);
	}

	@Override
	public ItemStack getItem() {
		ItemStack itemstack = this.getDataManager().get(EnigmaticPotionEntity.ITEM);

		if (PotionHelper.isAdvancedPotion(itemstack))
			// if (!itemstack.isEmpty())
			return itemstack;
		else
			return new ItemStack(Items.SPLASH_POTION);
		// else
		// return new ItemStack(Items.SPLASH_POTION);

	}

	public void setItem(ItemStack stack) {
		this.getDataManager().set(EnigmaticPotionEntity.ITEM, stack.copy());
	}

	/**
	 * Gets the amount of gravity to apply to the thrown entity with each tick.
	 */
	@Override
	protected float getGravityVelocity() {
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
	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote) {
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
					if (instance.getPotion().isInstant()) {
						i = 2007;
					}
				}
			}

			this.world.playEvent(i, new BlockPos(this.getOnPosition()), PotionHelper.getColor(itemstack));
			this.remove();
		}
	}

	private void triggerSplash(List<EffectInstance> p_213888_1_, @Nullable Entity p_213888_2_) {
		AxisAlignedBB axisalignedbb = this.getBoundingBox().grow(4.0D, 2.0D, 4.0D);
		List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb);
		if (!list.isEmpty()) {
			for (LivingEntity livingentity : list) {
				if (livingentity.canBeHitWithPotion()) {
					double d0 = this.getDistanceSq(livingentity);
					if (d0 < 16.0D) {
						double d1 = 1.0D - Math.sqrt(d0) / 4.0D;
						if (livingentity == p_213888_2_) {
							d1 = 1.0D;
						}

						for (EffectInstance effectinstance : p_213888_1_) {
							Effect effect = effectinstance.getPotion();
							if (effect.isInstant()) {
								effect.affectEntity(this, this.func_234616_v_(), livingentity,
										effectinstance.getAmplifier(), d1);
							} else {
								int i = (int) (d1 * effectinstance.getDuration() + 0.5D);
								if (i > 20) {
									livingentity.addPotionEffect(
											new EffectInstance(effect, i, effectinstance.getAmplifier(),
													effectinstance.isAmbient(), effectinstance.doesShowParticles()));
								}
							}
						}
					}
				}
			}
		}

	}

	private void makeAreaOfEffectCloud(ItemStack stack, List<EffectInstance> list) {
		AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(this.world, this.getPosX(),
				this.getPosY(), this.getPosZ());
		areaeffectcloudentity.setOwner((LivingEntity) this.func_234616_v_());
		areaeffectcloudentity.setRadius(3.0F);
		areaeffectcloudentity.setRadiusOnUse(-0.5F);
		areaeffectcloudentity.setWaitTime(10);
		areaeffectcloudentity
		.setRadiusPerTick(-areaeffectcloudentity.getRadius() / areaeffectcloudentity.getDuration());

		for (EffectInstance effectInstance : list) {
			areaeffectcloudentity.addEffect(new EffectInstance(effectInstance.getPotion(),
					effectInstance.getDuration() / 4, effectInstance.getAmplifier(), effectInstance.isAmbient(),
					effectInstance.doesShowParticles()));
		}

		areaeffectcloudentity.setColor(PotionHelper.getColor(stack));

		this.world.addEntity(areaeffectcloudentity);
	}

	private boolean isLingering() {
		return this.getItem().getItem() == EnigmaticLegacy.ultimatePotionLingering;
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		ItemStack itemstack = ItemStack.read(compound.getCompound("Potion"));
		if (itemstack.isEmpty()) {
			this.remove();
		} else {
			this.setItem(itemstack);
		}

	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		ItemStack itemstack = this.getItem();
		if (!itemstack.isEmpty()) {
			compound.put("Potion", itemstack.write(new CompoundNBT()));
		}

	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}