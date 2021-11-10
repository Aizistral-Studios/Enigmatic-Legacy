package com.integral.enigmaticlegacy;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.electronwill.nightconfig.core.file.FileWatcher;
import com.google.common.collect.Lists;
import com.integral.enigmaticlegacy.api.generic.ConfigurableItem;
import com.integral.enigmaticlegacy.api.items.IAdvancedPotionItem.PotionType;
import com.integral.enigmaticlegacy.api.materials.EnigmaticArmorMaterials;
import com.integral.enigmaticlegacy.api.materials.EnigmaticMaterials;
import com.integral.enigmaticlegacy.blocks.BlockBigLamp;
import com.integral.enigmaticlegacy.blocks.BlockMassiveLamp;
import com.integral.enigmaticlegacy.brewing.SpecialBrewingRecipe;
import com.integral.enigmaticlegacy.brewing.ValidationBrewingRecipe;
import com.integral.enigmaticlegacy.config.EtheriumConfigHandler;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.crafting.EnigmaticRecipeSerializers;
import com.integral.enigmaticlegacy.enchantments.CeaselessEnchantment;
import com.integral.enigmaticlegacy.enchantments.NemesisCurseEnchantment;
import com.integral.enigmaticlegacy.enchantments.SharpshooterEnchantment;
import com.integral.enigmaticlegacy.enchantments.SlayerEnchantment;
import com.integral.enigmaticlegacy.enchantments.TorrentEnchantment;
import com.integral.enigmaticlegacy.enchantments.WrathEnchantment;
import com.integral.enigmaticlegacy.entities.EnigmaticPotionEntity;
import com.integral.enigmaticlegacy.entities.PermanentItemEntity;
import com.integral.enigmaticlegacy.entities.UltimateWitherSkullEntity;
import com.integral.enigmaticlegacy.gui.containers.LoreInscriberContainer;
import com.integral.enigmaticlegacy.gui.containers.LoreInscriberScreen;
import com.integral.enigmaticlegacy.gui.containers.PortableCrafterContainer;
import com.integral.enigmaticlegacy.handlers.EnigmaticEventHandler;
import com.integral.enigmaticlegacy.handlers.EnigmaticKeybindHandler;
import com.integral.enigmaticlegacy.handlers.EnigmaticUpdateHandler;
import com.integral.enigmaticlegacy.handlers.OneSpecialHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.PotionHelper;
import com.integral.enigmaticlegacy.items.AngelBlessing;
import com.integral.enigmaticlegacy.items.AnimalGuide;
import com.integral.enigmaticlegacy.items.AntiforbiddenPotion;
import com.integral.enigmaticlegacy.items.AstralBreaker;
import com.integral.enigmaticlegacy.items.AstralDust;
import com.integral.enigmaticlegacy.items.AvariceScroll;
import com.integral.enigmaticlegacy.items.BerserkEmblem;
import com.integral.enigmaticlegacy.items.CursedRing;
import com.integral.enigmaticlegacy.items.CursedScroll;
import com.integral.enigmaticlegacy.items.CursedStone;
import com.integral.enigmaticlegacy.items.DarkArmor;
import com.integral.enigmaticlegacy.items.DarkMirror;
import com.integral.enigmaticlegacy.items.DarkestScroll;
import com.integral.enigmaticlegacy.items.EarthHeart;
import com.integral.enigmaticlegacy.items.EnchanterPearl;
import com.integral.enigmaticlegacy.items.EnchantmentTransposer;
import com.integral.enigmaticlegacy.items.EnderRing;
import com.integral.enigmaticlegacy.items.EnigmaticAmulet;
import com.integral.enigmaticlegacy.items.EnigmaticItem;
import com.integral.enigmaticlegacy.items.EscapeScroll;
import com.integral.enigmaticlegacy.items.EvilEssence;
import com.integral.enigmaticlegacy.items.ExtradimensionalEye;
import com.integral.enigmaticlegacy.items.EyeOfNebula;
import com.integral.enigmaticlegacy.items.FabulousScroll;
import com.integral.enigmaticlegacy.items.ForbiddenAxe;
import com.integral.enigmaticlegacy.items.ForbiddenFruit;
import com.integral.enigmaticlegacy.items.GemOfBinding;
import com.integral.enigmaticlegacy.items.GemRing;
import com.integral.enigmaticlegacy.items.GolemHeart;
import com.integral.enigmaticlegacy.items.GuardianHeart;
import com.integral.enigmaticlegacy.items.HastePotion;
import com.integral.enigmaticlegacy.items.HeavenScroll;
import com.integral.enigmaticlegacy.items.HunterGuide;
import com.integral.enigmaticlegacy.items.Infinimeal;
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
import com.integral.enigmaticlegacy.items.PlaceholderItem;
import com.integral.enigmaticlegacy.items.RecallPotion;
import com.integral.enigmaticlegacy.items.RelicOfTesting;
import com.integral.enigmaticlegacy.items.RevelationTome;
import com.integral.enigmaticlegacy.items.SoulCrystal;
import com.integral.enigmaticlegacy.items.StorageCrystal;
import com.integral.enigmaticlegacy.items.SuperMagnetRing;
import com.integral.enigmaticlegacy.items.TheAcknowledgment;
import com.integral.enigmaticlegacy.items.TheTwist;
import com.integral.enigmaticlegacy.items.ThiccScroll;
import com.integral.enigmaticlegacy.items.TwistedCore;
import com.integral.enigmaticlegacy.items.UltimatePotionBase;
import com.integral.enigmaticlegacy.items.UltimatePotionLingering;
import com.integral.enigmaticlegacy.items.UltimatePotionSplash;
import com.integral.enigmaticlegacy.items.UnholyGrail;
import com.integral.enigmaticlegacy.items.VoidPearl;
import com.integral.enigmaticlegacy.items.WormholePotion;
import com.integral.enigmaticlegacy.items.XPScroll;
import com.integral.enigmaticlegacy.items.generic.GenericBlockItem;
import com.integral.enigmaticlegacy.objects.AdvancedPotion;
import com.integral.enigmaticlegacy.objects.LoggerWrapper;
import com.integral.enigmaticlegacy.objects.RegisteredMeleeAttack;
import com.integral.enigmaticlegacy.packets.clients.PacketFlameParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketForceArrowRotations;
import com.integral.enigmaticlegacy.packets.clients.PacketGenericParticleEffect;
import com.integral.enigmaticlegacy.packets.clients.PacketHandleItemPickup;
import com.integral.enigmaticlegacy.packets.clients.PacketPlayerMotion;
import com.integral.enigmaticlegacy.packets.clients.PacketPlayerRotations;
import com.integral.enigmaticlegacy.packets.clients.PacketPlayerSetlook;
import com.integral.enigmaticlegacy.packets.clients.PacketPortalParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketRecallParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketSetEntryState;
import com.integral.enigmaticlegacy.packets.clients.PacketSlotUnlocked;
import com.integral.enigmaticlegacy.packets.clients.PacketSyncTransientData;
import com.integral.enigmaticlegacy.packets.clients.PacketUpdateExperience;
import com.integral.enigmaticlegacy.packets.clients.PacketUpdateNotification;
import com.integral.enigmaticlegacy.packets.clients.PacketWitherParticles;
import com.integral.enigmaticlegacy.packets.server.PacketAnvilField;
import com.integral.enigmaticlegacy.packets.server.PacketConfirmTeleportation;
import com.integral.enigmaticlegacy.packets.server.PacketEnderRingKey;
import com.integral.enigmaticlegacy.packets.server.PacketInkwellField;
import com.integral.enigmaticlegacy.packets.server.PacketSpellstoneKey;
import com.integral.enigmaticlegacy.packets.server.PacketXPScrollKey;
import com.integral.enigmaticlegacy.proxy.ClientProxy;
import com.integral.enigmaticlegacy.proxy.CommonProxy;
import com.integral.enigmaticlegacy.triggers.BeheadingTrigger;
import com.integral.enigmaticlegacy.triggers.CursedRingEquippedTrigger;
import com.integral.enigmaticlegacy.triggers.RevelationGainTrigger;
import com.integral.enigmaticlegacy.triggers.RevelationTomeBurntTrigger;
import com.integral.enigmaticlegacy.triggers.UseUnholyGrailTrigger;
import com.integral.etherium.core.EtheriumEventHandler;
import com.integral.etherium.items.EnderRod;
import com.integral.etherium.items.EtheriumArmor;
import com.integral.etherium.items.EtheriumAxe;
import com.integral.etherium.items.EtheriumIngot;
import com.integral.etherium.items.EtheriumOre;
import com.integral.etherium.items.EtheriumPickaxe;
import com.integral.etherium.items.EtheriumScythe;
import com.integral.etherium.items.EtheriumShovel;
import com.integral.etherium.items.EtheriumSword;
import com.integral.omniconfig.packets.PacketSyncOptions;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.inventory.CraftingScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import net.minecraft.block.AbstractBlock;

