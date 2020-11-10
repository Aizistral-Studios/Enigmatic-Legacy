package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorldOptionsScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import top.theillusivec4.curios.api.CuriosApi;

public class EnigmaticAmulet extends ItemBaseCurio {
	public static Omniconfig.DoubleParameter damageBonus;
	public static Omniconfig.BooleanParameter vesselEnabled;
	public static Omniconfig.BooleanParameter ownerOnlyVessel;
	public static Omniconfig.BooleanParameter seededColorGen;
	public static Omniconfig.BooleanParameter multiequip;
	public static final String amuletColorTag = "AssignedColor";
	public static final String amuletInscriptionTag = "Inscription";

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("EnigmaticAmulet");

		damageBonus = builder
				.comment("The damage bonus stat provided by red Enigmatic Amulet.")
				.minMax(32768)
				.getDouble("DamageBonus", 1.5);

		vesselEnabled = builder
				.comment("Whether or not Enigmatic Amulet should be summoning Extradimensional Vessel on owner's death.")
				.getBoolean("EnigmaticAmuletVesselEnabled", true);

		ownerOnlyVessel = builder
				.comment("If true, only original owner of Extradimensional Vessel will be able to pick it up.")
				.getBoolean("OwnerOnlyVessel", false);

		seededColorGen = builder
				.comment("If true, color of Enigmatic Amulet will be assigned using player's name as seed for generating it, instead of randomly - so that every player will always receive one specific color.")
				.getBoolean("SeededColorGen", false);

		multiequip = builder
				.comment("Whether or not it should be possible to equip multiple Enigmatic Amulets, "
						+ "granted player somehow gets more than one charm slot.")
				.getBoolean("Multiequip", false);

