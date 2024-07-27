package com.aizistral.enigmaticlegacy.handlers;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.items.*;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gameevent.GameEvent;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.capabilities.IPlaytimeCounter;
import com.aizistral.enigmaticlegacy.api.generic.ConfigurableItem;
import com.aizistral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.aizistral.enigmaticlegacy.api.items.ISpellstone;
import com.aizistral.enigmaticlegacy.api.quack.IProperShieldUser;
import com.aizistral.enigmaticlegacy.config.OmniconfigHandler;
import com.aizistral.enigmaticlegacy.helpers.AdvancedSpawnLocationHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemSpellstoneCurio;
import com.aizistral.enigmaticlegacy.objects.DimensionalPosition;
import com.aizistral.enigmaticlegacy.objects.EnigmaticTransience;
import com.aizistral.enigmaticlegacy.objects.TransientPlayerData;
import com.aizistral.enigmaticlegacy.objects.Vector3;
import com.aizistral.enigmaticlegacy.packets.clients.PacketPortalParticles;
import com.aizistral.enigmaticlegacy.packets.clients.PacketRecallParticles;
import com.aizistral.enigmaticlegacy.packets.clients.PacketUpdateCompass;
import com.aizistral.enigmaticlegacy.registries.EnigmaticEffects;
import com.aizistral.enigmaticlegacy.registries.EnigmaticEnchantments;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.aizistral.omniconfig.Configuration;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;
import com.google.common.base.Objects;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Tuple;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelSummary;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootPool.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * The core and vessel for most most of the handling methods in the Enigmatic Legacy.
 *
 * @author Integral
 */

public class SuperpositionHandler {
	public static final Random RANDOM = new Random();
	public static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toUpperCase().toCharArray();
	public static final UUID SCROLL_SLOT_UUID = UUID.fromString("ae465e52-ffc2-4f57-b09a-066aa0cea3d4");
	public static final UUID SPELLSTONE_SLOT_UUID = UUID.fromString("63df175a-0d6d-4163-8ef1-218bcb42feba");
	public static final UUID RING_SLOT_UUID = UUID.fromString("76012386-aa31-4c17-8d6a-e9dd29affcb0");
	public static final UUID CHARM_SLOT_UUID = UUID.fromString("485121e7-b670-45dc-b014-4c8b8f62283d");
	public static final char[] UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	public static final char[] LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	public static final char[] NUMBERS = "0123456789".toCharArray();
	public static final char[] SPECIAL_SYMBOLS = "-+=(){}[]':;./,<>*&^%$#@!?~".toCharArray();

	public static boolean hasAdvancedCurios(final LivingEntity entity) {
		return SuperpositionHandler.getAdvancedCurios(entity).size() > 0;
	}

