package com.integral.enigmaticlegacy.items;

import java.util.List;

import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.api.items.ICursed;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TheTwist extends TheAcknowledgment implements ICursed {
	public static Omniconfig.DoubleParameter attackDamage;
	public static Omniconfig.DoubleParameter attackSpeed;
	public static Omniconfig.PerhapsParameter bossDamageBonus;
	public static Omniconfig.PerhapsParameter knockbackBonus;

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("TheTwist");

		attackDamage = builder
				.comment("Attack damage of The Twist, actual damage shown in tooltip will be is 1 + this_value.")
				.max(32768)
				.getDouble("AttackDamage", 8);

		attackSpeed = builder
				.comment("Attack speed of The Twist.")
				.minMax(32768)
				.getDouble("AttackSpeed", -1.8);

		bossDamageBonus = builder
				.comment("Attack damage bonus of The Twist against players and bossess.")
				.getPerhaps("BossDamageBonus", 300);

		knockbackBonus = builder
				.comment("Knockback bonus of The Twist. For Phantoms, this value is multiplied by 1.5.")
				.getPerhaps("KnockbackPowerBonus", 300);

		builder.popPrefix();
	}

	public TheTwist() {
		super(getDefaultProperties().rarity(Rarity.EPIC).maxStackSize(1).isBurnable(),
				"the_twist", attackDamage.getValue(), attackSpeed.getValue());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flag) {
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theTwist1");
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theTwist2");
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			if (Minecraft.getInstance().player != null && SuperpositionHandler.isTheCursedOne(Minecraft.getInstance().player)) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theTwist4");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theTwist5");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			}
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theTwist6", TextFormatting.GOLD, bossDamageBonus + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theTwist7", TextFormatting.GOLD, knockbackBonus + "%");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		ItemLoreHelper.indicateCursedOnesOnly(list);

		try {
			//list.add(new StringTextComponent("").append(TheAcknowledgment.getEdition()).mergeStyle(TextFormatting.DARK_PURPLE));
		} catch (Exception ex) {
			// Just don't do it lol
		}
	}

}
