package com.integral.enigmaticlegacy;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.integral.enigmaticlegacy.brewing.SpecialBrewingRecipe;
import com.integral.enigmaticlegacy.brewing.ValidationBrewingRecipe;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.crafting.EnigmaticRecipeSerializers;
import com.integral.enigmaticlegacy.entities.EnigmaticPotionEntity;
import com.integral.enigmaticlegacy.entities.PermanentItemEntity;
import com.integral.enigmaticlegacy.entities.UltimateWitherSkullEntity;
import com.integral.enigmaticlegacy.handlers.EnigmaticArmorMaterials;
import com.integral.enigmaticlegacy.handlers.EnigmaticEventHandler;
import com.integral.enigmaticlegacy.handlers.EnigmaticKeybindHandler;
import com.integral.enigmaticlegacy.handlers.EnigmaticMaterials;
import com.integral.enigmaticlegacy.handlers.EnigmaticUpdateHandler;
import com.integral.enigmaticlegacy.handlers.OneSpecialHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.AdvancedPotion;
import com.integral.enigmaticlegacy.helpers.PotionHelper;
import com.integral.enigmaticlegacy.items.AngelBlessing;
import com.integral.enigmaticlegacy.items.AstralBreaker;
import com.integral.enigmaticlegacy.items.AstralDust;
import com.integral.enigmaticlegacy.items.EnchantmentTransposer;
import com.integral.enigmaticlegacy.items.EnderRing;
import com.integral.enigmaticlegacy.items.EnderRod;
import com.integral.enigmaticlegacy.items.EnigmaticAmulet;
import com.integral.enigmaticlegacy.items.EnigmaticItem;
import com.integral.enigmaticlegacy.items.EscapeScroll;
import com.integral.enigmaticlegacy.items.EtheriumArmor;
import com.integral.enigmaticlegacy.items.EtheriumAxe;
import com.integral.enigmaticlegacy.items.EtheriumIngot;
import com.integral.enigmaticlegacy.items.EtheriumOre;
import com.integral.enigmaticlegacy.items.EtheriumPickaxe;
import com.integral.enigmaticlegacy.items.EtheriumScythe;
import com.integral.enigmaticlegacy.items.EtheriumShovel;
import com.integral.enigmaticlegacy.items.EtheriumSword;
import com.integral.enigmaticlegacy.items.ExtradimensionalEye;
import com.integral.enigmaticlegacy.items.EyeOfNebula;
import com.integral.enigmaticlegacy.items.ForbiddenAxe;
import com.integral.enigmaticlegacy.items.GolemHeart;
import com.integral.enigmaticlegacy.items.HastePotion;
import com.integral.enigmaticlegacy.items.HeavenScroll;
import com.integral.enigmaticlegacy.items.IronRing;
import com.integral.enigmaticlegacy.items.LootGenerator;
import com.integral.enigmaticlegacy.items.LoreFragment;
import com.integral.enigmaticlegacy.items.LoreInscriber;
import com.integral.enigmaticlegacy.items.MagmaHeart;
import com.integral.enigmaticlegacy.items.MagnetRing;
import com.integral.enigmaticlegacy.items.Megasponge;
import com.integral.enigmaticlegacy.items.MendingMixture;
import com.integral.enigmaticlegacy.items.MiningCharm;
import com.integral.enigmaticlegacy.items.MonsterCharm;
import com.integral.enigmaticlegacy.items.OblivionStone;
import com.integral.enigmaticlegacy.items.OceanStone;
import com.integral.enigmaticlegacy.items.RecallPotion;
import com.integral.enigmaticlegacy.items.RelicOfTesting;
import com.integral.enigmaticlegacy.items.SuperMagnetRing;
import com.integral.enigmaticlegacy.items.ThiccScroll;
import com.integral.enigmaticlegacy.items.UltimatePotionBase;
import com.integral.enigmaticlegacy.items.UltimatePotionLingering;
import com.integral.enigmaticlegacy.items.UltimatePotionSplash;
import com.integral.enigmaticlegacy.items.UnholyGrail;
import com.integral.enigmaticlegacy.items.VoidPearl;
import com.integral.enigmaticlegacy.items.XPScroll;
import com.integral.enigmaticlegacy.packets.clients.PacketFlameParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketHandleItemPickup;
import com.integral.enigmaticlegacy.packets.clients.PacketPlayerMotion;
import com.integral.enigmaticlegacy.packets.clients.PacketPlayerRotations;
import com.integral.enigmaticlegacy.packets.clients.PacketPlayerSetlook;
import com.integral.enigmaticlegacy.packets.clients.PacketPortalParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketRecallParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketSlotUnlocked;
import com.integral.enigmaticlegacy.packets.clients.PacketUpdateNotification;
import com.integral.enigmaticlegacy.packets.clients.PacketWitherParticles;
import com.integral.enigmaticlegacy.packets.server.PacketAnvilField;
import com.integral.enigmaticlegacy.packets.server.PacketConfirmTeleportation;
import com.integral.enigmaticlegacy.packets.server.PacketEnderRingKey;
import com.integral.enigmaticlegacy.packets.server.PacketSpellstoneKey;
import com.integral.enigmaticlegacy.packets.server.PacketXPScrollKey;
import com.integral.enigmaticlegacy.proxy.ClientProxy;
import com.integral.enigmaticlegacy.proxy.CommonProxy;
import com.integral.enigmaticlegacy.triggers.BeheadingTrigger;
import com.integral.enigmaticlegacy.triggers.UseUnholyGrailTrigger;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.crafting.IngredientNBT;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod("enigmaticlegacy")
public class EnigmaticLegacy {

