package com.aizistral.enigmaticlegacy.registries;

import java.util.ArrayList;
import java.util.List;

import com.aizistral.enigmaticlegacy.api.generic.ConfigurableItem;
import com.aizistral.enigmaticlegacy.api.generic.ModRegistry;
import com.aizistral.enigmaticlegacy.api.items.IAdvancedPotionItem.PotionType;
import com.aizistral.enigmaticlegacy.items.*;
import com.aizistral.enigmaticlegacy.items.generic.GenericBlockItem;
import com.aizistral.etherium.items.*;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegisterEvent;

public class EnigmaticItems extends AbstractRegistry<Item> {
	private static final EnigmaticItems INSTANCE = new EnigmaticItems();
	public static final List<Item> SPELLSTONES = new ArrayList<>();

	@ConfigurableItem
	@ObjectHolder(value = MODID + ":enigmatic_item", registryName = "item")
	public static final EnigmaticItem ENIGMATIC_ITEM = null;

	@ConfigurableItem("Scroll of Ageless Wisdom")
	@ObjectHolder(value = MODID + ":xp_scroll", registryName = "item")
	public static final XPScroll XP_SCROLL = null;

	@ConfigurableItem("Enigmatic Amulet")
	@ObjectHolder(value = MODID + ":enigmatic_amulet", registryName = "item")
	public static final EnigmaticAmulet ENIGMATIC_AMULET = null;

	@ConfigurableItem("Magnet Ring")
	@ObjectHolder(value = MODID + ":magnet_ring", registryName = "item")
	public static final MagnetRing MAGNET_RING = null;

	@ConfigurableItem("Extradimensional Eye")
	@ObjectHolder(value = MODID + ":extradimensional_eye", registryName = "item")
	public static final ExtradimensionalEye EXTRADIMENSIONAL_EYE = null;

	@ConfigurableItem
	@ObjectHolder(value = MODID + ":relic_of_testing", registryName = "item")
	public static final RelicOfTesting RELIC_OF_TESTING = null;

	@ConfigurableItem("Potion of Recall")
	@ObjectHolder(value = MODID + ":recall_potion", registryName = "item")
	public static final RecallPotion RECALL_POTION = null;

	@ConfigurableItem("Axe of Executioner")
	@ObjectHolder(value = MODID + ":forbidden_axe", registryName = "item")
	public static final ForbiddenAxe FORBIDDEN_AXE = null;

	@ConfigurableItem("Scroll of Postmortal Recall")
	@ObjectHolder(value = MODID + ":escape_scroll", registryName = "item")
	public static final EscapeScroll ESCAPE_SCROLL = null;

	@ConfigurableItem("Gift of the Heaven")
	@ObjectHolder(value = MODID + ":heaven_scroll", registryName = "item")
	public static final HeavenScroll HEAVEN_SCROLL = null;

	@ConfigurableItem("Ring of Dislocation")
	@ObjectHolder(value = MODID + ":super_magnet_ring", registryName = "item")
	public static final SuperMagnetRing SUPER_MAGNET_RING = null;

	@ConfigurableItem("Heart of the Golem")
	@ObjectHolder(value = MODID + ":golem_heart", registryName = "item")
	public static final GolemHeart GOLEM_HEART = null;

	@ConfigurableItem("Megasponge")
	@ObjectHolder(value = MODID + ":mega_sponge", registryName = "item")
	public static final MegaSponge MEGA_SPONGE = null;

	@ConfigurableItem("Unholy Grail")
	@ObjectHolder(value = MODID + ":unholy_grail", registryName = "item")
	public static final UnholyGrail UNHOLY_GRAIL = null;

	@ConfigurableItem("Eye of Nebula")
	@ObjectHolder(value = MODID + ":eye_of_nebula", registryName = "item")
	public static final EyeOfNebula EYE_OF_NEBULA = null;

	@ConfigurableItem("Blazing Core")
	@ObjectHolder(value = MODID + ":blazing_core", registryName = "item")
	public static final BlazingCore BLAZING_CORE = null;

	@ConfigurableItem("Pearl of the Void")
	@ObjectHolder(value = MODID + ":void_pearl", registryName = "item")
	public static final VoidPearl VOID_PEARL = null;

