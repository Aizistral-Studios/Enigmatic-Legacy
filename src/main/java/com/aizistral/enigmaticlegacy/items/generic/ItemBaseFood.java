package com.aizistral.enigmaticlegacy.items.generic;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class ItemBaseFood extends ItemBase {

	public ItemBaseFood() {
		this(getDefaultProperties(), buildDefaultFood());
	}

	public ItemBaseFood(Properties props, FoodProperties food) {
		super(props.food(food));
	}

	public boolean canEat(Level world, Player player, ItemStack food) {
		return true;
	}

	public void onConsumed(Level worldIn, Player player, ItemStack food) {
		// NO-OP
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
		if (entityLiving instanceof Player) {
			this.onConsumed(worldIn, (Player) entityLiving, stack);
		}

		return super.finishUsingItem(stack, worldIn, entityLiving);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		if (this.canEat(worldIn, playerIn, playerIn.getItemInHand(handIn)))
			return super.use(worldIn, playerIn, handIn);
		else
			return new InteractionResultHolder<>(InteractionResult.PASS, playerIn.getItemInHand(handIn));
	}

	protected static FoodProperties buildDefaultFood() {
		return new FoodProperties.Builder().nutrition(0).saturationMod(0).alwaysEat().build();
	}

}
