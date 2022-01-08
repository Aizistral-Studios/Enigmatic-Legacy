package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.IAdvancedPotionItem;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.helpers.PotionHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;
import com.integral.enigmaticlegacy.objects.AdvancedPotion;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemGroup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.integral.enigmaticlegacy.api.items.IAdvancedPotionItem.PotionType;

public class UltimatePotionBase extends ItemBase implements IAdvancedPotionItem {
	public PotionType potionType;

	public UltimatePotionBase(Rarity rarity, PotionType type) {
		super(ItemBase.getDefaultProperties().rarity(rarity).stacksTo(1).tab(EnigmaticLegacy.enigmaticPotionTab));

		this.potionType = type;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(ItemStack stack) {
		return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getDefaultInstance() {
		ItemStack stack = super.getDefaultInstance().copy();
		PotionHelper.setAdvancedPotion(stack, EnigmaticLegacy.EMPTY);
		return stack.copy();
	}

	@Override
	public String getDescriptionId(ItemStack stack) {
		return this.getDescriptionId() + ".effect." + PotionHelper.getAdvancedPotion(stack).getId();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		SuperpositionHandler.addPotionTooltip(PotionHelper.getEffects(stack), stack, list, 1.0F);
	}

	@Override
	public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.allowdedIn(group)) {

			if (this.potionType == PotionType.COMMON) {
				for (AdvancedPotion potion : EnigmaticLegacy.commonPotionTypes) {
					ItemStack stack = new ItemStack(this);
					ItemNBTHelper.setString(stack, "EnigmaticPotion", potion.getId());
					items.add(stack);
				}
			} else {
				for (AdvancedPotion potion : EnigmaticLegacy.ultimatePotionTypes) {
					ItemStack stack = new ItemStack(this);
					ItemNBTHelper.setString(stack, "EnigmaticPotion", potion.getId());
					items.add(stack);
				}
			}
		}

	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
		Player Player = entityLiving instanceof Player ? (Player) entityLiving : null;
		List<MobEffectInstance> effectList = PotionHelper.getEffects(stack);
		if (Player == null || !Player.abilities.instabuild) {
			stack.shrink(1);
		}

		if (Player instanceof ServerPlayer) {
			CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) Player, stack);
		}

		if (!worldIn.isClientSide) {
			for (MobEffectInstance effectinstance : effectList) {
				if (effectinstance.getEffect().isInstantenous()) {
					effectinstance.getEffect().applyInstantenousEffect(Player, Player, entityLiving, effectinstance.getAmplifier(), 1.0D);
				} else {
					entityLiving.addEffect(new MobEffectInstance(effectinstance));
				}
			}
		}

		if (Player != null) {
			Player.awardStat(Stats.ITEM_USED.get(this));
		}

		if (Player == null || !Player.abilities.instabuild) {
			if (stack.isEmpty())
				return new ItemStack(Items.GLASS_BOTTLE);

			if (Player != null) {
				Player.inventory.add(new ItemStack(Items.GLASS_BOTTLE));
			}
		}

		return stack;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 32;
	}

	@Override
	public UseAction getUseAnimation(ItemStack stack) {
		return UseAction.DRINK;
	}

	@Override
	public ActionResult<ItemStack> use(Level worldIn, Player playerIn, Hand handIn) {
		playerIn.startUsingItem(handIn);
		return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getItemInHand(handIn));
	}

	@Override
	public PotionType getPotionType() {
		return this.potionType;
	}

}
