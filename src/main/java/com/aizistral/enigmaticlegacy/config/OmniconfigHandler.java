package com.aizistral.enigmaticlegacy.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.items.IPerhaps;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.omniconfig.Configuration;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;
import com.google.common.base.Objects;
import com.google.common.collect.Multimap;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.patchouli.api.PatchouliConfigAccess.TextOverflowMode;

public class OmniconfigHandler {
	private static final Map<Field, Omniconfig.BooleanParameter> ITEMS_OPTIONS = new HashMap<>();

	public static Omniconfig.BooleanParameter customDungeonLootEnabled;
	public static Omniconfig.BooleanParameter bonusWoolRecipesEnabled;
	public static Omniconfig.BooleanParameter disableAOEShiftSuppression;
	public static Omniconfig.BooleanParameter retriggerRecipeUnlocks;
	public static Omniconfig.BooleanParameter crashOnUnnamedPool;

	public static Omniconfig.IntParameter soulCrystalsMode;
	public static Omniconfig.IntParameter maxSoulCrystalLoss;

	private static final List<ResourceLocation> bossList = new ArrayList<>();

	// Client-Only

	public static Omniconfig.EnumParameter<TextOverflowMode> acknowledgmentOverflowMode;
	public static Omniconfig.BooleanParameter angelBlessingDoubleJump;
	public static Omniconfig.BooleanParameter disableQuoteSubtitles;

