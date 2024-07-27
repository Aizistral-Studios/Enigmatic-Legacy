package com.aizistral.enigmaticlegacy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Triple;
import org.lwjgl.glfw.GLFW;

import com.aizistral.enigmaticlegacy.api.capabilities.PlayerPlaytimeCounter;
import com.aizistral.enigmaticlegacy.api.items.IAdvancedPotionItem.PotionType;
import com.aizistral.enigmaticlegacy.api.items.ICreativeTabMember;
import com.aizistral.enigmaticlegacy.api.materials.EnigmaticArmorMaterials;
import com.aizistral.enigmaticlegacy.api.materials.EnigmaticMaterials;
import com.aizistral.enigmaticlegacy.blocks.BlockAstralDust;
import com.aizistral.enigmaticlegacy.blocks.BlockBigLamp;
import com.aizistral.enigmaticlegacy.blocks.BlockCosmicCake;
import com.aizistral.enigmaticlegacy.blocks.BlockEndAnchor;
import com.aizistral.enigmaticlegacy.blocks.BlockMassiveLamp;
import com.aizistral.enigmaticlegacy.blocks.TileEndAnchor;
import com.aizistral.enigmaticlegacy.brewing.SpecialBrewingRecipe;
import com.aizistral.enigmaticlegacy.brewing.ValidationBrewingRecipe;
import com.aizistral.enigmaticlegacy.client.Quote;
import com.aizistral.enigmaticlegacy.config.EtheriumConfigHandler;
import com.aizistral.enigmaticlegacy.config.OmniconfigHandler;
import com.aizistral.enigmaticlegacy.crafting.HiddenRecipe;
import com.aizistral.enigmaticlegacy.effects.BlazingStrengthEffect;
import com.aizistral.enigmaticlegacy.effects.MoltenHeartEffect;
import com.aizistral.enigmaticlegacy.entities.EnigmaticPotionEntity;
import com.aizistral.enigmaticlegacy.entities.PermanentItemEntity;
import com.aizistral.enigmaticlegacy.entities.UltimateWitherSkullEntity;
import com.aizistral.enigmaticlegacy.gui.containers.LoreInscriberContainer;
import com.aizistral.enigmaticlegacy.gui.containers.LoreInscriberScreen;
import com.aizistral.enigmaticlegacy.handlers.DevotedBelieversHandler;
import com.aizistral.enigmaticlegacy.handlers.EnigmaticEventHandler;
import com.aizistral.enigmaticlegacy.handlers.EnigmaticKeybindHandler;
import com.aizistral.enigmaticlegacy.handlers.EnigmaticUpdateHandler;
import com.aizistral.enigmaticlegacy.handlers.SoulArchive;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.PotionHelper;
import com.aizistral.enigmaticlegacy.items.CosmicCake;
import com.aizistral.enigmaticlegacy.items.CosmicScroll;
import com.aizistral.enigmaticlegacy.items.EndAnchor;
import com.aizistral.enigmaticlegacy.items.GolemHeart;
import com.aizistral.enigmaticlegacy.items.generic.GenericBlockItem;
import com.aizistral.enigmaticlegacy.objects.LoggerWrapper;
import com.aizistral.enigmaticlegacy.objects.RegisteredMeleeAttack;
import com.aizistral.enigmaticlegacy.objects.SpecialLootModifier;
import com.aizistral.enigmaticlegacy.packets.clients.PacketCosmicRevive;
import com.aizistral.enigmaticlegacy.packets.clients.PacketFlameParticles;
import com.aizistral.enigmaticlegacy.packets.clients.PacketForceArrowRotations;
import com.aizistral.enigmaticlegacy.packets.clients.PacketGenericParticleEffect;
import com.aizistral.enigmaticlegacy.packets.clients.PacketHandleItemPickup;
import com.aizistral.enigmaticlegacy.packets.clients.PacketPatchouliForce;
import com.aizistral.enigmaticlegacy.packets.clients.PacketPermadeath;
import com.aizistral.enigmaticlegacy.packets.clients.PacketPlayQuote;
import com.aizistral.enigmaticlegacy.packets.clients.PacketPlayerMotion;
import com.aizistral.enigmaticlegacy.packets.clients.PacketPlayerRotations;
import com.aizistral.enigmaticlegacy.packets.clients.PacketPlayerSetlook;
import com.aizistral.enigmaticlegacy.packets.clients.PacketPortalParticles;
import com.aizistral.enigmaticlegacy.packets.clients.PacketRecallParticles;
import com.aizistral.enigmaticlegacy.packets.clients.PacketSetEntryState;
import com.aizistral.enigmaticlegacy.packets.clients.PacketSlotUnlocked;
import com.aizistral.enigmaticlegacy.packets.clients.PacketSyncPlayTime;
import com.aizistral.enigmaticlegacy.packets.clients.PacketSyncTransientData;
import com.aizistral.enigmaticlegacy.packets.clients.PacketUpdateCompass;
import com.aizistral.enigmaticlegacy.packets.clients.PacketUpdateExperience;
import com.aizistral.enigmaticlegacy.packets.clients.PacketUpdateNotification;
import com.aizistral.enigmaticlegacy.packets.clients.PacketWitherParticles;
import com.aizistral.enigmaticlegacy.packets.server.PacketAnvilField;
import com.aizistral.enigmaticlegacy.packets.server.PacketConfirmTeleportation;
import com.aizistral.enigmaticlegacy.packets.server.PacketEnchantingGUI;
import com.aizistral.enigmaticlegacy.packets.server.PacketEnderRingKey;
import com.aizistral.enigmaticlegacy.packets.server.PacketInkwellField;
import com.aizistral.enigmaticlegacy.packets.server.PacketSpellstoneKey;
import com.aizistral.enigmaticlegacy.packets.server.PacketToggleMagnetEffects;
import com.aizistral.enigmaticlegacy.packets.server.PacketUpdateElytraBoosting;
import com.aizistral.enigmaticlegacy.packets.server.PacketXPScrollKey;
import com.aizistral.enigmaticlegacy.proxy.ClientProxy;
import com.aizistral.enigmaticlegacy.proxy.CommonProxy;
import com.aizistral.enigmaticlegacy.registries.EnigmaticBlocks;
import com.aizistral.enigmaticlegacy.registries.EnigmaticDamageTypes;
import com.aizistral.enigmaticlegacy.registries.EnigmaticEffects;
import com.aizistral.enigmaticlegacy.registries.EnigmaticEnchantments;
import com.aizistral.enigmaticlegacy.registries.EnigmaticEntities;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.aizistral.enigmaticlegacy.registries.EnigmaticLootFunctions;
import com.aizistral.enigmaticlegacy.registries.EnigmaticLootModifiers;
import com.aizistral.enigmaticlegacy.registries.EnigmaticMenus;
import com.aizistral.enigmaticlegacy.registries.EnigmaticPotions;
import com.aizistral.enigmaticlegacy.registries.EnigmaticRecipes;
import com.aizistral.enigmaticlegacy.registries.EnigmaticSounds;
import com.aizistral.enigmaticlegacy.registries.EnigmaticTabs;
import com.aizistral.enigmaticlegacy.registries.EnigmaticTiles;
import com.aizistral.enigmaticlegacy.triggers.BeheadingTrigger;
import com.aizistral.enigmaticlegacy.triggers.CursedInventoryChangedTrigger;
import com.aizistral.enigmaticlegacy.triggers.CursedRingEquippedTrigger;
import com.aizistral.enigmaticlegacy.triggers.ForbiddenFruitTrigger;
import com.aizistral.enigmaticlegacy.triggers.RevelationGainTrigger;
import com.aizistral.enigmaticlegacy.triggers.RevelationTomeBurntTrigger;
import com.aizistral.enigmaticlegacy.triggers.UseUnholyGrailTrigger;
import com.aizistral.etherium.blocks.BlockEtherium;
import com.aizistral.etherium.core.EtheriumEventHandler;
import com.aizistral.omniconfig.packets.PacketSyncOptions;
import com.google.common.collect.Lists;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.CreativeModeTabSearchRegistry;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.CreativeModeTabRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod(EnigmaticLegacy.MODID)
public class EnigmaticLegacy {
	public static final String MODID = "enigmaticlegacy";
	public static final String VERSION = "2.29.0";
	public static final String RELEASE_TYPE = "Release";
	public static final String NAME = "Enigmatic Legacy";

