package com.aizistral.enigmaticlegacy.gui;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.items.MagnetRing;
import com.aizistral.enigmaticlegacy.objects.TransientPlayerData;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;

public class ToggleMagnetEffectsButton extends PlayerInventoryButton {

	public ToggleMagnetEffectsButton(AbstractContainerScreen<?> gui, int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, OnPress onPressIn) {
		super(gui, xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, onPressIn);
	}

	@Override
	protected boolean beforeRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		boolean hasRing = SuperpositionHandler.hasCurio(Minecraft.getInstance().player, EnigmaticItems.MAGNET_RING) || SuperpositionHandler.hasCurio(Minecraft.getInstance().player, EnigmaticItems.SUPER_MAGNET_RING);

		if (!hasRing || !MagnetRing.inventoryButtonEnabled.getValue()) {
			this.active = false;
			return false;
		} else {
			TransientPlayerData data = TransientPlayerData.get(Minecraft.getInstance().player);
			if (data.getDisabledMagnetRingEffects()) {
				this.xTexStart = 42;
			} else {
				this.xTexStart = 21;
			}
			return true;
		}
	}

	@Override
	public Tuple<Integer, Integer> getOffsets(boolean creative) {
		int x = creative ? 147 + MagnetRing.buttonOffsetXCreative.getValue() : 127 + MagnetRing.buttonOffsetX.getValue();
		int y = creative ? 5 + MagnetRing.buttonOffsetYCreative.getValue() : 61 + MagnetRing.buttonOffsetY.getValue();

		return new Tuple<>(x, y);
	}

}
