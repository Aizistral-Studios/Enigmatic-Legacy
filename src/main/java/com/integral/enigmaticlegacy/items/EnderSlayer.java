package com.integral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.api.items.ICursed;
import com.integral.enigmaticlegacy.api.materials.EnigmaticMaterials;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseTool;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class EnderSlayer extends SwordItem implements ICursed {
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
		super(EnigmaticMaterials.ENDERSLAYER, attackDamage.getValue(), (float) attackSpeed.getValue(), ItemBaseTool.getDefaultProperties().defaultDurability(2000).rarity(Rarity.EPIC).fireResistant());
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "ender_slayer"));
	}

	public boolean isEndDweller(LivingEntity entity) {
		if (entity instanceof EnderMan || entity instanceof EnderDragon || entity instanceof Shulker || entity instanceof Endermite)
			return true;
		else
			return endDwellers.stream().anyMatch(name -> name.equals(entity.getType().getRegistryName()));
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

}
