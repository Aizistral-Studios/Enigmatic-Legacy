package com.aizistral.enigmaticlegacy.items;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.items.ISpellstone;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemSpellstoneCurio;
import com.aizistral.enigmaticlegacy.objects.Vector3;
import com.aizistral.enigmaticlegacy.packets.clients.PacketRecallParticles;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.api.SlotContext;

public class TheCube extends ItemSpellstoneCurio implements ISpellstone {
	private final List<MobEffect> randomBuffs;
	private final List<MobEffect> randomDebuffs;
	private final List<ResourceKey<Level>> worlds;
	private final Map<ServerPlayer, Future<CachedTeleportationLocation>> locationCache = new WeakHashMap<>();
	private final ExecutorService executor = Executors.newCachedThreadPool();

	public TheCube() {
		super(getDefaultProperties().rarity(Rarity.EPIC).fireResistant());

		this.worlds = ImmutableList.of(Level.OVERWORLD, Level.NETHER, Level.END);
		this.randomBuffs = ImmutableList.of(MobEffects.ABSORPTION, MobEffects.DAMAGE_BOOST, MobEffects.REGENERATION,
				MobEffects.DIG_SPEED, MobEffects.JUMP, MobEffects.MOVEMENT_SPEED, MobEffects.DAMAGE_RESISTANCE,
				MobEffects.SLOW_FALLING);
		this.randomDebuffs = ImmutableList.of(MobEffects.BLINDNESS, MobEffects.CONFUSION, MobEffects.DIG_SLOWDOWN,
				MobEffects.HUNGER, MobEffects.LEVITATION, MobEffects.MOVEMENT_SLOWDOWN, MobEffects.WEAKNESS,
				MobEffects.POISON, MobEffects.WITHER);

		this.immunityList.add(DamageTypes.ON_FIRE);
		this.immunityList.add(DamageTypes.IN_FIRE);
		this.immunityList.add(DamageTypes.LAVA);
		this.immunityList.add(DamageTypes.HOT_FLOOR);
		this.immunityList.add(DamageTypes.CRAMMING);
		this.immunityList.add(DamageTypes.DROWN);
		this.immunityList.add(DamageTypes.FALL);
		this.immunityList.add(DamageTypes.FLY_INTO_WALL);
		this.immunityList.add(DamageTypes.CACTUS);
		this.immunityList.add(DamageTypes.IN_WALL);
		this.immunityList.add(DamageTypes.FALLING_BLOCK);
		this.immunityList.add(DamageTypes.SWEET_BERRY_BUSH);
	}

