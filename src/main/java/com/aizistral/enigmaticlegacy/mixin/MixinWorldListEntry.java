package com.aizistral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.storage.LevelSummary;

@Mixin(WorldSelectionList.WorldListEntry.class)
public class MixinWorldListEntry {
	@Shadow @Final private LevelSummary summary;
	@Unique private Component enigmaticLegacy$cachedInfo;

	/**
	 * Gets called for every world in the world selection screen<br>
	 * Used to display Enigmatic Legacy related information
	 */
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/LevelSummary;getInfo()Lnet/minecraft/network/chat/Component;"))
	private Component getAltInfo(LevelSummary summary) {
		if (this.enigmaticLegacy$cachedInfo == null) {
			this.enigmaticLegacy$cachedInfo = SuperpositionHandler.getAltInfo(summary);
		}

		return this.enigmaticLegacy$cachedInfo;
	}

	/**
	 * Prevent the player from joining the world if they lose all their {@link com.aizistral.enigmaticlegacy.items.SoulCrystal}<br>
	 * (Also displays a relevant message)
	 */
	@Inject(method = "joinWorld", at = @At("HEAD"), cancellable = true)
	private void onJoinWorld(CallbackInfo info) {
		if (SuperpositionHandler.isWorldFractured(SuperpositionHandler.getSaveFolder(this.summary))) {
			EnigmaticLegacy.PROXY.displayPermadeathScreen();
			info.cancel();
		}
	}

}
