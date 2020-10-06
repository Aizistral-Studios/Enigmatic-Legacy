package com.integral.enigmaticlegacy.gui.containers;

import java.util.Map;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.LoreFragment;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import org.apache.commons.lang3.StringUtils;

public class LoreInscriberContainer extends Container {
	protected final IInventory craftResultInv = new CraftResultInventory();
	protected final IInventory craftSlotsInv = new Inventory(2) {
		/**
		 * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think
		 * it hasn't changed and skip it.
		 */
		@Override
		public void markDirty() {
			super.markDirty();
			LoreInscriberContainer.this.onCraftMatrixChanged(this);
		}
	};

	protected final IWorldPosCallable worldPosCallable;
	protected final PlayerEntity player;

	private static final Logger LOGGER = LogManager.getLogger();
	private String unparsedInputField;

	public LoreInscriberContainer(int syncID, PlayerInventory playerInv) {
		this(syncID, playerInv, IWorldPosCallable.of(playerInv.player.world, playerInv.player.getPosition()));
	}

	public LoreInscriberContainer(int syncID, PlayerInventory playerInv, PacketBuffer extraData) {
		this(syncID, playerInv, IWorldPosCallable.of(playerInv.player.world, playerInv.player.getPosition()));
	}

	private LoreInscriberContainer(int p_i50102_1_, PlayerInventory p_i50102_2_, IWorldPosCallable p_i50102_3_) {
		this(EnigmaticLegacy.LORE_INSCRIBER_CONTAINER, p_i50102_1_, p_i50102_2_, p_i50102_3_);
	}

	private LoreInscriberContainer(@Nullable ContainerType<?> p_i231587_1_, int p_i231587_2_, PlayerInventory playerInv, IWorldPosCallable p_i231587_4_) {
		super(p_i231587_1_, p_i231587_2_);
		this.worldPosCallable = p_i231587_4_;
		this.player = playerInv.player;
		this.addSlot(new Slot(this.craftSlotsInv, 0, 40, 51) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				if (stack.getItem() instanceof LoreFragment)
					return true;
				else
					return false;
			}

			@Override
			public int getSlotStackLimit() {
				return 1;
			}
		});
		this.addSlot(new Slot(this.craftResultInv, 2, 116, 51) {
			/**
			 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
			 */
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}

			/**
			 * Return whether this slot's stack can be taken from this slot.
			 */
			@Override
			public boolean canTakeStack(PlayerEntity playerIn) {
				return LoreInscriberContainer.this.canCraft(playerIn, this.getHasStack());
			}

			@Override
			public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
				this.onSlotChanged();
				return LoreInscriberContainer.this.claimResult(thePlayer, stack);
			}
		});

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k) {
			if (k == this.player.inventory.currentItem) {
				this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142) {
					@Override
					public boolean canTakeStack(PlayerEntity playerIn) {
						return false;
					}

					@Override
					public boolean isItemValid(ItemStack stack) {
						return false;
					}

					@Override
					public int getSlotStackLimit() {
						return 0;
					}
				});
			} else {
				this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
			}
		}

	}

	/**
	 * Callback for when the crafting matrix is changed.
	 */
	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn) {
		super.onCraftMatrixChanged(inventoryIn);
		if (inventoryIn == this.craftSlotsInv) {
			this.updateRepairOutput();
		}

	}

	/**
	 * Called when the container is closed.
	 */
	@Override
	public void onContainerClosed(PlayerEntity playerIn) {
		super.onContainerClosed(playerIn);
		/*
		this.field_234644_e_.consume((p_234647_2_, p_234647_3_) -> {
			this.clearContainer(playerIn, p_234647_2_, this.field_234643_d_);
		});
		 */
	}

	/**
	 * Determines whether supplied player can use this container
	 */
	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return true;
	}

	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
		//System.out.println("Slot ID: " + slotId + ", Drag Type: " + dragType + ", Click Type: " + clickTypeIn.toString());

		if (clickTypeIn == ClickType.SWAP) {
			if (dragType == player.inventory.currentItem || slotId == 29 + player.inventory.currentItem)
				return ItemStack.EMPTY;
		}
		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}

	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
	 * inventory and the other inventory(s).
	 */
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index != 0 && index != 1) {
				if (index >= 2 && index < 38) {
					if (!this.mergeItemStack(itemstack1, 0, 1, false))
						return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 2, 38, false))
				return ItemStack.EMPTY;

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount())
				return ItemStack.EMPTY;

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}

	protected boolean canCraft(PlayerEntity player, boolean hasOutputStack) {
		return hasOutputStack;
	}

	protected ItemStack claimResult(PlayerEntity player, ItemStack stack) {
		this.craftSlotsInv.setInventorySlotContents(0, ItemStack.EMPTY);

		if (!player.world.isRemote) {
			player.world.playSound(null, player.getPosition(), EnigmaticLegacy.WRITE, SoundCategory.PLAYERS, 1.0F, (float) (0.9F + (Math.random() * 0.1F)));
		}

		return stack;
	}

	/**
	 * called when the Anvil Input Slot changes, calculates the new result and puts it in the output slot
	 */
	public void updateRepairOutput() {
		ItemStack input = this.craftSlotsInv.getStackInSlot(0);
		if (input.isEmpty()) {
			this.craftResultInv.setInventorySlotContents(0, ItemStack.EMPTY);
		} else {
			ItemStack output = input.copy();

			if (StringUtils.isBlank(this.unparsedInputField)) {
				if (input.hasDisplayName()) {
					output.clearCustomName();
				} else {
					output = ItemStack.EMPTY;
				}
			} else if (!this.unparsedInputField.equals(input.getDisplayName().getString())) {
				this.unleashAnvilParser(output);
			} else {
				output = ItemStack.EMPTY;
			}

			this.craftResultInv.setInventorySlotContents(0, output);
			this.detectAndSendChanges();
		}
	}

	/**
	 * used by the Anvil GUI to update the Item Name being typed by the player
	 */
	public void updateItemName(String newName) {
		this.unparsedInputField = newName;
		this.updateRepairOutput();
	}

	private void unleashAnvilParser(ItemStack itemstack) {
		ItemLoreHelper.AnvilParser parser = ItemLoreHelper.AnvilParser.parseField(this.unparsedInputField);
		if (!parser.getFormattedString().equals("") || parser.shouldRemoveString()) {
			if (parser.isLoreString()) {
				if (parser.getLoreIndex() != -1) {
					ItemLoreHelper.setLoreString(itemstack, parser.getFormattedString(), parser.getLoreIndex());
				} else {
					ItemLoreHelper.addLoreString(itemstack, parser.getFormattedString());
				}
			} else if (parser.shouldRemoveString()) {
				ItemLoreHelper.removeLoreString(itemstack, parser.getLoreIndex());
			} else {
				ItemLoreHelper.setDisplayName(itemstack, parser.getFormattedString());
			}
		}
	}
}
