package com.integral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.helpers.LoreHelper;

import net.minecraft.client.gui.screen.Screen;
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

		LootGenerator.lootList.add(LootTables.CHESTS_SPAWN_BONUS_CHEST);
		LootGenerator.lootList.add(LootTables.CHESTS_END_CITY_TREASURE);
		LootGenerator.lootList.add(LootTables.CHESTS_SIMPLE_DUNGEON);
		LootGenerator.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_WEAPONSMITH);
		LootGenerator.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_TOOLSMITH);
		LootGenerator.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_ARMORER);
		LootGenerator.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_CARTOGRAPHER);
		LootGenerator.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_MASON);
		LootGenerator.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_SHEPHERD);
		LootGenerator.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_BUTCHER);
		LootGenerator.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_FLETCHER);
		LootGenerator.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_FISHER);
		LootGenerator.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_TANNERY);
		LootGenerator.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_TEMPLE);
		LootGenerator.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_DESERT_HOUSE);
		LootGenerator.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_PLAINS_HOUSE);
		LootGenerator.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_TAIGA_HOUSE);
		LootGenerator.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_SNOWY_HOUSE);
		LootGenerator.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_SAVANNA_HOUSE);
		LootGenerator.lootList.add(LootTables.CHESTS_ABANDONED_MINESHAFT);
		LootGenerator.lootList.add(LootTables.CHESTS_NETHER_BRIDGE);
		LootGenerator.lootList.add(LootTables.CHESTS_STRONGHOLD_LIBRARY);
		LootGenerator.lootList.add(LootTables.CHESTS_STRONGHOLD_CROSSING);
		LootGenerator.lootList.add(LootTables.CHESTS_STRONGHOLD_CORRIDOR);
		LootGenerator.lootList.add(LootTables.CHESTS_DESERT_PYRAMID);
		LootGenerator.lootList.add(LootTables.CHESTS_JUNGLE_TEMPLE);
		LootGenerator.lootList.add(LootTables.CHESTS_JUNGLE_TEMPLE_DISPENSER);
		LootGenerator.lootList.add(LootTables.CHESTS_IGLOO_CHEST);
		LootGenerator.lootList.add(LootTables.CHESTS_WOODLAND_MANSION);
		LootGenerator.lootList.add(LootTables.CHESTS_UNDERWATER_RUIN_SMALL);
		LootGenerator.lootList.add(LootTables.CHESTS_UNDERWATER_RUIN_BIG);
		LootGenerator.lootList.add(LootTables.CHESTS_BURIED_TREASURE);
		LootGenerator.lootList.add(LootTables.CHESTS_SHIPWRECK_MAP);
		LootGenerator.lootList.add(LootTables.CHESTS_SHIPWRECK_SUPPLY);
		LootGenerator.lootList.add(LootTables.CHESTS_SHIPWRECK_TREASURE);
		LootGenerator.lootList.add(LootTables.CHESTS_PILLAGER_OUTPOST);

	}

	public static Properties setupIntegratedProperties() {
		LootGenerator.integratedProperties.group(EnigmaticLegacy.enigmaticTab);
		LootGenerator.integratedProperties.maxStackSize(1);
		LootGenerator.integratedProperties.rarity(Rarity.EPIC);

		return LootGenerator.integratedProperties;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
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
		list.add(new TranslationTextComponent("tooltip.enigmaticlegacy.code6").appendText("" + LootGenerator.lootList.get(stack.getDamage())));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {

		ItemStack itemstack = player.getHeldItem(hand);

		if (!player.isShiftKeyDown()) {
			player.setActiveHand(hand);

			if (itemstack.getDamage() < LootGenerator.lootList.size() - 1) {
				itemstack.setDamage(itemstack.getDamage() + 1);
			} else {
				itemstack.setDamage(0);
			}

			player.swingArm(hand);

		} else {
			if (itemstack.getDamage() > 0) {
				itemstack.setDamage(itemstack.getDamage() - 1);
			} else {
				itemstack.setDamage(LootGenerator.lootList.size() - 1);
			}
			player.swingArm(hand);
		}

		return new ActionResult<>(ActionResultType.SUCCESS, itemstack);

	}

	@Override
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
					chest.setLootTable(LootGenerator.lootList.get(stack.getDamage()), LootGenerator.lootRandomizer.nextLong());
					chest.fillWithLoot(player);
				} else if (context.getFace() == Direction.DOWN) {

					HashMap<Item, Integer> lootMap = new HashMap<Item, Integer>();

					for (int counter = 0; counter < 32768; counter++) {
						chest.setLootTable(LootGenerator.lootList.get(stack.getDamage()), LootGenerator.lootRandomizer.nextLong());
						chest.fillWithLoot(player);

						for (int slot = 0; slot < chest.getSizeInventory(); slot++) {
							ItemStack generatedStack = chest.getStackInSlot(slot);
							Item generatedItem = generatedStack.getItem();
							int amount = generatedStack.getCount();

							if (!generatedStack.isEmpty()) {

								if (lootMap.containsKey(generatedItem)) {
									lootMap.put(generatedItem, lootMap.get(generatedItem) + amount);
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
