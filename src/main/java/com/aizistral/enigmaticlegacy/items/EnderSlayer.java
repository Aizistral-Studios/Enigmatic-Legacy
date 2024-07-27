package com.aizistral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.aizistral.enigmaticlegacy.api.items.ICreativeTabMember;
import com.aizistral.enigmaticlegacy.api.items.ICursed;
import com.aizistral.enigmaticlegacy.api.materials.EnigmaticMaterials;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBaseTool;
import com.aizistral.enigmaticlegacy.registries.EnigmaticTabs;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public class EnderSlayer extends SwordItem implements ICursed, ICreativeTabMember {
	public static final List<ResourceLocation> endDwellers = new ArrayList<>();

	public static Omniconfig.IntParameter attackDamage;
	public static Omniconfig.DoubleParameter attackSpeed;
	public static Omniconfig.PerhapsParameter endDamageBonus;
	public static Omniconfig.PerhapsParameter endKnockbackBonus;

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("EnderSlayer");

		attackDamage = builder
				.comment("Attack damage of The Ender Slayer, actual damage shown in tooltip will be is 4 + this_value.")
				.max(32768)
				.getInt("AttackDamage", 4);

		attackSpeed = builder
				.comment("Attack speed of The Ender Slayer.")
				.minMax(32768)
				.getDouble("AttackSpeed", -2.6);

		endDamageBonus = builder
				.comment("Attack damage bonus of The Ender Slayer against dwellers of The End.")
				.getPerhaps("EndDamageBonus", 150);

		endKnockbackBonus = builder
				.comment("Knockback bonus of The Ender Slayer against dwellers of The End.")
				.getPerhaps("EndKnockbackBonus", 600);

		builder.popPrefix();

		endDwellers.clear();
		String[] list = builder.config.getStringList("EnderSlayerEndDwellers", builder.getCurrentCategory(), new String[0],
				"List of entities that should be considered dwellers of The End by The Ender Slayer."
						+ " Examples: minecraft:iron_golem, minecraft:zombified_piglin");

		Arrays.stream(list).forEach(entry -> endDwellers.add(new ResourceLocation(entry)));
	}

	public EnderSlayer() {
		super(EnigmaticMaterials.ENDER_SLAYER, attackDamage.getValue(), (float) attackSpeed.getValue(), ItemBaseTool.getDefaultProperties().defaultDurability(2000).rarity(Rarity.EPIC).fireResistant());
	}

	public boolean isEndDweller(LivingEntity entity) {
		if (entity instanceof EnderMan || entity instanceof EnderDragon || entity instanceof Shulker || entity instanceof Endermite)
			return true;
		else
			return endDwellers.stream().anyMatch(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType())::equals);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enderSlayer3", ChatFormatting.GOLD, endDamageBonus + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enderSlayer4", ChatFormatting.GOLD, endKnockbackBonus + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enderSlayer5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enderSlayer6");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enderSlayer7");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enderSlayer8");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enderSlayer9");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enderSlayer10");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enderSlayer11");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enderSlayer12");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enderSlayer13");
		} else {
			String stackName = stack.getDisplayName().getString();

			if (stackName.substring(1, stackName.length()-1).equals("§dThe Ender Slapper")) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enderSlayer1_alt");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enderSlayer2_alt");
			} else {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enderSlayer1");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enderSlayer2");
			}
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		ItemLoreHelper.indicateCursedOnesOnly(list);
	}

	@Override
	public @Nullable CreativeModeTab getCreativeTab() {
		return EnigmaticTabs.MAIN;
	}

}
