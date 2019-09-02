package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.helpers.LoreHelper;

import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.capability.ICurio;

public class EnigmaticAmulet extends Item implements ICurio {
	
 public static Properties integratedProperties = new Item.Properties();

 public EnigmaticAmulet(Properties properties) {
		super(properties);
 }
 
 public static Properties setupIntegratedProperties() {
	 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(1);
	 integratedProperties.rarity(Rarity.UNCOMMON);
	 
	 return integratedProperties;
 
 }
 
 public static void initConfigValues() {}
 
 @OnlyIn(Dist.CLIENT)
 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
	 String name = ItemNBTHelper.getString(stack, "Inscription", null);
	 
	 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
	 
	 if(ControlsScreen.hasShiftDown()) {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet1");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet2");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet3");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet4");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet5");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmulet6");
	 } else {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
	 }
	 
	 if (name != null) {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmuletInscription", name);
	 }
 }
 
  @Override
  public boolean canRightClickEquip() {
    return true;
  }
  
  /*
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
	  
	  ItemStack itemstack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);
		
		EnigmaticLegacy.enigmaticLogger.info("Item used: " + CuriosAPI.getCurioTags(itemstack.getItem()));
		
		LazyOptional<ICurioItemHandler> curioHandler = CuriosAPI.getCuriosHandler(playerIn);
		curioHandler.ifPresent(handler -> {
			//handler.disableCurio("spellstone");
			//System.out.println(handler.getDisabled());
			//System.out.println(handler.getWearer());
		});
		
		if (worldIn.isRemote) {
			IToast toast = new SlotUnlockedToast(new ItemStack(EnigmaticLegacy.thiccScroll), "scroll");
			Minecraft.getInstance().getToastGui().add(toast);
			
		}
		
		//CuriosAPI.enableTypeForEntity("ring", playerIn);
		
		System.out.println("Value: " + EnigmaticLegacy.configHandler.SUPER_MAGNET_RING_SOUND.get());
		//System.out.println("Value Original: " + EnigmaticLegacy.configHandler.MAGNET_RING_RANGE.get());
		
	  return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
	  
  }
  */
  
  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
	 /* if (entityIn instanceof PlayerEntity & !worldIn.isRemote)
	  if (!ItemNBTHelper.verifyExistance(stack, "Inscription")) {
		  ItemNBTHelper.setString(stack, "Inscription", entityIn.getDisplayName().getString());
	  }*/
  }
  
  @Override
  public void onEquipped(String identifier, LivingEntity entityLivingBase) {
	  
  }
  
  @Override
  public void onUnequipped(String identifier, LivingEntity entityLivingBase) {
	   
  }
  
  @Override
  public void onCurioTick(String identifier, LivingEntity entityLivingBase) {
	 
  }
  
  @Override
  public Multimap<String, AttributeModifier> getAttributeModifiers(String identifier) {

    Multimap<String, AttributeModifier> atts = HashMultimap.create();

      atts.put(SharedMonsterAttributes.ARMOR.getName(),
               new AttributeModifier(UUID.fromString("50faf191-bf78-4654-b349-cc1f4f1143bf"), "Armor bonus", 2.0,
                                     AttributeModifier.Operation.ADDITION));
      atts.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
              new AttributeModifier(UUID.fromString("cb7f55d3-685c-4f38-a497-9c13a33db5cf"), "Attack bonus", 1.5,
                                    AttributeModifier.Operation.ADDITION));
    
    return atts;
  }
  
}