@Mod(EnigmaticLegacy.MODID)
public class EnigmaticLegacy {
	public static final String MODID = "enigmaticlegacy";
	public static final String VERSION = "2.11.5";
	public static final String RELEASE_TYPE = "Release";
	public static final String NAME = "Enigmatic Legacy";

	public static EnigmaticLegacy enigmaticLegacy;
	public static final LoggerWrapper logger = new LoggerWrapper("Enigmatic Legacy");
	public static SimpleChannel packetInstance;

	public static final int howCoolAmI = Integer.MAX_VALUE;

	public static EnigmaticEventHandler enigmaticHandler;
	public static EnigmaticKeybindHandler keybindHandler;
	public static final OneSpecialHandler butImAsGuiltyAsThe = new OneSpecialHandler();
	public static List<String> damageTypesFire = new ArrayList<String>();
	public static List<AdvancedPotion> ultimatePotionTypes = new ArrayList<AdvancedPotion>();
	public static List<AdvancedPotion> commonPotionTypes = new ArrayList<AdvancedPotion>();
	public static List<Block> cutoutBlockRegistry = new ArrayList<Block>();
	public static SoundEvent HHON;
	public static SoundEvent HHOFF;
	public static SoundEvent SHIELD_TRIGGER;
	public static SoundEvent DEFLECT;
	public static SoundEvent WRITE;
	public static SoundEvent LEARN;

	public static BlockMassiveLamp massiveLamp;
	public static BlockBigLamp bigLamp;
	public static BlockMassiveLamp massiveShroomlamp;
	public static BlockBigLamp bigShroomlamp;
	public static BlockMassiveLamp massiveRedstonelamp;
	public static BlockBigLamp bigRedstonelamp;

