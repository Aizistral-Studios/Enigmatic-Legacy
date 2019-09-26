package com.integral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;

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
 public static HashMap<String, Supplier<Float>> resistanceList = new HashMap<String, Supplier<Float>>();
 
 public static Multimap<String, AttributeModifier> attributesDefault = HashMultimap.create();
 public static Multimap<String, AttributeModifier> attributesNoArmor = HashMultimap.create();
 
 public GolemHeart(Properties properties) {
		super(properties);
		
		immunityList.add(DamageSource.CACTUS.damageType);
		immunityList.add(DamageSource.CRAMMING.damageType);
		immunityList.add(DamageSource.IN_WALL.damageType);
		immunityList.add(DamageSource.FALLING_BLOCK.damageType);
		immunityList.add(DamageSource.SWEET_BERRY_BUSH.damageType);
		
		resistanceList.put(DamageSource.GENERIC.damageType, () -> ConfigHandler.GOLEM_HEART_MELEE_RESISTANCE.getValue().asModifierInverted());
		resistanceList.put("mob", () -> ConfigHandler.GOLEM_HEART_MELEE_RESISTANCE.getValue().asModifierInverted());
		resistanceList.put("explosion", () -> ConfigHandler.GOLEM_HEART_EXPLOSION_RESISTANCE.getValue().asModifierInverted());
		resistanceList.put("explosion.player", () -> ConfigHandler.GOLEM_HEART_EXPLOSION_RESISTANCE.getValue().asModifierInverted());
		resistanceList.put("player", () -> ConfigHandler.GOLEM_HEART_MELEE_RESISTANCE.getValue().asModifierInverted());
		 
		resistanceList.put(DamageSource.MAGIC.damageType, () -> (float)ConfigHandler.GOLEM_HEART_VULNERABILITY_MODIFIER.getValue());
		resistanceList.put(DamageSource.DRAGON_BREATH.damageType, () -> (float)ConfigHandler.GOLEM_HEART_VULNERABILITY_MODIFIER.getValue());
 }
 
 public static Properties setupIntegratedProperties() {
	 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(1);
	 integratedProperties.rarity(Rarity.UNCOMMON);
	 
	 return integratedProperties;
 }
 
 @Override
 public boolean canEquip(String identifier, LivingEntity living) {
	  if (SuperpositionHandler.hasCurio(living, EnigmaticLegacy.golemHeart))
		  return false;
	  else
		  return true;
 }
 
 public static void initAttributes() {
	 
	 
	 attributesDefault.put(SharedMonsterAttributes.ARMOR.getName(),
             new AttributeModifier(UUID.fromString("15faf191-bf21-4654-b359-cc1f4f1243bf"), "GolemHeart DAB", ConfigHandler.GOLEM_HEART_DEFAULT_ARMOR.getValue(),
                                   AttributeModifier.Operation.ADDITION));
	 
	 attributesDefault.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(),
			 new AttributeModifier(UUID.fromString("10faf191-bf21-4554-b359-cc1f4f1233bf"), "GolemHeart KR", ConfigHandler.GOLEM_HEART_KNOCKBACK_RESISTANCE.getValue().asModifier(false),
					 AttributeModifier.Operation.ADDITION));
	 
	 
	 
	 attributesNoArmor.put(SharedMonsterAttributes.ARMOR.getName(),
             new AttributeModifier(UUID.fromString("14faf191-bf23-4654-b359-cc1f4f1243bf"), "GolemHeart SAB", ConfigHandler.GOLEM_HEART_SUPER_ARMOR.getValue(),
                                   AttributeModifier.Operation.ADDITION));
	 
	 attributesNoArmor.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(),
             new AttributeModifier(UUID.fromString("11faf181-bf23-4354-b359-cc1f5f1253bf"), "GolemHeart STB", ConfigHandler.GOLEM_HEART_SUPER_ARMOR_TOUGHNESS.getValue(),
                                   AttributeModifier.Operation.ADDITION));
	 
	 attributesNoArmor.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(),
			 new AttributeModifier(UUID.fromString("12faf181-bf21-4554-b359-cc1f4f1254bf"), "GolemHeart KR", ConfigHandler.GOLEM_HEART_KNOCKBACK_RESISTANCE.getValue().asModifier(false),
					 AttributeModifier.Operation.ADDITION));
	 
 }
 
 @Override
 public boolean isForMortals() {
 	return ConfigHandler.GOLEM_HEART_ENABLED.getValue();
 }
 
 @OnlyIn(Dist.CLIENT)
 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
	 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
	 
	 if(ControlsScreen.hasShiftDown()) {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart1");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart2");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeartCooldown", ((float)(ConfigHandler.GOLEM_HEART_COOLDOWN.getValue()))/20.0F);
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart3");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart4", (int)ConfigHandler.GOLEM_HEART_DEFAULT_ARMOR.getValue());
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart5");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart6", (int)ConfigHandler.GOLEM_HEART_SUPER_ARMOR.getValue(), (int)ConfigHandler.GOLEM_HEART_SUPER_ARMOR_TOUGHNESS.getValue());
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart7", ConfigHandler.GOLEM_HEART_KNOCKBACK_RESISTANCE.getValue().asPercentage()+"%");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart8", ConfigHandler.GOLEM_HEART_MELEE_RESISTANCE.getValue().asPercentage()+"%");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart9", ConfigHandler.GOLEM_HEART_EXPLOSION_RESISTANCE.getValue().asPercentage()+"%");
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
