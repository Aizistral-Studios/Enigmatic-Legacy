package com.integral.enigmaticlegacy.config;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

import com.electronwill.nightconfig.core.file.FileWatcher;
import com.integral.omniconfig.Configuration;
import com.integral.omniconfig.wrappers.EnigmaConfig.BooleanParameter;
import com.integral.omniconfig.wrappers.EnigmaConfig.FloatParameter;
import com.integral.omniconfig.wrappers.EnigmaConfig.GenericParameter;
import com.integral.omniconfig.wrappers.EnigmaConfig.IntParameter;
import com.integral.omniconfig.wrappers.EnigmaConfigBuilder;

import net.minecraftforge.fml.loading.FMLPaths;

public class UltimaTestConfig {
	/*
	public static boolean ifAOEPickaxeEnabled;
	public static boolean ifAOEAxeEnabled;
	public static boolean ifAOEShovelEnabled;
	public static boolean ifAstralDamageHealingEnabled;

	public static int getEnchantability;
	public static int getAstralDivisor;
	public static int getAstralDamageMin;
	public static int getAstralDamageMax;

	public static boolean ifVoidStoneEnabled;
	public static int getVoidStoneListCap;
	public static int getVoidStoneHardCap;

	public static int getDurability;
	 */

	public static BooleanParameter ifAOEPickaxeEnabled;
	public static BooleanParameter ifAOEAxeEnabled;
	public static BooleanParameter ifAOEShovelEnabled;
	public static BooleanParameter ifAstralDamageHealingEnabled;

	public static IntParameter getEnchantability;
	public static IntParameter getAstralDivisor;
	public static IntParameter getAstralDamageMin;
	public static IntParameter getAstralDamageMax;

	public static BooleanParameter ifVoidStoneEnabled;
	public static IntParameter getVoidStoneListCap;
	public static IntParameter getVoidStoneHardCap;

	public static IntParameter getDurability;
	public static FloatParameter fovOverride;
	public static List<GenericParameter> invocationList;


	public void preInit() {
		EnigmaConfigBuilder configBuilder = EnigmaConfigBuilder.setupBuilder("dftoolkit", true, "1.0");
		configBuilder.pushVersioningPolicy(Configuration.VersioningPolicy.AGGRESSIVE);
		configBuilder.pushTerminateNonInvokedKeys(true);

		this.load(configBuilder);

		configBuilder.pushOverloadingAction(config -> {
			config.load();
			if (invocationList != null) {
				invocationList.forEach(param -> param.invoke(config));
			}
			config.save();
		});
		configBuilder.pushBeholderAttachment();
	}

	/*
		Configuration config;
		try {
			config = new Configuration(new File(FMLPaths.CONFIGDIR.get().toFile().getCanonicalFile(), "dftoolkit.omniconf"), "1.0", true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		config.setVersioningPolicy(Configuration.VersioningPolicy.AGGRESSIVE);
		config.setTerminateNonInvokedKeys(true);

		this.overload(config);

		config.attachOverloadingAction(this::overload);
		config.attachBeholder();
	}
	 */