	@ConfigurableItem("") public static EnigmaticItem enigmaticItem;
	@ConfigurableItem("Scroll of Ageless Wisdom") public static XPScroll xpScroll;
	@ConfigurableItem("Enigmatic Amulet") public static EnigmaticAmulet enigmaticAmulet;
	@ConfigurableItem("Magnet Ring") public static MagnetRing magnetRing;
	@ConfigurableItem("Extradimensional Eye") public static ExtradimensionalEye extradimensionalEye;
	@ConfigurableItem("") public static RelicOfTesting relicOfTesting;
	@ConfigurableItem("Potion of Recall") public static RecallPotion recallPotion;
	@ConfigurableItem("Axe of Executioner") public static ForbiddenAxe forbiddenAxe;
	@ConfigurableItem("Scroll of Postmortal Recall") public static EscapeScroll escapeScroll;
	@ConfigurableItem("Gift of the Heaven") public static HeavenScroll heavenScroll;
	@ConfigurableItem("Ring of Dislocation") public static SuperMagnetRing superMagnetRing;
	@ConfigurableItem("Heart of the Golem") public static GolemHeart golemHeart;
	@ConfigurableItem("Megasponge") public static Megasponge megaSponge;
	@ConfigurableItem("Unholy Grail") public static UnholyGrail unholyGrail;
	@ConfigurableItem("Eye of Nebula") public static EyeOfNebula eyeOfNebula;
	@ConfigurableItem("Blazing Core") public static MagmaHeart magmaHeart;
	@ConfigurableItem("Pearl of the Void") public static VoidPearl voidPearl;
	@ConfigurableItem("Will of the Ocean") public static OceanStone oceanStone;
	@ConfigurableItem("Angel's Blessing") public static AngelBlessing angelBlessing;
	@ConfigurableItem("Emblem of Monster Slayer") public static MonsterCharm monsterCharm;
	@ConfigurableItem("Charm of Treasure Hunter") public static MiningCharm miningCharm;
	@ConfigurableItem("Ring of Ender") public static EnderRing enderRing;
	@ConfigurableItem("Mending Mixture") public static MendingMixture mendingMixture;
	@ConfigurableItem("") public static LootGenerator lootGenerator;
	@ConfigurableItem("Blank Scroll") public static ThiccScroll thiccScroll;
	@ConfigurableItem("Iron Ring") public static IronRing ironRing;
	@ConfigurableItem("") public static HastePotion hastePotionDefault;
	@ConfigurableItem("") public static HastePotion hastePotionExtended;
	@ConfigurableItem("") public static HastePotion hastePotionEmpowered;
	@ConfigurableItem("") public static HastePotion hastePotionExtendedEmpowered;
	@ConfigurableItem("Etherium Ore") public static EtheriumOre etheriumOre;
	@ConfigurableItem("Etherium Ingot") public static EtheriumIngot etheriumIngot;
	@ConfigurableItem("Ultimate Potions") public static UltimatePotionBase ultimatePotionBase;
	@ConfigurableItem("Ultimate Potions") public static UltimatePotionSplash ultimatePotionSplash;
	@ConfigurableItem("Ultimate Potions") public static UltimatePotionLingering ultimatePotionLingering;
	@ConfigurableItem("Common Potions") public static UltimatePotionBase commonPotionBase;
	@ConfigurableItem("Common Potions") public static UltimatePotionSplash commonPotionSplash;
	@ConfigurableItem("Common Potions") public static UltimatePotionLingering commonPotionLingering;

	@ConfigurableItem("Etherium Armor") public static EtheriumArmor etheriumHelmet;
	@ConfigurableItem("Etherium Armor") public static EtheriumArmor etheriumChestplate;
	@ConfigurableItem("Etherium Armor") public static EtheriumArmor etheriumLeggings;
	@ConfigurableItem("Etherium Armor") public static EtheriumArmor etheriumBoots;

	@ConfigurableItem("Etherium Pickaxe") public static EtheriumPickaxe etheriumPickaxe;
	@ConfigurableItem("Etherium Warxe") public static EtheriumAxe etheriumAxe;
	@ConfigurableItem("Etherium Shovel") public static EtheriumShovel etheriumShovel;
	@ConfigurableItem("Etherium Broadsword") public static EtheriumSword etheriumSword;
	@ConfigurableItem("Etherium Scythe") public static EtheriumScythe etheriumScythe;

	@ConfigurableItem("Astral Dust") public static AstralDust astralDust;
	@ConfigurableItem("The Architect's Inkwell") public static LoreInscriber loreInscriber;
	@ConfigurableItem("Lore Fragment") public static LoreFragment loreFragment;
	@ConfigurableItem("Ender Rod") public static EnderRod enderRod;

	@ConfigurableItem("Astral Breaker") public static AstralBreaker astralBreaker;
	@ConfigurableItem("Keystone of The Oblivion") public static OblivionStone oblivionStone;
	@ConfigurableItem("Tome of Hungering Knowledge") public static EnchantmentTransposer enchantmentTransposer;

	@ConfigurableItem("Gem of Binding") public static GemOfBinding gemOfBinding;
	@ConfigurableItem("Wormhole Potion") public static WormholePotion wormholePotion;
	@ConfigurableItem("Grace of the Creator") public static FabulousScroll fabulousScroll;
	@ConfigurableItem("") public static StorageCrystal storageCrystal;
	@ConfigurableItem("") public static SoulCrystal soulCrystal;

	@ConfigurableItem("The Acknowledgment") public static TheAcknowledgment theAcknowledgment;
	@ConfigurableItem("Tattered Tome") public static RevelationTome overworldRevelationTome;
	@ConfigurableItem("Withered Tome") public static RevelationTome netherRevelationTome;
	@ConfigurableItem("Corrupted Tome") public static RevelationTome endRevelationTome;

	@ConfigurableItem("Dark Armor") public static DarkArmor darkHelmet;
	@ConfigurableItem("Dark Armor") public static DarkArmor darkChestplate;
	@ConfigurableItem("Dark Armor") public static DarkArmor darkLeggings;
	@ConfigurableItem("Dark Armor") public static DarkArmor darkBoots;

