package com.integral.enigmaticlegacy.handlers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.ImmutableTriple;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.ConfigurableItem;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.api.items.IPerhaps;
import com.integral.enigmaticlegacy.api.items.ISpellstone;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.helpers.AdvancedSpawnLocationHelper;
import com.integral.enigmaticlegacy.items.TheAcknowledgment;
import com.integral.enigmaticlegacy.items.generic.ItemSpellstoneCurio;
import com.integral.enigmaticlegacy.objects.DimensionalPosition;
import com.integral.enigmaticlegacy.objects.TransientPlayerData;
import com.integral.enigmaticlegacy.objects.Vector3;
import com.integral.enigmaticlegacy.packets.clients.PacketPortalParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketRecallParticles;
import com.integral.omniconfig.Configuration;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;
import com.mojang.datafixers.util.Pair;

import net.minecraft.advancements.Advancement;
import net.minecraft.world.level.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.ParticleStatus;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityPredicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.world.entity.player.ServerPlayerEntity;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootPool.Builder;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.StandaloneLootEntry;
import net.minecraft.loot.functions.EnchantWithLevels;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.loot.functions.SetDamage;
import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

/**
 * The core and vessel for most most of the handling methods in the Enigmatic Legacy.
 *
 * @author Integral
 */

public class SuperpositionHandler {

	public static final Random random = new Random();
	public static final char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toUpperCase().toCharArray();

	public static boolean hasAdvancedCurios(final LivingEntity entity) {
		return SuperpositionHandler.getAdvancedCurios(entity).size() > 0;
	}

	public static List<ItemStack> getAdvancedCurios(final LivingEntity entity) {
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		ICuriosItemHandler handler = CuriosApi.getCuriosHelper().getCuriosHandler(entity).orElse(null);

		if (handler != null) {
			handler.getCurios().values().forEach(stacksHandler -> {
				IDynamicStackHandler soloStackHandler = stacksHandler.getStacks();

				for (int i = 0; i < stacksHandler.getSlots(); i++) {
					if (soloStackHandler.getStackInSlot(i) != null && soloStackHandler.getStackInSlot(i).getItem() instanceof ItemSpellstoneCurio) {
						stackList.add(soloStackHandler.getStackInSlot(i));
					}
				}

			});
		}

		return stackList;
	}

	public static boolean isSlotLocked(String id, final LivingEntity livingEntity) {
		ICuriosItemHandler handler = CuriosApi.getCuriosHelper().getCuriosHandler(livingEntity).orElse(null);

		if (handler != null)
			return handler.getLockedSlots().contains(id);
		else
			return true;

	}

	public static boolean hasSpellstone(final LivingEntity entity) {
		return SuperpositionHandler.getSpellstone(entity) != null;
	}

	@Nullable
	public static ItemStack getSpellstone(final LivingEntity entity) {
		List<ItemStack> spellstoneStack = new ArrayList<ItemStack>();

		CuriosApi.getCuriosHelper().getCuriosHandler(entity).ifPresent(handler -> {

			ICurioStacksHandler stacksHandler = handler.getCurios().get("spellstone");

			if (stacksHandler != null) {

				IDynamicStackHandler soloStackHandler = stacksHandler.getStacks();

				if (soloStackHandler != null) {
					for (int i = 0; i < stacksHandler.getSlots(); i++) {
						if (soloStackHandler.getStackInSlot(i) != null && soloStackHandler.getStackInSlot(i).getItem() instanceof ISpellstone) {
							spellstoneStack.add(soloStackHandler.getStackInSlot(i));
							break;
						}
					}
				}

			}

		});

		return spellstoneStack.isEmpty() ? null : spellstoneStack.get(0);
	}

	/**
	 * Checks whether LivingEntity has given Item equipped in it's Curios slots.
	 * @return True if has, false otherwise.
	 */

	public static boolean hasCurio(final LivingEntity entity, final Item curio) {
		final Optional<ImmutableTriple<String, Integer, ItemStack>> data = CuriosApi.getCuriosHelper().findEquippedCurio(curio, entity);
		return data.isPresent();
	}

	/**
	 * Gets the ItemStack of provided Item within entity's Curio slots.
	 * @return First sufficient ItemStack found, null if such item is not equipped.
	 */
	@Nullable
	public static ItemStack getCurioStack(final LivingEntity entity, final Item curio) {
		final Optional<ImmutableTriple<String, Integer, ItemStack>> data = CuriosApi.getCuriosHelper().findEquippedCurio(curio, entity);
		if (data.isPresent())
			return data.get().getRight();
		return null;
	}

	public static void destroyCurio(LivingEntity entity, Item curio) {
		CuriosApi.getCuriosHelper().getEquippedCurios(entity).ifPresent(handler -> {
			for (int i = 0; i < handler.getSlots()-1; i++) {
				if (handler.getStackInSlot(i) != null) {
					if (handler.getStackInSlot(i).getItem() == curio) {
						handler.setStackInSlot(i, ItemStack.EMPTY);
					}
				}
			}
		});
	}

