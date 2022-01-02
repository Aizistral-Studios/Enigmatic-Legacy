package com.integral.enigmaticlegacy.mixin;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.item.enchantment.EnchantmentData;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.world.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.EnchantmentContainer;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;

@Mixin(net.minecraft.inventory.container.EnchantmentContainer.class)
public abstract class MixinEnchantmentContainer extends Container {

	protected MixinEnchantmentContainer(ContainerType<?> type, int id) {
		super(type, id);
	}

	@Inject(at = @At("INVOKE"), method = "net.minecraft.inventory.container.EnchantmentContainer.clickMenuButton(Lnet/minecraft/entity/player/PlayerEntity;I)Z", cancellable = true)
	private void onEnchantedItem(PlayerEntity player, int clickedID, CallbackInfoReturnable<Boolean> info) {
		if (EnchantmentContainer.class.isInstance(this)) {
			// Evaluating expression promts error assuming incompatible types,
			// so we need to forget our own class to avoid alerting the compiler
			EnchantmentContainer container = (EnchantmentContainer) (Object) this;

			if (SuperpositionHandler.isTheCursedOne(player))
				if (SuperpositionHandler.hasItem(player, EnigmaticLegacy.enchanterPearl) || SuperpositionHandler.hasCurio(player, EnigmaticLegacy.enchanterPearl)) {
					ItemStack inputItem = container.enchantSlots.getItem(0);
					int levelsRequired = clickedID + 1;

					if (container.costs[clickedID] <= 0 || inputItem.isEmpty() || (player.experienceLevel < levelsRequired || player.experienceLevel < container.costs[clickedID]) && !player.abilities.instabuild) {
						info.setReturnValue(false);
						return; // false;
					} else {
						container.access.execute((world, blockPos) -> {
							ItemStack enchantedItem = inputItem;
							List<EnchantmentData> rolledEnchantments = container.getEnchantmentList(inputItem, clickedID, container.costs[clickedID]);
							if (!rolledEnchantments.isEmpty()) {
								ItemStack doubleRoll = EnchantmentHelper.enchantItem(player.getRandom(), enchantedItem.copy(), Math.min(container.costs[clickedID]+7, 40), true);

								player.onEnchantmentPerformed(inputItem, levelsRequired);
								boolean isBookStack = inputItem.getItem() == Items.BOOK;
								if (isBookStack) {
									enchantedItem = new ItemStack(Items.ENCHANTED_BOOK);
									CompoundNBT compoundnbt = inputItem.getTag();
									if (compoundnbt != null) {
										enchantedItem.setTag(compoundnbt.copy());
									}

									container.enchantSlots.setItem(0, enchantedItem);
								}

								for(EnchantmentData enchantmentdata : rolledEnchantments) {
									if (isBookStack) {
										EnchantedBookItem.addEnchantment(enchantedItem, enchantmentdata);
									} else {
										enchantedItem.enchant(enchantmentdata.enchantment, enchantmentdata.level);
									}
								}

								enchantedItem = SuperpositionHandler.mergeEnchantments(enchantedItem, doubleRoll, false, false);
								container.enchantSlots.setItem(0, enchantedItem);

								player.awardStat(Stats.ENCHANT_ITEM);
								if (player instanceof ServerPlayerEntity) {
									CriteriaTriggers.ENCHANTED_ITEM.trigger((ServerPlayerEntity)player, enchantedItem, levelsRequired);
								}

								container.enchantSlots.setChanged();
								container.enchantmentSeed.set(player.getEnchantmentSeed());
								container.slotsChanged(container.enchantSlots);
								world.playSound((PlayerEntity)null, blockPos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
							}

						});

						info.setReturnValue(true);
						return; // true;
					}

				}
		}

	}

	@OnlyIn(Dist.CLIENT)
	@Inject(at = @At("HEAD"), method = "net.minecraft.inventory.container.EnchantmentContainer.getGoldCount()I", cancellable = true)
	public void onGetLapisAmount(CallbackInfoReturnable<Integer> info) {
		if (EnchantmentContainer.class.isInstance(this)) {
			Object forgottenObject = this;
			EnchantmentContainer container = (EnchantmentContainer)forgottenObject;
			PlayerEntity containerUser = null;

			for (Slot slot : container.slots) {
				if (slot.container instanceof PlayerInventory) {
					PlayerInventory playerInv = (PlayerInventory) slot.container;
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