	@ConfigurableItem("Will of the Ocean")
	@ObjectHolder(value = MODID + ":ocean_stone", registryName = "item")
	public static final OceanStone OCEAN_STONE = null;

	@ConfigurableItem("Angel's Blessing")
	@ObjectHolder(value = MODID + ":angel_blessing", registryName = "item")
	public static final AngelBlessing ANGEL_BLESSING = null;

	@ConfigurableItem("Emblem of Monster Slayer")
	@ObjectHolder(value = MODID + ":monster_charm", registryName = "item")
	public static final MonsterCharm MONSTER_CHARM = null;

	@ConfigurableItem("Charm of Treasure Hunter")
	@ObjectHolder(value = MODID + ":mining_charm", registryName = "item")
	public static final MiningCharm MINING_CHARM = null;

	@ConfigurableItem("Ring of Ender")
	@ObjectHolder(value = MODID + ":ender_ring", registryName = "item")
	public static final EnderRing ENDER_RING = null;

	@ConfigurableItem("Mending Mixture")
	@ObjectHolder(value = MODID + ":mending_mixture", registryName = "item")
	public static final MendingMixture MENDING_MIXTURE = null;

	@ConfigurableItem
	@ObjectHolder(value = MODID + ":loot_generator", registryName = "item")
	public static final LootGenerator LOOT_GENERATOR = null;

	@ConfigurableItem("Blank Scroll")
	@ObjectHolder(value = MODID + ":thicc_scroll", registryName = "item")
	public static final ThiccScroll THICC_SCROLL = null;

	@ConfigurableItem("Iron Ring")
	@ObjectHolder(value = MODID + ":iron_ring", registryName = "item")
	public static final IronRing IRON_RING = null;

	@ConfigurableItem("Etherium Ore")
	@ObjectHolder(value = MODID + ":etherium_ore", registryName = "item")
	public static final EtheriumOre ETHERIUM_ORE = null;

	@ConfigurableItem("Etherium Ingot")
	@ObjectHolder(value = MODID + ":etherium_ingot", registryName = "item")
	public static final EtheriumIngot ETHERIUM_INGOT = null;

	@ConfigurableItem("Etherium Nugget")
	@ObjectHolder(value = MODID + ":etherium_nugget", registryName = "item")
	public static final EtheriumNugget ETHERIUM_NUGGET = null;

	@ConfigurableItem("Etherium Scraps")
	@ObjectHolder(value = MODID + ":etherium_scraps", registryName = "item")
	public static final EtheriumScraps ETHERIUM_SCRAPS = null;

	@ConfigurableItem("Ultimate Potions")
	@ObjectHolder(value = MODID + ":ultimate_potion", registryName = "item")
	public static final UltimatePotionBase ULTIMATE_POTION = null;

	@ConfigurableItem("Ultimate Potions")
	@ObjectHolder(value = MODID + ":ultimate_potion_splash", registryName = "item")
	public static final UltimatePotionSplash ULTIMATE_POTION_SPLASH = null;

	@ConfigurableItem("Ultimate Potions")
	@ObjectHolder(value = MODID + ":ultimate_potion_lingering", registryName = "item")
	public static final UltimatePotionLingering ULTIMATE_POTION_LINGERING = null;

	@ConfigurableItem("Common Potions")
	@ObjectHolder(value = MODID + ":common_potion", registryName = "item")
	public static final UltimatePotionBase COMMON_POTION = null;

	@ConfigurableItem("Common Potions")
	@ObjectHolder(value = MODID + ":common_potion_splash", registryName = "item")
	public static final UltimatePotionSplash COMMON_POTION_SPLASH = null;

	@ConfigurableItem("Common Potions")
	@ObjectHolder(value = MODID + ":common_potion_lingering", registryName = "item")
	public static final UltimatePotionLingering COMMON_POTION_LINGERING = null;

	@ConfigurableItem("Etherium Armor")
	@ObjectHolder(value = MODID + ":etherium_helmet", registryName = "item")
	public static final EtheriumArmor ETHERIUM_HELMET = null;

