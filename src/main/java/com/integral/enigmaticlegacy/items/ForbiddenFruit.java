package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.api.items.ICursed;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;
import com.integral.enigmaticlegacy.items.generic.ItemBaseFood;
import com.integral.enigmaticlegacy.objects.TransientPlayerData;
import com.integral.omniconfig.Configuration;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ForbiddenFruit extends ItemBaseFood implements IVanishable {
	public static final String consumedFruitTag = "ConsumedForbiddenFruit";
	public static Omniconfig.PerhapsParameter regenerationSubtraction;
	public static Omniconfig.BooleanParameter renderHungerBar;
	public static Omniconfig.BooleanParameter replaceHungerBar;

	@SubscribeConfig(receiveClient = true)
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("ForbiddenFruit");
		if (builder.config.getSidedType() != Configuration.SidedConfigType.CLIENT) {
			regenerationSubtraction = builder
					.comment("How much should be subtracted from regeneration of player who have consumed The Forbidden Fruit. ")
					.max(100)
					.getPerhaps("RegenerationSubtraction", 80);
		} else {
			renderHungerBar = builder
					.comment("Whether or not hunger bar should be rendered at all after Forbidden Fruit was consumed.")
					.getBoolean("RenderHungerbar", true);

			replaceHungerBar = builder
					.comment("Whether or not food icons on hunger bar should be replaced when custom ones after Forbidden Fruit was consumed.")
					.getBoolean("ReplaceHungerBar", true);
		}

		builder.popPrefix();
	}

	public ForbiddenFruit() {
		super(getDefaultProperties().rarity(Rarity.RARE).fireResistant(), buildDefaultFood());

		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "forbidden_fruit"));
	}


	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.forbiddenFruit1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.forbiddenFruit2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.forbiddenFruit3", TextFormatting.GOLD, regenerationSubtraction+"%");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	public boolean haveConsumedFruit(PlayerEntity player) {
		return TransientPlayerData.get(player).getConsumedForbiddenFruit();
	}

	public void defineConsumedFruit(PlayerEntity player, boolean consumedOrNot) {
		SuperpositionHandler.setPersistentBoolean(player, consumedFruitTag, consumedOrNot);
		TransientPlayerData.get(player).setConsumedForbiddenFruit(consumedOrNot);
	}

	@Override
	public void onConsumed(World worldIn, PlayerEntity player, ItemStack food) {
		this.defineConsumedFruit(player, true);

		if (player instanceof ServerPlayerEntity) {
			player.addEffect(new EffectInstance(Effects.WITHER, 300, 3, false, true));
			player.addEffect(new EffectInstance(Effects.CONFUSION, 300, 2, false, true));
			player.addEffect(new EffectInstance(Effects.WEAKNESS, 400, 2, false, true));
			player.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 500, 2, false, true));
		}
	}

	@Override
	public boolean canEat(World world, PlayerEntity player, ItemStack food) {
		return !this.haveConsumedFruit(player);
	}

}
