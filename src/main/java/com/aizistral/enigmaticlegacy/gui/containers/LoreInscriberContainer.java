package com.aizistral.enigmaticlegacy.gui.containers;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.LoreFragment;
import com.aizistral.enigmaticlegacy.registries.EnigmaticMenus;
import com.aizistral.enigmaticlegacy.registries.EnigmaticSounds;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;


public class LoreInscriberContainer extends AbstractContainerMenu {
	protected final ResultContainer craftResultInv = new ResultContainer();
	protected final Container craftSlotsInv = new SimpleContainer(2) {
		@Override
		public void setChanged() {
			super.setChanged();
			LoreInscriberContainer.this.slotsChanged(this);
		}
	};

	protected final ContainerLevelAccess worldPosCallable;
	protected final Player player;

	private static final Logger LOGGER = LogManager.getLogger();
	private String unparsedInputField;

	public LoreInscriberContainer(int syncID, Inventory playerInv) {
		this(syncID, playerInv, ContainerLevelAccess.create(playerInv.player.level(), playerInv.player.blockPosition()));
	}

	public LoreInscriberContainer(int syncID, Inventory playerInv, FriendlyByteBuf extras) {
		this(syncID, playerInv, ContainerLevelAccess.create(playerInv.player.level(), playerInv.player.blockPosition()));
	}

	private LoreInscriberContainer(int id, Inventory Inventory, ContainerLevelAccess worldPosCallable) {
		this(EnigmaticMenus.LORE_INSCRIBER_CONTAINER, id, Inventory, worldPosCallable);
	}

	private LoreInscriberContainer(@Nullable MenuType<?> p_i231587_1_, int p_i231587_2_, Inventory playerInv, ContainerLevelAccess p_i231587_4_) {
		super(p_i231587_1_, p_i231587_2_);

		this.worldPosCallable = p_i231587_4_;
		this.player = playerInv.player;
		this.addSlot(new Slot(this.craftSlotsInv, 0, 40, 51) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				if (stack.getItem() instanceof LoreFragment)
					return true;
				else
					return false;
			}

			@Override
			public int getMaxStackSize() {
				return 1;
			}
		});
		this.addSlot(new Slot(this.craftResultInv, 2, 116, 51) {
			/**
			 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
			 */
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}

			/**
			 * Return whether this slot's stack can be taken from this slot.
			 */
			@Override
			public boolean mayPickup(Player playerIn) {
				return LoreInscriberContainer.this.canCraft(playerIn, this.hasItem());
			}

			@Override
			public void onTake(Player thePlayer, ItemStack stack) {
				this.setChanged();
				LoreInscriberContainer.this.claimResult(thePlayer, stack);
			}
		});

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k) {
			if (k == this.player.getInventory().selected) {
				this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142) {
					@Override
					public boolean mayPickup(Player playerIn) {
						return false;
					}

					@Override
					public boolean mayPlace(ItemStack stack) {
						return false;
					}

					@Override
					public int getMaxStackSize() {
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
	public void slotsChanged(Container inventoryIn) {
		super.slotsChanged(inventoryIn);
		if (inventoryIn == this.craftSlotsInv) {
			this.updateRepairOutput();
		}

	}

	/**
	 * Called when the container is closed.
	 */
	@Override
	public void removed(Player playerIn) {
		super.removed(playerIn);
		this.clearContainer(playerIn, this.craftSlotsInv);
	}

	/**
	 * Determines whether supplied player can use this container
	 */
	@Override
	public boolean stillValid(Player playerIn) {
		return true;
	}

	@Override
	public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
		//System.out.println("Slot ID: " + slotId + ", Drag Type: " + dragType + ", Click Type: " + clickTypeIn.toString());

		if (clickTypeIn == ClickType.SWAP) {
			if (dragType == player.getInventory().selected || slotId == 29 + player.getInventory().selected)
				return;
		}
		super.clicked(slotId, dragType, clickTypeIn, player);
	}

	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
	 * inventory and the other inventory(s).
	 */
	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index != 0 && index != 1) {
				if (index >= 2 && index < 38) {
					if (!this.moveItemStackTo(itemstack1, 0, 1, false))
						return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 2, 38, false))
				return ItemStack.EMPTY;

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount())
				return ItemStack.EMPTY;

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}

	protected boolean canCraft(Player player, boolean hasOutputStack) {
		return hasOutputStack;
	}

	protected ItemStack claimResult(Player player, ItemStack stack) {
		this.craftSlotsInv.setItem(0, ItemStack.EMPTY);

		if (!player.level().isClientSide) {
			player.level().playSound(null, player.blockPosition(), EnigmaticSounds.WRITE, SoundSource.PLAYERS, 1.0F, (float) (0.9F + (Math.random() * 0.1F)));
		}

		return stack;
	}

	/**
	 * called when the Anvil Input Slot changes, calculates the new result and puts it in the output slot
	 */
	public void updateRepairOutput() {
		ItemStack input = this.craftSlotsInv.getItem(0);
		if (input.isEmpty()) {
			this.craftResultInv.setItem(0, ItemStack.EMPTY);
		} else {
			ItemStack output = input.copy();

			if (StringUtils.isBlank(this.unparsedInputField)) {
				if (input.hasCustomHoverName()) {
					output.resetHoverName();
				} else {
					output = ItemStack.EMPTY;
				}
			} else if (!this.unparsedInputField.equals(input.getHoverName().getString())) {
				this.unleashAnvilParser(output);
			} else {
				output = ItemStack.EMPTY;
			}

			this.craftResultInv.setItem(0, output);
			this.broadcastChanges();
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
