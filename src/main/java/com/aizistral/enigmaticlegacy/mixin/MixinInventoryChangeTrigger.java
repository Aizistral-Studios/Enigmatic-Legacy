package com.aizistral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.triggers.CursedInventoryChangedTrigger;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

@Mixin(InventoryChangeTrigger.class)
public class MixinInventoryChangeTrigger {

	@Inject(method = "trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;)V",
			at = @At("HEAD"), require = 1)
	private void onTrigger(ServerPlayer player, Inventory inventory, ItemStack stack, CallbackInfo info) {
		if (SuperpositionHandler.isTheCursedOne(player)) {
			CursedInventoryChangedTrigger.INSTANCE.trigger(player, inventory, stack);
		}
	}

}