	@ConfigurableItem("Etherium Armor")
	@ObjectHolder(value = MODID + ":etherium_chestplate", registryName = "item")
	public static final EtheriumArmor ETHERIUM_CHESTPLATE = null;

	@ConfigurableItem("Etherium Armor")
	@ObjectHolder(value = MODID + ":etherium_leggings", registryName = "item")
	public static final EtheriumArmor ETHERIUM_LEGGINGS = null;

	@ConfigurableItem("Etherium Armor")
	@ObjectHolder(value = MODID + ":etherium_boots", registryName = "item")
	public static final EtheriumArmor ETHERIUM_BOOTS = null;

	@ConfigurableItem("Etherium Pickaxe")
	@ObjectHolder(value = MODID + ":etherium_pickaxe", registryName = "item")
	public static final EtheriumPickaxe ETHERIUM_PICKAXE = null;

	@ConfigurableItem("Etherium Waraxe")
	@ObjectHolder(value = MODID + ":etherium_axe", registryName = "item")
	public static final EtheriumAxe ETHERIUM_AXE = null;

	@ConfigurableItem("Etherium Shovel")
	@ObjectHolder(value = MODID + ":etherium_shovel", registryName = "item")
	public static final EtheriumShovel ETHERIUM_SHOVEL = null;

	@ConfigurableItem("Etherium Broadsword")
	@ObjectHolder(value = MODID + ":etherium_sword", registryName = "item")
	public static final EtheriumSword ETHERIUM_SWORD = null;

	@ConfigurableItem("Etherium Scythe")
	@ObjectHolder(value = MODID + ":etherium_scythe", registryName = "item")
	public static final EtheriumScythe ETHERIUM_SCYTHE = null;

	@ConfigurableItem("Astral Dust")
	@ObjectHolder(value = MODID + ":astral_dust", registryName = "item")
	public static final AstralDust ASTRAL_DUST = null;

	@ConfigurableItem("The Architect's Inkwell")
	@ObjectHolder(value = MODID + ":lore_inscriber", registryName = "item")
	public static final LoreInscriber LORE_INSCRIBER = null;

	@ConfigurableItem("Lore Fragment")
	@ObjectHolder(value = MODID + ":lore_fragment", registryName = "item")
	public static final LoreFragment LORE_FRAGMENT = null;

	@ConfigurableItem("Ender Rod")
	@ObjectHolder(value = MODID + ":ender_rod", registryName = "item")
	public static final EnderRod ENDER_ROD = null;

	@ConfigurableItem("Astral Breaker")
	@ObjectHolder(value = MODID + ":astral_breaker", registryName = "item")
	public static final AstralBreaker ASTRAL_BREAKER = null;

	@ConfigurableItem("Keystone of The Oblivion")
	@ObjectHolder(value = MODID + ":void_stone", registryName = "item")
	public static final OblivionStone VOID_STONE = null;

	@ConfigurableItem("Tome of Hungering Knowledge")
	@ObjectHolder(value = MODID + ":enchantment_transposer", registryName = "item")
	public static final EnchantmentTransposer ENCHANTMENT_TRANSPOSER = null;

	@ConfigurableItem("Tome of Devoured Malignancy")
	@ObjectHolder(value = MODID + ":curse_transposer", registryName = "item")
	public static final CurseTransposer CURSE_TRANSPOSER = null;

	@ConfigurableItem("Grace of the Creator")
	@ObjectHolder(value = MODID + ":fabulous_scroll", registryName = "item")
	public static final FabulousScroll FABULOUS_SCROLL = null;

	@ConfigurableItem
	@ObjectHolder(value = MODID + ":storage_crystal", registryName = "item")
	public static final StorageCrystal STORAGE_CRYSTAL = null;

	@ConfigurableItem
	@ObjectHolder(value = MODID + ":soul_crystal", registryName = "item")
	public static final SoulCrystal SOUL_CRYSTAL = null;

	@ConfigurableItem("The Acknowledgment")
	@ObjectHolder(value = MODID + ":the_acknowledgment", registryName = "item")
	public static final TheAcknowledgment THE_ACKNOWLEDGMENT = null;

