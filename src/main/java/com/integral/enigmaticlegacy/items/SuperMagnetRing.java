package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;
import com.integral.enigmaticlegacy.packets.PacketPortalParticles;

import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;
import top.theillusivec4.curios.api.capability.ICurio;

public class SuperMagnetRing extends Item implements ICurio, IPerhaps {
	
 public static Properties integratedProperties = new Item.Properties();
 public static int range = 0;
 public static boolean sound;

 public SuperMagnetRing(Properties properties) {
		super(properties);
 }
 
 public static Properties setupIntegratedProperties() {
	 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(1);
	 integratedProperties.rarity(Rarity.EPIC);
	 
	 return integratedProperties;
 
 }
 
 public static void initConfigValues() {
	 range = EnigmaticLegacy.configHandler.SUPER_MAGNET_RING_RANGE.get();
	 sound = EnigmaticLegacy.configHandler.SUPER_MAGNET_RING_SOUND.get();
 }
 
 @Override
 public boolean isForMortals() {
 	return EnigmaticLegacy.configLoaded ? EnigmaticLegacy.configHandler.SUPER_MAGNET_RING_ENABLED.get() : false;
 }
 
 @OnlyIn(Dist.CLIENT)
 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
	 
	 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
	 
	 if(ControlsScreen.hasShiftDown()) {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.superMagnetRing1");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.superMagnetRing2", range);
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.superMagnetRing3");
	 } else {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
	 }
 }
 
  @Override
  public boolean canRightClickEquip() {

    return true;
  }
  
  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

  }
  
  @Override
  public void onEquipped(String identifier, LivingEntity entityLivingBase) {
	  
  }
  
  @Override
  public void onUnequipped(String identifier, LivingEntity entityLivingBase) {
	   
  }
  
  @Override
  public void onCurioTick(String identifier, LivingEntity living) {
	  
	  	if (living.isSneaking() || living.world.isRemote)
	  		return;
	  		
	    double x = living.posX;
		double y = living.posY + 0.75;
		double z = living.posZ;

		List<ItemEntity> items = living.world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range));
		int pulled = 0;
		for(ItemEntity item : items)
			if(canPullItem(item)) {
				if(pulled > 512)
					break;
				
				EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(item.posX, item.posY, item.posZ, 24, item.dimension)), new PacketPortalParticles(item.posX, item.posY+(item.getHeight()/2), item.posZ, 24, 0.75D));
				
				if (sound)
					item.world.playSound(null, item.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 1.0F, (float) (0.8F + (Math.random()*0.2D)));
				
				item.setPositionAndUpdate(x, y, z);
				item.setNoPickupDelay();
				pulled++;
			}
	 
  }
  
    private boolean canPullItem(ItemEntity item) {
		ItemStack stack = item.getItem();
		if(stack.isEmpty())
			return false;

		return true;
	}
  
}
