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
import com.integral.enigmaticlegacy.blocks.BlockAstralDust;
import com.integral.enigmaticlegacy.blocks.BlockBigLamp;
import com.integral.enigmaticlegacy.blocks.BlockCosmicCake;
import com.integral.enigmaticlegacy.blocks.BlockMassiveLamp;
import com.integral.enigmaticlegacy.brewing.SpecialBrewingRecipe;
import com.integral.enigmaticlegacy.brewing.ValidationBrewingRecipe;
import com.integral.enigmaticlegacy.client.Quote;
import com.integral.enigmaticlegacy.config.EtheriumConfigHandler;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.crafting.EnigmaticRecipeSerializers;
import com.integral.enigmaticlegacy.crafting.HiddenRecipe;
import com.integral.enigmaticlegacy.effects.BlazingStrengthEffect;
import com.integral.enigmaticlegacy.effects.MoltenHeartEffect;
import com.integral.enigmaticlegacy.enchantments.CeaselessEnchantment;
import com.integral.enigmaticlegacy.enchantments.EternalBindingCurse;
import com.integral.enigmaticlegacy.enchantments.NemesisCurse;
import com.integral.enigmaticlegacy.enchantments.SharpshooterEnchantment;
import com.integral.enigmaticlegacy.enchantments.SlayerEnchantment;
import com.integral.enigmaticlegacy.enchantments.SorrowCurse;
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
import com.integral.enigmaticlegacy.handlers.SoulArchive;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.PotionHelper;
import com.integral.enigmaticlegacy.items.AbyssalHeart;
import com.integral.enigmaticlegacy.items.AngelBlessing;
import com.integral.enigmaticlegacy.items.AnimalGuide;
import com.integral.enigmaticlegacy.items.AntiforbiddenPotion;
import com.integral.enigmaticlegacy.items.ArchitectEye;
import com.integral.enigmaticlegacy.items.AscensionAmulet;
import com.integral.enigmaticlegacy.items.AstralBreaker;
import com.integral.enigmaticlegacy.items.AstralDust;
import com.integral.enigmaticlegacy.items.AstralFruit;
import com.integral.enigmaticlegacy.items.AstralPotato;
import com.integral.enigmaticlegacy.items.AvariceScroll;
import com.integral.enigmaticlegacy.items.BerserkEmblem;
import com.integral.enigmaticlegacy.items.CosmicCake;
import com.integral.enigmaticlegacy.items.CosmicHeart;
import com.integral.enigmaticlegacy.items.CurseTransposer;
import com.integral.enigmaticlegacy.items.CursedRing;
import com.integral.enigmaticlegacy.items.CursedScroll;
import com.integral.enigmaticlegacy.items.CursedStone;
import com.integral.enigmaticlegacy.items.DarkArmor;
import com.integral.enigmaticlegacy.items.DarkMirror;
import com.integral.enigmaticlegacy.items.DarkestScroll;
import com.integral.enigmaticlegacy.items.DesolationRing;
import com.integral.enigmaticlegacy.items.EarthHeart;
import com.integral.enigmaticlegacy.items.EldritchAmulet;
import com.integral.enigmaticlegacy.items.EnchanterPearl;
import com.integral.enigmaticlegacy.items.EnchantmentTransposer;
import com.integral.enigmaticlegacy.items.EnderRing;
import com.integral.enigmaticlegacy.items.EnderSlayer;
import com.integral.enigmaticlegacy.items.EnigmaticAmulet;
import com.integral.enigmaticlegacy.items.EnigmaticElytra;
import com.integral.enigmaticlegacy.items.EnigmaticItem;
import com.integral.enigmaticlegacy.items.EscapeScroll;
import com.integral.enigmaticlegacy.items.EvilEssence;
import com.integral.enigmaticlegacy.items.EvilIngot;
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
import com.integral.enigmaticlegacy.items.IchorBottle;
import com.integral.enigmaticlegacy.items.InfernalShield;
import com.integral.enigmaticlegacy.items.Infinimeal;
import com.integral.enigmaticlegacy.items.IronRing;
import com.integral.enigmaticlegacy.items.LivingFlame;
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
import com.integral.enigmaticlegacy.items.QuotePlayer;
import com.integral.enigmaticlegacy.items.RecallPotion;
import com.integral.enigmaticlegacy.items.RelicOfTesting;
import com.integral.enigmaticlegacy.items.RevelationTome;
import com.integral.enigmaticlegacy.items.SoulCompass;
import com.integral.enigmaticlegacy.items.SoulCrystal;
import com.integral.enigmaticlegacy.items.StorageCrystal;
import com.integral.enigmaticlegacy.items.SuperMagnetRing;
import com.integral.enigmaticlegacy.items.TheAcknowledgment;
import com.integral.enigmaticlegacy.items.TheCube;
import com.integral.enigmaticlegacy.items.TheInfinitum;
import com.integral.enigmaticlegacy.items.TheTwist;
import com.integral.enigmaticlegacy.items.ThiccScroll;
import com.integral.enigmaticlegacy.items.TwistedCore;
import com.integral.enigmaticlegacy.items.TwistedPotion;
import com.integral.enigmaticlegacy.items.UltimatePotionBase;
import com.integral.enigmaticlegacy.items.UltimatePotionLingering;
import com.integral.enigmaticlegacy.items.UltimatePotionSplash;
import com.integral.enigmaticlegacy.items.UnholyGrail;
import com.integral.enigmaticlegacy.items.UnwitnessedAmulet;
import com.integral.enigmaticlegacy.items.VoidPearl;
import com.integral.enigmaticlegacy.items.WormholePotion;
import com.integral.enigmaticlegacy.items.XPScroll;
import com.integral.enigmaticlegacy.items.generic.GenericBlockItem;
import com.integral.enigmaticlegacy.objects.AdvancedPotion;
import com.integral.enigmaticlegacy.objects.LoggerWrapper;
import com.integral.enigmaticlegacy.objects.RegisteredMeleeAttack;
import com.integral.enigmaticlegacy.objects.SpecialLootModifier;
import com.integral.enigmaticlegacy.packets.clients.PacketCosmicRevive;
import com.integral.enigmaticlegacy.packets.clients.PacketFlameParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketForceArrowRotations;
import com.integral.enigmaticlegacy.packets.clients.PacketGenericParticleEffect;
import com.integral.enigmaticlegacy.packets.clients.PacketHandleItemPickup;
import com.integral.enigmaticlegacy.packets.clients.PacketPatchouliForce;
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
import com.integral.enigmaticlegacy.triggers.BeheadingTrigger;
import com.integral.enigmaticlegacy.triggers.CursedInventoryChangedTrigger;
import com.integral.enigmaticlegacy.triggers.CursedRingEquippedTrigger;
import com.integral.enigmaticlegacy.triggers.ForbiddenFruitTrigger;
import com.integral.enigmaticlegacy.triggers.RevelationGainTrigger;
import com.integral.enigmaticlegacy.triggers.RevelationTomeBurntTrigger;
import com.integral.enigmaticlegacy.triggers.UseUnholyGrailTrigger;
import com.integral.etherium.blocks.BlockEtherium;
import com.integral.etherium.core.EtheriumEventHandler;
import com.integral.etherium.items.EnderRod;
import com.integral.etherium.items.EtheriumArmor;
import com.integral.etherium.items.EtheriumAxe;
import com.integral.etherium.items.EtheriumIngot;
import com.integral.etherium.items.EtheriumNugget;
import com.integral.etherium.items.EtheriumOre;
import com.integral.etherium.items.EtheriumPickaxe;
import com.integral.etherium.items.EtheriumScraps;
import com.integral.etherium.items.EtheriumScythe;
import com.integral.etherium.items.EtheriumShovel;
import com.integral.etherium.items.EtheriumSword;
import com.integral.omniconfig.packets.PacketSyncOptions;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import com.mojang.blaze3d.platform.ScreenManager;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.AttackDamageMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
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
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod(EnigmaticLegacy.MODID)
public class EnigmaticLegacy {
	public static final String MODID = "enigmaticlegacy";
	public static final String VERSION = "2.21.1";
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
	public static SoundEvent SWORD_HIT_REJECT;
	public static SoundEvent UNEAT;