	@ConfigurableItem("Tattered Tome")
	@ObjectHolder(value = MODID + ":tattered_tome", registryName = "item")
	public static final RevelationTome TATTERED_TOME = null;

	@ConfigurableItem("Withered Tome")
	@ObjectHolder(value = MODID + ":withered_tome", registryName = "item")
	public static final RevelationTome WITHERED_TOME = null;

	@ConfigurableItem("Corrupted Tome")
	@ObjectHolder(value = MODID + ":corrupted_tome", registryName = "item")
	public static final RevelationTome CORRUPTED_TOME = null;

	@ConfigurableItem("Ring of the Seven Curses")
	@ObjectHolder(value = MODID + ":cursed_ring", registryName = "item")
	public static final CursedRing CURSED_RING = null;

	@ConfigurableItem("Twisted Mirror")
	@ObjectHolder(value = MODID + ":twisted_mirror", registryName = "item")
	public static final TwistedMirror TWISTED_MIRROR = null;

	@ConfigurableItem("Scroll of a Thousand Curses")
	@ObjectHolder(value = MODID + ":cursed_scroll", registryName = "item")
	public static final CursedScroll CURSED_SCROLL = null;

	@ConfigurableItem("Emblem of Bloodstained Valor")
	@ObjectHolder(value = MODID + ":berserk_charm", registryName = "item")
	public static final BerserkEmblem BERSERK_CHARM = null;

	@ConfigurableItem("Heart of the Earth")
	@ObjectHolder(value = MODID + ":earth_heart", registryName = "item")
	public static final EarthHeart EARTH_HEART = null;

	@ConfigurableItem("Twisted Heart")
	@ObjectHolder(value = MODID + ":twisted_heart", registryName = "item")
	public static final TwistedHeart TWISTED_HEART = null;

	@ConfigurableItem("Heart of the Guardian")
	@ObjectHolder(value = MODID + ":guardian_heart", registryName = "item")
	public static final GuardianHeart GUARDIAN_HEART = null;

	@ConfigurableItem("The Twist")
	@ObjectHolder(value = MODID + ":the_twist", registryName = "item")
	public static final TheTwist THE_TWIST = null;

	@ConfigurableItem("Nefarious Essence")
	@ObjectHolder(value = MODID + ":evil_essence", registryName = "item")
	public static final EvilEssence EVIL_ESSENCE = null;

	@ConfigurableItem("Nefarious Ingot")
	@ObjectHolder(value = MODID + ":evil_ingot", registryName = "item")
	public static final EvilIngot EVIL_INGOT = null;

	@ConfigurableItem("Guite to Animal Companionship")
	@ObjectHolder(value = MODID + ":animal_guidebook", registryName = "item")
	public static final PetGuidebook ANIMAL_GUIDEBOOK = null;

	@ConfigurableItem("Guide to Feral Hunt")
	@ObjectHolder(value = MODID + ":hunter_guidebook", registryName = "item")
	public static final HunterGuidebook HUNTER_GUIDEBOOK = null;

	@ConfigurableItem("Forbidden Fruit")
	@ObjectHolder(value = MODID + ":forbidden_fruit", registryName = "item")
	public static final ForbiddenFruit FORBIDDEN_FRUIT = null;

	@ConfigurableItem
	@ObjectHolder(value = MODID + ":redemption_potion", registryName = "item")
	public static final RedemptionPotion REDEMPTION_POTION = null;

	@ConfigurableItem("Exquisite Ring")
	@ObjectHolder(value = MODID + ":golden_ring", registryName = "item")
	public static final GoldenRing GOLDEN_RING = null;

	@ConfigurableItem("Unholy Stone")
	@ObjectHolder(value = MODID + ":cursed_stone", registryName = "item")
	public static final CursedStone CURSED_STONE = null;

	@ConfigurableItem("Enchanter's Pearl")
	@ObjectHolder(value = MODID + ":enchanter_pearl", registryName = "item")
	public static final EnchanterPearl ENCHANTER_PEARL = null;

	@ConfigurableItem("Pact of Infinite Avarice")
	@ObjectHolder(value = MODID + ":avarice_scroll", registryName = "item")
	public static final AvariceScroll AVARICE_SCROLL = null;