		builder.popPrefix();
	}

	public enum AmuletColor {
		RED(0.1F),
		AQUA(0.2F),
		VIOLET(0.3F),
		MAGENTA(0.4F),
		GREEN(0.5F),
		BLACK(0.6F),
		BLUE(0.7F);

		private float colorVar;
		private AmuletColor(float colorVar) {
			this.colorVar = colorVar;
		}

		public float getColorVar() {
			return this.colorVar;
		}

		public static AmuletColor getRandomColor() {
			return values()[random.nextInt(values().length)];
		}

		public static AmuletColor getSeededColor(Random rand) {
			return values()[rand.nextInt(values().length)];
		}

	}

	private AmuletColor evaluateColor(float colorVar) {
		float var = ((int)(colorVar*10F))/10F;

		for (AmuletColor color: AmuletColor.values()) {
			if (var == color.colorVar)
				return color;
		}

		return AmuletColor.RED;
	}

	public boolean ifHasColor(PlayerEntity player, AmuletColor color) {
		ItemStack enigmaticAmulet = SuperpositionHandler.getCurioStack(player, EnigmaticLegacy.enigmaticAmulet);

		if ((enigmaticAmulet != null) && (EnigmaticLegacy.enigmaticAmulet.getColor(enigmaticAmulet) == color))
			return true;
		else
			return false;
	}

	public AmuletColor getColor(ItemStack amulet) {
		return this.evaluateColor(ItemNBTHelper.getFloat(amulet, amuletColorTag, 0F));
	}

	public ItemStack setColor(ItemStack amulet, AmuletColor color) {
		if ((amulet != null) && amulet.getItem().equals(this)) {
			ItemNBTHelper.setFloat(amulet, amuletColorTag, color.colorVar);
		}
		return amulet;
	}

	public ItemStack setRandomColor(ItemStack amulet) {
		if ((amulet != null) && amulet.getItem().equals(this)) {
			ItemNBTHelper.setFloat(amulet, amuletColorTag, AmuletColor.getRandomColor().colorVar);
		}
		return amulet;
	}

	public ItemStack setSeededColor(ItemStack amulet) {
		if ((amulet != null) && amulet.getItem().equals(this)) {
			String name = ItemNBTHelper.getString(amulet, "Inscription", "Herobrine");
			long hash = name.hashCode();

			ItemNBTHelper.setFloat(amulet, amuletColorTag, AmuletColor.getSeededColor(new Random(hash)).colorVar);
		}
		return amulet;
	}

	public ItemStack setInscription(ItemStack amulet, String name) {
		if ((amulet != null) && amulet.getItem().equals(this)) {
			ItemNBTHelper.setString(amulet, "Inscription", name);
		}
		return amulet;
	}

	public EnigmaticAmulet() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.UNCOMMON).isImmuneToFire());
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "enigmatic_amulet"));
	}

	@OnlyIn(Dist.CLIENT)
	public void registerVariants() {
		ItemModelsProperties.registerProperty(this, new ResourceLocation(EnigmaticLegacy.MODID, "enigmatic_amulet_color"), (stack, world, entity) -> {
			return ItemNBTHelper.getFloat(stack, amuletColorTag, 0F);
		});
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (group == EnigmaticLegacy.enigmaticTab) {
			for (AmuletColor color : AmuletColor.values()) {
				items.add(this.setColor(new ItemStack(this), color));
			}
		}
	}


	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		String name = ItemNBTHelper.getString(stack, "Inscription", null);

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown() && this.isVesselEnabled()) {

			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmuletShift1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmuletShift2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmuletShift3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmuletShift4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmuletShift5");

		} else {
			if (this.isVesselEnabled()) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			}

			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet6");

			if (name != null) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmuletInscription", TextFormatting.DARK_RED, name);
			}
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		ItemLoreHelper.addLocalizedFormattedString(list, "curios.modifiers.charm", TextFormatting.GOLD);
		if (this.getColor(stack) != AmuletColor.RED) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmuletModifier" + this.getColor(stack));
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmuletModifierRED", TextFormatting.GOLD, minimizeNumber(damageBonus.getValue()));
		}
	}

	public Multimap<Attribute, AttributeModifier> getCurrentModifiers(ItemStack amulet, PlayerEntity player) {
		Multimap<Attribute, AttributeModifier> atts = HashMultimap.create();

		AmuletColor color = this.getColor(amulet);

		if (color == AmuletColor.RED) {
			atts.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("f5bb82c7-0332-4adf-a414-2e4f03471983"), EnigmaticLegacy.MODID+":attack_bonus", damageBonus.getValue(), AttributeModifier.Operation.ADDITION));
		} else if (color == AmuletColor.AQUA) {
			atts.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("cde98b8a-0cfc-45dc-929f-9cce9b6fbdfa"), EnigmaticLegacy.MODID+":sprint_bonus", player.isSprinting() ? 0.15F : 0F, AttributeModifier.Operation.MULTIPLY_TOTAL));
		} else if (color == AmuletColor.MAGENTA) {
			atts.put(ForgeMod.ENTITY_GRAVITY.get(), new AttributeModifier(UUID.fromString("d1a07f6f-1079-4b17-8dbd-c74dc5e9094d"), EnigmaticLegacy.MODID+":gravity_bonus", -0.25F, AttributeModifier.Operation.MULTIPLY_TOTAL));
		} else if (color == AmuletColor.BLUE) {
			atts.put(ForgeMod.SWIM_SPEED.get(), new AttributeModifier(UUID.fromString("a4d4b794-a691-4757-b1cb-f5f2d5a25571"), EnigmaticLegacy.MODID+":swim_bonus", 0.25F, AttributeModifier.Operation.MULTIPLY_TOTAL));
		}

		return atts;
	}

	public Multimap<Attribute, AttributeModifier> getAllModifiers() {
		Multimap<Attribute, AttributeModifier> atts = HashMultimap.create();

		atts.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("f5bb82c7-0332-4adf-a414-2e4f03471983"), EnigmaticLegacy.MODID+":attack_bonus", damageBonus.getValue(), AttributeModifier.Operation.ADDITION));
		atts.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("cde98b8a-0cfc-45dc-929f-9cce9b6fbdfa"), EnigmaticLegacy.MODID+":sprint_bonus", 0.15F, AttributeModifier.Operation.MULTIPLY_TOTAL));
		atts.put(ForgeMod.ENTITY_GRAVITY.get(), new AttributeModifier(UUID.fromString("d1a07f6f-1079-4b17-8dbd-c74dc5e9094d"), EnigmaticLegacy.MODID+":gravity_bonus", -0.25F, AttributeModifier.Operation.MULTIPLY_TOTAL));
		atts.put(ForgeMod.SWIM_SPEED.get(), new AttributeModifier(UUID.fromString("a4d4b794-a691-4757-b1cb-f5f2d5a25571"), EnigmaticLegacy.MODID+":swim_bonus", 0.25F, AttributeModifier.Operation.MULTIPLY_TOTAL));

		return atts;
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);

		if (!worldIn.isRemote)
			if (ItemNBTHelper.verifyExistance(stack, amuletInscriptionTag) && !ItemNBTHelper.verifyExistance(stack, amuletColorTag)) {
				this.setSeededColor(stack);
			}
	}


	@Override
	public void onUnequip(String identifier, int index, LivingEntity living) {
		if (living instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) living;

			AttributeModifierManager map = player.getAttributeManager();
			map.removeModifiers(this.getAllModifiers());
		}
	}


	@Override
	public void curioTick(String identifier, int index, LivingEntity living) {
		if (living instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) living;
			ItemStack amulet = SuperpositionHandler.getCurioStack(player, this);

			if (amulet != null) {
				AttributeModifierManager map = player.getAttributeManager();
				map.reapplyModifiers(this.getCurrentModifiers(amulet, player));
			}
		}

	}

	@Override
	public boolean canEquip(String identifier, LivingEntity living) {
		if (multiequip.getValue())
			return true;
		else
			return super.canEquip(identifier, living);
	}

	@Override
	public boolean showAttributesTooltip(String identifier) {
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRender(String identifier, int index, LivingEntity living) {
		return false;
	}

	public boolean isVesselEnabled() {
		return vesselEnabled.getValue();
	}

	public boolean isVesselOwnerOnly() {
		return ownerOnlyVessel.getValue();
	}

}
