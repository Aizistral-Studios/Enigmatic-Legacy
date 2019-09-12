package com.integral.enigmaticlegacy.items;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
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

public class EnigmaticItem extends Item implements ICurio {
 
 public static HashMap<PlayerEntity, Boolean> flightMap = new HashMap<PlayerEntity, Boolean>();
 public static Properties integratedProperties = new Item.Properties();

 public EnigmaticItem(Properties properties) {
		super(properties);
 }
 
 public static Properties setupIntegratedProperties() {
	 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(1);
	 integratedProperties.rarity(Rarity.EPIC);
	 
	 return integratedProperties;
 
 }
 
 public static void initConfigValues() {
	 // Insert existential void here
 }
 
 @OnlyIn(Dist.CLIENT)
 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
	 
	 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
	 
	 if(ControlsScreen.hasShiftDown()) {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem1");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem2");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem3");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem4");
	 } else {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
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
	  // Insert existential void here
  }
  
  @Override
  public void onCurioTick(String identifier, LivingEntity entityLivingBase) {
	  if (entityLivingBase.isBurning())
		  entityLivingBase.extinguish();

	  entityLivingBase.clearActivePotions();
  }
  
  public static void handleEnigmaticFlight(final PlayerEntity player) {
      try {
          if (SuperpositionHandler.hasCurio((LivingEntity)player, EnigmaticLegacy.enigmaticItem)) {
              flightMap.put(player, true);
              if (!player.abilities.allowFlying) {
                  player.abilities.allowFlying = true;
                  player.sendPlayerAbilities();
              }
          }
          else if (flightMap.get(player)) {
              player.abilities.allowFlying = false;
              player.abilities.isFlying = false;
              player.sendPlayerAbilities();
              flightMap.put(player, false);
          }
      }
      catch (NullPointerException ex) {
          flightMap.put(player, false);
      }
  }
  
}