	@ConfigurableItem("Ring of the Seven Curses") public static CursedRing cursedRing;
	@ConfigurableItem("Twisted Mirror") public static DarkMirror darkMirror;

	@ConfigurableItem("Crying Netherite Ingot") public static PlaceholderItem cryingIngot;
	@ConfigurableItem("Crying Netherite Armor") public static PlaceholderItem cryingHelmet;
	@ConfigurableItem("Crying Netherite Armor") public static PlaceholderItem cryingChestplate;
	@ConfigurableItem("Crying Netherite Armor") public static PlaceholderItem cryingLeggings;
	@ConfigurableItem("Crying Netherite Armor") public static PlaceholderItem cryingBoots;
	@ConfigurableItem("Crying Netherite Pickaxe") public static PlaceholderItem cryingPickaxe;
	@ConfigurableItem("Crying Netherite Axe") public static PlaceholderItem cryingAxe;
	@ConfigurableItem("Crying Netherite Sword") public static PlaceholderItem cryingSword;
	@ConfigurableItem("Crying Netherite Shovel") public static PlaceholderItem cryingShovel;
	@ConfigurableItem("Crying Netherite Hoe") public static PlaceholderItem cryingHoe;

	@ConfigurableItem("Scroll of a Thousand Curses") public static CursedScroll cursedScroll;
	@ConfigurableItem("Emblem of Bloodstained Valor") public static BerserkEmblem berserkEmblem;
	@ConfigurableItem("Heart of the Earth") public static EarthHeart earthHeart;
	@ConfigurableItem("Twisted Heart") public static TwistedCore twistedCore;

	@ConfigurableItem("Heart of the Guardian") public static GuardianHeart guardianHeart;
	@ConfigurableItem("The Twist") public static TheTwist theTwist;
	@ConfigurableItem("Nefarious Essence") public static EvilEssence evilEssence;
	@ConfigurableItem("Guite to Animal Companionship") public static AnimalGuide animalGuide;
	@ConfigurableItem("Guide to Feral Hunt") public static HunterGuide hunterGuide;
	@ConfigurableItem("Forbidden Fruit") public static ForbiddenFruit forbiddenFruit;
	@ConfigurableItem("") public static AntiforbiddenPotion antiforbiddenPotion;

	@ConfigurableItem("Exquisite Ring") public static GemRing gemRing;
	@ConfigurableItem("Unholy Stone") public static CursedStone cursedStone;
	@ConfigurableItem("Enchanter's Pearl") public static EnchanterPearl enchanterPearl;
	@ConfigurableItem("Fruit of Ascension") public static PlaceholderItem trueNotchApple;
	@ConfigurableItem("Pact of Infinite Avarice") public static AvariceScroll avariceScroll;
	@ConfigurableItem("Essence of Raging Life") public static Infinimeal infinimeal;
	@ConfigurableItem("Darkest Scroll") public static DarkestScroll darkestScroll;

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

	public static SharpshooterEnchantment sharpshooterEnchantment;
	public static CeaselessEnchantment ceaselessEnchantment;
	public static NemesisCurseEnchantment nemesisCurseEnchantment;
	public static TorrentEnchantment torrentEnchantment;
	public static WrathEnchantment wrathEnchantment;
	public static SlayerEnchantment slayerEnchantment;

	public static ItemStack universalClock;
	public static UUID soulOfTheArchitect;

	public static List<Item> spellstoneList;

	public static final ContainerType<PortableCrafterContainer> PORTABLE_CRAFTER = new ContainerType<>((syncId, playerInv) -> new PortableCrafterContainer(syncId, playerInv, IWorldPosCallable.of(playerInv.player.world, playerInv.player.getPosition())));

	@ObjectHolder(MODID + ":enigmatic_repair_container")
	public static final ContainerType<LoreInscriberContainer> LORE_INSCRIBER_CONTAINER = null;

	private static final String PTC_VERSION = "1";

	public static final CommonProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
	public static final List<Triple<LootTable, LootPool, Exception>> exceptionList = new ArrayList<>();