	public static EnigmaticLegacy enigmaticLegacy;
	public static final Logger enigmaticLogger = LogManager.getLogger("Enigmatic Legacy");
	public static SimpleChannel packetInstance;
	
	public static final String MODID = "enigmaticlegacy";
	public static final String VERSION = "1.6.1";
	public static final String RELEASE_TYPE = "Release";
	public static final String NAME = "Enigmatic Legacy";
	
	public static final int howCoolAmI = Integer.MAX_VALUE;
	
	public static EnigmaticEventHandler enigmaticHandler;
	public static EnigmaticKeybindHandler keybindHandler;
	public static final OneSpecialHandler butImAsGuiltyAsThe = new OneSpecialHandler();
	public static List<String> damageTypesFire = new ArrayList<String>();
	public static List<AdvancedPotion> ultimatePotionTypes = new ArrayList<AdvancedPotion>();
	public static List<AdvancedPotion> commonPotionTypes = new ArrayList<AdvancedPotion>();
	public static SoundEvent HHON;
	public static SoundEvent HHOFF;
	public static SoundEvent SHIELD_TRIGGER;
	
	public static Item enigmaticItem;
	public static Item xpScroll;
	public static Item enigmaticAmulet;
	public static Item magnetRing;
	public static Item extradimensionalEye;
	public static Item relicOfTesting;
	public static Item recallPotion;
	public static Item forbiddenAxe;
	public static Item escapeScroll;
	public static Item heavenScroll;
	public static Item superMagnetRing;
	public static Item golemHeart;
	public static Item megaSponge;
	public static Item unholyGrail;
	public static Item eyeOfNebula;
	public static Item magmaHeart;
	public static Item voidPearl;
	public static Item oceanStone;
	public static Item angelBlessing;
	public static Item monsterCharm;
	public static Item miningCharm;
	public static Item enderRing;
	public static Item mendingMixture;
	public static Item lootGenerator;
	public static Item thiccScroll;
	public static Item ironRing;
	public static Item hastePotionDefault;
	public static Item hastePotionExtended;
	public static Item hastePotionEmpowered;
	public static Item hastePotionExtendedEmpowered;
	public static Item etheriumOre;
	public static Item etheriumIngot;
	public static Item ultimatePotionBase;
	public static Item ultimatePotionSplash;
	public static Item ultimatePotionLingering;
	public static Item commonPotionBase;
	public static Item commonPotionSplash;
	public static Item commonPotionLingering;
	
	public static Item etheriumHelmet;
	public static Item etheriumChestplate;
	public static Item etheriumLeggings;
	public static Item etheriumBoots;
	
	public static Item etheriumPickaxe;
	public static Item etheriumAxe;
	public static Item etheriumShovel;
	public static Item etheriumSword;
	public static Item etheriumScythe;
	
	public static Item astralDust;
	public static Item loreInscriber;
	public static Item loreFragment;
	public static Item enderRod;
	
