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
 
 public static void initConfigValues() {
	 // Insert existential void here
 }
 
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
 
}
