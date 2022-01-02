package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.api.items.ISpellstone;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemSpellstoneCurio;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GolemHeart extends ItemSpellstoneCurio implements ISpellstone {
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
	}


	//private static final ResourceLocation TEXTURE = new ResourceLocation(EnigmaticLegacy.MODID, "textures/models/armor/dark_armor.png");
	public Object model;

	public Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();
	public Multimap<Attribute, AttributeModifier> attributesNoArmor = HashMultimap.create();

	public GolemHeart() {
		super(ItemSpellstoneCurio.getDefaultProperties().rarity(Rarity.UNCOMMON));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "golem_heart"));

		this.immunityList.add(DamageSource.CACTUS.msgId);
		this.immunityList.add(DamageSource.CRAMMING.msgId);
		this.immunityList.add(DamageSource.IN_WALL.msgId);
		this.immunityList.add(DamageSource.FALLING_BLOCK.msgId);
		this.immunityList.add(DamageSource.SWEET_BERRY_BUSH.msgId);

		Supplier<Float> meleeResistanceSupplier = () -> meleeResistance.getValue().asModifierInverted();
		Supplier<Float> explosionResistanceSupplier = () -> explosionResistance.getValue().asModifierInverted();
		Supplier<Float> magicVulnerabilitySupplier = () -> (float) vulnerabilityModifier.getValue();

		this.resistanceList.put(DamageSource.GENERIC.msgId, meleeResistanceSupplier);
		this.resistanceList.put("mob", meleeResistanceSupplier);
		this.resistanceList.put("player", meleeResistanceSupplier);
		this.resistanceList.put("explosion", explosionResistanceSupplier);
		this.resistanceList.put("explosion.player", explosionResistanceSupplier);

		this.resistanceList.put(DamageSource.MAGIC.msgId, magicVulnerabilitySupplier);
		this.resistanceList.put(DamageSource.WITHER.msgId, magicVulnerabilitySupplier);
		this.resistanceList.put(DamageSource.DRAGON_BREATH.msgId, magicVulnerabilitySupplier);

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
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeartCooldown", TextFormatting.GOLD, ((spellstoneCooldown.getValue())) / 20.0F);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart4", TextFormatting.GOLD, (int) defaultArmorBonus.getValue());
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart6", TextFormatting.GOLD, (int) superArmorBonus.getValue(), (int) superArmorToughnessBonus.getValue());
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart7", TextFormatting.GOLD, explosionResistance.getValue().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart8", TextFormatting.GOLD, meleeResistance.getValue().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart9", TextFormatting.GOLD, knockbackResistance.getValue().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart10");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart11");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		try {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.currentKeybind", TextFormatting.LIGHT_PURPLE, KeyBinding.createNameSupplier("key.spellstoneAbility").get().getString().toUpperCase());
		} catch (NullPointerException ex) {
			// Just don't do it lol
		}

	}

	@Override
	public void onUnequip(String identifier, int index, LivingEntity living, ItemStack stack) {
		if (living instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) living;

			AttributeModifierManager map = player.getAttributes();
			map.removeAttributeModifiers(this.attributesDefault);
			map.removeAttributeModifiers(this.attributesNoArmor);
		}
	}

	@Override
	public void curioTick(String identifier, int index, LivingEntity living, ItemStack stack) {
		if (living instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) living;

			AttributeModifierManager map = player.getAttributes();

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

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRender(String identifier, int index, LivingEntity living, ItemStack stack) {
		return false;
	}


	/*
	 @Override
	 @OnlyIn(Dist.CLIENT)
	 protected DarkArmorModel getModel() {

		 if (this.model == null) {
			 this.model = new DarkArmorModel(EquipmentSlotType.MAINHAND);
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
			this.model = new DarkArmorModel(EquipmentSlotType.MAINHAND);

	     return (DarkArmorModel) this.model;
	 }
	 */
}
