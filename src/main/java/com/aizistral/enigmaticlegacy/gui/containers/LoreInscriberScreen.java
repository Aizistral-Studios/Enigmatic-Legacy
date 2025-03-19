package com.aizistral.enigmaticlegacy.gui.containers;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.packets.server.PacketInkwellField;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CommandBlockEditScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LoreInscriberScreen extends AbstractContainerScreen<LoreInscriberContainer> implements ContainerListener {
	private ResourceLocation guiTexture;
	private static final ResourceLocation ANVIL_RESOURCE = new ResourceLocation(EnigmaticLegacy.MODID, "textures/gui/inkwell_gui.png");
	private EditBox nameField;

	public LoreInscriberScreen(LoreInscriberContainer container, Inventory Inventory, Component title) {
		this(container, Inventory, title, LoreInscriberScreen.ANVIL_RESOURCE);
		this.titleLabelX = 60;
	}

	private LoreInscriberScreen(LoreInscriberContainer container, Inventory Inventory, Component title, ResourceLocation guiTexture) {
		super(container, Inventory, title);
		this.guiTexture = guiTexture;
	}

	protected void initFields() {
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.nameField = new EditBox(this.font, i + 55, j + 30, 95, 12, Component.translatable("container.repair"));
		this.nameField.setCanLoseFocus(false);
		this.nameField.setTextColor(-1);
		this.nameField.setTextColorUneditable(-1);
		this.nameField.setBordered(false);
		this.nameField.setMaxLength(128);
		this.nameField.setResponder(this::renameResponder);
		this.addWidget(this.nameField);
		this.setInitialFocus(this.nameField);
	}

	@Override
	protected void init() {
		super.init();
		this.initFields();
		this.menu.addSlotListener(this);
	}

	@Override
	public void removed() {
		super.removed();
		this.menu.removeSlotListener(this);
	}

	@Override
	public void render(GuiGraphics graphics, int x, int y, float partialTicksIGuess) {
		this.renderBackground(graphics);
		super.render(graphics, x, y, partialTicksIGuess);
		RenderSystem.disableBlend();
		this.renderNameField(graphics, x, y, partialTicksIGuess);
		this.renderTooltip(graphics, x, y);
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
		RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, this.guiTexture);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		graphics.blit(this.guiTexture, i, j, 0, 0, this.imageWidth, this.imageHeight);
		graphics.blit(this.guiTexture, i + 52, j + 26, 0, this.imageHeight + (this.menu.getSlot(0).hasItem() ? 0 : 16), 102, 16);
		if (this.menu.getSlot(0).hasItem() && !this.menu.getSlot(1).hasItem()) {
			graphics.blit(this.guiTexture, i + 71, j + 49, this.imageWidth, 0, 28, 21);
		}
	}

	@Override
	public void resize(Minecraft minecraft, int width, int height) {
		String s = this.nameField.getValue();
		this.init(minecraft, width, height);
		this.nameField.setValue(s);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == 256) {
			this.minecraft.player.closeContainer();
		}

		return !this.nameField.keyPressed(keyCode, scanCode, modifiers) && !this.nameField.canConsumeInput() ? super.keyPressed(keyCode, scanCode, modifiers) : true;
	}

	private void renameResponder(String input) {
		if (!input.isEmpty()) {
			String s = input;
			Slot slot = this.menu.getSlot(0);
			if (slot != null && slot.hasItem() && !slot.getItem().hasCustomHoverName() && input.equals(slot.getItem().getHoverName().getString())) {
				s = "";
			}

			this.menu.updateItemName(s);
			EnigmaticLegacy.packetInstance.sendToServer(new PacketInkwellField(s));
		}
	}

	@Override
	protected void renderLabels(GuiGraphics graphics, int x, int y) {
		graphics.drawString(this.font, this.title, 52, 13, 4210752);
		//this.font.draw(PoseStack, this.Inventory.getDisplayName(), JsonConfigHandler.getFloat("playerInvX"), JsonConfigHandler.getFloat("playerInvY"), 4210752);

		RenderSystem.disableBlend();
		/*
		int i = this.container.getMaximumCost();
		if (i > 0) {
			int j = 8453920;
			boolean flag = true;
			String s = I18n.format("container.repair.cost", i);
			if (i >= 40 && !this.minecraft.player.getAbilities().isCreativeMode) {
				s = I18n.format("container.repair.expensive");
				j = 16736352;
			} else if (!this.container.getSlot(2).getHasStack()) {
				flag = false;
			} else if (!this.container.getSlot(2).canTakeStack(this.Inventory.player)) {
				j = 16736352;
			}

			if (flag) {
				int k = this.xSize - 8 - this.font.getStringWidth(s) - 2;
				int l = 69;
				GuiComponent.fill(PoseStack, k - 2, 67, this.xSize - 8, 79, 1325400064);
				this.font.drawStringWithShadow(PoseStack, s, k, 69.0F, j);
			}

		}
		 */
	}

	public void renderNameField(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		this.nameField.render(graphics, mouseX, mouseY, partialTicks);
	}

	/**
	 * Sends the contents of an inventory slot to the client-side Container. This doesn't have to match the actual
	 * contents of that slot.
	 */
	@Override
	public void slotChanged(AbstractContainerMenu containerToSend, int slotInd, ItemStack stack) {
		if (slotInd == 0) {
			this.nameField.setValue(stack.isEmpty() ? "" : stack.getHoverName().getString());
			this.nameField.setEditable(!stack.isEmpty());
			this.setFocused(this.nameField);
		}

	}

	@Override
	public void dataChanged(AbstractContainerMenu p_150524_, int p_150525_, int p_150526_) {
		// NO-OP
	}

}
