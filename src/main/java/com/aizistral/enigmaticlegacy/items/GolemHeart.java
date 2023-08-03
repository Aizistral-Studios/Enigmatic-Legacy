package com.aizistral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.aizistral.enigmaticlegacy.api.items.ISpellstone;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemSpellstoneCurio;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.SlotContext;

public class GolemHeart extends ItemSpellstoneCurio implements ISpellstone {
	public static final List<Item> EXCLUDED_ARMOR = new ArrayList<>();
	private static String[] excludedArmorRaw = new String[0];
	public static Omniconfig.IntParameter spellstoneCooldown;
	public static Omniconfig.DoubleParameter defaultArmorBonus;
	public static Omniconfig.DoubleParameter superArmorBonus;
	public static Omniconfig.DoubleParameter superArmorToughnessBonus;
	public static Omniconfig.PerhapsParameter knockbackResistance;
	public static Omniconfig.PerhapsParameter meleeResistance;
	public static Omniconfig.PerhapsParameter explosionResistance;
	public static Omniconfig.DoubleParameter vulnerabilityModifier;

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("GolemHeart");

		spellstoneCooldown = builder
				.comment("Active ability cooldown for Heart of the Golem. Measured in ticks. 20 ticks equal to 1 second.")
				.getInt("Cooldown", 0);

		defaultArmorBonus = builder
				.comment("Default amount of armor points provided by Heart of the Golem.")
				.max(256)
				.getDouble("DefaultArmor", 4.0);

		superArmorBonus = builder
				.comment("The amount of armor points provided by Heart of the Golem when it's bearer has no armor equipped.")
				.max(256)
				.getDouble("SuperArmor", 16.0);

		superArmorToughnessBonus = builder
				.comment("The amount of armor toughness provided by Heart of the Golem when it's bearer has no armor equipped.")
				.max(256)
				.getDouble("SuperArmorToughness", 4.0);

		meleeResistance = builder
				.comment("Resistance to melee attacks provided by Heart of the Golem. Defined as percentage.")
				.max(100)
				.getPerhaps("MeleeResistance", 25);

		explosionResistance = builder
				.comment("Resistance to explosion damage provided by Heart of the Golem. Defined as percentage.")
				.max(100)
				.getPerhaps("ExplosionResistance", 40);

		knockbackResistance = builder
				.comment("Resistance to knockback provided by Heart of the Golem. Defined as percentage.")
				.max(100)
				.getPerhaps("KnockbackResistance", 100);

		vulnerabilityModifier = builder
				.comment("Modifier for Magic Damage vulnerability applied by Heart of the Golem. Default value of 2.0 means that player will receive twice as much damage from magic.")
				.min(1.0)
				.max(256)
				.getDouble("VulnerabilityModifier", 2.0);

		builder.popPrefix();

