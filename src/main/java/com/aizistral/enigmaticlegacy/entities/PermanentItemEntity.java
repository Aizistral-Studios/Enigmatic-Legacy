package com.aizistral.enigmaticlegacy.entities;

import java.util.UUID;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.handlers.SoulArchive;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.aizistral.enigmaticlegacy.items.SoulCrystal;
import com.aizistral.enigmaticlegacy.items.StorageCrystal;
import com.aizistral.enigmaticlegacy.packets.clients.PacketHandleItemPickup;
import com.aizistral.enigmaticlegacy.packets.clients.PacketRecallParticles;
import com.aizistral.enigmaticlegacy.registries.EnigmaticEntities;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;

/**
 * Modified copy of ItemEntity that has special properties
 * and is not recognized as it's instance.
 * @author Integral
 */

public class PermanentItemEntity extends Entity {
	private static final EntityDataAccessor<ItemStack> ITEM = SynchedEntityData.defineId(PermanentItemEntity.class, EntityDataSerializers.ITEM_STACK);
	private int age;
	private int pickupDelay;
	private int health = 5;
	private UUID thrower;
	private UUID owner;
	private Vec3 position;

	public float hoverStart = (float) (Math.random() * Math.PI * 2.0D);

	public PermanentItemEntity(EntityType<PermanentItemEntity> type, Level world) {
		super(type, world);
	}

	public PermanentItemEntity(Level world, double x, double y, double z) {
		this(EnigmaticEntities.PERMANENT_ITEM_ENTITY, world);
		y = y <= world.getMinBuildHeight() ? world.getMinBuildHeight() + 8 : y;

		this.setPos(x, y, z);
		this.setYRot(this.random.nextFloat() * 360.0F);
		this.setInvulnerable(true);

		this.setNoGravity(true);
		this.position = new Vec3(x, y, z);
	}

	public PermanentItemEntity(Level worldIn, double x, double y, double z, ItemStack stack) {
		this(worldIn, x, y, z);
		this.setItem(stack);
	}

	@OnlyIn(Dist.CLIENT)
	private PermanentItemEntity(PermanentItemEntity p_i231561_1_) {
		super(p_i231561_1_.getType(), p_i231561_1_.level());
		this.setItem(p_i231561_1_.getItem().copy());
		this.copyPosition(p_i231561_1_);
		this.age = p_i231561_1_.age;
		this.hoverStart = p_i231561_1_.hoverStart;
	}

	@OnlyIn(Dist.CLIENT)
	public PermanentItemEntity copy() {
		return new PermanentItemEntity(this);
	}

	@Override
	public boolean dampensVibrations() {
		return this.getItem().is(ItemTags.DAMPENS_VIBRATIONS);
	}

	@Override
	protected Entity.MovementEmission getMovementEmission() {
		return Entity.MovementEmission.NONE;
	}

	@Override
	protected void defineSynchedData() {
		this.getEntityData().define(PermanentItemEntity.ITEM, ItemStack.EMPTY);
	}

	@Override
	public void tick() {
		if (this.getItem().isEmpty()) {
			this.discard();
		} else {
			if (!this.level().isClientSide && this.position != null) {
				if (!this.position().equals(this.position)) {
					this.teleportTo(this.position.x, this.position.y, this.position.z);
				}
			}

			super.tick();

			if (this.pickupDelay > 0 && this.pickupDelay != 32767) {
				--this.pickupDelay;
			}

			this.xo = this.getX();
			this.yo = this.getY();
			this.zo = this.getZ();
			Vec3 vec3d = this.getDeltaMovement();

			if (!this.isNoGravity()) {
				this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
			}

			if (this.level().isClientSide) {
				this.noPhysics = false;

				this.level().addParticle(ParticleTypes.PORTAL, this.getX(), this.getY() + (this.getBbHeight() / 2), this.getZ(), ((Math.random() - 0.5) * 2.0), ((Math.random() - 0.5) * 2.0), ((Math.random() - 0.5) * 2.0));
			}

			++this.age;

			if (!this.level().isClientSide) {
				double d0 = this.getDeltaMovement().subtract(vec3d).lengthSqr();
				if (d0 > 0.01D) {
					this.hasImpulse = true;
				}
			}

			ItemStack item = this.getItem();

			if (item.isEmpty()) {
				this.discard();
			}

			// Portal Cooldown
			this.setPortalCooldown();
		}
	}