	@ConfigurableItem("Essence of Raging Life")
	@ObjectHolder(value = MODID + ":infinimeal", registryName = "item")
	public static final Infinimeal INFINIMEAL = null;

	@ConfigurableItem("Darkest Scroll")
	@ObjectHolder(value = MODID + ":darkest_scroll", registryName = "item")
	public static final DarkestScroll DARKEST_SCROLL = null;

	@ConfigurableItem
	@ObjectHolder(value = MODID + ":unwitnessed_amulet", registryName = "item")
	public static final UnwitnessedAmulet UNWITNESSED_AMULET = null;

	@ConfigurableItem("Potion of Twisted Mercy")
	@ObjectHolder(value = MODID + ":twisted_potion", registryName = "item")
	public static final TwistedPotion TWISTED_POTION = null;

	@ConfigurableItem("Bulwark of Blazing Pride")
	@ObjectHolder(value = MODID + ":infernal_shield", registryName = "item")
	public static final InfernalShield INFERNAL_SHIELD = null;

	@ConfigurableItem("Heart of the Cosmos")
	@ObjectHolder(value = MODID + ":cosmic_heart", registryName = "item")
	public static final CosmicHeart COSMIC_HEART = null;

	@ConfigurableItem("Heart of the Abyss")
	@ObjectHolder(value = MODID + ":abyssal_heart", registryName = "item")
	public static final AbyssalHeart ABYSSAL_HEART = null;

	@ConfigurableItem("The Infinitum")
	@ObjectHolder(value = MODID + ":the_infinitum", registryName = "item")
	public static final TheInfinitum THE_INFINITUM = null;

	@ConfigurableItem("Celestial Fruit")
	@ObjectHolder(value = MODID + ":astral_fruit", registryName = "item")
	public static final AstralFruit ASTRAL_FRUIT = null;

	@ConfigurableItem("The Ender Slayer")
	@ObjectHolder(value = MODID + ":ender_slayer", registryName = "item")
	public static final EnderSlayer ENDER_SLAYER = null;

	@ConfigurableItem("Non-Euclidean Cube")
	@ObjectHolder(value = MODID + ":the_cube", registryName = "item")
	public static final TheCube THE_CUBE = null;

	@ConfigurableItem("The Burden of Desolation")
	@ObjectHolder(value = MODID + ":desolation_ring", registryName = "item")
	public static final DesolationRing DESOLATION_RING = null;

	@ConfigurableItem("Astral Potato")
	@ObjectHolder(value = MODID + ":astral_potato", registryName = "item")
	public static final AstralPotato ASTRAL_POTATO = null;

	@ConfigurableItem("Wayfinder of the Damned")
	@ObjectHolder(value = MODID + ":soul_compass", registryName = "item")
	public static final SoulCompass SOUL_COMPASS = null;

	@ConfigurableItem("Amulet of Ascension")
	@ObjectHolder(value = MODID + ":ascension_amulet", registryName = "item")
	public static final AscensionAmulet ASCENSION_AMULET = null;

	@ConfigurableItem("The Testament of Contempt")
	@ObjectHolder(value = MODID + ":eldritch_amulet", registryName = "item")
	public static final EldritchAmulet ELDRITCH_AMULET = null;

	@ConfigurableItem("Majestic Elytra")
	@ObjectHolder(value = MODID + ":enigmatic_elytra", registryName = "item")
	public static final EnigmaticElytra ENIGMATIC_ELYTRA = null;

	@ConfigurableItem("Inscrutable Eye")
	@ObjectHolder(value = MODID + ":enigmatic_eye", registryName = "item")
	public static final EnigmaticEye ENIGMATIC_EYE = null;

	@ConfigurableItem("Bottle of Ichor")
	@ObjectHolder(value = MODID + ":ichor_bottle", registryName = "item")
	public static final IchorBottle ICHOR_BOTTLE = null;

	@ConfigurableItem
	@ObjectHolder(value = MODID + ":quote_player", registryName = "item")
	public static final QuotePlayer QUOTE_PLAYER = null;

	@ConfigurableItem("Charming Insignia")
	@ObjectHolder(value = MODID + ":insignia", registryName = "item")
	public static final Insignia INSIGNIA = null;

