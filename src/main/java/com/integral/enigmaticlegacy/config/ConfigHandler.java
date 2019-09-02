package com.integral.enigmaticlegacy.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
    public final ForgeConfigSpec COMMON;
    
    public final ForgeConfigSpec.IntValue MAGNET_RING_RANGE;
    public final ForgeConfigSpec.IntValue SUPER_MAGNET_RING_RANGE;
    //public final ForgeConfigSpec.DoubleValue FORBIDDEN_AXE_ATTACK_DAMAGE;
    //public final ForgeConfigSpec.DoubleValue FORBIDDEN_AXE_ATTACK_SPEED;
    public final ForgeConfigSpec.IntValue FORBIDDEN_AXE_BEHEADING_BASE;
    public final ForgeConfigSpec.IntValue FORBIDDEN_AXE_BEHEADING_BONUS;
    public final ForgeConfigSpec.IntValue EXTRAPOLATED_MEGASPONGE_RADIUS;
    public final ForgeConfigSpec.IntValue MONSTER_CHARM_UNDEAD_DAMAGE;
    public final ForgeConfigSpec.IntValue MONSTER_CHARM_AGGRESSIVE_DAMAGE;
    public final ForgeConfigSpec.IntValue MINING_CHARM_BREAK_BOOST;
    public final ForgeConfigSpec.DoubleValue MINING_CHARM_REACH_BOOST;
    public final ForgeConfigSpec.DoubleValue XP_SCROLL_COLLECTION_RANGE;
    public final ForgeConfigSpec.DoubleValue HEAVEN_SCROLL_XP_COST_MODIFIER;
    
    public final ForgeConfigSpec.BooleanValue MONSTER_CHARM_BONUS_LOOTING;
    public final ForgeConfigSpec.BooleanValue MONSTER_CHARM_DOUBLE_XP;
    public final ForgeConfigSpec.BooleanValue MINING_CHARM_BONUS_LUCK;
    
    public final ForgeConfigSpec.BooleanValue SUPER_MAGNET_RING_SOUND;
    
    //SPELLSTONES SPECIFIC
    public final ForgeConfigSpec.IntValue ANGEL_BLESSING_COOLDOWN;
    public final ForgeConfigSpec.IntValue GOLEM_HEART_COOLDOWN;
    public final ForgeConfigSpec.IntValue BLAZING_CORE_COOLDOWN;
    public final ForgeConfigSpec.IntValue OCEAN_STONE_COOLDOWN;
    public final ForgeConfigSpec.IntValue EYE_OF_NEBULA_COOLDOWN;
    public final ForgeConfigSpec.IntValue VOID_PEARL_COOLDOWN;
    
    public final ForgeConfigSpec.DoubleValue ANGEL_BLESSING_ACCELERATION_MODIFIER;
    public final ForgeConfigSpec.DoubleValue ANGEL_BLESSING_ACCELERATION_MODIFIER_ELYTRA;
    
    public final ForgeConfigSpec.DoubleValue GOLEM_HEART_DEFAULT_ARMOR;
    public final ForgeConfigSpec.DoubleValue GOLEM_HEART_SUPER_ARMOR;
    public final ForgeConfigSpec.DoubleValue GOLEM_HEART_SUPER_ARMOR_TOUGHNESS;
    public final ForgeConfigSpec.IntValue GOLEM_HEART_KNOCKBACK_RESISTANCE;
    public final ForgeConfigSpec.IntValue GOLEM_HEART_MELEE_RESISTANCE;
    public final ForgeConfigSpec.IntValue GOLEM_HEART_EXPLOSION_RESISTANCE;
    public final ForgeConfigSpec.DoubleValue GOLEM_HEART_VULNERABILITY_MODIFIER;
    
    public final ForgeConfigSpec.DoubleValue BLAZING_CORE_DAMAGE_FEEDBACK;
    public final ForgeConfigSpec.IntValue BLAZING_CORE_IGNITION_FEEDBACK;
    
    public final ForgeConfigSpec.DoubleValue OCEAN_STONE_XP_COST_MODIFIER;
    public final ForgeConfigSpec.IntValue OCEAN_STONE_UNDERWATER_CREATURES_RESISTANCE;
    public final ForgeConfigSpec.IntValue OCEAN_STONE_SWIMMING_SPEED_BOOST;
    
    public final ForgeConfigSpec.IntValue EYE_OF_NEBULA_MAGIC_RESISTANCE;
    public final ForgeConfigSpec.IntValue EYE_OF_NEBULA_DODGE_PROBABILITY;
    public final ForgeConfigSpec.DoubleValue EYE_OF_NEBULA_DODGE_RANGE;
    public final ForgeConfigSpec.DoubleValue EYE_OF_NEBULA_PHASE_RANGE;
    
    public final ForgeConfigSpec.DoubleValue VOID_PEARL_REGENERATION_MODIFIER;
    public final ForgeConfigSpec.IntValue VOID_PEARL_WITHERING_EFFECT_LEVEL;
    public final ForgeConfigSpec.IntValue VOID_PEARL_WITHERING_EFFECT_TIME;
    public final ForgeConfigSpec.DoubleValue VOID_PEARL_SHADOW_RANGE;
    public final ForgeConfigSpec.DoubleValue VOID_PEARL_BASE_DARKNESS_DAMAGE;
    public final ForgeConfigSpec.IntValue VOID_PEARL_UNDEAD_PROBABILITY;
    
    
    public final ForgeConfigSpec.BooleanValue ANGEL_BLESSING_ENABLED;
    public final ForgeConfigSpec.BooleanValue ENDER_RING_ENABLED;
    public final ForgeConfigSpec.BooleanValue ESCAPE_SCROLL_ENABLED;
    public final ForgeConfigSpec.BooleanValue EXTRADIMENSIONAL_EYE_ENABLED;
    public final ForgeConfigSpec.BooleanValue EYE_OF_NEBULA_ENABLED;
    public final ForgeConfigSpec.BooleanValue FORBIDDEN_AXE_ENABLED;
    public final ForgeConfigSpec.BooleanValue GOLEM_HEART_ENABLED;
    public final ForgeConfigSpec.BooleanValue HASTE_POTION_ENABLED;
    public final ForgeConfigSpec.BooleanValue HEAVEN_SCROLL_ENABLED;
    public final ForgeConfigSpec.BooleanValue IRON_RING_ENABLED;
    public final ForgeConfigSpec.BooleanValue MAGMA_HEART_ENABLED;
    public final ForgeConfigSpec.BooleanValue MAGNET_RING_ENABLED;
    public final ForgeConfigSpec.BooleanValue MEGASPONGE_ENABLED;
    public final ForgeConfigSpec.BooleanValue MENDING_MIXTURE_ENABLED;
    public final ForgeConfigSpec.BooleanValue MINING_CHARM_ENABLED;
    public final ForgeConfigSpec.BooleanValue MONSTER_CHARM_ENABLED;
    public final ForgeConfigSpec.BooleanValue OCEAN_STONE_ENABLED;
    public final ForgeConfigSpec.BooleanValue RECALL_POTION_ENABLED;
    public final ForgeConfigSpec.BooleanValue SUPER_MAGNET_RING_ENABLED;
    public final ForgeConfigSpec.BooleanValue UNHOLY_GRAIL_ENABLED;
    public final ForgeConfigSpec.BooleanValue VOID_PEARL_ENABLED;
    public final ForgeConfigSpec.BooleanValue XP_SCROLL_ENABLED;


    public ConfigHandler() {
        final ForgeConfigSpec.Builder common = new ForgeConfigSpec.Builder();

        common.comment("Enable or disable certain items. Disabled items will not be craftable and will not generate in dungeons").push("Accesibility Config");
        
        this.ANGEL_BLESSING_ENABLED = common
                .comment("Whether or not Angel's Blessing should be enabled.")
                .translation("configGui.enigmaticlegacy.angel_blessing_enabled")
                .define("angelBlessingEnabled", true);
        
        this.ENDER_RING_ENABLED = common
        		.comment("Whether or not Ender Ring should be enabled.")
        		.translation("configGui.enigmaticlegacy.ender_ring_enabled")
        		.define("enderRingEnabled", true);
        
        this.ESCAPE_SCROLL_ENABLED = common
        		.comment("Whether or not Scroll of Postmortal Recall should be enabled.")
        		.translation("configGui.enigmaticlegacy.escape_scroll_enabled")
        		.define("escapeScrollEnabled", true);
        
        this.EXTRADIMENSIONAL_EYE_ENABLED = common
        		.comment("Whether or not Extradimensional Eye should be enabled.")
        		.translation("configGui.enigmaticlegacy.extradimensional_eye_enabled")
        		.define("extradimensionalEyeEnabled", true);
        
        this.EYE_OF_NEBULA_ENABLED = common
        		.comment("Whether or not Eye of the Nebula should be enabled.")
        		.translation("configGui.enigmaticlegacy.eye_of_nebula_enabled")
        		.define("eyeOfNebulaEnabled", true);
        
        this.FORBIDDEN_AXE_ENABLED = common
        		.comment("Whether or not Axe of Executioner should be enabled.")
        		.translation("configGui.enigmaticlegacy.forbidden_axe_enabled")
        		.define("forbiddenAxeEnabled", true);
        
        this.GOLEM_HEART_ENABLED = common
        		.comment("Whether or not Heart of the Golem should be enabled.")
        		.translation("configGui.enigmaticlegacy.golem_heart_enabled")
        		.define("golemHeartEnabled", true);
        
        this.HASTE_POTION_ENABLED = common
        		.comment("Whether or not Potion of Haste should be enabled.")
        		.translation("configGui.enigmaticlegacy.haste_potion_enabled")
        		.define("hastePotionEnabled", true);
        
        this.HEAVEN_SCROLL_ENABLED = common
        		.comment("Whether or not Gift of the Heaven should be enabled.")
        		.translation("configGui.enigmaticlegacy.heaven_scroll_enabled")
        		.define("heavenScrollEnabled", true);
        
        this.IRON_RING_ENABLED = common
        		.comment("Whether or not Iron Ring should be enabled.")
        		.translation("configGui.enigmaticlegacy.iron_ring_enabled")
        		.define("ironRingEnabled", true);
        
        this.MAGMA_HEART_ENABLED = common
        		.comment("Whether or not Blazing Core should be enabled.")
        		.translation("configGui.enigmaticlegacy.blazing_core_enabled")
        		.define("blazingCoreEnabled", true);
        
        this.MAGNET_RING_ENABLED = common
        		.comment("Whether or not Magnetic Ring should be enabled.")
        		.translation("configGui.enigmaticlegacy.magnet_ring_enabled")
        		.define("magnetRingEnabled", true);
        
        this.MEGASPONGE_ENABLED = common
        		.comment("Whether or not Extrapolated Megasponge should be enabled.")
        		.translation("configGui.enigmaticlegacy.megasponge_enabled")
        		.define("megaSpongeEnabled", true);
        
        this.MENDING_MIXTURE_ENABLED = common
        		.comment("Whether or not Mending Mixture should be enabled.")
        		.translation("configGui.enigmaticlegacy.mending_mixture_enabled")
        		.define("mendingMixtureEnabled", true);
        
        this.MINING_CHARM_ENABLED = common
        		.comment("Whether or not Charm of Treasure Hunter should be enabled.")
        		.translation("configGui.enigmaticlegacy.mining_charm_enabled")
        		.define("miningCharmEnabled", true);
        
        this.MONSTER_CHARM_ENABLED = common
        		.comment("Whether or not Emblem of Monster Slayer should be enabled.")
        		.translation("configGui.enigmaticlegacy.monster_charm_enabled")
        		.define("monsterCharmEnabled", true);
        
        this.OCEAN_STONE_ENABLED = common
        		.comment("Whether or not Will of the Ocean should be enabled.")
        		.translation("configGui.enigmaticlegacy.ocean_stone_enabled")
        		.define("oceanStoneEnabled", true);
        
        this.RECALL_POTION_ENABLED = common
        		.comment("Whether or not Potion of Recall should be enabled.")
        		.translation("configGui.enigmaticlegacy.recall_potion_enabled")
        		.define("recallPotionEnabled", true);
        
        this.UNHOLY_GRAIL_ENABLED = common
        		.comment("Whether or not Unholy Grail should be enabled.")
        		.translation("configGui.enigmaticlegacy.unholy_grail_enabled")
        		.define("unholyGrailEnabled", true);
        
        this.SUPER_MAGNET_RING_ENABLED = common
        		.comment("Whether or not Ring of Dislocation should be enabled.")
        		.translation("configGui.enigmaticlegacy.super_magnet_ring_enabled")
        		.define("superMagnetRingEnabled", true);
        
        this.VOID_PEARL_ENABLED = common
        		.comment("Whether or not Pearl of the Void should be enabled.")
        		.translation("configGui.enigmaticlegacy.void_pearl_enabled")
        		.define("voidPearlEnabled", true);
        
        this.XP_SCROLL_ENABLED = common
        		.comment("Whether or not Scroll of Ageless Wisdom should be enabled.")
        		.translation("configGui.enigmaticlegacy.xp_scroll_enabled")
        		.define("xpScrollEnabled", true);
        
        common.pop();
        
        
        
        
        common.comment("Just some different stuff").push("Generic Config");
        
        this.SUPER_MAGNET_RING_SOUND = common
                .comment("Whether or not Dislocation Ring should play any sounds when teleporting items to it's bearer.")
                .translation("configGui.enigmaticlegacy.super_magnet_ring_sound")
                .define("superMagnetRingSound", true);
        
        common.pop();
        
        
        
        
        common.comment("Various options that affect individual items").push("Balance Options");
        
        this.MAGNET_RING_RANGE = common
                .comment("The radius in which Magnetic Ring will attract items.")
                .translation("configGui.enigmaticlegacy.magnet_ring_range")
                .defineInRange("magnetRingRange", 8, 1, 32);
        
        this.SUPER_MAGNET_RING_RANGE = common
        		.comment("The radius in which Dislocation Ring will collect items.")
        		.translation("configGui.enigmaticlegacy.super_magnet_ring_range")
        		.defineInRange("superMagnetRingRange", 16, 1, 128);
        /*
        this.FORBIDDEN_AXE_ATTACK_DAMAGE = common
        		.comment("Default attack damage of Axe of Executioner.")
        		.translation("configGui.enigmaticlegacy.forbidden_axe_attack_damage")
        		.defineInRange("forbiddenAxeAttackDamage", 10.0, 1.0, 1000.0);
        
        this.FORBIDDEN_AXE_ATTACK_SPEED = common
        		.comment("Default attack speed of Axe of Executioner.")
        		.translation("configGui.enigmaticlegacy.forbidden_axe_attack_speed")
        		.defineInRange("forbiddenAxeAttackSpeed", 2.4, 0.0, 1000.0);
		*/
        this.FORBIDDEN_AXE_BEHEADING_BASE = common
        		.comment("Default chance to behead an enemy with Axe of Executioner. Defined as percentage.")
        		.translation("configGui.enigmaticlegacy.forbidden_axe_beheading_base")
        		.defineInRange("forbiddenAxeBeheadingBase", 10, 0, 100);

        this.FORBIDDEN_AXE_BEHEADING_BONUS = common
        		.comment("Bonus percantage to beheading chance from each looting level applied to Axe of Executioner.")
        		.translation("configGui.enigmaticlegacy.forbidden_axe_beheading_bonus")
        		.defineInRange("forbiddenBeheadingBonus", 5, 0, 100);
        
        this.EXTRAPOLATED_MEGASPONGE_RADIUS = common
        		.comment("Radius in which Exptrapolated Megaspong absorbs water. Default 4 equals to vanilla sponge")
        		.translation("configGui.enigmaticlegacy.megasponge_radius")
        		.defineInRange("megaspongeRadius", 4, 0, 128);
        
        this.MONSTER_CHARM_UNDEAD_DAMAGE = common
        		.comment("Damage multiplier against undead enemies for Emblem of Monster Slayer. Defined as percentage.")
        		.translation("configGui.enigmaticlegacy.monster_charm_undead_damage")
        		.defineInRange("monsterCharmUndeadDamage", 25, 0, 1000);
        
        this.MONSTER_CHARM_AGGRESSIVE_DAMAGE = common
        		.comment("Damage multiplier against agressive creatures for Emblem of Monster Slayer. Defined as percentage.")
        		.translation("configGui.enigmaticlegacy.monster_charm_agressive_damage")
        		.defineInRange("monsterCharmAgressiveDamage", 10, 0, 1000);
        
        this.MONSTER_CHARM_BONUS_LOOTING = common
        		.comment("Whether or not Emblem of Monster Slayer should provide +1 Looting Level.")
        		.translation("configGui.enigmaticlegacy.monster_charm_bonus_looting")
        		.define("monsterCharmBonusLooting", true);
        
        this.MONSTER_CHARM_DOUBLE_XP = common
        		.comment("Whether or not Emblem of Monster Slayer should provide double experience drop from monsyers.")
        		.translation("configGui.enigmaticlegacy.monster_charm_bonus_xp")
        		.define("monsterCharmBonusXP", true);
        
        this.MINING_CHARM_BREAK_BOOST = common
        		.comment("Mining speed boost granted by Charm of Treasure Hunter. Defined as percentage.")
        		.translation("configGui.enigmaticlegacy.mining_charm_break_boost")
        		.defineInRange("miningCharmBreakBoost", 50, 0, 1000);
        
        this.MINING_CHARM_REACH_BOOST = common
        		.comment("Additional block reach granted by Charm of Treasure Hunter.")
        		.translation("configGui.enigmaticlegacy.mining_charm_block_reach")
        		.defineInRange("miningCharmBlockReach", 2.15D, 0.0D, 16D);
        
        this.MINING_CHARM_BONUS_LUCK = common
        		.comment("Whether or not Charm of Treasure Hunter should provide +1 Luck.")
        		.translation("configGui.enigmaticlegacy.mining_charm_bonus_luck")
        		.define("miningCharmBonusLuck", true);
        
        this.HEAVEN_SCROLL_XP_COST_MODIFIER = common
        		.comment("Multiplier for experience consumption by Gift of the Heaven.")
        		.translation("configGui.enigmaticlegacy.heaven_scroll_xp_cost_modifier")
        		.defineInRange("heavenScrollXPCostModifier", 1.0D, 0.0D, 1000.0D);
        
        this.XP_SCROLL_COLLECTION_RANGE = common
        		.comment("Range in which Scroll of Ageless Wisdom collects experience orbs when active.")
        		.translation("configGui.enigmaticlegacy.xp_scroll_collection_range")
        		.defineInRange("xpScrollCollectionRange", 16.0D, 1.0D, 128.0D);
        
        common.pop();
        
        
        
        common.comment("Balancing and other option for all the spellstones").push("Spellstones Options");
        
        this.ANGEL_BLESSING_COOLDOWN = common
        		.comment("Active ability cooldown for Angel's Blessing. Measured in ticks. 20 ticks equal to 1 second.")
        		.translation("configGui.enigmaticlegacy.angel_blessing_cooldown")
        		.defineInRange("angelBlessingCooldown", 40, 0, 32768);
    	
    	this.ANGEL_BLESSING_ACCELERATION_MODIFIER = common
    			.comment("Acceleration modifier for active ability of Angel's Blessing. The greater it is, the more momentum you will gain.")
    			.translation("configGui.enigmaticlegacy.angel_blessing_acceleration_modifier")
    			.defineInRange("angelBlessingAccelerationModifier", 1.0, 0.0, 256.0);
    	
    	this.ANGEL_BLESSING_ACCELERATION_MODIFIER_ELYTRA = common
    			.comment("Separate acceleration modifier for active ability of Angel's Blessing when player is flying with Elytra.")
    			.translation("configGui.enigmaticlegacy.angel_blessing_acceleration_modifier_elytra")
    			.defineInRange("angelBlessingAccelerationModifierElytra", 0.6, 0.0, 256.0);
    	
    	
    	
    	this.GOLEM_HEART_COOLDOWN = common
    			.comment("Active ability cooldown for Heart of the Golem. Measured in ticks. 20 ticks equal to 1 second.")
    			.translation("configGui.enigmaticlegacy.golem_heart_cooldown")
    			.defineInRange("golemHeartCooldown", 0, 0, 32768);
    	
    	this.GOLEM_HEART_DEFAULT_ARMOR = common
    			.comment("Default amount of armor points provided by Heart of the Golem.")
    			.translation("configGui.enigmaticlegacy.golem_heart_default_armor")
    			.defineInRange("golemHeartDefaultArmor", 4.0, 0.0, 256.0);
    	
    	this.GOLEM_HEART_SUPER_ARMOR = common
    			.comment("The amount of armor points provided by Heart of the Golem when it's bearer has no armor equipped.")
    			.translation("configGui.enigmaticlegacy.golem_heart_super_armor")
    			.defineInRange("golemHeartSuperArmor", 16.0, 0.0, 256.0);
    	
    	this.GOLEM_HEART_SUPER_ARMOR_TOUGHNESS = common
    			.comment("The amount of armor toughness provided by Heart of the Golem when it's bearer has no armor equipped.")
    			.translation("configGui.enigmaticlegacy.golem_heart_super_armor_toughness")
    			.defineInRange("golemHeartSuperArmorToughness", 4.0, 0.0, 256.0);
    	
    	this.GOLEM_HEART_MELEE_RESISTANCE = common
    			.comment("Resistance to melee attacks provided by Heart of the Golem. Defined as percentage.")
    			.translation("configGui.enigmaticlegacy.golem_heart_melee_resistance")
    			.defineInRange("golemHeartMeleeResistance", 25, 0, 100);
    	
    	this.GOLEM_HEART_EXPLOSION_RESISTANCE = common
    			.comment("Resistance to explosion damage provided by Heart of the Golem. Defined as percentage.")
    			.translation("configGui.enigmaticlegacy.golem_heart_explosion_resistance")
    			.defineInRange("golemHeartExplosionResistance", 40, 0, 100);
    	
    	this.GOLEM_HEART_KNOCKBACK_RESISTANCE = common
    			.comment("Resistance to knockback provided by Heart of the Golem. Defined as percentage.")
    			.translation("configGui.enigmaticlegacy.golem_heart_knockback_resistance")
    			.defineInRange("golemHeartKnockbackResistance", 100, 0, 100);
    	
    	this.GOLEM_HEART_VULNERABILITY_MODIFIER = common
    			.comment("Modifier for Magic Damage vulnerability applied by Heart of the Golem. Default value of 2.0 means that player will receive twice as much damage from magic.")
    			.translation("configGui.enigmaticlegacy.golem_heart_vulnerability_modifier")
    			.defineInRange("golemHeartVulnerabilityModifier", 2.0, 1.0, 256.0);
    	
    	
    	
    	this.BLAZING_CORE_COOLDOWN = common
    			.comment("Active ability cooldown for Blazing Core. Measured in ticks. 20 ticks equal to 1 second.")
    			.translation("configGui.enigmaticlegacy.blazing_core_cooldown")
    			.defineInRange("blazingCoreCooldown", 0, 0, 32768);
    	
    	this.BLAZING_CORE_DAMAGE_FEEDBACK = common
    			.comment("How much fire-based damage instantly receives any creature that attacks bearer of the Blazing Core.")
    			.translation("configGui.enigmaticlegacy.blazing_core_damage_feedback")
    			.defineInRange("blazingCoreDamageFeedback", 4.0, 0.0, 512.0);
    	
    	this.BLAZING_CORE_IGNITION_FEEDBACK = common
    			.comment("How how many seconds any creature that attacks bearer of the Blazing Core will be set on fire.")
    			.translation("configGui.enigmaticlegacy.blazing_core_damage_feedback")
    			.defineInRange("blazingCoreIgnitionFeedback", 4, 0, 512);
    	
    	
    	
    	this.OCEAN_STONE_COOLDOWN = common
    			.comment("Active ability cooldown for Will of the Ocean. Measured in ticks. 20 ticks equal to 1 second.")
    			.translation("configGui.enigmaticlegacy.ocean_stone_cooldown")
    			.defineInRange("oceanStoneCooldown", 600, 0, 32768);
    	
    	this.OCEAN_STONE_SWIMMING_SPEED_BOOST = common
    			.comment("Swimming speed boost provided by Will of the Ocean. Defined as percentage.")
    			.translation("configGui.enigmaticlegacy.ocean_stone_swim_boost")
    			.defineInRange("oceanStoneSwimBoost", 200, 0, 1000);
    	
    	this.OCEAN_STONE_UNDERWATER_CREATURES_RESISTANCE = common
    			.comment("Damage resistance against underwater creatures provided by Will of the Ocean. Defined as percentage.")
    			.translation("configGui.enigmaticlegacy.ocean_stone_underwater_creatures_resistance")
    			.defineInRange("oceanStoneUnderwaterCreaturesResistance", 50, 0, 100);
    	
    	this.OCEAN_STONE_XP_COST_MODIFIER = common
    			.comment("Multiplier for experience consumption by active ability of Will of the Ocean.")
    			.translation("configGui.enigmaticlegacy.ocean_stone_xp_cost_modifier")
    			.defineInRange("oceanStoneXPCostModifier", 1.0, 0.0, 1000.0);

    	
    	
    	this.EYE_OF_NEBULA_COOLDOWN = common
    			.comment("Active ability cooldown for Eye of the Nebula. Measured in ticks. 20 ticks equal to 1 second.")
    			.translation("configGui.enigmaticlegacy.eye_of_nebula_cooldown")
    			.defineInRange("eyeOfNebulaCooldown", 60, 0, 32768);
    	
    	this.EYE_OF_NEBULA_DODGE_PROBABILITY = common
    			.comment("Probability for Eye of the Nebula to teleport it's bearer from any attack without receiving any damage. Defined as percentage.")
    			.translation("configGui.enigmaticlegacy.eye_of_nebula_dodge_chance")
    			.defineInRange("eyeOfNebulaDodgeChance", 15, 0, 100);

    	this.EYE_OF_NEBULA_DODGE_RANGE = common
    			.comment("Range in which Eye of the Nebula searches for a position to teleport it's bearer to when dodging the attack.")
    			.translation("configGui.enigmaticlegacy.eye_of_nebula_dodge_range")
    			.defineInRange("eyeOfNebulaDodgeRange", 16.0, 1.0, 128.0);
    	
    	this.EYE_OF_NEBULA_PHASE_RANGE = common
    			.comment("Range in which Eye of the Nebula can reach an entity when using it's active ability.")
    			.translation("configGui.enigmaticlegacy.eye_of_nebula_phase_range")
    			.defineInRange("eyeOfNebulaPhaseRange", 32.0, 1.0, 128.0);
    	
    	this.EYE_OF_NEBULA_MAGIC_RESISTANCE = common
    			.comment("Magic Damage resistance provided by Eye of the Nebula. Defined as percentage.")
    			.translation("configGui.enigmaticlegacy.eye_of_nebula_magic_resistance")
    			.defineInRange("eyeOfNebulaMagicResistance", 65, 0, 100);
    	
    	
    	
    	this.VOID_PEARL_COOLDOWN = common
    			.comment("Active ability cooldown for Pearl of the Void. Measured in ticks. 20 ticks equal to 1 second.")
    			.translation("configGui.enigmaticlegacy.void_pearl_cooldown")
    			.defineInRange("voidPearlCooldown", 0, 0, 32768);
    	
    	this.VOID_PEARL_BASE_DARKNESS_DAMAGE = common
    			.comment("Base damage dealt by Darkness every half a second, when it devours a creature in proximity of bearer of the pearl.")
    			.translation("configGui.enigmaticlegacy.void_pearl_base_darkness_damage")
    			.defineInRange("voidPearlBaseDarknessDamage", 4.0, 0.0, 1000.0);
    	
    	this.VOID_PEARL_REGENERATION_MODIFIER = common
    			.comment("Modifier for slowing down player's regeneration when bearing the pearl. This includes natural regeneration, as well as artificial healing effects that work over time. The greater it is, the slower player will regenerate.")
    			.translation("configGui.enigmaticlegacy.void_pearl_regeneration_modifier")
    			.defineInRange("voidPearlRegenerationModifier", 1.0, 0.0, 1000.0);
    	
    	this.VOID_PEARL_SHADOW_RANGE = common
    			.comment("Range in which Pearl of the Void will force darkness to devour living creatures.")
    			.translation("configGui.enigmaticlegacy.void_pearl_shadow_range")
    			.defineInRange("voidPearlShadowRange", 16.0, 0.0, 128.0);
    	
    	this.VOID_PEARL_UNDEAD_PROBABILITY = common
    			.comment("Chance for Pearl of the Void to prevent it's bearer death from receiving lethal amout of damage. Defined as percentage.")
    			.translation("configGui.enigmaticlegacy.void_pearl_undead_chance")
    			.defineInRange("voidPearlUndeadChance", 15, 0, 100);
    	
    	this.VOID_PEARL_WITHERING_EFFECT_TIME = common
    			.comment("Amout of ticks for which bearer of the pearl will apply Withering effect to entities they attack. 20 ticks equals to 1 second.")
    			.translation("configGui.enigmaticlegacy.void_pearl_withering_time")
    			.defineInRange("voidPearlWitheringTime", 100, 0, 32768);
    	
    	this.VOID_PEARL_WITHERING_EFFECT_LEVEL = common
    			.comment("Level of Withering that bearer of the pearl will apply to entitities they attack.")
    			.translation("configGui.enigmaticlegacy.void_pearl_withering_level")
    			.defineInRange("voidPearlWitheringLevel", 2, 0, 3);
        
        common.pop();
        
        
        COMMON = common.build();
    }
}

