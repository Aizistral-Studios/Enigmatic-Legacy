package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MonsterCharm extends ItemBaseCurio {
	public static Omniconfig.PerhapsParameter undeadDamageBonus;
	public static Omniconfig.PerhapsParameter hostileDamageBonus;
	public static Omniconfig.BooleanParameter bonusLootingEnabled;
	public static Omniconfig.BooleanParameter doubleXPEnabled;

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("MonsterCharm");

		undeadDamageBonus = builder
				.comment("Damage bonus against undead enemies for Emblem of Monster Slayer. Defined as percentage.")
				.max(1000)
				.getPerhaps("UndeadDamage", 25);

		hostileDamageBonus = builder
				.comment("Damage bonus against agressive creatures for Emblem of Monster Slayer. Defined as percentage.")
				.max(1000)
				.getPerhaps("HostileDamage", 10);

		bonusLootingEnabled = builder
				.comment("Whether or not Emblem of Monster Slayer should provide +1 Looting Level.")
				.getBoolean("BonusLooting", true);

		doubleXPEnabled = builder
				.comment("Whether or not Emblem of Monster Slayer should provide double experience drop from monsters.")
				.getBoolean("DoubleXP", true);

		builder.popPrefix();
	}

	public float bonusXPModifier = 2.0F;

	public MonsterCharm() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.EPIC));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "monster_charm"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.monsterCharm1", TextFormatting.GOLD, undeadDamageBonus.getValue().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.monsterCharm2", TextFormatting.GOLD, hostileDamageBonus.getValue().asPercentage() + "%");
			if (bonusLootingEnabled.getValue()) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.monsterCharm3");
			}
			if (doubleXPEnabled.getValue()) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.monsterCharm4");
			}
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRender(String identifier, int index, LivingEntity living) {
		return false;
	}

	@Override
	public int getLootingBonus(String identifier, LivingEntity livingEntity, ItemStack curio, int index) {
		return super.getLootingBonus(identifier, livingEntity, curio, index) + 1;
	}

}
