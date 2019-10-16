package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;

import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnchantmentTransposer extends Item implements IPerhaps {
	
 public static Properties integratedProperties = new Item.Properties();

 public EnchantmentTransposer(Properties properties) {
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
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enchantmentTransposer1");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enchantmentTransposer2");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enchantmentTransposer3");
	 } else {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
	 }

 }

 @Override
 public boolean isForMortals() {
	 return ConfigHandler.ENCHANTMENT_TRANSPOSER_ENABLED.getValue();
 }
 
  
}

