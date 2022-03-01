package com.integral.enigmaticlegacy.items;

import java.util.List;
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
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.SlotContext;

public class MiningCharm extends ItemBaseCurio {
	public static Omniconfig.PerhapsParameter breakSpeedBonus;
	public static Omniconfig.DoubleParameter reachDistanceBonus;
	//public static EnigmaConfig.BooleanParameter bonusFortuneEnabled;

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("MiningCharm");

		breakSpeedBonus = builder
				.comment("Mining speed boost granted by Charm of Treasure Hunter. Defined as percentage.")
				.max(1000)
				.getPerhaps("BreakSpeed", 30);

		reachDistanceBonus = builder
				.comment("Additional block reach granted by Charm of Treasure Hunter.")
				.max(16)
				.getDouble("ReachDistance", 2.15);

		builder.popPrefix();
	}

	public final int nightVisionDuration = 310;

	public MiningCharm() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.RARE));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "mining_charm"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		TranslatableComponent mode = new TranslatableComponent("tooltip.enigmaticlegacy.enabled");

		if (ItemNBTHelper.verifyExistance(stack, "nightVisionEnabled"))
			if (!ItemNBTHelper.getBoolean(stack, "nightVisionEnabled", true)) {
				mode = new TranslatableComponent("tooltip.enigmaticlegacy.disabled");
			}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.miningCharm1", ChatFormatting.GOLD, breakSpeedBonus.getValue().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.miningCharm2", ChatFormatting.GOLD, 1);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.miningCharm3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.miningCharm4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.miningCharm5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.miningCharm6");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.miningCharmNightVision", null, mode.getString());
	}

	public void removeNightVisionEffect(Player player, int duration) {
		if (player.getEffect(MobEffects.NIGHT_VISION) != null) {
			MobEffectInstance effect = player.getEffect(MobEffects.NIGHT_VISION);

			if (effect.getDuration() <= (duration - 1)) {
				player.removeEffect(MobEffects.NIGHT_VISION);
			}
		}
	}

	@Override
	public void curioTick(SlotContext context, ItemStack stack) {
		if (context.entity() instanceof Player player && !context.entity().level.isClientSide)
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.miningCharm)) {
				if (ItemNBTHelper.getBoolean(stack, "nightVisionEnabled", true)
						&& player.getY() < 50
						&& !player.level.dimension().equals(Level.NETHER)
						&& !player.level.dimension().equals(Level.END)
						&& !player.isEyeInFluid(FluidTags.WATER)
						&& !player.level.canSeeSkyFromBelowWater(player.blockPosition())
						/*&& player.level.getMaxLocalRawBrightness(player.blockPosition(), 0) <= 8*/) {

					player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, this.nightVisionDuration, 0, true, false));
				} else {
					//this.removeNightVisionEffect(player, this.nightVisionDuration);
				}

			}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand handIn) {
		ItemStack stack = player.getItemInHand(handIn);

		if (ItemNBTHelper.getBoolean(stack, "nightVisionEnabled", true)) {
			ItemNBTHelper.setBoolean(stack, "nightVisionEnabled", false);
			world.playSound(null, player.blockPosition(), EnigmaticLegacy.HHOFF, SoundSource.PLAYERS, (float) (0.8F + (Math.random() * 0.2F)), (float) (0.8F + (Math.random() * 0.2F)));
		} else {
			ItemNBTHelper.setBoolean(stack, "nightVisionEnabled", true);
			world.playSound(null, player.blockPosition(), EnigmaticLegacy.HHON, SoundSource.PLAYERS, (float) (0.8F + (Math.random() * 0.2F)), (float) (0.8F + (Math.random() * 0.2F)));
		}

		player.swing(handIn);

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}

	@Override
	public void onUnequip(SlotContext context, ItemStack newStack, ItemStack stack) {
		if (context.entity() instanceof Player player) {
			this.removeNightVisionEffect(player, this.nightVisionDuration);
		}
	}

	@Override
	public boolean canEquipFromUse(SlotContext context, ItemStack stack) {
		return false;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> atts = HashMultimap.create();

		atts.put(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(UUID.fromString("08c3c83d-7137-4b42-880f-b146bcb64d2e"), "Reach bonus", reachDistanceBonus.getValue(), AttributeModifier.Operation.ADDITION));

		return atts;
	}

	@Override
	public int getFortuneLevel(SlotContext slotContext, LootContext lootContext, ItemStack curio) {
		return super.getFortuneLevel(slotContext, lootContext, curio) + 1;
	}

}
