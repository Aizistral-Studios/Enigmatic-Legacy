package com.integral.enigmaticlegacy.items;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.entities.PermanentItemEntity;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.helpers.LoreHelper;

import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.capability.ICurio;

public class OblivionStone extends Item implements IPerhaps {
	
 public static Properties integratedProperties = new Item.Properties();

 public OblivionStone(Properties properties) {
		super(properties);
 }
 
 public static Properties setupIntegratedProperties() {
	 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(1);
	 integratedProperties.rarity(Rarity.EPIC);
	 
	 return integratedProperties;
 }
 
 @Override
 public boolean isForMortals() {
 	return ConfigHandler.OBLIVION_STONE_ENABLED.getValue();
 }
 
 /*
 @Override
 public boolean canEquip(String identifier, LivingEntity living) {
	  if (SuperpositionHandler.hasCurio(living, EnigmaticLegacy.oblivionStone))
		  return false;
	  else
		  return true;
 }
 
 @Override
 public boolean canRightClickEquip() {
    return false;
 }
 
 @Override
 public void onCurioTick(String identifier, LivingEntity living) {
	 // Insert existential void here
 }

 @Override
 public void onEquipped(String identifier, LivingEntity entityLivingBase) {
	// Insert existential void here
 }
  
 @Override
 public void onUnequipped(String identifier, LivingEntity entityLivingBase) {
	// Insert existential void here
 }
 */
 
 @OnlyIn(Dist.CLIENT)
 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
	 
	 //LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
	 
