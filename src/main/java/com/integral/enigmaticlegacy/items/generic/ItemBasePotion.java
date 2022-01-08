package com.integral.enigmaticlegacy.items.generic;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.InteractionResult;
import net.minecraft.util.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.world.item.Item.Properties;

public abstract class ItemBasePotion extends ItemBase {

	public ItemBasePotion() {
		this(getDefaultProperties().stacksTo(1));
	}

	public ItemBasePotion(Properties props) {
		super(props);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity living) {
		if (living instanceof Player) {
			Player player =  (Player) living;

			this.onConsumed(worldIn, player, stack);

			if (player instanceof ServerPlayer) {
				CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
			}

			if (!player.abilities.instabuild) {
				stack.shrink(1);

				if (stack.isEmpty())
					return new ItemStack(Items.GLASS_BOTTLE);

				player.inventory.add(new ItemStack(Items.GLASS_BOTTLE));
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
	public ActionResult<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		if (this.canDrink(worldIn, playerIn, playerIn.getItemInHand(handIn))) {
			playerIn.startUsingItem(handIn);
			return super.use(worldIn, playerIn, handIn);
		} else
			return new ActionResult<>(InteractionResult.PASS, playerIn.getItemInHand(handIn));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(ItemStack stack) {
		return true;
	}

	public boolean canDrink(Level world, Player player, ItemStack potion) {
		return true;
	}

	public void onConsumed(Level worldIn, Player player, ItemStack potion) {
		// NO-OP
	}

}
