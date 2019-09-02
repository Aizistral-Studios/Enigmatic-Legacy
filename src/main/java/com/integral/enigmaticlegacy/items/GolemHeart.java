package com.integral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;
import com.integral.enigmaticlegacy.helpers.Perhaps;

import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.capability.ICurio;

public class GolemHeart extends Item implements ICurio, IPerhaps {
	
 public static Properties integratedProperties = new Item.Properties();
 public static List<String> immunityList = new ArrayList<String>();
 public static HashMap<String, Float> resistanceList = new HashMap<String, Float>();
 
 public static Multimap<String, AttributeModifier> attributesDefault = HashMultimap.create();
 public static Multimap<String, AttributeModifier> attributesNoArmor = HashMultimap.create();

 public static int abilityCooldown = 0;
 public static double armorPointsDefault = 0;
 public static double armorPointsSuper = 0;
 public static double armorToughnessSuper = 0;
 public static Perhaps knockbackResistance = new Perhaps(0);
 public static Perhaps explosionResistance = new Perhaps(0);
 public static Perhaps meleeResistance = new Perhaps(0);
 public static double magicVulnerability = 0D;
 
 public GolemHeart(Properties properties) {
		super(properties);
 }
 
 public static Properties setupIntegratedProperties() {
	 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(1);
	 integratedProperties.rarity(Rarity.UNCOMMON);
	 
	 immunityList.add(DamageSource.CACTUS.damageType);
	 immunityList.add(DamageSource.CRAMMING.damageType);
	 immunityList.add(DamageSource.IN_WALL.damageType);
	 immunityList.add(DamageSource.FALLING_BLOCK.damageType);
	 immunityList.add(DamageSource.SWEET_BERRY_BUSH.damageType);
	 
	 return integratedProperties;
 
 }
 
 public static void initConfigValues() {
	 abilityCooldown = EnigmaticLegacy.configHandler.GOLEM_HEART_COOLDOWN.get();
	 armorPointsDefault = EnigmaticLegacy.configHandler.GOLEM_HEART_DEFAULT_ARMOR.get();
	 armorPointsSuper = EnigmaticLegacy.configHandler.GOLEM_HEART_SUPER_ARMOR.get();
	 armorToughnessSuper = EnigmaticLegacy.configHandler.GOLEM_HEART_SUPER_ARMOR_TOUGHNESS.get();
	 knockbackResistance = new Perhaps(EnigmaticLegacy.configHandler.GOLEM_HEART_KNOCKBACK_RESISTANCE.get());
	 explosionResistance = new Perhaps(EnigmaticLegacy.configHandler.GOLEM_HEART_EXPLOSION_RESISTANCE.get());
	 meleeResistance = new Perhaps(EnigmaticLegacy.configHandler.GOLEM_HEART_MELEE_RESISTANCE.get());
	 magicVulnerability = EnigmaticLegacy.configHandler.GOLEM_HEART_VULNERABILITY_MODIFIER.get();
	 
	 
	 attributesDefault.put(SharedMonsterAttributes.ARMOR.getName(),
             new AttributeModifier(UUID.fromString("15faf191-bf21-4654-b359-cc1f4f1243bf"), "GolemHeart DAB", armorPointsDefault,
                                   AttributeModifier.Operation.ADDITION));
	 
	 attributesDefault.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(),
			 new AttributeModifier(UUID.fromString("10faf191-bf21-4554-b359-cc1f4f1233bf"), "GolemHeart KR", knockbackResistance.asModifier(false),
					 AttributeModifier.Operation.ADDITION));
	 
	 
	 
	 attributesNoArmor.put(SharedMonsterAttributes.ARMOR.getName(),
             new AttributeModifier(UUID.fromString("14faf191-bf23-4654-b359-cc1f4f1243bf"), "GolemHeart SAB", armorPointsSuper,
                                   AttributeModifier.Operation.ADDITION));
	 
