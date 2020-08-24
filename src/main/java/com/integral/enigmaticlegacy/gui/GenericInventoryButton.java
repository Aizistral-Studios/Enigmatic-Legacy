package com.integral.enigmaticlegacy.gui;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class GenericInventoryButton extends ImageButton {

	private final ContainerScreen<?> parentGui;

	public GenericInventoryButton(ContainerScreen<?> gui, int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, Button.IPressable onPressIn) {
		super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, 256, 256, onPressIn);

		this.parentGui = gui;
	}

	@Override
	public void func_230431_b_(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

		if (this.parentGui instanceof CreativeScreen) {
			CreativeScreen gui = (CreativeScreen) this.parentGui;
			boolean isInventoryTab = gui.getSelectedTabIndex() == ItemGroup.INVENTORY.getIndex();
			this.field_230693_o_ = isInventoryTab;

			if (!isInventoryTab) {
				return;
			}
		}

		super.func_230431_b_(matrixStack, mouseX, mouseY, partialTicks);
	}

}
