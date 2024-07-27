package com.aizistral.enigmaticlegacy.items;

import java.awt.TextComponent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class LootGenerator extends ItemBase implements Vanishable {
	public Random lootRandomizer = new Random();
	public List<ResourceLocation> lootList = new ArrayList<ResourceLocation>();

	public LootGenerator() {
		super(ItemBase.getDefaultProperties().rarity(Rarity.EPIC).stacksTo(1));

		/*
		for (ResourceLocation table : BuiltInLootTables.getReadOnlyLootTables()) {
			if (table.getPath().startsWith("chests/") && !table.getPath().startsWith("chests/village/"))
				this.lootList.add(table);
		}

		for (ResourceLocation table : BuiltInLootTables.getReadOnlyLootTables()) {
			if (table.getPath().startsWith("chests/village/"))
				this.lootList.add(table);
		}
		 */

		this.lootList.add(BuiltInLootTables.SPAWN_BONUS_CHEST);
		this.lootList.add(BuiltInLootTables.END_CITY_TREASURE);
		this.lootList.add(BuiltInLootTables.SIMPLE_DUNGEON);
		this.lootList.add(BuiltInLootTables.ABANDONED_MINESHAFT);
		this.lootList.add(BuiltInLootTables.NETHER_BRIDGE);
		this.lootList.add(BuiltInLootTables.STRONGHOLD_LIBRARY);
		this.lootList.add(BuiltInLootTables.STRONGHOLD_CROSSING);
		this.lootList.add(BuiltInLootTables.STRONGHOLD_CORRIDOR);
		this.lootList.add(BuiltInLootTables.DESERT_PYRAMID);
		this.lootList.add(BuiltInLootTables.JUNGLE_TEMPLE);
		this.lootList.add(BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER);
		this.lootList.add(BuiltInLootTables.IGLOO_CHEST);
		this.lootList.add(BuiltInLootTables.WOODLAND_MANSION);
		this.lootList.add(BuiltInLootTables.UNDERWATER_RUIN_SMALL);
		this.lootList.add(BuiltInLootTables.UNDERWATER_RUIN_BIG);
		this.lootList.add(BuiltInLootTables.BURIED_TREASURE);
		this.lootList.add(BuiltInLootTables.SHIPWRECK_MAP);
		this.lootList.add(BuiltInLootTables.SHIPWRECK_SUPPLY);
		this.lootList.add(BuiltInLootTables.SHIPWRECK_TREASURE);
		this.lootList.add(BuiltInLootTables.PILLAGER_OUTPOST);
		this.lootList.add(BuiltInLootTables.BASTION_TREASURE);
		this.lootList.add(BuiltInLootTables.BASTION_OTHER);
		this.lootList.add(BuiltInLootTables.BASTION_BRIDGE);
		this.lootList.add(BuiltInLootTables.BASTION_HOGLIN_STABLE);
		this.lootList.add(BuiltInLootTables.RUINED_PORTAL);
		this.lootList.add(BuiltInLootTables.VILLAGE_WEAPONSMITH);
		this.lootList.add(BuiltInLootTables.VILLAGE_TOOLSMITH);
		this.lootList.add(BuiltInLootTables.VILLAGE_ARMORER);
		this.lootList.add(BuiltInLootTables.VILLAGE_CARTOGRAPHER);
		this.lootList.add(BuiltInLootTables.VILLAGE_MASON);
		this.lootList.add(BuiltInLootTables.VILLAGE_SHEPHERD);
		this.lootList.add(BuiltInLootTables.VILLAGE_BUTCHER);
		this.lootList.add(BuiltInLootTables.VILLAGE_FLETCHER);
		this.lootList.add(BuiltInLootTables.VILLAGE_FISHER);
		this.lootList.add(BuiltInLootTables.VILLAGE_TANNERY);
		this.lootList.add(BuiltInLootTables.VILLAGE_TEMPLE);
		this.lootList.add(BuiltInLootTables.VILLAGE_DESERT_HOUSE);
		this.lootList.add(BuiltInLootTables.VILLAGE_PLAINS_HOUSE);
		this.lootList.add(BuiltInLootTables.VILLAGE_TAIGA_HOUSE);
		this.lootList.add(BuiltInLootTables.VILLAGE_SNOWY_HOUSE);
		this.lootList.add(BuiltInLootTables.VILLAGE_SAVANNA_HOUSE);
		this.lootList.add(BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY);
		this.lootList.add(BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY);
		this.lootList.add(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON);
		this.lootList.add(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE);
		this.lootList.add(BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY);
		this.lootList.add(BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
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

		list.add(Component.literal("" + this.lootList.get(stack.getDamageValue())).withStyle(ChatFormatting.GOLD));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
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

		if (player instanceof ServerPlayer) {
			player.displayClientMessage(Component.literal("Table: " + this.lootList.get(itemstack.getDamageValue())), true);
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		Level world = context.getLevel();
		ItemStack stack = context.getItemInHand();

		if (world.isClientSide)
			return InteractionResult.SUCCESS;

		if (world.getBlockState(context.getClickedPos()).hasBlockEntity()) {
			if (world.getBlockEntity(context.getClickedPos()) instanceof ChestBlockEntity && player.isCrouching()) {
				ChestBlockEntity chest = (ChestBlockEntity) world.getBlockEntity(context.getClickedPos());
				Direction dir = context.getClickedFace();

				if (dir == Direction.UP) {
					chest.setLootTable(this.lootList.get(stack.getDamageValue()), this.lootRandomizer.nextLong());
					chest.unpackLootTable(player);
				} else if (dir == Direction.DOWN && !SuperpositionHandler.hasSpellstoneCooldown(player)) {

					SuperpositionHandler.setSpellstoneCooldown(player, 40);
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

					EnigmaticLegacy.LOGGER.info("Estimated generation complete in 32768 instances, results:");
					for (Item theItem : lootMap.keySet()) {
						EnigmaticLegacy.LOGGER.info("Item: " + theItem.getName(new ItemStack(theItem)).getString() + ", Amount: " + lootMap.get(theItem));
					}

					player.sendSystemMessage(Component.translatable("message.enigmaticlegacy.gen_sim_complete").withStyle(ChatFormatting.DARK_PURPLE));

				} else {
					chest.clearContent();
				}

				return InteractionResult.SUCCESS;
			}
		}

		return InteractionResult.PASS;
	}

}
