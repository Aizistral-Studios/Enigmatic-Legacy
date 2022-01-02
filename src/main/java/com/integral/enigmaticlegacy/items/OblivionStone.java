package com.integral.enigmaticlegacy.items;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.Entity;
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

public class OblivionStone extends ItemBase implements IVanishable {
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
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "oblivion_stone"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		//LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {

			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone2_more");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone6");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone7");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone8");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone9");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone10");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone11");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone12");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone13");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone14");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStone15");

		} else if (Screen.hasControlDown()) {

			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStoneCtrlList");
			if (stack.hasTag()) {
				CompoundNBT nbt = stack.getTag();
				ListNBT arr = nbt.getList("SupersolidID", 8);
				int counter = 0;

				if (arr.size() <= itemSoftcap.getValue()) {
					for (INBT s_uncast : arr) {
						String s = ((StringNBT) s_uncast).getAsString();
						Item something = ForgeRegistries.ITEMS.getValue(new ResourceLocation(s));
						if (something != null) {
							ItemStack displayStack;
							displayStack = new ItemStack(something, 1);

							list.add(new StringTextComponent(" - ").append(((TextComponent)displayStack.getHoverName()).withStyle(TextFormatting.GOLD)).withStyle(TextFormatting.GOLD));
						}
						counter++;
					}
				} else {
					for (int s = 0; s < itemSoftcap.getValue(); s++) {
						int randomID = Item.random.nextInt(arr.size());
						Item something = ForgeRegistries.ITEMS.getValue(new ResourceLocation(((StringNBT) arr.get(randomID)).getAsString()));

						if (something != null) {
							ItemStack displayStack;
							displayStack = new ItemStack(something, 1);

							list.add(new StringTextComponent(" - ").append(((TextComponent)displayStack.getHoverName()).withStyle(TextFormatting.GOLD)).withStyle(TextFormatting.GOLD));
						}
					}
				}
			}

		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStoneHoldCtrl");
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		TranslationTextComponent mode;

		if (ItemNBTHelper.getBoolean(stack, "IsActive", true)) {
			mode = new TranslationTextComponent("tooltip.enigmaticlegacy.oblivionStoneMode" + ItemNBTHelper.getInt(stack, "ConsumptionMode", 0));
		} else {
			mode = new TranslationTextComponent("tooltip.enigmaticlegacy.oblivionStoneModeInactive");
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oblivionStoneModeDesc", null, mode);
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {

		ItemStack stack = player.getItemInHand(hand);
		int mode = ItemNBTHelper.getInt(stack, "ConsumptionMode", 0);

		if (player.isCrouching()) {
			world.playSound(null, player.blockPosition(), ItemNBTHelper.getBoolean(stack, "IsActive", true) ? EnigmaticLegacy.HHOFF : EnigmaticLegacy.HHON, SoundCategory.PLAYERS, (float) (0.8F + (Math.random() * 0.2F)), (float) (0.8F + (Math.random() * 0.2F)));
			ItemNBTHelper.setBoolean(stack, "IsActive", !ItemNBTHelper.getBoolean(stack, "IsActive", true));
		} else {
			if (mode >= 0 && mode < 2) {
				ItemNBTHelper.setInt(stack, "ConsumptionMode", mode + 1);
			} else {
				ItemNBTHelper.setInt(stack, "ConsumptionMode", 0);
			}

			world.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2F)));
		}

		player.swing(hand);
		return new ActionResult<>(ActionResultType.SUCCESS, stack);

	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (!(entity instanceof PlayerEntity) || entity.tickCount % 4 != 0)
			return;

		PlayerEntity player = (PlayerEntity) entity;

		if (!ItemNBTHelper.getBoolean(stack, "IsActive", true) || stack.getOrCreateTag().getList("SupersolidID", 8).size() < 1)
			return;

		CompoundNBT nbt = stack.getOrCreateTag();
		ListNBT arr = nbt.getList("SupersolidID", 8);

		OblivionStone.consumeStuff(player, arr, ItemNBTHelper.getInt(stack, "ConsumptionMode", 0));
	}

	public static void consumeStuff(PlayerEntity player, ListNBT list, int mode) {
		HashMap<Integer, ItemStack> stackMap = new HashMap<Integer, ItemStack>();
		int cycleCounter = 0;
		int filledStacks = 0;

		for (int slot = 0; slot < player.inventory.items.size(); slot++) {
			if (!player.inventory.items.get(slot).isEmpty()) {
				filledStacks += 1;
				if (player.inventory.items.get(slot).getItem() != EnigmaticLegacy.oblivionStone) {
					stackMap.put(slot, player.inventory.items.get(slot));
				}
			}
		}

		if (stackMap.size() == 0)
			return;

		if (mode == 0) {
			for (INBT sID : list) {
				String str = ((StringNBT) sID).getAsString();

				for (int slot : stackMap.keySet()) {
					if (stackMap.get(slot).getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation(str))) {
						player.inventory.setItem(slot, ItemStack.EMPTY);
					}
				}
				cycleCounter++;
			}
		} else if (mode == 1) {

			for (INBT sID : list) {
				String str = ((StringNBT) sID).getAsString();

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

				while (localStackMap.size() > (player.inventory.offhand.get(0).getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation(str)) ? 0 : 1)) {
					int smallestStackSize = Collections.min(stackSizeMultimap.keySet());
					Collection<Integer> smallestStacks = stackSizeMultimap.get(smallestStackSize);
					int slotWithSmallestStack = Collections.max(smallestStacks);

					player.inventory.setItem(slotWithSmallestStack, ItemStack.EMPTY);
					stackSizeMultimap.remove(smallestStackSize, slotWithSmallestStack);
					localStackMap.remove(slotWithSmallestStack);
				}
				cycleCounter++;
			}

		} else if (mode == 2) {
			if (filledStacks >= player.inventory.items.size()) {

				for (INBT sID : list) {
					String str = ((StringNBT) sID).getAsString();
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

						player.inventory.setItem(slotWithSmallestStack, ItemStack.EMPTY);
						return;
					}

					cycleCounter++;
				}

			}
		}

	}

}
