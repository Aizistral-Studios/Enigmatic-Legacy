package com.integral.enigmaticlegacy.config;

import com.integral.enigmaticlegacy.items.AngelBlessing;
import com.integral.enigmaticlegacy.items.AstralDust;
import com.integral.enigmaticlegacy.items.EnderRing;
import com.integral.enigmaticlegacy.items.EnigmaticAmulet;
import com.integral.enigmaticlegacy.items.EscapeScroll;
import com.integral.enigmaticlegacy.items.EtheriumIngot;
import com.integral.enigmaticlegacy.items.ExtradimensionalEye;
import com.integral.enigmaticlegacy.items.EyeOfNebula;
import com.integral.enigmaticlegacy.items.ForbiddenAxe;
import com.integral.enigmaticlegacy.items.GolemHeart;
import com.integral.enigmaticlegacy.items.HastePotion;
import com.integral.enigmaticlegacy.items.HeavenScroll;
import com.integral.enigmaticlegacy.items.IronRing;
import com.integral.enigmaticlegacy.items.LootGenerator;
import com.integral.enigmaticlegacy.items.MagmaHeart;
import com.integral.enigmaticlegacy.items.MagnetRing;
import com.integral.enigmaticlegacy.items.Megasponge;
import com.integral.enigmaticlegacy.items.MendingMixture;
import com.integral.enigmaticlegacy.items.MiningCharm;
import com.integral.enigmaticlegacy.items.MonsterCharm;
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

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHelper {
	
	public static void startCategory(ForgeConfigSpec.Builder builder, String category, String comment) {
		builder.comment(comment).push(category);
	}
	
	public static void finishCategory(ForgeConfigSpec.Builder builder) {
		builder.pop();
	}
	
	/**
	 * Sets a copy of config values into classes that use them.
	 * @author Integral
	 */
	
	public static void initializeConfigValues() {
    	
    	AngelBlessing.initConfigValues();
    	EnderRing.initConfigValues();
    	EnigmaticAmulet.initConfigValues();
    	EscapeScroll.initConfigValues();
    	EtheriumIngot.initConfigValues();
    	ExtradimensionalEye.initConfigValues();
    	EyeOfNebula.initConfigValues();
    	ForbiddenAxe.initConfigValues();
    	GolemHeart.initConfigValues();
    	HastePotion.initConfigValues();
    	HeavenScroll.initConfigValues();
    	IronRing.initConfigValues();
    	LootGenerator.initConfigValues();
    	MagmaHeart.initConfigValues();
    	MagnetRing.initConfigValues();
    	Megasponge.initConfigValues();
    	MendingMixture.initConfigValues();
    	MiningCharm.initConfigValues();
    	MonsterCharm.initConfigValues();
    	OceanStone.initConfigValues();
    	RecallPotion.initConfigValues();
    	RelicOfTesting.initConfigValues();
    	SuperMagnetRing.initConfigValues();
    	ThiccScroll.initConfigValues();
    	UnholyGrail.initConfigValues();
    	VoidPearl.initConfigValues();
    	XPScroll.initConfigValues();
    	AstralDust.initConfigValues();
    	UltimatePotionBase.initConfigValues();
    	UltimatePotionSplash.initConfigValues();
    	UltimatePotionLingering.initConfigValues();
    	
    }
	
}
