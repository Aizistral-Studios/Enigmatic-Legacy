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

		this.lootList.add(LootTables.SPAWN_BONUS_CHEST);
		this.lootList.add(LootTables.END_CITY_TREASURE);
		this.lootList.add(LootTables.SIMPLE_DUNGEON);
		this.lootList.add(LootTables.ABANDONED_MINESHAFT);
		this.lootList.add(LootTables.NETHER_BRIDGE);
		this.lootList.add(LootTables.STRONGHOLD_LIBRARY);
		this.lootList.add(LootTables.STRONGHOLD_CROSSING);
		this.lootList.add(LootTables.STRONGHOLD_CORRIDOR);
		this.lootList.add(LootTables.DESERT_PYRAMID);
		this.lootList.add(LootTables.JUNGLE_TEMPLE);
		this.lootList.add(LootTables.JUNGLE_TEMPLE_DISPENSER);
		this.lootList.add(LootTables.IGLOO_CHEST);
		this.lootList.add(LootTables.WOODLAND_MANSION);
		this.lootList.add(LootTables.UNDERWATER_RUIN_SMALL);
		this.lootList.add(LootTables.UNDERWATER_RUIN_BIG);
		this.lootList.add(LootTables.BURIED_TREASURE);
		this.lootList.add(LootTables.SHIPWRECK_MAP);
		this.lootList.add(LootTables.SHIPWRECK_SUPPLY);
		this.lootList.add(LootTables.SHIPWRECK_TREASURE);
		this.lootList.add(LootTables.PILLAGER_OUTPOST);
		this.lootList.add(LootTables.BASTION_TREASURE);
		this.lootList.add(LootTables.BASTION_OTHER);
		this.lootList.add(LootTables.BASTION_BRIDGE);
		this.lootList.add(LootTables.BASTION_HOGLIN_STABLE);
		this.lootList.add(LootTables.RUINED_PORTAL);
		this.lootList.add(LootTables.VILLAGE_WEAPONSMITH);
		this.lootList.add(LootTables.VILLAGE_TOOLSMITH);
		this.lootList.add(LootTables.VILLAGE_ARMORER);
		this.lootList.add(LootTables.VILLAGE_CARTOGRAPHER);
		this.lootList.add(LootTables.VILLAGE_MASON);
		this.lootList.add(LootTables.VILLAGE_SHEPHERD);
		this.lootList.add(LootTables.VILLAGE_BUTCHER);
		this.lootList.add(LootTables.VILLAGE_FLETCHER);
		this.lootList.add(LootTables.VILLAGE_FISHER);
		this.lootList.add(LootTables.VILLAGE_TANNERY);
		this.lootList.add(LootTables.VILLAGE_TEMPLE);
		this.lootList.add(LootTables.VILLAGE_DESERT_HOUSE);
		this.lootList.add(LootTables.VILLAGE_PLAINS_HOUSE);
		this.lootList.add(LootTables.VILLAGE_TAIGA_HOUSE);
		this.lootList.add(LootTables.VILLAGE_SNOWY_HOUSE);
		this.lootList.add(LootTables.VILLAGE_SAVANNA_HOUSE);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
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

		list.add(new StringTextComponent("" + this.lootList.get(stack.getDamageValue())).withStyle(TextFormatting.GOLD));
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {

		ItemStack itemstack = player.getItemInHand(hand);

		if (!player.isCrouching()) {
			player.startUsingItem(hand);

			if (itemstack.getDamageValue() < this.lootList.size() - 1) {
				itemstack.setDamageValue(itemstack.getDamageValue() + 1);
			} else {
				itemstack.setDamageValue(0);
			}

			player.swing(hand);

		} else {
			if (itemstack.getDamageValue() > 0) {
				itemstack.setDamageValue(itemstack.getDamageValue() - 1);
			} else {
				itemstack.setDamageValue(this.lootList.size() - 1);
			}
			player.swing(hand);
		}

		return new ActionResult<>(ActionResultType.SUCCESS, itemstack);

	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		PlayerEntity player = context.getPlayer();
		World world = context.getLevel();
		ItemStack stack = context.getItemInHand();

		if (world.isClientSide)
			return ActionResultType.SUCCESS;

		if (world.getBlockState(context.getClickedPos()).hasTileEntity()) {
			if (world.getBlockEntity(context.getClickedPos()) instanceof ChestTileEntity && player.isCrouching()) {
				ChestTileEntity chest = (ChestTileEntity) world.getBlockEntity(context.getClickedPos());
				Direction dir = context.getClickedFace();

				if (dir == Direction.UP) {
					chest.setLootTable(this.lootList.get(stack.getDamageValue()), this.lootRandomizer.nextLong());
					chest.unpackLootTable(player);
				} else if (dir == Direction.DOWN && !player.getCooldowns().isOnCooldown(this)) {
					player.getCooldowns().addCooldown(this, 40);
					HashMap<Item, Integer> lootMap = new HashMap<Item, Integer>();

					for (int counter = 0; counter < 32768; counter++) {
						chest.setLootTable(this.lootList.get(stack.getDamageValue()), this.lootRandomizer.nextLong());
						chest.unpackLootTable(player);

						for (int slot = 0; slot < chest.getContainerSize(); slot++) {
							ItemStack generatedStack = chest.getItem(slot);
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

						chest.clearContent();
					}

					EtheriumMod.logger.info("Estimated generation complete in 32768 instances, results:");
					for (Item theItem : lootMap.keySet()) {
						EtheriumMod.logger.info("Item: " + theItem.getName(new ItemStack(theItem)).getString() + ", Amount: " + lootMap.get(theItem));
					}

					player.sendMessage(new TranslationTextComponent("message.enigmaticlegacy.gen_sim_complete").withStyle(TextFormatting.DARK_PURPLE), player.getUUID());

				} else {
					chest.clearContent();
				}

				return ActionResultType.SUCCESS;
			}
		}

		return ActionResultType.PASS;
	}

}

