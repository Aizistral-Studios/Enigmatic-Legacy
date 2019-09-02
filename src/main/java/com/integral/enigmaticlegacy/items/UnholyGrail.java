package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;
import com.integral.enigmaticlegacy.triggers.UseUnholyGrailTrigger;

import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class UnholyGrail extends Item implements IPerhaps {
	
 public static Properties integratedProperties = new Item.Properties();

 public UnholyGrail(Properties properties) {
		super(properties);
 }
 
 public static Properties setupIntegratedProperties() {
	 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(1);
	 integratedProperties.rarity(Rarity.EPIC);
	 
	 return integratedProperties;
 
 }
 
 public static void initConfigValues() {}
 
 @Override
 public boolean isForMortals() {
 	return EnigmaticLegacy.configLoaded ? EnigmaticLegacy.configHandler.UNHOLY_GRAIL_ENABLED.get() : false;
 }
 
 @OnlyIn(Dist.CLIENT)
 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
	 if(ControlsScreen.hasShiftDown()) {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.unholyGrail1");
	 } else {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
	 }
 }
 
 @Override
 public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
	 if (!(entityLiving instanceof PlayerEntity))
		 return stack;
	 
     PlayerEntity player = (PlayerEntity)entityLiving;
     
     if (!worldIn.isRemote) {
    	 player.addPotionEffect(new EffectInstance(Effects.WITHER, 100, 2, false, true));
    	 player.addPotionEffect(new EffectInstance(Effects.POISON, 160, 1, false, true));
    	 player.addPotionEffect(new EffectInstance(Effects.NAUSEA, 240, 0, false, true));
    	 player.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 200, 1, false, true));
    	 player.addPotionEffect(new EffectInstance(Effects.HUNGER, 160, 2, false, true));
    	 player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 240, 0, false, true));
    	 
    	 UseUnholyGrailTrigger.INSTANCE.trigger((ServerPlayerEntity) player);
    	 
     }
     
     player.addStat(Stats.ITEM_USED.get(this));

     return stack;
  }
 
 /**
  * How long it takes to use or consume an item
  */
 @Override
 public int getUseDuration(ItemStack stack) {
    return 32;
 }

 /**
  * returns the action that specifies what animation to play when the items is being used
  */
@Override
 public UseAction getUseAction(ItemStack stack) {
    return UseAction.DRINK;
 }

 /**
  * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
  * {@link #onItemUse}.
  */
@Override
 public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    playerIn.setActiveHand(handIn);
    return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
 }
  
  
}
