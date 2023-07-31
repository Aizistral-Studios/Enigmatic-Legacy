package com.aizistral.enigmaticlegacy.items;

import java.awt.TextComponent;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBase;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.aizistral.enigmaticlegacy.registries.EnigmaticSounds;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public class OblivionStone extends ItemBase implements Vanishable {
	public static Omniconfig.IntParameter itemSoftcap;
	public static Omniconfig.IntParameter itemHardcap;

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("OblivionStone");

		itemSoftcap = builder
				.comment("Soft cap for Keystone of The Oblivion. When it's reached, the list view seen in it's Ctrl tooltip will be fixed at this amount of items, and become chaotic and unreadable. Required since monitors are not infinitely large these days.")
				.min(1)
				.getInt("Softcap", 25);

		itemHardcap = builder
				.comment("Hard cap for Keystone of The Oblivion. When it's reached, you will no longer be able to add new items to it's list via crafting. Required to prevent potential perfomance issues with ridiculously large lists.")
				.min(1)
				.getInt("Hardcap", 100);

		builder.popPrefix();
	}

	public OblivionStone() {
		super(ItemBase.getDefaultProperties().stacksTo(1).rarity(Rarity.RARE).fireResistant());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidStone1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidStone2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidStone3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidStone4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidStone5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidStone6");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidStone7");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidStone8");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidStone9");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidStone10");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidStone11");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidStone12");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidStone13");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidStone14");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidStone15");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidStone16");
		} else if (Screen.hasControlDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidStoneCtrlList");
			if (stack.hasTag()) {
				CompoundTag nbt = stack.getTag();
				ListTag arr = nbt.getList("SupersolidID", 8);
				int counter = 0;

				if (arr.size() <= itemSoftcap.getValue()) {
					for (Tag s_uncast : arr) {
						String s = ((StringTag) s_uncast).getAsString();
						Item something = ForgeRegistries.ITEMS.getValue(new ResourceLocation(s));
						if (something != null) {
							ItemStack displayStack;
							displayStack = new ItemStack(something, 1);

							list.add(Component.literal(" - " + displayStack.getHoverName().getString()).withStyle(ChatFormatting.GOLD));
						}
						counter++;
					}
				} else {
					for (int s = 0; s < itemSoftcap.getValue(); s++) {
						int randomID = random.nextInt(arr.size());
						Item something = ForgeRegistries.ITEMS.getValue(new ResourceLocation(((StringTag) arr.get(randomID)).getAsString()));

						if (something != null) {
							ItemStack displayStack;
							displayStack = new ItemStack(something, 1);

							list.add(Component.literal(" - " + displayStack.getHoverName().getString()).withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.GOLD));
						}
					}
				}
			}

		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidStoneHoldCtrl");
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		MutableComponent mode;

		if (ItemNBTHelper.getBoolean(stack, "IsActive", true)) {
			mode = Component.translatable("tooltip.enigmaticlegacy.voidStoneMode" + ItemNBTHelper.getInt(stack, "ConsumptionMode", 0));
		} else {
			mode = Component.translatable("tooltip.enigmaticlegacy.voidStoneModeInactive");
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidStoneModeDesc", null, mode.getString());
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
		if (action == ClickAction.PRIMARY || !slot.mayPlace(stack) || !slot.mayPickup(player) || other.isEmpty())
			return false;

		other.setCount(0);

		if (player.level().isClientSide) {
			player.level().playSound(player, player.blockPosition(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 0.25F, 1.2F + (float) Math.random() * 0.4F);
		}

		return true;
	}

	@Override
	public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
		if (action == ClickAction.PRIMARY || !slot.mayPlace(stack) || !slot.mayPickup(player) || !slot.hasItem())
			return false;

		slot.set(ItemStack.EMPTY);

		if (player.level().isClientSide) {
			player.level().playSound(player, player.blockPosition(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 0.25F, 1.2F + (float) Math.random() * 0.4F);
		}

		return true;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {

		ItemStack stack = player.getItemInHand(hand);
		int mode = ItemNBTHelper.getInt(stack, "ConsumptionMode", 0);

		if (player.isCrouching()) {
			world.playSound(null, player.blockPosition(), ItemNBTHelper.getBoolean(stack, "IsActive", true) ? EnigmaticSounds.CHARGED_OFF : EnigmaticSounds.CHARGED_ON, SoundSource.PLAYERS, (float) (0.8F + (Math.random() * 0.2F)), (float) (0.8F + (Math.random() * 0.2F)));
			ItemNBTHelper.setBoolean(stack, "IsActive", !ItemNBTHelper.getBoolean(stack, "IsActive", true));
		} else {
			if (mode >= 0 && mode < 2) {
				ItemNBTHelper.setInt(stack, "ConsumptionMode", mode + 1);
			} else {
				ItemNBTHelper.setInt(stack, "ConsumptionMode", 0);
			}

			world.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2F)));
		}

		player.swing(hand);
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);

	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
		if (!(entity instanceof Player) || entity.tickCount % 4 != 0)
			return;

		Player player = (Player) entity;

		if (!ItemNBTHelper.getBoolean(stack, "IsActive", true) || stack.getOrCreateTag().getList("SupersolidID", 8).size() < 1)
			return;

		CompoundTag nbt = stack.getOrCreateTag();
		ListTag arr = nbt.getList("SupersolidID", 8);

		OblivionStone.consumeStuff(player, arr, ItemNBTHelper.getInt(stack, "ConsumptionMode", 0));
	}

	public static void consumeStuff(Player player, ListTag list, int mode) {
		HashMap<Integer, ItemStack> stackMap = new HashMap<Integer, ItemStack>();
		int cycleCounter = 0;
		int filledStacks = 0;

		for (int slot = 0; slot < player.getInventory().items.size(); slot++) {
			if (!player.getInventory().items.get(slot).isEmpty()) {
				filledStacks += 1;
				if (player.getInventory().items.get(slot).getItem() != EnigmaticItems.VOID_STONE) {
					stackMap.put(slot, player.getInventory().items.get(slot));
				}
			}
		}

		if (stackMap.size() == 0)
			return;

		if (mode == 0) {
			for (Tag sID : list) {
				String str = ((StringTag) sID).getAsString();

				for (int slot : stackMap.keySet()) {
					if (stackMap.get(slot).getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation(str))) {
						player.getInventory().setItem(slot, ItemStack.EMPTY);
					}
				}
				cycleCounter++;
			}
		} else if (mode == 1) {

			for (Tag sID : list) {
				String str = ((StringTag) sID).getAsString();

				HashMap<Integer, ItemStack> localStackMap = new HashMap<Integer, ItemStack>(stackMap);
				Multimap<Integer, Integer> stackSizeMultimap = ArrayListMultimap.create();

				for (int slot : stackMap.keySet()) {
					if (stackMap.get(slot).getItem() != ForgeRegistries.ITEMS.getValue(new ResourceLocation(str))) {
						localStackMap.remove(slot);
					}
				}

				for (int slot : localStackMap.keySet()) {
					stackSizeMultimap.put(localStackMap.get(slot).getCount(), slot);
				}

				while (localStackMap.size() > (player.getInventory().offhand.get(0).getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation(str)) ? 0 : 1)) {
					int smallestStackSize = Collections.min(stackSizeMultimap.keySet());
					Collection<Integer> smallestStacks = stackSizeMultimap.get(smallestStackSize);
					int slotWithSmallestStack = Collections.max(smallestStacks);

					player.getInventory().setItem(slotWithSmallestStack, ItemStack.EMPTY);
					stackSizeMultimap.remove(smallestStackSize, slotWithSmallestStack);
					localStackMap.remove(slotWithSmallestStack);
				}
				cycleCounter++;
			}

		} else if (mode == 2) {
			if (filledStacks >= player.getInventory().items.size()) {

				for (Tag sID : list) {
					String str = ((StringTag) sID).getAsString();
					HashMap<Integer, ItemStack> localStackMap = new HashMap<Integer, ItemStack>(stackMap);
					Multimap<Integer, Integer> stackSizeMultimap = ArrayListMultimap.create();

					for (int slot : stackMap.keySet()) {
						if (stackMap.get(slot).getItem() != ForgeRegistries.ITEMS.getValue(new ResourceLocation(str))) {
							localStackMap.remove(slot);
						}
					}

					for (int slot : localStackMap.keySet()) {
						stackSizeMultimap.put(localStackMap.get(slot).getCount(), slot);
					}

					if (localStackMap.size() > 0) {
						int smallestStackSize = Collections.min(stackSizeMultimap.keySet());
						Collection<Integer> smallestStacks = stackSizeMultimap.get(smallestStackSize);
						int slotWithSmallestStack = Collections.max(smallestStacks);

						player.getInventory().setItem(slotWithSmallestStack, ItemStack.EMPTY);
						return;
					}

					cycleCounter++;
				}

			}
		}

	}

}
