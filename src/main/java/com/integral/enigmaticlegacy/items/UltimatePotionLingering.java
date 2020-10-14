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

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class UltimatePotionLingering extends ItemBase implements IAdvancedPotionItem {
	public PotionType potionType;

	public UltimatePotionLingering(Rarity rarity, PotionType type) {
		super(ItemBase.getDefaultProperties().rarity(rarity).maxStackSize(1).group(EnigmaticLegacy.enigmaticPotionTab));

		this.potionType = type;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getDefaultInstance() {
		ItemStack stack = super.getDefaultInstance().copy();
		PotionHelper.setAdvancedPotion(stack, EnigmaticLegacy.EMPTY);
		return stack.copy();
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		return this.getTranslationKey() + ".effect." + PotionHelper.getAdvancedPotion(stack).getId();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		SuperpositionHandler.addPotionTooltip(PotionHelper.getEffects(stack), stack, list, 0.25F);
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.isInGroup(group)) {

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
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		ItemStack throwed = playerIn.abilities.isCreativeMode ? itemstack.copy() : itemstack.split(1);

		worldIn.playSound((PlayerEntity)null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_LINGERING_POTION_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (Item.random.nextFloat() * 0.4F + 0.8F));
		if (!worldIn.isRemote) {
			EnigmaticPotionEntity potionEntity = new EnigmaticPotionEntity(worldIn, playerIn);
			potionEntity.setItem(throwed);
			potionEntity.shoot(playerIn.rotationPitch, playerIn.rotationYaw, -20.0F, 0.5F, 1.0F);
			worldIn.addEntity(potionEntity);
		}

		playerIn.addStat(Stats.ITEM_USED.get(this));
		return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
	}

	@Override
	public PotionType getPotionType() {
		return this.potionType;
	}

}