	public static BlockMassiveLamp massiveLamp;
	public static BlockBigLamp bigLamp;
	public static BlockMassiveLamp massiveShroomlamp;
	public static BlockBigLamp bigShroomlamp;
	public static BlockMassiveLamp massiveRedstonelamp;
	public static BlockBigLamp bigRedstonelamp;
	public static BlockEtherium etheriumBlock;
	public static BlockCosmicCake cosmicCake;
	public static BlockAstralDust astralBlock;

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
	@ConfigurableItem("Etherium Nugget") public static EtheriumNugget etheriumNugget;
	@ConfigurableItem("Etherium Scraps") public static EtheriumScraps etheriumScraps;
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
	@ConfigurableItem("Tome of Devoured Malignancy") public static CurseTransposer curseTransposer;

	//@ConfigurableItem("Gem of Binding") public static GemOfBinding gemOfBinding;
	//@ConfigurableItem("Wormhole Potion") public static WormholePotion wormholePotion;
	@ConfigurableItem("Grace of the Creator") public static FabulousScroll fabulousScroll;
	@ConfigurableItem("") public static StorageCrystal storageCrystal;
	@ConfigurableItem("") public static SoulCrystal soulCrystal;

	@ConfigurableItem("The Acknowledgment") public static TheAcknowledgment theAcknowledgment;
	@ConfigurableItem("Tattered Tome") public static RevelationTome overworldRevelationTome;
	@ConfigurableItem("Withered Tome") public static RevelationTome netherRevelationTome;
	@ConfigurableItem("Corrupted Tome") public static RevelationTome endRevelationTome;

	//	@ConfigurableItem("Dark Armor") public static DarkArmor darkHelmet;
	//	@ConfigurableItem("Dark Armor") public static DarkArmor darkChestplate;
	//	@ConfigurableItem("Dark Armor") public static DarkArmor darkLeggings;
	//	@ConfigurableItem("Dark Armor") public static DarkArmor darkBoots;

	@ConfigurableItem("Ring of the Seven Curses") public static CursedRing cursedRing;
	@ConfigurableItem("Twisted Mirror") public static DarkMirror darkMirror;

	//	@ConfigurableItem("Crying Netherite Ingot") public static PlaceholderItem cryingIngot;
	//	@ConfigurableItem("Crying Netherite Armor") public static PlaceholderItem cryingHelmet;
	//	@ConfigurableItem("Crying Netherite Armor") public static PlaceholderItem cryingChestplate;
	//	@ConfigurableItem("Crying Netherite Armor") public static PlaceholderItem cryingLeggings;
	//	@ConfigurableItem("Crying Netherite Armor") public static PlaceholderItem cryingBoots;
	//	@ConfigurableItem("Crying Netherite Pickaxe") public static PlaceholderItem cryingPickaxe;
	//	@ConfigurableItem("Crying Netherite Axe") public static PlaceholderItem cryingAxe;
	//	@ConfigurableItem("Crying Netherite Sword") public static PlaceholderItem cryingSword;
	//	@ConfigurableItem("Crying Netherite Shovel") public static PlaceholderItem cryingShovel;
	//	@ConfigurableItem("Crying Netherite Hoe") public static PlaceholderItem cryingHoe;

