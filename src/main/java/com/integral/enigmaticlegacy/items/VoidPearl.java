package com.integral.enigmaticlegacy.items;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;

import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import top.theillusivec4.curios.api.capability.ICurio;

public class VoidPearl extends Item implements ICurio, IPerhaps {
	
 public static Properties integratedProperties = new Item.Properties();
 public static List<String> immunityList = new ArrayList<String>();
 public static List<String> healList = new ArrayList<String>();
 public static HashMap<String, Supplier<Float>> resistanceList = new HashMap<String, Supplier<Float>>();
 public static Field foodSaturationField;
 public static DamageSource theDarkness;
 
 public VoidPearl(Properties properties) {
		super(properties);
		
		 immunityList.add(DamageSource.DROWN.damageType);
		 immunityList.add(DamageSource.IN_WALL.damageType);
		 
		 healList.add(DamageSource.WITHER.damageType);
		 healList.add(DamageSource.MAGIC.damageType);
		 
		 theDarkness = new DamageSource("darkness");
		 theDarkness.setDamageIsAbsolute();
		 theDarkness.setDamageBypassesArmor();
		 theDarkness.setMagicDamage();
		 
		 foodSaturationField = ObfuscationReflectionHelper.findField(FoodStats.class, "field_75125_b");
 }
 
 public static Properties setupIntegratedProperties() {
	 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(1);
	 integratedProperties.rarity(Rarity.EPIC);
	 
	 return integratedProperties;
 
 }
 
 @Override
 public boolean isForMortals() {
 	return ConfigHandler.VOID_PEARL_ENABLED.getValue();
 }
 
 @Override
 public boolean canEquip(String identifier, LivingEntity living) {
	  if (SuperpositionHandler.hasCurio(living, EnigmaticLegacy.voidPearl))
		  return false;
	  else
		  return true;
 }
 
 @OnlyIn(Dist.CLIENT)
 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
	 
	 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
	 
	 if(ControlsScreen.hasShiftDown()) {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl1");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl2");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearlCooldown", ((float)(ConfigHandler.VOID_PEARL_COOLDOWN.getValue()))/20.0F);
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl3");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl4");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl5");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl6");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl7");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl8");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl9");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl10");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl11");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl12", ConfigHandler.VOID_PEARL_UNDEAD_PROBABILITY.getValue().asPercentage()+"%");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl13");
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
	  // Insert existential void here
  }
 
  @Override
  public boolean canRightClickEquip() {
      return true;
  }
  
  @Override
  public void onEquipped(String identifier, LivingEntity living) {
	  // Insert existential void here
  }
  
  @Override
  public void onUnequipped(String identifier, LivingEntity living) {
	  // Insert existential void here
  }
  
  @Override
  public void onCurioTick(String identifier, LivingEntity living) {
	  
	  if(living instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)living;
			
			FoodStats stats = player.getFoodStats();
			stats.setFoodLevel(20);
				
			if (foodSaturationField != null) {
				try {
					foodSaturationField.setFloat(stats, 0F);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		
			if (player.getAir() < 300)
			player.setAir(300);
			
			if (player.ticksExisted % 10 == 0) {
				List<LivingEntity> entities = living.world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(player.posX - ConfigHandler.VOID_PEARL_SHADOW_RANGE.getValue(), player.posY - ConfigHandler.VOID_PEARL_SHADOW_RANGE.getValue(), player.posZ - ConfigHandler.VOID_PEARL_SHADOW_RANGE.getValue(), player.posX + ConfigHandler.VOID_PEARL_SHADOW_RANGE.getValue(), player.posY + ConfigHandler.VOID_PEARL_SHADOW_RANGE.getValue(), player.posZ + ConfigHandler.VOID_PEARL_SHADOW_RANGE.getValue()));
				
				if (entities.contains(player))
					entities.remove(player);
				
				for (LivingEntity victim : entities) {
					if (victim.world.getNeighborAwareLightSubtracted(victim.getPosition(), 0) < 3) {
						
						if (victim instanceof PlayerEntity) {
							PlayerEntity playerVictim = (PlayerEntity) victim;
							if (SuperpositionHandler.hasCurio(playerVictim, EnigmaticLegacy.voidPearl)) {
								playerVictim.addPotionEffect(new EffectInstance(Effects.WITHER, 80, 1, false, true));
								continue;
							}
						}
						
						//if (player.ticksExisted % 20 == 0) {
							victim.attackEntityFrom(theDarkness, (float) ConfigHandler.VOID_PEARL_BASE_DARKNESS_DAMAGE.getValue());
							living.world.playSound(null, victim.getPosition(), SoundEvents.ENTITY_PHANTOM_BITE, SoundCategory.PLAYERS, 1.0F, (float) (0.3F + (Math.random()*0.4D)));
						//}
						
						victim.addPotionEffect(new EffectInstance(Effects.WITHER, 80, 1, false, true));
						victim.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 100, 2, false, true));
						victim.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 100, 0, false, true));
						victim.addPotionEffect(new EffectInstance(Effects.HUNGER, 160, 2, false, true));
						victim.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 100, 3, false, true));
					}
				}
			}
	  }
	  
  }
  
}
