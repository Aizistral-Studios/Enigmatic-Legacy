package com.integral.enigmaticlegacy.mixin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.JsonConfigHandler;
import com.integral.enigmaticlegacy.handlers.EnigmaticEventHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.merchant.villager.VillagerEntity;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinTasks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MerchantOffer;
import net.minecraft.util.InteractionResult;
import net.minecraft.util.InteractionHand;
import net.minecraft.server.level.ServerLevel;

@Mixin(PiglinTasks.class)
public class MixinPiglinTasks {
	private static final String AVARICE_SCROLL_TAG = EnigmaticLegacy.MODID + ":avarice_scroll_effect";

	private static void markPiglinWithCondition(Piglin piglin, Player player) {
		if (player != null && piglin != null && SuperpositionHandler.hasCurio(player, EnigmaticLegacy.avariceScroll))
			if (!piglin.getTags().contains(AVARICE_SCROLL_TAG)) {
				piglin.addTag(AVARICE_SCROLL_TAG);
			}
	}

	private static List<ItemStack> distributeExcess(ItemStack stack) {
		List<ItemStack> newStacks = new ArrayList<>();

		while (stack.getCount() > stack.getMaxStackSize()) {
			newStacks.add(stack.split(stack.getMaxStackSize()));
		}

		return newStacks;
	}

	@Inject(at = @At("RETURN"), method = "pickUpItem")
	private static void onPiglinItemPickup(Piglin piglin, ItemEntity itemEntity, CallbackInfo info) {
		UUID ownerID = itemEntity.getThrower();

		if (!itemEntity.isAlive() && itemEntity.level instanceof ServerLevel && ownerID != null) {
			ServerLevel world = (ServerLevel) itemEntity.level;
			markPiglinWithCondition(piglin, world.getPlayerByUUID(ownerID));
		}

	}

	@Inject(at = @At("RETURN"), method = "mobInteract")
	private static void onBarterByHand(Piglin piglin, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> info) {
		if (info.getReturnValue() == InteractionResult.CONSUME) {
			markPiglinWithCondition(piglin, player);
		}
	}

	@Inject(at = @At("HEAD"), method = "stopHoldingOffHandItem", cancellable = true)
	private static void onPiglinBarter(Piglin piglin, boolean repay, CallbackInfo info) {
		ItemStack stack = piglin.getItemInHand(InteractionHand.OFF_HAND);

		if (piglin.level instanceof ServerLevel && piglin.getTags().contains(AVARICE_SCROLL_TAG)) {
			piglin.removeTag(AVARICE_SCROLL_TAG);

			if (piglin.isAdult()) {
				if (repay && stack.isPiglinCurrency()) {
					info.cancel();

					piglin.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
					List<ItemStack> generatedLoot = PiglinTasks.getBarterResponseItems(piglin);
					List<ItemStack> newStacks = new ArrayList<>();

					generatedLoot.forEach(lootStack -> {
						if (lootStack != null && !lootStack.isEmpty()) {

							double multiplier = EnigmaticEventHandler.theySeeMeRollin.nextDouble()*2.0;
							int bonusAmount = (int)Math.round(lootStack.getCount() * multiplier);
							int newCount = lootStack.getCount() + bonusAmount;
							lootStack.setCount(newCount);

							newStacks.addAll(distributeExcess(lootStack));
						}
					});

					generatedLoot.addAll(newStacks);
					PiglinTasks.throwItems(piglin, generatedLoot);
				}
			}
		}

	}

}