	public static Item astralBreaker;
	public static Item oblivionStone;
	public static Item enchantmentTransposer;
	
	public static AdvancedPotion ULTIMATE_NIGHT_VISION;
	public static AdvancedPotion ULTIMATE_INVISIBILITY;
	public static AdvancedPotion ULTIMATE_LEAPING;
	public static AdvancedPotion ULTIMATE_FIRE_RESISTANCE;
	public static AdvancedPotion ULTIMATE_SWIFTNESS;
	public static AdvancedPotion ULTIMATE_SLOWNESS;
	public static AdvancedPotion ULTIMATE_TURTLE_MASTER;
	public static AdvancedPotion ULTIMATE_WATER_BREATHING;
	public static AdvancedPotion ULTIMATE_HEALING;
	public static AdvancedPotion ULTIMATE_HARMING;
	public static AdvancedPotion ULTIMATE_POISON;
	public static AdvancedPotion ULTIMATE_REGENERATION;
	public static AdvancedPotion ULTIMATE_STRENGTH;
	public static AdvancedPotion ULTIMATE_WEAKNESS;
	public static AdvancedPotion ULTIMATE_SLOW_FALLING;
	
	public static AdvancedPotion HASTE;
	public static AdvancedPotion LONG_HASTE;
	public static AdvancedPotion STRONG_HASTE;
	public static AdvancedPotion ULTIMATE_HASTE;
	
	public static AdvancedPotion EMPTY;
	
	public static AdvancedPotion testingPotion;
	
	public static ItemStack universalClock;
	
	private static final String PTC_VERSION = "1";
	
	public static final CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
	
