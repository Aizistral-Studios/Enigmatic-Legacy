package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;

import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.settings.KeyBinding;
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

public class EnderRing extends Item implements ICurio, IPerhaps {
	
 public static Properties integratedProperties = new Item.Properties();

 public EnderRing(Properties properties) {
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
 	return EnigmaticLegacy.configLoaded ? EnigmaticLegacy.configHandler.ENDER_RING_ENABLED.get() : false;
 }
 
 @OnlyIn(Dist.CLIENT)
 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

	 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
	 
	 if(ControlsScreen.hasShiftDown()) {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enderRing1");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enderRing2");
	 } else {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
	 }
	 	
	 try {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.currentKeybind", KeyBinding.getDisplayString("key.enderRing").get().toUpperCase());
	 } catch (NullPointerException ex) {
		// Just don't do it lol 
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
  /*
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handIn) {
	  
	  ItemStack itemstack = player.getHeldItem(handIn);
		player.setActiveHand(handIn);
		
		if (!worldIn.isRemote & player instanceof ServerPlayerEntity) {
		 ServerPlayerEntity playerServ = (ServerPlayerEntity) player;
		
		 ChestContainer container = ChestContainer.createGeneric9X3(8316, playerServ.inventory, playerServ.getInventoryEnderChest());
		
		 playerServ.currentWindowId = container.windowId;
		 playerServ.connection.sendPacket(new SOpenWindowPacket(container.windowId, container.getType(), new TranslationTextComponent("container.enderchest")));
         container.addListener(playerServ);
         playerServ.openContainer = container;
         net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open(playerServ, playerServ.openContainer));
		
		}
			
		EnigmaticLegacy.enigmaticLogger.info("Item used: " + CuriosAPI.getCurioTags(itemstack.getItem()));
		
	  return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
	  
  }
  */
  @Override
  public void onEquipped(String identifier, LivingEntity entityLivingBase) {
	 // Insert existential void here
  }
  
  @Override
  public void onUnequipped(String identifier, LivingEntity entityLivingBase) {
	 // Insert existential void here
  }
  
}

