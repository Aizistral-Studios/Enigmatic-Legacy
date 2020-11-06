package com.integral.enigmaticlegacy.mixin;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.EnchantmentContainer;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SAdvancementInfoPacket;
import net.minecraft.stats.Stats;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;

@Mixin(EnchantmentContainer.class)
public class MixinEnchantmentContainer {

	@Inject(at = @At("HEAD"), method = "enchantItem", cancellable = true)
	public void onEnchantedItem(PlayerEntity player, int clickedID, CallbackInfoReturnable<Boolean> info) {
		if (EnchantmentContainer.class.isInstance(this)) {
			// Evaluating expression promts error assuming incompatible types,
			// so we need to forget our own class to avoid alerting the compiler
			Object forgottenObject = this;
			EnchantmentContainer container = (EnchantmentContainer)forgottenObject;

			if (SuperpositionHandler.isTheCursedOne(player))
				if (SuperpositionHandler.hasItem(player, EnigmaticLegacy.enchanterPearl) || SuperpositionHandler.hasCurio(player, EnigmaticLegacy.enchanterPearl)) {
					ItemStack inputItem = container.tableInventory.getStackInSlot(0);
					int levelsRequired = clickedID + 1;

					if (container.enchantLevels[clickedID] <= 0 || inputItem.isEmpty() || (player.experienceLevel < levelsRequired || player.experienceLevel < container.enchantLevels[clickedID]) && !player.abilities.isCreativeMode) {
						info.setReturnValue(false);
						return; // false;
					} else {
						container.worldPosCallable.consume((world, blockPos) -> {
							ItemStack enchantedItem = inputItem;
							List<EnchantmentData> rolledEnchantments = container.getEnchantmentList(inputItem, clickedID, container.enchantLevels[clickedID]);
							if (!rolledEnchantments.isEmpty()) {
								ItemStack doubleRoll = EnchantmentHelper.addRandomEnchantment(player.getRNG(), enchantedItem.copy(), Math.min(container.enchantLevels[clickedID]+7, 40), true);

								player.onEnchant(inputItem, levelsRequired);
								boolean isBookStack = inputItem.getItem() == Items.BOOK;
								if (isBookStack) {
									enchantedItem = new ItemStack(Items.ENCHANTED_BOOK);
									CompoundNBT compoundnbt = inputItem.getTag();
									if (compoundnbt != null) {
										enchantedItem.setTag(compoundnbt.copy());
									}

									container.tableInventory.setInventorySlotContents(0, enchantedItem);
								}

								for(EnchantmentData enchantmentdata : rolledEnchantments) {
									if (isBookStack) {
										EnchantedBookItem.addEnchantment(enchantedItem, enchantmentdata);
									} else {
										enchantedItem.addEnchantment(enchantmentdata.enchantment, enchantmentdata.enchantmentLevel);
									}
								}

								enchantedItem = SuperpositionHandler.mergeEnchantments(enchantedItem, doubleRoll);
								container.tableInventory.setInventorySlotContents(0, enchantedItem);

								player.addStat(Stats.ENCHANT_ITEM);
								if (player instanceof ServerPlayerEntity) {
									CriteriaTriggers.ENCHANTED_ITEM.trigger((ServerPlayerEntity)player, enchantedItem, levelsRequired);
								}

								container.tableInventory.markDirty();
								container.xpSeed.set(player.getXPSeed());
								container.onCraftMatrixChanged(container.tableInventory);
								world.playSound((PlayerEntity)null, blockPos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
							}

						});

						info.setReturnValue(true);
						return; // true;
					}

				}
		}

	}

	@OnlyIn(Dist.CLIENT)
	@Inject(at = @At("HEAD"), method = "getLapisAmount", cancellable = true)
	public void onGetLapisAmount(CallbackInfoReturnable<Integer> info) {
		if (EnchantmentContainer.class.isInstance(this)) {
			Object forgottenObject = this;
			EnchantmentContainer container = (EnchantmentContainer)forgottenObject;
			PlayerEntity containerUser = null;

			for (Slot slot : container.inventorySlots) {
				if (slot.inventory instanceof PlayerInventory) {
					PlayerInventory playerInv = (PlayerInventory) slot.inventory;
					containerUser = playerInv.player;
					break;
				}
			}

			if (containerUser != null) {
				if (SuperpositionHandler.isTheCursedOne(containerUser))
					if (SuperpositionHandler.hasItem(containerUser, EnigmaticLegacy.enchanterPearl) || SuperpositionHandler.hasCurio(containerUser, EnigmaticLegacy.enchanterPearl)) {
						info.setReturnValue(64);
						return;
					}
			}
		}
	}

}
