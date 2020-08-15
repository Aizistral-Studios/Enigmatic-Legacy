package com.integral.enigmaticlegacy.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.ImmutableTriple;

import com.google.common.collect.Lists;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.IPerhaps;
import com.integral.enigmaticlegacy.api.items.ISpellstone;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.helpers.AdvancedSpawnLocationHelper;
import com.integral.enigmaticlegacy.helpers.ObfuscatedFields;
import com.integral.enigmaticlegacy.items.generic.ItemAdvancedCurio;
import com.integral.enigmaticlegacy.objects.Vector3;
import com.integral.enigmaticlegacy.packets.clients.PacketPortalParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketRecallParticles;
import com.mojang.datafixers.util.Pair;

import net.minecraft.advancements.Advancement;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.ParticleStatus;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.SpawnLocationHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

/**
 * The vessel and library for most most of the handling methods in the Enigmatic Legacy.
 *
 * @author Integral
 */

public class SuperpositionHandler {

	public static final Random random = new Random();
	public static final char[] symbols = "abcdefghijklmnopqrstuvwxyz".toUpperCase().toCharArray();

	public static HashMap<PlayerEntity, Integer> spellstoneCooldowns = new HashMap<PlayerEntity, Integer>();

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
					if (soloStackHandler.getStackInSlot(i) != null && soloStackHandler.getStackInSlot(i).getItem() instanceof ItemAdvancedCurio) {
						stackList.add(soloStackHandler.getStackInSlot(i));
					}
				}

			});
		}

		return stackList;
	}

	public static boolean isSlotLocked(String id, final LivingEntity livingEntity) {
		ICuriosItemHandler handler = CuriosApi.getCuriosHelper().getCuriosHandler(livingEntity).orElse(null);

		if (handler != null) {
			return handler.getLockedSlots().contains(id);
		} else
			return true;

	}

	public static boolean hasSpellstone(final LivingEntity entity) {
		return SuperpositionHandler.getSpellstone(entity) != null;
	}


	public static ItemStack getSpellstone(final LivingEntity entity) {
		List<ItemStack> spellstoneStack = new ArrayList<ItemStack>();

		CuriosApi.getCuriosHelper().getCuriosHandler(entity).ifPresent(handler -> {

			ICurioStacksHandler stacksHandler = handler.getCurios().get("spellstone");

			if (stacksHandler != null) {

				IDynamicStackHandler soloStackHandler = stacksHandler.getStacks();

				if (soloStackHandler != null)
					for (int i = 0; i < stacksHandler.getSlots(); i++) {
						if (soloStackHandler.getStackInSlot(i) != null && soloStackHandler.getStackInSlot(i).getItem() instanceof ISpellstone) {
							spellstoneStack.add(soloStackHandler.getStackInSlot(i));
							break;
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
		if (data.isPresent()) {
			return data.get().getRight();
		}
		return null;
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

		if (!isEnabled)
			message.lock();
		if (isHidden)
			message.hide();

		if (icon != null)
			message.icon(icon);

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
		return new AxisAlignedBB(entity.getPosX() - radius, entity.getPosY() - radius, entity.getPosZ() - radius,
				entity.getPosX() + radius, entity.getPosY() + radius, entity.getPosZ() + radius);
	}

	/**
	 * Accelerates the entity towards specific point.
	 *
	 * @param originalPosVector Absolute coordinates of the point entity should move
	 *                          towards.
	 * @param modifier          Applied velocity modifier.
	 */

	public static void setEntityMotionFromVector(final Entity entity, final Vector3 originalPosVector,
			final float modifier) {
		final Vector3 entityVector = Vector3.fromEntityCenter(entity);
		Vector3 finalVector = originalPosVector.subtract(entityVector);
		if (finalVector.mag() > 1.0) {
			finalVector = finalVector.normalize();
		}
		entity.setMotion(finalVector.x * modifier, finalVector.y * modifier, finalVector.z * modifier);
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
	public static LivingEntity getObservedEntity(final PlayerEntity player, final World world, final float range,
			final int maxDist) {
		LivingEntity newTarget = null;
		Vector3 target = Vector3.fromEntityCenter(player);
		List<LivingEntity> entities = new ArrayList<LivingEntity>();
		for (int distance = 1; entities.size() == 0 && distance < maxDist; ++distance) {
			target = target.add(new Vector3(player.getLookVec()).multiply(distance)).add(0.0, 0.5, 0.0);
			entities = player.world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(target.x - range,
					target.y - range, target.z - range, target.x + range, target.y + range, target.z + range));
			if (entities.contains(player)) {
				entities.remove(player);
			}
		}
		if (entities.size() > 0) {
			newTarget = entities.get(0);
		}
		return newTarget;
	}

	/**
	 * Returns the given player's spellstone cooldown.
	 */

	public static int getSpellstoneCooldown(PlayerEntity player) {
		if (!player.world.isRemote) {
			if (SuperpositionHandler.spellstoneCooldowns.containsKey(player))
				return SuperpositionHandler.spellstoneCooldowns.get(player);
			else {
				SuperpositionHandler.spellstoneCooldowns.put(player, 0);
				return 0;
			}
		}

		return 0;
	}

	/**
	 * Sets the given player's spellstone cooldown to specified value.
	 */

	public static void setSpellstoneCooldown(PlayerEntity player, int cooldown) {
		if (!player.world.isRemote) {
			SuperpositionHandler.spellstoneCooldowns.put(player, cooldown);
		}

		return;
	}

	/**
	 * Checks whether player's spellstone cooldown is greater than zero.
	 */

	public static boolean hasSpellstoneCooldown(PlayerEntity player) {
		if (!player.world.isRemote) {

			if (SuperpositionHandler.getSpellstoneCooldown(player) > 0)
				return true;
			else
				return false;

		}

		return false;
	}

	/**
	 * Sets the player's rotations so that they will looking at specified point in
	 * space.
	 */

	@OnlyIn(Dist.CLIENT)
	public static void lookAt(double px, double py, double pz, ClientPlayerEntity me) {
		double dirx = me.getPosX() - px;
		double diry = me.getPosY() - py;
		double dirz = me.getPosZ() - pz;

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
		me.rotationPitch = (float) pitch;
		me.rotationYaw = (float) yaw;
		// me.rotationYawHead = (float)yaw;
	}

	/**
	 * Attempts to teleport entity at given coordinates, or nearest valid location
	 * on Y axis.
	 *
	 * @return True if successfull, false otherwise.
	 */

	public static boolean validTeleport(Entity entity, double x_init, double y_init, double z_init, World world,
			int checkAxis) {

		int x = (int) x_init;
		int y = (int) y_init;
		int z = (int) z_init;

		BlockState block = world.getBlockState(new BlockPos(x, y - 1, z));

		if (world.isAirBlock(new BlockPos(x, y - 1, z)) & block.isSolid()) {

			for (int counter = 0; counter <= checkAxis; counter++) {

				if (!world.isAirBlock(new BlockPos(x, y + counter - 1, z))
						& world.getBlockState(new BlockPos(x, y + counter - 1, z)).isSolid()
						& world.isAirBlock(new BlockPos(x, y + counter, z))
						& world.isAirBlock(new BlockPos(x, y + counter + 1, z))) {

					world.playSound(null, entity.func_233580_cy_(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 1.0F, (float) (0.8F + (Math.random() * 0.2D)));

					EnigmaticLegacy.packetInstance.send(
							PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getPosX(),
									entity.getPosY(), entity.getPosZ(), 128, entity.world.func_234923_W_())),
							new PacketPortalParticles(entity.getPosX(), entity.getPosY() + (entity.getHeight() / 2),
									entity.getPosZ(), 72, 1.0F, false));

					if (entity instanceof ServerPlayerEntity) {
						ServerPlayerEntity player = (ServerPlayerEntity) entity;
						player.setPositionAndUpdate(x + 0.5, y + counter, z + 0.5);
					} else
						((LivingEntity) entity).setPositionAndUpdate(x + 0.5, y + counter, z + 0.5);

					world.playSound(null, entity.func_233580_cy_(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 1.0F, (float) (0.8F + (Math.random() * 0.2D)));

					EnigmaticLegacy.packetInstance.send(
							PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getPosX(),
									entity.getPosY(), entity.getPosZ(), 128, entity.world.func_234923_W_())),
							new PacketRecallParticles(entity.getPosX(), entity.getPosY() + (entity.getHeight() / 2),
									entity.getPosZ(), 48, false));

					return true;
				}

			}

		} else {

			for (int counter = 0; counter <= checkAxis; counter++) {

				if (!world.isAirBlock(new BlockPos(x, y - counter - 1, z))
						& world.getBlockState(new BlockPos(x, y - counter - 1, z)).isSolid()
						& world.isAirBlock(new BlockPos(x, y - counter, z))
						& world.isAirBlock(new BlockPos(x, y - counter + 1, z))) {

					world.playSound(null, entity.func_233580_cy_(), SoundEvents.ENTITY_ENDERMAN_TELEPORT,
							SoundCategory.HOSTILE, 1.0F, (float) (0.8F + (Math.random() * 0.2D)));
					EnigmaticLegacy.packetInstance.send(
							PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getPosX(),
									entity.getPosY(), entity.getPosZ(), 128, entity.world.func_234923_W_())),
							new PacketRecallParticles(entity.getPosX(), entity.getPosY() + (entity.getHeight() / 2),
									entity.getPosZ(), 48, false));

					if (entity instanceof ServerPlayerEntity) {
						ServerPlayerEntity player = (ServerPlayerEntity) entity;
						player.setPositionAndUpdate(x + 0.5, y - counter, z + 0.5);
					} else
						((LivingEntity) entity).setPositionAndUpdate(x + 0.5, y - counter, z + 0.5);

					world.playSound(null, entity.func_233580_cy_(), SoundEvents.ENTITY_ENDERMAN_TELEPORT,
							SoundCategory.HOSTILE, 1.0F, (float) (0.8F + (Math.random() * 0.2D)));
					EnigmaticLegacy.packetInstance.send(
							PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getPosX(),
									entity.getPosY(), entity.getPosZ(), 128, entity.world.func_234923_W_())),
							new PacketRecallParticles(entity.getPosX(), entity.getPosY() + (entity.getHeight() / 2),
									entity.getPosZ(), 48, false));

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

		double x = entity.getPosX() + ((Math.random() - 0.5D) * d);
		double y = entity.getPosY() + ((Math.random() - 0.5D) * d);
		double z = entity.getPosZ() + ((Math.random() - 0.5D) * d);
		return SuperpositionHandler.validTeleport(entity, x, y, z, world, radius);
	}

	/**
	 * Builds standart loot pool with any amount of ItemLootEntries.
	 */

	public static LootPool constructLootPool(String poolName, float minRolls, float maxRolls,
			@Nullable LootEntry.Builder<?>... entries) {

		Builder poolBuilder = LootPool.builder();
		poolBuilder.name(poolName);
		poolBuilder.rolls(RandomValueRange.of(minRolls, maxRolls));

		for (LootEntry.Builder<?> entry : entries) {
			if (entry != null)
				poolBuilder.addEntry(entry);
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
	public static StandaloneLootEntry.Builder<?> createOptionalLootEntry(Item item,
			int weight, float minCount, float maxCount) {

		if (item instanceof IPerhaps) {
			IPerhaps perhaps = (IPerhaps) item;

			if (!perhaps.isForMortals())
				return null;
		}
		return ItemLootEntry.builder(item).weight(weight)
				.acceptFunction(SetCount.builder(RandomValueRange.of(minCount, maxCount)));
	}

	/**
	 * Creates ItemLootEntry builder for an item. If item is disabled in config,
	 * returns null instead.
	 */

	@Nullable
	public static StandaloneLootEntry.Builder<?> createOptionalLootEntry(Item item,
			int weight) {

		if (item instanceof IPerhaps) {
			IPerhaps perhaps = (IPerhaps) item;

			if (!perhaps.isForMortals())
				return null;
		}
		return ItemLootEntry.builder(item).weight(weight);
	}

	/**
	 * Creates ItemLootEntry with a given weight, randomly ranged damage and
	 * level-based enchantments. Damage should be specified as modifier, where 1.0
	 * is full durability.
	 *
	 * @return
	 */

	public static ItemLootEntry.Builder<?> itemEntryBuilderED(Item item, int weight, float enchantLevelMin,
			float enchantLevelMax, float damageMin, float damageMax) {
		ItemLootEntry.Builder<?> builder = ItemLootEntry.builder(item);

		builder.weight(weight);
		builder.acceptFunction(SetDamage.func_215931_a(RandomValueRange.of(damageMax, damageMin)));
		builder.acceptFunction(
				EnchantWithLevels.func_215895_a(RandomValueRange.of(enchantLevelMin, enchantLevelMax)).func_216059_e());

		return builder;
	}

	public static List<ResourceLocation> getEarthenDungeons() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(LootTables.CHESTS_SIMPLE_DUNGEON);
		lootChestList.add(LootTables.CHESTS_ABANDONED_MINESHAFT);
		lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_ARMORER);

		return lootChestList;
	}

	public static List<ResourceLocation> getWaterDungeons() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(LootTables.CHESTS_UNDERWATER_RUIN_BIG);
		lootChestList.add(LootTables.CHESTS_UNDERWATER_RUIN_SMALL);
		lootChestList.add(LootTables.CHESTS_SHIPWRECK_TREASURE);
		lootChestList.add(LootTables.CHESTS_BURIED_TREASURE);

		return lootChestList;
	}

	public static List<ResourceLocation> getFieryDungeons() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(LootTables.CHESTS_NETHER_BRIDGE);

		return lootChestList;
	}

	public static List<ResourceLocation> getAirDungeons() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_TEMPLE);

		return lootChestList;
	}

	public static List<ResourceLocation> getEnderDungeons() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(LootTables.CHESTS_END_CITY_TREASURE);

		return lootChestList;
	}

	public static List<ResourceLocation> getMergedAir$EarthenDungeons() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(LootTables.CHESTS_DESERT_PYRAMID);
		lootChestList.add(LootTables.CHESTS_JUNGLE_TEMPLE);

		return lootChestList;
	}

	public static List<ResourceLocation> getMergedEnder$EarthenDungeons() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(LootTables.CHESTS_STRONGHOLD_CORRIDOR);
		lootChestList.add(LootTables.CHESTS_STRONGHOLD_CROSSING);

		return lootChestList;
	}

	public static List<ResourceLocation> getOverworldDungeons() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(LootTables.CHESTS_SIMPLE_DUNGEON);
		lootChestList.add(LootTables.CHESTS_ABANDONED_MINESHAFT);
		lootChestList.add(LootTables.CHESTS_STRONGHOLD_CROSSING);
		lootChestList.add(LootTables.CHESTS_STRONGHOLD_CORRIDOR);
		lootChestList.add(LootTables.CHESTS_DESERT_PYRAMID);
		lootChestList.add(LootTables.CHESTS_JUNGLE_TEMPLE);
		lootChestList.add(LootTables.CHESTS_IGLOO_CHEST);
		lootChestList.add(LootTables.CHESTS_WOODLAND_MANSION);
		lootChestList.add(LootTables.CHESTS_UNDERWATER_RUIN_SMALL);
		lootChestList.add(LootTables.CHESTS_UNDERWATER_RUIN_BIG);
		lootChestList.add(LootTables.CHESTS_SHIPWRECK_SUPPLY);
		lootChestList.add(LootTables.CHESTS_PILLAGER_OUTPOST);

		return lootChestList;
	}

	public static List<ResourceLocation> getVillageChests() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_WEAPONSMITH);
		lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_TOOLSMITH);
		lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_ARMORER);
		lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_CARTOGRAPHER);
		lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_MASON);
		lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_SHEPHERD);
		lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_BUTCHER);
		lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_FLETCHER);
		lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_FISHER);
		lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_TANNERY);
		lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_TEMPLE);
		lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_DESERT_HOUSE);
		lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_PLAINS_HOUSE);
		lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_TAIGA_HOUSE);
		lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_SNOWY_HOUSE);
		lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_SAVANNA_HOUSE);

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

		if (persistent.contains(tag)) {
			return persistent.get(tag);
		} else {
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
		return theTag instanceof ByteNBT ? ((ByteNBT)theTag).getByte() != 0 : expectedValue;
	}

	public static void setPersistentInteger(PlayerEntity player, String tag, int value) {
		SuperpositionHandler.setPersistentTag(player, tag, IntNBT.valueOf(value));
	}

	public static int getPersistentInteger(PlayerEntity player, String tag, int expectedValue) {
		INBT theTag = SuperpositionHandler.getPersistentTag(player, tag, IntNBT.valueOf(expectedValue));
		return theTag instanceof IntNBT ? ((IntNBT)theTag).getInt() : expectedValue;
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

	public static boolean hasStoredAnvilField(PlayerEntity entity) {
		if (EnigmaticEventHandler.anvilFields.containsKey(entity))
			if (EnigmaticEventHandler.anvilFields.get(entity) != null
					&& !EnigmaticEventHandler.anvilFields.get(entity).equals(""))
				return true;

		return false;
	}

	/**
	 * Checks whether or not player has completed specified advancement.
	 */

	public static boolean hasAdvancement(@Nonnull ServerPlayerEntity player, @Nonnull ResourceLocation location) {

		try {
			if (player.getAdvancements().getProgress(player.server.getAdvancementManager().getAdvancement(location)).isDone())
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
		Advancement adv = player.server.getAdvancementManager().getAdvancement(location);

		for (String criterion : player.getAdvancements().getProgress(adv).getRemaningCriteria()) {
			player.getAdvancements().grantCriterion(adv, criterion);
		}

	}

	/**
	 * Revokes specified advancement from specified player.
	 * Don't forget to specify!
	 */

	public static void revokeAdvancement(@Nonnull ServerPlayerEntity player, @Nonnull ResourceLocation location) {
		Advancement adv = player.server.getAdvancementManager().getAdvancement(location);

		for (String criterion : player.getAdvancements().getProgress(adv).getCompletedCriteria()) {
			player.getAdvancements().revokeCriterion(adv, criterion);
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
			number = number.concat("" + SuperpositionHandler.symbols[SuperpositionHandler.random
					.nextInt(SuperpositionHandler.symbols.length)]);
		}

		while (number.length() < 9) {
			number = number.concat("" + SuperpositionHandler.random.nextInt(10));
		}

		return number;
	}

	public static PlayerEntity getPlayerByName(World world, String name) {
		PlayerEntity player = null;

		for (PlayerEntity checkedPlayer : world.getPlayers()) {
			if (checkedPlayer.getDisplayName().getString().equals(name))
				player = checkedPlayer;
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
	public static void addPotionTooltip(List<EffectInstance> list, ItemStack itemIn, List<ITextComponent> lores,
			float durationFactor) {
		List<Pair<Attribute, AttributeModifier>> list1 = Lists.newArrayList();
		if (list.isEmpty()) {
			lores.add((new TranslationTextComponent("effect.none")).func_240699_a_(TextFormatting.GRAY));
		} else {
	         for(EffectInstance effectinstance : list) {
	             IFormattableTextComponent iformattabletextcomponent = new TranslationTextComponent(effectinstance.getEffectName());
	             Effect effect = effectinstance.getPotion();
	             Map<Attribute, AttributeModifier> map = effect.getAttributeModifierMap();
	             if (!map.isEmpty()) {
	                for(Entry<Attribute, AttributeModifier> entry : map.entrySet()) {
	                   AttributeModifier attributemodifier = entry.getValue();
	                   AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), effect.getAttributeModifierAmount(effectinstance.getAmplifier(), attributemodifier), attributemodifier.getOperation());
	                   list1.add(new Pair<>(entry.getKey(), attributemodifier1));
	                }
	             }

	             if (effectinstance.getAmplifier() > 0) {
	                iformattabletextcomponent.func_240702_b_(" ").func_230529_a_(new TranslationTextComponent("potion.potency." + effectinstance.getAmplifier()));
	             }

	             if (effectinstance.getDuration() > 20) {
	                iformattabletextcomponent.func_240702_b_(" (").func_240702_b_(EffectUtils.getPotionDurationString(effectinstance, durationFactor)).func_240702_b_(")");
	             }

	             lores.add(iformattabletextcomponent.func_240699_a_(effect.getEffectType().getColor()));
	          }
	       }

		if (!list1.isEmpty()) {
	         lores.add(StringTextComponent.field_240750_d_);
	         lores.add((new TranslationTextComponent("potion.whenDrank")).func_240699_a_(TextFormatting.DARK_PURPLE));

	         for(Pair<Attribute, AttributeModifier> pair : list1) {
	            AttributeModifier attributemodifier2 = pair.getSecond();
	            double d0 = attributemodifier2.getAmount();
	            double d1;
	            if (attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
	               d1 = attributemodifier2.getAmount();
	            } else {
	               d1 = attributemodifier2.getAmount() * 100.0D;
	            }

	            if (d0 > 0.0D) {
	               lores.add((new TranslationTextComponent("attribute.modifier.plus." + attributemodifier2.getOperation().getId(), ItemStack.DECIMALFORMAT.format(d1), new TranslationTextComponent(pair.getFirst().func_233754_c_()))).func_240699_a_(TextFormatting.BLUE));
	            } else if (d0 < 0.0D) {
	               d1 = d1 * -1.0D;
	               lores.add((new TranslationTextComponent("attribute.modifier.take." + attributemodifier2.getOperation().getId(), ItemStack.DECIMALFORMAT.format(d1), new TranslationTextComponent(pair.getFirst().func_233754_c_()))).func_240699_a_(TextFormatting.DARK_RED));
	            }
	         }
	      }

	}

	/**
	 * @return True if ItemStack can be added to player's inventory (fully or
	 *         partially), false otherwise.
	 */

	public static boolean canPickStack(PlayerEntity player, ItemStack stack) {

		if (player.inventory.getFirstEmptyStack() >= 0)
			return true;
		else {
			List<ItemStack> allInventories = new ArrayList<ItemStack>();

			// allInventories.addAll(player.inventory.armorInventory);
			allInventories.addAll(player.inventory.mainInventory);
			allInventories.addAll(player.inventory.offHandInventory);

			for (ItemStack invStack : allInventories) {
				if (SuperpositionHandler.canMergeStacks(invStack, stack, player.inventory.getInventoryStackLimit())) {
					return true;
				}
			}
		}

		return false;
	}

	public static boolean canMergeStacks(ItemStack stack1, ItemStack stack2, int invStackLimit) {
		return !stack1.isEmpty() && SuperpositionHandler.stackEqualExact(stack1, stack2) && stack1.isStackable()
				&& stack1.getCount() < stack1.getMaxStackSize() && stack1.getCount() < invStackLimit;
	}

	/**
	 * Checks item, NBT, and meta if the item is not damageable
	 */
	public static boolean stackEqualExact(ItemStack stack1, ItemStack stack2) {
		return stack1.getItem() == stack2.getItem() && ItemStack.areItemStackTagsEqual(stack1, stack2);
	}

	/**
	 * @return Multiplier for amount of particles based on client settings.
	 */
	@OnlyIn(Dist.CLIENT)
	public static float getParticleMultiplier() {

		if (Minecraft.getInstance().gameSettings.particles == ParticleStatus.MINIMAL) {
			return 0.35F;
		} else if (Minecraft.getInstance().gameSettings.particles == ParticleStatus.DECREASED) {
			return 0.65F;
		} else {
			return 1.0F;
		}

	}

	/**
	 * Checks whether or on the player is within the range of any active beacon.
	 */

	@SuppressWarnings("unchecked")
	public static boolean isInBeaconRange(PlayerEntity player) {
		List<BeaconTileEntity> list = new ArrayList<BeaconTileEntity>();
		boolean inRange = false;

		for (TileEntity tile : player.world.loadedTileEntityList) {
			if (tile instanceof BeaconTileEntity)
				list.add((BeaconTileEntity)tile);
		}

		if (list.size() > 0)
			for (BeaconTileEntity beacon : list)
				if (beacon.getLevels() > 0) {
					try {
						if (((List<BeaconTileEntity.BeamSegment>)ObfuscatedFields.beamSegmentsField.get(beacon)).isEmpty())
							continue;
					} catch (Exception ex) {
						ex.printStackTrace();
					}

					int range = (beacon.getLevels()+1)*10;
					double distance = Math.sqrt(beacon.getPos().distanceSq(player.getPosX(), beacon.getPos().getY(), player.getPosZ(), true));

					if (distance <= range)
						inRange = true;
				}

		return inRange;
	}

	public static boolean hasItem(PlayerEntity player, Item item) {
		return player.inventory.hasItemStack(new ItemStack(item));
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

	@SuppressWarnings("unused")
	public static boolean shouldPlayerDropSoulCrystal(PlayerEntity player) {
		int dropMode = ConfigHandler.SOUL_CRYSTALS_MODE.getValue();
		int maxCrystalLoss = ConfigHandler.MAX_SOUL_CRYSTAL_LOSS.getValue();

		if (true)
			return false;

		if (dropMode == 0) {
			return false;
		} else if (dropMode == 1) {
			return player.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY) && EnigmaticLegacy.soulCrystal.getLostCrystals(player) < maxCrystalLoss;
		} else if (dropMode == 2) {
			return EnigmaticLegacy.soulCrystal.getLostCrystals(player) < maxCrystalLoss;
		}

		return false;
	}

	public static ServerWorld getWorld(RegistryKey<World> key) {
		return ServerLifecycleHooks.getCurrentServer().getWorld(key);
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

	public static void backToSpawn(ServerPlayerEntity serverPlayer) {
		RegistryKey<World> respawnDimension = AdvancedSpawnLocationHelper.getPlayerRespawnDimension(serverPlayer);
		ServerWorld respawnWorld = SuperpositionHandler.getWorld(respawnDimension);

		serverPlayer.world.playSound(null, serverPlayer.func_233580_cy_(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));

		EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(serverPlayer.getPosX(), serverPlayer.getPosY(), serverPlayer.getPosZ(), 128, serverPlayer.world.func_234923_W_())), new PacketPortalParticles(serverPlayer.getPosX(), serverPlayer.getPosY() + (serverPlayer.getHeight() / 2), serverPlayer.getPosZ(), 100, 1.25F, false));

		Optional<Vector3d> vec = AdvancedSpawnLocationHelper.getValidSpawn(respawnWorld, serverPlayer);
		Optional<Vector3d> vec2;
		ServerWorld destinationWorld = vec.isPresent() ? respawnWorld : serverPlayer.server.func_241755_D_();

		if (!serverPlayer.getServerWorld().equals(destinationWorld)) {
			serverPlayer.changeDimension(destinationWorld, new RealSmoothTeleporter());
		}

		if (!respawnWorld.equals(destinationWorld)) {
			 vec2 = AdvancedSpawnLocationHelper.getValidSpawn(destinationWorld, serverPlayer);
		} else
			vec2 = Optional.empty();

		if (vec.isPresent()) {
			Vector3d trueVec = vec.get();
			serverPlayer.setPositionAndUpdate(trueVec.x, trueVec.y, trueVec.z);
		} else if (vec2.isPresent()) {
			Vector3d trueVec = vec2.get();
			serverPlayer.setPositionAndUpdate(trueVec.x, trueVec.y, trueVec.z);
		} else {
			AdvancedSpawnLocationHelper.fuckBackToSpawn(serverPlayer.getServerWorld(), serverPlayer);
		}

		serverPlayer.world.playSound(null, serverPlayer.func_233580_cy_(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));

		EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(serverPlayer.getPosX(), serverPlayer.getPosY(), serverPlayer.getPosZ(), 128, serverPlayer.world.func_234923_W_())), new PacketRecallParticles(serverPlayer.getPosX(), serverPlayer.getPosY() + (serverPlayer.getHeight() / 2), serverPlayer.getPosZ(), 48, false));
	}

}
