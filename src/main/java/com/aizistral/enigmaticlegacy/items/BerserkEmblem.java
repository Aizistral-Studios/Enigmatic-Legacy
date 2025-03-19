package com.aizistral.enigmaticlegacy.items;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.aizistral.enigmaticlegacy.api.items.ICursed;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.SlotContext;

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
	}

	private Multimap<Attribute, AttributeModifier> createAttributeMap(Player player) {
		Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();

		float missingHealthPool = SuperpositionHandler.getMissingHealthPool(player);

		attributesDefault.put(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("ec62548c-5b26-401e-83fd-693e4aafa532"), "enigmaticlegacy:attack_speed_modifier", missingHealthPool*attackSpeed.getValue(), AttributeModifier.Operation.MULTIPLY_BASE));
		attributesDefault.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("f4ece564-d2c0-40d2-a96a-dc68b493137c"), "enigmaticlegacy:speed_modifier", missingHealthPool*movementSpeed.getValue(), AttributeModifier.Operation.MULTIPLY_BASE));

		return attributesDefault;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.berserk_charm1", ChatFormatting.GOLD, minimizeNumber(attackDamage.getValue()) + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.berserk_charm2", ChatFormatting.GOLD, minimizeNumber(attackSpeed.getValue()) + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.berserk_charm3", ChatFormatting.GOLD, minimizeNumber(movementSpeed.getValue()) + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.berserk_charm4", ChatFormatting.GOLD, minimizeNumber(damageResistance.getValue()) + "%");

			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.berserk_charm5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.berserk_charm6");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		if (Minecraft.getInstance().player != null)
			if (SuperpositionHandler.getCurioStack(Minecraft.getInstance().player, this) == stack) {
				float missingPool = SuperpositionHandler.getMissingHealthPool(Minecraft.getInstance().player);
				int percentage = (int)(missingPool * 100F);

				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.berserk_charm7");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.berserk_charm8", ChatFormatting.GOLD, minimizeNumber(attackDamage.getValue()*percentage) + "%");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.berserk_charm9", ChatFormatting.GOLD, minimizeNumber(attackSpeed.getValue()*percentage) + "%");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.berserk_charm10", ChatFormatting.GOLD, minimizeNumber(movementSpeed.getValue()*percentage) + "%");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.berserk_charm11", ChatFormatting.GOLD, minimizeNumber(damageResistance.getValue()*percentage) + "%");

			}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		ItemLoreHelper.indicateCursedOnesOnly(list);

	}

	@Override
	public void curioTick(SlotContext context, ItemStack stack) {
		if (context.entity() instanceof Player player) {
			player.getAttributes().addTransientAttributeModifiers(this.createAttributeMap(player));
		}
	}

	@Override
	public void onUnequip(SlotContext context, ItemStack newStack, ItemStack stack) {
		if (context.entity() instanceof Player player) {
			player.getAttributes().removeAttributeModifiers(this.createAttributeMap(player));
		}
	}

	@Override
	public boolean canEquip(SlotContext context, ItemStack stack) {
		return super.canEquip(context, stack) && context.entity() instanceof Player player && SuperpositionHandler.isTheCursedOne(player);
	}

}