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
import net.minecraft.world.item.enchantment.EnchantmentData;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.world.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.EnchantmentContainer;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.stats.Stats;
import net.minecraft.util.IWorldPosCallable;
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

	public MixinApotheosisEnchantmentContainer(int id, PlayerInventory playerInventory, IWorldPosCallable worldPosCallable) {
		super(id, playerInventory, worldPosCallable);
	}

	@Inject(at = @At("HEAD"), method = "enchantItem(Lnet/minecraft/entity/player/PlayerEntity;I)Z", cancellable = true)
	private void onEnchantedItem(PlayerEntity player, int id, CallbackInfoReturnable<Boolean> info) {

		if (SuperpositionHandler.isTheCursedOne(player))
			if (SuperpositionHandler.hasItem(player, EnigmaticLegacy.enchanterPearl) || SuperpositionHandler.hasCurio(player, EnigmaticLegacy.enchanterPearl)) {

				int level = this.costs[id];
				ItemStack toEnchant = this.enchantSlots.getItem(0);
				int i = id + 1;

				if (this.costs[id] <= 0 || toEnchant.isEmpty() || (player.experienceLevel < i || player.experienceLevel < this.costs[id]) && !player.abilities.instabuild) {
					info.setReturnValue(false);
					return;// false;
				}

				this.access.execute((world, pos) -> {
					ItemStack enchanted = toEnchant;
					List<EnchantmentData> list = this.getEnchantmentList(toEnchant, id, this.costs[id]);
					if (!list.isEmpty()) {
						ItemStack firstRoll = this.enchantStack(player, enchanted, id, true);
						ItemStack secondRoll = this.enchantStack(player, enchanted, id, false);

						enchanted = SuperpositionHandler.mergeEnchantments(firstRoll, secondRoll, true, false);
						this.enchantSlots.setItem(0, enchanted);

						player.awardStat(Stats.ENCHANT_ITEM);
						if (player instanceof ServerPlayerEntity) {

							// TODO Gotta finish this someday

							/*
							try {
								Class<?> triggerClass = Class.forName("shadows.apotheosis.advancements.EnchantedTrigger");
								Method triggerMethod = triggerClass.getDeclaredMethod("trigger", ServerPlayerEntity.class, ItemStack.class, Integer.class, Float.class, Float.class, Float.class);
							} catch (Exception ex) {
							}
							 */
							//EnchantedItemTrigger
							CriteriaTriggers.ENCHANTED_ITEM.trigger((ServerPlayerEntity)player, enchanted, level);
						}

						this.enchantSlots.setChanged();
						this.slotsChanged(this.enchantSlots);
						world.playSound((PlayerEntity) null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
					}

				});


				info.setReturnValue(true);
				return;// true;
			}
	}

	private ItemStack enchantStack(PlayerEntity player, ItemStack stack, int id, boolean shadowsRollTwice) {
		ItemStack toEnchant = stack.copy();

		int i = id + 1;
		List<EnchantmentData> list = this.getEnchantmentList(toEnchant, id, this.costs[id]);
		if (!list.isEmpty()) {
			ItemStack doubleRoll = EnchantmentHelper.enchantItem(player.getRandom(), toEnchant.copy(), (int) Math.min(this.costs[id]/1.5, 40), true);
			player.onEnchantmentPerformed(toEnchant, i);

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
					toEnchant.enchant(enchantmentdata.enchantment, enchantmentdata.level);
				}
			}

			if (shadowsRollTwice) {
				toEnchant = SuperpositionHandler.mergeEnchantments(toEnchant, doubleRoll, false, true);
			}
			this.enchantmentSeed.set(player.getEnchantmentSeed());
		}
		return toEnchant;
	}

}