	@SuppressWarnings("deprecation")
	public EnigmaticLegacy() {
		logger.info("Constructing mod instance...");

		enigmaticLegacy = this;

		OmniconfigHandler.initialize();
		EtheriumConfigHandler etheriumConfig = new EtheriumConfigHandler();

		EnigmaticMaterials.setEtheriumConfig(etheriumConfig);
		EnigmaticArmorMaterials.setEtheriumConfig(etheriumConfig);

		enigmaticHandler = new EnigmaticEventHandler();
		keybindHandler = new EnigmaticKeybindHandler();

		massiveLamp = new BlockMassiveLamp(AbstractBlock.Properties.from(Blocks.LANTERN), "massive_lamp");
		bigLamp = new BlockBigLamp(AbstractBlock.Properties.from(Blocks.LANTERN), "big_lamp");
		massiveShroomlamp = new BlockMassiveLamp(AbstractBlock.Properties.from(Blocks.LANTERN), "massive_shroomlamp");
		bigShroomlamp = new BlockBigLamp(AbstractBlock.Properties.from(Blocks.LANTERN), "big_shroomlamp");
		massiveRedstonelamp = new BlockMassiveLamp(AbstractBlock.Properties.from(Blocks.LANTERN), "massive_redstonelamp");
		bigRedstonelamp = new BlockBigLamp(AbstractBlock.Properties.from(Blocks.LANTERN), "big_redstonelamp");

		enigmaticItem = new EnigmaticItem();
		xpScroll = new XPScroll();
		enigmaticAmulet = new EnigmaticAmulet();
		magnetRing = new MagnetRing();
		extradimensionalEye = new ExtradimensionalEye();
		relicOfTesting = new RelicOfTesting();
		recallPotion = new RecallPotion();
		forbiddenAxe = new ForbiddenAxe();
		escapeScroll = new EscapeScroll();
		heavenScroll = new HeavenScroll();
		superMagnetRing = new SuperMagnetRing();
		golemHeart = new GolemHeart();
		megaSponge = new Megasponge();
		unholyGrail = new UnholyGrail();
		eyeOfNebula = new EyeOfNebula();
		magmaHeart = new MagmaHeart();
		voidPearl = new VoidPearl();
		oceanStone = new OceanStone();
		angelBlessing = new AngelBlessing();
		monsterCharm = new MonsterCharm();
		miningCharm = new MiningCharm();
		enderRing = new EnderRing();
		mendingMixture = new MendingMixture();
		lootGenerator = new LootGenerator();
		thiccScroll = new ThiccScroll();
		ironRing = new IronRing();
		etheriumOre = new EtheriumOre(etheriumConfig);
		etheriumIngot = new EtheriumIngot(etheriumConfig);

		hastePotionDefault = (HastePotion) new HastePotion(Rarity.COMMON, 3600, 0).setRegistryName(new ResourceLocation(MODID, "haste_potion_default"));
		hastePotionExtended = (HastePotion) new HastePotion(Rarity.COMMON, 9600, 0).setRegistryName(new ResourceLocation(MODID, "haste_potion_extended"));
		hastePotionEmpowered = (HastePotion) new HastePotion(Rarity.COMMON, 1800, 1).setRegistryName(new ResourceLocation(MODID, "haste_potion_empowered"));
		hastePotionExtendedEmpowered = (HastePotion) new HastePotion(Rarity.RARE, 4800, 1).setRegistryName(new ResourceLocation(MODID, "haste_potion_extended_empowered"));

		commonPotionBase = (UltimatePotionBase) new UltimatePotionBase(Rarity.COMMON, PotionType.COMMON).setRegistryName(new ResourceLocation(MODID, "common_potion"));
		commonPotionSplash = (UltimatePotionSplash) new UltimatePotionSplash(Rarity.COMMON, PotionType.COMMON).setRegistryName(new ResourceLocation(MODID, "common_potion_splash"));
		commonPotionLingering = (UltimatePotionLingering) new UltimatePotionLingering(Rarity.COMMON, PotionType.COMMON).setRegistryName(new ResourceLocation(MODID, "common_potion_lingering"));

		ultimatePotionBase = (UltimatePotionBase) new UltimatePotionBase(Rarity.RARE, PotionType.ULTIMATE).setRegistryName(new ResourceLocation(MODID, "ultimate_potion"));
		ultimatePotionSplash = (UltimatePotionSplash) new UltimatePotionSplash(Rarity.RARE, PotionType.ULTIMATE).setRegistryName(new ResourceLocation(MODID, "ultimate_potion_splash"));
		ultimatePotionLingering = (UltimatePotionLingering) new UltimatePotionLingering(Rarity.RARE, PotionType.ULTIMATE).setRegistryName(new ResourceLocation(MODID, "ultimate_potion_lingering"));

		etheriumHelmet = (EtheriumArmor) new EtheriumArmor(etheriumConfig, EquipmentSlotType.HEAD).setRegistryName(new ResourceLocation(MODID, "etherium_helmet"));
		etheriumChestplate = (EtheriumArmor) new EtheriumArmor(etheriumConfig, EquipmentSlotType.CHEST).setRegistryName(new ResourceLocation(MODID, "etherium_chestplate"));
		etheriumLeggings = (EtheriumArmor) new EtheriumArmor(etheriumConfig, EquipmentSlotType.LEGS).setRegistryName(new ResourceLocation(MODID, "etherium_leggings"));
		etheriumBoots = (EtheriumArmor) new EtheriumArmor(etheriumConfig, EquipmentSlotType.FEET).setRegistryName(new ResourceLocation(MODID, "etherium_boots"));

		etheriumPickaxe = new EtheriumPickaxe(etheriumConfig);
		etheriumAxe = new EtheriumAxe(etheriumConfig);
		etheriumShovel = new EtheriumShovel(etheriumConfig);
		etheriumSword = new EtheriumSword(etheriumConfig);
		etheriumScythe = new EtheriumScythe(etheriumConfig);

		astralDust = new AstralDust();
		loreInscriber = new LoreInscriber();
		loreFragment = new LoreFragment();
		enderRod = new EnderRod(etheriumConfig);

		astralBreaker = new AstralBreaker();
		oblivionStone = new OblivionStone();
		enchantmentTransposer = new EnchantmentTransposer();

		gemOfBinding = new GemOfBinding();
		wormholePotion = new WormholePotion();
		fabulousScroll = new FabulousScroll();
		storageCrystal = new StorageCrystal();
		soulCrystal = new SoulCrystal();

		theAcknowledgment = new TheAcknowledgment();
		overworldRevelationTome = new RevelationTome(Rarity.UNCOMMON, RevelationTome.TomeType.OVERWORLD, "tattered_tome");
		netherRevelationTome = new RevelationTome(Rarity.UNCOMMON, RevelationTome.TomeType.NETHER, "withered_tome");
		endRevelationTome = new RevelationTome(Rarity.RARE, RevelationTome.TomeType.END, "corrupted_tome");

		darkHelmet = (DarkArmor) new DarkArmor(EnigmaticArmorMaterials.ETHERIUM, EquipmentSlotType.HEAD).setRegistryName(new ResourceLocation(MODID, "dark_helmet"));
		darkChestplate = (DarkArmor) new DarkArmor(EnigmaticArmorMaterials.ETHERIUM, EquipmentSlotType.CHEST).setRegistryName(new ResourceLocation(MODID, "dark_chestplate"));
		darkLeggings = (DarkArmor) new DarkArmor(EnigmaticArmorMaterials.ETHERIUM, EquipmentSlotType.LEGS).setRegistryName(new ResourceLocation(MODID, "dark_leggings"));
		darkBoots = (DarkArmor) new DarkArmor(EnigmaticArmorMaterials.ETHERIUM, EquipmentSlotType.FEET).setRegistryName(new ResourceLocation(MODID, "dark_boots"));

		cursedRing = new CursedRing();
		darkMirror = new DarkMirror();

		cryingIngot = new PlaceholderItem("crying_ingot", Rarity.RARE);
		cryingHelmet = new PlaceholderItem("crying_helmet", Rarity.RARE);
		cryingChestplate = new PlaceholderItem("crying_chestplate", Rarity.RARE);
		cryingLeggings = new PlaceholderItem("crying_leggings", Rarity.RARE);
		cryingBoots = new PlaceholderItem("crying_boots", Rarity.RARE);
		cryingPickaxe = new PlaceholderItem("crying_pickaxe", Rarity.RARE);
		cryingAxe = new PlaceholderItem("crying_axe", Rarity.RARE);
		cryingSword = new PlaceholderItem("crying_sword", Rarity.RARE);
		cryingShovel = new PlaceholderItem("crying_shovel", Rarity.RARE);
		cryingHoe = new PlaceholderItem("crying_hoe", Rarity.RARE);

		cursedScroll = new CursedScroll();
		berserkEmblem = new BerserkEmblem();
		guardianHeart = new GuardianHeart();
		theTwist = new TheTwist();
		evilEssence = new EvilEssence();
		forbiddenFruit = new ForbiddenFruit();
		antiforbiddenPotion = new AntiforbiddenPotion();
		animalGuide = new AnimalGuide();
		hunterGuide = new HunterGuide();
		earthHeart = new EarthHeart();
		twistedCore = new TwistedCore();

		gemRing = new GemRing();
		cursedStone = new CursedStone();
		enchanterPearl = new EnchanterPearl();
		trueNotchApple = new PlaceholderItem("true_notch_apple", Rarity.EPIC);
		avariceScroll = new AvariceScroll();
		infinimeal = new Infinimeal();
		darkestScroll = new DarkestScroll();

		sharpshooterEnchantment = new SharpshooterEnchantment(EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND);
		ceaselessEnchantment = new CeaselessEnchantment(EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND);
		nemesisCurseEnchantment = new NemesisCurseEnchantment(EquipmentSlotType.MAINHAND);
		torrentEnchantment = new TorrentEnchantment(EquipmentSlotType.MAINHAND);
		wrathEnchantment = new WrathEnchantment(EquipmentSlotType.MAINHAND);
		slayerEnchantment = new SlayerEnchantment(EquipmentSlotType.MAINHAND);

		spellstoneList = Lists.newArrayList(
				angelBlessing,
				magmaHeart,
				golemHeart,
				oceanStone,
				eyeOfNebula,
				voidPearl,
				enigmaticItem
				);

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
		MinecraftForge.EVENT_BUS.register(new EtheriumEventHandler(etheriumConfig, etheriumOre));
		MinecraftForge.EVENT_BUS.addListener(this::onServerStart);

		logger.info("Mod instance constructed successfully.");
	}