	private static final String PTC_VERSION = "1";
	public static final int HOW_COOL_I_AM = Integer.MAX_VALUE;
	public static final LoggerWrapper LOGGER = new LoggerWrapper("Enigmatic Legacy");
	public static final UUID SOUL_OF_THE_ARCHITECT = UUID.fromString("bfa45411-874a-4ee0-b3bd-00c716059d95");
	public static final CommonProxy PROXY = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

	public static EnigmaticLegacy enigmaticLegacy;
	public static SimpleChannel packetInstance;

	public static EnigmaticEventHandler enigmaticHandler;
	public static EnigmaticKeybindHandler keybindHandler;
	public static List<ResourceKey<DamageType>> damageTypesFire = new ArrayList<>();

	public static EtheriumConfigHandler etheriumConfig;

	public EnigmaticLegacy() {
		LOGGER.info("Constructing mod instance...");

		enigmaticLegacy = this;

		OmniconfigHandler.initialize();
		etheriumConfig = new EtheriumConfigHandler();

		EnigmaticMaterials.setEtheriumConfig(etheriumConfig);
		EnigmaticArmorMaterials.setEtheriumConfig(etheriumConfig);

		enigmaticHandler = new EnigmaticEventHandler();
		keybindHandler = new EnigmaticKeybindHandler();

		this.loadClass(EnigmaticItems.class);
		this.loadClass(EnigmaticTiles.class);
		this.loadClass(EnigmaticMenus.class);
		this.loadClass(EnigmaticBlocks.class);
		this.loadClass(EnigmaticSounds.class);
		this.loadClass(EnigmaticEffects.class);
		this.loadClass(EnigmaticRecipes.class);
		this.loadClass(EnigmaticEntities.class);
		this.loadClass(EnigmaticEnchantments.class);
		this.loadClass(EnigmaticLootModifiers.class);
		this.loadClass(EnigmaticTabs.class);

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientRegistries);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::intermodStuff);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onLoadComplete);
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
		FMLJavaModLoadingContext.get().getModEventBus().register(PROXY);

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(PROXY);
		MinecraftForge.EVENT_BUS.register(enigmaticHandler);
		MinecraftForge.EVENT_BUS.register(keybindHandler);
		MinecraftForge.EVENT_BUS.register(new EnigmaticUpdateHandler());
		MinecraftForge.EVENT_BUS.register(new EtheriumEventHandler(etheriumConfig, EnigmaticItems.ETHERIUM_ORE));
		MinecraftForge.EVENT_BUS.addListener(this::onServerStart);
		MinecraftForge.EVENT_BUS.addListener(this::onServerStarted);

		LOGGER.info("Mod instance constructed successfully.");
	}

	private void loadClass(Class<?> theClass) {
		try {
			Class.forName(theClass.getName());
		} catch (ClassNotFoundException ex) {
			throw new IllegalStateException("This can't be hapenning.");
		}
	}

	public void onLoadComplete(FMLLoadCompleteEvent event) {
		LOGGER.info("Initializing load completion phase...");

		EnigmaticItems.SPELLSTONES.add(EnigmaticItems.ANGEL_BLESSING);
		EnigmaticItems.SPELLSTONES.add(EnigmaticItems.GOLEM_HEART);
		EnigmaticItems.SPELLSTONES.add(EnigmaticItems.OCEAN_STONE);
		EnigmaticItems.SPELLSTONES.add(EnigmaticItems.BLAZING_CORE);
		EnigmaticItems.SPELLSTONES.add(EnigmaticItems.EYE_OF_NEBULA);
		EnigmaticItems.SPELLSTONES.add(EnigmaticItems.VOID_PEARL);
		EnigmaticItems.SPELLSTONES.add(EnigmaticItems.THE_CUBE);
		EnigmaticItems.SPELLSTONES.add(EnigmaticItems.ENIGMATIC_ITEM);

		LOGGER.info("Registering brewing recipes...");

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.RECALL_POTION)) {
			BrewingRecipeRegistry.addRecipe(new SpecialBrewingRecipe(Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.AWKWARD)), Ingredient.of(Items.ENDER_EYE), new ItemStack(EnigmaticItems.RECALL_POTION), new ResourceLocation(MODID, "recall_potion")));
		}

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.TWISTED_POTION)) {
			BrewingRecipeRegistry.addRecipe(new SpecialBrewingRecipe(Ingredient.of(EnigmaticItems.RECALL_POTION), Ingredient.of(EnigmaticItems.TWISTED_HEART), new ItemStack(EnigmaticItems.TWISTED_POTION), new ResourceLocation(MODID, "twisted_potion")));
		}

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.COMMON_POTION)) {
			PotionHelper.registerCommonPotions();
		}

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.ULTIMATE_POTION)) {
			PotionHelper.registerBasicUltimatePotions();
			PotionHelper.registerSplashUltimatePotions();
			PotionHelper.registerLingeringUltimatePotions();
		}

		BrewingRecipeRegistry.addRecipe(new ValidationBrewingRecipe(Ingredient.of(EnigmaticItems.RECALL_POTION, EnigmaticItems.TWISTED_POTION, EnigmaticItems.ULTIMATE_POTION_LINGERING, EnigmaticItems.COMMON_POTION_LINGERING), null));

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.THE_CUBE)) {
			HiddenRecipe.addRecipe(new ItemStack(EnigmaticItems.THE_CUBE),
					new ItemStack(EnigmaticItems.GOLEM_HEART), new ItemStack(EnigmaticItems.COSMIC_HEART), new ItemStack(EnigmaticItems.BLAZING_CORE),
					new ItemStack(EnigmaticItems.ANGEL_BLESSING), new ItemStack(Blocks.OBSIDIAN), new ItemStack(EnigmaticItems.EYE_OF_NEBULA),
					new ItemStack(EnigmaticItems.OCEAN_STONE), new ItemStack(EnigmaticItems.COSMIC_HEART), new ItemStack(EnigmaticItems.VOID_PEARL)
					);
		}

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.ASCENSION_AMULET)) {
			HiddenRecipe.addRecipe(new ItemStack(EnigmaticItems.ASCENSION_AMULET),
					new ItemStack(Items.AMETHYST_SHARD), new ItemStack(EnigmaticItems.ASTRAL_DUST), new ItemStack(Items.AMETHYST_SHARD),
					new ItemStack(EnigmaticItems.ETHERIUM_INGOT), new ItemStack(EnigmaticItems.ENIGMATIC_AMULET), new ItemStack(EnigmaticItems.ETHERIUM_INGOT),
					new ItemStack(Items.DRAGON_BREATH), new ItemStack(EnigmaticItems.COSMIC_HEART), new ItemStack(Items.DRAGON_BREATH)
					);
		}

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.ELDRITCH_AMULET)) {
			HiddenRecipe.addRecipe(new ItemStack(EnigmaticItems.ELDRITCH_AMULET),
					new ItemStack(EnigmaticItems.EVIL_ESSENCE), new ItemStack(EnigmaticItems.ABYSSAL_HEART), new ItemStack(EnigmaticItems.EVIL_ESSENCE),
					new ItemStack(Items.NETHERITE_INGOT), new ItemStack(EnigmaticItems.ASCENSION_AMULET), new ItemStack(Items.NETHERITE_INGOT),
					new ItemStack(EnigmaticItems.TWISTED_HEART), new ItemStack(Items.NETHER_STAR), new ItemStack(EnigmaticItems.TWISTED_HEART)
					);
		}

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.COSMIC_SCROLL)) {
			HiddenRecipe.addRecipe(new ItemStack(EnigmaticItems.COSMIC_SCROLL),
					new ItemStack(EnigmaticItems.ASTRAL_DUST), new ItemStack(Items.ENCHANTED_GOLDEN_APPLE), new ItemStack(EnigmaticItems.ASTRAL_DUST),
					new ItemStack(EnigmaticItems.ETHERIUM_INGOT), new ItemStack(EnigmaticItems.DARKEST_SCROLL), new ItemStack(EnigmaticItems.ETHERIUM_INGOT),
					new ItemStack(EnigmaticItems.ASTRAL_DUST), new ItemStack(EnigmaticItems.COSMIC_HEART), new ItemStack(EnigmaticItems.ASTRAL_DUST)
					);
		}

		EnigmaticUpdateHandler.init();

		PROXY.loadComplete(event);

		DevotedBelieversHandler.getDevotedBelievers().entrySet().forEach(entry -> {
			LOGGER.getInternal().info("Believer: {}, UUID: {}", entry.getKey(), entry.getValue());
		});

		LOGGER.info("Load completion phase finished successfully");
	}

	private void setup(FMLCommonSetupEvent event) {
		LOGGER.info("Initializing common setup phase...");

		event.enqueueWork(() -> this.loadClass(EnigmaticLootFunctions.class));
		event.enqueueWork(() -> this.loadClass(EnigmaticDamageTypes.class));
		event.enqueueWork(() -> this.loadClass(EnigmaticPotions.class));

		GolemHeart.buildArmorExclusions();

		damageTypesFire.add(DamageTypes.LAVA);
		damageTypesFire.add(DamageTypes.IN_FIRE);
		damageTypesFire.add(DamageTypes.ON_FIRE);

		LOGGER.info("Registering packets...");
		packetInstance = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MODID, "main")).networkProtocolVersion(() -> PTC_VERSION).clientAcceptedVersions(PTC_VERSION::equals).serverAcceptedVersions(PTC_VERSION::equals).simpleChannel();

		packetInstance.registerMessage(0, PacketRecallParticles.class, PacketRecallParticles::encode, PacketRecallParticles::decode, PacketRecallParticles::handle);
		packetInstance.registerMessage(1, PacketEnderRingKey.class, PacketEnderRingKey::encode, PacketEnderRingKey::decode, PacketEnderRingKey::handle);
		packetInstance.registerMessage(2, PacketSpellstoneKey.class, PacketSpellstoneKey::encode, PacketSpellstoneKey::decode, PacketSpellstoneKey::handle);
		packetInstance.registerMessage(3, PacketPlayerMotion.class, PacketPlayerMotion::encode, PacketPlayerMotion::decode, PacketPlayerMotion::handle);
		packetInstance.registerMessage(4, PacketPlayerRotations.class, PacketPlayerRotations::encode, PacketPlayerRotations::decode, PacketPlayerRotations::handle);
		packetInstance.registerMessage(5, PacketPlayerSetlook.class, PacketPlayerSetlook::encode, PacketPlayerSetlook::decode, PacketPlayerSetlook::handle);

		packetInstance.registerMessage(7, PacketConfirmTeleportation.class, PacketConfirmTeleportation::encode, PacketConfirmTeleportation::decode, PacketConfirmTeleportation::handle);
		packetInstance.registerMessage(8, PacketPortalParticles.class, PacketPortalParticles::encode, PacketPortalParticles::decode, PacketPortalParticles::handle);
		packetInstance.registerMessage(9, PacketXPScrollKey.class, PacketXPScrollKey::encode, PacketXPScrollKey::decode, PacketXPScrollKey::handle);
		packetInstance.registerMessage(10, PacketSlotUnlocked.class, PacketSlotUnlocked::encode, PacketSlotUnlocked::decode, PacketSlotUnlocked::handle);
		packetInstance.registerMessage(11, PacketHandleItemPickup.class, PacketHandleItemPickup::encode, PacketHandleItemPickup::decode, PacketHandleItemPickup::handle);
		packetInstance.registerMessage(12, PacketUpdateNotification.class, PacketUpdateNotification::encode, PacketUpdateNotification::decode, PacketUpdateNotification::handle);
		packetInstance.registerMessage(13, PacketAnvilField.class, PacketAnvilField::encode, PacketAnvilField::decode, PacketAnvilField::handle);
		packetInstance.registerMessage(14, PacketWitherParticles.class, PacketWitherParticles::encode, PacketWitherParticles::decode, PacketWitherParticles::handle);
		packetInstance.registerMessage(15, PacketFlameParticles.class, PacketFlameParticles::encode, PacketFlameParticles::decode, PacketFlameParticles::handle);
		packetInstance.registerMessage(16, PacketSetEntryState.class, PacketSetEntryState::encode, PacketSetEntryState::decode, PacketSetEntryState::handle);
		packetInstance.registerMessage(17, PacketForceArrowRotations.class, PacketForceArrowRotations::encode, PacketForceArrowRotations::decode, PacketForceArrowRotations::handle);
		packetInstance.registerMessage(18, PacketInkwellField.class, PacketInkwellField::encode, PacketInkwellField::decode, PacketInkwellField::handle);
		packetInstance.registerMessage(19, PacketSyncTransientData.class, PacketSyncTransientData::encode, PacketSyncTransientData::decode, PacketSyncTransientData::handle);
		packetInstance.registerMessage(20, PacketSyncOptions.class, PacketSyncOptions::encode, PacketSyncOptions::decode, PacketSyncOptions::handle);
		packetInstance.registerMessage(21, PacketGenericParticleEffect.class, PacketGenericParticleEffect::encode, PacketGenericParticleEffect::decode, PacketGenericParticleEffect::handle);
		packetInstance.registerMessage(22, PacketUpdateExperience.class, PacketUpdateExperience::encode, PacketUpdateExperience::decode, PacketUpdateExperience::handle);
		packetInstance.registerMessage(23, PacketToggleMagnetEffects.class, PacketToggleMagnetEffects::encode, PacketToggleMagnetEffects::decode, PacketToggleMagnetEffects::handle);
		packetInstance.registerMessage(24, PacketPatchouliForce.class, PacketPatchouliForce::encode, PacketPatchouliForce::decode, PacketPatchouliForce::handle);
		packetInstance.registerMessage(25, PacketSyncPlayTime.class, PacketSyncPlayTime::encode, PacketSyncPlayTime::decode, PacketSyncPlayTime::handle);
		packetInstance.registerMessage(26, PacketCosmicRevive.class, PacketCosmicRevive::encode, PacketCosmicRevive::decode, PacketCosmicRevive::handle);
		packetInstance.registerMessage(27, PacketEnchantingGUI.class, PacketEnchantingGUI::encode, PacketEnchantingGUI::decode, PacketEnchantingGUI::handle);
		packetInstance.registerMessage(28, PacketUpdateCompass.class, PacketUpdateCompass::encode, PacketUpdateCompass::decode, PacketUpdateCompass::handle);
		packetInstance.registerMessage(29, PacketPlayQuote.class, PacketPlayQuote::encode, PacketPlayQuote::decode, PacketPlayQuote::handle);
		packetInstance.registerMessage(30, PacketUpdateElytraBoosting.class, PacketUpdateElytraBoosting::encode, PacketUpdateElytraBoosting::decode, PacketUpdateElytraBoosting::handle);
		packetInstance.registerMessage(31, PacketPermadeath.class, PacketPermadeath::encode, PacketPermadeath::decode, PacketPermadeath::handle);

		LOGGER.info("Registering triggers...");
		CriteriaTriggers.register(UseUnholyGrailTrigger.INSTANCE);
		CriteriaTriggers.register(BeheadingTrigger.INSTANCE);
		CriteriaTriggers.register(RevelationGainTrigger.INSTANCE);
		CriteriaTriggers.register(CursedRingEquippedTrigger.INSTANCE);
		CriteriaTriggers.register(RevelationTomeBurntTrigger.INSTANCE);
		CriteriaTriggers.register(ForbiddenFruitTrigger.INSTANCE);
		CriteriaTriggers.register(CursedInventoryChangedTrigger.INSTANCE);

		LOGGER.info("Registering stats...");

		event.enqueueWork(() -> {
			Registry.register(BuiltInRegistries.CUSTOM_STAT, PlayerPlaytimeCounter.TIME_WITH_CURSES_STAT,
					PlayerPlaytimeCounter.TIME_WITH_CURSES_STAT);
			Stats.CUSTOM.get(PlayerPlaytimeCounter.TIME_WITH_CURSES_STAT, StatFormatter.TIME);

			Registry.register(BuiltInRegistries.CUSTOM_STAT, PlayerPlaytimeCounter.TIME_WITHOUT_CURSES_STAT,
					PlayerPlaytimeCounter.TIME_WITHOUT_CURSES_STAT);
			Stats.CUSTOM.get(PlayerPlaytimeCounter.TIME_WITHOUT_CURSES_STAT, StatFormatter.TIME);
		});

		LOGGER.info("Common setup phase finished successfully.");
	}

	private void clientRegistries(FMLClientSetupEvent event) {
		LOGGER.info("Initializing client setup phase...");
		EnigmaticItems.ENIGMATIC_AMULET.registerVariants();
		EnigmaticItems.ENIGMATIC_EYE.registerVariants();
		EnigmaticItems.SOUL_COMPASS.registerVariants();

		PROXY.initEntityRendering();

		//MenuScreens.register(PORTABLE_CRAFTER, CraftingScreen::new);
		MenuScreens.register(EnigmaticMenus.LORE_INSCRIBER_CONTAINER, LoreInscriberScreen::new);

		LOGGER.info("Client setup phase finished successfully.");
	}

	private void intermodStuff(InterModEnqueueEvent event) {
		LOGGER.info("Sending messages to Curios API...");
		SuperpositionHandler.registerCurioType("charm", 1, false, null);
		SuperpositionHandler.registerCurioType("ring", 2, false, null);
		SuperpositionHandler.registerCurioType("back", 1, false, null);
		SuperpositionHandler.registerCurioType("spellstone", 0, false, new ResourceLocation(MODID, "slots/empty_spellstone_slot"));
		SuperpositionHandler.registerCurioType("scroll", 0, false, new ResourceLocation(MODID, "slots/empty_scroll_slot"));
		//SuperpositionHandler.registerCurioType("curio", -1, true, false, null);

	}

	private void onServerStart(ServerAboutToStartEvent event) {
		this.performCleanup();
		SoulArchive.initialize(event.getServer());
	}

	private void onServerStarted(ServerStartedEvent event) {
		// NO-OP
	}

	/**
	 * Alright boys, it's cleanup time!
	 */

	public void performCleanup() {
		// TODO Figure something out with those multimaps
		// I'd really like there to be a weak multimap or something

		PROXY.clearTransientData();
		EnigmaticEventHandler.AGERED_GUARDIANS.clear();
		EnigmaticEventHandler.POSTMORTAL_POSESSIONS.clear();
		EnigmaticEventHandler.KNOCKBACK_THAT_BASTARD.clear();
		EnigmaticEventHandler.DEFERRED_TOASTS.clear();
		EnigmaticEventHandler.DESOLATION_BOXES.clear();
		EnigmaticEventHandler.LAST_SOUL_COMPASS_UPDATE.clear();
		EnigmaticEventHandler.LAST_HEALTH.clear();
		EnigmaticEventHandler.SCHEDULED_DATA_SYNC.clear();
		EnigmaticItems.SOUL_CRYSTAL.attributeDispatcher.clear();
		EnigmaticItems.ENIGMATIC_ITEM.flightMap.clear();
		EnigmaticItems.HEAVEN_SCROLL.flyMap.clear();
		EnigmaticItems.THE_CUBE.clearLocationCache();
		RegisteredMeleeAttack.clearRegistry();
	}

	public boolean isCSGPresent() {
		return ModList.get().isLoaded("customstartinggear");
	}

	public boolean isLockboxPresent() {
		return ModList.get().isLoaded("enigmaticlockbox");
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onRegisterKeybinds(RegisterKeyMappingsEvent event) {
		keybindHandler.enderRingKey = new KeyMapping("key.enderRing", GLFW.GLFW_KEY_I, "key.categories.enigmaticLegacy");
		keybindHandler.spellstoneAbilityKey = new KeyMapping("key.spellstoneAbility", GLFW.GLFW_KEY_K, "key.categories.enigmaticLegacy");
		keybindHandler.xpScrollKey = new KeyMapping("key.xpScroll", GLFW.GLFW_KEY_J, "key.categories.enigmaticLegacy");

		event.register(keybindHandler.enderRingKey);
		event.register(keybindHandler.spellstoneAbilityKey);
		event.register(keybindHandler.xpScrollKey);
	}

	//  Thx Forge :\
	//
	//	@SubscribeEvent
	//	@OnlyIn(Dist.CLIENT)
	//	public void stitchTextures(TextureStitchEvent.Pre event) {
	//		if (event.getAtlas().location() == InventoryMenu.BLOCK_ATLAS) {
	//			event.addSprite(new ResourceLocation(MODID, "slots/empty_spellstone_slot"));
	//			event.addSprite(new ResourceLocation(MODID, "slots/empty_scroll_slot"));
	//		}
	//	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onColorInit(RegisterColorHandlersEvent.Item event) {
		LOGGER.info("Initializing colors registration...");

		event.register((stack, color) -> {
			if (PotionHelper.isAdvancedPotion(stack))
				return color > 0 ? -1 : PotionHelper.getColor(stack);
				else return color > 0 ? -1 : PotionUtils.getColor(stack);
		}, EnigmaticItems.ULTIMATE_POTION, EnigmaticItems.ULTIMATE_POTION_SPLASH,
				EnigmaticItems.ULTIMATE_POTION_LINGERING, EnigmaticItems.COMMON_POTION,
				EnigmaticItems.COMMON_POTION_SPLASH, EnigmaticItems.COMMON_POTION_LINGERING);

		LOGGER.info("Colors registered successfully.");
	}

	@SubscribeEvent
	public void onCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
		ForgeRegistries.ITEMS.forEach(item -> {
			if (item instanceof ICreativeTabMember member) {
				if (event.getTab() != member.getCreativeTab())
					return;

				member.getCreativeTabStacks().forEach(event::accept);
			}
		});
	}

	public static final Rarity LEGENDARY = Rarity.create("legendary", ChatFormatting.GOLD);

}
