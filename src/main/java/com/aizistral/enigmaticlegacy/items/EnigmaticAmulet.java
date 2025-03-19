package com.aizistral.enigmaticlegacy.items;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.server.ServerLifecycleHooks;
import top.theillusivec4.curios.api.SlotContext;

public class EnigmaticAmulet extends ItemBaseCurio {
	public static Omniconfig.DoubleParameter damageBonus;
	public static Omniconfig.BooleanParameter vesselEnabled;
	public static Omniconfig.BooleanParameter ownerOnlyVessel;
	public static Omniconfig.BooleanParameter seededColorGen;
	public static Omniconfig.BooleanParameter multiequip;
	public static Omniconfig.DoubleParameter savedXPFraction;
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
				.getBoolean("VesselEnabled", true);

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

		savedXPFraction = builder
				.comment("What fraction of player's experience should be stored in Extradimensional Vessel upon their death. "
						+ "Experience that is not stored will be lost forever. 1.0 means that all experience is saved.")
				.max(1).getDouble("SavedXPFraction", 1.0);

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

	public boolean hasColor(Player player, AmuletColor color) {
		if (SuperpositionHandler.hasCurio(player, EnigmaticItems.ASCENSION_AMULET))
			return true;

		ItemStack enigmaticAmulet = SuperpositionHandler.getCurioStack(player, EnigmaticItems.ENIGMATIC_AMULET);

		if ((enigmaticAmulet != null) && (EnigmaticItems.ENIGMATIC_AMULET.getColor(enigmaticAmulet) == color))
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

	public ItemStack setPseudoRandomColor(ItemStack amulet) {
		if ((amulet != null) && amulet.getItem().equals(this)) {
			String name = ItemNBTHelper.getString(amulet, "Inscription", "Herobrine");
			name += ServerLifecycleHooks.getCurrentServer().getWorldData().worldGenOptions().seed();
			long hash = name.hashCode();

			ItemNBTHelper.setFloat(amulet, amuletColorTag, AmuletColor.getSeededColor(new Random(hash)).colorVar);
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

	public ItemStack setProperlyGranted(ItemStack amulet) {
		if ((amulet != null) && amulet.getItem().equals(this)) {
			ItemNBTHelper.setBoolean(amulet, "ProperlyGranted", true);
		}
		return amulet;
	}

	public EnigmaticAmulet() {
		this(getDefaultProperties().rarity(Rarity.UNCOMMON).fireResistant(), "enigmatic_amulet");
	}

	protected EnigmaticAmulet(Properties properties, String name) {
		super(properties);
	}

	@OnlyIn(Dist.CLIENT)
	public void registerVariants() {
		ItemProperties.register(this, new ResourceLocation(EnigmaticLegacy.MODID, "enigmatic_amulet_color"), (stack, world, entity, numberlol) -> {
			return ItemNBTHelper.getFloat(stack, amuletColorTag, 0F);
		});
	}

	@Override
	public List<ItemStack> getCreativeTabStacks() {
		ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();

		for (AmuletColor color : AmuletColor.values()) {
			builder.add(this.setColor(new ItemStack(this), color));
		}

		return builder.build();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
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

			if (ItemNBTHelper.getBoolean(stack, "ProperlyGranted", false)) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet1");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet2");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet3");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet4");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet5");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet6");
			} else {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet1_alt");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet2_alt");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet3_alt");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet4_alt");
			}

			if (name != null) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmuletInscription", ChatFormatting.DARK_RED, name);
			}
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		this.addAttributes(list, stack);
	}

	@OnlyIn(Dist.CLIENT)
	protected void addAttributes(List<Component> list, ItemStack stack) {
		ItemLoreHelper.addLocalizedFormattedString(list, "curios.modifiers.charm", ChatFormatting.GOLD);

		if (this.getColor(stack) != AmuletColor.RED) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmuletModifier" + this.getColor(stack));
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmuletModifierRED", ChatFormatting.GOLD, minimizeNumber(damageBonus.getValue()));
		}
	}

	public Multimap<Attribute, AttributeModifier> getCurrentModifiers(ItemStack amulet, Player player) {
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

	protected Multimap<Attribute, AttributeModifier> getAllModifiers(@Nullable Player player) {
		Multimap<Attribute, AttributeModifier> atts = HashMultimap.create();

		atts.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("f5bb82c7-0332-4adf-a414-2e4f03471983"), EnigmaticLegacy.MODID+":attack_bonus", damageBonus.getValue(), AttributeModifier.Operation.ADDITION));
		atts.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("cde98b8a-0cfc-45dc-929f-9cce9b6fbdfa"), EnigmaticLegacy.MODID+":sprint_bonus", player != null ? (player.isSprinting() ? 0.15F : 0F) : 0.15F, AttributeModifier.Operation.MULTIPLY_TOTAL));
		atts.put(ForgeMod.ENTITY_GRAVITY.get(), new AttributeModifier(UUID.fromString("d1a07f6f-1079-4b17-8dbd-c74dc5e9094d"), EnigmaticLegacy.MODID+":gravity_bonus", -0.25F, AttributeModifier.Operation.MULTIPLY_TOTAL));
		atts.put(ForgeMod.SWIM_SPEED.get(), new AttributeModifier(UUID.fromString("a4d4b794-a691-4757-b1cb-f5f2d5a25571"), EnigmaticLegacy.MODID+":swim_bonus", 0.25F, AttributeModifier.Operation.MULTIPLY_TOTAL));

		return atts;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);

		if (!worldIn.isClientSide)
			if (ItemNBTHelper.verifyExistance(stack, amuletInscriptionTag) && !ItemNBTHelper.verifyExistance(stack, amuletColorTag)) {
				this.setSeededColor(stack);
			}
	}


	@Override
	public void onUnequip(SlotContext context, ItemStack newStack, ItemStack stack) {
		if (context.entity() instanceof ServerPlayer player) {
			AttributeMap map = player.getAttributes();
			map.removeAttributeModifiers(this.getAllModifiers(null));
		}
	}


	@Override
	public void curioTick(SlotContext context, ItemStack stack) {
		if (context.entity() instanceof ServerPlayer player) {
			ItemStack amulet = SuperpositionHandler.getCurioStack(player, this);

			if (amulet != null) {
				AttributeMap map = player.getAttributes();
				map.addTransientAttributeModifiers(this.getCurrentModifiers(amulet, player));
			}
		}
	}

	@Override
	public boolean canEquip(SlotContext context, ItemStack stack) {
		if (multiequip.getValue())
			return true;
		else
			return super.canEquip(context, stack);
	}

	@Override
	public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
		tooltips.clear();
		return tooltips;
	}

	public boolean isVesselEnabled() {
		return vesselEnabled.getValue();
	}

	public boolean isVesselOwnerOnly() {
		return ownerOnlyVessel.getValue();
	}

}