		excludedArmorRaw = builder.config.getStringList("GolemHeartExcludedArmor", builder.getCurrentCategory(), new String[] { "minecraft:elytra", "enigmaticlegacy:enigmatic_elytra" },
				"List of items that should not be counted as armor by Heart of the Golem, even when equipped in armor slots.");
	}

	public static void buildArmorExclusions() {
		Arrays.stream(excludedArmorRaw).forEach(entry -> {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(entry));

			if (item != null) {
				EXCLUDED_ARMOR.add(item);
			} else {
				EnigmaticLegacy.LOGGER.error("Could not find excluded item when parsing config: " + entry);
			}
		});
	}

	//private static final ResourceLocation TEXTURE = new ResourceLocation(EnigmaticLegacy.MODID, "textures/models/armor/dark_armor.png");
	public Object model;

	public Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();
	public Multimap<Attribute, AttributeModifier> attributesNoArmor = HashMultimap.create();

	public GolemHeart() {
		super(ItemSpellstoneCurio.getDefaultProperties().rarity(Rarity.UNCOMMON));

		this.immunityList.add(DamageTypes.CACTUS);
		this.immunityList.add(DamageTypes.CRAMMING);
		this.immunityList.add(DamageTypes.IN_WALL);
		this.immunityList.add(DamageTypes.FALLING_BLOCK);
		this.immunityList.add(DamageTypes.SWEET_BERRY_BUSH);

		Supplier<Float> meleeResistanceSupplier = () -> meleeResistance.getValue().asModifierInverted();
		Supplier<Float> explosionResistanceSupplier = () -> explosionResistance.getValue().asModifierInverted();
		Supplier<Float> magicVulnerabilitySupplier = () -> (float) vulnerabilityModifier.getValue();

		this.resistanceList.put(DamageTypes.GENERIC, meleeResistanceSupplier);
		this.resistanceList.put(DamageTypes.MOB_ATTACK, meleeResistanceSupplier);
		this.resistanceList.put(DamageTypes.PLAYER_ATTACK, meleeResistanceSupplier);
		this.resistanceList.put(DamageTypes.EXPLOSION, explosionResistanceSupplier);
		this.resistanceList.put(DamageTypes.PLAYER_EXPLOSION, explosionResistanceSupplier);

		this.resistanceList.put(DamageTypes.MAGIC, magicVulnerabilitySupplier);
		this.resistanceList.put(DamageTypes.WITHER, magicVulnerabilitySupplier);
		this.resistanceList.put(DamageTypes.DRAGON_BREATH, magicVulnerabilitySupplier);

		this.initAttributes();
	}

	private void initAttributes() {
		this.attributesDefault.put(Attributes.ARMOR, new AttributeModifier(UUID.fromString("15faf191-bf21-4654-b359-cc1f4f1243bf"), "GolemHeart DAB", defaultArmorBonus.getValue(), AttributeModifier.Operation.ADDITION));
		this.attributesDefault.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.fromString("10faf191-bf21-4554-b359-cc1f4f1233bf"), "GolemHeart KR", knockbackResistance.getValue().asModifier(false), AttributeModifier.Operation.ADDITION));

		this.attributesNoArmor.put(Attributes.ARMOR, new AttributeModifier(UUID.fromString("14faf191-bf23-4654-b359-cc1f4f1243bf"), "GolemHeart SAB", superArmorBonus.getValue(), AttributeModifier.Operation.ADDITION));
		this.attributesNoArmor.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(UUID.fromString("11faf181-bf23-4354-b359-cc1f5f1253bf"), "GolemHeart STB", superArmorToughnessBonus.getValue(), AttributeModifier.Operation.ADDITION));
		this.attributesNoArmor.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.fromString("12faf181-bf21-4554-b359-cc1f4f1254bf"), "GolemHeart KR", knockbackResistance.getValue().asModifier(false), AttributeModifier.Operation.ADDITION));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeartCooldown", ChatFormatting.GOLD, ((spellstoneCooldown.getValue())) / 20.0F);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart4", ChatFormatting.GOLD, (int) defaultArmorBonus.getValue());
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart6", ChatFormatting.GOLD, (int) superArmorBonus.getValue(), (int) superArmorToughnessBonus.getValue());
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart7", ChatFormatting.GOLD, explosionResistance.getValue().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart8", ChatFormatting.GOLD, meleeResistance.getValue().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart9", ChatFormatting.GOLD, knockbackResistance.getValue().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart10");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart11");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		try {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.currentKeybind", ChatFormatting.LIGHT_PURPLE, KeyMapping.createNameSupplier("key.spellstoneAbility").get().getString().toUpperCase());
		} catch (NullPointerException ex) {
			// Just don't do it lol
		}

	}

	@Override
	public void onUnequip(SlotContext context, ItemStack newStack, ItemStack stack) {
		if (context.entity() instanceof ServerPlayer player) {
			AttributeMap map = player.getAttributes();
			map.removeAttributeModifiers(this.attributesDefault);
			map.removeAttributeModifiers(this.attributesNoArmor);
		}
	}

	@Override
	public void curioTick(SlotContext context, ItemStack stack) {
		if (context.entity() instanceof ServerPlayer player) {
			AttributeMap map = player.getAttributes();

			if (SuperpositionHandler.hasAnyArmor(player)) {
				// Removes attributes
				map.removeAttributeModifiers(this.attributesDefault);
				map.removeAttributeModifiers(this.attributesNoArmor);

				// Applies new attributes
				map.addTransientAttributeModifiers(this.attributesDefault);
			} else {
				// Removes attributes
				map.removeAttributeModifiers(this.attributesDefault);
				map.removeAttributeModifiers(this.attributesNoArmor);

				// Applies new attributes
				map.addTransientAttributeModifiers(this.attributesNoArmor);
			}

		}
	}

	/*
	 @Override
	 @OnlyIn(Dist.CLIENT)
	 protected DarkArmorModel getModel() {

		 if (this.model == null) {
			 this.model = new DarkArmorModel(EquipmentSlot.MAINHAND);
		 }

		boolean shouldUpdate = false;

		for (AnchorType anchorType : AnchorType.values()) {

			float offsetX = 0F, offsetY = 0F, offsetZ = 0F;

			offsetX = JsonConfigHandler.getFloat(anchorType.name + "_x");
			offsetY = JsonConfigHandler.getFloat(anchorType.name + "_y");
			offsetZ = JsonConfigHandler.getFloat(anchorType.name + "_z");

			if (SpecialArmorModelRenderer.offsetMap.getOrDefault(anchorType.name + "_x", -32768F) != offsetX || SpecialArmorModelRenderer.offsetMap.getOrDefault(anchorType.name + "_y", -32768F) != offsetY || SpecialArmorModelRenderer.offsetMap.getOrDefault(anchorType.name + "_z", -32768F) != offsetZ) {
				SpecialArmorModelRenderer.offsetMap.put(anchorType.name + "_x", offsetX);
				SpecialArmorModelRenderer.offsetMap.put(anchorType.name + "_y", offsetY);
				SpecialArmorModelRenderer.offsetMap.put(anchorType.name + "_z", offsetZ);

				shouldUpdate = true;
			}

		}

		if (shouldUpdate)
			this.model = new DarkArmorModel(EquipmentSlot.MAINHAND);

	     return (DarkArmorModel) this.model;
	 }
	 */
}