	public EnigmaticLegacy() {
		enigmaticLogger.info("Constructing mod instance...");
		
		enigmaticLegacy = this;
		
		enigmaticHandler = new EnigmaticEventHandler();
		keybindHandler = new EnigmaticKeybindHandler();
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientRegistries);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::intermodStuff);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onLoadComplete);
		FMLJavaModLoadingContext.get().getModEventBus().register(new EnigmaticRecipeSerializers());
		
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
		
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(enigmaticHandler);
		MinecraftForge.EVENT_BUS.register(keybindHandler);
		MinecraftForge.EVENT_BUS.register(new EnigmaticUpdateHandler());
		
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON, "enigmatic-legacy-common.toml");
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT, "enigmatic-legacy-client.toml");
		
		enigmaticLogger.info("Mod instance constructed successfully.");
	}
	
	private void onLoadComplete(final FMLLoadCompleteEvent event) {
		
		enigmaticLogger.info("Initializing load completion phase...");
		
		enigmaticLogger.info("Initializing config values...");

		if (!ConfigHandler.CONFIG_VERSION.get().equals(ConfigHandler.CURRENT_VERSION)) {
			ConfigHandler.resetConfig();
			
			ConfigHandler.CONFIG_VERSION.set(ConfigHandler.CURRENT_VERSION);
			ConfigHandler.CONFIG_VERSION.save();
		}
		
		GolemHeart.initAttributes();
		
		enigmaticLogger.info("Registering brewing recipes...");
		
		if (ConfigHandler.RECALL_POTION_ENABLED.getValue())
			BrewingRecipeRegistry.addRecipe(new SpecialBrewingRecipe(IngredientNBT.fromStacks(PotionHelper.createVanillaPotion(Items.POTION, Potions.AWKWARD)), Ingredient.fromItems(Items.ENDER_EYE), new ItemStack(recallPotion)));
		
		if (ConfigHandler.COMMON_POTIONS_ENABLED.getValue())
			PotionHelper.registerCommonPotions();
		
		if (ConfigHandler.ULTIMATE_POTIONS_ENABLED.getValue()) {
			
			PotionHelper.registerBasicUltimatePotions();
			PotionHelper.registerSplashUltimatePotions();
			PotionHelper.registerLingeringUltimatePotions();
			
		}
		
		BrewingRecipeRegistry.addRecipe(new ValidationBrewingRecipe(Ingredient.fromItems(hastePotionExtendedEmpowered, recallPotion, ultimatePotionLingering, commonPotionLingering), null));
		
		EnigmaticUpdateHandler.init();

		proxy.loadComplete(event);
		
		universalClock = new ItemStack(Items.CLOCK);
		
		enigmaticLogger.info("Load completion phase finished successfully");
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		
		enigmaticLogger.info("Initializing common setup phase...");
		
		damageTypesFire.add(DamageSource.LAVA.damageType);
		damageTypesFire.add(DamageSource.IN_FIRE.damageType);
		damageTypesFire.add(DamageSource.ON_FIRE.damageType);
		
		enigmaticLogger.info("Registering packets...");
		packetInstance = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(EnigmaticLegacy.MODID, "main"))
                .networkProtocolVersion(() -> PTC_VERSION)
                .clientAcceptedVersions(PTC_VERSION::equals)
                .serverAcceptedVersions(PTC_VERSION::equals)
                .simpleChannel();
		
		packetInstance.registerMessage(0, PacketRecallParticles.class, PacketRecallParticles::encode, PacketRecallParticles::decode,
				PacketRecallParticles::handle);
		packetInstance.registerMessage(1, PacketEnderRingKey.class, PacketEnderRingKey::encode, PacketEnderRingKey::decode,
				PacketEnderRingKey::handle);
		packetInstance.registerMessage(2, PacketSpellstoneKey.class, PacketSpellstoneKey::encode, PacketSpellstoneKey::decode,
				PacketSpellstoneKey::handle);
		packetInstance.registerMessage(3, PacketPlayerMotion.class, PacketPlayerMotion::encode, PacketPlayerMotion::decode,
				PacketPlayerMotion::handle);
		packetInstance.registerMessage(4, PacketPlayerRotations.class, PacketPlayerRotations::encode, PacketPlayerRotations::decode,
				PacketPlayerRotations::handle);
		packetInstance.registerMessage(5, PacketPlayerSetlook.class, PacketPlayerSetlook::encode, PacketPlayerSetlook::decode,
				PacketPlayerSetlook::handle);
		
		packetInstance.registerMessage(7, PacketConfirmTeleportation.class, PacketConfirmTeleportation::encode, PacketConfirmTeleportation::decode,
				PacketConfirmTeleportation::handle);
		packetInstance.registerMessage(8, PacketPortalParticles.class, PacketPortalParticles::encode, PacketPortalParticles::decode,
				PacketPortalParticles::handle);
		packetInstance.registerMessage(9, PacketXPScrollKey.class, PacketXPScrollKey::encode, PacketXPScrollKey::decode,
				PacketXPScrollKey::handle);
		packetInstance.registerMessage(10, PacketSlotUnlocked.class, PacketSlotUnlocked::encode, PacketSlotUnlocked::decode,
				PacketSlotUnlocked::handle);
		packetInstance.registerMessage(11, PacketHandleItemPickup.class, PacketHandleItemPickup::encode, PacketHandleItemPickup::decode,
				PacketHandleItemPickup::handle);
		packetInstance.registerMessage(12, PacketUpdateNotification.class, PacketUpdateNotification::encode, PacketUpdateNotification::decode,
				PacketUpdateNotification::handle);
		packetInstance.registerMessage(13, PacketAnvilField.class, PacketAnvilField::encode, PacketAnvilField::decode,
				PacketAnvilField::handle);
		packetInstance.registerMessage(14, PacketWitherParticles.class, PacketWitherParticles::encode, PacketWitherParticles::decode,
				PacketWitherParticles::handle);
		packetInstance.registerMessage(15, PacketFlameParticles.class, PacketFlameParticles::encode, PacketFlameParticles::decode,
				PacketFlameParticles::handle);
		
		
		enigmaticLogger.info("Registering triggers...");
		CriteriaTriggers.register(UseUnholyGrailTrigger.INSTANCE);
		CriteriaTriggers.register(BeheadingTrigger.INSTANCE);
		
		enigmaticLogger.info("Common setup phase finished successfully.");
	}
	
	private void clientRegistries(final FMLClientSetupEvent event) {
		enigmaticLogger.info("Initializing client setup phase...");
		keybindHandler.registerKeybinds();
		
		enigmaticLogger.info("Client setup phase finished successfully.");
	}
	
	private void intermodStuff(final InterModEnqueueEvent event) {
		enigmaticLogger.info("Sending messages to Curios API...");
		SuperpositionHandler.registerCurioType("charm", 1, true, false, null);
		SuperpositionHandler.registerCurioType("ring", 2, false, false, null);
		SuperpositionHandler.registerCurioType("spellstone", 1, false, false, new ResourceLocation(EnigmaticLegacy.MODID, "textures/slots/spellstone_slot.png"));
		SuperpositionHandler.registerCurioType("scroll", 1, false, false, new ResourceLocation(EnigmaticLegacy.MODID, "textures/slots/scroll_slot.png"));
		
	}
	
	@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD, modid = EnigmaticLegacy.MODID)
	public static class RegistryEvents {
		
		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
			
			enigmaticLogger.info("Initializing items registration...");
			
			enigmaticItem = new EnigmaticItem(EnigmaticItem.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "enigmatic_item"));
			xpScroll = new XPScroll(XPScroll.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "xp_scroll"));
			enigmaticAmulet = new EnigmaticAmulet(EnigmaticAmulet.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "enigmatic_amulet"));
			magnetRing = new MagnetRing(MagnetRing.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "magnet_ring"));
			extradimensionalEye = new ExtradimensionalEye(ExtradimensionalEye.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "extradimensional_eye"));
			relicOfTesting = new RelicOfTesting(RelicOfTesting.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "relic_of_testing"));
			recallPotion = new RecallPotion(RecallPotion.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "recall_potion"));
			forbiddenAxe = new ForbiddenAxe(EnigmaticMaterials.FORBIDDENAXE, 6, -2.4F, ForbiddenAxe.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "forbidden_axe"));
			escapeScroll = new EscapeScroll(EscapeScroll.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "escape_scroll"));
			heavenScroll = new HeavenScroll(HeavenScroll.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "heaven_scroll"));
			superMagnetRing = new SuperMagnetRing(SuperMagnetRing.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "super_magnet_ring"));
			golemHeart = new GolemHeart(GolemHeart.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "golem_heart"));
			megaSponge = new Megasponge(Megasponge.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "mega_sponge"));
			unholyGrail = new UnholyGrail(UnholyGrail.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "unholy_grail"));
			eyeOfNebula = new EyeOfNebula(EyeOfNebula.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "eye_of_nebula"));
			magmaHeart = new MagmaHeart(MagmaHeart.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "magma_heart"));
			voidPearl = new VoidPearl(VoidPearl.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "void_pearl"));
			oceanStone = new OceanStone(OceanStone.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "ocean_stone"));
			angelBlessing = new AngelBlessing(AngelBlessing.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "angel_blessing"));
			monsterCharm = new MonsterCharm(MonsterCharm.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "monster_charm"));
			miningCharm = new MiningCharm(MiningCharm.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "mining_charm"));
			enderRing = new EnderRing(EnderRing.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "ender_ring"));
			mendingMixture = new MendingMixture(MendingMixture.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "mending_mixture"));
			lootGenerator = new LootGenerator(LootGenerator.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "loot_generator"));
			thiccScroll = new ThiccScroll(ThiccScroll.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "thicc_scroll"));
			ironRing = new IronRing(IronRing.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "iron_ring"));
			hastePotionDefault = new HastePotion(HastePotion.setupIntegratedProperties(Rarity.COMMON), 3600, 0).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "haste_potion_default"));
			hastePotionExtended = new HastePotion(HastePotion.setupIntegratedProperties(Rarity.COMMON), 9600, 0).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "haste_potion_extended"));
			hastePotionEmpowered = new HastePotion(HastePotion.setupIntegratedProperties(Rarity.COMMON), 1800, 1).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "haste_potion_empowered"));
			hastePotionExtendedEmpowered = new HastePotion(HastePotion.setupIntegratedProperties(Rarity.RARE), 4800, 1).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "haste_potion_extended_empowered"));
			etheriumOre = new EtheriumOre(EtheriumOre.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "etherium_ore"));
			etheriumIngot = new EtheriumIngot(EtheriumIngot.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "etherium_ingot"));
			
			commonPotionBase = new UltimatePotionBase(UltimatePotionBase.setupIntegratedProperties(Rarity.COMMON), true).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "common_potion"));
			commonPotionSplash = new UltimatePotionSplash(UltimatePotionSplash.setupIntegratedProperties(Rarity.COMMON), true).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "common_potion_splash"));
			commonPotionLingering = new UltimatePotionLingering(UltimatePotionLingering.setupIntegratedProperties(Rarity.COMMON), true).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "common_potion_lingering"));
			
			ultimatePotionBase = new UltimatePotionBase(UltimatePotionBase.setupIntegratedProperties(Rarity.RARE), false).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "ultimate_potion"));
			ultimatePotionSplash = new UltimatePotionSplash(UltimatePotionSplash.setupIntegratedProperties(Rarity.RARE), false).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "ultimate_potion_splash"));
			ultimatePotionLingering = new UltimatePotionLingering(UltimatePotionLingering.setupIntegratedProperties(Rarity.RARE), false).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "ultimate_potion_lingering"));

			etheriumHelmet = new EtheriumArmor(EnigmaticArmorMaterials.ETHERIUM, EquipmentSlotType.HEAD, EtheriumArmor.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "etherium_helmet"));
			etheriumChestplate = new EtheriumArmor(EnigmaticArmorMaterials.ETHERIUM, EquipmentSlotType.CHEST, EtheriumArmor.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "etherium_chestplate"));
			etheriumLeggings = new EtheriumArmor(EnigmaticArmorMaterials.ETHERIUM, EquipmentSlotType.LEGS, EtheriumArmor.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "etherium_leggings"));
			etheriumBoots = new EtheriumArmor(EnigmaticArmorMaterials.ETHERIUM, EquipmentSlotType.FEET, EtheriumArmor.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "etherium_boots"));

			etheriumPickaxe = new EtheriumPickaxe(EnigmaticMaterials.ETHERIUM, p -> EtheriumPickaxe.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "etherium_pickaxe"));
			etheriumAxe = new EtheriumAxe(EnigmaticMaterials.ETHERIUM, EtheriumAxe.setupIntegratedProperties(0), -3.2F, 10).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "etherium_axe"));
			etheriumShovel = new EtheriumShovel(EnigmaticMaterials.ETHERIUM, p -> EtheriumShovel.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "etherium_shovel"));
			etheriumSword = new EtheriumSword(EtheriumSword.setupIntegratedProperties(), EnigmaticMaterials.ETHERIUM, -2.6F, 6).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "etherium_sword"));
			etheriumScythe = new EtheriumScythe(EtheriumScythe.setupIntegratedProperties(), EnigmaticMaterials.ETHERIUM, -2.0F, 3).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "etherium_scythe"));
			
			astralDust = new AstralDust(AstralDust.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "astral_dust"));
			loreInscriber = new LoreInscriber(LoreInscriber.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "lore_inscriber"));
			loreFragment = new LoreFragment(LoreFragment.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "lore_fragment"));
			enderRod = new EnderRod(EnderRod.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "ender_rod"));
			
			astralBreaker = new AstralBreaker(EnigmaticMaterials.ETHERIUM, p -> AstralBreaker.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "astral_breaker"));
			oblivionStone = new OblivionStone(OblivionStone.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "oblivion_stone"));
			enchantmentTransposer = new EnchantmentTransposer(EnchantmentTransposer.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "enchantment_transposer"));
			
			event.getRegistry().registerAll(
					enigmaticItem,
					golemHeart,
					angelBlessing,
					oceanStone,
					magmaHeart,
					eyeOfNebula,
					voidPearl,
					ironRing,
					enigmaticAmulet,
					thiccScroll,
					xpScroll,
					escapeScroll,
					heavenScroll,
					magnetRing,
					superMagnetRing,
					enderRing,
					monsterCharm,
					miningCharm,
					megaSponge,
					extradimensionalEye,
					forbiddenAxe,
					unholyGrail,
					recallPotion,
					mendingMixture,
					lootGenerator,
					hastePotionDefault,
					hastePotionExtended,
					hastePotionEmpowered,
					hastePotionExtendedEmpowered,
					relicOfTesting,
					etheriumOre,
					etheriumIngot,
					commonPotionBase,
					commonPotionSplash,
					commonPotionLingering,
					ultimatePotionBase,
					ultimatePotionSplash,
					ultimatePotionLingering,
					etheriumHelmet,
					etheriumChestplate,
					etheriumLeggings,
					etheriumBoots,
					etheriumPickaxe,
					etheriumAxe,
					etheriumShovel,
					etheriumSword,
					etheriumScythe,
					astralBreaker,
					astralDust,
					enderRod,
					loreInscriber,
					loreFragment,
					oblivionStone,
					enchantmentTransposer
			);
			
			enigmaticLogger.info("Items registered successfully.");
		}
		
		@SubscribeEvent
		public static void registerSounds(final RegistryEvent.Register<SoundEvent> event) {
			enigmaticLogger.info("Initializing sounds registration...");
			
			HHON = SuperpositionHandler.registerSound("misc.hhon");
			HHOFF = SuperpositionHandler.registerSound("misc.hhoff");
			SHIELD_TRIGGER = SuperpositionHandler.registerSound("misc.shield_trigger");
			
			enigmaticLogger.info("Sounds registered successfully.");
		}
		
		@SubscribeEvent
        public static void onRecipeRegister(final RegistryEvent.Register<IRecipeSerializer<?>> e) {
			
        }
		
		@SubscribeEvent
		public static void registerBrewing(RegistryEvent.Register<Potion> event) {
			
			enigmaticLogger.info("Initializing advanced potion system...");

			ULTIMATE_NIGHT_VISION = new AdvancedPotion("ultimate_night_vision", new EffectInstance(Effects.NIGHT_VISION, 19200));
			ULTIMATE_INVISIBILITY = new AdvancedPotion("ultimate_invisibility", new EffectInstance(Effects.INVISIBILITY, 19200));
			ULTIMATE_LEAPING = new AdvancedPotion("ultimate_leaping", new EffectInstance(Effects.JUMP_BOOST, 9600, 1));
			ULTIMATE_FIRE_RESISTANCE = new AdvancedPotion("ultimate_fire_resistance", new EffectInstance(Effects.FIRE_RESISTANCE, 19200));
			ULTIMATE_SWIFTNESS = new AdvancedPotion("ultimate_swiftness", new EffectInstance(Effects.SPEED, 9600, 1));
			ULTIMATE_SLOWNESS = new AdvancedPotion("ultimate_slowness", new EffectInstance(Effects.SLOWNESS, 1200, 3));
			ULTIMATE_TURTLE_MASTER = new AdvancedPotion("ultimate_turtle_master", new EffectInstance(Effects.SLOWNESS, 800, 5), new EffectInstance(Effects.RESISTANCE, 800, 3));
			ULTIMATE_WATER_BREATHING =  new AdvancedPotion("ultimate_water_breathing", new EffectInstance(Effects.WATER_BREATHING, 19200));
			ULTIMATE_HEALING = new AdvancedPotion("ultimate_healing", new EffectInstance(Effects.INSTANT_HEALTH, 1, 2));
			ULTIMATE_HARMING = new AdvancedPotion("ultimate_harming", new EffectInstance(Effects.INSTANT_DAMAGE, 1, 2));
			ULTIMATE_POISON = new AdvancedPotion("ultimate_poison", new EffectInstance(Effects.POISON, 1800, 1));
			ULTIMATE_REGENERATION = new AdvancedPotion("ultimate_regeneration", new EffectInstance(Effects.REGENERATION, 1800, 1));
			ULTIMATE_STRENGTH = new AdvancedPotion("ultimate_strength", new EffectInstance(Effects.STRENGTH, 9600, 1));
			ULTIMATE_WEAKNESS = new AdvancedPotion("ultimate_weakness", new EffectInstance(Effects.WEAKNESS, 9600));
			ULTIMATE_SLOW_FALLING = new AdvancedPotion("ultimate_slow_falling", new EffectInstance(Effects.SLOW_FALLING, 9600));
	
			HASTE = new AdvancedPotion("haste", new EffectInstance(Effects.HASTE, 3600));
			LONG_HASTE = new AdvancedPotion("long_haste", new EffectInstance(Effects.HASTE, 9600));
			STRONG_HASTE = new AdvancedPotion("strong_haste", new EffectInstance(Effects.HASTE, 1800, 1));
			ULTIMATE_HASTE = new AdvancedPotion("ultimate_haste", new EffectInstance(Effects.HASTE, 9600, 1));
			
			EMPTY = new AdvancedPotion("empty");
			
			ultimatePotionTypes.add(ULTIMATE_NIGHT_VISION);
			ultimatePotionTypes.add(ULTIMATE_INVISIBILITY);
			ultimatePotionTypes.add(ULTIMATE_LEAPING);
			ultimatePotionTypes.add(ULTIMATE_FIRE_RESISTANCE);
			ultimatePotionTypes.add(ULTIMATE_SWIFTNESS);
			ultimatePotionTypes.add(ULTIMATE_SLOWNESS);
			ultimatePotionTypes.add(ULTIMATE_TURTLE_MASTER);
			ultimatePotionTypes.add(ULTIMATE_WATER_BREATHING);
			ultimatePotionTypes.add(ULTIMATE_HEALING);
			ultimatePotionTypes.add(ULTIMATE_HARMING);
			ultimatePotionTypes.add(ULTIMATE_POISON);
			ultimatePotionTypes.add(ULTIMATE_REGENERATION);
			ultimatePotionTypes.add(ULTIMATE_STRENGTH);
			ultimatePotionTypes.add(ULTIMATE_WEAKNESS);
			ultimatePotionTypes.add(ULTIMATE_SLOW_FALLING);
			
			commonPotionTypes.add(HASTE);
			commonPotionTypes.add(LONG_HASTE);
			commonPotionTypes.add(STRONG_HASTE);
			ultimatePotionTypes.add(ULTIMATE_HASTE);
			
			enigmaticLogger.info("Advanced potion system initialized successfully.");
		}
		
		@SubscribeEvent
    	public static void onEntitiesRegistry(final RegistryEvent.Register<EntityType<?>> event) {
			enigmaticLogger.info("Initializing entities registration...");
			
			event.getRegistry().register(EntityType.Builder.<PermanentItemEntity>create(PermanentItemEntity::new, EntityClassification.MISC)
					.size(0.25F, 0.25F)
					.setTrackingRange(64)
					.setCustomClientFactory((spawnEntity,world) -> new PermanentItemEntity(PermanentItemEntity.TYPE, world))
					.setUpdateInterval(2)
					.setShouldReceiveVelocityUpdates(true)
					.build("")
					.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "permanent_item_entity")));
			
			event.getRegistry().register(EntityType.Builder.<EnigmaticPotionEntity>create(EnigmaticPotionEntity::new, EntityClassification.MISC)
					.size(0.25F, 0.25F)
					.setTrackingRange(64)
					.setCustomClientFactory((spawnEntity,world) -> new EnigmaticPotionEntity(EnigmaticPotionEntity.TYPE, world))
					.setUpdateInterval(10)
					.setShouldReceiveVelocityUpdates(true)
					.build("")
					.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "enigmatic_potion_entity")));
			
			event.getRegistry().register(EntityType.Builder.<UltimateWitherSkullEntity>create(UltimateWitherSkullEntity::new, EntityClassification.MISC)
					.size(0.25F, 0.25F)
					.setTrackingRange(64)
					.setCustomClientFactory((spawnEntity, world) -> new UltimateWitherSkullEntity(UltimateWitherSkullEntity.TYPE, world))
					//.setUpdateInterval(1)
					//.setShouldReceiveVelocityUpdates(true)
					.build("")
					.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "ultimate_wither_skull_entity")));
    		
			enigmaticLogger.info("Entities registered successfully.");
    	}
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onColorInit(net.minecraftforge.client.event.ColorHandlerEvent.Item event) {
		enigmaticLogger.info("Initializing colors registration...");
		
		event.getItemColors().register((stack, color) -> {
			 if (PotionHelper.isAdvancedPotion(stack))
				 return color > 0 ? -1 : PotionHelper.getColor(stack);
				 
	         return color > 0 ? -1 : PotionUtils.getColor(stack);
	      }, EnigmaticLegacy.ultimatePotionBase, EnigmaticLegacy.ultimatePotionSplash, EnigmaticLegacy.ultimatePotionLingering, EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.commonPotionSplash, EnigmaticLegacy.commonPotionLingering);
		
		enigmaticLogger.info("Colors registered successfully.");
	}
	
	 public static final ItemGroup enigmaticTab = new ItemGroup("enigmaticCreativeTab") {
	      @OnlyIn(Dist.CLIENT)
	      public ItemStack createIcon() {
	         return new ItemStack(EnigmaticLegacy.enigmaticItem);
	      }
	   };
	   
	   
	 public static final ItemGroup enigmaticPotionTab = new ItemGroup("enigmaticPotionCreativeTab") {
		 @OnlyIn(Dist.CLIENT)
	      public ItemStack createIcon() {
	         return new ItemStack(EnigmaticLegacy.recallPotion);
	      }
	 };
	   
	   
	
	
}