	@ConfigurableItem("Scroll of a Thousand Curses") public static CursedScroll cursedScroll;
	@ConfigurableItem("Emblem of Bloodstained Valor") public static BerserkEmblem berserkEmblem;
	@ConfigurableItem("Heart of the Earth") public static EarthHeart earthHeart;
	@ConfigurableItem("Twisted Heart") public static TwistedCore twistedCore;

	@ConfigurableItem("Heart of the Guardian") public static GuardianHeart guardianHeart;
	@ConfigurableItem("The Twist") public static TheTwist theTwist;
	@ConfigurableItem("Nefarious Essence") public static EvilEssence evilEssence;
	@ConfigurableItem("Nefarious Ingot") public static EvilIngot evilIngot;
	@ConfigurableItem("Guite to Animal Companionship") public static AnimalGuide animalGuide;
	@ConfigurableItem("Guide to Feral Hunt") public static HunterGuide hunterGuide;
	@ConfigurableItem("Forbidden Fruit") public static ForbiddenFruit forbiddenFruit;
	@ConfigurableItem("") public static AntiforbiddenPotion antiforbiddenPotion;

	@ConfigurableItem("Exquisite Ring") public static GemRing gemRing;
	@ConfigurableItem("Unholy Stone") public static CursedStone cursedStone;
	@ConfigurableItem("Enchanter's Pearl") public static EnchanterPearl enchanterPearl;
	//@ConfigurableItem("Fruit of Ascension") public static PlaceholderItem trueNotchApple;
	@ConfigurableItem("Pact of Infinite Avarice") public static AvariceScroll avariceScroll;
	@ConfigurableItem("Essence of Raging Life") public static Infinimeal infinimeal;
	@ConfigurableItem("Darkest Scroll") public static DarkestScroll darkestScroll;
	@ConfigurableItem("") public static UnwitnessedAmulet unwitnessedAmulet;
	@ConfigurableItem("Potion of Twisted Mercy") public static TwistedPotion twistedPotion;
	@ConfigurableItem("Bulwark of Blazing Pride") public static InfernalShield infernalShield;
	@ConfigurableItem("Heart of the Cosmos") public static CosmicHeart cosmicHeart;
	@ConfigurableItem("Heart of the Abyss") public static AbyssalHeart abyssalHeart;
	@ConfigurableItem("The Infinitum") public static TheInfinitum theInfinitum;
	@ConfigurableItem("Celestial Fruit") public static AstralFruit astralFruit;
	@ConfigurableItem("The Ender Slayer") public static EnderSlayer enderSlayer;
	@ConfigurableItem("Non-Euclidean Cube") public static TheCube theCube;
	@ConfigurableItem("The Burden of Desolation") public static DesolationRing desolationRing;
	@ConfigurableItem("Astral Potato") public static AstralPotato astralPotato;
	@ConfigurableItem("Wayfinder of the Damned") public static SoulCompass soulCompass;
	@ConfigurableItem("Amulet of Ascension") public static AscensionAmulet ascensionAmulet;
	@ConfigurableItem("The Testament of Contempt") public static EldritchAmulet eldritchAmulet;
	//@ConfigurableItem("Living Flame") public static LivingFlame livingFlame;
	@ConfigurableItem("Majestic Elytra") public static EnigmaticElytra enigmaticElytra;
	@ConfigurableItem("Inscrutable Eye") public static ArchitectEye architectEye;
	@ConfigurableItem("Bottle of Ichor") public static IchorBottle ichorBottle;
	@ConfigurableItem("") public static QuotePlayer quotePlayer;

	@ConfigurableItem("Cosmic Scroll")
	public static Item cosmicScroll;

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

	public static AdvancedPotion MOLTEN_HEART;
	public static AdvancedPotion LONG_MOLTEN_HEART;
	public static AdvancedPotion ULTIMATE_MOLTEN_HEART;

	public static AdvancedPotion EMPTY;
	public static AdvancedPotion testingPotion;

	public static BlazingStrengthEffect blazingStrengthEffect;
	public static MoltenHeartEffect moltenHeartEffect;

	public static ResourceLocation timeWithCursesStat;
	public static ResourceLocation timeWithoutCursesStat;

	@ConfigurableItem("Sharpshooter Enchantment") public static SharpshooterEnchantment sharpshooterEnchantment;
	@ConfigurableItem("Ceaseless Enchantment") public static CeaselessEnchantment ceaselessEnchantment;
	@ConfigurableItem("Torrent Enchantment") public static TorrentEnchantment torrentEnchantment;
	@ConfigurableItem("Wrath Enchantment") public static WrathEnchantment wrathEnchantment;
	@ConfigurableItem("Slayer Enchantment") public static SlayerEnchantment slayerEnchantment;
	@ConfigurableItem("Curse of Nemesis") public static NemesisCurse nemesisCurse;
	@ConfigurableItem("Curse of Eternal Binding") public static EternalBindingCurse eternalBindingCurse;
	@ConfigurableItem("Curse of Sorrow") public static SorrowCurse sorrowCurse;

	public static final UUID SOUL_OF_THE_ARCHITECT = UUID.fromString("bfa45411-874a-4ee0-b3bd-00c716059d95");

	public static List<Item> spellstoneList;
	public static EtheriumConfigHandler etheriumConfig;