	public void onLoadComplete(final FMLLoadCompleteEvent event) {
		logger.info("Initializing load completion phase...");

		logger.info("Registering brewing recipes...");

		if (OmniconfigHandler.isItemEnabled(recallPotion)) {
			BrewingRecipeRegistry.addRecipe(new SpecialBrewingRecipe(Ingredient.fromStacks(PotionHelper.createVanillaPotion(Items.POTION, Potions.AWKWARD)), Ingredient.fromItems(Items.ENDER_EYE), new ItemStack(recallPotion), new ResourceLocation(MODID, "recall_potion")));
		}

		if (OmniconfigHandler.isItemEnabled(commonPotionBase)) {
			PotionHelper.registerCommonPotions();
		}

		if (OmniconfigHandler.isItemEnabled(ultimatePotionBase)) {
			PotionHelper.registerBasicUltimatePotions();
			PotionHelper.registerSplashUltimatePotions();
			PotionHelper.registerLingeringUltimatePotions();
		}

		BrewingRecipeRegistry.addRecipe(new ValidationBrewingRecipe(Ingredient.fromItems(hastePotionExtendedEmpowered, recallPotion, ultimatePotionLingering, commonPotionLingering), null));

		EnigmaticUpdateHandler.init();

		proxy.loadComplete(event);

		universalClock = new ItemStack(Items.CLOCK);
		soulOfTheArchitect = UUID.fromString("3efc546d-30bb-4c29-bb61-b3081a118408");

		logger.info("Load completion phase finished successfully");
	}

	private void setup(final FMLCommonSetupEvent event) {
		logger.info("Initializing common setup phase...");

		damageTypesFire.add(DamageSource.LAVA.damageType);
		damageTypesFire.add(DamageSource.IN_FIRE.damageType);
		damageTypesFire.add(DamageSource.ON_FIRE.damageType);

		logger.info("Registering packets...");
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

		logger.info("Registering triggers...");
		CriteriaTriggers.register(UseUnholyGrailTrigger.INSTANCE);
		CriteriaTriggers.register(BeheadingTrigger.INSTANCE);
		CriteriaTriggers.register(RevelationGainTrigger.INSTANCE);
		CriteriaTriggers.register(CursedRingEquippedTrigger.INSTANCE);
		CriteriaTriggers.register(RevelationTomeBurntTrigger.INSTANCE);

		logger.info("Common setup phase finished successfully.");
	}

