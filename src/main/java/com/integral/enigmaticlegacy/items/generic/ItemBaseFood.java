package com.integral.enigmaticlegacy.items.generic;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.item.Food;
import net.minecraft.item.Item.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class ItemBaseFood extends ItemBase {

	public ItemBaseFood() {
		this(getDefaultProperties(), buildDefaultFood());
	}

	public ItemBaseFood(Properties props, Food food) {
		super(props.food(food));
	}

	public boolean canEat(World world, PlayerEntity player, ItemStack food) {
		return true;
	}

	public void onConsumed(World worldIn, PlayerEntity player, ItemStack food) {
		// NO-OP
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		if (entityLiving instanceof PlayerEntity) {
			this.onConsumed(worldIn, (PlayerEntity) entityLiving, stack);
		}

		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (this.canEat(worldIn, playerIn, playerIn.getHeldItem(handIn)))
			return super.onItemRightClick(worldIn, playerIn, handIn);
		else
			return new ActionResult<>(ActionResultType.PASS, playerIn.getHeldItem(handIn));
	}

	protected static Food buildDefaultFood() {
		return new Food.Builder().hunger(0).saturation(0).setAlwaysEdible().build();
	}

}