	public static final MenuType<PortableCrafterContainer> PORTABLE_CRAFTER = new MenuType<>((syncId, playerInv) -> new PortableCrafterContainer(syncId, playerInv, ContainerLevelAccess.create(playerInv.player.level, playerInv.player.blockPosition())));

	@ObjectHolder(MODID + ":enigmatic_repair_container")
	public static final MenuType<LoreInscriberContainer> LORE_INSCRIBER_CONTAINER = null;

	private static final String PTC_VERSION = "1";

	public static final CommonProxy proxy = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
	public static final List<Triple<LootTable, LootPool, Exception>> exceptionList = new ArrayList<>();

	@SuppressWarnings("deprecation")
	public EnigmaticLegacy() {
		logger.info("Constructing mod instance...");

		enigmaticLegacy = this;

		OmniconfigHandler.initialize();
		etheriumConfig = new EtheriumConfigHandler();

		EnigmaticMaterials.setEtheriumConfig(etheriumConfig);
		EnigmaticArmorMaterials.setEtheriumConfig(etheriumConfig);

		enigmaticHandler = new EnigmaticEventHandler();
		keybindHandler = new EnigmaticKeybindHandler();

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientRegistries);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::intermodStuff);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onLoadComplete);
		FMLJavaModLoadingContext.get().getModEventBus().register(new EnigmaticRecipeSerializers());
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
		FMLJavaModLoadingContext.get().getModEventBus().register(proxy);

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(proxy);
		MinecraftForge.EVENT_BUS.register(enigmaticHandler);
		MinecraftForge.EVENT_BUS.register(keybindHandler);
		MinecraftForge.EVENT_BUS.register(new EnigmaticUpdateHandler());
		MinecraftForge.EVENT_BUS.register(new EtheriumEventHandler(etheriumConfig, etheriumOre));
		MinecraftForge.EVENT_BUS.addListener(this::onServerStart);
		MinecraftForge.EVENT_BUS.addListener(this::onServerStarted);

		logger.info("Mod instance constructed successfully.");
	}

	public void onLoadComplete(final FMLLoadCompleteEvent event) {
		logger.info("Initializing load completion phase...");

		logger.info("Registering brewing recipes...");

		if (OmniconfigHandler.isItemEnabled(recallPotion)) {
			BrewingRecipeRegistry.addRecipe(new SpecialBrewingRecipe(Ingredient.of(PotionHelper.createVanillaPotion(Items.POTION, Potions.AWKWARD)), Ingredient.of(Items.ENDER_EYE), new ItemStack(recallPotion), new ResourceLocation(MODID, "recall_potion")));
		}

		if (OmniconfigHandler.isItemEnabled(twistedPotion)) {
			BrewingRecipeRegistry.addRecipe(new SpecialBrewingRecipe(Ingredient.of(recallPotion), Ingredient.of(twistedCore), new ItemStack(twistedPotion), new ResourceLocation(MODID, "twisted_potion")));
		}

		if (OmniconfigHandler.isItemEnabled(commonPotionBase)) {
			PotionHelper.registerCommonPotions();
		}

		if (OmniconfigHandler.isItemEnabled(ultimatePotionBase)) {
			PotionHelper.registerBasicUltimatePotions();
			PotionHelper.registerSplashUltimatePotions();
			PotionHelper.registerLingeringUltimatePotions();
		}

		BrewingRecipeRegistry.addRecipe(new ValidationBrewingRecipe(Ingredient.of(hastePotionExtendedEmpowered, recallPotion, twistedPotion, ultimatePotionLingering, commonPotionLingering), null));

		if (OmniconfigHandler.isItemEnabled(theCube)) {
			HiddenRecipe.addRecipe(new ItemStack(theCube),
					new ItemStack(golemHeart), new ItemStack(cosmicHeart), new ItemStack(magmaHeart),
					new ItemStack(angelBlessing), new ItemStack(Blocks.OBSIDIAN), new ItemStack(eyeOfNebula),
					new ItemStack(oceanStone), new ItemStack(cosmicHeart), new ItemStack(voidPearl)
					);
		}

		if (OmniconfigHandler.isItemEnabled(ascensionAmulet)) {
			HiddenRecipe.addRecipe(new ItemStack(ascensionAmulet),
					new ItemStack(Items.AMETHYST_SHARD), new ItemStack(astralDust), new ItemStack(Items.AMETHYST_SHARD),
					new ItemStack(etheriumIngot), new ItemStack(enigmaticAmulet), new ItemStack(etheriumIngot),
					new ItemStack(Items.DRAGON_BREATH), new ItemStack(cosmicHeart), new ItemStack(Items.DRAGON_BREATH)
					);
		}

		if (OmniconfigHandler.isItemEnabled(eldritchAmulet)) {
			HiddenRecipe.addRecipe(new ItemStack(eldritchAmulet),
					new ItemStack(evilEssence), new ItemStack(abyssalHeart), new ItemStack(evilEssence),
					new ItemStack(Items.NETHERITE_INGOT), new ItemStack(ascensionAmulet), new ItemStack(Items.NETHERITE_INGOT),
					new ItemStack(twistedCore), new ItemStack(Items.NETHER_STAR), new ItemStack(twistedCore)
					);
		}

		if (OmniconfigHandler.isItemEnabled(cosmicScroll)) {
			HiddenRecipe.addRecipe(new ItemStack(cosmicScroll),
					new ItemStack(astralDust), new ItemStack(Items.ENCHANTED_GOLDEN_APPLE), new ItemStack(astralDust),
					new ItemStack(etheriumIngot), new ItemStack(darkestScroll), new ItemStack(etheriumIngot),
					new ItemStack(astralDust), new ItemStack(cosmicHeart), new ItemStack(astralDust)
					);
		}

		EnigmaticUpdateHandler.init();

		proxy.loadComplete(event);

		logger.info("Load completion phase finished successfully");
	}

	private void setup(final FMLCommonSetupEvent event) {
		logger.info("Initializing common setup phase...");

		damageTypesFire.add(DamageSource.LAVA.msgId);
		damageTypesFire.add(DamageSource.IN_FIRE.msgId);
		damageTypesFire.add(DamageSource.ON_FIRE.msgId);

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
		packetInstance.registerMessage(23, PacketToggleMagnetEffects.class, PacketToggleMagnetEffects::encode, PacketToggleMagnetEffects::decode, PacketToggleMagnetEffects::handle);
		packetInstance.registerMessage(24, PacketPatchouliForce.class, PacketPatchouliForce::encode, PacketPatchouliForce::decode, PacketPatchouliForce::handle);
		packetInstance.registerMessage(25, PacketSyncPlayTime.class, PacketSyncPlayTime::encode, PacketSyncPlayTime::decode, PacketSyncPlayTime::handle);
		packetInstance.registerMessage(26, PacketCosmicRevive.class, PacketCosmicRevive::encode, PacketCosmicRevive::decode, PacketCosmicRevive::handle);
		packetInstance.registerMessage(27, PacketEnchantingGUI.class, PacketEnchantingGUI::encode, PacketEnchantingGUI::decode, PacketEnchantingGUI::handle);
		packetInstance.registerMessage(28, PacketUpdateCompass.class, PacketUpdateCompass::encode, PacketUpdateCompass::decode, PacketUpdateCompass::handle);
		packetInstance.registerMessage(29, PacketPlayQuote.class, PacketPlayQuote::encode, PacketPlayQuote::decode, PacketPlayQuote::handle);
		packetInstance.registerMessage(30, PacketUpdateElytraBoosting.class, PacketUpdateElytraBoosting::encode, PacketUpdateElytraBoosting::decode, PacketUpdateElytraBoosting::handle);

		logger.info("Registering triggers...");
		CriteriaTriggers.register(UseUnholyGrailTrigger.INSTANCE);
		CriteriaTriggers.register(BeheadingTrigger.INSTANCE);
		CriteriaTriggers.register(RevelationGainTrigger.INSTANCE);
		CriteriaTriggers.register(CursedRingEquippedTrigger.INSTANCE);
		CriteriaTriggers.register(RevelationTomeBurntTrigger.INSTANCE);
		CriteriaTriggers.register(ForbiddenFruitTrigger.INSTANCE);
		CriteriaTriggers.register(CursedInventoryChangedTrigger.INSTANCE);

		logger.info("Registering stats...");

		event.enqueueWork(() -> {
			timeWithCursesStat = this.makeCustomStat("play_time_with_seven_curses", StatFormatter.TIME);
			timeWithoutCursesStat = this.makeCustomStat("play_time_without_seven_curses", StatFormatter.TIME);
		});

		logger.info("Common setup phase finished successfully.");
	}

	private void clientRegistries(final FMLClientSetupEvent event) {
		logger.info("Initializing client setup phase...");
		keybindHandler.registerKeybinds();
		enigmaticAmulet.registerVariants();
		architectEye.registerVariants();
		soulCompass.registerVariants();

		for (final Block theBlock : cutoutBlockRegistry) {
			ItemBlockRenderTypes.setRenderLayer(theBlock, RenderType.cutout());
		}

		proxy.initEntityRendering();

		//MenuScreens.register(PORTABLE_CRAFTER, CraftingScreen::new);
		MenuScreens.register(LORE_INSCRIBER_CONTAINER, LoreInscriberScreen::new);

		logger.info("Client setup phase finished successfully.");
	}

	private void intermodStuff(final InterModEnqueueEvent event) {
		logger.info("Sending messages to Curios API...");
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

		proxy.clearTransientData();
		EnigmaticEventHandler.angeredGuardians.clear();
		EnigmaticEventHandler.postmortalPossession.clear();
		EnigmaticEventHandler.knockbackThatBastard.clear();
		EnigmaticEventHandler.deferredToast.clear();
		EnigmaticEventHandler.desolationBoxes.clear();
		EnigmaticEventHandler.lastSoulCompassUpdate.clear();
		EnigmaticEventHandler.lastHealth.clear();
		soulCrystal.attributeDispatcher.clear();
		enigmaticItem.flightMap.clear();
		heavenScroll.flyMap.clear();
		theCube.clearLocationCache();
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

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MODID)
	public static class RegistryEvents {

		@SubscribeEvent
		public static void registerContainers(final RegistryEvent.Register<MenuType<?>> event) {
			final IForgeRegistry<MenuType<?>> registry = event.getRegistry();

			registry.registerAll(
					//PORTABLE_CRAFTER.setRegistryName(MODID, "portable_crafter"),
					IForgeMenuType.create(LoreInscriberContainer::new).setRegistryName(MODID, "enigmatic_repair_container")
					);
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
		public static void registerBlocks(final RegistryEvent.Register<Block> event) {
			logger.info("Initializing blocks registration...");

			massiveLamp = new BlockMassiveLamp(BlockBehaviour.Properties.copy(Blocks.LANTERN), "massive_lamp");
			bigLamp = new BlockBigLamp(BlockBehaviour.Properties.copy(Blocks.LANTERN), "big_lamp");
			massiveShroomlamp = new BlockMassiveLamp(BlockBehaviour.Properties.copy(Blocks.LANTERN), "massive_shroomlamp");
			bigShroomlamp = new BlockBigLamp(BlockBehaviour.Properties.copy(Blocks.LANTERN), "big_shroomlamp");
			massiveRedstonelamp = new BlockMassiveLamp(BlockBehaviour.Properties.copy(Blocks.LANTERN), "massive_redstonelamp");
			bigRedstonelamp = new BlockBigLamp(BlockBehaviour.Properties.copy(Blocks.LANTERN), "big_redstonelamp");
			etheriumBlock = new BlockEtherium(etheriumConfig);
			cosmicCake = new BlockCosmicCake();
			astralBlock = new BlockAstralDust();

			event.getRegistry().registerAll(
					massiveLamp,
					bigLamp,
					massiveShroomlamp,
					bigShroomlamp,
					massiveRedstonelamp,
					bigRedstonelamp,
					etheriumBlock,
					cosmicCake,
					astralBlock
					);

			logger.info("Blocks registered successfully.");
		}

		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
			logger.info("Initializing items registration...");

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
			etheriumNugget = new EtheriumNugget(etheriumConfig);
			etheriumScraps = new EtheriumScraps(etheriumConfig);

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

			etheriumHelmet = (EtheriumArmor) new EtheriumArmor(etheriumConfig, EquipmentSlot.HEAD).setRegistryName(new ResourceLocation(MODID, "etherium_helmet"));
			etheriumChestplate = (EtheriumArmor) new EtheriumArmor(etheriumConfig, EquipmentSlot.CHEST).setRegistryName(new ResourceLocation(MODID, "etherium_chestplate"));
			etheriumLeggings = (EtheriumArmor) new EtheriumArmor(etheriumConfig, EquipmentSlot.LEGS).setRegistryName(new ResourceLocation(MODID, "etherium_leggings"));
			etheriumBoots = (EtheriumArmor) new EtheriumArmor(etheriumConfig, EquipmentSlot.FEET).setRegistryName(new ResourceLocation(MODID, "etherium_boots"));

			etheriumPickaxe = new EtheriumPickaxe(etheriumConfig);
			etheriumAxe = new EtheriumAxe(etheriumConfig);
			etheriumShovel = new EtheriumShovel(etheriumConfig);
			etheriumSword = new EtheriumSword(etheriumConfig);
			etheriumScythe = new EtheriumScythe(etheriumConfig);

			astralDust = new AstralDust();
			loreInscriber = new LoreInscriber();
			loreFragment = new LoreFragment();
			enderRod = new EnderRod(etheriumConfig);

			astralBreaker = new AstralBreaker(etheriumConfig);
			oblivionStone = new OblivionStone();
			enchantmentTransposer = new EnchantmentTransposer();
			curseTransposer = new CurseTransposer();

			//			gemOfBinding = new GemOfBinding();
			//			wormholePotion = new WormholePotion();
			fabulousScroll = new FabulousScroll();
			storageCrystal = new StorageCrystal();
			soulCrystal = new SoulCrystal();

			theAcknowledgment = new TheAcknowledgment();
			overworldRevelationTome = new RevelationTome(Rarity.UNCOMMON, RevelationTome.TomeType.OVERWORLD, "tattered_tome");
			netherRevelationTome = new RevelationTome(Rarity.UNCOMMON, RevelationTome.TomeType.NETHER, "withered_tome");
			endRevelationTome = new RevelationTome(Rarity.RARE, RevelationTome.TomeType.END, "corrupted_tome");

			//			darkHelmet = (DarkArmor) new DarkArmor(EnigmaticArmorMaterials.ETHERIUM, EquipmentSlot.HEAD).setRegistryName(new ResourceLocation(MODID, "dark_helmet"));
			//			darkChestplate = (DarkArmor) new DarkArmor(EnigmaticArmorMaterials.ETHERIUM, EquipmentSlot.CHEST).setRegistryName(new ResourceLocation(MODID, "dark_chestplate"));
			//			darkLeggings = (DarkArmor) new DarkArmor(EnigmaticArmorMaterials.ETHERIUM, EquipmentSlot.LEGS).setRegistryName(new ResourceLocation(MODID, "dark_leggings"));
			//			darkBoots = (DarkArmor) new DarkArmor(EnigmaticArmorMaterials.ETHERIUM, EquipmentSlot.FEET).setRegistryName(new ResourceLocation(MODID, "dark_boots"));

			cursedRing = new CursedRing();
			darkMirror = new DarkMirror();

			//			cryingIngot = new PlaceholderItem("crying_ingot", Rarity.RARE);
			//			cryingHelmet = new PlaceholderItem("crying_helmet", Rarity.RARE);
			//			cryingChestplate = new PlaceholderItem("crying_chestplate", Rarity.RARE);
			//			cryingLeggings = new PlaceholderItem("crying_leggings", Rarity.RARE);
			//			cryingBoots = new PlaceholderItem("crying_boots", Rarity.RARE);
			//			cryingPickaxe = new PlaceholderItem("crying_pickaxe", Rarity.RARE);
			//			cryingAxe = new PlaceholderItem("crying_axe", Rarity.RARE);
			//			cryingSword = new PlaceholderItem("crying_sword", Rarity.RARE);
			//			cryingShovel = new PlaceholderItem("crying_shovel", Rarity.RARE);
			//			cryingHoe = new PlaceholderItem("crying_hoe", Rarity.RARE);

			cursedScroll = new CursedScroll();
			berserkEmblem = new BerserkEmblem();
			guardianHeart = new GuardianHeart();
			theTwist = new TheTwist();
			evilEssence = new EvilEssence();
			evilIngot = new EvilIngot();
			forbiddenFruit = new ForbiddenFruit();
			antiforbiddenPotion = new AntiforbiddenPotion();
			animalGuide = new AnimalGuide();
			hunterGuide = new HunterGuide();
			earthHeart = new EarthHeart();
			twistedCore = new TwistedCore();

			gemRing = new GemRing();
			cursedStone = new CursedStone();
			enchanterPearl = new EnchanterPearl();
			//trueNotchApple = new PlaceholderItem("true_notch_apple", Rarity.EPIC);
			avariceScroll = new AvariceScroll();
			infinimeal = new Infinimeal();
			darkestScroll = new DarkestScroll();
			unwitnessedAmulet = new UnwitnessedAmulet();
			twistedPotion = new TwistedPotion();
			infernalShield = new InfernalShield();
			cosmicHeart = new CosmicHeart();
			abyssalHeart = new AbyssalHeart();
			theInfinitum = new TheInfinitum();
			astralFruit = new AstralFruit();
			enderSlayer = new EnderSlayer();
			theCube = new TheCube();
			desolationRing = new DesolationRing();
			astralPotato = new AstralPotato();
			soulCompass = new SoulCompass();
			ascensionAmulet = new AscensionAmulet();
			eldritchAmulet = new EldritchAmulet();
			//livingFlame = new LivingFlame();
			enigmaticElytra = new EnigmaticElytra();
			architectEye = new ArchitectEye();
			ichorBottle = new IchorBottle();
			quotePlayer = new QuotePlayer();

			spellstoneList = Lists.newArrayList(
					angelBlessing,
					magmaHeart,
					golemHeart,
					oceanStone,
					eyeOfNebula,
					voidPearl,
					theCube,
					enigmaticItem
					);

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
					etheriumNugget,
					etheriumScraps,
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
					evilIngot,
					animalGuide,
					hunterGuide,
					forbiddenFruit,
					antiforbiddenPotion,
					gemRing,
					cursedStone,
					enchanterPearl,
					avariceScroll,
					infinimeal,
					darkestScroll,
					unwitnessedAmulet,
					twistedPotion,
					infernalShield,
					cosmicHeart,
					abyssalHeart,
					theInfinitum,
					astralFruit,
					enderSlayer,
					theCube,
					desolationRing,
					astralPotato,
					curseTransposer,
					soulCompass,
					ascensionAmulet,
					eldritchAmulet,
					//livingFlame,
					enigmaticElytra,
					architectEye,
					ichorBottle,
					quotePlayer,
					new GenericBlockItem(massiveLamp),
					new GenericBlockItem(bigLamp),
					new GenericBlockItem(massiveShroomlamp),
					new GenericBlockItem(bigShroomlamp),
					new GenericBlockItem(massiveRedstonelamp),
					new GenericBlockItem(bigRedstonelamp),
					new CosmicCake(),
					new GenericBlockItem(astralBlock, GenericBlockItem.getDefaultProperties().rarity(Rarity.EPIC).tab(null)),
					new GenericBlockItem(etheriumBlock, GenericBlockItem.getDefaultProperties().rarity(Rarity.RARE))
					//,gemOfBinding,wormholePotion
					);

			if (!EnigmaticLegacy.enigmaticLegacy.isLockboxPresent()) {
				event.getRegistry().register(new PlaceholderItem("cosmic_scroll", Rarity.EPIC, 1));
			}

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
			SWORD_HIT_REJECT = SuperpositionHandler.registerSound("misc.sword_hit_reject");
			UNEAT = SuperpositionHandler.registerSound("misc.uneat");

			Quote.getByID(0);

			logger.info("Sounds registered successfully.");
		}

		@SubscribeEvent
		public static void onRecipeRegister(final RegistryEvent.Register<RecipeSerializer<?>> event) {

		}

		@SubscribeEvent
		public static void registerEffects(final RegistryEvent.Register<MobEffect> event) {
			blazingStrengthEffect = new BlazingStrengthEffect();
			moltenHeartEffect = new MoltenHeartEffect();

			event.getRegistry().register(blazingStrengthEffect);
			event.getRegistry().register(moltenHeartEffect);
		}

		@SubscribeEvent
		public static void registerBrewing(final RegistryEvent.Register<Potion> event) {
			logger.info("Initializing advanced potion system...");

			ULTIMATE_NIGHT_VISION = new AdvancedPotion("ultimate_night_vision", new MobEffectInstance(MobEffects.NIGHT_VISION, 19200));
			ULTIMATE_INVISIBILITY = new AdvancedPotion("ultimate_invisibility", new MobEffectInstance(MobEffects.INVISIBILITY, 19200));
			ULTIMATE_LEAPING = new AdvancedPotion("ultimate_leaping", new MobEffectInstance(MobEffects.JUMP, 9600, 1));
			ULTIMATE_FIRE_RESISTANCE = new AdvancedPotion("ultimate_fire_resistance", new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 19200));
			ULTIMATE_SWIFTNESS = new AdvancedPotion("ultimate_swiftness", new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 9600, 1));
			ULTIMATE_SLOWNESS = new AdvancedPotion("ultimate_slowness", new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1200, 3));
			ULTIMATE_TURTLE_MASTER = new AdvancedPotion("ultimate_turtle_master", new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 800, 5), new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 800, 3));
			ULTIMATE_WATER_BREATHING = new AdvancedPotion("ultimate_water_breathing", new MobEffectInstance(MobEffects.WATER_BREATHING, 19200));
			ULTIMATE_HEALING = new AdvancedPotion("ultimate_healing", new MobEffectInstance(MobEffects.HEAL, 1, 2));
			ULTIMATE_HARMING = new AdvancedPotion("ultimate_harming", new MobEffectInstance(MobEffects.HARM, 1, 2));
			ULTIMATE_POISON = new AdvancedPotion("ultimate_poison", new MobEffectInstance(MobEffects.POISON, 1800, 1));
			ULTIMATE_REGENERATION = new AdvancedPotion("ultimate_regeneration", new MobEffectInstance(MobEffects.REGENERATION, 1800, 1));
			ULTIMATE_STRENGTH = new AdvancedPotion("ultimate_strength", new MobEffectInstance(MobEffects.DAMAGE_BOOST, 9600, 1));
			ULTIMATE_WEAKNESS = new AdvancedPotion("ultimate_weakness", new MobEffectInstance(MobEffects.WEAKNESS, 9600));
			ULTIMATE_SLOW_FALLING = new AdvancedPotion("ultimate_slow_falling", new MobEffectInstance(MobEffects.SLOW_FALLING, 9600));

			HASTE = new AdvancedPotion("haste", new MobEffectInstance(MobEffects.DIG_SPEED, 3600));
			LONG_HASTE = new AdvancedPotion("long_haste", new MobEffectInstance(MobEffects.DIG_SPEED, 9600));
			STRONG_HASTE = new AdvancedPotion("strong_haste", new MobEffectInstance(MobEffects.DIG_SPEED, 1800, 1));
			ULTIMATE_HASTE = new AdvancedPotion("ultimate_haste", new MobEffectInstance(MobEffects.DIG_SPEED, 9600, 1));

			MOLTEN_HEART = new AdvancedPotion("molten_heart", new MobEffectInstance(moltenHeartEffect, 3600));
			LONG_MOLTEN_HEART = new AdvancedPotion("long_molten_heart", new MobEffectInstance(moltenHeartEffect, 9600));
			ULTIMATE_MOLTEN_HEART = new AdvancedPotion("ultimate_molten_heart", new MobEffectInstance(moltenHeartEffect, 19200));

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

			commonPotionTypes.add(MOLTEN_HEART);
			commonPotionTypes.add(LONG_MOLTEN_HEART);
			ultimatePotionTypes.add(ULTIMATE_MOLTEN_HEART);

			logger.info("Advanced potion system initialized successfully.");
		}

		@SubscribeEvent
		public static void registerEnchantments(final RegistryEvent.Register<Enchantment> event) {
			final IForgeRegistry<Enchantment> registry = event.getRegistry();

			sharpshooterEnchantment = new SharpshooterEnchantment(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
			ceaselessEnchantment = new CeaselessEnchantment(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
			nemesisCurse = new NemesisCurse(EquipmentSlot.MAINHAND);
			torrentEnchantment = new TorrentEnchantment(EquipmentSlot.MAINHAND);
			wrathEnchantment = new WrathEnchantment(EquipmentSlot.MAINHAND);
			slayerEnchantment = new SlayerEnchantment(EquipmentSlot.MAINHAND);
			eternalBindingCurse = new EternalBindingCurse(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);
			sorrowCurse = new SorrowCurse(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);

			registry.registerAll(
					sharpshooterEnchantment,
					ceaselessEnchantment,
					nemesisCurse,
					torrentEnchantment,
					wrathEnchantment,
					slayerEnchantment,
					eternalBindingCurse,
					sorrowCurse
					);
		}

		@SubscribeEvent
		public static void registerLootModifiers(final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
			IForgeRegistry<GlobalLootModifierSerializer<?>> registry = event.getRegistry();

			registry.registerAll(new SpecialLootModifier.Serializer().setRegistryName(new ResourceLocation(MODID, "special_loot_modifier")));
		}

		@SubscribeEvent
		public static void onEntitiesRegistry(final RegistryEvent.Register<EntityType<?>> event) {
			logger.info("Initializing entities registration...");

			event.getRegistry().register(EntityType.Builder.<PermanentItemEntity>of(PermanentItemEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(64).setCustomClientFactory((spawnEntity, world) -> new PermanentItemEntity(PermanentItemEntity.TYPE, world)).setUpdateInterval(2).setShouldReceiveVelocityUpdates(true).build(MODID+":permanent_item_entity").setRegistryName(new ResourceLocation(MODID, "permanent_item_entity")));

			event.getRegistry().register(EntityType.Builder.<EnigmaticPotionEntity>of(EnigmaticPotionEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(64).setCustomClientFactory((spawnEntity, world) -> new EnigmaticPotionEntity(EnigmaticPotionEntity.TYPE, world)).setUpdateInterval(10).setShouldReceiveVelocityUpdates(true).build(MODID+":enigmatic_potion_entity").setRegistryName(new ResourceLocation(MODID, "enigmatic_potion_entity")));

			event.getRegistry().register(EntityType.Builder.<UltimateWitherSkullEntity>of(UltimateWitherSkullEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(64).setCustomClientFactory((spawnEntity, world) -> new UltimateWitherSkullEntity(UltimateWitherSkullEntity.TYPE, world))
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

	public static final CreativeModeTab enigmaticTab = new CreativeModeTab("enigmaticCreativeTab") {
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack makeIcon() {
			return new ItemStack(enigmaticItem);
		}
	};

	public static final CreativeModeTab enigmaticPotionTab = new CreativeModeTab("enigmaticPotionCreativeTab") {
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack makeIcon() {
			return new ItemStack(recallPotion);
		}
	};

	public static final Rarity LEGENDARY = Rarity.create("legendary", ChatFormatting.GOLD);

}