	private void clientRegistries(final FMLClientSetupEvent event) {
		logger.info("Initializing client setup phase...");
		keybindHandler.registerKeybinds();
		enigmaticAmulet.registerVariants();

		for (final Block theBlock : cutoutBlockRegistry) {
			RenderTypeLookup.setRenderLayer(theBlock, RenderType.getCutout());
		}

		proxy.initEntityRendering();

		ScreenManager.registerFactory(PORTABLE_CRAFTER, CraftingScreen::new);
		ScreenManager.registerFactory(LORE_INSCRIBER_CONTAINER, LoreInscriberScreen::new);

		logger.info("Client setup phase finished successfully.");
	}

	private void intermodStuff(final InterModEnqueueEvent event) {
		logger.info("Sending messages to Curios API...");
		SuperpositionHandler.registerCurioType("charm", 1, true, false, null);
		SuperpositionHandler.registerCurioType("ring", 2, true, false, null);
		SuperpositionHandler.registerCurioType("spellstone", 1, false, false, new ResourceLocation(MODID, "slots/empty_spellstone_slot"));
		SuperpositionHandler.registerCurioType("scroll", 1, false, false, new ResourceLocation(MODID, "slots/empty_scroll_slot"));
		//SuperpositionHandler.registerCurioType("curio", -1, true, false, null);

	}

	private void onServerStart(FMLServerAboutToStartEvent event) {
		this.performCleanup();
	}

	/**
	 * Alright boys, it's cleanup time!
	 * @param event
	 */

	public void performCleanup() {
		// TODO Figure something out with those multimaps
		// I'd really like there to be a weak multimap or something

		proxy.clearTransientData();
		EnigmaticEventHandler.angeredGuardians.clear();
		EnigmaticEventHandler.postmortalPossession.clear();
		EnigmaticEventHandler.knockbackThatBastard.clear();
		EnigmaticEventHandler.deferredToast.clear();
		soulCrystal.attributeDispatcher.clear();
		enigmaticItem.flightMap.clear();
		heavenScroll.flyMap.clear();
		RegisteredMeleeAttack.clearRegistry();
	}

	public boolean isCSGPresent() {
		return ModList.get().isLoaded("customstartinggear");
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {

		@SubscribeEvent
		public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
			final IForgeRegistry<ContainerType<?>> registry = event.getRegistry();

			registry.registerAll(
					PORTABLE_CRAFTER.setRegistryName(MODID, "portable_crafter"),
					IForgeContainerType.create(LoreInscriberContainer::new).setRegistryName(MODID, "enigmatic_repair_container")
					);
		}

		@OnlyIn(Dist.CLIENT)
		@SubscribeEvent
		public static void stitchTextures(final TextureStitchEvent.Pre evt) {

			if (evt.getMap().getTextureLocation() == PlayerContainer.LOCATION_BLOCKS_TEXTURE) {

				evt.addSprite(new ResourceLocation(MODID, "slots/empty_spellstone_slot"));
				evt.addSprite(new ResourceLocation(MODID, "slots/empty_scroll_slot"));

			}
		}

		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> event) {
			logger.info("Initializing blocks registration...");

			event.getRegistry().registerAll(
					massiveLamp,
					bigLamp,
					massiveShroomlamp,
					bigShroomlamp,
					massiveRedstonelamp,
					bigRedstonelamp);

			logger.info("Blocks registered successfully.");
		}

		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {

			logger.info("Initializing items registration...");

			final IForgeRegistry<Item> registry = event.getRegistry();

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
					enchantmentTransposer,
					fabulousScroll,
					storageCrystal,
					soulCrystal,
					theAcknowledgment,
					overworldRevelationTome,
					netherRevelationTome,
					endRevelationTome,
					//darkHelmet,
					//darkChestplate,
					//darkLeggings,
					//darkBoots,
					cursedRing,
					darkMirror,
					//cryingIngot,
					//cryingHelmet,
					//cryingChestplate,
					//cryingLeggings,
					//cryingBoots,
					//cryingPickaxe,
					//cryingAxe,
					//cryingSword,
					//cryingShovel,
					//cryingHoe,
					earthHeart,
					twistedCore,
					cursedScroll,
					berserkEmblem,
					guardianHeart,
					theTwist,
					evilEssence,
					animalGuide,
					hunterGuide,
					forbiddenFruit,
					antiforbiddenPotion,
					gemRing,
					cursedStone,
					enchanterPearl,
					//trueNotchApple,
					avariceScroll,
					infinimeal,
					darkestScroll,
					new GenericBlockItem(massiveLamp),
					new GenericBlockItem(bigLamp),
					new GenericBlockItem(massiveShroomlamp),
					new GenericBlockItem(bigShroomlamp),
					new GenericBlockItem(massiveRedstonelamp),
					new GenericBlockItem(bigRedstonelamp)
					//,gemOfBinding,wormholePotion
					);

