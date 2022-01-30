package com.integral.enigmaticlegacy.gui;

import javax.annotation.Nonnull;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.items.EnderRing;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.CreativeModeTab;
import top.theillusivec4.curios.client.gui.CuriosScreen;

public class EnderChestInventoryButton extends PlayerInventoryButton {

	public EnderChestInventoryButton(AbstractContainerScreen<?> gui, int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, OnPress onPressIn) {
		super(gui, xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, onPressIn);
	}

	@Override
	protected boolean beforeRender(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		boolean hasRing = SuperpositionHandler.hasCurio(Minecraft.getInstance().player, EnigmaticLegacy.enderRing) || SuperpositionHandler.hasCurio(Minecraft.getInstance().player, EnigmaticLegacy.cursedRing);

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
