package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.AdvancedPotion;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.helpers.PotionHelper;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class UltimatePotionBase extends Item implements IPerhaps {
	
 public static Properties integratedProperties = new Item.Properties();
 public boolean common;
 
 public UltimatePotionBase(Properties properties, boolean common) {
		super(properties);
		
		this.common = common;
 }
 
 public static Properties setupIntegratedProperties(Rarity rarity) {
	 integratedProperties.group(EnigmaticLegacy.enigmaticPotionTab);
	 integratedProperties.maxStackSize(1);
	 integratedProperties.rarity(rarity);
	 
	 return integratedProperties;
 
 }
 
 public static void initConfigValues() {
	 // Insert existential void here
 }
 
 @Override
 public boolean isForMortals() {
 	return true;
 }
 
 @OnlyIn(Dist.CLIENT)
 public boolean hasEffect(ItemStack stack) {
    return true;
 }
 
 @OnlyIn(Dist.CLIENT)
 public ItemStack getDefaultInstance() {
	ItemStack stack = super.getDefaultInstance().copy();
	PotionHelper.setAdvancedPotion(stack, EnigmaticLegacy.EMPTY);
    return stack.copy();
 }
 
 public String getTranslationKey(ItemStack stack) {
     return this.getTranslationKey() + ".effect." + PotionHelper.getAdvancedPotion(stack).getId();
  }
 
 @OnlyIn(Dist.CLIENT)
 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
	 SuperpositionHandler.addPotionTooltip(PotionHelper.getEffects(stack), stack, list, 1.0F);
 }
  
  @Override
  public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
      if (this.isInGroup(group)) {
    	  
    	  if (this.common) {
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
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
      PlayerEntity playerentity = entityLiving instanceof PlayerEntity ? (PlayerEntity)entityLiving : null;
      List<EffectInstance> effectList = PotionHelper.getEffects(stack);
      if (playerentity == null || !playerentity.abilities.isCreativeMode) {
          stack.shrink(1);
       }
      
      if (playerentity instanceof ServerPlayerEntity) {
         CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity)playerentity, stack);
      }

      if (!worldIn.isRemote) {
         for(EffectInstance effectinstance : effectList) {
            if (effectinstance.getPotion().isInstant()) {
               effectinstance.getPotion().affectEntity(playerentity, playerentity, entityLiving, effectinstance.getAmplifier(), 1.0D);
            } else {
               entityLiving.addPotionEffect(new EffectInstance(effectinstance));
            }
         }
      }

      if (playerentity != null) {
         playerentity.addStat(Stats.ITEM_USED.get(this));
      }

      if (playerentity == null || !playerentity.abilities.isCreativeMode) {
         if (stack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
         }

         if (playerentity != null) {
            playerentity.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
         }
      }

      return stack;
   }
  
  @Override
  public int getUseDuration(ItemStack stack) {
     return 32;
  }

 @Override
  public UseAction getUseAction(ItemStack stack) {
     return UseAction.DRINK;
  }

 @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
     playerIn.setActiveHand(handIn);
     return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
  }
  
}

