package com.integral.enigmaticlegacy.items;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.helpers.LoreHelper;

import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ExtradimensionalEye extends Item implements IPerhaps {
	
 public static Properties integratedProperties = new Item.Properties();
 public static HashMap <PlayerEntity, LivingEntity> boundTargets = new HashMap<PlayerEntity, LivingEntity>();
 public static HashMap <PlayerEntity, LivingEntity> boundTargetsClient = new HashMap<PlayerEntity, LivingEntity>();

 public float range = 3.0F;
 
 public ExtradimensionalEye(Properties properties) {
		super(properties);
 }

 public static Properties setupIntegratedProperties() {
	 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(1);
	 integratedProperties.rarity(Rarity.UNCOMMON);
	 
	 return integratedProperties;
 
 }
 
 public static void initConfigValues() {}
 
 @Override
 public boolean isForMortals() {
 	return EnigmaticLegacy.configLoaded ? EnigmaticLegacy.configHandler.EXTRADIMENSIONAL_EYE_ENABLED.get() : false;
 }
 
 @OnlyIn(Dist.CLIENT)
 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
	 if(ControlsScreen.hasShiftDown()) {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEye1");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEye2");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEye3");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEye4");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEye5");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEye6");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEye7");
	 } else {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
	 }
	 if (ItemNBTHelper.verifyExistance(stack, "BoundDimension")) {
		 
		 String boundDimensionName = null;
		 int dimensionID = ItemNBTHelper.getInt(stack, "BoundDimension", 0);
		 
		 if (dimensionID == 0)
			 boundDimensionName = "tooltip.enigmaticlegacy.overworld";
		 else if (dimensionID == -1)
			 boundDimensionName = "tooltip.enigmaticlegacy.nether";
		 else if (dimensionID == 1)
			 boundDimensionName = "tooltip.enigmaticlegacy.end";
		 
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEyeLocation");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEyeX", ItemNBTHelper.getInt(stack, "BoundX", 0));
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEyeY", ItemNBTHelper.getInt(stack, "BoundY", 0));
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEyeZ", ItemNBTHelper.getInt(stack, "BoundZ", 0));
		 if (boundDimensionName != null)
			 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEyeDimension", new TranslationTextComponent(boundDimensionName).getFormattedText());
		 else
			 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEyeDimension", ItemNBTHelper.getInt(stack, "BoundDimension", 0));
	 }
 }

 public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
	ItemStack itemstack = playerIn.getHeldItem(handIn);
	
	if (playerIn.isSneaking() & !ItemNBTHelper.verifyExistance(itemstack, "BoundDimension")) {
    	ItemNBTHelper.setDouble(itemstack, "BoundX", playerIn.posX);
    	ItemNBTHelper.setDouble(itemstack, "BoundY", playerIn.posY);
    	ItemNBTHelper.setDouble(itemstack, "BoundZ", playerIn.posZ);
    	
    	ItemNBTHelper.setDouble(itemstack, "BoundDimension", playerIn.dimension.getId());
    	playerIn.swingArm(handIn);
    	return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }
	
	return new ActionResult<>(ActionResultType.FAIL, itemstack);
 }
 
 
  /*


 public UseAction getUseAction(ItemStack stack) {
    return UseAction.BOW;
 }



 public int getUseDuration(ItemStack stack) {
    return 72;
 }

 public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    ItemStack itemstack = playerIn.getHeldItem(handIn);
    
    if (playerIn.isSneaking() & !ItemNBTHelper.verifyExistance(itemstack, "BoundDimension")) {
    	ItemNBTHelper.setDouble(itemstack, "BoundX", playerIn.posX);
    	ItemNBTHelper.setDouble(itemstack, "BoundY", playerIn.posY);
    	ItemNBTHelper.setDouble(itemstack, "BoundZ", playerIn.posZ);
    	
    	ItemNBTHelper.setDouble(itemstack, "BoundDimension", playerIn.dimension.getId());
    	playerIn.swingArm(handIn);
    	return new ActionResult<>(ActionResultType.PASS, itemstack);
    } else if (ItemNBTHelper.verifyExistance(itemstack, "BoundX") & ItemNBTHelper.verifyExistance(itemstack, "BoundY") & ItemNBTHelper.verifyExistance(itemstack, "BoundZ") & ItemNBTHelper.verifyExistance(itemstack, "BoundDimension") & ItemNBTHelper.getInt(itemstack, "BoundDimension", 0) == playerIn.dimension.getId()) {
    	
    	if (!worldIn.isRemote & !boundTargets.containsKey(playerIn) & SuperpositionHandler.searchForTarget(playerIn, worldIn, range, 16) == null)
    		return new ActionResult<>(ActionResultType.FAIL, itemstack);
    	if (worldIn.isRemote & !boundTargetsClient.containsKey(playerIn) & SuperpositionHandler.searchForTarget(playerIn, worldIn, range, 16) == null)
    		return new ActionResult<>(ActionResultType.FAIL, itemstack);
    	
    	playerIn.setActiveHand(handIn);
    	return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    } else
    	return new ActionResult<>(ActionResultType.FAIL, itemstack);
 }
 
 public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
	 if (!(entityLiving instanceof PlayerEntity))
		  return;
	  
	 PlayerEntity player = (PlayerEntity) entityLiving;
	 
	 if (boundTargets.containsKey(player))
	 boundTargets.remove(player);
 }
 
 
 private void useTickClient(ItemStack stack, World worldIn, PlayerEntity player, int itemSlot, boolean isSelected) {
	  if (!isSelected || player.getActiveItemStack() != stack)
		if (boundTargetsClient.containsKey(player))
			boundTargetsClient.remove(player);
	  
	  if (isSelected)
		  if (player.getActiveItemStack() == stack) {
			  LivingEntity target;
			  if (boundTargetsClient.get(player) != null) {
				  target = boundTargetsClient.get(player);
				  if (target.getDistance(player) > 8) {
					  target = SuperpositionHandler.searchForTarget(player, worldIn, range, 16);
					  boundTargetsClient.put(player, target);
				  }
					  
			  } else {
				  target = SuperpositionHandler.searchForTarget(player, worldIn, range, 16);
				  boundTargetsClient.put(player, target);
			  }
			  
			  if (target != null) {
				  for (int counter = 0; counter <= 4; counter++)
				  worldIn.addParticle(ParticleTypes.PORTAL, target.posX, Vector3.fromEntityCenter(target).y, target.posZ, ((Math.random()-0.5)*3.0), ((Math.random()-0.5)*3.0), ((Math.random()-0.5)*3.0));
				  if (player.getItemInUseCount() == 1)
					  boundTargetsClient.remove(player);
			  }
		  }
 }

  
  
  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
	  if (!(entityIn instanceof PlayerEntity))
		  return;
	  
	  PlayerEntity player = (PlayerEntity) entityIn;
	  
	  if (worldIn.isRemote) {
		  this.useTickClient(stack, worldIn, player, itemSlot, isSelected);
		  return;
	  }

	  if (!isSelected || player.getActiveItemStack() != stack)
		if (boundTargets.containsKey(player))
			boundTargets.remove(player);
	  
	  if (isSelected)
		  if (player.getActiveItemStack() == stack) {
			  LivingEntity target;
			  if (boundTargets.get(player) != null) {
				  target = boundTargets.get(player);
				  if (target.getDistance(player) > 8) {
					  target = SuperpositionHandler.searchForTarget(player, worldIn, range, 16);
					  boundTargets.put(player, target);
				  }
					  
			  } else {
				  target = SuperpositionHandler.searchForTarget(player, worldIn, range, 16);
				  boundTargets.put(player, target);
			  }
			  
			  if (target != null) {
				  for (int counter = 0; counter <= 4; counter++)
				  worldIn.addParticle(ParticleTypes.PORTAL, target.posX, Vector3.fromEntityCenter(target).y, target.posZ, ((Math.random()-0.5)*3.0), ((Math.random()-0.5)*3.0), ((Math.random()-0.5)*3.0));
				  target.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 20, 3, false, true));
				  if (player.getItemInUseCount() == 1) {
					  worldIn.playSound(null, target.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 1.0F, (float) (0.8F + (Math.random()*0.2)));
					  target.setPositionAndUpdate(ItemNBTHelper.getDouble(stack, "BoundX", 0D), ItemNBTHelper.getDouble(stack, "BoundY", 0D), ItemNBTHelper.getDouble(stack, "BoundZ", 0D));
					  worldIn.playSound(null, target.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 1.0F, (float) (0.8F + (Math.random()*0.2)));
					  boundTargets.remove(player);
					  if (!player.abilities.isCreativeMode)
					  stack.shrink(1);
				  }
			  } else
				  player.stopActiveHand();
		  }
  }
  */
  
}