	public void load(final EnigmaConfigBuilder builder) {
		builder.loadConfigFile();
		builder.pushCategory("General Config");

		getVoidStoneHardCap = builder
				.pushComment("How much items you can add into list of single Keystone of The Oblivion before you would be unable add nothing more. This limit exists to prevent players from occasional or intentional abusing, since multiple keystones with huge lists (like tens of thousands of items) may cause significant performance impact.")
				.pushMax(2048).generate("VoidStoneHardCap", 100);

		getVoidStoneListCap = builder
				.pushComment("Controls the amount of items that can be added into list of Keystone of The Oblivion, before displayble version of list in Ctrl tooltip stops expanding and becomes unreadable. You may want to increase or decrease it, depending on your screen resolution.")
				.pushMax(512).generate("VoidStoneListCap", 30);

		ifVoidStoneEnabled = builder
				.pushComment("Whether or not Keystone of The Oblivion should be enabled as a part of toolkit.")
				.generate("VoidStoneEnabled", true);

		ifAOEPickaxeEnabled = builder
				.pushComment("Whether or not AOE mining should be enabled on Astral Pickaxe.")
				.generate("AOEPickaxe", true);

		ifAOEAxeEnabled = builder
				.pushComment("Whether or not AOE mining should be enabled on Astral Axe.")
				.generate("AOEAxe", true);

		ifAOEShovelEnabled = builder
				.pushComment("Whether or not vertical mining should be enabled on Astral Shovel.")
				.generate("AOEShovel", true);

		ifAstralDamageHealingEnabled = builder
				.pushComment("Whether or not a part of Astral Damage should return as health or hunger.")
				.generate("AstralDamageHealing", true);

		// TODO It screws up for whatever reason

		fovOverride = builder
				.pushComment("FOV Override")
				.pushMin(-32768F)
				.pushMax(32768F)
				.generate("fovOverride", 1F);

		builder.popCategory();

		builder.pushCategory("Balance Options");

		getEnchantability = builder
				.pushComment("Enchantability of all Astral tools & armor.")
				.pushMax(1000).generate("AstraltiteEnchantability", 0);

		getAstralDivisor = builder
				.pushComment("Controls how much of Astral Damage is returned as health. Value of Astral Damage dealt is divided by this number before turning into healing.")
				.pushMax(1).pushMax(100).generate("AstralDivisor", 8);

		getAstralDamageMin = builder
				.pushComment("Minimal amount of Astral Damage that is dealt by Astral Blade.")
				.pushMax(10000).generate("AstralDamageMIN", 1);

		getAstralDamageMax = builder
				.pushComment("Maximum amount of Astral Damage that is dealt by Astral Blade.")
				.pushMax(10000).generate("AstralDamageMAX", 30);

		getDurability = builder
				.pushComment("Durability")
				.pushMax(10000).generate("Durability", 0);

		this.invocationList = builder.retrieveInvocationList();
		builder.popCategory();

		builder.build();
	}
	/*
	public synchronized void overload(final Configuration config) {
		config.load();

		getVoidStoneHardCap = config.getInt("VoidStoneHardCap", "General Config", 100, 0, 2048,
				"How much items you can add into list of single Keystone of The Oblivion before you would be unable add nothing more. This limit exists to prevent players from occasional or intentional abusing, since multiple keystones with huge lists (like tens of thousands of items) may cause significant performance impact.");

		getVoidStoneListCap = config.getInt("VoidStoneListCap", "General Config", 30, 0, 512,
				"Controls the amount of items that can be added into list of Keystone of The Oblivion, before displayble version of list in Ctrl tooltip stops expanding and becomes unreadable. You may want to increase or decrease it, depending on your screen resolution.");

		ifVoidStoneEnabled = config.getBoolean("VoidStoneEnabled", "General Config", true,
				"Whether or not Keystone of The Oblivion should be enabled as a part of toolkit.");

		ifAOEPickaxeEnabled = config.getBoolean("AOEPickaxe", "General Config", true,
				"Whether or not AOE mining should be enabled on Astral Pickaxe.");

		ifAOEAxeEnabled = config.getBoolean("AOEAxe", "General Config", true,
				"Whether or not AOE mining should be enabled on Astral Axe.");

		ifAOEShovelEnabled = config.getBoolean("AOEShovel", "General Config", true,
				"Whether or not vertical mining should be enabled on Astral Shovel.");

		ifAstralDamageHealingEnabled = config.getBoolean("AstralDamageHealing", "General Config", true,
				"Whether or not a part of Astral Damage should return as health or hunger.");

		getEnchantability = config.getInt("AstraltiteEnchantability", "Balance Options", 0, 0, 1000,
				"Enchantability of all Astral tools & armor.");

		getAstralDivisor = config.getInt("AstralDivisor", "Balance Options", 8, 1, 100,
				"Controls how much of Astral Damage is returned as health. Value of Astral Damage dealt is divided by this number before turning into healing.");

		getAstralDamageMin = config.getInt("AstralDamageMIN", "Balance Options", 1, 0, 10000,
				"Minimal amount of Astral Damage that is dealt by Astral Blade.");

		getAstralDamageMax = config.getInt("AstralDamageMAX", "Balance Options", 30, 0, 10000,
				"Maximum amount of Astral Damage that is dealt by Astral Blade.");

		getDurability = config.getInt("Durability", "Balance Options", 0, 0, 10000,
				"Durability");

		config.save();
	}
	 */
}
