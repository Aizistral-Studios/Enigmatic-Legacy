package com.integral.enigmaticlegacy.gui;

import javax.annotation.Nonnull;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.config.JsonConfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import top.theillusivec4.curios.client.gui.CuriosScreen;

public class EnderChestInventoryButton extends ImageButton {

	private final ContainerScreen<?> parentGui;
	private boolean isRecipeBookVisible = false;

	public EnderChestInventoryButton(ContainerScreen<?> gui, int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, Button.IPressable onPressIn) {
		super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, 256, 256, onPressIn);

		this.parentGui = gui;
	}

	@Override
	public void renderButton(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.active = true;

		if (this.parentGui instanceof InventoryScreen || this.parentGui instanceof CuriosScreen) {
			boolean lastVisible = this.isRecipeBookVisible;

			if (this.parentGui instanceof InventoryScreen) {
				this.isRecipeBookVisible = ((InventoryScreen)this.parentGui).getRecipeGui().isVisible();
			} else if (this.parentGui instanceof CuriosScreen) {
				this.isRecipeBookVisible = ((CuriosScreen)this.parentGui).getRecipeGui().isVisible();
			}

			if (lastVisible != this.isRecipeBookVisible) {
				Tuple<Integer, Integer> offsets = EnderChestInventoryButton.getOffsets(false);
				this.setPosition(this.parentGui.getGuiLeft() + offsets.getA(), (this.parentGui.height / 2 - 22) + offsets.getB());
			}

		} else if (this.parentGui instanceof CreativeScreen) {
			CreativeScreen gui = (CreativeScreen) this.parentGui;
			boolean isInventoryTab = gui.getSelectedTabIndex() == ItemGroup.INVENTORY.getIndex();

			if (!isInventoryTab) {
				this.active = false;
				return;
			}
		}

		boolean hasRing = SuperpositionHandler.hasCurio(Minecraft.getInstance().player, EnigmaticLegacy.enderRing) || SuperpositionHandler.hasCurio(Minecraft.getInstance().player, EnigmaticLegacy.cursedRing);

		if (!hasRing || !ConfigHandler.ECHEST_BUTTON_ENABLED.getValue()) {
			this.active = false;
			return;
		}

		super.renderButton(matrixStack, mouseX, mouseY, partialTicks);
	}

	public static Tuple<Integer, Integer> getOffsets(boolean creative) {
		int x = creative ? 170 + ConfigHandler.ECHEST_BUTTON_OFFSET_X_CREATIVE.getValue() : 150 + ConfigHandler.ECHEST_BUTTON_OFFSET_X.getValue();
		int y = creative ? -41 + ConfigHandler.ECHEST_BUTTON_OFFSET_Y_CREATIVE.getValue() : 0 + ConfigHandler.ECHEST_BUTTON_OFFSET_Y.getValue();

		return new Tuple<>(x, y);
	}

}