	public static boolean isItemEnabled(Object item) {
		if (item == null)
			return false;

		// TODO Try to put EnabledCondition in recipe unlocking advancements

		for (Field optionalItemField : ITEMS_OPTIONS.keySet()) {
			try {
				//System.out.println("Field: " + optionalItemField);
				if (optionalItemField.get(null) != null) {
					Object optionalItem = optionalItemField.get(null);
					//System.out.println("Object: " + optionalItem);
					//System.out.println("Passed object: " + item);
					//System.out.println("Equal?: " + Objects.equal(item, optionalItem));

					if (Objects.equal(item, optionalItem) && ITEMS_OPTIONS.get(optionalItemField) != null)
						//System.out.println("Checks out; returning this boy.");
						return ITEMS_OPTIONS.get(optionalItemField).getValue();
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
		OmniconfigWrapper configBuilder = OmniconfigWrapper.setupBuilder("enigmaticlegacy-common", true, "2.2");
		configBuilder.pushVersioningPolicy(Configuration.VersioningPolicy.AGGRESSIVE);
		configBuilder.pushTerminateNonInvokedKeys(true);

		loadCommon(configBuilder);

		configBuilder.setReloadable();

		OmniconfigWrapper clientBuilder = OmniconfigWrapper.setupBuilder("enigmaticlegacy-client", true, "2.2");
		clientBuilder.pushSidedType(Configuration.SidedConfigType.CLIENT);
		clientBuilder.pushVersioningPolicy(Configuration.VersioningPolicy.AGGRESSIVE);
		clientBuilder.pushTerminateNonInvokedKeys(true);

		loadClient(clientBuilder);

		clientBuilder.setReloadable();
	}

	private static void loadClient(final OmniconfigWrapper client) {
		client.loadConfigFile();
		client.pushCategory("Generic Config", "Some more different stuff");

		acknowledgmentOverflowMode = client
				.comment("Text overflow mode which should be used by The Acknowledgment specifically. This is separate from Patchouli's"
						+ " global setting since it uses RESIZE by default, which unpromptly attempts to rescale font even when no rescaling"
						+ " is neccessary, and it is never neccessary for The Acknowledgment thanks to my continuous efforts to make texts fit"
						+ " perfectly on each and every page.")
				.clientOnly()
				.getEnum("AcknowledgmentOverflowMode", TextOverflowMode.OVERFLOW, TextOverflowMode.values());

		angelBlessingDoubleJump = client
				.comment("If false, active ability of Angel's Blessing will not be triggerable by pressing jump key in mid-air.")
				.clientOnly()
				.getBoolean("AngelBlessingDoubleJump", true);

		disableQuoteSubtitles = client
				.comment("If true, disables subtitles for The Architect's narration.")
				.clientOnly()
				.getBoolean("DisableQuoteSubtitles", false);

		SuperpositionHandler.dispatchWrapperToHolders(EnigmaticLegacy.MODID, client);

		client.popCategory();
		client.build();
	}

	private static void loadCommon(final OmniconfigWrapper builder) {
		builder.loadConfigFile();
		builder.pushCategory("Accessibility Options",
				"You may disable certain items or features from being obtainable/usable here." + System.lineSeparator() + System.lineSeparator() +
				"A BLASTED WARNING, PLEASE READ CAREFULLY:" + System.lineSeparator() +
				"This WILL NOT \"delete\" any of the items from the mod. For items, none of the options here do more than" + System.lineSeparator() +
				"just disable default ways of obtaining them. For stuff in dungeon loot - it is removed from dungeon loot, " + System.lineSeparator() +
				"for starter items - no longer given at the start, for craftables - default recipe is disabled." + System.lineSeparator() +
				"If you're a modpack developer or whatever, that is your way to add your own ways of obtaining them." + System.lineSeparator() +
				"Want to disable Enigmatic Amulet/Ring of the Seven Curses from being granted to player when they spawn?" + System.lineSeparator() +
				"HERE IS THE PLACE AND TIME, COME ON AND SLAM!" + System.lineSeparator() + System.lineSeparator() +
				"Please note that as of release 2.6.0 of Enigmatic Legacy, those options are automatically generated" + System.lineSeparator() +
				"for most items in the mod. They may refer to items that do not exist yet or are not obtainable in any" + System.lineSeparator() +
				"case, and may not work for certain items due to non-generic obtaining methods or generic oversight." + System.lineSeparator() + System.lineSeparator() +
				"If you discover option that does not work, but for whatever reason you really need it, submit an issue" + System.lineSeparator() +
				"pointing out to such option here: https://github.com/Extegral/Enigmatic-Legacy/issues");

		customDungeonLootEnabled = builder
				.comment("Whether or not this mod should add any custom loot to dungeon chests' loot tables. This options is mainly "
						+ "for modpack developers and enables them to re-add that loot on their own terms, since no other ways of "
						+ "modifying what Enigmatic Legacy adds to loot tables currently exist.")
				.sync()
				.getBoolean("CustomDungeonLootEnabled", true);

		bonusWoolRecipesEnabled = builder
				.comment("Whether or not bonus recipes for wool dyeing should be enabled.")
				.sync()
				.getBoolean("BonusWoolRecipesEnabled", true);

		crashOnUnnamedPool = builder
				.comment("Whether or not Enigmatic Legacy should purposefully crash client whenever any mod tries to inject unnamed "
						+ "LootPool into any loot table. If false, stacktraces will be printed to log but game will proceed as normal.")
				.getBoolean("CrashOnUnnamedPool", true);

		disableAOEShiftSuppression = builder
				.comment("If true, tools with area of effect abilities will not have those abilities disabled when player holds Shift (crouches).")
				.sync()
				.getBoolean("DisableAOEShiftSuppression", false);

		retriggerRecipeUnlocks = builder
				.comment("If true, Enigmatic Legacy will cycle through each player's recipe book and trigger 'minecraft:recipe_unlocked' criterion trigger for everything that they have unlocked upon player joining the world.")
				.getBoolean("RetriggerRecipeUnlocks", true);

		Multimap<String, Field> accessibilityGeneratorMap = SuperpositionHandler.retainAccessibilityGeneratorMap(EnigmaticLegacy.MODID);
		ITEMS_OPTIONS.clear();
		builder.forceSynchronized(true);

		for (String itemName : accessibilityGeneratorMap.keySet()) {
			String optionName = itemName.replaceAll("[^a-zA-Z0-9]", "")+"Enabled";
			//String firstLetter = optionName.substring(0, 2).toLowerCase();
			//optionName = firstLetter + optionName.substring(1, optionName.length());

			Omniconfig.BooleanParameter param = builder.comment("Whether or not " + itemName + " should be enabled.").getBoolean(optionName, true);
			for (Field associatedField : accessibilityGeneratorMap.get(itemName)) {
				ITEMS_OPTIONS.put(associatedField, param);
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
				.comment("Maximum amount of Soul Crystals a player can lose before they won't drop anymore."
						+ " If set to 10 - player can loose all of them, and doing so will result in permadeath."
						+ " Each crystal lost subtracts 10% of from their maximum possible health value.")
				.min(1)
				.max(10)
				.getInt("MaxSoulCrystalLoss", 9);

		bossList.clear();
		String[] bosses = builder.config.getStringList("CompleteBossList", "Generic Config", new String[] { "minecraft:ender_dragon", "minecraft:wither", "minecraft:elder_guardian" }, "List of entities that should be accounted for as bosses"
				+ " by The Twist and The Infinitum. Changing this option requires game restart to take effect.");

		Arrays.stream(bosses).forEach(entry -> bossList.add(new ResourceLocation(entry)));

		builder.popCategory();


		builder.pushCategory("Balance Options", "Various options that mostly affect individual items");
		builder.forceSynchronized(true);
		SuperpositionHandler.dispatchWrapperToHolders(EnigmaticLegacy.MODID, builder);
		OmniconfigHooks.HOOKS.forEach(hook -> hook.accept(builder));
		builder.forceSynchronized(false);
		builder.popCategory();

		builder.build();

		//builder.comment("Balancing and other option for all the spellstones").push("Spellstones Options");
		//builder.pop();

	}

	public static boolean isBoss(LivingEntity entity) {
		return bossList.stream().anyMatch(id -> id.equals(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType())));
	}

	public static boolean isBossOrPlayer(LivingEntity entity) {
		return entity instanceof Player || isBoss(entity);
	}

}

