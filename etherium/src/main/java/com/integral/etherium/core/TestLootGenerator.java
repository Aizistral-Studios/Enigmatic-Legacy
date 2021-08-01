package com.integral.etherium.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.etherium.EtheriumMod;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.loot.LootTables;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TestLootGenerator extends Item implements IVanishable {
	public Random lootRandomizer = new Random();
	public List<ResourceLocation> lootList = new ArrayList<ResourceLocation>();

	public TestLootGenerator(IEtheriumConfig config) {
		super(EtheriumUtil.defaultProperties(config, TestLootGenerator.class));
		this.setRegistryName(new ResourceLocation(EtheriumMod.MODID, "loot_generator"));

		/*
		for (ResourceLocation table : LootTables.getReadOnlyLootTables()) {
			if (table.getPath().startsWith("chests/") && !table.getPath().startsWith("chests/village/"))
				this.lootList.add(table);
		}

		for (ResourceLocation table : LootTables.getReadOnlyLootTables()) {
			if (table.getPath().startsWith("chests/village/"))
				this.lootList.add(table);
		}
		 */

		this.lootList.add(LootTables.CHESTS_SPAWN_BONUS_CHEST);
		this.lootList.add(LootTables.CHESTS_END_CITY_TREASURE);
		this.lootList.add(LootTables.CHESTS_SIMPLE_DUNGEON);
		this.lootList.add(LootTables.CHESTS_ABANDONED_MINESHAFT);
		this.lootList.add(LootTables.CHESTS_NETHER_BRIDGE);
		this.lootList.add(LootTables.CHESTS_STRONGHOLD_LIBRARY);
		this.lootList.add(LootTables.CHESTS_STRONGHOLD_CROSSING);
		this.lootList.add(LootTables.CHESTS_STRONGHOLD_CORRIDOR);
		this.lootList.add(LootTables.CHESTS_DESERT_PYRAMID);
		this.lootList.add(LootTables.CHESTS_JUNGLE_TEMPLE);
		this.lootList.add(LootTables.CHESTS_JUNGLE_TEMPLE_DISPENSER);
		this.lootList.add(LootTables.CHESTS_IGLOO_CHEST);
		this.lootList.add(LootTables.CHESTS_WOODLAND_MANSION);
		this.lootList.add(LootTables.CHESTS_UNDERWATER_RUIN_SMALL);
		this.lootList.add(LootTables.CHESTS_UNDERWATER_RUIN_BIG);
		this.lootList.add(LootTables.CHESTS_BURIED_TREASURE);
		this.lootList.add(LootTables.CHESTS_SHIPWRECK_MAP);
		this.lootList.add(LootTables.CHESTS_SHIPWRECK_SUPPLY);
		this.lootList.add(LootTables.CHESTS_SHIPWRECK_TREASURE);
		this.lootList.add(LootTables.CHESTS_PILLAGER_OUTPOST);
		this.lootList.add(LootTables.BASTION_TREASURE);
		this.lootList.add(LootTables.BASTION_OTHER);
		this.lootList.add(LootTables.BASTION_BRIDGE);
		this.lootList.add(LootTables.BASTION_HOGLIN_STABLE);
		this.lootList.add(LootTables.RUINED_PORTAL);
		this.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_WEAPONSMITH);
		this.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_TOOLSMITH);
		this.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_ARMORER);
		this.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_CARTOGRAPHER);
		this.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_MASON);
		this.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_SHEPHERD);
		this.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_BUTCHER);
		this.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_FLETCHER);
		this.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_FISHER);
		this.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_TANNERY);
		this.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_TEMPLE);
		this.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_DESERT_HOUSE);
		this.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_PLAINS_HOUSE);
		this.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_TAIGA_HOUSE);
		this.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_SNOWY_HOUSE);
		this.lootList.add(LootTables.CHESTS_VILLAGE_VILLAGE_SAVANNA_HOUSE);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.lootGenerator1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.lootGenerator2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.lootGenerator3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.lootGenerator4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.lootGenerator5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.lootGenerator6");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.lootGenerator7");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.lootGenerator8");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.lootGenerator9");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.lootGeneratorCurrent");

		list.add(new StringTextComponent("" + this.lootList.get(stack.getDamage())).mergeStyle(TextFormatting.GOLD));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {

		ItemStack itemstack = player.getHeldItem(hand);

		if (!player.isCrouching()) {
			player.setActiveHand(hand);

			if (itemstack.getDamage() < this.lootList.size() - 1) {
				itemstack.setDamage(itemstack.getDamage() + 1);
			} else {
				itemstack.setDamage(0);
			}

			player.swingArm(hand);

		} else {
			if (itemstack.getDamage() > 0) {
				itemstack.setDamage(itemstack.getDamage() - 1);
			} else {
				itemstack.setDamage(this.lootList.size() - 1);
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
			if (world.getTileEntity(context.getPos()) instanceof ChestTileEntity && player.isCrouching()) {
				ChestTileEntity chest = (ChestTileEntity) world.getTileEntity(context.getPos());
				Direction dir = context.getFace();

				if (dir == Direction.UP) {
					chest.setLootTable(this.lootList.get(stack.getDamage()), this.lootRandomizer.nextLong());
					chest.fillWithLoot(player);
				} else if (dir == Direction.DOWN && !player.getCooldownTracker().hasCooldown(this)) {
					player.getCooldownTracker().setCooldown(this, 40);
					HashMap<Item, Integer> lootMap = new HashMap<Item, Integer>();

					for (int counter = 0; counter < 32768; counter++) {
						chest.setLootTable(this.lootList.get(stack.getDamage()), this.lootRandomizer.nextLong());
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

					EtheriumMod.logger.info("Estimated generation complete in 32768 instances, results:");
					for (Item theItem : lootMap.keySet()) {
						EtheriumMod.logger.info("Item: " + theItem.getDisplayName(new ItemStack(theItem)).getString() + ", Amount: " + lootMap.get(theItem));
					}

					player.sendMessage(new TranslationTextComponent("message.enigmaticlegacy.gen_sim_complete").mergeStyle(TextFormatting.DARK_PURPLE), player.getUniqueID());

				} else {
					chest.clear();
				}

				return ActionResultType.SUCCESS;
			}
		}

		return ActionResultType.PASS;
	}

}

