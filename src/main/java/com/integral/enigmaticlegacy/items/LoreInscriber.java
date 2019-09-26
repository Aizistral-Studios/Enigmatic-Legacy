package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;

import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class LoreInscriber extends Item implements IPerhaps {
	
 public static Properties integratedProperties = new Item.Properties();

 public LoreInscriber(Properties properties) {
		super(properties);
 }
 
 public static Properties setupIntegratedProperties() {
	 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(1);
	 integratedProperties.rarity(Rarity.EPIC);
	 
	 return integratedProperties;
 
 }

 @Override
 public boolean isForMortals() {
	 return true;
 }
 
 @OnlyIn(Dist.CLIENT)
 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
	 
	 if(ControlsScreen.hasShiftDown()) {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.loreInscriber1");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.loreInscriber2");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.loreInscriber3");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.loreInscriber4");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.loreInscriber5");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.loreInscriber6");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.loreInscriber7");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.loreInscriber8");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.loreInscriber9");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.loreInscriber10");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.loreInscriber11");
	 } else {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
	 }
	 
 }
  
}

