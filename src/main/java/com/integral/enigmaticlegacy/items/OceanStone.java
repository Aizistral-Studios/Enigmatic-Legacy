package com.integral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.helpers.LoreHelper;
import com.integral.enigmaticlegacy.helpers.Perhaps;

import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.capability.ICurio;

public class OceanStone extends Item implements ICurio, IPerhaps {
	
 public static Properties integratedProperties = new Item.Properties();
 public static List<String> immunityList = new ArrayList<String>();
 
 public static Perhaps underwaterCreaturesResistance = new Perhaps(0);
 public static Perhaps swimmingBoost = new Perhaps(0);
 public static int abilityCooldown = 0;
 public static int xpCostBase = 150;
 public static double xpCostModifier = 0D;

 public OceanStone(Properties properties) {
		super(properties);
 }
 
 public static Properties setupIntegratedProperties() {
	 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(1);
	 integratedProperties.rarity(Rarity.RARE);
	 
	 immunityList.add(DamageSource.DROWN.damageType);
	 
	 return integratedProperties;
 
 }
 
 public static void initConfigValues() {
	 abilityCooldown = EnigmaticLegacy.configHandler.OCEAN_STONE_COOLDOWN.get();
	 underwaterCreaturesResistance = new Perhaps(EnigmaticLegacy.configHandler.OCEAN_STONE_UNDERWATER_CREATURES_RESISTANCE.get());
	 swimmingBoost = new Perhaps(EnigmaticLegacy.configHandler.OCEAN_STONE_SWIMMING_SPEED_BOOST.get());
	 xpCostModifier = EnigmaticLegacy.configHandler.OCEAN_STONE_XP_COST_MODIFIER.get();
 }
 
 @Override
 public boolean isForMortals() {
 	return EnigmaticLegacy.configLoaded ? EnigmaticLegacy.configHandler.OCEAN_STONE_ENABLED.get() : false;
 }
 
 @OnlyIn(Dist.CLIENT)
 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
	 
	 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
	 
	 if(ControlsScreen.hasShiftDown()) {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone1");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone2");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone3");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStoneCooldown", ((float)(abilityCooldown))/20.0F);
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone4");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone5", underwaterCreaturesResistance.asPercentage()+"%");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone6");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone7");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone8");
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
 
 public void triggerActiveAbility(World world, PlayerEntity player, ItemStack stack) {
	 if (SuperpositionHandler.hasSpellstoneCooldown(player))
		 return;
	 
	 if (player.world.getDimension().getType().getId() != 1 & player.world.getDimension().getType().getId() != -1)
	 if (!world.getWorldInfo().isThundering()) {
		 boolean paybackReceived = false;
		 ItemStack scroll = SuperpositionHandler.getCurioStack(player, EnigmaticLegacy.xpScroll);
		 
		 if (scroll != null && ItemNBTHelper.getInt(scroll, "XPStored", 0) >= xpCostBase*2) {
			 ItemNBTHelper.setInt(scroll, "XPStored", ItemNBTHelper.getInt(scroll, "XPStored", 0) - (int) ((xpCostBase+(Math.random()*xpCostBase))*xpCostModifier));
			 paybackReceived = true;
		 } else if (player.experienceTotal >= xpCostBase*2) {
			 player.giveExperiencePoints((int) -((xpCostBase+(Math.random()*xpCostBase))*xpCostModifier));
			 paybackReceived = true;
		 }
		 
		 if (paybackReceived) {
			 
			 int thunderstormTime = (int) (10000+(Math.random()*20000));
		 
			 world.getWorldInfo().setClearWeatherTime(0);
			 world.getWorldInfo().setRainTime(thunderstormTime);
			 world.getWorldInfo().setThunderTime(thunderstormTime);
			 world.getWorldInfo().setRaining(true);
			 world.getWorldInfo().setThundering(true);
		 
			 world.playSound(null, player.getPosition(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.NEUTRAL, 2.0F, (float) (0.7F + (Math.random()*0.3D)));
		 
			 SuperpositionHandler.setSpellstoneCooldown(player, abilityCooldown);
		 }
		 
	 	 }
	 }
 
 
  @Override
  public boolean canRightClickEquip() {
      return true;
  }
  
  @Override
  public void onEquipped(String identifier, LivingEntity entityLivingBase) {
	  //Insert existential void here
  }
  
  @Override
  public void onUnequipped(String identifier, LivingEntity entityLivingBase) {
	  // Insert existential void here
  }
  
  @Override
  public void onCurioTick(String identifier, LivingEntity living) {
	  
	  if (living instanceof PlayerEntity & !living.world.isRemote)
			 if (SuperpositionHandler.hasCurio(living, EnigmaticLegacy.oceanStone)) {
				 PlayerEntity player = (PlayerEntity) living;
				 //ItemStack stack = SuperpositionHandler.getCurioStack(player, EnigmaticLegacy.oceanStone);
				 
				 if (player.areEyesInFluid(FluidTags.WATER, true)) {
					 player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 4, 0, true, false));
					 player.setAir(300);
				 }
			 }
	  
  }
  
  @Override
  public Multimap<String, AttributeModifier> getAttributeModifiers(String identifier) {

    Multimap<String, AttributeModifier> atts = HashMultimap.create();

      atts.put(PlayerEntity.SWIM_SPEED.getName(),
               new AttributeModifier(UUID.fromString("13faf191-bf38-4654-b369-cc1f4f1143bf"), "Swim speed bonus", swimmingBoost.asMultiplier(false),
                                     AttributeModifier.Operation.MULTIPLY_BASE));
    
    return atts;
  }
  
}
