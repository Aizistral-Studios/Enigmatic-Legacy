package com.integral.enigmaticlegacy.entities;

import java.util.UUID;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.items.SoulCrystal;
import com.integral.enigmaticlegacy.items.StorageCrystal;
import com.integral.enigmaticlegacy.packets.clients.PacketHandleItemPickup;
import com.integral.enigmaticlegacy.packets.clients.PacketRecallParticles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.ITeleporter;
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

	public final float hoverStart = (float) (Math.random() * Math.PI * 2.0D);

	public PermanentItemEntity(EntityType<PermanentItemEntity> type, World world) {
		super(type, world);
	}

	public PermanentItemEntity(World worldIn, double x, double y, double z) {
		this(PermanentItemEntity.TYPE, worldIn);
		this.setPosition(x, y <= 0 ? 1 : y, z);
		this.rotationYaw = this.rand.nextFloat() * 360.0F;
		this.setInvulnerable(true);

		this.setNoGravity(true);
	}

	public PermanentItemEntity(World worldIn, double x, double y, double z, ItemStack stack) {
		this(worldIn, x, y, z);
		this.setItem(stack);
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected void registerData() {
		this.getDataManager().register(PermanentItemEntity.ITEM, ItemStack.EMPTY);
	}

	@Override
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
			Vector3d vec3d = this.getMotion();

			if (!this.hasNoGravity()) {
				this.setMotion(this.getMotion().add(0.0D, -0.04D, 0.0D));
			}

			if (this.world.isRemote) {
				this.noClip = false;

				this.world.addParticle(ParticleTypes.PORTAL, this.getPosX(), this.getPosY() + (this.getHeight() / 2), this.getPosZ(), ((Math.random() - 0.5) * 2.0), ((Math.random() - 0.5) * 2.0), ((Math.random() - 0.5) * 2.0));

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

			// Portal Cooldown
			this.field_242273_aw = Short.MAX_VALUE;

		}
	}

	@Override
	public Entity changeDimension(ServerWorld server, ITeleporter teleporter) {
		return null;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.world.isRemote || !this.isAlive())
			return false;

		if (source.isDamageAbsolute()) {
			EnigmaticLegacy.enigmaticLogger.warn("[WARN] Attacked permanent item entity with absolute DamageSource: " + source);
			this.remove();
			return true;
		} else
			return false;

	}

	@Override
	public void remove() {
		EnigmaticLegacy.enigmaticLogger.warn("[WARN] Removing Permanent Item Entity: " + this);
		super.remove();
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		compound.putShort("Health", (short) this.health);
		compound.putShort("Age", (short) this.age);
		compound.putShort("PickupDelay", (short) this.pickupDelay);
		if (this.getThrowerId() != null) {
			compound.putUniqueId("Thrower", this.getThrowerId());
		}

		if (this.getOwnerId() != null) {
			compound.putUniqueId("Owner", this.getOwnerId());
		}

		if (!this.getItem().isEmpty()) {
			compound.put("Item", this.getItem().write(new CompoundNBT()));
		}

	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		this.health = compound.getShort("Health");
		this.age = compound.getShort("Age");
		if (compound.contains("PickupDelay")) {
			this.pickupDelay = compound.getShort("PickupDelay");
		}

		if (compound.contains("Owner")) {
			this.owner = compound.getUniqueId("Owner");
		}

		if (compound.contains("Thrower")) {
			this.thrower = compound.getUniqueId("Thrower");
		}

		CompoundNBT compoundnbt = compound.getCompound("Item");
		this.setItem(ItemStack.read(compoundnbt));
		if (this.getItem().isEmpty()) {
			this.remove();
		}

	}

	@Override
	public void onCollideWithPlayer(PlayerEntity player) {
		if (!this.world.isRemote) {
			if (this.pickupDelay > 0)
				return;
			ItemStack itemstack = this.getItem();
			Item item = itemstack.getItem();
			int i = itemstack.getCount();

			ItemStack copy = itemstack.copy();
			boolean isPlayerOwner = player.getUniqueID().equals(this.getOwnerId());
			boolean allowPickUp = false;

			if (item instanceof StorageCrystal && (isPlayerOwner || !EnigmaticLegacy.enigmaticAmulet.isVesselOwnerOnly())) {
				allowPickUp = true;
			} else if (item instanceof SoulCrystal && isPlayerOwner) {
				allowPickUp = true;
			}

			if (allowPickUp) {

				if (item instanceof StorageCrystal) {
					CompoundNBT crystalNBT = ItemNBTHelper.getNBT(itemstack);
					ItemStack embeddedSoul = crystalNBT.contains("embeddedSoul") ? ItemStack.read(crystalNBT.getCompound("embeddedSoul")) : null;

					if (!isPlayerOwner && embeddedSoul != null)
						return;

					EnigmaticLegacy.storageCrystal.retrieveDropsFromCrystal(itemstack, player, embeddedSoul);
					/*
					if (!isPlayerOwner && embeddedSoul != null) {
						PermanentItemEntity droppedSoulCrystal = new PermanentItemEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ(), embeddedSoul);
						droppedSoulCrystal.setOwnerId(this.getOwnerId());
						this.world.addEntity(droppedSoulCrystal);
					}
					 */

				} else if (item instanceof SoulCrystal) {
					if (!EnigmaticLegacy.soulCrystal.retrieveSoulFromCrystal(player, itemstack))
						return;
				}

				EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getPosX(), this.getPosY() + (this.getHeight() / 2), this.getPosZ(), 64, player.world.func_234923_W_())), new PacketRecallParticles(this.getPosX(), this.getPosY() + (this.getHeight() / 2), this.getPosZ(), 48, false));

				player.onItemPickup(this, i);
				EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getPosX(), this.getPosY(), this.getPosZ(), 64, this.world.func_234923_W_())), new PacketHandleItemPickup(player.getEntityId(), this.getEntityId()));

				this.remove();
				itemstack.setCount(0);

			} else if (this.pickupDelay == 0 && (this.owner == null || this.owner.equals(player.getUniqueID())) && (i <= 0 || player.inventory.addItemStackToInventory(itemstack))) {
				copy.setCount(copy.getCount() - this.getItem().getCount());
				if (itemstack.isEmpty()) {
					player.onItemPickup(this, i);

					EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getPosX(), this.getPosY(), this.getPosZ(), 64, this.world.func_234923_W_())), new PacketHandleItemPickup(player.getEntityId(), this.getEntityId()));

					this.remove();
					itemstack.setCount(i);
				}

				player.addStat(Stats.ITEM_PICKED_UP.get(item), i);
			}

		}
	}

	@OnlyIn(Dist.CLIENT)
	public float func_234272_a_(float p_234272_1_) {
		return (this.getAge() + p_234272_1_) / 20.0F + this.hoverStart;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isInRangeToRenderDist(double distance) {
		double d0 = this.getBoundingBox().getAverageEdgeLength() * 4.0D;
		if (Double.isNaN(d0)) {
			d0 = 4.0D;
		}

		d0 = d0 * 64.0D;
		return distance < d0 * d0;
	}

	@Override
	public ITextComponent getName() {
		ITextComponent itextcomponent = this.getCustomName();
		return itextcomponent != null ? itextcomponent : new TranslationTextComponent(this.getItem().getTranslationKey());
	}

	@Override
	public boolean canBeAttackedWithItem() {
		return false;
	}

	public ItemStack getItem() {
		return this.getDataManager().get(PermanentItemEntity.ITEM);
	}

	public void setItem(ItemStack stack) {
		this.getDataManager().set(PermanentItemEntity.ITEM, stack);
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

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}