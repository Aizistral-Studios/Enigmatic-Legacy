package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.helpers.LoreHelper;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.capability.ICurio;

public class MiningCharm extends Item implements ICurio, IPerhaps {

	public static Properties integratedProperties = new Item.Properties();
	public static final int nightVisionDuration = 210;

	public MiningCharm(Properties properties) {
		super(properties);
	}

	public static Properties setupIntegratedProperties() {
		MiningCharm.integratedProperties.group(EnigmaticLegacy.enigmaticTab);
		MiningCharm.integratedProperties.maxStackSize(1);
		MiningCharm.integratedProperties.rarity(Rarity.RARE);

		return MiningCharm.integratedProperties;
	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.MINING_CHARM_ENABLED.getValue();
	}

	@Override
	public boolean canEquip(String identifier, LivingEntity living) {
		if (SuperpositionHandler.hasCurio(living, EnigmaticLegacy.miningCharm))
			return false;
		else
			return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		TranslationTextComponent mode = new TranslationTextComponent("tooltip.enigmaticlegacy.enabled");

		if (ItemNBTHelper.verifyExistance(stack, "nightVisionEnabled"))
			if (!ItemNBTHelper.getBoolean(stack, "nightVisionEnabled", true))
				mode = new TranslationTextComponent("tooltip.enigmaticlegacy.disabled");

		LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.miningCharm1", ConfigHandler.MINING_CHARM_BREAK_BOOST.getValue().asPercentage() + "%");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.miningCharm2");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.miningCharm3");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.miningCharm4");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.miningCharm5");
		} else {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.miningCharmNightVision", mode.getFormattedText());
	}

	public static void removeNightVisionEffect(PlayerEntity player, int duration) {
		if (player.getActivePotionEffect(Effects.NIGHT_VISION) != null) {
			EffectInstance effect = player.getActivePotionEffect(Effects.NIGHT_VISION);

			if (effect.getDuration() == (duration - 1))
				player.removePotionEffect(Effects.NIGHT_VISION);

		}
	}

	@Override
	public void onCurioTick(String identifier, int index, LivingEntity living) {

		if (living instanceof PlayerEntity & !living.world.isRemote)
			if (SuperpositionHandler.hasCurio(living, EnigmaticLegacy.miningCharm)) {
				PlayerEntity player = (PlayerEntity) living;
				ItemStack stack = SuperpositionHandler.getCurioStack(player, EnigmaticLegacy.miningCharm);

				//System.out.println("Light Level:" + player.world.getNeighborAwareLightSubtracted(player.getPosition(), 0));

				if (ItemNBTHelper.getBoolean(stack, "nightVisionEnabled", true) && player.getPosY() < 50 && player.dimension.getId() != -1 && player.dimension.getId() != 1 && !player.areEyesInFluid(FluidTags.WATER, true) && !player.world.canBlockSeeSky(player.getPosition()) && player.world.getNeighborAwareLightSubtracted(player.getPosition(), 0) <= 8) {

					player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, MiningCharm.nightVisionDuration, 0, true, false));
				} else {
					MiningCharm.removeNightVisionEffect(player, MiningCharm.nightVisionDuration);
				}

			}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand handIn) {

		ItemStack stack = player.getHeldItem(handIn);

		if (ItemNBTHelper.getBoolean(stack, "nightVisionEnabled", true)) {
			ItemNBTHelper.setBoolean(stack, "nightVisionEnabled", false);
			world.playSound(null, player.getPosition(), EnigmaticLegacy.HHOFF, SoundCategory.PLAYERS, (float) (0.8F + (Math.random() * 0.2F)), (float) (0.8F + (Math.random() * 0.2F)));
		} else {
			ItemNBTHelper.setBoolean(stack, "nightVisionEnabled", true);
			world.playSound(null, player.getPosition(), EnigmaticLegacy.HHON, SoundCategory.PLAYERS, (float) (0.8F + (Math.random() * 0.2F)), (float) (0.8F + (Math.random() * 0.2F)));
		}

		player.swingArm(handIn);

		return new ActionResult<>(ActionResultType.SUCCESS, stack);

	}

	@Override
	public boolean canRightClickEquip() {
		return false;
	}

	@Override
	public void onEquipped(String identifier, LivingEntity living) {
		// Insert existential void here
	}

	@Override
	public void onUnequipped(String identifier, LivingEntity living) {
		if (living instanceof PlayerEntity)
			MiningCharm.removeNightVisionEffect((PlayerEntity) living, MiningCharm.nightVisionDuration);
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(String identifier) {

		Multimap<String, AttributeModifier> atts = HashMultimap.create();

		if (ConfigHandler.MINING_CHARM_BONUS_LUCK.getValue())
			atts.put(SharedMonsterAttributes.LUCK.getName(), new AttributeModifier(UUID.fromString("03c3c89d-7037-4b42-880f-b146bcb64d2e"), "Fortune bonus", 1, AttributeModifier.Operation.ADDITION));

		atts.put(PlayerEntity.REACH_DISTANCE.getName(), new AttributeModifier(UUID.fromString("08c3c83d-7137-4b42-880f-b146bcb64d2e"), "Reach bonus", ConfigHandler.MINING_CHARM_REACH_BOOST.getValue(), AttributeModifier.Operation.ADDITION));

		return atts;
	}

}
