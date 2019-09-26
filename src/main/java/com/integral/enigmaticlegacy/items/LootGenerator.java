package com.integral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.helpers.LoreHelper;

import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class LootGenerator extends Item {
	
 public static Properties integratedProperties = new Item.Properties();
 public static Random lootRandomizer = new Random();
 public static List<ResourceLocation> lootList = new ArrayList<ResourceLocation>();

 public LootGenerator(Properties properties) {
		super(properties);
		
		   lootList.add(LootTables.CHESTS_SPAWN_BONUS_CHEST);
		   lootList.add(LootTables.CHESTS_END_CITY_TREASURE);
		   lootList.add(LootTables.CHESTS_SIMPLE_DUNGEON);
		   lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_WEAPONSMITH);
		   lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_TOOLSMITH);
		   lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_ARMORER);
		   lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_CARTOGRAPHER);
		   lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_MASON);
		   lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_SHEPHERD);
		   lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_BUTCHER);
		   lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_FLETCHER);
		   lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_FISHER);
		   lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_TANNERY);
		   lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_TEMPLE);
		   lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_DESERT_HOUSE);
		   lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_PLAINS_HOUSE);
		   lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_TAIGA_HOUSE);
		   lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_SNOWY_HOUSE);
		   lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_SAVANNA_HOUSE);
		   lootList.add(LootTables.CHESTS_ABANDONED_MINESHAFT);
		   lootList.add(LootTables.CHESTS_NETHER_BRIDGE);
		   lootList.add(LootTables.CHESTS_STRONGHOLD_LIBRARY);
		   lootList.add(LootTables.CHESTS_STRONGHOLD_CROSSING);
		   lootList.add(LootTables.CHESTS_STRONGHOLD_CORRIDOR);
		   lootList.add(LootTables.CHESTS_DESERT_PYRAMID);
		   lootList.add(LootTables.CHESTS_JUNGLE_TEMPLE);
		   lootList.add(LootTables.CHESTS_JUNGLE_TEMPLE_DISPENSER);
		   lootList.add(LootTables.CHESTS_IGLOO_CHEST);
		   lootList.add(LootTables.CHESTS_WOODLAND_MANSION);
		   lootList.add(LootTables.CHESTS_UNDERWATER_RUIN_SMALL);
		   lootList.add(LootTables.CHESTS_UNDERWATER_RUIN_BIG);
		   lootList.add(LootTables.CHESTS_BURIED_TREASURE);
		   lootList.add(LootTables.CHESTS_SHIPWRECK_MAP);
		   lootList.add(LootTables.CHESTS_SHIPWRECK_SUPPLY);
		   lootList.add(LootTables.CHESTS_SHIPWRECK_TREASURE);
		   lootList.add(LootTables.CHESTS_PILLAGER_OUTPOST);
		   
 }
 
 public static Properties setupIntegratedProperties() {
	 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(1);
	 integratedProperties.rarity(Rarity.EPIC);
	 
	 return integratedProperties;
 }

 @OnlyIn(Dist.CLIENT)
 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
	 if(ControlsScreen.hasShiftDown()) {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.lootGenerator1");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.lootGenerator2");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.lootGenerator3");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.lootGenerator4");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.lootGenerator5");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.lootGenerator6");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.lootGenerator7");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.lootGenerator8");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.lootGenerator9");
	 } else {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
	 }
	 
	 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
	 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.lootGeneratorCurrent");
	 list.add(new TranslationTextComponent("tooltip.enigmaticlegacy.code6").appendText("" + lootList.get(stack.getDamage())));
 }
 
 
 @Override
 public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {

	 	ItemStack itemstack = player.getHeldItem(hand);
	 	
	 	
	 	if (!player.isSneaking()) {
	 		player.setActiveHand(hand);
	 		
	 		if (itemstack.getDamage() < lootList.size()-1) {
	 			itemstack.setDamage(itemstack.getDamage()+1);
	 		} else {
	 			itemstack.setDamage(0);
	 		}
	 		
	 		player.swingArm(hand);
	 		
	 	} else {
	 		if (itemstack.getDamage() > 0) {
	 			itemstack.setDamage(itemstack.getDamage()-1);
	 		} else {
	 			itemstack.setDamage(lootList.size()-1);
	 		}
	 		player.swingArm(hand);
	 	}

        return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
        
  }
 
 
 public ActionResultType onItemUse(ItemUseContext context) {
	 PlayerEntity player = context.getPlayer();
	 World world = context.getWorld();
	 ItemStack stack = context.getItem();
	 
	 if (world.isRemote)
		 return ActionResultType.SUCCESS;
	 
	 if (world.getBlockState(context.getPos()).hasTileEntity()) {
		 if (world.getTileEntity(context.getPos()) instanceof ChestTileEntity) {
			 ChestTileEntity chest = (ChestTileEntity) world.getTileEntity(context.getPos());
			 
			 
			 if (context.getFace() == Direction.UP) {
				 chest.setLootTable(lootList.get(stack.getDamage()), lootRandomizer.nextLong());
				 chest.fillWithLoot(player);
		 	 } else if (context.getFace() == Direction.DOWN) {
		 		 
		 		HashMap<Item, Integer> lootMap = new HashMap<Item, Integer>();
		 		 
		 		 for (int counter = 0; counter < 32768; counter++) {
		 			 chest.setLootTable(lootList.get(stack.getDamage()), lootRandomizer.nextLong());
		 			 chest.fillWithLoot(player);
		 			 
		 			 for (int slot = 0; slot < chest.getSizeInventory(); slot++) {
		 				 ItemStack generatedStack = chest.getStackInSlot(slot);
		 				 Item generatedItem = generatedStack.getItem();
		 				 int amount = generatedStack.getCount();
		 				 
		 				 if (!generatedStack.isEmpty()) {
		 				 
		 				 if (lootMap.containsKey(generatedItem)) {
		 					 lootMap.put(generatedItem, lootMap.get(generatedItem)+amount);
		 				 } else {
		 					lootMap.put(generatedItem, amount);
		 				 }
		 				 
		 				 }
		 			 }
		 			 
		 			chest.clear();
		 		 }
		 		 
		 		 EnigmaticLegacy.enigmaticLogger.info("Estimated generation complete in 32768 instances, results:");
	 			 for (Item theItem : lootMap.keySet()) {
	 				EnigmaticLegacy.enigmaticLogger.info("Item: " + theItem.getDisplayName(new ItemStack(theItem)).getUnformattedComponentText() + ", Amount: " + lootMap.get(theItem));
	 			 }
	 			 
	 			 player.sendMessage(new TranslationTextComponent("message.enigmaticlegacy.gen_sim_complete").applyTextStyle(TextFormatting.DARK_PURPLE));
	 			 
			 } else {
				 chest.clear();
			 }
			 
			 return ActionResultType.SUCCESS;
		 }
	 }
	 
     return ActionResultType.PASS;
  }
 
  
}
