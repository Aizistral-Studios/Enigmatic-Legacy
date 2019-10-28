package com.integral.enigmaticlegacy.items;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.helpers.LoreHelper;

import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GemOfBinding extends Item {
	
 public static Properties integratedProperties = new Item.Properties();
 
 public GemOfBinding(Properties properties) {
		super(properties);
 }

 public static Properties setupIntegratedProperties() {
	 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(1);
	 integratedProperties.rarity(Rarity.UNCOMMON);
	 
	 return integratedProperties;
 
 }
 
 @OnlyIn(Dist.CLIENT)
 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
	 if(ControlsScreen.hasShiftDown()) {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.gemOfBinding1");
	 } else {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
	 }
	 
	 if (ItemNBTHelper.verifyExistance(stack, "BoundPlayer")) {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.boundToPlayer", ItemNBTHelper.getString(stack, "BoundPlayer", "Herobrine"));
	 }
 }
 
 @OnlyIn(Dist.CLIENT)
 public boolean hasEffect(ItemStack stack) {
    return ItemNBTHelper.verifyExistance(stack, "BoundPlayer") || super.hasEffect(stack);
 }

 public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand hand) {
	ItemStack itemstack = player.getHeldItem(hand);
	
	if (player.isSneaking()) {
    	ItemNBTHelper.setString(itemstack, "BoundPlayer", player.getDisplayName().getString());
    	ItemNBTHelper.setUUID(itemstack, "BoundUUID", player.getUniqueID());
    	
    	worldIn.playSound(null, player.getPosition(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1.0F, (float) (0.9F + (Math.random()*0.1F)));
    	
    	player.swingArm(hand);
    	return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }
	
	return new ActionResult<>(ActionResultType.FAIL, itemstack);
 }
 
}
