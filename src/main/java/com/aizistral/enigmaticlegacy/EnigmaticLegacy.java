package com.aizistral.enigmaticlegacy;

import com.aizistral.enigmaticlegacy.api.capabilities.PlayerPlaytimeCounter;
import com.aizistral.enigmaticlegacy.api.items.ICreativeTabMember;
import com.aizistral.enigmaticlegacy.api.materials.EnigmaticArmorMaterials;
import com.aizistral.enigmaticlegacy.api.materials.EnigmaticMaterials;
import com.aizistral.enigmaticlegacy.brewing.SpecialBrewingRecipe;
import com.aizistral.enigmaticlegacy.brewing.ValidationBrewingRecipe;
import com.aizistral.enigmaticlegacy.config.EtheriumConfigHandler;
import com.aizistral.enigmaticlegacy.config.OmniconfigHandler;
import com.aizistral.enigmaticlegacy.crafting.HiddenRecipe;
import com.aizistral.enigmaticlegacy.gui.containers.LoreInscriberScreen;
import com.aizistral.enigmaticlegacy.handlers.*;
import com.aizistral.enigmaticlegacy.helpers.PotionHelper;
import com.aizistral.enigmaticlegacy.items.GolemHeart;
import com.aizistral.enigmaticlegacy.objects.LoggerWrapper;
import com.aizistral.enigmaticlegacy.objects.RegisteredMeleeAttack;
import com.aizistral.enigmaticlegacy.packets.clients.*;
import com.aizistral.enigmaticlegacy.packets.server.*;
import com.aizistral.enigmaticlegacy.proxy.ClientProxy;
import com.aizistral.enigmaticlegacy.proxy.CommonProxy;
import com.aizistral.enigmaticlegacy.registries.*;
import com.aizistral.enigmaticlegacy.triggers.*;
import com.aizistral.etherium.core.EtheriumEventHandler;
import com.aizistral.omniconfig.packets.PacketSyncOptions;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
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
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mod(EnigmaticLegacy.MODID)
public class EnigmaticLegacy {
	public static final String MODID = "enigmaticlegacy";
	public static final String VERSION = "2.30.1";
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

		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		EnigmaticItems.registerItems(modEventBus);
		EnigmaticTiles.registerTiles(modEventBus);
		EnigmaticMenus.registerMenus(modEventBus);
		EnigmaticBlocks.registerBlocks(modEventBus);
		EnigmaticSounds.registerSounds(modEventBus);
		EnigmaticEffects.registerEffects(modEventBus);
		EnigmaticRecipes.registerRecipes(modEventBus);
		EnigmaticEntities.registerEntities(modEventBus);
		EnigmaticEnchantments.registerEnchantments(modEventBus);
		EnigmaticLootModifiers.registerLootModifiers(modEventBus);
		EnigmaticTabs.registerTabs(modEventBus);

		modEventBus.addListener(this::setup);
		modEventBus.addListener(this::clientRegistries);
		modEventBus.addListener(this::intermodStuff);
		modEventBus.addListener(this::onLoadComplete);
		modEventBus.register(this);
		modEventBus.register(PROXY);

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(PROXY);
		MinecraftForge.EVENT_BUS.register(enigmaticHandler);
		MinecraftForge.EVENT_BUS.register(keybindHandler);
		MinecraftForge.EVENT_BUS.register(new EnigmaticUpdateHandler());
		MinecraftForge.EVENT_BUS.register(new EtheriumEventHandler(etheriumConfig, EnigmaticItems.ETHERIUM_ORE::get));
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

		EnigmaticItems.SPELLSTONES.add(EnigmaticItems.ANGEL_BLESSING.get());
		EnigmaticItems.SPELLSTONES.add(EnigmaticItems.GOLEM_HEART.get());
		EnigmaticItems.SPELLSTONES.add(EnigmaticItems.OCEAN_STONE.get());
		EnigmaticItems.SPELLSTONES.add(EnigmaticItems.BLAZING_CORE.get());
		EnigmaticItems.SPELLSTONES.add(EnigmaticItems.EYE_OF_NEBULA.get());
		EnigmaticItems.SPELLSTONES.add(EnigmaticItems.VOID_PEARL.get());
		EnigmaticItems.SPELLSTONES.add(EnigmaticItems.THE_CUBE.get());
		EnigmaticItems.SPELLSTONES.add(EnigmaticItems.ENIGMATIC_ITEM.get());