	@ConfigurableItem("Cosmic Scroll")
	@ObjectHolder(value = MODID + ":cosmic_scroll", registryName = "item")
	public static final Item COSMIC_SCROLL = null;

	@ConfigurableItem("Deception Amulet")
	@ObjectHolder(value = MODID + ":deception_amulet", registryName = "item")
	public static final Item DECEPTION_AMULET = null;

	@ConfigurableItem("The Judgement")
	@ObjectHolder(value = MODID + ":the_judgement", registryName = "item")
	public static final Item THE_JUDGEMENT = null;

	@ConfigurableItem("Soul Dust")
	@ObjectHolder(value = MODID + ":soul_dust", registryName = "item")
	public static final Item SOUL_DUST = null;

	@ConfigurableItem("Eldritch Frying Pan")
	@ObjectHolder(value = MODID + ":eldritch_pan", registryName = "item")
	public static final Item ELDRITCH_PAN = null;

	private EnigmaticItems() {
		super(ForgeRegistries.ITEMS);
		this.register("enigmatic_item", EnigmaticItem::new);
		this.register("xp_scroll", XPScroll::new);
		this.register("enigmatic_amulet", EnigmaticAmulet::new);
		this.register("magnet_ring", MagnetRing::new);
		this.register("extradimensional_eye", ExtradimensionalEye::new);
		this.register("relic_of_testing", RelicOfTesting::new);
		this.register("recall_potion", RecallPotion::new);
		this.register("forbidden_axe", ForbiddenAxe::new);
		this.register("escape_scroll", EscapeScroll::new);
		this.register("heaven_scroll", HeavenScroll::new);
		this.register("super_magnet_ring", SuperMagnetRing::new);
		this.register("golem_heart", GolemHeart::new);
		this.register("mega_sponge", MegaSponge::new);
		this.register("unholy_grail", UnholyGrail::new);
		this.register("eye_of_nebula", EyeOfNebula::new);
		this.register("blazing_core", BlazingCore::new);
		this.register("void_pearl", VoidPearl::new);
		this.register("ocean_stone", OceanStone::new);
		this.register("angel_blessing", AngelBlessing::new);
		this.register("monster_charm", MonsterCharm::new);
		this.register("mining_charm", MiningCharm::new);
		this.register("ender_ring", EnderRing::new);
		this.register("mending_mixture", MendingMixture::new);
		this.register("loot_generator", LootGenerator::new);
		this.register("thicc_scroll", ThiccScroll::new);
		this.register("iron_ring", IronRing::new);
		this.register("etherium_ore", EtheriumOre::new);
		this.register("etherium_ingot", EtheriumIngot::new);
		this.register("etherium_nugget", EtheriumNugget::new);
		this.register("etherium_scraps", EtheriumScraps::new);
		this.register("etherium_pickaxe", EtheriumPickaxe::new);
		this.register("etherium_axe", EtheriumAxe::new);
		this.register("etherium_shovel", EtheriumShovel::new);
		this.register("etherium_sword", EtheriumSword::new);
		this.register("etherium_scythe", EtheriumScythe::new);
		this.register("etherium_helmet", () -> new EtheriumArmor(ArmorItem.Type.HELMET));
		this.register("etherium_chestplate", () -> new EtheriumArmor(ArmorItem.Type.CHESTPLATE));
		this.register("etherium_leggings", () -> new EtheriumArmor(ArmorItem.Type.LEGGINGS));
		this.register("etherium_boots", () -> new EtheriumArmor(ArmorItem.Type.BOOTS));
		this.register("ender_rod", EnderRod::new);
		this.register("astral_dust", AstralDust::new);
		this.register("lore_inscriber", LoreInscriber::new);
		this.register("lore_fragment", LoreFragment::new);
		this.register("astral_breaker", AstralBreaker::new);
		this.register("void_stone", OblivionStone::new);
		this.register("enchantment_transposer", EnchantmentTransposer::new);
		this.register("curse_transposer", CurseTransposer::new);
		this.register("fabulous_scroll", FabulousScroll::new);
		this.register("storage_crystal", StorageCrystal::new);
		this.register("soul_crystal", SoulCrystal::new);
		this.register("the_acknowledgment", TheAcknowledgment::new);
		this.register("tattered_tome", () -> new RevelationTome(Rarity.UNCOMMON, RevelationTome.TomeType.OVERWORLD));
		this.register("withered_tome", () -> new RevelationTome(Rarity.UNCOMMON, RevelationTome.TomeType.NETHER));
		this.register("corrupted_tome", () -> new RevelationTome(Rarity.RARE, RevelationTome.TomeType.END));
		this.register("cursed_ring", CursedRing::new);
		this.register("twisted_mirror", TwistedMirror::new);
		this.register("cursed_scroll", CursedScroll::new);
		this.register("avarice_scroll", AvariceScroll::new);
		this.register("berserk_charm", BerserkEmblem::new);
		this.register("guardian_heart", GuardianHeart::new);
		this.register("the_twist", TheTwist::new);
		this.register("evil_essence", EvilEssence::new);
		this.register("evil_ingot", EvilIngot::new);
		this.register("forbidden_fruit", ForbiddenFruit::new);
		this.register("redemption_potion", RedemptionPotion::new);
		this.register("animal_guidebook", PetGuidebook::new);
		this.register("hunter_guidebook", HunterGuidebook::new);
		this.register("earth_heart", EarthHeart::new);
		this.register("twisted_heart", TwistedHeart::new);
		this.register("golden_ring", GoldenRing::new);
		this.register("cursed_stone", CursedStone::new);
		this.register("enchanter_pearl", EnchanterPearl::new);
		this.register("infinimeal", Infinimeal::new);
		this.register("darkest_scroll", DarkestScroll::new);
		this.register("unwitnessed_amulet", UnwitnessedAmulet::new);
		this.register("twisted_potion", TwistedPotion::new);
		this.register("infernal_shield", InfernalShield::new);
		this.register("cosmic_heart", CosmicHeart::new);
		this.register("abyssal_heart", AbyssalHeart::new);
		this.register("the_infinitum", TheInfinitum::new);
		this.register("astral_fruit", AstralFruit::new);
		this.register("ender_slayer", EnderSlayer::new);
		this.register("the_cube", TheCube::new);
		this.register("desolation_ring", DesolationRing::new);
		this.register("astral_potato", AstralPotato::new);
		this.register("soul_compass", SoulCompass::new);
		this.register("ascension_amulet", AscensionAmulet::new);
		this.register("eldritch_amulet", EldritchAmulet::new);
		this.register("enigmatic_elytra", EnigmaticElytra::new);
		this.register("enigmatic_eye", EnigmaticEye::new);
		this.register("ichor_bottle", IchorBottle::new);
		this.register("quote_player", QuotePlayer::new);
		this.register("insignia", Insignia::new);
		this.register("cosmic_scroll", CosmicScroll::new);
		this.register("deception_amulet", DeceptionAmulet::new);
		this.register("the_judgement", TheJudgement::new);
		this.register("soul_dust", SoulDust::new);
		this.register("eldritch_pan", EldritchPan::new);

		this.register("common_potion", () ->  new UltimatePotionBase(Rarity.COMMON, PotionType.COMMON));
		this.register("common_potion_splash", () ->  new UltimatePotionSplash(Rarity.COMMON, PotionType.COMMON));
		this.register("common_potion_lingering", () ->  new UltimatePotionLingering(Rarity.COMMON, PotionType.COMMON));
		this.register("ultimate_potion", () ->  new UltimatePotionBase(Rarity.RARE, PotionType.ULTIMATE));
		this.register("ultimate_potion_splash", () ->  new UltimatePotionSplash(Rarity.RARE, PotionType.ULTIMATE));
		this.register("ultimate_potion_lingering", () ->  new UltimatePotionLingering(Rarity.RARE, PotionType.ULTIMATE));
	}

	@Override
	protected void onRegister(RegisterEvent event) {
		EnigmaticBlocks.getBlockItemMap().forEach((block, item) ->
		event.register(ForgeRegistries.Keys.ITEMS, block, () ->
		item.apply(ForgeRegistries.BLOCKS.getValue(block))));
	}

}
