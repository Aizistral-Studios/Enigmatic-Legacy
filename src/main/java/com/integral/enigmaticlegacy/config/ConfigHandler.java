package com.integral.enigmaticlegacy.config;

import java.util.ArrayList;
import java.util.List;

import com.integral.enigmaticlegacy.config.OmnipotentConfig.DoubleParameter;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
    public static ForgeConfigSpec COMMON;
    public static ForgeConfigSpec CLIENT;
    
    public static List<IComplexParameter> allValues = new ArrayList<IComplexParameter>();
    
    public static final ForgeConfigSpec.ConfigValue<String> CONFIG_VERSION;
    public static final String CURRENT_VERSION = "1.0";
    
    public static final OmnipotentConfig.IntParameter MAGNET_RING_RANGE
    = new OmnipotentConfig.IntParameter(8);
    
    public static final OmnipotentConfig.IntParameter SUPER_MAGNET_RING_RANGE
	= new OmnipotentConfig.IntParameter(16);
    
    public static final OmnipotentConfig.PerhapsParameter FORBIDDEN_AXE_BEHEADING_BASE
	= new OmnipotentConfig.PerhapsParameter(10);
    
    public static final OmnipotentConfig.PerhapsParameter FORBIDDEN_AXE_BEHEADING_BONUS
	= new OmnipotentConfig.PerhapsParameter(5);
    
    public static final OmnipotentConfig.IntParameter EXTRAPOLATED_MEGASPONGE_RADIUS
	= new OmnipotentConfig.IntParameter(4);
    
    public static final OmnipotentConfig.PerhapsParameter MONSTER_CHARM_UNDEAD_DAMAGE
	= new OmnipotentConfig.PerhapsParameter(25);
    
    public static final OmnipotentConfig.PerhapsParameter MONSTER_CHARM_AGGRESSIVE_DAMAGE
	= new OmnipotentConfig.PerhapsParameter(10);
    
    public static final OmnipotentConfig.PerhapsParameter MINING_CHARM_BREAK_BOOST
	= new OmnipotentConfig.PerhapsParameter(50);
    
    public static final OmnipotentConfig.DoubleParameter MINING_CHARM_REACH_BOOST
	= new OmnipotentConfig.DoubleParameter(2.15D);
    
    public static final OmnipotentConfig.DoubleParameter XP_SCROLL_COLLECTION_RANGE
	= new OmnipotentConfig.DoubleParameter(16.0D);
    
    public static final OmnipotentConfig.DoubleParameter HEAVEN_SCROLL_XP_COST_MODIFIER
	= new OmnipotentConfig.DoubleParameter(1.0D);
    
    public static final OmnipotentConfig.BooleanParameter MONSTER_CHARM_BONUS_LOOTING
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter MONSTER_CHARM_DOUBLE_XP
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter MINING_CHARM_BONUS_LUCK
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter SUPER_MAGNET_RING_SOUND
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.IntParameter ETHERIUM_PICKAXE_RADIUS
    = new OmnipotentConfig.IntParameter(3);
    
    public static final OmnipotentConfig.IntParameter ETHERIUM_PICKAXE_DEPTH
    = new OmnipotentConfig.IntParameter(1);
    
    public static final OmnipotentConfig.IntParameter ETHERIUM_SHOVEL_RADIUS
    = new OmnipotentConfig.IntParameter(3);
    
    public static final OmnipotentConfig.IntParameter ETHERIUM_SHOVEL_DEPTH
    = new OmnipotentConfig.IntParameter(1);
    
    public static final OmnipotentConfig.IntParameter ETHERIUM_AXE_VOLUME
    = new OmnipotentConfig.IntParameter(3);
    
    public static final OmnipotentConfig.IntParameter ETHERIUM_SCYTHE_VOLUME
    = new OmnipotentConfig.IntParameter(3);
    
    public static final OmnipotentConfig.IntParameter ETHERIUM_SWORD_COOLDOWN
    = new OmnipotentConfig.IntParameter(40);
    
    public static final OmnipotentConfig.PerhapsParameter ETHERIUM_ARMOR_SHIELD_THRESHOLD
    = new OmnipotentConfig.PerhapsParameter(40);
    
    public static final OmnipotentConfig.PerhapsParameter ETHERIUM_ARMOR_SHIELD_REDUCTION
    = new OmnipotentConfig.PerhapsParameter(50);
    
    public static final OmnipotentConfig.BooleanParameter INVERT_MAGNETS_SHIFT
    = new OmnipotentConfig.BooleanParameter(false);
    
    public static final OmnipotentConfig.IntParameter MINING_CHARM_NIGHT_VISION_DURATION
    = new OmnipotentConfig.IntParameter(210);

    public static final OmnipotentConfig.IntParameter OCEAN_STONE_NIGHT_VISION_DURATION
    = new OmnipotentConfig.IntParameter(210);
    
    public static final OmnipotentConfig.IntParameter OBLIVION_STONE_SOFTCAP
    = new OmnipotentConfig.IntParameter(25);
    
    public static final OmnipotentConfig.IntParameter OBLIVION_STONE_HARDCAP
    = new OmnipotentConfig.IntParameter(100);
    
    
    //SPELLSTONES SPECIFIC
    public static final OmnipotentConfig.IntParameter ANGEL_BLESSING_COOLDOWN
	= new OmnipotentConfig.IntParameter(40);
    
    public static final OmnipotentConfig.IntParameter GOLEM_HEART_COOLDOWN
	= new OmnipotentConfig.IntParameter(0);
    
    public static final OmnipotentConfig.IntParameter BLAZING_CORE_COOLDOWN
	= new OmnipotentConfig.IntParameter(0);
    
    public static final OmnipotentConfig.IntParameter OCEAN_STONE_COOLDOWN
	= new OmnipotentConfig.IntParameter(600);
    
    public static final OmnipotentConfig.IntParameter EYE_OF_NEBULA_COOLDOWN
	= new OmnipotentConfig.IntParameter(60);
    
    public static final OmnipotentConfig.IntParameter VOID_PEARL_COOLDOWN
	= new OmnipotentConfig.IntParameter(0);
    
    public static final OmnipotentConfig.IntParameter ENIGMATIC_ITEM_COOLDOWN
    = new OmnipotentConfig.IntParameter(3);
    
    
    public static final OmnipotentConfig.DoubleParameter ANGEL_BLESSING_ACCELERATION_MODIFIER
	= new OmnipotentConfig.DoubleParameter(1.0D);
    
    public static final OmnipotentConfig.DoubleParameter ANGEL_BLESSING_ACCELERATION_MODIFIER_ELYTRA
	= new OmnipotentConfig.DoubleParameter(0.6D);
    
    public static final OmnipotentConfig.DoubleParameter GOLEM_HEART_DEFAULT_ARMOR
	= new OmnipotentConfig.DoubleParameter(4.0D);
    
    public static final OmnipotentConfig.DoubleParameter GOLEM_HEART_SUPER_ARMOR
	= new OmnipotentConfig.DoubleParameter(16.0D);
    
    public static final OmnipotentConfig.DoubleParameter GOLEM_HEART_SUPER_ARMOR_TOUGHNESS
	= new OmnipotentConfig.DoubleParameter(4.0D);
    
    public static final OmnipotentConfig.PerhapsParameter GOLEM_HEART_KNOCKBACK_RESISTANCE
	= new OmnipotentConfig.PerhapsParameter(100);
    
    public static final OmnipotentConfig.PerhapsParameter GOLEM_HEART_MELEE_RESISTANCE
	= new OmnipotentConfig.PerhapsParameter(25);
    
    public static final OmnipotentConfig.PerhapsParameter GOLEM_HEART_EXPLOSION_RESISTANCE
	= new OmnipotentConfig.PerhapsParameter(40);
    
    public static final OmnipotentConfig.DoubleParameter GOLEM_HEART_VULNERABILITY_MODIFIER
	= new OmnipotentConfig.DoubleParameter(2.0D);
    
    public static final OmnipotentConfig.DoubleParameter BLAZING_CORE_DAMAGE_FEEDBACK
	= new OmnipotentConfig.DoubleParameter(4.0D);
    
    public static final OmnipotentConfig.IntParameter BLAZING_CORE_IGNITION_FEEDBACK
	= new OmnipotentConfig.IntParameter(4);
    
    public static final OmnipotentConfig.DoubleParameter OCEAN_STONE_XP_COST_MODIFIER
	= new OmnipotentConfig.DoubleParameter(1.0D);
    
    public static final OmnipotentConfig.PerhapsParameter OCEAN_STONE_UNDERWATER_CREATURES_RESISTANCE
	= new OmnipotentConfig.PerhapsParameter(40);
    
    public static final OmnipotentConfig.PerhapsParameter OCEAN_STONE_SWIMMING_SPEED_BOOST
	= new OmnipotentConfig.PerhapsParameter(200);
    
    public static final OmnipotentConfig.PerhapsParameter EYE_OF_NEBULA_MAGIC_RESISTANCE
	= new OmnipotentConfig.PerhapsParameter(65);
    
    public static final OmnipotentConfig.PerhapsParameter EYE_OF_NEBULA_DODGE_PROBABILITY
	= new OmnipotentConfig.PerhapsParameter(15);
    
    public static final OmnipotentConfig.DoubleParameter EYE_OF_NEBULA_DODGE_RANGE
	= new OmnipotentConfig.DoubleParameter(16.0D);
    
    public static final OmnipotentConfig.DoubleParameter EYE_OF_NEBULA_PHASE_RANGE
	= new OmnipotentConfig.DoubleParameter(32.0D);;
    
    public static final OmnipotentConfig.DoubleParameter VOID_PEARL_REGENERATION_MODIFIER
	= new OmnipotentConfig.DoubleParameter(1.0D);
    
    public static final OmnipotentConfig.IntParameter VOID_PEARL_WITHERING_EFFECT_LEVEL
	= new OmnipotentConfig.IntParameter(2);
    
    public static final OmnipotentConfig.IntParameter VOID_PEARL_WITHERING_EFFECT_TIME
	= new OmnipotentConfig.IntParameter(100);
    
    public static final OmnipotentConfig.DoubleParameter VOID_PEARL_SHADOW_RANGE
	= new OmnipotentConfig.DoubleParameter(16.0D);
    
    public static final OmnipotentConfig.DoubleParameter VOID_PEARL_BASE_DARKNESS_DAMAGE
	= new OmnipotentConfig.DoubleParameter(4.0D);
    
    public static final OmnipotentConfig.PerhapsParameter VOID_PEARL_UNDEAD_PROBABILITY
	= new OmnipotentConfig.PerhapsParameter(15);
    
    
    public static final OmnipotentConfig.BooleanParameter ANGEL_BLESSING_ENABLED
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter ENDER_RING_ENABLED
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter ESCAPE_SCROLL_ENABLED
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter EXTRADIMENSIONAL_EYE_ENABLED
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter EYE_OF_NEBULA_ENABLED
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter FORBIDDEN_AXE_ENABLED
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter GOLEM_HEART_ENABLED
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter HASTE_POTION_ENABLED
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter HEAVEN_SCROLL_ENABLED
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter IRON_RING_ENABLED
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter MAGMA_HEART_ENABLED
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter MAGNET_RING_ENABLED
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter MEGASPONGE_ENABLED
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter MENDING_MIXTURE_ENABLED
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter MINING_CHARM_ENABLED
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter MONSTER_CHARM_ENABLED
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter OCEAN_STONE_ENABLED
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter RECALL_POTION_ENABLED
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter SUPER_MAGNET_RING_ENABLED
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter UNHOLY_GRAIL_ENABLED
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter VOID_PEARL_ENABLED
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter XP_SCROLL_ENABLED
	= new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter ETHERIUM_TOOLS_ENABLED
    = new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter ETHERIUM_ARMOR_ENABLED
    = new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter ETHERIUM_ORE_ENABLED
    = new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter LORE_INSCRIBER_ENABLED
    = new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter ULTIMATE_POTIONS_ENABLED
    = new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter COMMON_POTIONS_ENABLED
    = new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter ASTRAL_BREAKER_ENABLED
    = new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter OBLIVION_STONE_ENABLED
    = new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter BONUS_WOOL_RECIPES_ENABLED
    = new OmnipotentConfig.BooleanParameter(true);
    
    
    
    
    public static final OmnipotentConfig.BooleanParameter CLOCK_HUD_ENABLED
    = new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter CLOCK_HUD_ONLY_IN_FULLSCREEN
    = new OmnipotentConfig.BooleanParameter(false);
    
    public static final OmnipotentConfig.BooleanParameter CLOCK_HUD_HIDE_IN_CHAT
    = new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.BooleanParameter CLOCK_HUD_BACKGROUND_ENABLED
    = new OmnipotentConfig.BooleanParameter(true);
    
    public static final OmnipotentConfig.IntParameter CLOCK_HUD_X
    = new OmnipotentConfig.IntParameter(30);
    
    public static final OmnipotentConfig.IntParameter CLOCK_HUD_Y
    = new OmnipotentConfig.IntParameter(20);
    
    public static final OmnipotentConfig.DoubleParameter CLOCK_HUD_SCALE
    = new OmnipotentConfig.DoubleParameter(1.0D);
    


    static {
        final ForgeConfigSpec.Builder common = new ForgeConfigSpec.Builder();
        final ForgeConfigSpec.Builder client = new ForgeConfigSpec.Builder();
        
        client.comment("Options that allow to disable/enable Univesal Clock and adjust it").push("Universal Clock HUD");
        
        CLOCK_HUD_ENABLED.configObj = client
        		.comment("Whether or not Universal Clock should be displayed in the HUD.")
        		.translation("configGui.enigmaticlegacy.clock_hud_enabled")
        		.define("clockHudEnabled", CLOCK_HUD_ENABLED.getValueDefault());

        CLOCK_HUD_ONLY_IN_FULLSCREEN.configObj = client
        		.comment("Whether or not Universal Clock should be displayed only when in fullscreen mode.")
        		.translation("configGui.enigmaticlegacy.clock_hud_only_in_fullscreen")
        		.define("clockHudOnlyInFullscreen", CLOCK_HUD_ONLY_IN_FULLSCREEN.getValueDefault());

        CLOCK_HUD_HIDE_IN_CHAT.configObj = client
        		.comment("Whether or not Universal Clock should be hidden when chat screen is opened.")
        		.translation("configGui.enigmaticlegacy.clock_hud_hide_in_chat")
        		.define("clockHudHideInChat", CLOCK_HUD_HIDE_IN_CHAT.getValueDefault());
        
        CLOCK_HUD_BACKGROUND_ENABLED.configObj = client
        		.comment("Whether or not Universal Clock should have background.")
        		.translation("configGui.enigmaticlegacy.clock_hud_background_enabled")
        		.define("clockHudBackgroundEnabled", CLOCK_HUD_BACKGROUND_ENABLED.getValueDefault());

        CLOCK_HUD_X.configObj = client
                .comment("Position of Universal Clock on X axis of the screen.")
                .translation("configGui.enigmaticlegacy.clock_hud_X")
                .defineInRange("clockHudX", CLOCK_HUD_X.getValueDefault(), -32768, 32768);
        
        CLOCK_HUD_Y.configObj = client
        		.comment("Position of Universal Clock on Y axis of the screen.")
        		.translation("configGui.enigmaticlegacy.clock_hud_Y")
        		
        		.defineInRange("clockHudY", CLOCK_HUD_Y.getValueDefault(), -32768, 32768);
        CLOCK_HUD_SCALE.configObj = client
        		.comment("Visible size of the Universal Clock.")
        		.translation("configGui.enigmaticlegacy.clock_hud_scale")
        		.defineInRange("clockHudScale", CLOCK_HUD_SCALE.getValueDefault(), -32768F, 32768F);
        
        client.pop();

        common.comment("Enable or disable certain features. Disabled items will not be craftable and will not generate in dungeons").push("Accesibility Config");
        
        ANGEL_BLESSING_ENABLED.configObj = common
                .comment("Whether or not Angel's Blessing should be enabled.")
                .translation("configGui.enigmaticlegacy.angel_blessing_enabled")
                .define("angelBlessingEnabled", ANGEL_BLESSING_ENABLED.getValueDefault());
        
        ENDER_RING_ENABLED.configObj = common
        		.comment("Whether or not Ender Ring should be enabled.")
        		.translation("configGui.enigmaticlegacy.ender_ring_enabled")
        		.define("enderRingEnabled", ENDER_RING_ENABLED.getValueDefault());
        
        ESCAPE_SCROLL_ENABLED.configObj = common
        		.comment("Whether or not Scroll of Postmortal Recall should be enabled.")
        		.translation("configGui.enigmaticlegacy.escape_scroll_enabled")
        		.define("escapeScrollEnabled", ESCAPE_SCROLL_ENABLED.getValueDefault());
        
        EXTRADIMENSIONAL_EYE_ENABLED.configObj = common
        		.comment("Whether or not Extradimensional Eye should be enabled.")
        		.translation("configGui.enigmaticlegacy.extradimensional_eye_enabled")
        		.define("extradimensionalEyeEnabled", EXTRADIMENSIONAL_EYE_ENABLED.getValueDefault());
        
        EYE_OF_NEBULA_ENABLED.configObj = common
        		.comment("Whether or not Eye of the Nebula should be enabled.")
        		.translation("configGui.enigmaticlegacy.eye_of_nebula_enabled")
        		.define("eyeOfNebulaEnabled", EYE_OF_NEBULA_ENABLED.getValueDefault());
        
        FORBIDDEN_AXE_ENABLED.configObj = common
        		.comment("Whether or not Axe of Executioner should be enabled.")
        		.translation("configGui.enigmaticlegacy.forbidden_axe_enabled")
        		.define("forbiddenAxeEnabled", FORBIDDEN_AXE_ENABLED.getValueDefault());
        
        GOLEM_HEART_ENABLED.configObj = common
        		.comment("Whether or not Heart of the Golem should be enabled.")
        		.translation("configGui.enigmaticlegacy.golem_heart_enabled")
        		.define("golemHeartEnabled", GOLEM_HEART_ENABLED.getValueDefault());
        
        HASTE_POTION_ENABLED.configObj = common
        		.comment("Whether or not Potion of Haste should be enabled.")
        		.translation("configGui.enigmaticlegacy.haste_potion_enabled")
        		.define("hastePotionEnabled", HASTE_POTION_ENABLED.getValueDefault());
        
        HEAVEN_SCROLL_ENABLED.configObj = common
        		.comment("Whether or not Gift of the Heaven should be enabled.")
        		.translation("configGui.enigmaticlegacy.heaven_scroll_enabled")
        		.define("heavenScrollEnabled", HEAVEN_SCROLL_ENABLED.getValueDefault());
        
        IRON_RING_ENABLED.configObj = common
        		.comment("Whether or not Iron Ring should be enabled.")
        		.translation("configGui.enigmaticlegacy.iron_ring_enabled")
        		.define("ironRingEnabled", IRON_RING_ENABLED.getValueDefault());
        
        MAGMA_HEART_ENABLED.configObj = common
        		.comment("Whether or not Blazing Core should be enabled.")
        		.translation("configGui.enigmaticlegacy.blazing_core_enabled")
        		.define("blazingCoreEnabled", MAGMA_HEART_ENABLED.getValueDefault());
        
        MAGNET_RING_ENABLED.configObj = common
        		.comment("Whether or not Magnetic Ring should be enabled.")
        		.translation("configGui.enigmaticlegacy.magnet_ring_enabled")
        		.define("magnetRingEnabled", MAGNET_RING_ENABLED.getValueDefault());
        
        MEGASPONGE_ENABLED.configObj = common
        		.comment("Whether or not Extrapolated Megasponge should be enabled.")
        		.translation("configGui.enigmaticlegacy.megasponge_enabled")
        		.define("megaSpongeEnabled", MEGASPONGE_ENABLED.getValueDefault());
        
        MENDING_MIXTURE_ENABLED.configObj = common
        		.comment("Whether or not Mending Mixture should be enabled.")
        		.translation("configGui.enigmaticlegacy.mending_mixture_enabled")
        		.define("mendingMixtureEnabled", MENDING_MIXTURE_ENABLED.getValueDefault());
        
        MINING_CHARM_ENABLED.configObj = common
        		.comment("Whether or not Charm of Treasure Hunter should be enabled.")
        		.translation("configGui.enigmaticlegacy.mining_charm_enabled")
        		.define("miningCharmEnabled", MINING_CHARM_ENABLED.getValueDefault());
        
        MONSTER_CHARM_ENABLED.configObj = common
        		.comment("Whether or not Emblem of Monster Slayer should be enabled.")
        		.translation("configGui.enigmaticlegacy.monster_charm_enabled")
        		.define("monsterCharmEnabled", MONSTER_CHARM_ENABLED.getValueDefault());
        
        OCEAN_STONE_ENABLED.configObj = common
        		.comment("Whether or not Will of the Ocean should be enabled.")
        		.translation("configGui.enigmaticlegacy.ocean_stone_enabled")
        		.define("oceanStoneEnabled", OCEAN_STONE_ENABLED.getValueDefault());
        
        RECALL_POTION_ENABLED.configObj = common
        		.comment("Whether or not Potion of Recall should be enabled.")
        		.translation("configGui.enigmaticlegacy.recall_potion_enabled")
        		.define("recallPotionEnabled", RECALL_POTION_ENABLED.getValueDefault());
        
        UNHOLY_GRAIL_ENABLED.configObj = common
        		.comment("Whether or not Unholy Grail should be enabled.")
        		.translation("configGui.enigmaticlegacy.unholy_grail_enabled")
        		.define("unholyGrailEnabled", UNHOLY_GRAIL_ENABLED.getValueDefault());
        
        SUPER_MAGNET_RING_ENABLED.configObj = common
        		.comment("Whether or not Ring of Dislocation should be enabled.")
        		.translation("configGui.enigmaticlegacy.super_magnet_ring_enabled")
        		.define("superMagnetRingEnabled", SUPER_MAGNET_RING_ENABLED.getValueDefault());
        
        VOID_PEARL_ENABLED.configObj = common
        		.comment("Whether or not Pearl of the Void should be enabled.")
        		.translation("configGui.enigmaticlegacy.void_pearl_enabled")
        		.define("voidPearlEnabled", VOID_PEARL_ENABLED.getValueDefault());
        
        XP_SCROLL_ENABLED.configObj = common
        		.comment("Whether or not Scroll of Ageless Wisdom should be enabled.")
        		.translation("configGui.enigmaticlegacy.xp_scroll_enabled")
        		.define("xpScrollEnabled", XP_SCROLL_ENABLED.getValueDefault());
        
        ETHERIUM_ARMOR_ENABLED.configObj = common
        		.comment("Whether or not Etherium Armor should be enabled.")
        		.translation("configGui.enigmaticlegacy.etherium_armor_enabled")
        		.define("etheriumArmorEnabled", ETHERIUM_ARMOR_ENABLED.getValueDefault());
        
        ETHERIUM_TOOLS_ENABLED.configObj = common
        		.comment("Whether or not Etherium Tools should be enabled.")
        		.translation("configGui.enigmaticlegacy.etherium_tools_enabled")
        		.define("etheriumToolsEnabled", ETHERIUM_TOOLS_ENABLED.getValueDefault());
        
        ETHERIUM_ORE_ENABLED.configObj = common
        		.comment("Whether or not Etherium Ore should generate among dungeon loot.")
        		.translation("configGui.enigmaticlegacy.etherium_ore_enabled")
        		.define("etheriumOreEnabled", ETHERIUM_ORE_ENABLED.getValueDefault());
        
        LORE_INSCRIBER_ENABLED.configObj = common
        		.comment("Whether or not Arcane Inscriber should be enabled.")
        		.translation("configGui.enigmaticlegacy.lore_inscriber_enabled")
        		.define("loreInscriberEnabled", LORE_INSCRIBER_ENABLED.getValueDefault());
        
        ULTIMATE_POTIONS_ENABLED.configObj = common
        		.comment("Whether or not Ultimate Potions should be enabled.")
        		.translation("configGui.enigmaticlegacy.ultimate_potions_enabled")
        		.define("ultimatePotionsEnabled", ULTIMATE_POTIONS_ENABLED.getValueDefault());
        
        COMMON_POTIONS_ENABLED.configObj = common
        		.comment("Whether or not new conventional potions should be enabled. This includes only Haste potions, currently.")
        		.translation("configGui.enigmaticlegacy.common_potions_enabled")
        		.define("commonPotionsEnabled", COMMON_POTIONS_ENABLED.getValueDefault());
        
        ASTRAL_BREAKER_ENABLED.configObj = common
        		.comment("Whether or not Astral Breaker should be enabled.")
        		.translation("configGui.enigmaticlegacy.astral_breaker_enabled")
        		.define("astralBreakerEnabled", ASTRAL_BREAKER_ENABLED.getValueDefault());

        OBLIVION_STONE_ENABLED.configObj = common
        		.comment("Whether or not Keystone of The Oblivion should be enabled.")
        		.translation("configGui.enigmaticlegacy.oblivion_stone_enabled")
        		.define("oblivionStoneEnabled", OBLIVION_STONE_ENABLED.getValueDefault());
        
        BONUS_WOOL_RECIPES_ENABLED.configObj = common
        		.comment("Whether or not bonus recipes for wool dyeing should be enabled.")
        		.translation("configGui.enigmaticlegacy.bonus_wool_recipes_enabled")
        		.define("bonusWoolRecipesEnabled", BONUS_WOOL_RECIPES_ENABLED.getValueDefault());
        
        common.pop();
        
        
        
        
        common.comment("Just some different stuff").push("Generic Config");
        
        CONFIG_VERSION = common
                .comment("Version of config file. DO NOT MODIFY UNLESS YOU KNOW EXACTLY WHAT YOU'RE DOING!")
                .translation("configGui.enigmaticlegacy.config_version")
                .define("configVersion", CURRENT_VERSION);
        
        SUPER_MAGNET_RING_SOUND.configObj = common
                .comment("Whether or not Dislocation Ring should play any sounds when teleporting items to it's bearer.")
                .translation("configGui.enigmaticlegacy.super_magnet_ring_sound")
                .define("superMagnetRingSound", SUPER_MAGNET_RING_SOUND.getValueDefault());
        
        INVERT_MAGNETS_SHIFT.configObj = common
        		.comment("Inverts the Shift behaviour of Magnetic Ring and Dislocation Ring.")
        		.translation("configGui.enigmaticlegacy.invert_magnets_shift")
        		.define("invertMagnetsShift", INVERT_MAGNETS_SHIFT.getValueDefault());
        
        common.pop();
        
        common.comment("Various options that affect individual items").push("Balance Options");
        
        MAGNET_RING_RANGE.configObj = common
                .comment("The radius in which Magnetic Ring will attract items.")
                .translation("configGui.enigmaticlegacy.magnet_ring_range")
                .defineInRange("magnetRingRange", MAGNET_RING_RANGE.getValueDefault(), 1, 32);
        
        SUPER_MAGNET_RING_RANGE.configObj = common
        		.comment("The radius in which Dislocation Ring will collect items.")
        		.translation("configGui.enigmaticlegacy.super_magnet_ring_range")
        		.defineInRange("superMagnetRingRange", SUPER_MAGNET_RING_RANGE.getValueDefault(), 1, 128);
        /*
        FORBIDDEN_AXE_ATTACK_DAMAGE = common
        		.comment("Default attack damage of Axe of Executioner.")
        		.translation("configGui.enigmaticlegacy.forbidden_axe_attack_damage")
        		.defineInRange("forbiddenAxeAttackDamage", 10.0, 1.0, 1000.0);
        
        FORBIDDEN_AXE_ATTACK_SPEED = common
        		.comment("Default attack speed of Axe of Executioner.")
        		.translation("configGui.enigmaticlegacy.forbidden_axe_attack_speed")
        		.defineInRange("forbiddenAxeAttackSpeed", 2.4, 0.0, 1000.0);
		*/
        FORBIDDEN_AXE_BEHEADING_BASE.configObj = common
        		.comment("Default chance to behead an enemy with Axe of Executioner. Defined as percentage.")
        		.translation("configGui.enigmaticlegacy.forbidden_axe_beheading_base")
        		.defineInRange("forbiddenAxeBeheadingBase", FORBIDDEN_AXE_BEHEADING_BASE.getValueDefault().asPercentage(), 0, 100);

        FORBIDDEN_AXE_BEHEADING_BONUS.configObj = common
        		.comment("Bonus percantage to beheading chance from each looting level applied to Axe of Executioner.")
        		.translation("configGui.enigmaticlegacy.forbidden_axe_beheading_bonus")
        		.defineInRange("forbiddenBeheadingBonus", FORBIDDEN_AXE_BEHEADING_BONUS.getValueDefault().asPercentage(), 0, 100);
        
        EXTRAPOLATED_MEGASPONGE_RADIUS.configObj = common
        		.comment("Radius in which Exptrapolated Megaspong absorbs water. Default 4 equals to vanilla sponge")
        		.translation("configGui.enigmaticlegacy.megasponge_radius")
        		.defineInRange("megaspongeRadius", EXTRAPOLATED_MEGASPONGE_RADIUS.getValueDefault(), 0, 128);
        
        MONSTER_CHARM_UNDEAD_DAMAGE.configObj = common
        		.comment("Damage multiplier against undead enemies for Emblem of Monster Slayer. Defined as percentage.")
        		.translation("configGui.enigmaticlegacy.monster_charm_undead_damage")
        		.defineInRange("monsterCharmUndeadDamage", MONSTER_CHARM_UNDEAD_DAMAGE.getValueDefault().asPercentage(), 0, 1000);
        
        MONSTER_CHARM_AGGRESSIVE_DAMAGE.configObj = common
        		.comment("Damage multiplier against agressive creatures for Emblem of Monster Slayer. Defined as percentage.")
        		.translation("configGui.enigmaticlegacy.monster_charm_agressive_damage")
        		.defineInRange("monsterCharmAgressiveDamage", MONSTER_CHARM_AGGRESSIVE_DAMAGE.getValueDefault().asPercentage(), 0, 1000);
        
        MONSTER_CHARM_BONUS_LOOTING.configObj = common
        		.comment("Whether or not Emblem of Monster Slayer should provide +1 Looting Level.")
        		.translation("configGui.enigmaticlegacy.monster_charm_bonus_looting")
        		.define("monsterCharmBonusLooting", MONSTER_CHARM_BONUS_LOOTING.getValueDefault());
        
        MONSTER_CHARM_DOUBLE_XP.configObj = common
        		.comment("Whether or not Emblem of Monster Slayer should provide double experience drop from monsters.")
        		.translation("configGui.enigmaticlegacy.monster_charm_bonus_xp")
        		.define("monsterCharmBonusXP", MONSTER_CHARM_DOUBLE_XP.getValueDefault());
        
        MINING_CHARM_BREAK_BOOST.configObj = common
        		.comment("Mining speed boost granted by Charm of Treasure Hunter. Defined as percentage.")
        		.translation("configGui.enigmaticlegacy.mining_charm_break_boost")
        		.defineInRange("miningCharmBreakBoost", MINING_CHARM_BREAK_BOOST.getValueDefault().asPercentage(), 0, 1000);
        
        MINING_CHARM_REACH_BOOST.configObj = common
        		.comment("Additional block reach granted by Charm of Treasure Hunter.")
        		.translation("configGui.enigmaticlegacy.mining_charm_block_reach")
        		.defineInRange("miningCharmBlockReach", MINING_CHARM_REACH_BOOST.getValueDefault(), 0.0D, 16D);
        
        MINING_CHARM_BONUS_LUCK.configObj = common
        		.comment("Whether or not Charm of Treasure Hunter should provide +1 Luck.")
        		.translation("configGui.enigmaticlegacy.mining_charm_bonus_luck")
        		.define("miningCharmBonusLuck", MINING_CHARM_BONUS_LUCK.getValueDefault());
        
        HEAVEN_SCROLL_XP_COST_MODIFIER.configObj = common
        		.comment("Multiplier for experience consumption by Gift of the Heaven.")
        		.translation("configGui.enigmaticlegacy.heaven_scroll_xp_cost_modifier")
        		.defineInRange("heavenScrollXPCostModifier", HEAVEN_SCROLL_XP_COST_MODIFIER.getValueDefault(), 0.0D, 1000.0D);
        
        XP_SCROLL_COLLECTION_RANGE.configObj = common
        		.comment("Range in which Scroll of Ageless Wisdom collects experience orbs when active.")
        		.translation("configGui.enigmaticlegacy.xp_scroll_collection_range")
        		.defineInRange("xpScrollCollectionRange", XP_SCROLL_COLLECTION_RANGE.getValueDefault(), 1.0D, 128.0D);
        
        ETHERIUM_ARMOR_SHIELD_THRESHOLD.configObj = common
        		.comment("The value of health to which player wearing full Etherium Armor set should be brough to activate the shield ability. Defined as percentage.")
        		.translation("configGui.enigmaticlegacy.etherium_armor_shield_threshold")
        		.defineInRange("etheriumArmorShieldThreshold", ETHERIUM_ARMOR_SHIELD_THRESHOLD.getValueDefault().asPercentage(), 0, 100);
 
        ETHERIUM_ARMOR_SHIELD_REDUCTION.configObj = common
        		.comment("Damage reduction of shield generated by Etherium Armor. Defined as percentage.")
        		.translation("configGui.enigmaticlegacy.etherium_armor_shield_reduction")
        		.defineInRange("etheriumArmorShieldReduction", ETHERIUM_ARMOR_SHIELD_REDUCTION.getValueDefault().asPercentage(), 0, 100);
        
        ETHERIUM_PICKAXE_RADIUS.configObj = common
                .comment("The radius of Etherium Pickaxe AOE mining. Set to -1 to disable the feature.")
                .translation("configGui.enigmaticlegacy.etherium_pickaxe_radius")
                .defineInRange("etheriumPickaxeRadius", ETHERIUM_PICKAXE_RADIUS.getValueDefault(), -1, 99);
        
        ETHERIUM_PICKAXE_DEPTH.configObj = common
        		.comment("The depth of Etherium Pickaxe AOE mining.")
        		.translation("configGui.enigmaticlegacy.etherium_pickaxe_depth")
        		.defineInRange("etheriumPickaxeDepth", ETHERIUM_PICKAXE_DEPTH.getValueDefault(), 1, 99);
        
        ETHERIUM_SHOVEL_RADIUS.configObj = common
                .comment("The radius of Etherium Shovel AOE digging. Set to -1 to disable the feature.")
                .translation("configGui.enigmaticlegacy.etherium_shovel_radius")
                .defineInRange("etheriumShovelRadius", ETHERIUM_SHOVEL_RADIUS.getValueDefault(), -1, 99);
        
        ETHERIUM_SHOVEL_DEPTH.configObj = common
        		.comment("The depth of Etherium Shovel AOE digging.")
        		.translation("configGui.enigmaticlegacy.etherium_shovel_depth")
        		.defineInRange("etheriumShovelDepth", ETHERIUM_SHOVEL_DEPTH.getValueDefault(), 1, 99);
        
        ETHERIUM_AXE_VOLUME.configObj = common
        		.comment("The volume Etherium Waraxe AOE chopping. Set to -1 to disable the feature.")
        		.translation("configGui.enigmaticlegacy.etherium_axe_volume")
        		.defineInRange("etheriumAxeVolume", ETHERIUM_AXE_VOLUME.getValueDefault(), -1, 99);
        
        ETHERIUM_SCYTHE_VOLUME.configObj = common
        		.comment("The volume Etherium Scythe AOE harvesting. Set to -1 to disable the feature.")
        		.translation("configGui.enigmaticlegacy.etherium_scythe_volume")
        		.defineInRange("etheriumScytheVolume", ETHERIUM_SCYTHE_VOLUME.getValueDefault(), -1, 99);
        
        ETHERIUM_SWORD_COOLDOWN.configObj = common
        		.comment("Cooldown of Etherium Broadsword ability. Measured in ticks.")
        		.translation("configGui.enigmaticlegacy.etherium_sword_cooldown")
        		.defineInRange("etheriumSwordCooldown", ETHERIUM_SWORD_COOLDOWN.getValueDefault(), 0, 32768);
        
        MINING_CHARM_NIGHT_VISION_DURATION.configObj = common
                .comment("The duration (in ticks) for which Charm of the Treasure Hunter applies Night Vision effect each tick required conditions are met. Deprecated as of 1.4.0, affects nothing now.")
                .translation("configGui.enigmaticlegacy.mining_charm_night_vision_duration")
                .defineInRange("miningCharmNightVisionDuration", MINING_CHARM_NIGHT_VISION_DURATION.getValueDefault(), 0, 32768);
 
        OCEAN_STONE_NIGHT_VISION_DURATION.configObj = common
        		.comment("The duration (in ticks) for which Will of the Ocean applies Night Vision effect each tick required conditions are met. Deprecated as of 1.4.0, affects nothing now.")
        		.translation("configGui.enigmaticlegacy.ocean_stone_night_vision_duration")
        		.defineInRange("oceanStoneNightVisionDuration", OCEAN_STONE_NIGHT_VISION_DURATION.getValueDefault(), 0, 32768);

        OBLIVION_STONE_SOFTCAP.configObj = common
        		.comment("Soft cap for Keystone of The Oblivion. When it's reached, the list view seen in it's Ctrl tooltip will be fixed at this amount of items, and become chaotic and unreadable. Required since monitors are not infinitely large these days.")
        		.translation("configGui.enigmaticlegacy.oblivion_stone_softcap")
        		.defineInRange("oblivionStoneSoftcap", OBLIVION_STONE_SOFTCAP.getValueDefault(), 1, 32768);
        
        OBLIVION_STONE_HARDCAP.configObj = common
        		.comment("Hard cap for Keystone of The Oblivion. When it's reached, you will no longer be able to add new items to it's list via crafting. Required to prevent potential perfomance issues with ridiculously large lists.")
        		.translation("configGui.enigmaticlegacy.oblivion_stone_hardcap")
        		.defineInRange("oblivionStoneHardcap", OBLIVION_STONE_HARDCAP.getValueDefault(), 1, 32768);
        
        common.pop();
        
        
        
        common.comment("Balancing and other option for all the spellstones").push("Spellstones Options");
        
        ANGEL_BLESSING_COOLDOWN.configObj = common
        		.comment("Active ability cooldown for Angel's Blessing. Measured in ticks. 20 ticks equal to 1 second.")
        		.translation("configGui.enigmaticlegacy.angel_blessing_cooldown")
        		.defineInRange("angelBlessingCooldown", ANGEL_BLESSING_COOLDOWN.getValueDefault(), 0, 32768);
    	
    	ANGEL_BLESSING_ACCELERATION_MODIFIER.configObj = common
    			.comment("Acceleration modifier for active ability of Angel's Blessing. The greater it is, the more momentum you will gain.")
    			.translation("configGui.enigmaticlegacy.angel_blessing_acceleration_modifier")
    			.defineInRange("angelBlessingAccelerationModifier", ANGEL_BLESSING_ACCELERATION_MODIFIER.getValueDefault(), 0.0, 256.0);
    	
    	ANGEL_BLESSING_ACCELERATION_MODIFIER_ELYTRA.configObj = common
    			.comment("Separate acceleration modifier for active ability of Angel's Blessing when player is flying with Elytra.")
    			.translation("configGui.enigmaticlegacy.angel_blessing_acceleration_modifier_elytra")
    			.defineInRange("angelBlessingAccelerationModifierElytra", ANGEL_BLESSING_ACCELERATION_MODIFIER_ELYTRA.getValueDefault(), 0.0, 256.0);
    	
    	
    	
    	GOLEM_HEART_COOLDOWN.configObj = common
    			.comment("Active ability cooldown for Heart of the Golem. Measured in ticks. 20 ticks equal to 1 second.")
    			.translation("configGui.enigmaticlegacy.golem_heart_cooldown")
    			.defineInRange("golemHeartCooldown", GOLEM_HEART_COOLDOWN.getValueDefault(), 0, 32768);
    	
    	GOLEM_HEART_DEFAULT_ARMOR.configObj = common
    			.comment("Default amount of armor points provided by Heart of the Golem.")
    			.translation("configGui.enigmaticlegacy.golem_heart_default_armor")
    			.defineInRange("golemHeartDefaultArmor", GOLEM_HEART_DEFAULT_ARMOR.getValueDefault(), 0.0, 256.0);
    	
    	GOLEM_HEART_SUPER_ARMOR.configObj = common
    			.comment("The amount of armor points provided by Heart of the Golem when it's bearer has no armor equipped.")
    			.translation("configGui.enigmaticlegacy.golem_heart_super_armor")
    			.defineInRange("golemHeartSuperArmor", GOLEM_HEART_SUPER_ARMOR.getValueDefault(), 0.0, 256.0);
    	
    	GOLEM_HEART_SUPER_ARMOR_TOUGHNESS.configObj = common
    			.comment("The amount of armor toughness provided by Heart of the Golem when it's bearer has no armor equipped.")
    			.translation("configGui.enigmaticlegacy.golem_heart_super_armor_toughness")
    			.defineInRange("golemHeartSuperArmorToughness", GOLEM_HEART_SUPER_ARMOR_TOUGHNESS.getValueDefault(), 0.0, 256.0);
    	
    	GOLEM_HEART_MELEE_RESISTANCE.configObj = common
    			.comment("Resistance to melee attacks provided by Heart of the Golem. Defined as percentage.")
    			.translation("configGui.enigmaticlegacy.golem_heart_melee_resistance")
    			.defineInRange("golemHeartMeleeResistance", GOLEM_HEART_MELEE_RESISTANCE.getValueDefault().asPercentage(), 0, 100);
    	
    	GOLEM_HEART_EXPLOSION_RESISTANCE.configObj = common
    			.comment("Resistance to explosion damage provided by Heart of the Golem. Defined as percentage.")
    			.translation("configGui.enigmaticlegacy.golem_heart_explosion_resistance")
    			.defineInRange("golemHeartExplosionResistance", GOLEM_HEART_EXPLOSION_RESISTANCE.getValueDefault().asPercentage(), 0, 100);
    	
    	GOLEM_HEART_KNOCKBACK_RESISTANCE.configObj = common
    			.comment("Resistance to knockback provided by Heart of the Golem. Defined as percentage.")
    			.translation("configGui.enigmaticlegacy.golem_heart_knockback_resistance")
    			.defineInRange("golemHeartKnockbackResistance", GOLEM_HEART_KNOCKBACK_RESISTANCE.getValueDefault().asPercentage(), 0, 100);
    	
    	GOLEM_HEART_VULNERABILITY_MODIFIER.configObj = common
    			.comment("Modifier for Magic Damage vulnerability applied by Heart of the Golem. Default value of 2.0 means that player will receive twice as much damage from magic.")
    			.translation("configGui.enigmaticlegacy.golem_heart_vulnerability_modifier")
    			.defineInRange("golemHeartVulnerabilityModifier", GOLEM_HEART_VULNERABILITY_MODIFIER.getValueDefault(), 1.0, 256.0);
    	
    	
    	
    	BLAZING_CORE_COOLDOWN.configObj = common
    			.comment("Active ability cooldown for Blazing Core. Measured in ticks. 20 ticks equal to 1 second.")
    			.translation("configGui.enigmaticlegacy.blazing_core_cooldown")
    			.defineInRange("blazingCoreCooldown", BLAZING_CORE_COOLDOWN.getValueDefault(), 0, 32768);
    	
    	BLAZING_CORE_DAMAGE_FEEDBACK.configObj = common
    			.comment("How much fire-based damage instantly receives any creature that attacks bearer of the Blazing Core.")
    			.translation("configGui.enigmaticlegacy.blazing_core_damage_feedback")
    			.defineInRange("blazingCoreDamageFeedback", BLAZING_CORE_DAMAGE_FEEDBACK.getValueDefault(), 0.0, 512.0);
    	
    	BLAZING_CORE_IGNITION_FEEDBACK.configObj = common
    			.comment("How how many seconds any creature that attacks bearer of the Blazing Core will be set on fire.")
    			.translation("configGui.enigmaticlegacy.blazing_core_damage_feedback")
    			.defineInRange("blazingCoreIgnitionFeedback", BLAZING_CORE_IGNITION_FEEDBACK.getValueDefault(), 0, 512);
    	
    	
    	
    	OCEAN_STONE_COOLDOWN.configObj = common
    			.comment("Active ability cooldown for Will of the Ocean. Measured in ticks. 20 ticks equal to 1 second.")
    			.translation("configGui.enigmaticlegacy.ocean_stone_cooldown")
    			.defineInRange("oceanStoneCooldown", OCEAN_STONE_COOLDOWN.getValueDefault(), 0, 32768);
    	
    	OCEAN_STONE_SWIMMING_SPEED_BOOST.configObj = common
    			.comment("Swimming speed boost provided by Will of the Ocean. Defined as percentage.")
    			.translation("configGui.enigmaticlegacy.ocean_stone_swim_boost")
    			.defineInRange("oceanStoneSwimBoost", OCEAN_STONE_SWIMMING_SPEED_BOOST.getValueDefault().asPercentage(), 0, 1000);
    	
    	OCEAN_STONE_UNDERWATER_CREATURES_RESISTANCE.configObj = common
    			.comment("Damage resistance against underwater creatures provided by Will of the Ocean. Defined as percentage.")
    			.translation("configGui.enigmaticlegacy.ocean_stone_underwater_creatures_resistance")
    			.defineInRange("oceanStoneUnderwaterCreaturesResistance", OCEAN_STONE_UNDERWATER_CREATURES_RESISTANCE.getValueDefault().asPercentage(), 0, 100);
    	
    	OCEAN_STONE_XP_COST_MODIFIER.configObj = common
    			.comment("Multiplier for experience consumption by active ability of Will of the Ocean.")
    			.translation("configGui.enigmaticlegacy.ocean_stone_xp_cost_modifier")
    			.defineInRange("oceanStoneXPCostModifier", OCEAN_STONE_XP_COST_MODIFIER.getValueDefault(), 0.0, 1000.0);

    	
    	
    	EYE_OF_NEBULA_COOLDOWN.configObj = common
    			.comment("Active ability cooldown for Eye of the Nebula. Measured in ticks. 20 ticks equal to 1 second.")
    			.translation("configGui.enigmaticlegacy.eye_of_nebula_cooldown")
    			.defineInRange("eyeOfNebulaCooldown", EYE_OF_NEBULA_COOLDOWN.getValueDefault(), 0, 32768);
    	
    	EYE_OF_NEBULA_DODGE_PROBABILITY.configObj = common
    			.comment("Probability for Eye of the Nebula to teleport it's bearer from any attack without receiving any damage. Defined as percentage.")
    			.translation("configGui.enigmaticlegacy.eye_of_nebula_dodge_chance")
    			.defineInRange("eyeOfNebulaDodgeChance", EYE_OF_NEBULA_DODGE_PROBABILITY.getValueDefault().asPercentage(), 0, 100);

    	EYE_OF_NEBULA_DODGE_RANGE.configObj = common
    			.comment("Range in which Eye of the Nebula searches for a position to teleport it's bearer to when dodging the attack.")
    			.translation("configGui.enigmaticlegacy.eye_of_nebula_dodge_range")
    			.defineInRange("eyeOfNebulaDodgeRange", EYE_OF_NEBULA_DODGE_RANGE.getValueDefault(), 1.0, 128.0);
    	
    	EYE_OF_NEBULA_PHASE_RANGE.configObj = common
    			.comment("Range in which Eye of the Nebula can reach an entity when using it's active ability.")
    			.translation("configGui.enigmaticlegacy.eye_of_nebula_phase_range")
    			.defineInRange("eyeOfNebulaPhaseRange", EYE_OF_NEBULA_PHASE_RANGE.getValueDefault(), 1.0, 128.0);
    	
    	EYE_OF_NEBULA_MAGIC_RESISTANCE.configObj = common
    			.comment("Magic Damage resistance provided by Eye of the Nebula. Defined as percentage.")
    			.translation("configGui.enigmaticlegacy.eye_of_nebula_magic_resistance")
    			.defineInRange("eyeOfNebulaMagicResistance", EYE_OF_NEBULA_MAGIC_RESISTANCE.getValueDefault().asPercentage(), 0, 100);
    	
    	
    	
    	VOID_PEARL_COOLDOWN.configObj = common
    			.comment("Active ability cooldown for Pearl of the Void. Measured in ticks. 20 ticks equal to 1 second.")
    			.translation("configGui.enigmaticlegacy.void_pearl_cooldown")
    			.defineInRange("voidPearlCooldown", VOID_PEARL_COOLDOWN.getValueDefault(), 0, 32768);
    	
    	VOID_PEARL_BASE_DARKNESS_DAMAGE.configObj = common
    			.comment("Base damage dealt by Darkness every half a second, when it devours a creature in proximity of bearer of the pearl.")
    			.translation("configGui.enigmaticlegacy.void_pearl_base_darkness_damage")
    			.defineInRange("voidPearlBaseDarknessDamage", VOID_PEARL_BASE_DARKNESS_DAMAGE.getValueDefault(), 0.0, 1000.0);
    	
    	VOID_PEARL_REGENERATION_MODIFIER.configObj = common
    			.comment("Modifier for slowing down player's regeneration when bearing the pearl. This includes natural regeneration, as well as artificial healing effects that work over time. The greater it is, the slower player will regenerate.")
    			.translation("configGui.enigmaticlegacy.void_pearl_regeneration_modifier")
    			.defineInRange("voidPearlRegenerationModifier", VOID_PEARL_REGENERATION_MODIFIER.getValueDefault(), 0.0, 1000.0);
    	
    	VOID_PEARL_SHADOW_RANGE.configObj = common
    			.comment("Range in which Pearl of the Void will force darkness to devour living creatures.")
    			.translation("configGui.enigmaticlegacy.void_pearl_shadow_range")
    			.defineInRange("voidPearlShadowRange", VOID_PEARL_SHADOW_RANGE.getValueDefault(), 0.0, 128.0);
    	
    	VOID_PEARL_UNDEAD_PROBABILITY.configObj = common
    			.comment("Chance for Pearl of the Void to prevent it's bearer death from receiving lethal amout of damage. Defined as percentage.")
    			.translation("configGui.enigmaticlegacy.void_pearl_undead_chance")
    			.defineInRange("voidPearlUndeadChance", VOID_PEARL_UNDEAD_PROBABILITY.getValueDefault().asPercentage(), 0, 100);
    	
    	VOID_PEARL_WITHERING_EFFECT_TIME.configObj = common
    			.comment("Amout of ticks for which bearer of the pearl will apply Withering effect to entities they attack. 20 ticks equals to 1 second.")
    			.translation("configGui.enigmaticlegacy.void_pearl_withering_time")
    			.defineInRange("voidPearlWitheringTime", VOID_PEARL_WITHERING_EFFECT_TIME.getValueDefault(), 0, 32768);
    	
    	VOID_PEARL_WITHERING_EFFECT_LEVEL.configObj = common
    			.comment("Level of Withering that bearer of the pearl will apply to entitities they attack.")
    			.translation("configGui.enigmaticlegacy.void_pearl_withering_level")
    			.defineInRange("voidPearlWitheringLevel", VOID_PEARL_WITHERING_EFFECT_LEVEL.getValueDefault(), 0, 3);
    	
    	ENIGMATIC_ITEM_COOLDOWN.configObj = common
        		.comment("Active ability cooldown for Heart of Creation. Measured in ticks. 20 ticks equal to 1 second.")
        		.translation("configGui.enigmaticlegacy.enigmatic_item_cooldown")
        		.defineInRange("enigmaticItemCooldown", ENIGMATIC_ITEM_COOLDOWN.getValueDefault(), 0, 32768);
    	
        common.pop();
        
        
        COMMON = common.build();
        CLIENT = client.build();
    }
    
    
    public static void resetConfig() {
    	for (IComplexParameter par : allValues)
    		par.reset();
	}
}

