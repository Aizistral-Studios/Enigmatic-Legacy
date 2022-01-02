package com.integral.enigmaticlegacy.items.generic;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.world.entity.player.ServerPlayerEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAction;
import net.minecraft.world.item.Food;
import net.minecraft.world.item.Item.Properties;
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
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		if (entityLiving instanceof PlayerEntity) {
			this.onConsumed(worldIn, (PlayerEntity) entityLiving, stack);
		}

		return super.finishUsingItem(stack, worldIn, entityLiving);
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (this.canEat(worldIn, playerIn, playerIn.getItemInHand(handIn)))
			return super.use(worldIn, playerIn, handIn);
		else
			return new ActionResult<>(ActionResultType.PASS, playerIn.getItemInHand(handIn));
	}

	protected static Food buildDefaultFood() {
		return new Food.Builder().nutrition(0).saturationMod(0).alwaysEat().build();
	}

}
