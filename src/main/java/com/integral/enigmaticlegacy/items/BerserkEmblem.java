package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.api.items.ICursed;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BerserkEmblem extends ItemBaseCurio implements ICursed {
	public static Omniconfig.DoubleParameter attackDamage;
	public static Omniconfig.DoubleParameter attackSpeed;
	public static Omniconfig.DoubleParameter movementSpeed;
	public static Omniconfig.DoubleParameter damageResistance;

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("BerserkEmblem");

		attackDamage = builder
				.comment("Damage increase provided by Emblem of Bloodstained Valor for each missing percent of health. Measured as percentage.")
				.getDouble("DamageBoost", 1);

		attackSpeed = builder
				.comment("Attack speed increase provided by Emblem of Bloodstained Valor for each missing percent of health. Measured as percentage.")
				.getDouble("AttackSpeedBoost", 1);

		movementSpeed = builder
				.comment("Movement speed increase provided by Emblem of Bloodstained Valor for each missing percent of health. Measured as percentage.")
				.getDouble("SpeedBoost", 0.5);

		damageResistance = builder
				.comment("Damage resistance provided by Emblem of Bloodstained Valor for each missing percent of health. Measured as percentage.")
				.getDouble("ResistanceBoost", 0.5);

		builder.popPrefix();
	}


	public BerserkEmblem() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.EPIC));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "berserk_emblem"));
	}

	private Multimap<Attribute, AttributeModifier> createAttributeMap(PlayerEntity player) {
		Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();

		float missingHealthPool = SuperpositionHandler.getMissingHealthPool(player);

		attributesDefault.put(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("ec62548c-5b26-401e-83fd-693e4aafa532"), "enigmaticlegacy:attack_speed_modifier", missingHealthPool*attackSpeed.getValue(), AttributeModifier.Operation.MULTIPLY_BASE));
		attributesDefault.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("f4ece564-d2c0-40d2-a96a-dc68b493137c"), "enigmaticlegacy:speed_modifier", missingHealthPool*movementSpeed.getValue(), AttributeModifier.Operation.MULTIPLY_BASE));

		return attributesDefault;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.berserk_emblem1", TextFormatting.GOLD, minimizeNumber(attackDamage.getValue()) + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.berserk_emblem2", TextFormatting.GOLD, minimizeNumber(attackSpeed.getValue()) + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.berserk_emblem3", TextFormatting.GOLD, minimizeNumber(movementSpeed.getValue()) + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.berserk_emblem4", TextFormatting.GOLD, minimizeNumber(damageResistance.getValue()) + "%");

			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.berserk_emblem5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.berserk_emblem6");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		if (Minecraft.getInstance().player != null)
			if (SuperpositionHandler.getCurioStack(Minecraft.getInstance().player, this) == stack) {
				float missingPool = SuperpositionHandler.getMissingHealthPool(Minecraft.getInstance().player);
				int percentage = (int)(missingPool * 100F);

				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.berserk_emblem7");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.berserk_emblem8", TextFormatting.GOLD, minimizeNumber(attackDamage.getValue()*percentage) + "%");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.berserk_emblem9", TextFormatting.GOLD, minimizeNumber(attackSpeed.getValue()*percentage) + "%");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.berserk_emblem10", TextFormatting.GOLD, minimizeNumber(movementSpeed.getValue()*percentage) + "%");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.berserk_emblem11", TextFormatting.GOLD, minimizeNumber(damageResistance.getValue()*percentage) + "%");

			}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		ItemLoreHelper.indicateCursedOnesOnly(list);

	}

	@Override
	public void curioTick(String identifier, int index, LivingEntity living) {
		if (living instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) living;

			living.getAttributeManager().reapplyModifiers(this.createAttributeMap(player));
		}
	}

	@Override
	public void onUnequip(String identifier, int index, LivingEntity living) {
		if (living instanceof PlayerEntity) {
			living.getAttributeManager().removeModifiers(this.createAttributeMap((PlayerEntity) living));
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRender(String identifier, int index, LivingEntity living) {
		return false;
	}

	@Override
	public boolean canEquip(String identifier, LivingEntity living) {
		return living instanceof PlayerEntity && SuperpositionHandler.isTheCursedOne((PlayerEntity)living);
	}

}