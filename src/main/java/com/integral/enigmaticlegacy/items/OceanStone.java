package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.api.items.ISpellstone;
import com.integral.enigmaticlegacy.config.JsonConfigHandler;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ExperienceHelper;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemSpellstoneCurio;
import com.integral.omniconfig.Configuration;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.world.entity.player.ServerPlayerEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;

public class OceanStone extends ItemSpellstoneCurio implements ISpellstone {
	public static Omniconfig.IntParameter spellstoneCooldown;
	public static Omniconfig.PerhapsParameter swimminSpeedBoost;
	public static Omniconfig.PerhapsParameter underwaterCreaturesResistance;
	public static Omniconfig.DoubleParameter xpCostModifier;
	public static Omniconfig.BooleanParameter preventOxygenBarRender;

	@SubscribeConfig(receiveClient = true)
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("OceanStone");
		if (builder.config.getSidedType() != Configuration.SidedConfigType.CLIENT) {
			spellstoneCooldown = builder
					.comment("Active ability cooldown for Will of the Ocean. Measured in ticks. 20 ticks equal to 1 second.")
					.getInt("Cooldown", 600);

			swimminSpeedBoost = builder
					.comment("Swimming speed boost provided by Will of the Ocean. Defined as percentage.")
					.max(1000)
					.getPerhaps("SwimBoost", 200);

			underwaterCreaturesResistance = builder
					.comment("Damage resistance against underwater creatures provided by Will of the Ocean. Defined as percentage.")
					.max(100)
					.getPerhaps("UnderwaterCreaturesResistance", 40);

			xpCostModifier = builder
					.comment("Multiplier for experience consumption by active ability of Will of the Ocean.")
					.max(1000)
					.getDouble("XPCostModifier", 1.0);
		} else {
			builder.popPrefix();

			preventOxygenBarRender = builder
					.comment("Whether or not oxygen bar should pe prevented from rendering if Will of the Ocean or Pearl of the Void is equipped.")
					.getBoolean("SuppressUnneccessaryOxygenRender", true);
		}
		builder.popPrefix();
	}

	public final int xpCostBase = 150;
	public final int nightVisionDuration = 210;

	public OceanStone() {
		super(ItemSpellstoneCurio.getDefaultProperties().rarity(Rarity.RARE));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "ocean_stone"));

		this.immunityList.add(DamageSource.DROWN.msgId);

		this.resistanceList.put(DamageSource.IN_FIRE.msgId, () -> 2F);
		this.resistanceList.put(DamageSource.ON_FIRE.msgId, () -> 2F);
		this.resistanceList.put(DamageSource.LAVA.msgId, () -> 2F);
		this.resistanceList.put(DamageSource.HOT_FLOOR.msgId, () -> 2F);
		this.resistanceList.put("fireball", () -> 2F);
	}

	private Multimap<Attribute, AttributeModifier> createAttributeMap(PlayerEntity player) {
		Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();

		attributesDefault.put(ForgeMod.ENTITY_GRAVITY.get(), new AttributeModifier(UUID.fromString("79e1cc36-fb4e-4c7d-802b-583b8d90648a"), EnigmaticLegacy.MODID+":gravity_bonus", player.isEyeInFluid(FluidTags.WATER) ? -1.0F : 0F, AttributeModifier.Operation.MULTIPLY_TOTAL));

		return attributesDefault;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone2");
			//ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStoneCooldown", TextFormatting.GOLD, ((spellstoneCooldown.getValue())) / 20.0F);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone5", TextFormatting.GOLD, underwaterCreaturesResistance.getValue().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone6");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone7");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone8");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone9");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone10");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone11");
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
	public void triggerActiveAbility(World world, ServerPlayerEntity player, ItemStack stack) {
		if (SuperpositionHandler.hasSpellstoneCooldown(player))
			return;

		if (!player.level.dimension().location().toString().equals("minecraft:the_end") && !player.level.dimension().location().toString().equals("minecraft:the_nether"))
			if (!world.getLevelData().isThundering()) {
				boolean paybackReceived = false;

				/*
				 * ItemStack scroll = SuperpositionHandler.getCurioStack(player,
				 * EnigmaticLegacy.xpScroll);
				 *
				 * if (scroll != null && ItemNBTHelper.getInt(scroll, "XPStored", 0) >=
				 * xpCostBase*2) { ItemNBTHelper.setInt(scroll, "XPStored",
				 * ItemNBTHelper.getInt(scroll, "XPStored", 0) - (int)
				 * ((xpCostBase+(Math.random()*xpCostBase))*ConfigHandler.
				 * OCEAN_STONE_XP_COST_MODIFIER.getValue())); paybackReceived = true; } else
				 */

				if (ExperienceHelper.getPlayerXP(player) >= this.xpCostBase * 2) {
					ExperienceHelper.drainPlayerXP(player, (int) ((this.xpCostBase + (Math.random() * this.xpCostBase)) * xpCostModifier.getValue()));
					paybackReceived = true;
				}

				if (paybackReceived) {

					if (world instanceof ServerWorld) {
						ServerWorld serverworld = (ServerWorld) world;

						int thunderstormTime = (int) (10000 + (Math.random() * 20000));

						serverworld.setWeatherParameters(0, thunderstormTime, true, true);
						/*
							info.setWanderingTraderSpawnDelay(delay);
							info.setRaining(true);
							info.setThundering(true);
							info.setRainTime(thunderstormTime);
							info.setThunderTime(thunderstormTime);
						 */
					}

					world.playSound(null, player.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundCategory.NEUTRAL, 2.0F, (float) (0.7F + (Math.random() * 0.3D)));

					SuperpositionHandler.setSpellstoneCooldown(player, spellstoneCooldown.getValue());
				}

			}
	}

	@Override
	public void onUnequip(String identifier, int index, LivingEntity living, ItemStack stack) {
		if (living instanceof PlayerEntity) {
			EnigmaticLegacy.miningCharm.removeNightVisionEffect((PlayerEntity) living, this.nightVisionDuration);
			living.getAttributes().removeAttributeModifiers(this.createAttributeMap((PlayerEntity)living));
		}
	}

	@Override
	public void curioTick(String identifier, int index, LivingEntity living, ItemStack stack) {

		if (living instanceof PlayerEntity & !living.level.isClientSide)
			if (SuperpositionHandler.hasCurio(living, EnigmaticLegacy.oceanStone)) {
				PlayerEntity player = (PlayerEntity) living;

				if (player.isEyeInFluid(FluidTags.WATER)) {
					player.addEffect(new EffectInstance(Effects.NIGHT_VISION, this.nightVisionDuration, 0, true, false));
					player.setAirSupply(300);
				} else {
					EnigmaticLegacy.miningCharm.removeNightVisionEffect(player, this.nightVisionDuration);
				}

				player.getAttributes().addTransientAttributeModifiers(this.createAttributeMap(player));
			}

	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(String identifier, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> atts = HashMultimap.create();

		atts.put(net.minecraftforge.common.ForgeMod.SWIM_SPEED.get(), new AttributeModifier(UUID.fromString("13faf191-bf38-4654-b369-cc1f4f1143bf"), "Swim speed bonus", swimminSpeedBoost.getValue().asMultiplier(false), AttributeModifier.Operation.MULTIPLY_BASE));

		return atts;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRender(String identifier, int index, LivingEntity living, ItemStack stack) {
		return false;
	}

	@Override
	public boolean showAttributesTooltip(String identifier, ItemStack stack) {
		return false;
	}

}
