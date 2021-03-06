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

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.server.ServerWorld;

@Mixin(PiglinTasks.class)
public class MixinPiglinTasks {
	private static final String AVARICE_SCROLL_TAG = EnigmaticLegacy.MODID + ":avarice_scroll_effect";

	private static void markPiglinWithCondition(PiglinEntity piglin, PlayerEntity player) {
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

	@Inject(at = @At("RETURN"), method = "func_234470_a_")
	private static void onPiglinItemPickup(PiglinEntity piglin, ItemEntity itemEntity, CallbackInfo info) {
		UUID ownerID = itemEntity.getThrowerId();

		if (!itemEntity.isAlive() && itemEntity.world instanceof ServerWorld && ownerID != null) {
			ServerWorld world = (ServerWorld) itemEntity.world;
			markPiglinWithCondition(piglin, world.getPlayerByUuid(ownerID));
		}

	}

	@Inject(at = @At("RETURN"), method = "func_234471_a_")
	private static void onBarterByHand(PiglinEntity piglin, PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResultType> info) {
		if (info.getReturnValue() == ActionResultType.CONSUME) {
			markPiglinWithCondition(piglin, player);
		}
	}

	@Inject(at = @At("HEAD"), method = "func_234477_a_", cancellable = true)
	private static void onPiglinBarter(PiglinEntity piglin, boolean repay, CallbackInfo info) {
		ItemStack stack = piglin.getHeldItem(Hand.OFF_HAND);

		if (piglin.world instanceof ServerWorld && piglin.getTags().contains(AVARICE_SCROLL_TAG)) {
			piglin.removeTag(AVARICE_SCROLL_TAG);

			if (piglin.func_242337_eM()) {
				if (repay && stack.isPiglinCurrency()) {
					info.cancel();

					piglin.setHeldItem(Hand.OFF_HAND, ItemStack.EMPTY);
					List<ItemStack> generatedLoot = PiglinTasks.func_234524_k_(piglin);
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
					PiglinTasks.func_234475_a_(piglin, generatedLoot);
				}
			}
		}

	}

}
