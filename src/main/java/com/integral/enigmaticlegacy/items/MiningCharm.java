package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MiningCharm extends ItemBaseCurio {

	public final int nightVisionDuration = 210;

	public MiningCharm() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.RARE));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "mining_charm"));
	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.MINING_CHARM_ENABLED.getValue();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		TranslationTextComponent mode = new TranslationTextComponent("tooltip.enigmaticlegacy.enabled");

		if (ItemNBTHelper.verifyExistance(stack, "nightVisionEnabled"))
			if (!ItemNBTHelper.getBoolean(stack, "nightVisionEnabled", true))
				mode = new TranslationTextComponent("tooltip.enigmaticlegacy.disabled");

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.func_231173_s_()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.miningCharm1", TextFormatting.GOLD, ConfigHandler.MINING_CHARM_BREAK_BOOST.getValue().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.miningCharm2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.miningCharm3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.miningCharm4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.miningCharm5");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.miningCharmNightVision", null, mode);
	}

	public void removeNightVisionEffect(PlayerEntity player, int duration) {
		if (player.getActivePotionEffect(Effects.NIGHT_VISION) != null) {
			EffectInstance effect = player.getActivePotionEffect(Effects.NIGHT_VISION);

			if (effect.getDuration() == (duration - 1))
				player.removePotionEffect(Effects.NIGHT_VISION);

		}
	}

	@Override
	public void curioTick(String identifier, int index, LivingEntity living) {

		if (living instanceof PlayerEntity & !living.world.isRemote)
			if (SuperpositionHandler.hasCurio(living, EnigmaticLegacy.miningCharm)) {
				PlayerEntity player = (PlayerEntity) living;
				ItemStack stack = SuperpositionHandler.getCurioStack(player, EnigmaticLegacy.miningCharm);

				if (ItemNBTHelper.getBoolean(stack, "nightVisionEnabled", true) && player.getPosY() < 50 && !player.world.func_234923_W_().func_240901_a_().toString().equals("minecraft:the_nether") && !player.world.func_234923_W_().func_240901_a_().toString().equals("minecraft:the_end") && !player.areEyesInFluid(FluidTags.WATER) && !player.world.canBlockSeeSky(player.func_233580_cy_()) && player.world.getNeighborAwareLightSubtracted(player.func_233580_cy_(), 0) <= 8) {

					player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, this.nightVisionDuration, 0, true, false));
				} else {
					this.removeNightVisionEffect(player, this.nightVisionDuration);
				}

			}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand handIn) {

		ItemStack stack = player.getHeldItem(handIn);

		if (ItemNBTHelper.getBoolean(stack, "nightVisionEnabled", true)) {
			ItemNBTHelper.setBoolean(stack, "nightVisionEnabled", false);
			world.playSound(null, player.func_233580_cy_(), EnigmaticLegacy.HHOFF, SoundCategory.PLAYERS, (float) (0.8F + (Math.random() * 0.2F)), (float) (0.8F + (Math.random() * 0.2F)));
		} else {
			ItemNBTHelper.setBoolean(stack, "nightVisionEnabled", true);
			world.playSound(null, player.func_233580_cy_(), EnigmaticLegacy.HHON, SoundCategory.PLAYERS, (float) (0.8F + (Math.random() * 0.2F)), (float) (0.8F + (Math.random() * 0.2F)));
		}

		player.swingArm(handIn);

		return new ActionResult<>(ActionResultType.SUCCESS, stack);

	}

	@Override
	public void onUnequip(String identifier, int index, LivingEntity living) {
		if (living instanceof PlayerEntity)
			this.removeNightVisionEffect((PlayerEntity) living, this.nightVisionDuration);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(String identifier) {

		Multimap<Attribute, AttributeModifier> atts = HashMultimap.create();

		if (ConfigHandler.MINING_CHARM_BONUS_LUCK.getValue())
			atts.put(Attributes.field_233828_k_, new AttributeModifier(UUID.fromString("03c3c89d-7037-4b42-880f-b146bcb64d2e"), "Fortune bonus", 1, AttributeModifier.Operation.ADDITION));

		atts.put(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(UUID.fromString("08c3c83d-7137-4b42-880f-b146bcb64d2e"), "Reach bonus", ConfigHandler.MINING_CHARM_REACH_BOOST.getValue(), AttributeModifier.Operation.ADDITION));

		return atts;
	}

}
