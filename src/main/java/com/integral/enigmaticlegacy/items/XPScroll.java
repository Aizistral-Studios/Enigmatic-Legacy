package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ExperienceHelper;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.helpers.LoreHelper;

import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.capability.ICurio;

public class XPScroll extends Item implements ICurio, IPerhaps {
	
 public static Properties integratedProperties = new Item.Properties();
 public static final int xpPortion = 5;
 public static double range = 0D;

 public XPScroll(Properties properties) {
		super(properties);

 }
 
 public static Properties setupIntegratedProperties() {
	 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(1);
	 integratedProperties.rarity(Rarity.UNCOMMON);
	 
	 return integratedProperties;
 
 }
 
 public static void initConfigValues() {
	 range = EnigmaticLegacy.configHandler.XP_SCROLL_COLLECTION_RANGE.get();
 }
 
 @Override
 public boolean isForMortals() {
 	return EnigmaticLegacy.configLoaded ? EnigmaticLegacy.configHandler.XP_SCROLL_ENABLED.get() : false;
 }
 
 @OnlyIn(Dist.CLIENT)
 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
	 
	 TranslationTextComponent cMode;
	 if (!ItemNBTHelper.getBoolean(stack, "IsActive", false))
		 cMode = new TranslationTextComponent("tooltip.enigmaticlegacy.xpTomeDeactivated");
	 else if (ItemNBTHelper.getBoolean(stack, "AbsorptionMode", true))
		 cMode = new TranslationTextComponent("tooltip.enigmaticlegacy.xpTomeAbsorption");
	 else
		 cMode = new TranslationTextComponent("tooltip.enigmaticlegacy.xpTomeExtraction");
	 
	 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
	 
	 if(ControlsScreen.hasShiftDown()) {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome1");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome2");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome3");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome4");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome4_5");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome5");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome6");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome7");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome8");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome9");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome10");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome11", (int)range);
	 } else {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
	 }
	 
	 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
	 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTomeMode", cMode.getFormattedText());
	 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
	 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTomeStoredXP");
	 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTomeUnits", ItemNBTHelper.getInt(stack, "XPStored", 0), ExperienceHelper.getLevelForExperience(ItemNBTHelper.getInt(stack, "XPStored", 0)));
 
	 try {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.currentKeybind", KeyBinding.getDisplayString("key.xpScroll").get().toUpperCase());
	 } catch (NullPointerException ex) {
			// Just don't do it lol 
	 }
 }
 
 
 @Override
 public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand handIn) {
	 
	 ItemStack stack = player.getHeldItem(handIn);
	 this.trigger(world, stack, player, handIn, true);
	 	
     return new ActionResult<>(ActionResultType.SUCCESS, stack);
        
 }
 
 
  public void trigger(World world, ItemStack stack, PlayerEntity player, Hand hand, boolean swing) {
	  
	  if (!player.isSneaking()) {
	 		
	 		if (ItemNBTHelper.getBoolean(stack, "AbsorptionMode", true)) {
	 		ItemNBTHelper.setBoolean(stack, "AbsorptionMode", false);
	 		world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.NEUTRAL, 1.0F, (float) (0.8F + (Math.random() * 0.2F)));
	 		}
	 		else { 
	 		ItemNBTHelper.setBoolean(stack, "AbsorptionMode", true);
	 		world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.NEUTRAL, 1.0F, (float) (0.8F + (Math.random() * 0.2F)));
	 		}
	 	} else {
	 		
	 		if (ItemNBTHelper.getBoolean(stack, "IsActive", false)) {
	 			ItemNBTHelper.setBoolean(stack, "IsActive", false);
	 			world.playSound(null, player.getPosition(), EnigmaticLegacy.HHOFF, SoundCategory.NEUTRAL, (float) (0.8F + (Math.random() * 0.2F)), (float) (0.8F + (Math.random() * 0.2F)));
	 		}
	 		else {
	 			ItemNBTHelper.setBoolean(stack, "IsActive", true);
	 			world.playSound(null, player.getPosition(), EnigmaticLegacy.HHON, SoundCategory.NEUTRAL, (float) (0.8F + (Math.random() * 0.2F)), (float) (0.8F + (Math.random() * 0.2F)));
	 		}
	 	}
	 	
	  	if (swing)
	 	player.swingArm(hand);

  }
 
  @Override
  @OnlyIn(Dist.CLIENT)
  public boolean hasEffect(ItemStack stack) {
	 return ItemNBTHelper.getBoolean(stack, "IsActive", false);
  }
 
 
  
  @Override
  public void onCurioTick(String identifier, LivingEntity entity) {
	  
	  ItemStack itemstack = SuperpositionHandler.getCurioStack(entity, EnigmaticLegacy.xpScroll);
	  
	  if (!(entity instanceof PlayerEntity) || entity.world.isRemote || !ItemNBTHelper.getBoolean(itemstack, "IsActive", false))
			 return;
		 
		 PlayerEntity player = (PlayerEntity) entity;
		 World world = player.world;
		 
		 if (ItemNBTHelper.getBoolean(itemstack, "AbsorptionMode", true)) {
			 
			 if (player.experienceTotal >= xpPortion) {
			 player.giveExperiencePoints(-xpPortion);
			 ItemNBTHelper.setInt(itemstack, "XPStored", ItemNBTHelper.getInt(itemstack, "XPStored", 0) + xpPortion);
			 }
			 else if (player.experienceTotal > 0 & ExperienceHelper.getPlayerXP(player) < xpPortion) {
			 int exp = player.experienceTotal;
			 player.giveExperiencePoints(-exp);
			 ItemNBTHelper.setInt(itemstack, "XPStored", ItemNBTHelper.getInt(itemstack, "XPStored", 0) + exp);
			 }
			
		 
		 } else {
			 
			 int xp = ItemNBTHelper.getInt(itemstack, "XPStored", 0);
			 
				if (xp >= xpPortion) {
					ItemNBTHelper.setInt(itemstack, "XPStored", xp-xpPortion);
					player.giveExperiencePoints(xpPortion);
				} else if (xp > 0 & xp < xpPortion) {
					ItemNBTHelper.setInt(itemstack, "XPStored", 0);
					player.giveExperiencePoints(xp);
				}
			 
		 }
		 
		 List<ExperienceOrbEntity> orbs = world.getEntitiesWithinAABB(ExperienceOrbEntity.class, SuperpositionHandler.getBoundingBoxAroundEntity(player, range));
		 for (ExperienceOrbEntity processed : orbs) {
			 player.xpCooldown = 0;
			 processed.setPositionAndUpdate(player.posX, player.posY, player.posZ);
		 }
  }
 
  @Override
  public boolean canRightClickEquip() {
      return false;
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
