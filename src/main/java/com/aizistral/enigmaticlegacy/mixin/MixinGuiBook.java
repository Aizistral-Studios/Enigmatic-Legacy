package com.aizistral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookEntry;

@Mixin(GuiBook.class)
public class MixinGuiBook {

	/**
	 * @reason Make links in The Acknowledgment trusted.
	 * @author Aizistral
	 */

	@Inject(method = "openWebLink", at = @At("HEAD"), cancellable = true, require = 0, remap = false)
	private static void onOpenWebLink(Screen prevScreen, String address, CallbackInfo info) {
		if (prevScreen instanceof GuiBookEntry entry) {
			if (EnigmaticLegacy.MODID.equals(entry.book.id.getNamespace())) {
				info.cancel();
				var mc = Minecraft.getInstance();
				mc.setScreen(new ConfirmLinkScreen(yes -> {
					if (yes) {
						Util.getPlatform().openUri(address);
					}

					mc.setScreen(prevScreen);
				}, address, true));
			}
		}
	}

}