			logger.info("Items registered successfully.");
		}

		@SubscribeEvent
		public static void registerSounds(final RegistryEvent.Register<SoundEvent> event) {
			logger.info("Initializing sounds registration...");

			HHON = SuperpositionHandler.registerSound("misc.hhon");
			HHOFF = SuperpositionHandler.registerSound("misc.hhoff");
			SHIELD_TRIGGER = SuperpositionHandler.registerSound("misc.shield_trigger");
			DEFLECT = SuperpositionHandler.registerSound("misc.deflect");
			WRITE = SuperpositionHandler.registerSound("misc.write");
			LEARN = SuperpositionHandler.registerSound("misc.learn");

			logger.info("Sounds registered successfully.");
		}

		@SubscribeEvent
		public static void onRecipeRegister(final RegistryEvent.Register<IRecipeSerializer<?>> e) {

		}

		@SubscribeEvent
		public static void registerBrewing(final RegistryEvent.Register<Potion> event) {

			logger.info("Initializing advanced potion system...");

			ULTIMATE_NIGHT_VISION = new AdvancedPotion("ultimate_night_vision", new EffectInstance(Effects.NIGHT_VISION, 19200));
			ULTIMATE_INVISIBILITY = new AdvancedPotion("ultimate_invisibility", new EffectInstance(Effects.INVISIBILITY, 19200));
			ULTIMATE_LEAPING = new AdvancedPotion("ultimate_leaping", new EffectInstance(Effects.JUMP_BOOST, 9600, 1));
			ULTIMATE_FIRE_RESISTANCE = new AdvancedPotion("ultimate_fire_resistance", new EffectInstance(Effects.FIRE_RESISTANCE, 19200));
			ULTIMATE_SWIFTNESS = new AdvancedPotion("ultimate_swiftness", new EffectInstance(Effects.SPEED, 9600, 1));
			ULTIMATE_SLOWNESS = new AdvancedPotion("ultimate_slowness", new EffectInstance(Effects.SLOWNESS, 1200, 3));
			ULTIMATE_TURTLE_MASTER = new AdvancedPotion("ultimate_turtle_master", new EffectInstance(Effects.SLOWNESS, 800, 5), new EffectInstance(Effects.RESISTANCE, 800, 3));
			ULTIMATE_WATER_BREATHING = new AdvancedPotion("ultimate_water_breathing", new EffectInstance(Effects.WATER_BREATHING, 19200));
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

			logger.info("Advanced potion system initialized successfully.");
		}

		@SubscribeEvent
		public static void registerEnchantments(final RegistryEvent.Register<Enchantment> event) {
			final IForgeRegistry<Enchantment> registry = event.getRegistry();

			registry.registerAll(
					sharpshooterEnchantment,
					ceaselessEnchantment,
					nemesisCurseEnchantment,
					torrentEnchantment,
					wrathEnchantment,
					slayerEnchantment);

		}

		@SubscribeEvent
		public static void registerLootModifiers(final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
			final IForgeRegistry<GlobalLootModifierSerializer<?>> registry = event.getRegistry();

			registry.registerAll();
		}

		@SubscribeEvent
		public static void onEntitiesRegistry(final RegistryEvent.Register<EntityType<?>> event) {
			logger.info("Initializing entities registration...");

			event.getRegistry().register(EntityType.Builder.<PermanentItemEntity>create(PermanentItemEntity::new, EntityClassification.MISC).size(0.25F, 0.25F).setTrackingRange(64).setCustomClientFactory((spawnEntity, world) -> new PermanentItemEntity(PermanentItemEntity.TYPE, world)).setUpdateInterval(2).setShouldReceiveVelocityUpdates(true).build(MODID+":permanent_item_entity").setRegistryName(new ResourceLocation(MODID, "permanent_item_entity")));

			event.getRegistry().register(EntityType.Builder.<EnigmaticPotionEntity>create(EnigmaticPotionEntity::new, EntityClassification.MISC).size(0.25F, 0.25F).setTrackingRange(64).setCustomClientFactory((spawnEntity, world) -> new EnigmaticPotionEntity(EnigmaticPotionEntity.TYPE, world)).setUpdateInterval(10).setShouldReceiveVelocityUpdates(true).build(MODID+":enigmatic_potion_entity").setRegistryName(new ResourceLocation(MODID, "enigmatic_potion_entity")));

			event.getRegistry().register(EntityType.Builder.<UltimateWitherSkullEntity>create(UltimateWitherSkullEntity::new, EntityClassification.MISC).size(0.25F, 0.25F).setTrackingRange(64).setCustomClientFactory((spawnEntity, world) -> new UltimateWitherSkullEntity(UltimateWitherSkullEntity.TYPE, world))
					//.setUpdateInterval(1)
					//.setShouldReceiveVelocityUpdates(true)
					.build(MODID+":ultimate_wither_skull_entity").setRegistryName(new ResourceLocation(MODID, "ultimate_wither_skull_entity")));

			logger.info("Entities registered successfully.");
		}

	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onColorInit(final net.minecraftforge.client.event.ColorHandlerEvent.Item event) {
		logger.info("Initializing colors registration...");

		event.getItemColors().register((stack, color) -> {
			if (PotionHelper.isAdvancedPotion(stack))
				return color > 0 ? -1 : PotionHelper.getColor(stack);

				return color > 0 ? -1 : PotionUtils.getColor(stack);
		}, ultimatePotionBase, ultimatePotionSplash, ultimatePotionLingering, commonPotionBase, commonPotionSplash, commonPotionLingering);

		logger.info("Colors registered successfully.");
	}

	public static final ItemGroup enigmaticTab = new ItemGroup("enigmaticCreativeTab") {
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack createIcon() {
			return new ItemStack(enigmaticItem);
		}
	};

	public static final ItemGroup enigmaticPotionTab = new ItemGroup("enigmaticPotionCreativeTab") {
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack createIcon() {
			return new ItemStack(recallPotion);
		}
	};

	public static final Rarity LEGENDARY = Rarity.create("legendary", TextFormatting.GOLD);

}