	 if(ControlsScreen.hasShiftDown()) {
		 
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone1");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone2");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone2_more");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone3");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone4");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone5");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone6");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone7");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone8");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone9");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone10");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone11");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone12");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone13");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone14");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone15");
		 
	 } else if (ControlsScreen.hasControlDown()) {
		 
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStoneCtrlList");
			if (stack.hasTag()) {
				CompoundNBT nbt = stack.getTag();
				ListNBT arr = nbt.getList("SupersolidID", 8);
				int counter = 0;
				
				if (arr.size() <= ConfigHandler.OBLIVION_STONE_SOFTCAP.getValue()) {
				for (INBT s_uncast : arr) {
					String s = ((StringNBT)s_uncast).getString();
					Item something = ForgeRegistries.ITEMS.getValue(new ResourceLocation(s));
					if (something != null) {
						ItemStack displayStack;
						displayStack = new ItemStack(something, 1);
						
						list.add(new StringTextComponent(" - " + displayStack.getDisplayName().getUnformattedComponentText()).applyTextStyles(TextFormatting.GOLD));
					}
					counter++;
				}
				} else {
					for (int s = 0; s < ConfigHandler.OBLIVION_STONE_SOFTCAP.getValue(); s++) {
						int randomID = random.nextInt(arr.size());
						Item something = ForgeRegistries.ITEMS.getValue(new ResourceLocation(((StringNBT)arr.get(randomID)).getString()));
						
						if (something != null) {
							ItemStack displayStack;
							displayStack = new ItemStack(something, 1);
							
							list.add(new StringTextComponent(" - " + displayStack.getDisplayName().getUnformattedComponentText()).applyTextStyles(TextFormatting.GOLD));
						}
					}
				}
			}
		
		 
	 } else {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStoneHoldCtrl");
	 }
	 
	 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
	 
	 TranslationTextComponent mode;
	 
	 if (ItemNBTHelper.getBoolean(stack, "IsActive", true)) {
		 mode = new TranslationTextComponent("tooltip.enigmaticlegacy.oblivionStoneMode" + ItemNBTHelper.getInt(stack, "ConsumptionMode", 0));
	 } else
		 mode = new TranslationTextComponent("tooltip.enigmaticlegacy.oblivionStoneModeInactive");
	 
	 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStoneModeDesc", mode);
 }
 
 @Override
 public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
	 
	 ItemStack stack = player.getHeldItem(hand);
	 
	 int mode = ItemNBTHelper.getInt(stack, "ConsumptionMode", 0);

		if (player.isSneaking()) {
			
			world.playSound(null, player.getPosition(), ItemNBTHelper.getBoolean(stack, "IsActive", true) ? EnigmaticLegacy.HHOFF : EnigmaticLegacy.HHON, SoundCategory.PLAYERS, (float) (0.8F + (Math.random() * 0.2F)), (float) (0.8F + (Math.random() * 0.2F)));
			ItemNBTHelper.setBoolean(stack, "IsActive", !ItemNBTHelper.getBoolean(stack, "IsActive", true));

		} else {

			if (mode >= 0 && mode < 2)
				ItemNBTHelper.setInt(stack, "ConsumptionMode", mode+1);
			else
				ItemNBTHelper.setInt(stack, "ConsumptionMode", 0);

			world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2F)));

	  }
	 
	 
	 player.swingArm(hand);

     return new ActionResult<>(ActionResultType.SUCCESS, stack);
        
 }
 
 @Override
 public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
	 if (!(entity instanceof PlayerEntity) || entity.ticksExisted % 4 != 0)
			return;

	    PlayerEntity player = (PlayerEntity) entity;
	    
		if (!ItemNBTHelper.getBoolean(stack, "IsActive", true) || stack.getOrCreateTag().getList("SupersolidID", 8).size() < 1)
			return;

		CompoundNBT nbt = stack.getOrCreateTag();
		ListNBT arr = nbt.getList("SupersolidID", 8);
		
		consumeStuff(player, arr, ItemNBTHelper.getInt(stack, "ConsumptionMode", 0));
 }
 
 
 
 	public static void consumeStuff(PlayerEntity player, ListNBT list, int mode) {
		HashMap<Integer, ItemStack> stackMap = new HashMap<Integer, ItemStack>();
		int cycleCounter = 0;
		int filledStacks = 0;
		
		for (int slot = 0; slot < player.inventory.mainInventory.size(); slot++) {
			if (!player.inventory.mainInventory.get(slot).isEmpty()) {
				filledStacks += 1;
				if (player.inventory.mainInventory.get(slot).getItem() != EnigmaticLegacy.oblivionStone)
					stackMap.put(slot, player.inventory.mainInventory.get(slot));
			}
		}
		
		if (stackMap.size() == 0)
			return;

		if (mode == 0) {
			
			for (INBT sID : list) {
				String str = ((StringNBT)sID).getString();
				
				for (int slot : stackMap.keySet()) {

					if (stackMap.get(slot).getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation(str))) 
						player.inventory.setInventorySlotContents(slot, ItemStack.EMPTY);
					
					
				}
				cycleCounter++;
			}
		} else if (mode == 1) {
			
			for (INBT sID : list) {
				String str = ((StringNBT)sID).getString();
				
				HashMap<Integer, ItemStack> localStackMap = new HashMap<Integer, ItemStack>(stackMap);
				Multimap<Integer, Integer> stackSizeMultimap = ArrayListMultimap.create();
				
				for (int slot : stackMap.keySet()) {

						if (stackMap.get(slot).getItem() != ForgeRegistries.ITEMS.getValue(new ResourceLocation(str)))
							localStackMap.remove(slot);
					
				}
				
				for (int slot : localStackMap.keySet()) {
					stackSizeMultimap.put(localStackMap.get(slot).getCount(), slot);
				}
				
				while (localStackMap.size() > (player.inventory.offHandInventory.get(0).getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation(str)) ? 0 : 1)) {
					int smallestStackSize = Collections.min(stackSizeMultimap.keySet());
					Collection<Integer> smallestStacks = stackSizeMultimap.get(smallestStackSize);
					int slotWithSmallestStack = Collections.max(smallestStacks);
					
					player.inventory.setInventorySlotContents(slotWithSmallestStack, ItemStack.EMPTY);
					stackSizeMultimap.remove(smallestStackSize, slotWithSmallestStack);
					localStackMap.remove(slotWithSmallestStack);
				}
				cycleCounter++;
			}
			
		} else if (mode == 2) {
			if (filledStacks >= player.inventory.mainInventory.size()) {
				
				for (INBT sID : list) {
					String str = ((StringNBT)sID).getString();
					
					HashMap<Integer, ItemStack> localStackMap = new HashMap<Integer, ItemStack>(stackMap);
					Multimap<Integer, Integer> stackSizeMultimap = ArrayListMultimap.create();
					
					for (int slot : stackMap.keySet()) {

							if (stackMap.get(slot).getItem() != ForgeRegistries.ITEMS.getValue(new ResourceLocation(str)))
								localStackMap.remove(slot);
						
					}
					
					for (int slot : localStackMap.keySet()) {
						stackSizeMultimap.put(localStackMap.get(slot).getCount(), slot);
					}
					
					if (localStackMap.size() > 0) {
						int smallestStackSize = Collections.min(stackSizeMultimap.keySet());
						Collection<Integer> smallestStacks = stackSizeMultimap.get(smallestStackSize);
						int slotWithSmallestStack = Collections.max(smallestStacks);
						
						player.inventory.setInventorySlotContents(slotWithSmallestStack, ItemStack.EMPTY);
						return;
					}
					
					cycleCounter++;
				}
				
			}
		}
		
	}
  
}
