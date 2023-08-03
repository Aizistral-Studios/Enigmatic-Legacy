package com.aizistral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.items.IAdvancedPotionItem;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.aizistral.enigmaticlegacy.helpers.PotionHelper;
import com.aizistral.enigmaticlegacy.items.EnigmaticAmulet.AmuletColor;
import com.aizistral.enigmaticlegacy.items.generic.ItemBase;
import com.aizistral.enigmaticlegacy.objects.AdvancedPotion;
import com.aizistral.enigmaticlegacy.registries.EnigmaticPotions;
import com.google.common.collect.ImmutableList;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class UltimatePotionBase extends ItemBase implements IAdvancedPotionItem {
	public PotionType potionType;

	public UltimatePotionBase(Rarity rarity, PotionType type) {
		super(ItemBase.getDefaultProperties().rarity(rarity).stacksTo(1));

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
		PotionHelper.setAdvancedPotion(stack, EnigmaticPotions.EMPTY_POTION);
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
	public CreativeModeTab getCreativeTab() {
		return EnigmaticLegacy.potionTab;
	}

	@Override
	public List<ItemStack> getCreativeTabStacks() {
		ImmutableList.Builder<ItemStack> items = ImmutableList.builder();

		if (this.potionType == PotionType.COMMON) {
			for (AdvancedPotion potion : EnigmaticPotions.COMMON_POTIONS) {
				ItemStack stack = new ItemStack(this);
				ItemNBTHelper.setString(stack, "EnigmaticPotion", potion.getId());
				items.add(stack);
			}
		} else {
			for (AdvancedPotion potion : EnigmaticPotions.ULTIMATE_POTIONS) {
				ItemStack stack = new ItemStack(this);
				ItemNBTHelper.setString(stack, "EnigmaticPotion", potion.getId());
				items.add(stack);
			}
		}

		return items.build();
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
		Player player = entityLiving instanceof Player ? (Player) entityLiving : null;
		List<MobEffectInstance> effectList = PotionHelper.getEffects(stack);
		if (player == null || !player.getAbilities().instabuild) {
			stack.shrink(1);
		}

		if (player instanceof ServerPlayer) {
			CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
		}

		if (!worldIn.isClientSide) {
			for (MobEffectInstance effectinstance : effectList) {
				if (effectinstance.getEffect().isInstantenous()) {
					effectinstance.getEffect().applyInstantenousEffect(player, player, entityLiving, effectinstance.getAmplifier(), 1.0D);
				} else {
					entityLiving.addEffect(new MobEffectInstance(effectinstance));
				}
			}
		}

		if (player != null) {
			player.awardStat(Stats.ITEM_USED.get(this));
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
	public PotionType getPotionType() {
		return this.potionType;
	}

}
