package com.integral.enigmaticlegacy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Triple;

import com.google.common.collect.Lists;
import com.integral.enigmaticlegacy.api.items.IAdvancedPotionItem.PotionType;
import com.integral.enigmaticlegacy.api.materials.EnigmaticArmorMaterials;
import com.integral.enigmaticlegacy.api.materials.EnigmaticMaterials;
import com.integral.enigmaticlegacy.blocks.BlockAstralDust;
import com.integral.enigmaticlegacy.blocks.BlockBigLamp;
import com.integral.enigmaticlegacy.blocks.BlockCosmicCake;
import com.integral.enigmaticlegacy.blocks.BlockEndAnchor;
import com.integral.enigmaticlegacy.blocks.BlockMassiveLamp;
import com.integral.enigmaticlegacy.blocks.TileEndAnchor;
import com.integral.enigmaticlegacy.brewing.SpecialBrewingRecipe;
import com.integral.enigmaticlegacy.brewing.ValidationBrewingRecipe;
import com.integral.enigmaticlegacy.client.Quote;
import com.integral.enigmaticlegacy.config.EtheriumConfigHandler;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.crafting.HiddenRecipe;
import com.integral.enigmaticlegacy.effects.BlazingStrengthEffect;
import com.integral.enigmaticlegacy.effects.MoltenHeartEffect;
import com.integral.enigmaticlegacy.entities.EnigmaticPotionEntity;
import com.integral.enigmaticlegacy.entities.PermanentItemEntity;
import com.integral.enigmaticlegacy.entities.UltimateWitherSkullEntity;
import com.integral.enigmaticlegacy.gui.containers.LoreInscriberContainer;
import com.integral.enigmaticlegacy.gui.containers.LoreInscriberScreen;
import com.integral.enigmaticlegacy.handlers.DevotedBelieversHandler;
import com.integral.enigmaticlegacy.handlers.EnigmaticEventHandler;
import com.integral.enigmaticlegacy.handlers.EnigmaticKeybindHandler;
import com.integral.enigmaticlegacy.handlers.EnigmaticUpdateHandler;
import com.integral.enigmaticlegacy.handlers.SoulArchive;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.PotionHelper;
import com.integral.enigmaticlegacy.items.CosmicCake;
import com.integral.enigmaticlegacy.items.CosmicScroll;
import com.integral.enigmaticlegacy.items.EndAnchor;
import com.integral.enigmaticlegacy.items.generic.GenericBlockItem;
import com.integral.enigmaticlegacy.objects.LoggerWrapper;
import com.integral.enigmaticlegacy.objects.RegisteredMeleeAttack;
import com.integral.enigmaticlegacy.objects.SpecialLootModifier;
import com.integral.enigmaticlegacy.packets.clients.PacketCosmicRevive;
import com.integral.enigmaticlegacy.packets.clients.PacketFlameParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketForceArrowRotations;
import com.integral.enigmaticlegacy.packets.clients.PacketGenericParticleEffect;
import com.integral.enigmaticlegacy.packets.clients.PacketHandleItemPickup;
import com.integral.enigmaticlegacy.packets.clients.PacketPatchouliForce;
import com.integral.enigmaticlegacy.packets.clients.PacketPermadeath;
import com.integral.enigmaticlegacy.packets.clients.PacketPlayQuote;
import com.integral.enigmaticlegacy.packets.clients.PacketPlayerMotion;
import com.integral.enigmaticlegacy.packets.clients.PacketPlayerRotations;
import com.integral.enigmaticlegacy.packets.clients.PacketPlayerSetlook;
import com.integral.enigmaticlegacy.packets.clients.PacketPortalParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketRecallParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketSetEntryState;
import com.integral.enigmaticlegacy.packets.clients.PacketSlotUnlocked;
import com.integral.enigmaticlegacy.packets.clients.PacketSyncPlayTime;
import com.integral.enigmaticlegacy.packets.clients.PacketSyncTransientData;
import com.integral.enigmaticlegacy.packets.clients.PacketUpdateCompass;
import com.integral.enigmaticlegacy.packets.clients.PacketUpdateExperience;
import com.integral.enigmaticlegacy.packets.clients.PacketUpdateNotification;
import com.integral.enigmaticlegacy.packets.clients.PacketWitherParticles;
import com.integral.enigmaticlegacy.packets.server.PacketAnvilField;
import com.integral.enigmaticlegacy.packets.server.PacketConfirmTeleportation;
import com.integral.enigmaticlegacy.packets.server.PacketEnchantingGUI;
import com.integral.enigmaticlegacy.packets.server.PacketEnderRingKey;
import com.integral.enigmaticlegacy.packets.server.PacketInkwellField;
import com.integral.enigmaticlegacy.packets.server.PacketSpellstoneKey;
import com.integral.enigmaticlegacy.packets.server.PacketToggleMagnetEffects;
import com.integral.enigmaticlegacy.packets.server.PacketUpdateElytraBoosting;
import com.integral.enigmaticlegacy.packets.server.PacketXPScrollKey;
import com.integral.enigmaticlegacy.proxy.ClientProxy;
import com.integral.enigmaticlegacy.proxy.CommonProxy;
import com.integral.enigmaticlegacy.registry.EnigmaticBlocks;
import com.integral.enigmaticlegacy.registry.EnigmaticEffects;
import com.integral.enigmaticlegacy.registry.EnigmaticEnchantments;
import com.integral.enigmaticlegacy.registry.EnigmaticEntities;
import com.integral.enigmaticlegacy.registry.EnigmaticItems;
import com.integral.enigmaticlegacy.registry.EnigmaticLootModifiers;
import com.integral.enigmaticlegacy.registry.EnigmaticMenus;
import com.integral.enigmaticlegacy.registry.EnigmaticPotions;
import com.integral.enigmaticlegacy.registry.EnigmaticRecipes;
import com.integral.enigmaticlegacy.registry.EnigmaticSounds;
import com.integral.enigmaticlegacy.registry.EnigmaticTiles;
import com.integral.enigmaticlegacy.triggers.BeheadingTrigger;
import com.integral.enigmaticlegacy.triggers.CursedInventoryChangedTrigger;
import com.integral.enigmaticlegacy.triggers.CursedRingEquippedTrigger;
import com.integral.enigmaticlegacy.triggers.ForbiddenFruitTrigger;
import com.integral.enigmaticlegacy.triggers.RevelationGainTrigger;
import com.integral.enigmaticlegacy.triggers.RevelationTomeBurntTrigger;
import com.integral.enigmaticlegacy.triggers.UseUnholyGrailTrigger;
import com.integral.etherium.blocks.BlockEtherium;
import com.integral.etherium.core.EtheriumEventHandler;
import com.integral.omniconfig.packets.PacketSyncOptions;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.CreativeModeTab;
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
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.extensions.IForgeMenuType;
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
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(EnigmaticLegacy.MODID)
public class EnigmaticLegacy {
	public static final String MODID = "enigmaticlegacy";
	public static final String VERSION = "2.25.0";
	public static final String RELEASE_TYPE = "Release";
	public static final String NAME = "Enigmatic Legacy";

