package com.integral.enigmaticlegacy.gui.containers;

import java.util.List;
import java.util.Random;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.EnchantmentContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnigmaticEnchantmentContainer extends EnchantmentContainer {

	private IInventory tableInventory = new Inventory(2) {

		@Override
		public void setChanged() {
			super.setChanged();
			EnigmaticEnchantmentContainer.this.slotsChanged(this);
		}
	};

	private IWorldPosCallable worldPosCallable;
	private Random rand = new Random();
	private IntReferenceHolder xpSeed = IntReferenceHolder.standalone();
	public int[] enchantLevels = new int[3];
	public int[] enchantClue = new int[] { -1, -1, -1 };
	public int[] worldClue = new int[] { -1, -1, -1 };

	public static EnigmaticEnchantmentContainer fromOld(EnchantmentContainer oldContainer, PlayerEntity player) throws IllegalArgumentException, IllegalAccessException {
		EnigmaticEnchantmentContainer newContainer = new EnigmaticEnchantmentContainer(oldContainer.containerId, player.inventory, oldContainer.access);

		newContainer.tableInventory = oldContainer.enchantSlots;
		newContainer.enchantLevels = oldContainer.costs;
		newContainer.enchantClue = oldContainer.enchantClue;
		newContainer.worldClue = oldContainer.levelClue;

		return newContainer;
	}

	private EnigmaticEnchantmentContainer(int id, PlayerInventory playerInventory, IWorldPosCallable pos) {
		super(id, playerInventory, pos);

		this.worldPosCallable = pos;
		this.addSlot(new Slot(this.tableInventory, 0, 15, 47) {
			/**
			 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
			 */
			@Override
			public boolean mayPlace(ItemStack stack) {
				return true;
			}

			/**
			 * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the
			 * case of armor slots)
			 */
			@Override
			public int getMaxStackSize() {
				return 1;
			}
		});
		this.addSlot(new Slot(this.tableInventory, 1, 35, 47) {
			/**
			 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
			 */
			@Override
			public boolean mayPlace(ItemStack stack) {
				return net.minecraftforge.common.Tags.Items.GEMS_LAPIS.contains(stack.getItem());
			}
		});

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
		}

		this.addDataSlot(IntReferenceHolder.shared(this.enchantLevels, 0));
		this.addDataSlot(IntReferenceHolder.shared(this.enchantLevels, 1));
		this.addDataSlot(IntReferenceHolder.shared(this.enchantLevels, 2));
		this.addDataSlot(this.xpSeed).set(playerInventory.player.getEnchantmentSeed());
		this.addDataSlot(IntReferenceHolder.shared(this.enchantClue, 0));
		this.addDataSlot(IntReferenceHolder.shared(this.enchantClue, 1));
		this.addDataSlot(IntReferenceHolder.shared(this.enchantClue, 2));
		this.addDataSlot(IntReferenceHolder.shared(this.worldClue, 0));
		this.addDataSlot(IntReferenceHolder.shared(this.worldClue, 1));
		this.addDataSlot(IntReferenceHolder.shared(this.worldClue, 2));
	}

	@Override
	public void slotsChanged(IInventory inventoryIn) {
		if (inventoryIn == this.tableInventory) {
			ItemStack itemstack = inventoryIn.getItem(0);
			if (!itemstack.isEmpty() && itemstack.isEnchantable()) {
				this.worldPosCallable.execute((p_217002_2_, p_217002_3_) -> {
					int power = 0;

					for (int k = -1; k <= 1; ++k) {
						for (int l = -1; l <= 1; ++l) {
							if ((k != 0 || l != 0) && p_217002_2_.isEmptyBlock(p_217002_3_.offset(l, 0, k)) && p_217002_2_.isEmptyBlock(p_217002_3_.offset(l, 1, k))) {
								power += this.getPower(p_217002_2_, p_217002_3_.offset(l * 2, 0, k * 2));
								power += this.getPower(p_217002_2_, p_217002_3_.offset(l * 2, 1, k * 2));

								if (l != 0 && k != 0) {
									power += this.getPower(p_217002_2_, p_217002_3_.offset(l * 2, 0, k));
									power += this.getPower(p_217002_2_, p_217002_3_.offset(l * 2, 1, k));
									power += this.getPower(p_217002_2_, p_217002_3_.offset(l, 0, k * 2));
									power += this.getPower(p_217002_2_, p_217002_3_.offset(l, 1, k * 2));
								}
							}
						}
					}

					this.rand.setSeed(this.xpSeed.get());

					for (int i1 = 0; i1 < 3; ++i1) {
						this.enchantLevels[i1] = EnchantmentHelper.getEnchantmentCost(this.rand, i1, power, itemstack);
						this.enchantClue[i1] = -1;
						this.worldClue[i1] = -1;
						if (this.enchantLevels[i1] < i1 + 1) {
							this.enchantLevels[i1] = 0;
						}
						this.enchantLevels[i1] = net.minecraftforge.event.ForgeEventFactory.onEnchantmentLevelSet(p_217002_2_, p_217002_3_, i1, power, itemstack, this.enchantLevels[i1]);
					}

					for (int j1 = 0; j1 < 3; ++j1) {
						if (this.enchantLevels[j1] > 0) {
							List<EnchantmentData> list = this.getEnchantmentList(itemstack, j1, this.enchantLevels[j1]);
							if (list != null && !list.isEmpty()) {
								EnchantmentData enchantmentdata = list.get(this.rand.nextInt(list.size()));
								this.enchantClue[j1] = Registry.ENCHANTMENT.getId(enchantmentdata.enchantment);
								this.worldClue[j1] = enchantmentdata.level;
							}
						}
					}

					this.broadcastChanges();
				});
			} else {
				for (int i = 0; i < 3; ++i) {
					this.enchantLevels[i] = 0;
					this.enchantClue[i] = -1;
					this.worldClue[i] = -1;
				}
			}
		}

	}

	private float getPower(net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos) {
		return world.getBlockState(pos).getEnchantPowerBonus(world, pos);
	}


	@Override
	public boolean clickMenuButton(PlayerEntity playerIn, int id) {
		System.out.println("We hooked in!");
		ItemStack itemstack = this.tableInventory.getItem(0);
		ItemStack itemstack1 = this.tableInventory.getItem(1);
		int i = id + 1;
		if ((itemstack1.isEmpty() || itemstack1.getCount() < i) && !playerIn.abilities.instabuild)
			return false;
		else if (this.enchantLevels[id] <= 0 || itemstack.isEmpty() || (playerIn.experienceLevel < i || playerIn.experienceLevel < this.enchantLevels[id]) && !playerIn.abilities.instabuild)
			return false;
		else {
			this.worldPosCallable.execute((p_217003_6_, p_217003_7_) -> {
				ItemStack itemstack2 = itemstack;
				List<EnchantmentData> list = this.getEnchantmentList(itemstack, id, this.enchantLevels[id]);
				if (!list.isEmpty()) {
					playerIn.onEnchantmentPerformed(itemstack, i);
					boolean flag = itemstack.getItem() == Items.BOOK;
					if (flag) {
						itemstack2 = new ItemStack(Items.ENCHANTED_BOOK);
						CompoundNBT compoundnbt = itemstack.getTag();
						if (compoundnbt != null) {
							itemstack2.setTag(compoundnbt.copy());
						}

						this.tableInventory.setItem(0, itemstack2);
					}

					for (int j = 0; j < list.size(); ++j) {
						EnchantmentData enchantmentdata = list.get(j);
						if (flag) {
							EnchantedBookItem.addEnchantment(itemstack2, enchantmentdata);
						} else {
							itemstack2.enchant(enchantmentdata.enchantment, enchantmentdata.level);
						}
					}

					if (!playerIn.abilities.instabuild) {
						itemstack1.shrink(i);
						if (itemstack1.isEmpty()) {
							this.tableInventory.setItem(1, ItemStack.EMPTY);
						}
					}

					playerIn.awardStat(Stats.ENCHANT_ITEM);
					if (playerIn instanceof ServerPlayerEntity) {
						CriteriaTriggers.ENCHANTED_ITEM.trigger((ServerPlayerEntity) playerIn, itemstack2, i);
					}

					this.tableInventory.setChanged();
					this.xpSeed.set(playerIn.getEnchantmentSeed());
					this.slotsChanged(this.tableInventory);
					p_217003_6_.playSound((PlayerEntity) null, p_217003_7_, SoundEvents.ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, p_217003_6_.random.nextFloat() * 0.1F + 0.9F);
				}

			});
			return true;
		}
	}

	@Override
	public List<EnchantmentData> getEnchantmentList(ItemStack stack, int enchantSlot, int level) {
		this.rand.setSeed(this.xpSeed.get() + enchantSlot);
		List<EnchantmentData> list = EnchantmentHelper.selectEnchantment(this.rand, stack, level, false);
		if (stack.getItem() == Items.BOOK && list.size() > 1) {
			list.remove(this.rand.nextInt(list.size()));
		}

		return list;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public int getGoldCount() {
		ItemStack itemstack = this.tableInventory.getItem(1);
		return itemstack.isEmpty() ? 0 : itemstack.getCount();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public int getEnchantmentSeed() {
		return this.xpSeed.get();
	}

	/**
	 * Called when the container is closed.
	 */
	@Override
	public void removed(PlayerEntity playerIn) {
		super.removed(playerIn);
		this.worldPosCallable.execute((p_217004_2_, p_217004_3_) -> {
			this.clearContainer(playerIn, playerIn.level, this.tableInventory);
		});
	}

	/**
	 * Determines whether supplied player can use this container
	 */
	@Override
	public boolean stillValid(PlayerEntity playerIn) {
		return Container.stillValid(this.worldPosCallable, playerIn, Blocks.ENCHANTING_TABLE);
	}

	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
	 * inventory and the other inventory(s).
	 */
	@Override
	public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index == 0) {
				if (!this.moveItemStackTo(itemstack1, 2, 38, true))
					return ItemStack.EMPTY;
			} else if (index == 1) {
				if (!this.moveItemStackTo(itemstack1, 2, 38, true))
					return ItemStack.EMPTY;
			} else if (itemstack1.getItem() == Items.LAPIS_LAZULI) {
				if (!this.moveItemStackTo(itemstack1, 1, 2, true))
					return ItemStack.EMPTY;
			} else {
				if (this.slots.get(0).hasItem() || !this.slots.get(0).mayPlace(itemstack1))
					return ItemStack.EMPTY;

				ItemStack itemstack2 = itemstack1.copy();
				itemstack2.setCount(1);
				itemstack1.shrink(1);
				this.slots.get(0).set(itemstack2);
			}

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
}