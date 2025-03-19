package com.aizistral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

@Pseudo
@Mixin(targets = "vazkii.quark.content.management.client.screen.widgets.MiniInventoryButton")
public class MixinMiniInventoryButton {

	@Redirect(method = { "render", "m_6305_" }, remap = false, at = @At(value = "INVOKE", remap = false,
			target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;getGuiLeft()I"))
	private int onGetGuiLeft(AbstractContainerScreen<?> parent) {
		return parent.getGuiLeft() + 2;
	}

}
