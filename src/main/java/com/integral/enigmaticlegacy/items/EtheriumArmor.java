package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EtheriumArmor extends ArmorItem implements IPerhaps {
	
 //public Properties integratedProperties;
	
 public EtheriumArmor(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builder) {
		super(materialIn, slot, builder);
 }
 
 public static Properties setupIntegratedProperties() {
	 Properties properties = new Item.Properties();
	 properties.group(EnigmaticLegacy.enigmaticTab);
	 properties.rarity(Rarity.RARE);
	 
	 return properties;
 
 }

 public static boolean hasFullSet(@Nonnull PlayerEntity player) {
	 if (player == null)
		 return false;
	 
	 for (ItemStack stack : player.getArmorInventoryList()) {
			 if (!(stack.getItem() instanceof EtheriumArmor))
				 return false;
	 }
	 
	 return true;
 }
 
 public static boolean hasShield(@Nonnull PlayerEntity player) {
	 if (player != null)
	 if (hasFullSet(player) && player.getHealth()/player.getMaxHealth() <= ConfigHandler.ETHERIUM_ARMOR_SHIELD_THRESHOLD.getValue().asMultiplier(false))
		 return true;
	 
		 return false;
 }
 
 @Override
 public boolean isForMortals() {
	 return ConfigHandler.ETHERIUM_ARMOR_ENABLED.getValue();
 }
 
 @OnlyIn(Dist.CLIENT)
 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
	 if(ControlsScreen.hasShiftDown()) {
		 if (stack.getItem().equals(EnigmaticLegacy.etheriumHelmet))
			 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumHelmet1");
		 else if (stack.getItem().equals(EnigmaticLegacy.etheriumChestplate))
			 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumChestplate1");
		 else if (stack.getItem().equals(EnigmaticLegacy.etheriumLeggings))
			 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumLeggings1");
		 else if (stack.getItem().equals(EnigmaticLegacy.etheriumBoots))
			 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumBoots1");
	 } else {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
	 }

		 if (hasFullSet(Minecraft.getInstance().player)) {
		 	LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 	LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumArmorSetBonus1");
		 	LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumArmorSetBonus2", ConfigHandler.ETHERIUM_ARMOR_SHIELD_THRESHOLD.getValue().asPercentage() + "%");
		 	LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumArmorSetBonus3");
		 	LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumArmorSetBonus4", ConfigHandler.ETHERIUM_ARMOR_SHIELD_REDUCTION.getValue().asPercentage() + "%");
		 	LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumArmorSetBonus5");
		 	LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumArmorSetBonus6");
	 	}
		 
	if (stack.isEnchanted())
		LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
	 
 	
	 
 }
 
  
}

