package com.integral.enigmaticlegacy.mixin;

import java.lang.reflect.Method;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.EnchantmentContainer;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stats.Stats;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

/**
 * No, if I said player gets more enchantments - they will get their enchantments.
 * @author Integral
 */

@Pseudo
@Mixin(targets="shadows.apotheosis.ench.table.ApothEnchantContainer")
public class MixinApotheosisEnchantmentContainer extends EnchantmentContainer {
	public MixinApotheosisEnchantmentContainer(int id, PlayerInventory playerInventory) {
		super(id, playerInventory);
	}

	@Inject(at = @At("HEAD"), method = "enchantItem(Lnet/minecraft/entity/player/PlayerEntity;I)Z", cancellable = true)
	private void onEnchantedItem(PlayerEntity player, int id, CallbackInfoReturnable<Boolean> info) {

		if (SuperpositionHandler.isTheCursedOne(player))
			if (SuperpositionHandler.hasItem(player, EnigmaticLegacy.enchanterPearl) || SuperpositionHandler.hasCurio(player, EnigmaticLegacy.enchanterPearl)) {

				int level = this.enchantLevels[id];
				ItemStack toEnchant = this.tableInventory.getStackInSlot(0);
				int i = id + 1;

				if (this.enchantLevels[id] <= 0 || toEnchant.isEmpty() || (player.experienceLevel < i || player.experienceLevel < this.enchantLevels[id]) && !player.abilities.isCreativeMode) {
					info.setReturnValue(false);
					return;// false;
				}

				this.worldPosCallable.consume((world, pos) -> {
					ItemStack enchanted = toEnchant;
					List<EnchantmentData> list = this.getEnchantmentList(toEnchant, id, this.enchantLevels[id]);
					if (!list.isEmpty()) {
						ItemStack firstRoll = this.enchantStack(player, enchanted, id, true);
						ItemStack secondRoll = this.enchantStack(player, enchanted, id, false);

						enchanted = SuperpositionHandler.mergeEnchantments(firstRoll, secondRoll, true, false);
						this.tableInventory.setInventorySlotContents(0, enchanted);

						player.addStat(Stats.ENCHANT_ITEM);
						if (player instanceof ServerPlayerEntity) {
							/*
							try {
								Class<?> triggerClass = Class.forName("shadows.apotheosis.advancements.EnchantedTrigger");
								Method triggerMethod = triggerClass.getDeclaredMethod("trigger", ServerPlayerEntity.class, ItemStack.class, Integer.class, Float.class, Float.class, Float.class);
							} catch (Exception ex) {
							}
							 */
							//EnchantedItemTrigger
							//CriteriaTriggers.ENCHANTED_ITEM.trigger((ServerPlayerEntity)player, enchanted, level);
						}

						this.tableInventory.markDirty();
						this.onCraftMatrixChanged(this.tableInventory);
						world.playSound((PlayerEntity) null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
					}

				});


				info.setReturnValue(true);
				return;// true;
			}
	}

	private ItemStack enchantStack(PlayerEntity player, ItemStack stack, int id, boolean shadowsRollTwice) {
		ItemStack toEnchant = stack.copy();

		int i = id + 1;
		List<EnchantmentData> list = this.getEnchantmentList(toEnchant, id, this.enchantLevels[id]);
		if (!list.isEmpty()) {
			ItemStack doubleRoll = EnchantmentHelper.addRandomEnchantment(player.getRNG(), toEnchant.copy(), (int) Math.min(this.enchantLevels[id]/1.5, 40), true);
			player.onEnchant(toEnchant, i);

			boolean tomeConfirmed = false;

			try {
				Class<?> tomeClass = Class.forName("shadows.apotheosis.ench.objects.TomeItem");
				if (tomeClass.isInstance(toEnchant.getItem())) {
					tomeConfirmed = true;
				}
			} catch (Exception ex) {
				// NO-OP
			}

			boolean flag = toEnchant.getItem() == Items.BOOK || tomeConfirmed;
			if (flag) {
				toEnchant = new ItemStack(Items.ENCHANTED_BOOK);
			}

			for (int j = 0; j < list.size(); ++j) {
				EnchantmentData enchantmentdata = list.get(j);
				if (flag) {
					EnchantedBookItem.addEnchantment(toEnchant, enchantmentdata);
				} else {
					toEnchant.addEnchantment(enchantmentdata.enchantment, enchantmentdata.enchantmentLevel);
				}
			}

			if (shadowsRollTwice) {
				toEnchant = SuperpositionHandler.mergeEnchantments(toEnchant, doubleRoll, false, true);
			}
			this.xpSeed.set(player.getXPSeed());
		}
		return toEnchant;
	}

}
