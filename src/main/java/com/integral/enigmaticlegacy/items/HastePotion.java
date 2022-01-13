package com.integral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Deprecated since update 1.2.0.
 * @author Integral
 */

@Deprecated
public class HastePotion extends ItemBase {

	public List<MobEffectInstance> effectList;

	public HastePotion(Rarity rarity, int duration, int amplifier) {
		super(ItemBase.getDefaultProperties().rarity(rarity).stacksTo(1).tab(null));

		this.effectList = new ArrayList<MobEffectInstance>();
		this.effectList.add(new MobEffectInstance(MobEffects.DIG_SPEED, duration, amplifier, false, true));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		SuperpositionHandler.addPotionTooltip(this.effectList, stack, list, 1.0F);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
		Player player = entityLiving instanceof Player ? (Player) entityLiving : null;
		if (player == null || !player.getAbilities().instabuild) {
			stack.shrink(1);
		}

		if (player instanceof ServerPlayer) {
			CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
		}

		if (!worldIn.isClientSide && player != null) {
			for (MobEffectInstance instance : this.effectList) {
				player.addEffect(new MobEffectInstance(instance));
			}
		}

		if (player == null || !player.getAbilities().instabuild) {
			if (stack.isEmpty())
				return new ItemStack(Items.GLASS_BOTTLE);

			if (player != null) {
				player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
			}
		}

		return stack;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 32;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.DRINK;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		playerIn.startUsingItem(handIn);
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(handIn));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(ItemStack stack) {
		return true;
	}

}