	public static EnigmaticLegacy enigmaticLegacy;
	public static final LoggerWrapper LOGGER = new LoggerWrapper("Enigmatic Legacy");
	public static SimpleChannel packetInstance;

	public static final int HOW_COOL_I_AM = Integer.MAX_VALUE;

	public static EnigmaticEventHandler enigmaticHandler;
	public static EnigmaticKeybindHandler keybindHandler;
	public static List<String> damageTypesFire = new ArrayList<String>();

	public static ResourceLocation timeWithCursesStat;
	public static ResourceLocation timeWithoutCursesStat;

	public static final UUID SOUL_OF_THE_ARCHITECT = UUID.fromString("bfa45411-874a-4ee0-b3bd-00c716059d95");

	public static EtheriumConfigHandler etheriumConfig;

	private static final String PTC_VERSION = "1";

	public static final CommonProxy PROXY = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
	public static final List<Triple<LootTable, LootPool, Exception>> exceptionList = new ArrayList<>();

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

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientRegistries);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::intermodStuff);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onLoadComplete);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(keybindHandler::onRegisterKeybinds);
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
		FMLJavaModLoadingContext.get().getModEventBus().register(PROXY);

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(PROXY);
		MinecraftForge.EVENT_BUS.register(enigmaticHandler);
		MinecraftForge.EVENT_BUS.register(keybindHandler);
		MinecraftForge.EVENT_BUS.register(new EnigmaticUpdateHandler());
		MinecraftForge.EVENT_BUS.register(new EtheriumEventHandler(etheriumConfig, EnigmaticItems.etheriumOre));
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

		EnigmaticItems.SPELLSTONES.add(EnigmaticItems.angelBlessing);
		EnigmaticItems.SPELLSTONES.add(EnigmaticItems.golemHeart);
		EnigmaticItems.SPELLSTONES.add(EnigmaticItems.oceanStone);
		EnigmaticItems.SPELLSTONES.add(EnigmaticItems.magmaHeart);
		EnigmaticItems.SPELLSTONES.add(EnigmaticItems.eyeOfNebula);
		EnigmaticItems.SPELLSTONES.add(EnigmaticItems.voidPearl);
		EnigmaticItems.SPELLSTONES.add(EnigmaticItems.theCube);
		EnigmaticItems.SPELLSTONES.add(EnigmaticItems.enigmaticItem);

		LOGGER.info("Registering brewing recipes...");

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.recallPotion)) {
			BrewingRecipeRegistry.addRecipe(new SpecialBrewingRecipe(Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.AWKWARD)), Ingredient.of(Items.ENDER_EYE), new ItemStack(EnigmaticItems.recallPotion), new ResourceLocation(MODID, "recall_potion")));
		}

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.twistedPotion)) {
			BrewingRecipeRegistry.addRecipe(new SpecialBrewingRecipe(Ingredient.of(EnigmaticItems.recallPotion), Ingredient.of(EnigmaticItems.twistedCore), new ItemStack(EnigmaticItems.twistedPotion), new ResourceLocation(MODID, "twisted_potion")));
		}

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.commonPotionBase)) {
			PotionHelper.registerCommonPotions();
		}

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.ultimatePotionBase)) {
			PotionHelper.registerBasicUltimatePotions();
			PotionHelper.registerSplashUltimatePotions();
			PotionHelper.registerLingeringUltimatePotions();
		}

		BrewingRecipeRegistry.addRecipe(new ValidationBrewingRecipe(Ingredient.of(EnigmaticItems.recallPotion, EnigmaticItems.twistedPotion, EnigmaticItems.ultimatePotionLingering, EnigmaticItems.commonPotionLingering), null));

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.theCube)) {
			HiddenRecipe.addRecipe(new ItemStack(EnigmaticItems.theCube),
					new ItemStack(EnigmaticItems.golemHeart), new ItemStack(EnigmaticItems.cosmicHeart), new ItemStack(EnigmaticItems.magmaHeart),
					new ItemStack(EnigmaticItems.angelBlessing), new ItemStack(Blocks.OBSIDIAN), new ItemStack(EnigmaticItems.eyeOfNebula),
					new ItemStack(EnigmaticItems.oceanStone), new ItemStack(EnigmaticItems.cosmicHeart), new ItemStack(EnigmaticItems.voidPearl)
					);
		}

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.ascensionAmulet)) {
			HiddenRecipe.addRecipe(new ItemStack(EnigmaticItems.ascensionAmulet),
					new ItemStack(Items.AMETHYST_SHARD), new ItemStack(EnigmaticItems.astralDust), new ItemStack(Items.AMETHYST_SHARD),
					new ItemStack(EnigmaticItems.etheriumIngot), new ItemStack(EnigmaticItems.enigmaticAmulet), new ItemStack(EnigmaticItems.etheriumIngot),
					new ItemStack(Items.DRAGON_BREATH), new ItemStack(EnigmaticItems.cosmicHeart), new ItemStack(Items.DRAGON_BREATH)
					);
		}

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.eldritchAmulet)) {
			HiddenRecipe.addRecipe(new ItemStack(EnigmaticItems.eldritchAmulet),
					new ItemStack(EnigmaticItems.evilEssence), new ItemStack(EnigmaticItems.abyssalHeart), new ItemStack(EnigmaticItems.evilEssence),
					new ItemStack(Items.NETHERITE_INGOT), new ItemStack(EnigmaticItems.ascensionAmulet), new ItemStack(Items.NETHERITE_INGOT),
					new ItemStack(EnigmaticItems.twistedCore), new ItemStack(Items.NETHER_STAR), new ItemStack(EnigmaticItems.twistedCore)
					);
		}

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.cosmicScroll)) {
			HiddenRecipe.addRecipe(new ItemStack(EnigmaticItems.cosmicScroll),
					new ItemStack(EnigmaticItems.astralDust), new ItemStack(Items.ENCHANTED_GOLDEN_APPLE), new ItemStack(EnigmaticItems.astralDust),
					new ItemStack(EnigmaticItems.etheriumIngot), new ItemStack(EnigmaticItems.darkestScroll), new ItemStack(EnigmaticItems.etheriumIngot),
					new ItemStack(EnigmaticItems.astralDust), new ItemStack(EnigmaticItems.cosmicHeart), new ItemStack(EnigmaticItems.astralDust)
					);
		}

		EnigmaticUpdateHandler.init();

		PROXY.loadComplete(event);

		DevotedBelieversHandler.getDevotedBelievers().entrySet().forEach(entry -> {
			LOGGER.getInternal().info("Believer: {}, UUID: {}", entry.getKey(), entry.getValue());
		});

		LOGGER.info("Load completion phase finished successfully");
	}

	private void setup(final FMLCommonSetupEvent event) {
		LOGGER.info("Initializing common setup phase...");

		this.loadClass(EnigmaticPotions.class);

		damageTypesFire.add(DamageSource.LAVA.msgId);
		damageTypesFire.add(DamageSource.IN_FIRE.msgId);
		damageTypesFire.add(DamageSource.ON_FIRE.msgId);

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
			timeWithCursesStat = this.makeCustomStat("play_time_with_seven_curses", StatFormatter.TIME);
			timeWithoutCursesStat = this.makeCustomStat("play_time_without_seven_curses", StatFormatter.TIME);
		});

		LOGGER.info("Common setup phase finished successfully.");
	}

	private void clientRegistries(final FMLClientSetupEvent event) {
		LOGGER.info("Initializing client setup phase...");
		EnigmaticItems.enigmaticAmulet.registerVariants();
		EnigmaticItems.architectEye.registerVariants();
		EnigmaticItems.soulCompass.registerVariants();

		PROXY.initEntityRendering();

		//MenuScreens.register(PORTABLE_CRAFTER, CraftingScreen::new);
		MenuScreens.register(EnigmaticMenus.LORE_INSCRIBER_CONTAINER, LoreInscriberScreen::new);

		LOGGER.info("Client setup phase finished successfully.");
	}

	private void intermodStuff(final InterModEnqueueEvent event) {
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
	 * @param event
	 */

	public void performCleanup() {
		// TODO Figure something out with those multimaps
		// I'd really like there to be a weak multimap or something

		PROXY.clearTransientData();
		EnigmaticEventHandler.angeredGuardians.clear();
		EnigmaticEventHandler.postmortalPossession.clear();
		EnigmaticEventHandler.knockbackThatBastard.clear();
		EnigmaticEventHandler.deferredToast.clear();
		EnigmaticEventHandler.desolationBoxes.clear();
		EnigmaticEventHandler.lastSoulCompassUpdate.clear();
		EnigmaticEventHandler.lastHealth.clear();
		EnigmaticItems.soulCrystal.attributeDispatcher.clear();
		EnigmaticItems.enigmaticItem.flightMap.clear();
		EnigmaticItems.heavenScroll.flyMap.clear();
		EnigmaticItems.theCube.clearLocationCache();
		RegisteredMeleeAttack.clearRegistry();
	}

	public boolean isCSGPresent() {
		return ModList.get().isLoaded("customstartinggear");
	}

	public boolean isLockboxPresent() {
		return ModList.get().isLoaded("enigmaticlockbox");
	}

	private ResourceLocation makeCustomStat(String pKey, StatFormatter pFormatter) {
		ResourceLocation resourcelocation = new ResourceLocation(EnigmaticLegacy.MODID, pKey);
		Registry.register(Registry.CUSTOM_STAT, pKey, resourcelocation);
		Stats.CUSTOM.get(resourcelocation, pFormatter);
		return resourcelocation;
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void stitchTextures(TextureStitchEvent.Pre evt) {
		if (evt.getAtlas().location() == InventoryMenu.BLOCK_ATLAS) {
			evt.addSprite(new ResourceLocation(MODID, "slots/empty_spellstone_slot"));
			evt.addSprite(new ResourceLocation(MODID, "slots/empty_scroll_slot"));
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onColorInit(final RegisterColorHandlersEvent.Item event) {
		LOGGER.info("Initializing colors registration...");

		event.register((stack, color) -> {
			if (PotionHelper.isAdvancedPotion(stack))
				return color > 0 ? -1 : PotionHelper.getColor(stack);

				return color > 0 ? -1 : PotionUtils.getColor(stack);
		}, EnigmaticItems.ultimatePotionBase, EnigmaticItems.ultimatePotionSplash,
				EnigmaticItems.ultimatePotionLingering, EnigmaticItems.commonPotionBase,
				EnigmaticItems.commonPotionSplash, EnigmaticItems.commonPotionLingering);

		LOGGER.info("Colors registered successfully.");
	}

	public static final CreativeModeTab enigmaticTab = new CreativeModeTab("enigmaticCreativeTab") {
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack makeIcon() {
			return new ItemStack(EnigmaticItems.enigmaticItem);
		}
	};

	public static final CreativeModeTab enigmaticPotionTab = new CreativeModeTab("enigmaticPotionCreativeTab") {
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack makeIcon() {
			return new ItemStack(EnigmaticItems.recallPotion);
		}
	};

	public static final Rarity LEGENDARY = Rarity.create("legendary", ChatFormatting.GOLD);

}
