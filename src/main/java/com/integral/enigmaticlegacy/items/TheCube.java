package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.api.items.ISpellstone;
import com.integral.enigmaticlegacy.handlers.EnigmaticEventHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.EnigmaticAmulet.AmuletColor;
import com.integral.enigmaticlegacy.items.generic.ItemSpellstoneCurio;
import com.integral.enigmaticlegacy.objects.TransientPlayerData;
import com.integral.enigmaticlegacy.objects.Vector3;
import com.integral.enigmaticlegacy.packets.clients.PacketPlayerSetlook;
import com.integral.enigmaticlegacy.packets.clients.PacketPortalParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketRecallParticles;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.ItemPredicate;
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
import net.minecraft.world.level.block.state.BlockState;
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

	public TheCube() {
		super(getDefaultProperties().rarity(Rarity.EPIC));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "the_cube"));

		this.worlds = ImmutableList.of(Level.OVERWORLD, Level.NETHER, Level.END);
		this.randomBuffs = ImmutableList.of(MobEffects.ABSORPTION, MobEffects.DAMAGE_BOOST, MobEffects.REGENERATION,
				MobEffects.DIG_SPEED, MobEffects.JUMP, MobEffects.MOVEMENT_SPEED, MobEffects.DAMAGE_RESISTANCE,
				MobEffects.SLOW_FALLING);
		this.randomDebuffs = ImmutableList.of(MobEffects.BLINDNESS, MobEffects.CONFUSION, MobEffects.DIG_SLOWDOWN,
				MobEffects.HUNGER, MobEffects.LEVITATION, MobEffects.MOVEMENT_SLOWDOWN, MobEffects.WEAKNESS,
				MobEffects.POISON, MobEffects.WITHER);

		this.immunityList.add(DamageSource.ON_FIRE.msgId);
		this.immunityList.add(DamageSource.LAVA.msgId);
		this.immunityList.add(DamageSource.CRAMMING.msgId);
		this.immunityList.add(DamageSource.DROWN.msgId);
		this.immunityList.add(DamageSource.FALL.msgId);
		this.immunityList.add(DamageSource.FLY_INTO_WALL.msgId);
		this.immunityList.add(DamageSource.CACTUS.msgId);
		this.immunityList.add(DamageSource.IN_WALL.msgId);
		this.immunityList.add(DamageSource.FALLING_BLOCK.msgId);
		this.immunityList.add(DamageSource.SWEET_BERRY_BUSH.msgId);
	}

	@Override
	public int getCooldown(Player player) {
		if (player != null && SuperpositionHandler.hasArchitectsFavor(player))
			return 600;
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
		//tooltips.clear();
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

			//if (context.entity() instanceof ServerPlayer) {
			AttributeMap map = player.getAttributes();
			map.addTransientAttributeModifiers(this.getCurrentModifiers(player));
			//}
		}
	}

	@Override
	public void triggerActiveAbility(Level world, ServerPlayer player, ItemStack stack) {
		if (SuperpositionHandler.hasSpellstoneCooldown(player))
			return;

		ResourceKey<Level> key = this.worlds.get(random.nextInt(this.worlds.size()));
		ServerLevel level = SuperpositionHandler.getWorld(key);

		if (level == null) {
			key = Level.OVERWORLD;
			level = SuperpositionHandler.getOverworld();
		}

		System.out.println(level.getWorldBorder().getAbsoluteMaxSize());

		int attempts = 0;
		int radius = 10000;

		cycle: while (true) {
			BlockPos pos = new BlockPos(radius - random.nextInt(radius * 2), key == Level.NETHER ? 100 : 200, radius - random.nextInt(radius * 2));
			level.getChunkAt(pos);

			for (int i = 0; i < 4; i++) {
				if (i > 0) {
					pos = new BlockPos((pos.getX() << 4) + random.nextInt(16), pos.getY(), (pos.getZ() << 4) + random.nextInt(16));
				}

				if (this.tryTeleport(player, player.level, pos.getX(), pos.getY(), pos.getZ())) {
					break cycle;
				}
			}

			if (++attempts > 100) {
				this.triggerActiveAbility(world, player, stack);
				return;
			}
		}

		SuperpositionHandler.setSpellstoneCooldown(player, this.getCooldown(player));
	}

	private boolean tryTeleport(ServerPlayer player, Level world, int x, int y, int z) {
		int checkAxis = y - 10;

		for (int counter = 0; counter <= checkAxis; counter++) {
			if (!world.isEmptyBlock(new BlockPos(x, y - counter - 1, z)) && world.getBlockState(new BlockPos(x, y - counter - 1, z)).canOcclude() && world.isEmptyBlock(new BlockPos(x, y - counter, z)) & world.isEmptyBlock(new BlockPos(x, y - counter + 1, z))) {
				world.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2D)));
				EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getX(), player.getY(), player.getZ(), 128, player.level.dimension())), new PacketRecallParticles(player.getX(), player.getY() + (player.getBbHeight() / 2), player.getZ(), 48, false));

				player.teleportTo(x + 0.5, y - counter, z + 0.5);

				world.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2D)));
				EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getX(), player.getY(), player.getZ(), 128, player.level.dimension())), new PacketRecallParticles(player.getX(), player.getY() + (player.getBbHeight() / 2), player.getZ(), 48, false));
				return true;
			}
		}

		return false;
	}

}
