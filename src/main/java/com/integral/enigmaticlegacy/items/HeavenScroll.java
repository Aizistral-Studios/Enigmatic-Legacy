package com.integral.enigmaticlegacy.items;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;

import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.capability.ICurio;

public class HeavenScroll extends Item implements ICurio, IPerhaps {
	
 public static Properties integratedProperties = new Item.Properties();
 public static HashMap<PlayerEntity, Boolean> flyMap = new HashMap<PlayerEntity, Boolean>();
 
 public static double xpConsumptionMultiplier;

 public HeavenScroll(Properties properties) {
		super(properties);
 }
 
 public static Properties setupIntegratedProperties() {
	 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(1);
	 integratedProperties.rarity(Rarity.EPIC);
	 
	 return integratedProperties;
 }
 
 public static void initConfigValues() {
	 xpConsumptionMultiplier = EnigmaticLegacy.configHandler.HEAVEN_SCROLL_XP_COST_MODIFIER.get();
 }
 
 @Override
 public boolean isForMortals() {
 	return EnigmaticLegacy.configLoaded ? EnigmaticLegacy.configHandler.HEAVEN_SCROLL_ENABLED.get() : false;
 }
 
 @OnlyIn(Dist.CLIENT)
 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
	 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
	 
	 if(ControlsScreen.hasShiftDown()) {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.heavenTome1");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.heavenTome2");
	 } else {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
	 }
 }
 
 @Override
 public void onCurioTick(String identifier, LivingEntity living) {
	 if (living.world.isRemote)
		 return;
	 
	 if (living instanceof PlayerEntity) {
		 PlayerEntity player = (PlayerEntity) living;
		 
		 if (Math.random() <= (0.025D*xpConsumptionMultiplier) & player.abilities.isFlying)
			 player.giveExperiencePoints(-1);
		 
		 try {
		 if (player.experienceTotal > 0) {
			 
		 if (!player.abilities.allowFlying)
			 player.abilities.allowFlying = true;
		 	 player.sendPlayerAbilities();
		 	 HeavenScroll.flyMap.put(player, true);
		 } else if (HeavenScroll.flyMap.get(player)) {
			 player.abilities.allowFlying = false;
			 player.abilities.isFlying = false;
			 player.sendPlayerAbilities();
			 HeavenScroll.flyMap.put(player, false);
		 }
		 
		 } catch (NullPointerException ex) {
			 HeavenScroll.flyMap.put(player, false);
		 }
	 }
	 
 }
 
  @Override
  public boolean canRightClickEquip() {
      return true;
  }
  
  @Override
  public void onEquipped(String identifier, LivingEntity entityLivingBase) {
	  // Insert existential void here
  }
  
  @Override
  public void onUnequipped(String identifier, LivingEntity entityLivingBase) {
	  	
	  if (entityLivingBase instanceof PlayerEntity) {
		  PlayerEntity player = (PlayerEntity) entityLivingBase;
		  
			 player.abilities.allowFlying = false;
			 player.abilities.isFlying = false;
			 player.sendPlayerAbilities();
			 HeavenScroll.flyMap.put(player, false);
	  
	  }
  }
  
}
