package com.aizistral.enigmaticlegacy.registries;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.generic.ConfigurableItem;
import com.aizistral.enigmaticlegacy.api.items.IAdvancedPotionItem.PotionType;
import com.aizistral.enigmaticlegacy.items.*;
import com.aizistral.etherium.items.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = EnigmaticLegacy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnigmaticItems {
	private static final DeferredRegister<Item> ITEMS =
			DeferredRegister.create(ForgeRegistries.ITEMS, EnigmaticLegacy.MODID);

	public static final List<Item> SPELLSTONES = new ArrayList<>();

	@ConfigurableItem
	public static final RegistryObject<EnigmaticItem> ENIGMATIC_ITEM = register("enigmatic_item", EnigmaticItem::new);

	@ConfigurableItem("Scroll of Ageless Wisdom")
	public static final RegistryObject<XPScroll> XP_SCROLL = register("xp_scroll", XPScroll::new);

	@ConfigurableItem("Enigmatic Amulet")
	public static final RegistryObject<EnigmaticAmulet> ENIGMATIC_AMULET = register("enigmatic_amulet", EnigmaticAmulet::new);

	@ConfigurableItem("Magnet Ring")
	public static final RegistryObject<MagnetRing> MAGNET_RING = register("magnet_ring", MagnetRing::new);

	@ConfigurableItem("Extradimensional Eye")
	public static final RegistryObject<ExtradimensionalEye> EXTRADIMENSIONAL_EYE = register("extradimensional_eye", ExtradimensionalEye::new);

	@ConfigurableItem
	public static final RegistryObject<RelicOfTesting> RELIC_OF_TESTING = register("relic_of_testing", RelicOfTesting::new);

	@ConfigurableItem("Potion of Recall")
	public static final RegistryObject<RecallPotion> RECALL_POTION = register("recall_potion", RecallPotion::new);

	@ConfigurableItem("Axe of Executioner")
	public static final RegistryObject<ForbiddenAxe> FORBIDDEN_AXE = register("forbidden_axe", ForbiddenAxe::new);

	@ConfigurableItem("Scroll of Postmortal Recall")
	public static final RegistryObject<EscapeScroll> ESCAPE_SCROLL = register("escape_scroll", EscapeScroll::new);

	@ConfigurableItem("Gift of the Heaven")
	public static final RegistryObject<HeavenScroll> HEAVEN_SCROLL = register("heaven_scroll", HeavenScroll::new);

	@ConfigurableItem("Ring of Dislocation")
	public static final RegistryObject<SuperMagnetRing> SUPER_MAGNET_RING = register("super_magnet_ring", SuperMagnetRing::new);

	@ConfigurableItem("Heart of the Golem")
	public static final RegistryObject<GolemHeart> GOLEM_HEART = register("golem_heart", GolemHeart::new);

	@ConfigurableItem("Megasponge")
	public static final RegistryObject<MegaSponge> MEGA_SPONGE = register("mega_sponge", MegaSponge::new);

	@ConfigurableItem("Unholy Grail")
	public static final RegistryObject<UnholyGrail> UNHOLY_GRAIL = register("unholy_grail", UnholyGrail::new);

	@ConfigurableItem("Eye of Nebula")
	public static final RegistryObject<EyeOfNebula> EYE_OF_NEBULA = register("eye_of_nebula", EyeOfNebula::new);

	@ConfigurableItem("Blazing Core")
	public static final RegistryObject<BlazingCore> BLAZING_CORE = register("blazing_core", BlazingCore::new);

	@ConfigurableItem("Pearl of the Void")
	public static final RegistryObject<VoidPearl> VOID_PEARL = register("void_pearl", VoidPearl::new);

	@ConfigurableItem("Will of the Ocean")
	public static final RegistryObject<OceanStone> OCEAN_STONE = register("ocean_stone", OceanStone::new);

	@ConfigurableItem("Angel's Blessing")
	public static final RegistryObject<AngelBlessing> ANGEL_BLESSING = register("angel_blessing", AngelBlessing::new);

	@ConfigurableItem("Emblem of Monster Slayer")
	public static final RegistryObject<MonsterCharm> MONSTER_CHARM = register("monster_charm", MonsterCharm::new);

	@ConfigurableItem("Charm of Treasure Hunter")
	public static final RegistryObject<MiningCharm> MINING_CHARM = register("mining_charm", MiningCharm::new);

	@ConfigurableItem("Ring of Ender")
	public static final RegistryObject<EnderRing> ENDER_RING = register("ender_ring", EnderRing::new);

	@ConfigurableItem("Mending Mixture")
	public static final RegistryObject<MendingMixture> MENDING_MIXTURE = register("mending_mixture", MendingMixture::new);

	@ConfigurableItem
	public static final RegistryObject<LootGenerator> LOOT_GENERATOR = register("loot_generator", LootGenerator::new);

	@ConfigurableItem("Blank Scroll")
	public static final RegistryObject<ThiccScroll> THICC_SCROLL = register("thicc_scroll", ThiccScroll::new);

	@ConfigurableItem("Iron Ring")
	public static final RegistryObject<IronRing> IRON_RING = register("iron_ring", IronRing::new);

	@ConfigurableItem("Etherium Ore")
	public static final RegistryObject<EtheriumOre> ETHERIUM_ORE = register("etherium_ore", EtheriumOre::new);

	@ConfigurableItem("Etherium Ingot")
	public static final RegistryObject<EtheriumIngot> ETHERIUM_INGOT = register("etherium_ingot", EtheriumIngot::new);

	@ConfigurableItem("Etherium Nugget")
	public static final RegistryObject<EtheriumNugget> ETHERIUM_NUGGET = register("etherium_nugget", EtheriumNugget::new);

	@ConfigurableItem("Etherium Scraps")
	public static final RegistryObject<EtheriumScraps> ETHERIUM_SCRAPS = register("etherium_scraps", EtheriumScraps::new);

	@ConfigurableItem("Ultimate Potions")
	public static final RegistryObject<UltimatePotionBase> ULTIMATE_POTION = register("ultimate_potion", () ->  new UltimatePotionBase(Rarity.RARE, PotionType.ULTIMATE));

	@ConfigurableItem("Ultimate Potions")
	public static final RegistryObject<UltimatePotionSplash> ULTIMATE_POTION_SPLASH = register("ultimate_potion_splash", () ->  new UltimatePotionSplash(Rarity.RARE, PotionType.ULTIMATE));

	@ConfigurableItem("Ultimate Potions")
	public static final RegistryObject<UltimatePotionLingering> ULTIMATE_POTION_LINGERING = register("ultimate_potion_lingering", () ->  new UltimatePotionLingering(Rarity.RARE, PotionType.ULTIMATE));

	@ConfigurableItem("Common Potions")
	public static final RegistryObject<UltimatePotionBase> COMMON_POTION = register("common_potion", () ->  new UltimatePotionBase(Rarity.COMMON, PotionType.COMMON));

	@ConfigurableItem("Common Potions")
	public static final RegistryObject<UltimatePotionSplash> COMMON_POTION_SPLASH = register("common_potion_splash", () ->  new UltimatePotionSplash(Rarity.COMMON, PotionType.COMMON));

	@ConfigurableItem("Common Potions")
	public static final RegistryObject<UltimatePotionLingering> COMMON_POTION_LINGERING = register("common_potion_lingering", () ->  new UltimatePotionLingering(Rarity.COMMON, PotionType.COMMON));

	@ConfigurableItem("Etherium Armor")
	public static final RegistryObject<EtheriumArmor> ETHERIUM_HELMET = register("etherium_helmet", () -> new EtheriumArmor(ArmorItem.Type.HELMET));

	@ConfigurableItem("Etherium Armor")
	public static final RegistryObject<EtheriumArmor> ETHERIUM_CHESTPLATE = register("etherium_chestplate", () -> new EtheriumArmor(ArmorItem.Type.CHESTPLATE));

	@ConfigurableItem("Etherium Armor")
	public static final RegistryObject<EtheriumArmor> ETHERIUM_LEGGINGS = register("etherium_leggings", () -> new EtheriumArmor(ArmorItem.Type.LEGGINGS));

	@ConfigurableItem("Etherium Armor")
	public static final RegistryObject<EtheriumArmor> ETHERIUM_BOOTS = register("etherium_boots", () -> new EtheriumArmor(ArmorItem.Type.BOOTS));

	@ConfigurableItem("Etherium Pickaxe")
	public static final RegistryObject<EtheriumPickaxe> ETHERIUM_PICKAXE = register("etherium_pickaxe", EtheriumPickaxe::new);

	@ConfigurableItem("Etherium Waraxe")
	public static final RegistryObject<EtheriumAxe> ETHERIUM_AXE = register("etherium_axe", EtheriumAxe::new);

	@ConfigurableItem("Etherium Shovel")
	public static final RegistryObject<EtheriumShovel> ETHERIUM_SHOVEL = register("etherium_shovel", EtheriumShovel::new);

	@ConfigurableItem("Etherium Broadsword")
	public static final RegistryObject<EtheriumSword> ETHERIUM_SWORD = register("etherium_sword", EtheriumSword::new);

	@ConfigurableItem("Etherium Scythe")
	public static final RegistryObject<EtheriumScythe> ETHERIUM_SCYTHE = register("etherium_scythe", EtheriumScythe::new);

	@ConfigurableItem("Astral Dust")
	public static final RegistryObject<AstralDust> ASTRAL_DUST = register("astral_dust", AstralDust::new);

	@ConfigurableItem("The Architect's Inkwell")
	public static final RegistryObject<LoreInscriber> LORE_INSCRIBER = register("lore_inscriber", LoreInscriber::new);

	@ConfigurableItem("Lore Fragment")
	public static final RegistryObject<LoreFragment> LORE_FRAGMENT = register("lore_fragment", LoreFragment::new);

	@ConfigurableItem("Ender Rod")
	public static final RegistryObject<EnderRod> ENDER_ROD = register("ender_rod", EnderRod::new);

	@ConfigurableItem("Astral Breaker")
	public static final RegistryObject<AstralBreaker> ASTRAL_BREAKER = register("astral_breaker", AstralBreaker::new);

	@ConfigurableItem("Keystone of The Oblivion")
	public static final RegistryObject<OblivionStone> VOID_STONE = register("void_stone", OblivionStone::new);

	@ConfigurableItem("Tome of Hungering Knowledge")
	public static final RegistryObject<EnchantmentTransposer> ENCHANTMENT_TRANSPOSER = register("enchantment_transposer", EnchantmentTransposer::new);

	@ConfigurableItem("Tome of Devoured Malignancy")
	public static final RegistryObject<CurseTransposer> CURSE_TRANSPOSER = register("curse_transposer", CurseTransposer::new);

	@ConfigurableItem("Grace of the Creator")
	public static final RegistryObject<FabulousScroll> FABULOUS_SCROLL = register("fabulous_scroll", FabulousScroll::new);

	@ConfigurableItem
	public static final RegistryObject<StorageCrystal> STORAGE_CRYSTAL = register("storage_crystal", StorageCrystal::new);

	@ConfigurableItem
	public static final RegistryObject<SoulCrystal> SOUL_CRYSTAL = register("soul_crystal", SoulCrystal::new);

	@ConfigurableItem("The Acknowledgment")
	public static final RegistryObject<TheAcknowledgment> THE_ACKNOWLEDGMENT = register("the_acknowledgment", TheAcknowledgment::new);

	@ConfigurableItem("Tattered Tome")
	public static final RegistryObject<RevelationTome> TATTERED_TOME = register("tattered_tome", () -> new RevelationTome(Rarity.UNCOMMON, RevelationTome.TomeType.OVERWORLD));

	@ConfigurableItem("Withered Tome")
	public static final RegistryObject<RevelationTome> WITHERED_TOME = register("withered_tome", () -> new RevelationTome(Rarity.UNCOMMON, RevelationTome.TomeType.NETHER));

	@ConfigurableItem("Corrupted Tome")
	public static final RegistryObject<RevelationTome> CORRUPTED_TOME = register("corrupted_tome", () -> new RevelationTome(Rarity.RARE, RevelationTome.TomeType.END));

	@ConfigurableItem("Ring of the Seven Curses")
	public static final RegistryObject<CursedRing> CURSED_RING = register("cursed_ring", CursedRing::new);

	@ConfigurableItem("Twisted Mirror")
	public static final RegistryObject<TwistedMirror> TWISTED_MIRROR = register("twisted_mirror", TwistedMirror::new);

	@ConfigurableItem("Scroll of a Thousand Curses")
	public static final RegistryObject<CursedScroll> CURSED_SCROLL = register("cursed_scroll", CursedScroll::new);

	@ConfigurableItem("Emblem of Bloodstained Valor")
	public static final RegistryObject<BerserkEmblem> BERSERK_CHARM = register("berserk_charm", BerserkEmblem::new);

	@ConfigurableItem("Heart of the Earth")
	public static final RegistryObject<EarthHeart> EARTH_HEART = register("earth_heart", EarthHeart::new);

	@ConfigurableItem("Twisted Heart")
	public static final RegistryObject<TwistedHeart> TWISTED_HEART = register("twisted_heart", TwistedHeart::new);

	@ConfigurableItem("Heart of the Guardian")
	public static final RegistryObject<GuardianHeart> GUARDIAN_HEART = register("guardian_heart", GuardianHeart::new);

	@ConfigurableItem("The Twist")
	public static final RegistryObject<TheTwist> THE_TWIST = register("the_twist", TheTwist::new);

	@ConfigurableItem("Nefarious Essence")
	public static final RegistryObject<EvilEssence> EVIL_ESSENCE = register("evil_essence", EvilEssence::new);

	@ConfigurableItem("Nefarious Ingot")
	public static final RegistryObject<EvilIngot> EVIL_INGOT = register("evil_ingot", EvilIngot::new);

	@ConfigurableItem("Guite to Animal Companionship")
	public static final RegistryObject<PetGuidebook> ANIMAL_GUIDEBOOK = register("animal_guidebook", PetGuidebook::new);

	@ConfigurableItem("Guide to Feral Hunt")
	public static final RegistryObject<HunterGuidebook> HUNTER_GUIDEBOOK = register("hunter_guidebook", HunterGuidebook::new);

	@ConfigurableItem("Forbidden Fruit")
	public static final RegistryObject<ForbiddenFruit> FORBIDDEN_FRUIT = register("forbidden_fruit", ForbiddenFruit::new);

	@ConfigurableItem
	public static final RegistryObject<RedemptionPotion> REDEMPTION_POTION = register("redemption_potion", RedemptionPotion::new);

	@ConfigurableItem("Exquisite Ring")
	public static final RegistryObject<GoldenRing> GOLDEN_RING = register("golden_ring", GoldenRing::new);

	@ConfigurableItem("Unholy Stone")
	public static final RegistryObject<CursedStone> CURSED_STONE = register("cursed_stone", CursedStone::new);

	@ConfigurableItem("Enchanter's Pearl")
	public static final RegistryObject<EnchanterPearl> ENCHANTER_PEARL = register("enchanter_pearl", EnchanterPearl::new);

	@ConfigurableItem("Pact of Infinite Avarice")
	public static final RegistryObject<AvariceScroll> AVARICE_SCROLL = register("avarice_scroll", AvariceScroll::new);

	@ConfigurableItem("Essence of Raging Life")
	public static final RegistryObject<Infinimeal> INFINIMEAL = register("infinimeal", Infinimeal::new);

	@ConfigurableItem("Darkest Scroll")
	public static final RegistryObject<DarkestScroll> DARKEST_SCROLL = register("darkest_scroll", DarkestScroll::new);

	@ConfigurableItem
	public static final RegistryObject<UnwitnessedAmulet> UNWITNESSED_AMULET = register("unwitnessed_amulet", UnwitnessedAmulet::new);

	@ConfigurableItem("Potion of Twisted Mercy")
	public static final RegistryObject<TwistedPotion> TWISTED_POTION = register("twisted_potion", TwistedPotion::new);

	@ConfigurableItem("Bulwark of Blazing Pride")
	public static final RegistryObject<InfernalShield> INFERNAL_SHIELD = register("infernal_shield", InfernalShield::new);

	@ConfigurableItem("Heart of the Cosmos")
	public static final RegistryObject<CosmicHeart> COSMIC_HEART = register("cosmic_heart", CosmicHeart::new);

	@ConfigurableItem("Heart of the Abyss")
	public static final RegistryObject<AbyssalHeart> ABYSSAL_HEART = register("abyssal_heart", AbyssalHeart::new);

	@ConfigurableItem("The Infinitum")
	public static final RegistryObject<TheInfinitum> THE_INFINITUM = register("the_infinitum", TheInfinitum::new);

	@ConfigurableItem("Celestial Fruit")
	public static final RegistryObject<AstralFruit> ASTRAL_FRUIT = register("astral_fruit", AstralFruit::new);

	@ConfigurableItem("The Ender Slayer")
	public static final RegistryObject<EnderSlayer> ENDER_SLAYER = register("ender_slayer", EnderSlayer::new);

	@ConfigurableItem("Non-Euclidean Cube")
	public static final RegistryObject<TheCube> THE_CUBE = register("the_cube", TheCube::new);

	@ConfigurableItem("The Burden of Desolation")
	public static final RegistryObject<DesolationRing> DESOLATION_RING = register("desolation_ring", DesolationRing::new);

	@ConfigurableItem("Astral Potato")
	public static final RegistryObject<AstralPotato> ASTRAL_POTATO = register("astral_potato", AstralPotato::new);

	@ConfigurableItem("Wayfinder of the Damned")
	public static final RegistryObject<SoulCompass> SOUL_COMPASS = register("soul_compass", SoulCompass::new);

	@ConfigurableItem("Amulet of Ascension")
	public static final RegistryObject<AscensionAmulet> ASCENSION_AMULET = register("ascension_amulet", AscensionAmulet::new);

	@ConfigurableItem("The Testament of Contempt")
	public static final RegistryObject<EldritchAmulet> ELDRITCH_AMULET = register("eldritch_amulet", EldritchAmulet::new);

	@ConfigurableItem("Majestic Elytra")
	public static final RegistryObject<EnigmaticElytra> ENIGMATIC_ELYTRA = register("enigmatic_elytra", EnigmaticElytra::new);

	@ConfigurableItem("Inscrutable Eye")
	public static final RegistryObject<EnigmaticEye> ENIGMATIC_EYE = register("enigmatic_eye", EnigmaticEye::new);

	@ConfigurableItem("Bottle of Ichor")
	public static final RegistryObject<IchorBottle> ICHOR_BOTTLE = register("ichor_bottle", IchorBottle::new);

	@ConfigurableItem
	public static final RegistryObject<QuotePlayer> QUOTE_PLAYER = register("quote_player", QuotePlayer::new);

	@ConfigurableItem("Charming Insignia")
	public static final RegistryObject<Insignia> INSIGNIA = register("insignia", Insignia::new);

	@ConfigurableItem("Cosmic Scroll")
	public static final RegistryObject<Item> COSMIC_SCROLL = register("cosmic_scroll", CosmicScroll::new);

	@ConfigurableItem("Deception Amulet")
	public static final RegistryObject<Item> DECEPTION_AMULET = register("deception_amulet", DeceptionAmulet::new);

	@ConfigurableItem("The Judgement")
	public static final RegistryObject<Item> THE_JUDGEMENT = register("the_judgement", TheJudgement::new);

	@ConfigurableItem("Soul Dust")
	public static final RegistryObject<Item> SOUL_DUST = register("soul_dust", SoulDust::new);

	@ConfigurableItem("Eldritch Frying Pan")
	public static final RegistryObject<Item> ELDRITCH_PAN = register("eldritch_pan", EldritchPan::new);

	public static void registerItems(IEventBus modEventBus) {
		ITEMS.register(modEventBus);
	}

	private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> builder) {
		return ITEMS.register(name, builder);
	}

	@SubscribeEvent
	public static void onRegister(RegisterEvent event) {
		if (!event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)) return;

		EnigmaticBlocks.getBlockItemMap().forEach((block, item) ->
				event.register(ForgeRegistries.Keys.ITEMS, block, () ->
						item.apply(ForgeRegistries.BLOCKS.getValue(block))));
	}

}
