package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.IAdvancedPotionItem;
import com.integral.enigmaticlegacy.entities.EnigmaticPotionEntity;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.helpers.PotionHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;
import com.integral.enigmaticlegacy.objects.AdvancedPotion;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemGroup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.integral.enigmaticlegacy.api.items.IAdvancedPotionItem.PotionType;

public class UltimatePotionSplash extends ItemBase implements IAdvancedPotionItem {
	public PotionType potionType;

	public UltimatePotionSplash(Rarity rarity, PotionType type) {
		super(ItemBase.getDefaultProperties().rarity(rarity).stacksTo(1).tab(EnigmaticLegacy.enigmaticPotionTab));

		this.potionType = type;
	}

	@Override
	public String getDescriptionId(ItemStack stack) {
		return this.getDescriptionId() + ".effect." + PotionHelper.getAdvancedPotion(stack).getId();
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
	public ActionResult<ItemStack> use(Level worldIn, Player playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		ItemStack throwed = playerIn.abilities.instabuild ? itemstack.copy() : itemstack.split(1);

		worldIn.playSound((Player)null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.LINGERING_POTION_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (Item.random.nextFloat() * 0.4F + 0.8F));
		if (!worldIn.isClientSide) {
			EnigmaticPotionEntity potionEntity = new EnigmaticPotionEntity(worldIn, playerIn);
			potionEntity.setItem(throwed);
			potionEntity.shootFromRotation(playerIn, playerIn.xRot, playerIn.yRot, -20.0F, 0.5F, 1.0F);
			worldIn.addFreshEntity(potionEntity);
		}

		playerIn.awardStat(Stats.ITEM_USED.get(this));
		return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
	}

	@Override
	public PotionType getPotionType() {
		return this.potionType;
	}

}