	 attributesNoArmor.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(),
             new AttributeModifier(UUID.fromString("11faf181-bf23-4354-b359-cc1f5f1253bf"), "GolemHeart STB", armorToughnessSuper,
                                   AttributeModifier.Operation.ADDITION));
	 
	 attributesNoArmor.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(),
			 new AttributeModifier(UUID.fromString("12faf181-bf21-4554-b359-cc1f4f1254bf"), "GolemHeart KR", knockbackResistance.asModifier(false),
					 AttributeModifier.Operation.ADDITION));
	 
	 
	 resistanceList.put(DamageSource.GENERIC.damageType, meleeResistance.asModifierInverted());
	 resistanceList.put("mob", meleeResistance.asModifierInverted());
	 resistanceList.put("explosion", explosionResistance.asModifierInverted());
	 resistanceList.put("explosion.player", explosionResistance.asModifierInverted());
	 resistanceList.put("player", meleeResistance.asModifierInverted());
	 
	 resistanceList.put(DamageSource.MAGIC.damageType, (float) magicVulnerability);
	 resistanceList.put(DamageSource.DRAGON_BREATH.damageType, (float) magicVulnerability);
	 
 }
 
 @Override
 public boolean isForMortals() {
 	return EnigmaticLegacy.configLoaded ? EnigmaticLegacy.configHandler.GOLEM_HEART_ENABLED.get() : false;
 }
 
 @OnlyIn(Dist.CLIENT)
 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
	 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
	 
	 if(ControlsScreen.hasShiftDown()) {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart1");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart2");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeartCooldown", ((float)(abilityCooldown))/20.0F);
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart3");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart4", (int)armorPointsDefault);
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart5");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart6", (int)armorPointsSuper, (int)armorToughnessSuper);
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart7", knockbackResistance.asPercentage()+"%");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart8", meleeResistance.asPercentage()+"%");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart9", explosionResistance.asPercentage()+"%");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart10");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart11");
	 } else {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
	 }
	 
	 	 try {
	 		LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
	 		LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.currentKeybind", KeyBinding.getDisplayString("key.spellstoneAbility").get().toUpperCase());
		 } catch (NullPointerException ex) {
			// Just don't do it lol 
		 }
	 
	 
 }
 
 public void triggerActiveAbility(World world, ServerPlayerEntity player, ItemStack stack) {
	 if (SuperpositionHandler.hasSpellstoneCooldown(player))
		 return;
	 //Insert existential void here
 }
 
  @Override
  public boolean canRightClickEquip() {

    return true;
  }
  
  @Override
  public void onEquipped(String identifier, LivingEntity living) {
	  //Insert existential void here
  }
  
  @Override
  public void onUnequipped(String identifier, LivingEntity living) {
	  if (living instanceof PlayerEntity) {
		  PlayerEntity player = (PlayerEntity) living;
		  
		  AbstractAttributeMap map = player.getAttributes();
		  map.removeAttributeModifiers(attributesDefault);
		  map.removeAttributeModifiers(attributesNoArmor);
	  }
  }
  
  @Override
  public void onCurioTick(String identifier, LivingEntity living) {
	  if (living instanceof PlayerEntity) {
		  PlayerEntity player = (PlayerEntity) living;
		  
		  AbstractAttributeMap map = player.getAttributes();
		  int armorAmount = 0;
		  
		  for (ItemStack stack : player.getArmorInventoryList()) {
			  if (!stack.isEmpty())
			  armorAmount++;
		  }
		  /*
		  if (player.ticksExisted % 20 == 0)
		  System.out.println("Armor stacks: " + player.inventory.getStackInSlot(100) + ", " + player.inventory.getStackInSlot(101) + ", " + player.inventory.getStackInSlot(102) + ", " + player.inventory.getStackInSlot(103));
		  
		  if (player.inventory.getStackInSlot(100) != null || player.inventory.getStackInSlot(101) != null || player.inventory.getStackInSlot(102) != null || player.inventory.getStackInSlot(103) != null) {
			*/  
		  if (armorAmount != 0) {
			  map.removeAttributeModifiers(attributesDefault);
			  map.removeAttributeModifiers(attributesNoArmor);
			  
			  map.applyAttributeModifiers(attributesDefault);			  
		  } else {
			  map.removeAttributeModifiers(attributesDefault);
			  map.removeAttributeModifiers(attributesNoArmor);
			  
			  map.applyAttributeModifiers(attributesNoArmor);	
		  }
		  
	  }
  }
  
}
