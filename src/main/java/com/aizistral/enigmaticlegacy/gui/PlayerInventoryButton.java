package com.aizistral.enigmaticlegacy.gui;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import top.theillusivec4.curios.client.gui.CuriosScreen;

public abstract class PlayerInventoryButton extends ImageButton {
	protected final AbstractContainerScreen<?> parentGui;
	protected final ResourceLocation resourceLocation;
	protected int xTexStart = 0, yTexStart = 0, textureWidth = 0, textureHeight = 0, yDiffTex = 0;
	protected boolean isRecipeBookVisible = false;

	public PlayerInventoryButton(AbstractContainerScreen<?> gui, int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, Button.OnPress onPressIn) {
		super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, 256, 256, onPressIn);
		this.parentGui = gui;

		this.resourceLocation = resourceLocationIn;
		this.xTexStart = xTexStartIn;
		this.yTexStart = yTexStartIn;
		this.textureWidth = 256;
		this.textureHeight = 256;
		this.yDiffTex = yDiffTextIn;
	}

	@Override
	public void renderWidget(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		this.active = true;

		if (this.parentGui instanceof InventoryScreen || this.parentGui instanceof CuriosScreen) {
			boolean lastVisible = this.isRecipeBookVisible;

			if (this.parentGui instanceof InventoryScreen) {
				this.isRecipeBookVisible = ((InventoryScreen)this.parentGui).getRecipeBookComponent().isVisible();
			} else if (this.parentGui instanceof CuriosScreen) {
				this.isRecipeBookVisible = ((CuriosScreen)this.parentGui).getRecipeBookComponent().isVisible();
			}

			if (lastVisible != this.isRecipeBookVisible) {
				Tuple<Integer, Integer> offsets = this.getOffsets(false);
				this.setPosition(this.parentGui.getGuiLeft() + offsets.getA(), this.parentGui.getGuiTop() + offsets.getB());
			}
		} else if (this.parentGui instanceof CreativeModeInventoryScreen) {
			CreativeModeInventoryScreen gui = (CreativeModeInventoryScreen) this.parentGui;
			boolean isInventoryTab = gui.selectedTab == BuiltInRegistries.CREATIVE_MODE_TAB
					.getOrThrow(CreativeModeTabs.INVENTORY);

			if (!isInventoryTab) {
				this.active = false;
				return;
			}
		}

		if (this.beforeRender(graphics, mouseX, mouseY, partialTicks)) {
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderTexture(0, this.resourceLocation);
			int i = this.yTexStart;
			if (this.isHoveredOrFocused()) {
				i += this.yDiffTex;
			}

			RenderSystem.enableDepthTest();
			graphics.blit(this.resourceLocation, this.getX(), this.getY(), this.xTexStart, i, this.width, this.height,
					this.textureWidth, this.textureHeight);
		}
	}

	protected abstract boolean beforeRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks);

	public abstract Tuple<Integer, Integer> getOffsets(boolean creative);

}
