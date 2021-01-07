package com.integral.enigmaticlegacy.config;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.IPerhaps;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.OverlayPositionHelper;
import com.integral.enigmaticlegacy.helpers.OverlayPositionHelper.AnchorPoint;
import com.integral.omniconfig.Configuration;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

public class OmniconfigHandler {
	private static final Map<Field, Omniconfig.BooleanParameter> itemsOptions = new HashMap<>();

	public static Omniconfig.BooleanParameter bonusWoolRecipesEnabled;
	public static Omniconfig.BooleanParameter disableAOEShiftSuppression;
	public static Omniconfig.BooleanParameter retriggerRecipeUnlocks;
	public static Omniconfig.BooleanParameter crashOnUnnamedPool;

	public static Omniconfig.IntParameter soulCrystalsMode;
	public static Omniconfig.IntParameter maxSoulCrystalLoss;

	public static Omniconfig.EnumParameter<AnchorPoint> testEnumParam;

	// Client-Only

	public static boolean isItemEnabled(Object item) {
		if (item == null)
			return false;

		// TODO Try to put EnabledCondition in recipe unlocking advancements

		for (Field optionalItemField : itemsOptions.keySet()) {
			try {
				//System.out.println("Field: " + optionalItemField);
				if (optionalItemField.get(null) != null) {
					Object optionalItem = optionalItemField.get(null);
					//System.out.println("Object: " + optionalItem);
					//System.out.println("Passed object: " + item);
					//System.out.println("Equal?: " + Objects.equal(item, optionalItem));

					if (Objects.equal(item, optionalItem) && itemsOptions.get(optionalItemField) != null)
						//System.out.println("Checks out; returning this boy.");
						return itemsOptions.get(optionalItemField).getValue();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		if (item instanceof IPerhaps)
			return ((IPerhaps)item).isForMortals();
		else
			return true;
	}


	public static void initialize() {
		OmniconfigWrapper configBuilder = OmniconfigWrapper.setupBuilder("enigmaticlegacy-common", true, "2.1");
		configBuilder.pushVersioningPolicy(Configuration.VersioningPolicy.AGGRESSIVE);
		configBuilder.pushTerminateNonInvokedKeys(true);

		loadCommon(configBuilder);

		configBuilder.setReloadable();

		OmniconfigWrapper clientBuilder = OmniconfigWrapper.setupBuilder("enigmaticlegacy-client", true, "2.0");
		clientBuilder.pushSidedType(Configuration.SidedConfigType.CLIENT);
		clientBuilder.pushVersioningPolicy(Configuration.VersioningPolicy.AGGRESSIVE);
		clientBuilder.pushTerminateNonInvokedKeys(true);

		loadClient(clientBuilder);

		clientBuilder.setReloadable();
	}

	private static void loadClient(final OmniconfigWrapper client) {
		client.loadConfigFile();
		client.pushCategory("Generic Config", "Some more different stuff");

		/*
		testEnumParam = client
				.comment("Test enum option")
				.clientOnly()
				.getEnum("testEnum", AnchorPoint.BOTTOM, AnchorPoint.BOTTOM, AnchorPoint.CENTER, AnchorPoint.TOP);
		 */

		SuperpositionHandler.dispatchWrapperToHolders(EnigmaticLegacy.MODID, client);

		client.popCategory();
		client.build();
	}

	private static void loadCommon(final OmniconfigWrapper builder) {
		builder.loadConfigFile();
		builder.pushCategory("Accessibility Options",
				"You may disable certain items or features from being obtainable/usable here." + System.lineSeparator() +
				"Please note that as of release 2.6.0 of Enigmatic Legacy, those options are automatically generated" + System.lineSeparator() +
				"for most items in the mod. They may refer to items that do not exist yet or are not obtainable in any" + System.lineSeparator() +
				"case, and may not work for certain items due to non-generic obtaining methods or generic oversight." + System.lineSeparator() + System.lineSeparator() +
				"If you discover option that does not work, but for whatever reason you really need it, submit an issue" + System.lineSeparator() +
				"pointing out to such option here: https://github.com/Extegral/Enigmatic-Legacy/issues");

		bonusWoolRecipesEnabled = builder
				.comment("Whether or not bonus recipes for wool dyeing should be enabled.")
				.sync()
				.getBoolean("BonusWoolRecipesEnabled", true);

		crashOnUnnamedPool = builder
				.comment("Whether or not Enigmatic Legacy should purposefully crash client whenever any mod tries to inject unnamed "
						+ "LootPool into any loot table. If false, stacktraces will be printed to log but game will proceed as normal.")
				.getBoolean("CrashOnUnnamedPool", false);

		disableAOEShiftSuppression = builder
				.comment("If true, tools with area of effect abilities will not have those abilities disabled when player holds Shift (crouches).")
				.sync()
				.getBoolean("DisableAOEShiftSuppression", false);

		retriggerRecipeUnlocks = builder
				.comment("If true, Enigmatic Legacy will cycle through each player's recipe book and trigger 'minecraft:recipe_unlocked' criterion trigger for everything that they have unlocked upon player joining the world.")
				.getBoolean("RetriggerRecipeUnlocks", true);

		Multimap<String, Field> accessibilityGeneratorMap = SuperpositionHandler.retainAccessibilityGeneratorMap(EnigmaticLegacy.MODID);
		itemsOptions.clear();
		builder.forceSynchronized(true);

		for (String itemName : accessibilityGeneratorMap.keySet()) {
			String optionName = itemName.replaceAll("[^a-zA-Z0-9]", "")+"Enabled";
			//String firstLetter = optionName.substring(0, 2).toLowerCase();
			//optionName = firstLetter + optionName.substring(1, optionName.length());

			Omniconfig.BooleanParameter param = builder.comment("Whether or not " + itemName + " should be enabled.").getBoolean(optionName, true);
			for (Field associatedField : accessibilityGeneratorMap.get(itemName)) {
				itemsOptions.put(associatedField, param);
			}
		}

		builder.forceSynchronized(false);
		builder.popCategory();


		builder.pushCategory("Generic Config", "Some different stuff");

		soulCrystalsMode = builder
				.comment("Soul Crystals mechanic mode. 0 - disabled unless enforced by specific in-game items; 1 - also enabled when keepInventory is true; 2 - always enabled.")
				.max(2)
				.getInt("SoulCrystalsMode", 0);

		maxSoulCrystalLoss = builder
				.comment("Maximum amount of Soul Crystals a player can loose before they won't drop anymore. Each crystal lost subtracts 10% of from their maximum possible health value.")
				.min(1)
				.max(10)
				.getInt("MaxSoulCrystalLoss", 9);

		builder.popCategory();


		builder.pushCategory("Balance Options", "Various options that mostly affect individual items");
		builder.forceSynchronized(true);
		SuperpositionHandler.dispatchWrapperToHolders(EnigmaticLegacy.MODID, builder);
		builder.forceSynchronized(false);
		builder.popCategory();

		builder.build();

		//builder.comment("Balancing and other option for all the spellstones").push("Spellstones Options");
		//builder.pop();

	}

}

