package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;
import com.integral.enigmaticlegacy.helpers.Perhaps;

import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.capability.ICurio;

public class MonsterCharm extends Item implements ICurio, IPerhaps {
	
 public static Properties integratedProperties = new Item.Properties();
 public static float bonusXPModifier = 2.0F;
 
 public static Perhaps aggressiveDamageModifier = new Perhaps(0);
 public static Perhaps undeadDamageModifier = new Perhaps(0);
 public static boolean lootingLevelEnabled = false;
 public static boolean xpBonusEnabled = false;
 
 public MonsterCharm(Properties properties) {
		super(properties);
 }
 
 public static void initConfigValues() {
	 	undeadDamageModifier = new Perhaps(EnigmaticLegacy.configHandler.MONSTER_CHARM_UNDEAD_DAMAGE.get());
		aggressiveDamageModifier = new Perhaps(EnigmaticLegacy.configHandler.MONSTER_CHARM_AGGRESSIVE_DAMAGE.get());
		lootingLevelEnabled = EnigmaticLegacy.configHandler.MONSTER_CHARM_BONUS_LOOTING.get();
		xpBonusEnabled = EnigmaticLegacy.configHandler.MONSTER_CHARM_DOUBLE_XP.get();
 }
 
 public static Properties setupIntegratedProperties() {
	 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(1);
	 integratedProperties.rarity(Rarity.EPIC);
	 
	 return integratedProperties;
 }
 
 @Override
 public boolean isForMortals() {
 	return EnigmaticLegacy.configLoaded ? EnigmaticLegacy.configHandler.MONSTER_CHARM_ENABLED.get() : false;
 }
 
 @OnlyIn(Dist.CLIENT)
 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
	 
	 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
	 
	 
	 if(ControlsScreen.hasShiftDown()) {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.monsterCharm1", undeadDamageModifier.asPercentage()+"%");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.monsterCharm2", aggressiveDamageModifier.asPercentage()+"%");
		 if (lootingLevelEnabled)
			 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.monsterCharm3");
		 if (xpBonusEnabled) {
			 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.monsterCharm4");
		 }
	 } else {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
	 }
 }
 
 @Override
 public void onCurioTick(String identifier, LivingEntity living) {
	 // Insert existential void here
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
  
}