		LOGGER.info("Registering brewing recipes...");

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.RECALL_POTION.get())) {
			BrewingRecipeRegistry.addRecipe(new SpecialBrewingRecipe(Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.AWKWARD)), Ingredient.of(Items.ENDER_EYE), new ItemStack(EnigmaticItems.RECALL_POTION.get()), new ResourceLocation(MODID, "recall_potion")));
		}

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.TWISTED_POTION.get())) {
			BrewingRecipeRegistry.addRecipe(new SpecialBrewingRecipe(Ingredient.of(EnigmaticItems.RECALL_POTION.get()), Ingredient.of(EnigmaticItems.TWISTED_HEART.get()), new ItemStack(EnigmaticItems.TWISTED_POTION.get()), new ResourceLocation(MODID, "twisted_potion")));
		}

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.COMMON_POTION.get())) {
			PotionHelper.registerCommonPotions();
		}

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.ULTIMATE_POTION.get())) {
			PotionHelper.registerBasicUltimatePotions();
			PotionHelper.registerSplashUltimatePotions();
			PotionHelper.registerLingeringUltimatePotions();
		}

		BrewingRecipeRegistry.addRecipe(new ValidationBrewingRecipe(Ingredient.of(EnigmaticItems.RECALL_POTION.get(), EnigmaticItems.TWISTED_POTION.get(), EnigmaticItems.ULTIMATE_POTION_LINGERING.get(), EnigmaticItems.COMMON_POTION_LINGERING.get()), null));

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.THE_CUBE.get())) {
			HiddenRecipe.addRecipe(new ItemStack(EnigmaticItems.THE_CUBE.get()),
					new ItemStack(EnigmaticItems.GOLEM_HEART.get()), new ItemStack(EnigmaticItems.COSMIC_HEART.get()), new ItemStack(EnigmaticItems.BLAZING_CORE.get()),
					new ItemStack(EnigmaticItems.ANGEL_BLESSING.get()), new ItemStack(Blocks.OBSIDIAN), new ItemStack(EnigmaticItems.EYE_OF_NEBULA.get()),
					new ItemStack(EnigmaticItems.OCEAN_STONE.get()), new ItemStack(EnigmaticItems.COSMIC_HEART.get()), new ItemStack(EnigmaticItems.VOID_PEARL.get())
					);
		}

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.ASCENSION_AMULET.get())) {
			HiddenRecipe.addRecipe(new ItemStack(EnigmaticItems.ASCENSION_AMULET.get()),
					new ItemStack(Items.AMETHYST_SHARD), new ItemStack(EnigmaticItems.ASTRAL_DUST.get()), new ItemStack(Items.AMETHYST_SHARD),
					new ItemStack(EnigmaticItems.ETHERIUM_INGOT.get()), new ItemStack(EnigmaticItems.ENIGMATIC_AMULET.get()), new ItemStack(EnigmaticItems.ETHERIUM_INGOT.get()),
					new ItemStack(Items.DRAGON_BREATH), new ItemStack(EnigmaticItems.COSMIC_HEART.get()), new ItemStack(Items.DRAGON_BREATH)
					);
		}

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.ELDRITCH_AMULET.get())) {
			HiddenRecipe.addRecipe(new ItemStack(EnigmaticItems.ELDRITCH_AMULET.get()),
					new ItemStack(EnigmaticItems.EVIL_ESSENCE.get()), new ItemStack(EnigmaticItems.ABYSSAL_HEART.get()), new ItemStack(EnigmaticItems.EVIL_ESSENCE.get()),
					new ItemStack(Items.NETHERITE_INGOT), new ItemStack(EnigmaticItems.ASCENSION_AMULET.get()), new ItemStack(Items.NETHERITE_INGOT),
					new ItemStack(EnigmaticItems.TWISTED_HEART.get()), new ItemStack(Items.NETHER_STAR), new ItemStack(EnigmaticItems.TWISTED_HEART.get())
					);
		}

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.COSMIC_SCROLL.get())) {
			HiddenRecipe.addRecipe(new ItemStack(EnigmaticItems.COSMIC_SCROLL.get()),
					new ItemStack(EnigmaticItems.ASTRAL_DUST.get()), new ItemStack(Items.ENCHANTED_GOLDEN_APPLE), new ItemStack(EnigmaticItems.ASTRAL_DUST.get()),
					new ItemStack(EnigmaticItems.ETHERIUM_INGOT.get()), new ItemStack(EnigmaticItems.DARKEST_SCROLL.get()), new ItemStack(EnigmaticItems.ETHERIUM_INGOT.get()),
					new ItemStack(EnigmaticItems.ASTRAL_DUST.get()), new ItemStack(EnigmaticItems.COSMIC_HEART.get()), new ItemStack(EnigmaticItems.ASTRAL_DUST.get())
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
		EnigmaticItems.ENIGMATIC_AMULET.get().registerVariants();
		EnigmaticItems.ENIGMATIC_EYE.get().registerVariants();
		EnigmaticItems.SOUL_COMPASS.get().registerVariants();

		PROXY.initEntityRendering();

		//MenuScreens.register(PORTABLE_CRAFTER, CraftingScreen::new);
		MenuScreens.register(EnigmaticMenus.LORE_INSCRIBER_CONTAINER.get(), LoreInscriberScreen::new);

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
		EnigmaticItems.SOUL_CRYSTAL.get().attributeDispatcher.clear();
		EnigmaticItems.ENIGMATIC_ITEM.get().flightMap.clear();
		EnigmaticItems.HEAVEN_SCROLL.get().flyMap.clear();
		EnigmaticItems.THE_CUBE.get().clearLocationCache();
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
		}, EnigmaticItems.ULTIMATE_POTION.get(), EnigmaticItems.ULTIMATE_POTION_SPLASH.get(),
				EnigmaticItems.ULTIMATE_POTION_LINGERING.get(), EnigmaticItems.COMMON_POTION.get(),
				EnigmaticItems.COMMON_POTION_SPLASH.get(), EnigmaticItems.COMMON_POTION_LINGERING.get());

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