	/**
	 * Sends message to Curios API in order to register specified Curio type. Should
	 * be used within InterModEnqueueEvent.
	 *
	 * @param identifier Unique identifier of the type.
	 * @param slots      How many slots of this curio will be available by default.
	 * @param isEnabled  Whether or not this curio type should be initially
	 *                   acessible by player.
	 * @param isHidden   Whether or not this curio type should be hidden from
	 *                   default Curio GUI.
	 * @param icon       Optional resource location for custom icon.
	 */
	public static void registerCurioType(final String identifier, final int slots, final boolean isEnabled, final boolean isHidden, @Nullable final ResourceLocation icon) {
		final SlotTypeMessage.Builder message = new SlotTypeMessage.Builder(identifier);

		message.size(slots);

		if (!isEnabled) {
			message.lock();
		}
		if (isHidden) {
			message.hide();
		}

		if (icon != null) {
			message.icon(icon);
		}

		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> message.build());

	}

	/**
	 * Registers a sound in Enigmatic Legacy namespace, for it to be lately defined
	 * in respective .json file.
	 *
	 * @return SoundEvent usable by playSound() methods.
	 */

	public static SoundEvent registerSound(final String soundName) {
		final ResourceLocation location = new ResourceLocation("enigmaticlegacy", soundName);
		final SoundEvent event = new SoundEvent(location);
		event.setRegistryName(location);
		ForgeRegistries.SOUND_EVENTS.register(event);
		return event;
	}

	/**
	 * Creates and returns simple bounding box of given radius around the entity.
	 */

	public static AxisAlignedBB getBoundingBoxAroundEntity(final Entity entity, final double radius) {
		return new AxisAlignedBB(entity.getX() - radius, entity.getY() - radius, entity.getZ() - radius, entity.getX() + radius, entity.getY() + radius, entity.getZ() + radius);
	}

	/**
	 * Accelerates the entity towards specific point.
	 *
	 * @param originalPosVector Absolute coordinates of the point entity should move
	 *                          towards.
	 * @param modifier          Applied velocity modifier.
	 */

	public static void setEntityMotionFromVector(final Entity entity, final Vector3 originalPosVector, final float modifier) {
		final Vector3 entityVector = Vector3.fromEntityCenter(entity);
		Vector3 finalVector = originalPosVector.subtract(entityVector);
		if (finalVector.mag() > 1.0) {
			finalVector = finalVector.normalize();
		}
		entity.setDeltaMovement(finalVector.x * modifier, finalVector.y * modifier, finalVector.z * modifier);
	}

	/**
	 * Gets the entity player is looking at.
	 *
	 * @param range   Defines the size of bounding box checked for entities with
	 *                each iteration. Smaller box may provide more precise results,
	 *                but is ineffective over large distances.
	 * @param maxDist Maximum amount of iteration the method should trace for.
	 * @return First entity found on the path, or null if none exist.
	 */

	@Nullable
	public static LivingEntity getObservedEntity(final PlayerEntity player, final World world, final float range, final int maxDist) {
		LivingEntity newTarget = null;
		Vector3 target = Vector3.fromEntityCenter(player);
		List<LivingEntity> entities = new ArrayList<LivingEntity>();
		for (int distance = 1; entities.size() == 0 && distance < maxDist; ++distance) {
			target = target.add(new Vector3(player.getLookAngle()).multiply(distance)).add(0.0, 0.5, 0.0);
			entities = player.level.getEntitiesOfClass(LivingEntity.class, new AxisAlignedBB(target.x - range, target.y - range, target.z - range, target.x + range, target.y + range, target.z + range));
			if (entities.contains(player)) {
				entities.remove(player);
			}
		}
		if (entities.size() > 0) {
			newTarget = entities.get(0);
		}
		return newTarget;
	}

	@Nullable
	public static <T extends LivingEntity> T getClosestEntity(List<? extends T> entities, Predicate<LivingEntity> predicate, double x, double y, double z) {
		double d0 = -1.0D;
		T t = null;

		for (T t1 : entities) {
			if (predicate.test(t1)) {
				double d1 = t1.distanceToSqr(x, y, z);
				if (d0 == -1.0D || d1 < d0) {
					d0 = d1;
					t = t1;
				}
			}
		}

		return t;
	}

	public static boolean doesObserveEntity(PlayerEntity player, LivingEntity entity) {
		Vector3d vector3d = player.getViewVector(1.0F).normalize();
		Vector3d vector3d1 = new Vector3d(entity.getX() - player.getX(), entity.getEyeY() - player.getEyeY(), entity.getZ() - player.getZ());
		double d0 = vector3d1.length();
		vector3d1 = vector3d1.normalize();
		double d1 = vector3d.dot(vector3d1);

		return d1 > 1.0D - 0.025D / d0 ? player.canSee(entity) : false;
	}


	public static int getSpellstoneCooldown(PlayerEntity player) {
		return TransientPlayerData.get(player).getSpellstoneCooldown();
	}

	public static void setSpellstoneCooldown(PlayerEntity playerIn, int value) {
		TransientPlayerData.get(playerIn).setSpellstoneCooldown(value);
	}

	public static void tickSpellstoneCooldown(PlayerEntity player, int decrementedTicks) {
		TransientPlayerData data = TransientPlayerData.get(player);
		data.spellstoneCooldown = data.getSpellstoneCooldown()-decrementedTicks;

		return;
	}

	/**
	 * Checks whether player's spellstone cooldown is greater than zero.
	 */

	public static boolean hasSpellstoneCooldown(PlayerEntity player) {
		return TransientPlayerData.get(player).getSpellstoneCooldown() > 0;
	}

	/**
	 * Sets the player's rotations so that they will looking at specified point in
	 * space.
	 */

	@OnlyIn(Dist.CLIENT)
	public static void lookAt(double px, double py, double pz, ClientPlayerEntity me) {
		double dirx = me.getX() - px;
		double diry = me.getY() - py;
		double dirz = me.getZ() - pz;

		double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);

		dirx /= len;
		diry /= len;
		dirz /= len;

		double pitch = Math.asin(diry);
		double yaw = Math.atan2(dirz, dirx);

		// to degree
		pitch = pitch * 180.0 / Math.PI;
		yaw = yaw * 180.0 / Math.PI;

		yaw += 90f;
		me.xRot = (float) pitch;
		me.yRot = (float) yaw;
		// me.rotationYawHead = (float)yaw;
	}

	/**
	 * Attempts to teleport entity at given coordinates, or nearest valid location
	 * on Y axis.
	 *
	 * @return True if successfull, false otherwise.
	 */

	public static boolean validTeleport(Entity entity, double x_init, double y_init, double z_init, World world, int checkAxis) {

		int x = (int) x_init;
		int y = (int) y_init;
		int z = (int) z_init;

		BlockState block = world.getBlockState(new BlockPos(x, y - 1, z));

		if (world.isEmptyBlock(new BlockPos(x, y - 1, z)) & block.canOcclude()) {

			for (int counter = 0; counter <= checkAxis; counter++) {

				if (!world.isEmptyBlock(new BlockPos(x, y + counter - 1, z)) & world.getBlockState(new BlockPos(x, y + counter - 1, z)).canOcclude() & world.isEmptyBlock(new BlockPos(x, y + counter, z)) & world.isEmptyBlock(new BlockPos(x, y + counter + 1, z))) {

					world.playSound(null, entity.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 1.0F, (float) (0.8F + (Math.random() * 0.2D)));

					EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 128, entity.level.dimension())), new PacketPortalParticles(entity.getX(), entity.getY() + (entity.getBbHeight() / 2), entity.getZ(), 72, 1.0F, false));

					if (entity instanceof ServerPlayerEntity) {
						ServerPlayerEntity player = (ServerPlayerEntity) entity;
						player.teleportTo(x + 0.5, y + counter, z + 0.5);
					} else {
						((LivingEntity) entity).teleportTo(x + 0.5, y + counter, z + 0.5);
					}

					world.playSound(null, entity.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 1.0F, (float) (0.8F + (Math.random() * 0.2D)));

					EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 128, entity.level.dimension())), new PacketRecallParticles(entity.getX(), entity.getY() + (entity.getBbHeight() / 2), entity.getZ(), 48, false));

					return true;
				}

			}

		} else {

			for (int counter = 0; counter <= checkAxis; counter++) {

				if (!world.isEmptyBlock(new BlockPos(x, y - counter - 1, z)) & world.getBlockState(new BlockPos(x, y - counter - 1, z)).canOcclude() & world.isEmptyBlock(new BlockPos(x, y - counter, z)) & world.isEmptyBlock(new BlockPos(x, y - counter + 1, z))) {

					world.playSound(null, entity.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 1.0F, (float) (0.8F + (Math.random() * 0.2D)));
					EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 128, entity.level.dimension())), new PacketRecallParticles(entity.getX(), entity.getY() + (entity.getBbHeight() / 2), entity.getZ(), 48, false));

					if (entity instanceof ServerPlayerEntity) {
						ServerPlayerEntity player = (ServerPlayerEntity) entity;
						player.teleportTo(x + 0.5, y - counter, z + 0.5);
					} else {
						((LivingEntity) entity).teleportTo(x + 0.5, y - counter, z + 0.5);
					}

					world.playSound(null, entity.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 1.0F, (float) (0.8F + (Math.random() * 0.2D)));
					EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 128, entity.level.dimension())), new PacketRecallParticles(entity.getX(), entity.getY() + (entity.getBbHeight() / 2), entity.getZ(), 48, false));

					return true;
				}

			}

		}

		return false;
	}

	/**
	 * Attempts to find valid location within given radius and teleport entity
	 * there.
	 *
	 * @return True if teleportation were successfull, false otherwise.
	 */
	public static boolean validTeleportRandomly(Entity entity, World world, int radius) {
		int d = radius * 2;

		double x = entity.getX() + ((Math.random() - 0.5D) * d);
		double y = entity.getY() + ((Math.random() - 0.5D) * d);
		double z = entity.getZ() + ((Math.random() - 0.5D) * d);
		return SuperpositionHandler.validTeleport(entity, x, y, z, world, radius);
	}

	/**
	 * Builds standart loot pool with any amount of ItemLootEntries.
	 */

	public static LootPool constructLootPool(String poolName, float minRolls, float maxRolls, @Nullable LootEntry.Builder<?>... entries) {

		Builder poolBuilder = LootPool.lootPool();
		poolBuilder.name(poolName);
		poolBuilder.setRolls(RandomValueRange.between(minRolls, maxRolls));

		for (LootEntry.Builder<?> entry : entries) {
			if (entry != null) {
				poolBuilder.add(entry);
			}
		}

		LootPool constructedPool = poolBuilder.build();

		return constructedPool;

	}

	/**
	 * Creates ItemLootEntry builder for an item. If item is disabled in config,
	 * returns null instead. Count-sensitive version, allows you to specifiy min-max
	 * amounts of items generated per entry.
	 */

	@Nullable
	public static StandaloneLootEntry.Builder<?> createOptionalLootEntry(Item item, int weight, float minCount, float maxCount) {
		if (!OmniconfigHandler.isItemEnabled(item))
			return null;

		return ItemLootEntry.lootTableItem(item).setWeight(weight).apply(SetCount.setCount(RandomValueRange.between(minCount, maxCount)));
	}

	/**
	 * Creates ItemLootEntry builder for an item. If item is disabled in config,
	 * returns null instead.
	 */

	@Nullable
	public static StandaloneLootEntry.Builder<?> createOptionalLootEntry(Item item, int weight) {
		if (!OmniconfigHandler.isItemEnabled(item))
			return null;

		return ItemLootEntry.lootTableItem(item).setWeight(weight);
	}

	/**
	 * Creates ItemLootEntry with a given weight, randomly ranged damage and
	 * level-based enchantments. Damage should be specified as modifier, where 1.0
	 * is full durability.
	 *
	 * @return
	 */

	public static StandaloneLootEntry.Builder<?> itemEntryBuilderED(Item item, int weight, float enchantLevelMin, float enchantLevelMax, float damageMin, float damageMax) {
		StandaloneLootEntry.Builder<?> builder = ItemLootEntry.lootTableItem(item);

		builder.setWeight(weight);
		builder.apply(SetDamage.setDamage(RandomValueRange.between(damageMax, damageMin)));
		builder.apply(EnchantWithLevels.enchantWithLevels(RandomValueRange.between(enchantLevelMin, enchantLevelMax)).allowTreasure());

		return builder;
	}

	public static List<ResourceLocation> getEarthenDungeons() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(LootTables.SIMPLE_DUNGEON);
		lootChestList.add(LootTables.ABANDONED_MINESHAFT);
		lootChestList.add(LootTables.VILLAGE_ARMORER);

		return lootChestList;
	}

	public static List<ResourceLocation> getWaterDungeons() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(LootTables.UNDERWATER_RUIN_BIG);
		lootChestList.add(LootTables.UNDERWATER_RUIN_SMALL);
		lootChestList.add(LootTables.SHIPWRECK_TREASURE);
		lootChestList.add(LootTables.BURIED_TREASURE);

		return lootChestList;
	}

	public static List<ResourceLocation> getLibraries() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(LootTables.STRONGHOLD_LIBRARY);
		lootChestList.add(LootTables.SHIPWRECK_MAP);

		return lootChestList;
	}

	public static List<ResourceLocation> getBastionChests() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(LootTables.BASTION_TREASURE);
		lootChestList.add(LootTables.BASTION_OTHER);
		lootChestList.add(LootTables.BASTION_BRIDGE);
		lootChestList.add(LootTables.BASTION_HOGLIN_STABLE);

		return lootChestList;
	}

	public static List<ResourceLocation> getNetherDungeons() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(LootTables.NETHER_BRIDGE);
		lootChestList.add(LootTables.BASTION_TREASURE);
		lootChestList.add(LootTables.BASTION_OTHER);
		lootChestList.add(LootTables.BASTION_BRIDGE);
		lootChestList.add(LootTables.BASTION_HOGLIN_STABLE);
		lootChestList.add(LootTables.RUINED_PORTAL);

		return lootChestList;
	}

	public static List<ResourceLocation> getAirDungeons() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(LootTables.VILLAGE_TEMPLE);

		return lootChestList;
	}

	public static List<ResourceLocation> getEnderDungeons() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(LootTables.END_CITY_TREASURE);

		return lootChestList;
	}

	public static List<ResourceLocation> getMergedAir$EarthenDungeons() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(LootTables.DESERT_PYRAMID);
		lootChestList.add(LootTables.JUNGLE_TEMPLE);

		return lootChestList;
	}

	public static List<ResourceLocation> getMergedEnder$EarthenDungeons() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(LootTables.STRONGHOLD_CORRIDOR);
		lootChestList.add(LootTables.STRONGHOLD_CROSSING);

		return lootChestList;
	}

	public static List<ResourceLocation> getOverworldDungeons() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(LootTables.SIMPLE_DUNGEON);
		lootChestList.add(LootTables.ABANDONED_MINESHAFT);
		lootChestList.add(LootTables.STRONGHOLD_CROSSING);
		lootChestList.add(LootTables.STRONGHOLD_CORRIDOR);
		lootChestList.add(LootTables.DESERT_PYRAMID);
		lootChestList.add(LootTables.JUNGLE_TEMPLE);
		lootChestList.add(LootTables.IGLOO_CHEST);
		lootChestList.add(LootTables.WOODLAND_MANSION);
		lootChestList.add(LootTables.UNDERWATER_RUIN_SMALL);
		lootChestList.add(LootTables.UNDERWATER_RUIN_BIG);
		lootChestList.add(LootTables.SHIPWRECK_SUPPLY);
		lootChestList.add(LootTables.PILLAGER_OUTPOST);

		return lootChestList;
	}

	public static List<ResourceLocation> getVillageChests() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(LootTables.VILLAGE_WEAPONSMITH);
		lootChestList.add(LootTables.VILLAGE_TOOLSMITH);
		lootChestList.add(LootTables.VILLAGE_ARMORER);
		lootChestList.add(LootTables.VILLAGE_CARTOGRAPHER);
		lootChestList.add(LootTables.VILLAGE_MASON);
		lootChestList.add(LootTables.VILLAGE_SHEPHERD);
		lootChestList.add(LootTables.VILLAGE_BUTCHER);
		lootChestList.add(LootTables.VILLAGE_FLETCHER);
		lootChestList.add(LootTables.VILLAGE_FISHER);
		lootChestList.add(LootTables.VILLAGE_TANNERY);
		lootChestList.add(LootTables.VILLAGE_TEMPLE);
		lootChestList.add(LootTables.VILLAGE_DESERT_HOUSE);
		lootChestList.add(LootTables.VILLAGE_PLAINS_HOUSE);
		lootChestList.add(LootTables.VILLAGE_TAIGA_HOUSE);
		lootChestList.add(LootTables.VILLAGE_SNOWY_HOUSE);
		lootChestList.add(LootTables.VILLAGE_SAVANNA_HOUSE);

		return lootChestList;
	}

	/**
	 * Retrieves the given INBT type from the player's persistent NBT.
	 */

	public static INBT getPersistentTag(PlayerEntity player, String tag, INBT expectedValue) {
		CompoundNBT data = player.getPersistentData();
		CompoundNBT persistent;

		if (!data.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
			data.put(PlayerEntity.PERSISTED_NBT_TAG, (persistent = new CompoundNBT()));
		} else {
			persistent = data.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
		}

		if (persistent.contains(tag))
			return persistent.get(tag);
		else {
			persistent.put(tag, expectedValue);
			return expectedValue;
		}

	}

	/**
	 * Sets the given INBT type to the player's persistent NBT.
	 */

	public static void setPersistentTag(PlayerEntity player, String tag, INBT value) {
		CompoundNBT data = player.getPersistentData();
		CompoundNBT persistent;

		if (!data.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
			data.put(PlayerEntity.PERSISTED_NBT_TAG, (persistent = new CompoundNBT()));
		} else {
			persistent = data.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
		}

		persistent.put(tag, value);
	}

	/**
	 * Sets the given boolean tag to the player's persistent NBT.
	 */

	public static void setPersistentBoolean(PlayerEntity player, String tag, boolean value) {
		SuperpositionHandler.setPersistentTag(player, tag, ByteNBT.valueOf(value));
	}

	/**
	 * Retrieves the given boolean tag from the player's persistent NBT.
	 */

	public static boolean getPersistentBoolean(PlayerEntity player, String tag, boolean expectedValue) {
		INBT theTag = SuperpositionHandler.getPersistentTag(player, tag, ByteNBT.valueOf(expectedValue));
		return theTag instanceof ByteNBT ? ((ByteNBT) theTag).getAsByte() != 0 : expectedValue;
	}

	public static void setPersistentInteger(PlayerEntity player, String tag, int value) {
		SuperpositionHandler.setPersistentTag(player, tag, IntNBT.valueOf(value));
	}

	public static int getPersistentInteger(PlayerEntity player, String tag, int expectedValue) {
		INBT theTag = SuperpositionHandler.getPersistentTag(player, tag, IntNBT.valueOf(expectedValue));
		return theTag instanceof IntNBT ? ((IntNBT) theTag).getAsInt() : expectedValue;
	}

	/**
	 * Checks whether player has specified tag in their persistent NBT, whatever
	 * it's type or value is.
	 */

	public static boolean hasPersistentTag(PlayerEntity player, String tag) {
		CompoundNBT data = player.getPersistentData();
		CompoundNBT persistent;

		if (!data.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
			data.put(PlayerEntity.PERSISTED_NBT_TAG, (persistent = new CompoundNBT()));
		} else {
			persistent = data.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
		}

		if (persistent.contains(tag))
			return true;
		else
			return false;

	}

	/**
	 * Checks whether or not player has completed specified advancement.
	 */

	public static boolean hasAdvancement(@Nonnull ServerPlayerEntity player, @Nonnull ResourceLocation location) {

		try {
			if (player.getAdvancements().getOrStartProgress(player.server.getAdvancements().getAdvancement(location)).isDone())
				return true;
		} catch (NullPointerException ex) {
			// Just don't do it lol }
		}

		return false;
	}

	/**
	 * Grant specified advancement to specified player.
	 * Don't forget to specify!
	 */

	public static void grantAdvancement(@Nonnull ServerPlayerEntity player, @Nonnull ResourceLocation location) {
		Advancement adv = player.server.getAdvancements().getAdvancement(location);

		for (String criterion : player.getAdvancements().getOrStartProgress(adv).getRemainingCriteria()) {
			player.getAdvancements().award(adv, criterion);
		}

	}

	/**
	 * Revokes specified advancement from specified player.
	 * Don't forget to specify!
	 */

	public static void revokeAdvancement(@Nonnull ServerPlayerEntity player, @Nonnull ResourceLocation location) {
		Advancement adv = player.server.getAdvancements().getAdvancement(location);

		for (String criterion : player.getAdvancements().getOrStartProgress(adv).getCompletedCriteria()) {
			player.getAdvancements().revoke(adv, criterion);
		}
	}

	/**
	 * Creates random world number. Formatted like XXXX-FFXX, where X is any digit
	 * and F is any letter from A to Z.
	 */

	public static String generateRandomWorldNumber() {

		String number = "";

		while (number.length() < 4) {
			number = number.concat("" + SuperpositionHandler.random.nextInt(10));
		}

		number = number.concat("-");

		while (number.length() < 7) {
			number = number.concat("" + SuperpositionHandler.alphabet[SuperpositionHandler.random.nextInt(SuperpositionHandler.alphabet.length)]);
		}

		while (number.length() < 9) {
			number = number.concat("" + SuperpositionHandler.random.nextInt(10));
		}

		return number;
	}

	public static PlayerEntity getPlayerByName(World world, String name) {
		PlayerEntity player = null;

		for (PlayerEntity checkedPlayer : world.players()) {
			if (checkedPlayer.getDisplayName().getString().equals(name)) {
				player = checkedPlayer;
			}
		}

		return player;
	}

	/**
	 * Applies vanilla-stype potion tooltip to the item.
	 *
	 * @param list           List of effects to be displayed in tooltip.
	 * @param lores          Text component list provided in addInformation()
	 *                       method.
	 * @param durationFactor Displayed effects duration will be multiplied by this
	 *                       value.
	 */

	@OnlyIn(Dist.CLIENT)
	public static void addPotionTooltip(List<EffectInstance> list, ItemStack itemIn, List<ITextComponent> lores, float durationFactor) {
		List<Pair<Attribute, AttributeModifier>> list1 = Lists.newArrayList();
		if (list.isEmpty()) {
			lores.add((new TranslationTextComponent("effect.none")).withStyle(TextFormatting.GRAY));
		} else {
			for (EffectInstance effectinstance : list) {
				IFormattableTextComponent iformattabletextcomponent = new TranslationTextComponent(effectinstance.getDescriptionId());
				Effect effect = effectinstance.getEffect();
				Map<Attribute, AttributeModifier> map = effect.getAttributeModifiers();
				if (!map.isEmpty()) {
					for (Entry<Attribute, AttributeModifier> entry : map.entrySet()) {
						AttributeModifier attributemodifier = entry.getValue();
						AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), effect.getAttributeModifierValue(effectinstance.getAmplifier(), attributemodifier), attributemodifier.getOperation());
						list1.add(new Pair<>(entry.getKey(), attributemodifier1));
					}
				}

				if (effectinstance.getAmplifier() > 0) {
					iformattabletextcomponent.append(" ").append(new TranslationTextComponent("potion.potency." + effectinstance.getAmplifier()));
				}

				if (effectinstance.getDuration() > 20) {
					iformattabletextcomponent.append(" (").append(EffectUtils.formatDuration(effectinstance, durationFactor)).append(")");
				}

				lores.add(iformattabletextcomponent.withStyle(effect.getCategory().getTooltipFormatting()));
			}
		}

		if (!list1.isEmpty()) {
			lores.add(StringTextComponent.EMPTY);
			lores.add((new TranslationTextComponent("potion.whenDrank")).withStyle(TextFormatting.DARK_PURPLE));

			for (Pair<Attribute, AttributeModifier> pair : list1) {
				AttributeModifier attributemodifier2 = pair.getSecond();
				double d0 = attributemodifier2.getAmount();
				double d1;
				if (attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
					d1 = attributemodifier2.getAmount();
				} else {
					d1 = attributemodifier2.getAmount() * 100.0D;
				}

				if (d0 > 0.0D) {
					lores.add((new TranslationTextComponent("attribute.modifier.plus." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), new TranslationTextComponent(pair.getFirst().getDescriptionId()))).withStyle(TextFormatting.BLUE));
				} else if (d0 < 0.0D) {
					d1 = d1 * -1.0D;
					lores.add((new TranslationTextComponent("attribute.modifier.take." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), new TranslationTextComponent(pair.getFirst().getDescriptionId()))).withStyle(TextFormatting.DARK_RED));
				}
			}
		}

	}

	/**
	 * @return True if ItemStack can be added to player's inventory (fully or
	 *         partially), false otherwise.
	 */

	public static boolean canPickStack(PlayerEntity player, ItemStack stack) {

		if (player.inventory.getFreeSlot() >= 0)
			return true;
		else {
			List<ItemStack> allInventories = new ArrayList<ItemStack>();

			// allInventories.addAll(player.inventory.armorInventory);
			allInventories.addAll(player.inventory.items);
			allInventories.addAll(player.inventory.offhand);

			for (ItemStack invStack : allInventories) {
				if (SuperpositionHandler.canMergeStacks(invStack, stack, player.inventory.getMaxStackSize()))
					return true;
			}
		}

		return false;
	}

	public static boolean canMergeStacks(ItemStack stack1, ItemStack stack2, int invStackLimit) {
		return !stack1.isEmpty() && SuperpositionHandler.stackEqualExact(stack1, stack2) && stack1.isStackable() && stack1.getCount() < stack1.getMaxStackSize() && stack1.getCount() < invStackLimit;
	}

	/**
	 * Checks item, NBT, and meta if the item is not damageable
	 */
	public static boolean stackEqualExact(ItemStack stack1, ItemStack stack2) {
		return stack1.getItem() == stack2.getItem() && ItemStack.tagMatches(stack1, stack2);
	}

	/**
	 * @return Multiplier for amount of particles based on client settings.
	 */
	@OnlyIn(Dist.CLIENT)
	public static float getParticleMultiplier() {

		if (Minecraft.getInstance().options.particles == ParticleStatus.MINIMAL)
			return 0.35F;
		else if (Minecraft.getInstance().options.particles == ParticleStatus.DECREASED)
			return 0.65F;
		else
			return 1.0F;

	}

	/**
	 * Checks whether or on the player is within the range of any active beacon.
	 * @author Integral
	 */

	public static boolean isInBeaconRange(PlayerEntity player) {
		List<BeaconTileEntity> list = new ArrayList<BeaconTileEntity>();
		boolean inRange = false;

		for (TileEntity tile : player.level.blockEntityList) {
			if (tile instanceof BeaconTileEntity) {
				list.add((BeaconTileEntity) tile);
			}
		}

		if (list.size() > 0) {
			for (BeaconTileEntity beacon : list)
				if (beacon.getLevels() > 0) {
					try {
						if (beacon.beamSections.isEmpty()) {
							continue;
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}

					int range = (beacon.getLevels() + 1) * 10;
					double distance = Math.sqrt(beacon.getBlockPos().distSqr(player.getX(), beacon.getBlockPos().getY(), player.getZ(), true));

					if (distance <= range) {
						inRange = true;
					}
				}
		}

		return inRange;
	}

	public static boolean hasItem(PlayerEntity player, Item item) {
		return player.inventory.contains(new ItemStack(item));
	}

	/**
	 * Checks whether the collection of ItemEntities contains given Item.
	 * @author Integral
	 */

	public static boolean ifDroplistContainsItem(Collection<ItemEntity> drops, Item item) {

		for (ItemEntity drop : drops) {
			if (drop.getItem() != null)
				if (drop.getItem().getItem() == item)
					return true;
		}

		return false;
	}

	public static boolean shouldPlayerDropSoulCrystal(PlayerEntity player, boolean hadRing) {
		int dropMode = OmniconfigHandler.soulCrystalsMode.getValue();
		int maxCrystalLoss = OmniconfigHandler.maxSoulCrystalLoss.getValue();

		boolean canDropMore = EnigmaticLegacy.soulCrystal.getLostCrystals(player) < maxCrystalLoss;
		boolean keepInventory = player.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);

		// TODO Use Enum config

		if (dropMode == 0)
			return hadRing && canDropMore;
		else if (dropMode == 1)
			return (hadRing || keepInventory) && canDropMore;
		else if (dropMode == 2)
			return canDropMore;

		return false;
	}

	public static ServerWorld getWorld(RegistryKey<World> key) {
		return ServerLifecycleHooks.getCurrentServer().getLevel(key);
	}

	public static ServerWorld getOverworld() {
		return SuperpositionHandler.getWorld(EnigmaticLegacy.proxy.getOverworldKey());
	}

	public static ServerWorld getNether() {
		return SuperpositionHandler.getWorld(EnigmaticLegacy.proxy.getNetherKey());
	}

	public static ServerWorld getEnd() {
		return SuperpositionHandler.getWorld(EnigmaticLegacy.proxy.getEndKey());
	}

	public static ServerWorld backToSpawn(ServerPlayerEntity serverPlayer) {
		RegistryKey<World> respawnDimension = AdvancedSpawnLocationHelper.getPlayerRespawnDimension(serverPlayer);
		ServerWorld respawnWorld = SuperpositionHandler.getWorld(respawnDimension);

		serverPlayer.level.playSound(null, serverPlayer.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));

		EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), 128, serverPlayer.level.dimension())), new PacketPortalParticles(serverPlayer.getX(), serverPlayer.getY() + (serverPlayer.getBbHeight() / 2), serverPlayer.getZ(), 100, 1.25F, false));

		Optional<Vector3d> vec = AdvancedSpawnLocationHelper.getValidSpawn(respawnWorld, serverPlayer);
		Optional<Vector3d> vec2;
		ServerWorld destinationWorld = vec.isPresent() ? respawnWorld : serverPlayer.server.overworld();

		if (!serverPlayer.getLevel().equals(destinationWorld)) {
			serverPlayer.changeDimension(destinationWorld, new RealSmoothTeleporter());
		}

		if (!respawnWorld.equals(destinationWorld)) {
			vec2 = AdvancedSpawnLocationHelper.getValidSpawn(destinationWorld, serverPlayer);
		} else {
			vec2 = Optional.empty();
		}

		if (vec.isPresent()) {
			Vector3d trueVec = vec.get();
			serverPlayer.teleportTo(trueVec.x, trueVec.y, trueVec.z);
		} else if (vec2.isPresent()) {
			Vector3d trueVec = vec2.get();
			serverPlayer.teleportTo(trueVec.x, trueVec.y, trueVec.z);
		} else {
			AdvancedSpawnLocationHelper.fuckBackToSpawn(serverPlayer.getLevel(), serverPlayer);
		}

		serverPlayer.level.playSound(null, serverPlayer.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));

		EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), 128, serverPlayer.level.dimension())), new PacketRecallParticles(serverPlayer.getX(), serverPlayer.getY() + (serverPlayer.getBbHeight() / 2), serverPlayer.getZ(), 48, false));

		return destinationWorld;
	}

	public static DimensionalPosition getRespawnPoint(ServerPlayerEntity serverPlayer) {
		RegistryKey<World> respawnDimension = AdvancedSpawnLocationHelper.getPlayerRespawnDimension(serverPlayer);
		ServerWorld respawnWorld = SuperpositionHandler.getWorld(respawnDimension);

		Optional<Vector3d> currentDimensionRespawnCoords = AdvancedSpawnLocationHelper.getValidSpawn(respawnWorld, serverPlayer);
		Optional<Vector3d> destinationDimensionRespawnCoords;

		ServerWorld destinationWorld = currentDimensionRespawnCoords.isPresent() ? respawnWorld : serverPlayer.server.overworld();

		if (!respawnWorld.equals(destinationWorld)) {
			destinationDimensionRespawnCoords = AdvancedSpawnLocationHelper.getValidSpawn(destinationWorld, serverPlayer);
		} else {
			destinationDimensionRespawnCoords = Optional.empty();
		}

		Vector3d trueVec;

		if (currentDimensionRespawnCoords.isPresent()) {
			trueVec = currentDimensionRespawnCoords.get();
		} else if (destinationDimensionRespawnCoords.isPresent()) {
			trueVec = destinationDimensionRespawnCoords.get();
		} else {
			/*
			 * TODO Spawning player at the world's center involves a lot of collision checks, which we can't do
			 * without actually teleporting the player. Investigate on possible workarounds.
			 */
			trueVec = new Vector3d(destinationWorld.getSharedSpawnPos().getX() + 0.5, destinationWorld.getSharedSpawnPos().getY() + 0.5, destinationWorld.getSharedSpawnPos().getZ() + 0.5);

			while (!destinationWorld.getBlockState(new BlockPos(trueVec)).isAir(destinationWorld, new BlockPos(trueVec)) && trueVec.y < 255.0D) {
				trueVec = trueVec.add(0, 1D, 0);
			}

		}

		return new DimensionalPosition(trueVec.x, trueVec.y, trueVec.z, destinationWorld);
	}

	public static void removeAttributeMap(PlayerEntity player, Multimap<Attribute, AttributeModifier> attributes) {
		AttributeModifierManager map = player.getAttributes();
		map.removeAttributeModifiers(attributes);
	}

	public static void applyAttributeMap(PlayerEntity player, Multimap<Attribute, AttributeModifier> attributes) {
		AttributeModifierManager map = player.getAttributes();

		map.addTransientAttributeModifiers(attributes);
	}

	public static boolean isTheCursedOne(PlayerEntity player) {
		return SuperpositionHandler.hasCurio(player, EnigmaticLegacy.cursedRing);
	}

	public static float getMissingHealthPool(PlayerEntity player) {
		return (player.getMaxHealth() - Math.min(player.getHealth(), player.getMaxHealth()))/player.getMaxHealth();
	}

	public static int getCurseAmount(ItemStack stack) {
		Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
		int totalCurses = 0;

		for (Enchantment enchantment: enchantments.keySet()) {
			if (enchantment.isCurse() && enchantments.get(enchantment) > 0) {
				totalCurses+=1;
			}
		}

		if (stack.getItem() == EnigmaticLegacy.cursedRing) {
			totalCurses+=7;
		}

		return totalCurses;
	}

	public static int getCurseAmount(PlayerEntity player) {
		int count = 0;

		for (ItemStack theStack : getFullEquipment(player)) {
			if (theStack != null) {
				count += getCurseAmount(theStack);
			}
		}

		return count;
	}

	public static List<ItemStack> getFullEquipment(PlayerEntity player) {
		List<ItemStack> equipmentStacks = Lists.newArrayList();

		equipmentStacks.add(player.getMainHandItem());
		equipmentStacks.add(player.getOffhandItem());
		equipmentStacks.addAll(player.inventory.armor);

		if (CuriosApi.getCuriosHelper().getCuriosHandler(player).isPresent()) {
			ICuriosItemHandler handler = CuriosApi.getCuriosHelper().getCuriosHandler(player).orElse(null);
			Map<String, ICurioStacksHandler> curios = handler.getCurios();

			for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
				ICurioStacksHandler stacksHandler = entry.getValue();
				IDynamicStackHandler stackHandler = stacksHandler.getStacks();

				for (int i = 0; i < stackHandler.getSlots(); i++) {
					ItemStack stack = stackHandler.getStackInSlot(i);

					equipmentStacks.add(stack);
				}
			}
		}

		return equipmentStacks;
	}

	public static double sinFunction(double lowerBound, double upperBound, double value) {
		double range = upperBound - lowerBound;

		//System.out.println("Range: " + range);
		double coef = value/range;
		//System.out.println("Coef: " + coef);
		coef *= 90D;
		coef = Math.toRadians(coef);
		double func = Math.pow(Math.sin(coef), -1);

		//System.out.println("Coef Angles: " + coef);
		//System.out.println("Sin Value: " + Math.sin(coef));

		return Math.pow(Math.sin(coef), -1);
	}

	public static double parabolicFunction(double lowerBound, double upperBound, double value) {
		double range = upperBound - lowerBound;
		double coef = value/range;

		double func = Math.pow(coef, 2);

		return func;
	}

	public static double flippedParabolicFunction(double lowerBound, double upperBound, double value) {
		double range = upperBound - lowerBound;
		double coef = value/range;

		double func = Math.pow((coef-1), 2);

		return 1.0-func;
	}

	public static boolean hasAnyArmor(LivingEntity entity) {
		int armorAmount = 0;

		for (ItemStack stack : entity.getArmorSlots()) {
			if (!stack.isEmpty()) {
				armorAmount++;
			}
		}

		if (armorAmount != 0)
			return true;
		else
			return false;
	}

	public static boolean areWeDedicatedServer() {
		return FMLEnvironment.dist == Dist.DEDICATED_SERVER;
	}

	/**
	 * Are we a remote server for specified player. Always true if we are
	 * a dedicated server; also true if we are integrated one but provided player
	 * does not host us.
	 * @param player Player in question
	 */

	public static boolean areWeRemoteServer(PlayerEntity player) {
		if (areWeDedicatedServer())
			return true;
		else
			return player.getServer() != null && !player.getServer().isSingleplayerOwner(player.getGameProfile());
	}

	public static void executeOnServer(Consumer<MinecraftServer> action) {
		if (ServerLifecycleHooks.getCurrentServer() != null) {
			action.accept(ServerLifecycleHooks.getCurrentServer());
		}
	}

	public static List<ModFileScanData.AnnotationData> retainAnnotations(String modid, Class<?> annotationClass) {
		ModFileScanData modFileInfo = ModList.get().getModFileById(modid).getFile().getScanResult();
		List<ModFileScanData.AnnotationData> list = new ArrayList<>();

		for (ModFileScanData.AnnotationData annotation : modFileInfo.getAnnotations()) {
			if (annotation.getAnnotationType().getClassName().equals(annotationClass.getName())) {
				list.add(annotation);
			}
		}

		return list;
	}

	public static List<ModFileScanData.AnnotationData> retainConfigurableItemAnnotations(String modid) {
		return retainAnnotations(modid, ConfigurableItem.class);
	}

	public static List<ModFileScanData.AnnotationData> retainConfigHolderAnnotations(String modid) {
		return retainAnnotations(modid, SubscribeConfig.class);
	}

	public static void dispatchWrapperToHolders(String modid, OmniconfigWrapper wrapper) {
		for (ModFileScanData.AnnotationData annotationData : retainConfigHolderAnnotations(modid)) {
			try {
				Class<?> retainerClass = Class.forName(annotationData.getClassType().getClassName());
				String methodName = annotationData.getMemberName().split("\\(")[0];

				boolean receiveClient;

				if (annotationData.getAnnotationData().get("receiveClient") != null) {
					receiveClient = (boolean) annotationData.getAnnotationData().get("receiveClient");
				} else {
					receiveClient = SubscribeConfig.defaultReceiveClient;
				}

				Method method = retainerClass.getDeclaredMethod(methodName, OmniconfigWrapper.class);

				if (wrapper.config.getSidedType() != Configuration.SidedConfigType.CLIENT || receiveClient) {
					method.invoke(null, wrapper);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static Multimap<String, Field> retainAccessibilityGeneratorMap(String modid) {
		final Multimap<String, Field> accessibilityGeneratorMap = HashMultimap.create();

		for (ModFileScanData.AnnotationData annotationData : retainConfigurableItemAnnotations(modid)) {
			try {
				Class<?> retainerClass = Class.forName(annotationData.getClassType().getClassName());
				String itemName = (String) annotationData.getAnnotationData().get("value");
				String fieldName = annotationData.getMemberName();

				Field field = retainerClass.getDeclaredField(fieldName);
				if (!itemName.isEmpty()) {
					accessibilityGeneratorMap.put(itemName, field);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return accessibilityGeneratorMap;
	}

	public static boolean hasAntiInsectAcknowledgement(PlayerEntity player) {
		List<ItemStack> heldItems = Lists.newArrayList(player.getMainHandItem(), player.getOffhandItem());

		for (ItemStack held : heldItems) {
			if (held != null && held.getItem() instanceof TheAcknowledgment) {
				if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BANE_OF_ARTHROPODS, held) > 0)
					return true;
			}
		}

		return false;
	}

	public static boolean isStaringAt(PlayerEntity player, LivingEntity living) {
		Vector3d vector3d = player.getViewVector(1.0F).normalize();
		Vector3d vector3d1 = new Vector3d(living.getX() - player.getX(), living.getEyeY() - player.getEyeY(), living.getZ() - player.getZ());
		double d0 = vector3d1.length();
		vector3d1 = vector3d1.normalize();
		double d1 = vector3d.dot(vector3d1);
		return d1 > 1.0D - 0.025D / d0 ? player.canSee(living) : false;
	}

	public static double getRandomNegative() {
		return (Math.random()-0.5)*2.0;
	}

	public static String minimizeNumber(double num) {
		int intg = (int)num;

		if (num - intg == 0)
			return "" + intg;
		else
			return "" + num;
	}

	/**
	 * Merges enchantments from mergeFrom onto input ItemStack, with exact same
	 * rules as vanilla Anvil when used in Survival Mode.
	 * @param input
	 * @param mergeFrom
	 * @param overmerge Shifts the rules of merging so that they make more sense with Apotheosis compat
	 * @return Copy of input ItemStack with new enchantments merged from mergeFrom
	 */

	public static ItemStack mergeEnchantments(ItemStack input, ItemStack mergeFrom, boolean overmerge, boolean onlyTreasure) {
		ItemStack returnedStack = input.copy();
		Map<Enchantment, Integer> inputEnchants = EnchantmentHelper.getEnchantments(returnedStack);
		Map<Enchantment, Integer> mergedEnchants = EnchantmentHelper.getEnchantments(mergeFrom);

		for(Enchantment mergedEnchant : mergedEnchants.keySet()) {
			if (mergedEnchant != null) {
				int inputEnchantLevel = inputEnchants.getOrDefault(mergedEnchant, 0);
				int mergedEnchantLevel = mergedEnchants.get(mergedEnchant);

				if (!overmerge) {
					mergedEnchantLevel = inputEnchantLevel == mergedEnchantLevel ? (mergedEnchantLevel + 1 > mergedEnchant.getMaxLevel() ? mergedEnchant.getMaxLevel() : mergedEnchantLevel + 1) : Math.max(mergedEnchantLevel, inputEnchantLevel);
				} else {
					mergedEnchantLevel = inputEnchantLevel > 0 ? Math.max(mergedEnchantLevel, inputEnchantLevel) + 1 : Math.max(mergedEnchantLevel, inputEnchantLevel);
					mergedEnchantLevel = Math.min(mergedEnchantLevel, 10);
				}

				boolean compatible = mergedEnchant.canEnchant(input);
				if (input.getItem() instanceof EnchantedBookItem) {
					compatible = true;
				}

				for(Enchantment originalEnchant : inputEnchants.keySet()) {
					if (originalEnchant != mergedEnchant && !mergedEnchant.isCompatibleWith(originalEnchant)) {
						compatible = false;
					}
				}

				if (compatible) {
					if (!onlyTreasure || mergedEnchant.isTreasureOnly() || mergedEnchant.isCurse()) {
						inputEnchants.put(mergedEnchant, mergedEnchantLevel);
					}
				}
			}
		}

		EnchantmentHelper.setEnchantments(inputEnchants, returnedStack);
		return returnedStack;
	}

}
