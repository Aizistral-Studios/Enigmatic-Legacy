package com.aizistral.enigmaticlegacy.gui;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.items.EnderRing;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;

public class EnderChestInventoryButton extends PlayerInventoryButton {

	public EnderChestInventoryButton(AbstractContainerScreen<?> gui, int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, OnPress onPressIn) {
		super(gui, xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, onPressIn);
	}

	@Override
	protected boolean beforeRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		boolean hasRing = SuperpositionHandler.hasCurio(Minecraft.getInstance().player, EnigmaticItems.ENDER_RING) || SuperpositionHandler.hasCurio(Minecraft.getInstance().player, EnigmaticItems.CURSED_RING);

		if (!hasRing || !EnderRing.inventoryButtonEnabled.getValue()) {
			this.active = false;
			return false;
		}

		return true;
	}

	@Override
	public Tuple<Integer, Integer> getOffsets(boolean creative) {
		int x = creative ? 170 + EnderRing.buttonOffsetXCreative.getValue() : 150 + EnderRing.buttonOffsetX.getValue();
		int y = creative ? 5 + EnderRing.buttonOffsetYCreative.getValue() : 61 + EnderRing.buttonOffsetY.getValue();

		return new Tuple<>(x, y);
	}

}