	public static boolean unlockSpecialSlot(String slot, Player player) {
		if (!slot.equals("scroll") && !slot.equals("spellstone") && !slot.equals("ring") && !slot.equals("charm"))
			throw new IllegalArgumentException("Slot type '" + slot + "' is not supported!");

		MutableBoolean success = new MutableBoolean(false);
		UUID id = slot.equals("scroll") ? SCROLL_SLOT_UUID : (slot.equals("spellstone") ? SPELLSTONE_SLOT_UUID
				: (slot.equals("ring") ? RING_SLOT_UUID : CHARM_SLOT_UUID));

		ICuriosHelper apiHelper = CuriosApi.getCuriosHelper();

		apiHelper.getCuriosHandler(player).ifPresent(handler -> handler.getStacksHandler(slot).ifPresent(stacks -> {
			Map<UUID, AttributeModifier> map = stacks.getModifiers();
			if (!stacks.getModifiers().containsKey(id)) {
				stacks.addPermanentModifier(new AttributeModifier(id, "Masterslot", 1, Operation.ADDITION));
				success.setTrue();
			}
		}));

		return success.getValue();
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

	public static boolean tryForceEquip(LivingEntity entity, ItemStack curio) {
		if (!(curio.getItem() instanceof ICurioItem))
			throw new IllegalArgumentException("I fear for now this only works with ICurioItem");

		MutableBoolean equipped = new MutableBoolean(false);
		ICurioItem item = (ICurioItem) curio.getItem();

		CuriosApi.getCuriosHelper().getCuriosHandler(entity).ifPresent(handler -> {
			if (!entity.level().isClientSide) {
				Map<String, ICurioStacksHandler> curios = handler.getCurios();

				cycle: for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
					IDynamicStackHandler stackHandler = entry.getValue().getStacks();

					for (int i = 0; i < stackHandler.getSlots(); i++) {
						ItemStack present = stackHandler.getStackInSlot(i);
						Set<String> tags = CuriosApi.getCuriosHelper().getCurioTags(curio.getItem());
						String id = entry.getKey();

						SlotContext context = new SlotContext(id, entity, i, false, entry.getValue().isVisible());

						if (present.isEmpty() && (tags.contains(id) || tags.contains("curio")) && item.canEquip(context, curio)) {
							stackHandler.setStackInSlot(i, curio);
							item.playRightClickEquipSound(entity, curio);
							equipped.setTrue();
							break cycle;
						}
					}
				}

			}
		});

		return equipped.booleanValue();
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
	public static void registerCurioType(final String identifier, final int slots, final boolean isHidden, @Nullable final ResourceLocation icon) {
		final SlotTypeMessage.Builder message = new SlotTypeMessage.Builder(identifier);

		message.size(slots);

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
		ResourceLocation location = new ResourceLocation("enigmaticlegacy", soundName);
		SoundEvent event = SoundEvent.createVariableRangeEvent(location);
		ForgeRegistries.SOUND_EVENTS.register(location, event);
		return event;
	}

	/**
	 * Creates and returns simple bounding box of given radius around the entity.
	 */

	public static AABB getBoundingBoxAroundEntity(final Entity entity, final double radius) {
		return new AABB(entity.getX() - radius, entity.getY() - radius, entity.getZ() - radius, entity.getX() + radius, entity.getY() + radius, entity.getZ() + radius);
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
	public static LivingEntity getObservedEntity(Player player, Level world, float range, int maxDist) {
		List<LivingEntity> entities = getObservedEntities(player, world, range, maxDist, true);
		return entities.size() > 0 ? entities.get(0) : null;
	}

	public static List<LivingEntity> getObservedEntities(Player player, Level world, float range, int maxDist, boolean stopWhenFound) {
		Vector3 target = Vector3.fromEntityCenter(player);
		List<LivingEntity> entities = new ArrayList<>();

		for (int distance = 1; distance < maxDist; ++distance) {
			target = target.add(new Vector3(player.getLookAngle()).multiply(distance)).add(0.0, 0.5, 0.0);
			List<LivingEntity> list = player.level().getEntitiesOfClass(LivingEntity.class, new AABB(target.x - range, target.y - range, target.z - range, target.x + range, target.y + range, target.z + range));
			list.removeIf(entity -> entity == player || !player.hasLineOfSight(entity));
			entities.addAll(list);

			if (stopWhenFound && entities.size() > 0) {
				break;
			}
		}

		return entities;
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

	public static boolean doesObserveEntity(Player player, LivingEntity entity) {
		Vec3 vector3d = player.getViewVector(1.0F).normalize();
		Vec3 vector3d1 = new Vec3(entity.getX() - player.getX(), entity.getEyeY() - player.getEyeY(), entity.getZ() - player.getZ());
		double d0 = vector3d1.length();
		vector3d1 = vector3d1.normalize();
		double d1 = vector3d.dot(vector3d1);

		return d1 > 1.0D - 0.025D / d0 ? player.hasLineOfSight(entity) : false;
	}


	public static int getSpellstoneCooldown(Player player) {
		return TransientPlayerData.get(player).getSpellstoneCooldown();
	}

	public static void setSpellstoneCooldown(Player playerIn, int value) {
		TransientPlayerData.get(playerIn).setSpellstoneCooldown(value);
	}

	public static void tickSpellstoneCooldown(Player player, int decrementedTicks) {
		TransientPlayerData data = TransientPlayerData.get(player);
		data.spellstoneCooldown = data.getSpellstoneCooldown()-decrementedTicks;

		return;
	}

	/**
	 * Checks whether player's spellstone cooldown is greater than zero.
	 */

	public static boolean hasSpellstoneCooldown(Player player) {
		return TransientPlayerData.get(player).getSpellstoneCooldown() > 0;
	}

	/**
	 * Sets the player's rotations so that they will looking at specified point in
	 * space.
	 */

	@OnlyIn(Dist.CLIENT)
	public static void lookAt(double px, double py, double pz, LocalPlayer me) {
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
		me.setXRot((float) pitch);
		me.setYRot((float) yaw);
		// me.rotationYawHead = (float)yaw;
	}

	/**
	 * Attempts to teleport entity at given coordinates, or nearest valid location
	 * on Y axis.
	 *
	 * @return True if successfull, false otherwise.
	 */

	public static boolean validTeleport(Entity entity, double x_init, double y_init, double z_init, Level world, int checkAxis) {
		int x = (int) x_init;
		int y = (int) y_init;
		int z = (int) z_init;

		BlockState block = world.getBlockState(new BlockPos(x, y - 1, z));

		if (!world.isEmptyBlock(new BlockPos(x, y - 1, z)) && block.canOcclude()) {
			for (int counter = 0; counter <= checkAxis; counter++) {
				if (!world.isEmptyBlock(new BlockPos(x, y + counter - 1, z)) && world.getBlockState(new BlockPos(x, y + counter - 1, z)).canOcclude() && world.isEmptyBlock(new BlockPos(x, y + counter, z)) && world.isEmptyBlock(new BlockPos(x, y + counter + 1, z))) {
					world.playSound(null, entity.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2D)));

					EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 128, entity.level().dimension())), new PacketPortalParticles(entity.getX(), entity.getY() + (entity.getBbHeight() / 2), entity.getZ(), 72, 1.0F, false));

					if (entity instanceof ServerPlayer) {
						ServerPlayer player = (ServerPlayer) entity;
						player.teleportTo(x + 0.5, y + counter, z + 0.5);
					} else {
						((LivingEntity) entity).teleportTo(x + 0.5, y + counter, z + 0.5);
					}

					world.playSound(null, entity.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2D)));
					EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 128, entity.level().dimension())), new PacketRecallParticles(entity.getX(), entity.getY() + (entity.getBbHeight() / 2), entity.getZ(), 48, false));
					return true;
				}
			}
		} else {
			for (int counter = 0; counter <= checkAxis; counter++) {
				if (!world.isEmptyBlock(new BlockPos(x, y - counter - 1, z)) && world.getBlockState(new BlockPos(x, y - counter - 1, z)).canOcclude() && world.isEmptyBlock(new BlockPos(x, y - counter, z)) && world.isEmptyBlock(new BlockPos(x, y - counter + 1, z))) {
					world.playSound(null, entity.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2D)));
					EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 128, entity.level().dimension())), new PacketRecallParticles(entity.getX(), entity.getY() + (entity.getBbHeight() / 2), entity.getZ(), 48, false));

					if (entity instanceof ServerPlayer) {
						ServerPlayer player = (ServerPlayer) entity;
						player.teleportTo(x + 0.5, y - counter, z + 0.5);
					} else {
						((LivingEntity) entity).teleportTo(x + 0.5, y - counter, z + 0.5);
					}

					world.playSound(null, entity.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2D)));
					EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 128, entity.level().dimension())), new PacketRecallParticles(entity.getX(), entity.getY() + (entity.getBbHeight() / 2), entity.getZ(), 48, false));
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
	public static boolean validTeleportRandomly(Entity entity, Level world, int radius) {
		int d = radius * 2;

		double x = entity.getX() + ((Math.random() - 0.5D) * d);
		double y = entity.getY() + ((Math.random() - 0.5D) * d);
		double z = entity.getZ() + ((Math.random() - 0.5D) * d);
		return SuperpositionHandler.validTeleport(entity, x, y, z, world, radius);
	}

	/**
	 * Builds standart loot pool with any amount of ItemLootEntries.
	 */

	public static LootPool constructLootPool(String poolName, float minRolls, float maxRolls, @Nullable LootPoolEntryContainer.Builder<?>... entries) {

		Builder poolBuilder = LootPool.lootPool();
		poolBuilder.name(poolName);
		poolBuilder.setRolls(UniformGenerator.between(minRolls, maxRolls));

		for (LootPoolEntryContainer.Builder<?> entry : entries) {
			if (entry != null) {
				poolBuilder.add(entry);
			}
		}

		LootPool constructedPool = poolBuilder.build();

		return constructedPool;

	}

	/**
	 * Creates LootItem builder for an item. If item is disabled in config,
	 * returns null instead. Count-sensitive version, allows you to specifiy min-max
	 * amounts of items generated per entry.
	 */

	@Nullable
	public static LootPoolSingletonContainer.Builder<?> createOptionalLootEntry(Item item, int weight, float minCount, float maxCount) {
		if (!OmniconfigHandler.isItemEnabled(item))
			return null;

		return LootItem.lootTableItem(item).setWeight(weight).apply(SetItemCountFunction.setCount(UniformGenerator.between(minCount, maxCount)));
	}

	/**
	 * Creates LootItem builder for an item. If item is disabled in config,
	 * returns null instead.
	 */

	@Nullable
	public static LootPoolSingletonContainer.Builder<?> createOptionalLootEntry(Item item, int weight) {
		if (!OmniconfigHandler.isItemEnabled(item))
			return null;

		return LootItem.lootTableItem(item).setWeight(weight);
	}

	/**
	 * Creates LootItem with a given weight, randomly ranged damage and
	 * level-based enchantments. Damage should be specified as modifier, where 1.0
	 * is full durability.
	 *
	 * @return
	 */

	public static LootPoolSingletonContainer.Builder<?> itemEntryBuilderED(Item item, int weight, float enchantLevelMin, float enchantLevelMax, float damageMin, float damageMax) {
		LootPoolSingletonContainer.Builder<?> builder = LootItem.lootTableItem(item);

		builder.setWeight(weight);
		builder.apply(SetItemDamageFunction.setDamage(UniformGenerator.between(damageMax, damageMin)));
		builder.apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(enchantLevelMin, enchantLevelMax)).allowTreasure());

		return builder;
	}

	public static List<ResourceLocation> getEarthenDungeons() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(BuiltInLootTables.SIMPLE_DUNGEON);
		lootChestList.add(BuiltInLootTables.ABANDONED_MINESHAFT);
		lootChestList.add(BuiltInLootTables.VILLAGE_ARMORER);

		return lootChestList;
	}

	public static List<ResourceLocation> getWaterDungeons() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(BuiltInLootTables.UNDERWATER_RUIN_BIG);
		lootChestList.add(BuiltInLootTables.UNDERWATER_RUIN_SMALL);
		lootChestList.add(BuiltInLootTables.SHIPWRECK_TREASURE);
		lootChestList.add(BuiltInLootTables.BURIED_TREASURE);

		return lootChestList;
	}

	public static List<ResourceLocation> getLibraries() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(BuiltInLootTables.STRONGHOLD_LIBRARY);
		lootChestList.add(BuiltInLootTables.SHIPWRECK_MAP);

		return lootChestList;
	}

	public static List<ResourceLocation> getBastionChests() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(BuiltInLootTables.BASTION_TREASURE);
		lootChestList.add(BuiltInLootTables.BASTION_OTHER);
		lootChestList.add(BuiltInLootTables.BASTION_BRIDGE);
		lootChestList.add(BuiltInLootTables.BASTION_HOGLIN_STABLE);

		return lootChestList;
	}

	public static List<ResourceLocation> getNetherDungeons() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(BuiltInLootTables.NETHER_BRIDGE);
		lootChestList.add(BuiltInLootTables.BASTION_TREASURE);
		lootChestList.add(BuiltInLootTables.BASTION_OTHER);
		lootChestList.add(BuiltInLootTables.BASTION_BRIDGE);
		lootChestList.add(BuiltInLootTables.BASTION_HOGLIN_STABLE);
		lootChestList.add(BuiltInLootTables.RUINED_PORTAL);

		return lootChestList;
	}

	public static List<ResourceLocation> getAirDungeons() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(BuiltInLootTables.VILLAGE_TEMPLE);

		return lootChestList;
	}

	public static List<ResourceLocation> getEnderDungeons() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(BuiltInLootTables.END_CITY_TREASURE);

		return lootChestList;
	}

	public static List<ResourceLocation> getMergedAir$EarthenDungeons() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(BuiltInLootTables.DESERT_PYRAMID);
		lootChestList.add(BuiltInLootTables.JUNGLE_TEMPLE);

		return lootChestList;
	}

	public static List<ResourceLocation> getMergedEnder$EarthenDungeons() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(BuiltInLootTables.STRONGHOLD_CORRIDOR);
		lootChestList.add(BuiltInLootTables.STRONGHOLD_CROSSING);

		return lootChestList;
	}

	public static List<ResourceLocation> getOverworldDungeons() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(BuiltInLootTables.SIMPLE_DUNGEON);
		lootChestList.add(BuiltInLootTables.ABANDONED_MINESHAFT);
		lootChestList.add(BuiltInLootTables.STRONGHOLD_CROSSING);
		lootChestList.add(BuiltInLootTables.STRONGHOLD_CORRIDOR);
		lootChestList.add(BuiltInLootTables.DESERT_PYRAMID);
		lootChestList.add(BuiltInLootTables.JUNGLE_TEMPLE);
		lootChestList.add(BuiltInLootTables.IGLOO_CHEST);
		lootChestList.add(BuiltInLootTables.WOODLAND_MANSION);
		lootChestList.add(BuiltInLootTables.UNDERWATER_RUIN_SMALL);
		lootChestList.add(BuiltInLootTables.UNDERWATER_RUIN_BIG);
		lootChestList.add(BuiltInLootTables.SHIPWRECK_SUPPLY);
		lootChestList.add(BuiltInLootTables.PILLAGER_OUTPOST);

		return lootChestList;
	}

	public static List<ResourceLocation> getVillageChests() {
		List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		lootChestList.add(BuiltInLootTables.VILLAGE_WEAPONSMITH);
		lootChestList.add(BuiltInLootTables.VILLAGE_TOOLSMITH);
		lootChestList.add(BuiltInLootTables.VILLAGE_ARMORER);
		lootChestList.add(BuiltInLootTables.VILLAGE_CARTOGRAPHER);
		lootChestList.add(BuiltInLootTables.VILLAGE_MASON);
		lootChestList.add(BuiltInLootTables.VILLAGE_SHEPHERD);
		lootChestList.add(BuiltInLootTables.VILLAGE_BUTCHER);
		lootChestList.add(BuiltInLootTables.VILLAGE_FLETCHER);
		lootChestList.add(BuiltInLootTables.VILLAGE_FISHER);
		lootChestList.add(BuiltInLootTables.VILLAGE_TANNERY);
		lootChestList.add(BuiltInLootTables.VILLAGE_TEMPLE);
		lootChestList.add(BuiltInLootTables.VILLAGE_DESERT_HOUSE);
		lootChestList.add(BuiltInLootTables.VILLAGE_PLAINS_HOUSE);
		lootChestList.add(BuiltInLootTables.VILLAGE_TAIGA_HOUSE);
		lootChestList.add(BuiltInLootTables.VILLAGE_SNOWY_HOUSE);
		lootChestList.add(BuiltInLootTables.VILLAGE_SAVANNA_HOUSE);

		return lootChestList;
	}

	/**
	 * Retrieves the given Tag type from the player's persistent NBT.
	 */

	public static Tag getPersistentTag(Player player, String tag, Tag expectedValue) {
		CompoundTag data = player.getPersistentData();
		CompoundTag persistent;

		if (!data.contains(Player.PERSISTED_NBT_TAG)) {
			data.put(Player.PERSISTED_NBT_TAG, (persistent = new CompoundTag()));
		} else {
			persistent = data.getCompound(Player.PERSISTED_NBT_TAG);
		}

		if (persistent.contains(tag))
			return persistent.get(tag);
		else
			//persistent.put(tag, expectedValue);
			return expectedValue;
	}

	/**
	 * Remove tag from the player's persistent NBT.
	 */

	public static void removePersistentTag(Player player, String tag) {
		CompoundTag data = player.getPersistentData();
		CompoundTag persistent;

		if (!data.contains(Player.PERSISTED_NBT_TAG)) {
			data.put(Player.PERSISTED_NBT_TAG, (persistent = new CompoundTag()));
		} else {
			persistent = data.getCompound(Player.PERSISTED_NBT_TAG);
		}

		if (persistent.contains(tag)) {
			persistent.remove(tag);
		}
	}

	/**
	 * Sets the given Tag type to the player's persistent NBT.
	 */

	public static void setPersistentTag(Player player, String tag, Tag value) {
		CompoundTag data = player.getPersistentData();
		CompoundTag persistent;

		if (!data.contains(Player.PERSISTED_NBT_TAG)) {
			data.put(Player.PERSISTED_NBT_TAG, (persistent = new CompoundTag()));
		} else {
			persistent = data.getCompound(Player.PERSISTED_NBT_TAG);
		}

		persistent.put(tag, value);
	}

	/**
	 * Sets the given boolean tag to the player's persistent NBT.
	 */

	public static void setPersistentBoolean(Player player, String tag, boolean value) {
		SuperpositionHandler.setPersistentTag(player, tag, ByteTag.valueOf(value));
	}

	/**
	 * Retrieves the given boolean tag from the player's persistent NBT.
	 */

	public static boolean getPersistentBoolean(Player player, String tag, boolean expectedValue) {
		Tag theTag = SuperpositionHandler.getPersistentTag(player, tag, ByteTag.valueOf(expectedValue));
		return theTag instanceof ByteTag ? ((ByteTag) theTag).getAsByte() != 0 : expectedValue;
	}

	public static void setPersistentInteger(Player player, String tag, int value) {
		SuperpositionHandler.setPersistentTag(player, tag, IntTag.valueOf(value));
	}

	public static int getPersistentInteger(Player player, String tag, int expectedValue) {
		Tag theTag = SuperpositionHandler.getPersistentTag(player, tag, IntTag.valueOf(expectedValue));
		return theTag instanceof IntTag ? ((IntTag) theTag).getAsInt() : expectedValue;
	}

	/**
	 * Checks whether player has specified tag in their persistent NBT, whatever
	 * it's type or value is.
	 */

	public static boolean hasPersistentTag(Player player, String tag) {
		CompoundTag data = player.getPersistentData();
		CompoundTag persistent;

		if (!data.contains(Player.PERSISTED_NBT_TAG)) {
			data.put(Player.PERSISTED_NBT_TAG, (persistent = new CompoundTag()));
		} else {
			persistent = data.getCompound(Player.PERSISTED_NBT_TAG);
		}

		if (persistent.contains(tag))
			return true;
		else
			return false;

	}

	/**
	 * Checks whether or not player has completed specified advancement.
	 */

	public static boolean hasAdvancement(@Nonnull ServerPlayer player, @Nonnull ResourceLocation location) {
		try {
			if (player.getAdvancements().getOrStartProgress(player.server.getAdvancements().getAdvancement(location)).isDone())
				return true;
		} catch (NullPointerException ex) {
			// Just don't do it lol }
		}

		return false;
	}

	public static boolean doesAdvancementExist(@Nonnull ResourceLocation location) {
		return ServerLifecycleHooks.getCurrentServer().getAdvancements().getAdvancement(location) != null;
	}

	/**
	 * Grant specified advancement to specified player.
	 * Don't forget to specify!
	 */

	public static void grantAdvancement(@Nonnull ServerPlayer player, @Nonnull ResourceLocation location) {
		Advancement adv = player.server.getAdvancements().getAdvancement(location);

		for (String criterion : player.getAdvancements().getOrStartProgress(adv).getRemainingCriteria()) {
			player.getAdvancements().award(adv, criterion);
		}

	}

	/**
	 * Revokes specified advancement from specified player.
	 * Don't forget to specify!
	 */

	public static void revokeAdvancement(@Nonnull ServerPlayer player, @Nonnull ResourceLocation location) {
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
			number = number.concat("" + RANDOM.nextInt(10));
		}

		number = number.concat("-");

		while (number.length() < 7) {
			number = number.concat("" + ALPHABET[RANDOM.nextInt(ALPHABET.length)]);
		}

		while (number.length() < 9) {
			number = number.concat("" + RANDOM.nextInt(10));
		}

		return number;
	}

	public static Player getPlayerByName(Level world, String name) {
		Player player = null;

		for (Player checkedPlayer : world.players()) {
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
	public static void addPotionTooltip(List<MobEffectInstance> list, ItemStack itemIn, List<Component> lores, float durationFactor) {
		List<Pair<Attribute, AttributeModifier>> list1 = Lists.newArrayList();
		if (list.isEmpty()) {
			lores.add((Component.translatable("effect.none")).withStyle(ChatFormatting.GRAY));
		} else {
			for (MobEffectInstance effectinstance : list) {
				MutableComponent iformattabletextcomponent = Component.translatable(effectinstance.getDescriptionId());
				MobEffect effect = effectinstance.getEffect();
				Map<Attribute, AttributeModifier> map = effect.getAttributeModifiers();
				if (!map.isEmpty()) {
					for (Entry<Attribute, AttributeModifier> entry : map.entrySet()) {
						AttributeModifier attributemodifier = entry.getValue();
						AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), effect.getAttributeModifierValue(effectinstance.getAmplifier(), attributemodifier), attributemodifier.getOperation());
						list1.add(new Pair<>(entry.getKey(), attributemodifier1));
					}
				}

				if (effectinstance.getAmplifier() > 0) {
					iformattabletextcomponent.append(" ").append(Component.translatable("potion.potency." + effectinstance.getAmplifier()));
				}

				if (effectinstance.getDuration() > 20) {
					iformattabletextcomponent.append(" (").append(MobEffectUtil.formatDuration(effectinstance, durationFactor)).append(")");
				}

				lores.add(iformattabletextcomponent.withStyle(effect.getCategory().getTooltipFormatting()));
			}
		}

		if (!list1.isEmpty()) {
			lores.add(CommonComponents.EMPTY);
			lores.add((Component.translatable("potion.whenDrank")).withStyle(ChatFormatting.DARK_PURPLE));

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
					lores.add((Component.translatable("attribute.modifier.plus." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(pair.getFirst().getDescriptionId()))).withStyle(ChatFormatting.BLUE));
				} else if (d0 < 0.0D) {
					d1 = d1 * -1.0D;
					lores.add((Component.translatable("attribute.modifier.take." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(pair.getFirst().getDescriptionId()))).withStyle(ChatFormatting.DARK_RED));
				}
			}
		}

	}

	/**
	 * @return True if ItemStack can be added to player's inventory (fully or
	 *         partially), false otherwise.
	 */

	public static boolean canPickStack(Player player, ItemStack stack) {
		if (player.getInventory().getFreeSlot() >= 0)
			return true;
		else {
			List<ItemStack> allInventories = new ArrayList<ItemStack>();

			// allInventories.addAll(player.getInventory().armorInventory);
			allInventories.addAll(player.getInventory().items);
			allInventories.addAll(player.getInventory().offhand);

			for (ItemStack invStack : allInventories) {
				if (SuperpositionHandler.canMergeStacks(invStack, stack, player.getInventory().getMaxStackSize()))
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
		return stack1.getItem() == stack2.getItem() && ItemStack.isSameItemSameTags(stack1, stack2);
	}

	public static boolean isPermanentlyDead(Player player) {
		// TODO Proper permadeath screen, Cursed Mode
		return EnigmaticItems.SOUL_CRYSTAL.getLostCrystals(player) >= 10
				&& OmniconfigHandler.maxSoulCrystalLoss.getValue() >= 10;
	}

	/**
	 * @return Multiplier for amount of particles based on client settings.
	 */
	@OnlyIn(Dist.CLIENT)
	public static float getParticleMultiplier() {
		if (Minecraft.getInstance().options.particles().get() == ParticleStatus.MINIMAL)
			return 0.35F;
		else if (Minecraft.getInstance().options.particles().get() == ParticleStatus.DECREASED)
			return 0.65F;
		else
			return 1.0F;

	}

	/**
	 * Checks whether or on the player is within the range of any active beacon.
	 * @author Integral
	 */

	public static boolean isInBeaconRange(Player player) {
		if (player.level().isClientSide)
			return false;

		List<BeaconBlockEntity> list = new ArrayList<BeaconBlockEntity>();
		boolean inRange = false;

		ServerLevel level = (ServerLevel) player.level();
		ServerChunkCache cache = (ServerChunkCache) player.level().getChunkSource();

		for (ChunkHolder holder : cache.chunkMap.visibleChunkMap.values()) {
			ChunkPos pos = holder.getPos();

			if (pos != null) {
				LevelChunk chunk = holder.getTickingChunk();
				//ChunkStatus.
				if (chunk != null) {
					for (BlockEntity tile : chunk.getBlockEntities().values()) {
						if (tile instanceof BeaconBlockEntity) {
							list.add((BeaconBlockEntity) tile);
						}
					}
				}
			}
		}

		if (list.size() > 0) {
			for (BeaconBlockEntity beacon : list)
				if (beacon.levels> 0) {
					try {
						if (beacon.getBeamSections().isEmpty()) {
							continue;
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}

					int range = (beacon.levels + 1) * 10;
					double distance = Math.sqrt(beacon.getBlockPos().distToCenterSqr(player.getX(), beacon.getBlockPos().getY(), player.getZ()));

					if (distance <= range) {
						inRange = true;
					}
				}
		}

		return inRange;
	}

	public static boolean hasItem(Player player, Item item) {
		for (List<ItemStack> list : player.getInventory().compartments) {
			for (ItemStack itemstack : list) {
				if (!itemstack.isEmpty() && itemstack.is(item))
					return true;
			}
		}

		return false;
	}

	public static boolean hasExactStack(Player player, ItemStack stack) {
		for (List<ItemStack> list : player.getInventory().compartments) {
			for (ItemStack inventoryStack : list) {
				if (inventoryStack == stack)
					return true;
			}
		}

		return false;
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

	public static boolean isAffectedBySoulLoss(Player player, boolean hadRing) {
		int dropMode = OmniconfigHandler.soulCrystalsMode.getValue(); // TODO Use Enum config
		boolean keepInventory = player.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);

		if (dropMode == 0)
			return hadRing;
		else if (dropMode == 1)
			return (hadRing || keepInventory);
		else if (dropMode == 2)
			return true;
		else
			return false;
	}

	public static boolean canDropSoulCrystal(Player player, boolean hadRing) {
		if (isAffectedBySoulLoss(player, hadRing)) {
			int maxCrystalLoss = OmniconfigHandler.maxSoulCrystalLoss.getValue();
			return EnigmaticItems.SOUL_CRYSTAL.getLostCrystals(player) < maxCrystalLoss;
		} else
			return false;
	}

	public static ServerLevel getWorld(ResourceKey<Level> key) {
		return ServerLifecycleHooks.getCurrentServer().getLevel(key);
	}

	public static ServerLevel getOverworld() {
		return SuperpositionHandler.getWorld(EnigmaticLegacy.PROXY.getOverworldKey());
	}

	public static ServerLevel getNether() {
		return SuperpositionHandler.getWorld(EnigmaticLegacy.PROXY.getNetherKey());
	}

	public static ServerLevel getEnd() {
		return SuperpositionHandler.getWorld(EnigmaticLegacy.PROXY.getEndKey());
	}

	public static void sendToDimension(ServerPlayer player, ResourceKey<Level> dimension, ITeleporter teleporter) {
		if (!player.level().dimension().equals(dimension)) {
			ServerLevel world = SuperpositionHandler.getWorld(dimension);
			if (world != null) {
				player.changeDimension(world, teleporter);
			}
		}
	}

	public static void sendToDimension(ServerPlayer player, ResourceKey<Level> dimension) {
		sendToDimension(player, dimension, new RealSmoothTeleporter());
	}

	public static ServerLevel backToSpawn(ServerPlayer serverPlayer) {
		ResourceKey<Level> respawnDimension = AdvancedSpawnLocationHelper.getPlayerRespawnDimension(serverPlayer);
		ServerLevel respawnWorld = SuperpositionHandler.getWorld(respawnDimension);

		serverPlayer.level().playSound(null, serverPlayer.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));

		EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), 128, serverPlayer.level().dimension())), new PacketPortalParticles(serverPlayer.getX(), serverPlayer.getY() + (serverPlayer.getBbHeight() / 2), serverPlayer.getZ(), 100, 1.25F, false));

		Optional<Vec3> vec = AdvancedSpawnLocationHelper.getValidSpawn(respawnWorld, serverPlayer);
		Optional<Vec3> vec2;
		ServerLevel destinationWorld = vec.isPresent() ? respawnWorld : serverPlayer.server.overworld();

		if (!serverPlayer.level().equals(destinationWorld)) {
			serverPlayer.changeDimension(destinationWorld, new RealSmoothTeleporter());
		}

		if (!respawnWorld.equals(destinationWorld)) {
			vec2 = AdvancedSpawnLocationHelper.getValidSpawn(destinationWorld, serverPlayer);
		} else {
			vec2 = Optional.empty();
		}

		if (vec.isPresent()) {
			Vec3 trueVec = vec.get();
			serverPlayer.teleportTo(trueVec.x, trueVec.y, trueVec.z);
		} else if (vec2.isPresent()) {
			Vec3 trueVec = vec2.get();
			serverPlayer.teleportTo(trueVec.x, trueVec.y, trueVec.z);
		} else {
			AdvancedSpawnLocationHelper.fuckBackToSpawn(serverPlayer.serverLevel(), serverPlayer);
		}

		serverPlayer.level().playSound(null, serverPlayer.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));

		EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), 128, serverPlayer.level().dimension())), new PacketRecallParticles(serverPlayer.getX(), serverPlayer.getY() + (serverPlayer.getBbHeight() / 2), serverPlayer.getZ(), 48, false));

		return destinationWorld;
	}

	public static DimensionalPosition getRespawnPoint(ServerPlayer serverPlayer) {
		ResourceKey<Level> respawnDimension = AdvancedSpawnLocationHelper.getPlayerRespawnDimension(serverPlayer);
		ServerLevel respawnWorld = SuperpositionHandler.getWorld(respawnDimension);

		Optional<Vec3> currentDimensionRespawnCoords = AdvancedSpawnLocationHelper.getValidSpawn(respawnWorld, serverPlayer);
		Optional<Vec3> destinationDimensionRespawnCoords;

		ServerLevel destinationWorld = currentDimensionRespawnCoords.isPresent() ? respawnWorld : serverPlayer.server.overworld();

		if (!respawnWorld.equals(destinationWorld)) {
			destinationDimensionRespawnCoords = AdvancedSpawnLocationHelper.getValidSpawn(destinationWorld, serverPlayer);
		} else {
			destinationDimensionRespawnCoords = Optional.empty();
		}

		Vec3 trueVec;

		if (currentDimensionRespawnCoords.isPresent()) {
			trueVec = currentDimensionRespawnCoords.get();
		} else if (destinationDimensionRespawnCoords.isPresent()) {
			trueVec = destinationDimensionRespawnCoords.get();
		} else {
			/*
			 * TODO Spawning player at the world's center involves a lot of collision checks, which we can't do
			 * without actually teleporting the player. Investigate on possible workarounds.
			 */
			trueVec = new Vec3(destinationWorld.getSharedSpawnPos().getX() + 0.5, destinationWorld.getSharedSpawnPos().getY() + 0.5, destinationWorld.getSharedSpawnPos().getZ() + 0.5);

			while (!destinationWorld.getBlockState(BlockPos.containing(trueVec)).isAir() && trueVec.y < 255.0D) {
				trueVec = trueVec.add(0, 1D, 0);
			}

		}

		return new DimensionalPosition(trueVec.x, trueVec.y, trueVec.z, destinationWorld);
	}

	public static void removeAttributeMap(Player player, Multimap<Attribute, AttributeModifier> attributes) {
		AttributeMap map = player.getAttributes();
		map.removeAttributeModifiers(attributes);
	}

	public static void applyAttributeMap(Player player, Multimap<Attribute, AttributeModifier> attributes) {
		AttributeMap map = player.getAttributes();

		map.addTransientAttributeModifiers(attributes);
	}

	public static boolean isTheCursedOne(Player player) {
		return SuperpositionHandler.hasCurio(player, EnigmaticItems.CURSED_RING);
	}

	public static boolean isTheBlessedOne(Player player) {
		if (EnigmaticLegacy.SOUL_OF_THE_ARCHITECT.equals(player.getUUID()))
			return true;
		else
			return DevotedBelieversHandler.isDevotedBeliever(player);
	}

	public static boolean hasArchitectsFavor(Player player) {
		return isTheBlessedOne(player) && SuperpositionHandler.hasCurio(player, EnigmaticItems.COSMIC_SCROLL);
	}

	public static boolean isTheWorthyOne(Player player) {
		if (isTheCursedOne(player)) {
			return getSufferingFraction(player) >= CursedRing.superCursedTime.getValue();
		} else
			return false;
	}

	public static double getSufferingFraction(@Nullable Player player) {
		if (player == null)
			return 0;

		var counter = IPlaytimeCounter.get(player);
		long timeWithRing = counter.getTimeWithCurses();
		long timeWithoutRing = counter.getTimeWithoutCurses();

		if (timeWithRing <= 0)
			return 0;
		else if (timeWithoutRing <= 0)
			return 1;

		if (timeWithRing > 100000 || timeWithoutRing > 100000) {
			timeWithRing = timeWithRing / 100;
			timeWithoutRing = timeWithoutRing / 100;

			if (timeWithRing <= 0)
				return 0;
			else if (timeWithoutRing <= 0)
				return 1;
		}

		double total = timeWithRing + timeWithoutRing;
		double ringFraction = (timeWithRing / total);
		ringFraction = roundToPlaces(ringFraction, 3);

		return ringFraction;
	}

	public static String getSufferingTime(@Nullable Player player) {
		String text = "";

		double ringPercent = 100 * getSufferingFraction(player);

		ringPercent = roundToPlaces(ringPercent, 1);
		if (ringPercent - Math.floor(ringPercent) == 0) {
			text += ((int) ringPercent) + "%";
		} else {
			text += ringPercent + "%";
		}

		return text;
	}

	public static String getNoSufferingTime(@Nullable Player player) {
		String text = "";

		double noRingPercent = 100 * (1.0 - getSufferingFraction(player));

		if (noRingPercent - Math.floor(noRingPercent) == 0) {
			text += ((int) noRingPercent) + "%";
		} else {
			text += noRingPercent + "%";
		}

		return text;
	}

	public static double roundToPlaces(double value, int places) {
		BigDecimal bd = new BigDecimal(Double.toString(value));
		bd = bd.setScale(places, RoundingMode.HALF_UP);

		return bd.doubleValue();
	}

	public static float getMissingHealthPool(Player player) {
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

		if (stack.getItem() == EnigmaticItems.CURSED_RING) {
			totalCurses+=7;
		}

		return totalCurses;
	}

	public static int getCurseAmount(Player player) {
		int count = 0;
		boolean ringCounted = false;

		for (ItemStack theStack : getFullEquipment(player)) {
			if (theStack != null) {
				if (theStack.getItem() != EnigmaticItems.CURSED_RING || !ringCounted) {
					count += getCurseAmount(theStack);

					if (theStack.getItem() == EnigmaticItems.CURSED_RING) {
						ringCounted = true;
					}
				}
			}
		}

		return count;
	}

	public static List<ItemStack> getFullEquipment(Player player) {
		List<ItemStack> equipmentStacks = Lists.newArrayList();

		equipmentStacks.add(player.getMainHandItem());
		equipmentStacks.add(player.getOffhandItem());
		equipmentStacks.addAll(player.getInventory().armor);

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
			if (!stack.isEmpty() && !GolemHeart.EXCLUDED_ARMOR.stream().anyMatch(stack::is)) {
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

	public static boolean areWeRemoteServer(Player player) {
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
			if (annotation.annotationType().getClassName().equals(annotationClass.getName())) {
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
				Class<?> retainerClass = Class.forName(annotationData.clazz().getClassName());
				String methodName = annotationData.memberName().split("\\(")[0];

				boolean receiveClient;

				if (annotationData.annotationData().get("receiveClient") != null) {
					receiveClient = (boolean) annotationData.annotationData().get("receiveClient");
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
				Class<?> retainerClass = Class.forName(annotationData.clazz().getClassName());
				String itemName = (String) annotationData.annotationData().get("value");
				String fieldName = annotationData.memberName();

				Field field = retainerClass.getDeclaredField(fieldName);
				if (itemName != null && !itemName.isEmpty()) {
					accessibilityGeneratorMap.put(itemName, field);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return accessibilityGeneratorMap;
	}

	public static boolean hasAntiInsectAcknowledgement(Player player) {
		List<ItemStack> heldItems = Lists.newArrayList(player.getMainHandItem(), player.getOffhandItem());

		for (ItemStack held : heldItems) {
			if (held != null && held.getItem() instanceof TheAcknowledgment) {
				if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BANE_OF_ARTHROPODS, held) > 0)
					return true;
			}
		}

		return false;
	}

	public static boolean isStaringAt(Player player, LivingEntity living) {
		Vec3 vector3d = player.getViewVector(1.0F).normalize();
		Vec3 vector3d1 = new Vec3(living.getX() - player.getX(), living.getEyeY() - player.getEyeY(), living.getZ() - player.getZ());
		double d0 = vector3d1.length();
		vector3d1 = vector3d1.normalize();
		double d1 = vector3d.dot(vector3d1);
		return d1 > 1.0D - 0.025D / d0 ? player.hasLineOfSight(living) : false;
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

	public static Optional<Tuple<UUID, BlockPos>> updateSoulCompass(ServerPlayer player) {
		var optional = SoulArchive.getInstance().findNearest(player.level(), player.blockPosition());
		boolean noValid = optional.isEmpty();
		BlockPos pos = noValid ? BlockPos.ZERO : optional.get().getB();

		EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with((() -> player)),
				new PacketUpdateCompass(pos.getX(), pos.getY(), pos.getZ(), noValid));
		EnigmaticEventHandler.LAST_SOUL_COMPASS_UPDATE.put(player, player.tickCount);
		return optional;
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

	@OnlyIn(Dist.CLIENT)
	public static int greatestWidth(Font font, String[] lines) {
		return Arrays.stream(lines).mapToInt(font::width).reduce((num1, num2) -> num1 > num2 ? num1 : num2)
				.getAsInt();
	}

	public static boolean hasEnigmaticElytra(LivingEntity living) {
		return getEnigmaticElytra(living) != null;
	}

	@Nullable
	public static ItemStack getEnigmaticElytra(LivingEntity living) {
		ItemStack stack = living.getItemBySlot(EquipmentSlot.CHEST);

		if (stack.is(EnigmaticItems.ENIGMATIC_ELYTRA))
			return stack;
		else
			return getCurioStack(living, EnigmaticItems.ENIGMATIC_ELYTRA);
	}

	public static ItemStack maybeApplyEternalBinding(ItemStack stack) {
		if (Math.random() < 0.5)
			if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BINDING_CURSE, stack) > 0) {
				Map<Enchantment, Integer> map =  EnchantmentHelper.getEnchantments(stack);

				map.remove(Enchantments.VANISHING_CURSE);
				int level = map.remove(Enchantments.BINDING_CURSE);
				map.put(EnigmaticEnchantments.ETERNAL_BINDING, level);
				EnchantmentHelper.setEnchantments(map, stack);
			}

		return stack;
	}

	public static boolean canUnequipBoundRelics(Player player) {
		return player.isCreative() || EnigmaticLegacy.SOUL_OF_THE_ARCHITECT.equals(player.getUUID());
	}

	public static boolean onDamageSourceBlocking(LivingEntity blocker, ItemStack useItem, DamageSource source,
												 CallbackInfoReturnable<Boolean> info) {
		if (blocker instanceof Player player && useItem != null) {
			boolean blocking = ((IProperShieldUser)blocker).isActuallyReallyBlocking();

			if (!blocking)
				return false;

			if (useItem.getItem() instanceof InfernalShield) {
				boolean piercingArrow = false;
				Entity entity = source.getDirectEntity();

				if (entity instanceof AbstractArrow arrow) {
					if (arrow.getPierceLevel() > 0) {
						piercingArrow = true;
					}
				}

				piercingArrow = false; // defend against Piercing... for now

				if (!source.is(DamageTypeTags.BYPASSES_SHIELD) && !piercingArrow) {
					Vec3 sourcePos = source.getSourcePosition();

					if (sourcePos != null) {
						Vec3 lookVec = blocker.getViewVector(1.0F);
						Vec3 sourceToSelf = sourcePos.vectorTo(blocker.position()).normalize();
						sourceToSelf = new Vec3(sourceToSelf.x, 0.0D, sourceToSelf.z);

						if (sourceToSelf.dot(lookVec) < 0.0D) {
							int strength = -1;

							if (player.hasEffect(EnigmaticEffects.BLAZING_STRENGTH)) {
								MobEffectInstance effectInstance = player.getEffect(EnigmaticEffects.BLAZING_STRENGTH);
								strength = effectInstance.getAmplifier();
								player.removeEffect(EnigmaticEffects.BLAZING_STRENGTH);
								strength = strength > 2 ? 2 : strength;
							}

							player.addEffect(new MobEffectInstance(EnigmaticEffects.BLAZING_STRENGTH, 1200, strength + 1, true, true));

							if (source.getDirectEntity() instanceof LivingEntity living && living.isAlive()) {
								if (!living.fireImmune() && !(living instanceof Guardian)) {
									StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();

									// Ensure that we will not be caught in any sort of StackOverflow because of thorns-like effects
									if (Arrays.stream(stacktrace).filter(element -> {
										return SuperpositionHandler.class.getName().equals(element.getClassName());
									}).count() < 2) {
										living.invulnerableTime = 0;
										living.hurt(living.damageSources().source(DamageTypes.ON_FIRE, player), 4F);
										living.setSecondsOnFire(4);
										EnigmaticEventHandler.KNOCKBACK_THAT_BASTARD.remove(living);
									}
								}
							}

							return true;
						}
					}
				}
			} else if (useItem.getItem() instanceof EldritchPan) {
				Entity projectile = source.getDirectEntity();

				if (!(projectile instanceof Projectile))
					return false;

				if (!source.is(DamageTypeTags.BYPASSES_SHIELD)) {
					Vec3 sourcePos = source.getSourcePosition();

					if (sourcePos != null) {
						Vec3 lookVec = blocker.getViewVector(1.0F);
						Vec3 sourceToSelf = sourcePos.vectorTo(blocker.position()).normalize();
						sourceToSelf = new Vec3(sourceToSelf.x, 0.0D, sourceToSelf.z);

						if (sourceToSelf.dot(lookVec) < 0.0D) {
							projectile.kill();

							FoodData data = player.getFoodData();
							data.eat(4, 0.5F);

							player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
									SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F,
									player.level().random.nextFloat() * 0.1F + 0.9F);

							player.gameEvent(GameEvent.EAT);

							if (projectile instanceof LargeFireball fireball) {
								fireball.explosionPower = 0;
							}

							if (player.level() instanceof ServerLevel level) {
								Vec3 angle = player.getLookAngle();
								angle.multiply(1, 0, 1).normalize().multiply(0.5, 0.5, 0.5);

								level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM,
										new ItemStack(Items.FIRE_CHARGE)), player.getX() + angle.x,
										player.getY() + player.getEyeHeight() - 0.1, player.getZ() + angle.z,
										10, 0.3D, 0.3D, 0.3D, 0.03D);
							}

							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public static boolean cannotHunger(@Nullable Player player) {
		boolean noHunger = false;

		if (player != null) {
			if (EnigmaticItems.FORBIDDEN_FRUIT.haveConsumedFruit(player)) {
				noHunger = true;
			} else if (player.level().getDifficulty() == Difficulty.PEACEFUL) {
				noHunger = true;
			}
		}

		return noHunger;
	}

	public static <K, V extends Comparable<? super V>> void sortByKey(Map<K, V> map) {
		List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
		list.sort((Comparator)Entry.comparingByKey());

		map.clear();

		for (Entry<K, V> entry : list) {
			map.put(entry.getKey(), entry.getValue());
		}
	}

	private static boolean contains(char[] array, char ch) {
		for (char ach : array) {
			if (ach == ch)
				return true;
		}

		return false;
	}

	public static String obscureString(String string) {
		char[] oldArray = string.toCharArray();
		char[] newArray = new char[oldArray.length];
		boolean code = false;

		for (int i = 0; i < oldArray.length; i++) {
			char ch = oldArray[i];
			newArray[i] = ch;

			if (ch == '\u00a7') {
				code = true;
				continue;
			} else if (code) {
				code = false;
				continue;
			} else if (ch == ' ') {
				continue;
			}

			char[] replacements = null;

			if (contains(UPPERCASE_LETTERS, ch)) {
				replacements = UPPERCASE_LETTERS;
			} else if (contains(LOWERCASE_LETTERS, ch)) {
				replacements = LOWERCASE_LETTERS;
			} else if (contains(NUMBERS, ch)) {
				replacements = NUMBERS;
			} else {
				replacements = SPECIAL_SYMBOLS;
			}

			ch = replacements[RANDOM.nextInt(replacements.length)];

			newArray[i] = ch;
		}

		return new String(newArray);
	}

	@OnlyIn(Dist.CLIENT)
	public static void obscureTooltip(List<Component> tooltip) {
		tooltip.replaceAll(component -> Component.literal(obscureString(component.getString()))
				.withStyle(component.getStyle()));
	}

	@OnlyIn(Dist.CLIENT)
	public static String[] wrapString(String string, Font font, int width) {
		var list = font.getSplitter().splitLines(string, width, Style.EMPTY);
		String[] lines = new String[list.size()];

		for (int i = 0; i < lines.length; i++) {
			FormattedText text = list.get(i);
			lines[i] = text.getString();
		}

		return lines;
	}

	@SafeVarargs
	public static <T> T getRandomElement(List<T> list, T... excluding) {
		List<T> filtered = new ArrayList<>(list);
		Arrays.stream(excluding).forEach(filtered::remove);

		if (filtered.size() <= 0)
			throw new IllegalArgumentException("List has no valid elements to choose");
		else if (filtered.size() == 1)
			return filtered.get(0);
		else
			return filtered.get(RANDOM.nextInt(filtered.size()));
	}

	public static String getMD5Hash(String string) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(string.getBytes());
			return bytesToHex(md.digest()).toUpperCase();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static String bytesToHex(byte[] bytes) {
		char[] hexArray = "0123456789ABCDEF".toCharArray();

		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}

		return new String(hexChars);
	}

	@OnlyIn(Dist.CLIENT)
	public static void renderInsigniaNameplate(Entity entity, Component name, PoseStack stack, MultiBufferSource buffer,
			int packedLight, EntityRenderDispatcher entityRenderDispatcher, Font font) {
		double d0 = entityRenderDispatcher.distanceToSqr(entity);

		if (ForgeHooksClient.isNameplateInRenderDistance(entity, d0)) {
			boolean render = font.width(name) > 0;
			boolean override = false;

			if (!render && entity != Minecraft.getInstance().player
					&& EnigmaticItems.INSIGNIA.canSeeTrueName(Minecraft.getInstance().player)) {
				name = entity.getDisplayName();
				render = override = true;
			}

			boolean flag = !entity.isDiscrete();
			float f = entity.getBbHeight() + 0.5F;
			int i = 0;
			stack.pushPose();
			stack.translate(0.0D, f, 0.0D);
			stack.mulPose(entityRenderDispatcher.cameraOrientation());
			stack.scale(-0.025F, -0.025F, 0.025F);
			Matrix4f matrix4f = stack.last().pose();
			float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
			int j = (int)(f1 * 255.0F) << 24;
			float f2 = -font.width(name) / 2;

			if (render) {
				font.drawInBatch(name, f2, i, 553648127, false, matrix4f, buffer, flag ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, j, packedLight);

				if (flag) {
					font.drawInBatch(name, f2, i, -1, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLight);
				}
			}

			if (!override && entity != Minecraft.getInstance().player
					&& EnigmaticItems.INSIGNIA.canSeeTrueName(Minecraft.getInstance().player)) {
				stack.pushPose();
				name = Component.literal("(" + entity.getDisplayName().getString() + ")");
				f2 = -font.width(name) / 2;

				float scale = 0.4F;
				int offset = (int) (-10 * (1.0F / scale));
				stack.scale(scale, scale, scale);
				matrix4f = stack.last().pose();

				font.drawInBatch(name, f2, i - offset, 553648127, false, matrix4f, buffer, flag ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, j, packedLight);

				if (flag) {
					font.drawInBatch(name, f2, i - offset, -1, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLight);
				}
				stack.popPose();
			}

			stack.popPose();
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static File getSaveFolder(LevelSummary summary) {
		LevelStorageSource levels = Minecraft.getInstance().getLevelSource();
		return new File(levels.getBaseDir().toFile(), summary.getLevelId());
	}

	@OnlyIn(Dist.CLIENT)
	public static Component getAltInfo(LevelSummary summary) {
		Component info = summary.getInfo();
		File world = getSaveFolder(summary);
		boolean fractured = isWorldFractured(world);

		if (summary.getGameMode() == GameType.SURVIVAL || fractured) {
			String key = "gameMode.enigmaticlegacy.";

			if (fractured) {
				key += "fractured";
			} else if (isWorldCursed(world)) {
				key += "cursed";

				if (summary.isHardcore()) {
					key += "Hardcore";
				}
			} else
				return info;

			MutableComponent tcn = Component.translatable(key);
			tcn.withStyle(info.getStyle());

			if (info instanceof MutableComponent mutable)
				if (mutable.getContents() instanceof TranslatableContents) {
					info.getSiblings().forEach(tcn::append);
				} else if (mutable.getContents() instanceof LiteralContents) {
					for (int i = 1; i < info.getSiblings().size(); i++) {
						tcn.append(info.getSiblings().get(i));
					}
				}

			return tcn;
		}

		return info;
	}

	public static Item findItem(String namespace, String name) {
		return ForgeRegistries.ITEMS.getValue(new ResourceLocation(namespace, name));
	}

	/**
	 * @return Current server if in singleplayer, otherwise empty optional.
	 */

	public static Optional<MinecraftServer> getSingleplayerServer() {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

		if (server != null && server.isSingleplayer() &&
				Objects.equal(server.getSingleplayerProfile().getName(),
						EnigmaticLegacy.PROXY.getClientUsername()))
			return Optional.of(server);
		else
			return Optional.empty();
	}

	public static void setCurrentWorldCursed(boolean cursed) {
		getSingleplayerServer().ifPresent(server -> {
			File saveFolder = server.getWorldPath(LevelResource.ROOT).toFile();
			EnigmaticTransience transience = EnigmaticTransience.read(saveFolder);
			transience.setCursed(cursed);
			transience.write(saveFolder);
		});
	}

	public static void setCurrentWorldFractured(boolean fractured) {
		getSingleplayerServer().ifPresent(server -> {
			File saveFolder = server.getWorldPath(LevelResource.ROOT).toFile();
			EnigmaticTransience transience = EnigmaticTransience.read(saveFolder);
			transience.setPermanentlyDead(fractured);
			transience.write(saveFolder);
		});
	}

	public static boolean isWorldCursed(File world) {
		return EnigmaticTransience.read(world).isCursed();
	}

	public static boolean isWorldFractured(File world) {
		return EnigmaticTransience.read(world).isPermanentlyDead();
	}

	public static boolean isMagic(DamageSource source) {
		return source.is(DamageTypes.MAGIC) || source.is(DamageTypes.WITHER) || source.is(DamageTypes.WITHER_SKULL)
				|| source.is(DamageTypes.DRAGON_BREATH) || source.is(DamageTypes.INDIRECT_MAGIC)
				|| source.is(DamageTypes.THORNS);
	}

	public static boolean isExplosion(DamageSource source) {
		return source.is(DamageTypeTags.IS_EXPLOSION);
	}

	public static boolean isBypassingArmor(DamageSource source) {
		return source.is(DamageTypeTags.BYPASSES_ARMOR);
	}

	public static boolean isBypassingMagic(DamageSource source) {
		return source.is(DamageTypeTags.BYPASSES_ENCHANTMENTS);
	}

	// TODO This logic may be improvable
	public static boolean isAbsolute(DamageSource source) {
		return source.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
	}

}