	@Override
	public int getCooldown(Player player) {
		if (player != null && reducedCooldowns.test(player))
			return 1600;
		else
			return 3200;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			boolean cursed = Minecraft.getInstance().player != null && SuperpositionHandler.isTheCursedOne(Minecraft.getInstance().player);

			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube3", ChatFormatting.GOLD, 120);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube6");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube7");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube8");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube9");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube10");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube11", ChatFormatting.GOLD, (int)this.getDamageLimit(cursed));
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube12");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube13");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube14");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube15");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube16");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube17");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube18");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube19");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube20");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube21");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube22");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		try {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.currentKeybind", ChatFormatting.LIGHT_PURPLE, KeyMapping.createNameSupplier("key.spellstoneAbility").get().getString().toUpperCase());
		} catch (NullPointerException ex) {
			// Just don't do it lol
		}
	}

	public void applyRandomEffect(LivingEntity entity, boolean positive) {
		List<MobEffect> effects = positive ? this.randomBuffs : this.randomDebuffs;
		MobEffect effect = effects.get(random.nextInt(effects.size()));

		if (positive) {
			int time = 100 + random.nextInt(500);
			int amplifier = random.nextDouble() <= 0.25 ? 1 : 0;
			entity.addEffect(new MobEffectInstance(effect, time, amplifier, false, true));
		} else {
			int time = 200 + random.nextInt(1000);
			int amplifier = random.nextDouble() <= 0.15 ? 2 : random.nextDouble() <= 0.4 ? 1 : 0;
			entity.addEffect(new MobEffectInstance(effect, time, amplifier, false, true));
		}
	}

	public float getDamageLimit(Player player) {
		return this.getDamageLimit(SuperpositionHandler.isTheCursedOne(player));
	}

	private float getDamageLimit(boolean cursed) {
		return cursed ? 150 : 100;
	}

	public Multimap<Attribute, AttributeModifier> getCurrentModifiers(Player player) {
		Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();
		attributes.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("a601a528-fbf3-49bb-84af-f65023c1a188"), EnigmaticLegacy.MODID + ":sprint_bonus", player.isSprinting() ? 0.35F : 0F, AttributeModifier.Operation.MULTIPLY_TOTAL));
		return attributes;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();

		attributes.put(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("d171890c-ba68-42e3-ba2e-ac275e8de595"), EnigmaticLegacy.MODID + ":attack_speed_modifier", 0.4F, AttributeModifier.Operation.MULTIPLY_TOTAL));
		attributes.put(ForgeMod.SWIM_SPEED.get(), new AttributeModifier(UUID.fromString("7652a7d5-1e7c-4c8e-8bd2-b0dd38411581"), EnigmaticLegacy.MODID + ":swim_bonus", 1.0F, AttributeModifier.Operation.MULTIPLY_TOTAL));
		attributes.put(Attributes.LUCK, new AttributeModifier(UUID.fromString("290d5f76-87aa-4f7c-9c1a-9aef2fe25d05"),EnigmaticLegacy.MODID+":luck_bonus", 1, AttributeModifier.Operation.ADDITION));

		return attributes;
	}

	@Override
	public int getFortuneLevel(SlotContext slotContext, LootContext lootContext, ItemStack stack) {
		return super.getFortuneLevel(slotContext, lootContext, stack) + 1;
	}

	@Override
	public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
		tooltips.clear();
		return tooltips;
	}

	@Override
	public void onUnequip(SlotContext context, ItemStack newStack, ItemStack stack) {
		if (context.entity() instanceof Player player) {
			AttributeMap map = player.getAttributes();
			map.removeAttributeModifiers(this.getCurrentModifiers(player));
		}
	}

	@Override
	public void curioTick(SlotContext context, ItemStack stack) {
		if (context.entity() instanceof Player player) {
			if (player.getAirSupply() < 300) {
				player.setAirSupply(300);
			}

			if (player.isOnFire()) {
				player.clearFire();
			}

			AttributeMap map = player.getAttributes();
			map.addTransientAttributeModifiers(this.getCurrentModifiers(player));

			if (context.entity() instanceof ServerPlayer) {
				if (!this.locationCache.containsKey(player)) {
					this.generateCachedLocation((ServerPlayer) player);
				} else {
					Future<CachedTeleportationLocation> future = this.locationCache.get(player);

					if (future.isDone() && !future.isCancelled()) {
						try {
							CachedTeleportationLocation location = future.get();

							if (location.dimension() == player.level().dimension()) {
								this.generateCachedLocation((ServerPlayer) player);
							}
						} catch (Exception ex) {
							throw new RuntimeException(ex);
						}
					}
				}
			}
		}
	}

	@Override
	public void triggerActiveAbility(Level world, ServerPlayer player, ItemStack stack) {
		if (SuperpositionHandler.hasSpellstoneCooldown(player))
			return;

		CachedTeleportationLocation location = null;

		if (this.locationCache.containsKey(player)) {
			try {
				var future = this.locationCache.get(player);

				if (future.isDone()) {
					location = this.locationCache.get(player).get();
				} else {
					future.cancel(true);
				}

				this.locationCache.remove(player);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		if (location == null) {
			EnigmaticLegacy.LOGGER.getInternal().info("No cached location found for {}, generating new one synchronously.", player.getGameProfile().getName());
			location = this.findRandomLocation(player);
		}

		ResourceKey<Level> key = location.dimension();

		world.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2D)));
		EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getX(), player.getY(), player.getZ(), 128, player.level().dimension())), new PacketRecallParticles(player.getX(), player.getY() + (player.getBbHeight() / 2), player.getZ(), 48, false));
		player.teleportTo(location.x(), location.y(), location.z());

		if (player.level().dimension() != key) {
			SuperpositionHandler.sendToDimension(player, key);
			player.teleportTo(location.x(), location.y(), location.z());
		}

		world.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2D)));
		EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getX(), player.getY(), player.getZ(), 128, player.level().dimension())), new PacketRecallParticles(player.getX(), player.getY() + (player.getBbHeight() / 2), player.getZ(), 48, false));

		SuperpositionHandler.setSpellstoneCooldown(player, this.getCooldown(player));

		EnigmaticLegacy.LOGGER.getInternal().info("Player {} triggered active ability of Non-Euclidean Cube. Teleported to D: {}, X: {}, Y: {}, Z: {}.",
				player.getGameProfile().getName(), player.level().dimension(), player.getX(), player.getY(), player.getZ());
	}

	private void generateCachedLocation(ServerPlayer player) {
		Future<CachedTeleportationLocation> future = this.executor.submit(() -> {
			try {
				CachedTeleportationLocation location = this.findRandomLocation(player);
				EnigmaticLegacy.LOGGER.debug("Found random location: " + location);
				return location;
			} catch (Exception ex) {
				EnigmaticLegacy.LOGGER.error("Could not find random location for:" + player.getGameProfile().getName());
				ex.printStackTrace();
				throw ex;
			}
		});

		this.locationCache.put(player, future);
	}

	private CachedTeleportationLocation findRandomLocation(ServerPlayer player) {
		ResourceKey<Level> key = SuperpositionHandler.getRandomElement(this.worlds, player.level().dimension());
		ServerLevel level = SuperpositionHandler.getWorld(key);

		if (level == null) {
			EnigmaticLegacy.LOGGER.error("Could not find world: " + key);
			EnigmaticLegacy.LOGGER.error("This is never supposed to happen!");
			key = Level.OVERWORLD;
			level = SuperpositionHandler.getOverworld();
		}

		int border = (int) level.getWorldBorder().getSize() / 2;
		int attempts = 0;
		int radius = border < 10000 ? border : 10000;

		while (true) {
			BlockPos pos = new BlockPos(radius - random.nextInt(radius * 2), key == Level.NETHER ? 100 : 200, radius - random.nextInt(radius * 2));
			level.getChunkAt(pos);

			for (int i = 0; i < 4; i++) {
				if (i > 0) {
					pos = new BlockPos((pos.getX() >> 4) * 16 + random.nextInt(16), pos.getY(), (pos.getZ() >> 4) * 16 + random.nextInt(16));
				}

				var location = this.findValidPosition(player, level, pos.getX(), pos.getY(), pos.getZ());

				if (!location.isEmpty())
					return new CachedTeleportationLocation(key, location.get().x, location.get().y, location.get().z);
			}

			if (++attempts > 100)
				return this.findRandomLocation(player);
		}
	}

	private Optional<Vector3> findValidPosition(ServerPlayer player, Level world, int x, int y, int z) {
		int checkAxis = y - 10;

		for (int counter = 0; counter <= checkAxis; counter++) {
			BlockPos below = new BlockPos(x, y - counter - 1, z);
			BlockPos feet = new BlockPos(x, y - counter, z);
			BlockPos head = new BlockPos(x, y - counter + 1, z);

			if (world.getMinBuildHeight() >= below.getY())
				return Optional.empty();

			if (!world.isEmptyBlock(below) && world.getBlockState(below).canOcclude() && world.isEmptyBlock(feet) && world.isEmptyBlock(head))
				return Optional.of(new Vector3(feet.getX() + 0.5, feet.getY(), feet.getZ() + 0.5));
		}

		return Optional.empty();
	}

	public void clearLocationCache() {
		this.locationCache.clear();
	}

	private static record CachedTeleportationLocation(ResourceKey<Level> dimension, double x, double y, double z) {
		// NO-OP
	}

}
