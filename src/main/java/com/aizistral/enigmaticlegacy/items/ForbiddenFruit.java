package com.aizistral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBase;
import com.aizistral.enigmaticlegacy.objects.TransientPlayerData;
import com.aizistral.enigmaticlegacy.triggers.ForbiddenFruitTrigger;
import com.aizistral.omniconfig.Configuration;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ForbiddenFruit extends ItemBase implements Vanishable {
	public static final String consumedFruitTag = "ConsumedForbiddenFruit";
	public static Omniconfig.PerhapsParameter regenerationSubtraction;
	public static Omniconfig.DoubleParameter debuffDurationMultiplier;
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

			debuffDurationMultiplier = builder
					.comment("Multiplier for duration of debuffs applied upon consumption of The Forbidden Fruit. Setting it to 0 will disable debuffs entirely.")
					.getDouble("DebuffDurationMultiplier", 1.0);
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
		super(getDefaultProperties().rarity(Rarity.RARE).fireResistant());
	}


	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.forbiddenFruit1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.forbiddenFruit2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.forbiddenFruit3", ChatFormatting.GOLD, regenerationSubtraction+"%");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.forbiddenFruitLore");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	public boolean haveConsumedFruit(Player player) {
		return TransientPlayerData.get(player).getConsumedForbiddenFruit();
	}

	public void defineConsumedFruit(Player player, boolean consumedOrNot) {
		TransientPlayerData.get(player).setConsumedForbiddenFruit(consumedOrNot);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
		worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.GENERIC_EAT, SoundSource.NEUTRAL, 1.0F, 1.0F + (entityLiving.getRandom().nextFloat() - entityLiving.getRandom().nextFloat()) * 0.4F);

		if (!(entityLiving instanceof Player) || !((Player)entityLiving).getAbilities().instabuild) {
			stack.shrink(1);
		}

		entityLiving.gameEvent(GameEvent.EAT);

		if (entityLiving instanceof Player) {
			this.onConsumed(worldIn, (Player) entityLiving, stack);
		}

		return super.finishUsingItem(stack, worldIn, entityLiving);
	}

	public void onConsumed(Level worldIn, Player player, ItemStack food) {
		this.defineConsumedFruit(player, true);

		if (player instanceof ServerPlayer playerMP) {
			ForbiddenFruitTrigger.INSTANCE.trigger(playerMP);
			double multiplier = debuffDurationMultiplier.getValue();

			player.addEffect(new MobEffectInstance(MobEffects.WITHER,            (int) (300 * multiplier), 3, false, true));
			player.addEffect(new MobEffectInstance(MobEffects.CONFUSION,         (int) (300 * multiplier), 2, false, true));
			player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,          (int) (400 * multiplier), 3, false, true));
			player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, (int) (500 * multiplier), 2, false, true));
		}
	}

	public boolean canEat(Level world, Player player, ItemStack food) {
		return !this.haveConsumedFruit(player);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		if (this.canEat(worldIn, playerIn, playerIn.getItemInHand(handIn))) {
			playerIn.startUsingItem(handIn);
			return InteractionResultHolder.consume(playerIn.getItemInHand(handIn));
		} else
			return InteractionResultHolder.pass(playerIn.getItemInHand(handIn));
	}

	@Override
	public int getUseDuration(ItemStack pStack) {
		return 32;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack pStack) {
		return UseAnim.EAT;
	}

}
