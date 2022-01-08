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

public class EnderChestInventoryButton extends ImageButton {

	private final AbstractContainerScreen<?> parentGui;
	private boolean isRecipeBookVisible = false;

	public EnderChestInventoryButton(AbstractContainerScreen<?> gui, int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, Button.OnPress onPressIn) {
		super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, 256, 256, onPressIn);

		this.parentGui = gui;
	}

	@Override
	public void renderButton(@Nonnull PoseStack PoseStack, int mouseX, int mouseY, float partialTicks) {
		this.active = true;

		if (this.parentGui instanceof InventoryScreen || this.parentGui instanceof CuriosScreen) {
			boolean lastVisible = this.isRecipeBookVisible;

			if (this.parentGui instanceof InventoryScreen) {
				this.isRecipeBookVisible = ((InventoryScreen)this.parentGui).getRecipeBookComponent().isVisible();
			} else if (this.parentGui instanceof CuriosScreen) {
				this.isRecipeBookVisible = ((CuriosScreen)this.parentGui).getRecipeBookComponent().isVisible();
			}

			if (lastVisible != this.isRecipeBookVisible) {
				Tuple<Integer, Integer> offsets = EnderChestInventoryButton.getOffsets(false);
				this.setPosition(this.parentGui.getGuiLeft() + offsets.getA(), (this.parentGui.height / 2 - 22) + offsets.getB());
			}

		} else if (this.parentGui instanceof CreativeModeInventoryScreen) {
			CreativeModeInventoryScreen gui = (CreativeModeInventoryScreen) this.parentGui;
			boolean isInventoryTab = gui.getSelectedTab() == CreativeModeTab.TAB_INVENTORY.getId();

			if (!isInventoryTab) {
				this.active = false;
				return;
			}
		}

		boolean hasRing = SuperpositionHandler.hasCurio(Minecraft.getInstance().player, EnigmaticLegacy.enderRing) || SuperpositionHandler.hasCurio(Minecraft.getInstance().player, EnigmaticLegacy.cursedRing);

		if (!hasRing || !EnderRing.inventoryButtonEnabled.getValue()) {
			this.active = false;
			return;
		}

		super.renderButton(PoseStack, mouseX, mouseY, partialTicks);
	}

	public static Tuple<Integer, Integer> getOffsets(boolean creative) {
		int x = creative ? 170 + EnderRing.buttonOffsetXCreative.getValue() : 150 + EnderRing.buttonOffsetX.getValue();
		int y = creative ? 5 + EnderRing.buttonOffsetYCreative.getValue() : 61 + EnderRing.buttonOffsetY.getValue();

		return new Tuple<>(x, y);
	}

}