	@Override
	public int getDimensionChangingDelay() {
		return Short.MAX_VALUE;
	}

	@Override
	public Entity changeDimension(ServerLevel server, ITeleporter teleporter) {
		return null;
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (this.level().isClientSide || !this.isAlive())
			return false;

		if (SuperpositionHandler.isAbsolute(source)) {
			EnigmaticLegacy.LOGGER.warn("[WARN] Attacked permanent item entity with absolute DamageSource: " + source);
			this.kill();
			return true;
		} else
			return false;
	}

	@Override
	public void remove(Entity.RemovalReason reason) {
		if (reason == RemovalReason.DISCARDED || reason == RemovalReason.KILLED) {
			EnigmaticLegacy.LOGGER.warn("[WARN] Removing Permanent Item Entity: " + this);

			if (!this.level().isClientSide) {
				SoulArchive.getInstance().removeItem(this);
			}
		}

		super.remove(reason);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		compound.putShort("Health", (short) this.health);
		compound.putShort("Age", (short) this.age);
		compound.putShort("PickupDelay", (short) this.pickupDelay);
		if (this.getThrowerId() != null) {
			compound.putUUID("Thrower", this.getThrowerId());
		}

		if (this.getOwnerId() != null) {
			compound.putUUID("Owner", this.getOwnerId());
		}

		if (!this.getItem().isEmpty()) {
			compound.put("Item", this.getItem().save(new CompoundTag()));
		}

		if (this.position != null) {
			compound.putDouble("BoundX", this.position.x);
			compound.putDouble("BoundY", this.position.y);
			compound.putDouble("BoundZ", this.position.z);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		this.health = compound.getShort("Health");
		this.age = compound.getShort("Age");
		if (compound.contains("PickupDelay")) {
			this.pickupDelay = compound.getShort("PickupDelay");
		}

		if (compound.contains("Owner")) {
			this.owner = compound.getUUID("Owner");
		}

		if (compound.contains("Thrower")) {
			this.thrower = compound.getUUID("Thrower");
		}

		if (compound.contains("BoundX") && compound.contains("BoundY") && compound.contains("BoundZ")) {
			double x = compound.getDouble("BoundX");
			double y = compound.getDouble("BoundY");
			double z = compound.getDouble("BoundZ");
			this.position = new Vec3(x, y, z);
		}

		CompoundTag compoundnbt = compound.getCompound("Item");
		this.setItem(ItemStack.of(compoundnbt));
		if (this.getItem().isEmpty()) {
			this.discard();
		}

	}

	@Override
	public void playerTouch(Player player) {
		if (!this.level().isClientSide) {
			if (this.pickupDelay > 0)
				return;

			ItemStack itemstack = this.getItem();
			Item item = itemstack.getItem();
			int i = itemstack.getCount();

			ItemStack copy = itemstack.copy();
			boolean isPlayerOwner = player.getUUID().equals(this.getOwnerId());
			boolean allowPickUp = false;

			if (item instanceof StorageCrystal && (isPlayerOwner || !EnigmaticItems.ENIGMATIC_AMULET.isVesselOwnerOnly())) {
				allowPickUp = true;
			} else if (item instanceof SoulCrystal && isPlayerOwner) {
				allowPickUp = true;
			}

			if (allowPickUp) {
				if (item instanceof StorageCrystal) {
					CompoundTag crystalNBT = ItemNBTHelper.getNBT(itemstack);
					ItemStack embeddedSoul = crystalNBT.contains("embeddedSoul") ? ItemStack.of(crystalNBT.getCompound("embeddedSoul")) : null;

					if (!isPlayerOwner && embeddedSoul != null)
						return;

					EnigmaticItems.STORAGE_CRYSTAL.retrieveDropsFromCrystal(itemstack, player, embeddedSoul);
					SoulArchive.getInstance().removeItem(this);

					/*
					if (!isPlayerOwner && embeddedSoul != null) {
						PermanentItemEntity droppedSoulCrystal = new PermanentItemEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ(), embeddedSoul);
						droppedSoulCrystal.setOwnerId(this.getOwnerId());
						this.world.addEntity(droppedSoulCrystal);
					}
					 */

				} else if (item instanceof SoulCrystal) {
					if (!EnigmaticItems.SOUL_CRYSTAL.retrieveSoulFromCrystal(player, itemstack))
						return;
					else {
						SoulArchive.getInstance().removeItem(this);
					}
				}

				EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY() + (this.getBbHeight() / 2), this.getZ(), 64, player.level().dimension())), new PacketRecallParticles(this.getX(), this.getY() + (this.getBbHeight() / 2), this.getZ(), 48, false));

				player.take(this, i);
				EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 64, this.level().dimension())), new PacketHandleItemPickup(player.getId(), this.getId()));

				EnigmaticLegacy.LOGGER.info("Player " + player.getGameProfile().getName() + " picking up: " + this);
				this.discard();
				itemstack.setCount(0);
			} else if (this.pickupDelay == 0 && (this.owner == null || this.owner.equals(player.getUUID())) && (i <= 0 || player.getInventory().add(itemstack))) {
				copy.setCount(copy.getCount() - this.getItem().getCount());
				if (itemstack.isEmpty()) {
					player.take(this, i);

					EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 64, this.level().dimension())), new PacketHandleItemPickup(player.getId(), this.getId()));

					EnigmaticLegacy.LOGGER.info("Player " + player.getGameProfile().getName() + " picking up: " + this);
					this.discard();
					itemstack.setCount(i);
				}

				player.awardStat(Stats.ITEM_PICKED_UP.get(item), i);
			}

		}
	}

	public boolean containsSoul() {
		if (this.getItem().is(EnigmaticItems.SOUL_CRYSTAL))
			return true;
		else if (this.getItem().is(EnigmaticItems.STORAGE_CRYSTAL))
			return ItemNBTHelper.getNBT(this.getItem()).contains("embeddedSoul");
		else
			return false;
	}

	@OnlyIn(Dist.CLIENT)
	public float getItemHover(float partialTicks) {
		return (this.getAge() + partialTicks) / 20.0F + this.hoverStart;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean shouldRenderAtSqrDistance(double distance) {
		double d0 = this.getBoundingBox().getSize() * 4.0D;
		if (Double.isNaN(d0)) {
			d0 = 4.0D;
		}

		d0 = d0 * 64.0D;
		return distance < d0 * d0;
	}

	@Override
	public Component getName() {
		Component itextcomponent = this.getCustomName();
		return itextcomponent != null ? itextcomponent : Component.translatable(this.getItem().getDescriptionId());
	}

	@Override
	public boolean isAttackable() {
		return false;
	}

	@Override
	public boolean isCurrentlyGlowing() {
		return true;
	}

	public ItemStack getItem() {
		return this.getEntityData().get(PermanentItemEntity.ITEM);
	}

	public void setItem(ItemStack stack) {
		this.getEntityData().set(PermanentItemEntity.ITEM, stack);
	}

	@Nullable
	public UUID getOwnerId() {
		return this.owner;
	}

	public void setOwnerId(@Nullable UUID ownerId) {
		this.owner = ownerId;
	}

	@Nullable
	public UUID getThrowerId() {
		return this.thrower;
	}

	public void setThrowerId(@Nullable UUID throwerId) {
		this.thrower = throwerId;
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

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}