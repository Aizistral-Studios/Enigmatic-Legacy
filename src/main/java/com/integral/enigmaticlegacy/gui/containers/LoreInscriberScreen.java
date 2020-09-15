package com.integral.enigmaticlegacy.gui.containers;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.JsonConfigHandler;
import com.integral.enigmaticlegacy.packets.server.PacketInkwellField;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.AbstractRepairContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CRenameItemPacket;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LoreInscriberScreen extends ContainerScreen<LoreInscriberContainer> implements IContainerListener {
	private ResourceLocation field_238817_A_;
	private static final ResourceLocation ANVIL_RESOURCE = new ResourceLocation(EnigmaticLegacy.MODID, "textures/gui/inkwell_gui.png");
	private TextFieldWidget nameField;

	public LoreInscriberScreen(LoreInscriberContainer p_i51103_1_, PlayerInventory p_i51103_2_, ITextComponent p_i51103_3_) {
		this(p_i51103_1_, p_i51103_2_, p_i51103_3_, LoreInscriberScreen.ANVIL_RESOURCE);
		this.field_238742_p_ = 60;
	}

	private LoreInscriberScreen(LoreInscriberContainer p_i232291_1_, PlayerInventory p_i232291_2_, ITextComponent p_i232291_3_, ResourceLocation p_i232291_4_) {
		super(p_i232291_1_, p_i232291_2_, p_i232291_3_);
		this.field_238817_A_ = p_i232291_4_;
	}

	protected void func_230453_j_() {
		this.field_230706_i_.keyboardListener.enableRepeatEvents(true);
		int i = (this.field_230708_k_ - this.xSize) / 2;
		int j = (this.field_230709_l_ - this.ySize) / 2;
		this.nameField = new TextFieldWidget(this.field_230712_o_, i + 55, j + 30, 95, 12, new TranslationTextComponent("container.repair"));
		this.nameField.setCanLoseFocus(false);
		this.nameField.setTextColor(-1);
		this.nameField.setDisabledTextColour(-1);
		this.nameField.setEnableBackgroundDrawing(false);
		this.nameField.setMaxStringLength(128);
		this.nameField.setResponder(this::renameResponder);
		this.field_230705_e_.add(this.nameField);
		this.setFocusedDefault(this.nameField);
	}

	@Override
	protected void func_231160_c_() {
		super.func_231160_c_();
		this.func_230453_j_();
		this.container.addListener(this);
	}

	@Override
	public void func_231164_f_() {
		super.func_231164_f_();
		this.container.removeListener(this);
		this.field_230706_i_.keyboardListener.enableRepeatEvents(false);
	}

	@Override
	public void func_230430_a_(MatrixStack matrixStack, int x, int y, float partialTicksIGuess) {
		this.func_230446_a_(matrixStack);
		super.func_230430_a_(matrixStack, x, y, partialTicksIGuess);
		RenderSystem.disableBlend();
		this.func_230452_b_(matrixStack, x, y, partialTicksIGuess);
		this.func_230459_a_(matrixStack, x, y);
	}

	@Override
	protected void func_230450_a_(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.field_230706_i_.getTextureManager().bindTexture(this.field_238817_A_);
		int i = (this.field_230708_k_ - this.xSize) / 2;
		int j = (this.field_230709_l_ - this.ySize) / 2;
		this.func_238474_b_(p_230450_1_, i, j, 0, 0, this.xSize, this.ySize);
		this.func_238474_b_(p_230450_1_, i + 52, j + 26, 0, this.ySize + (this.container.getSlot(0).getHasStack() ? 0 : 16), 102, 16);
		if (this.container.getSlot(0).getHasStack() && !this.container.getSlot(1).getHasStack()) {
			this.func_238474_b_(p_230450_1_, i + 71, j + 49, this.xSize, 0, 28, 21);
		}

	}

	/**
	* update the crafting window inventory with the items in the list
	*/
	@Override
	public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList) {
		this.sendSlotContents(containerToSend, 0, containerToSend.getSlot(0).getStack());
	}

	/**
	* Sends two ints to the client-side Container. Used for furnace burning time, smelting progress, brewing progress,
	* and enchanting level. Normally the first int identifies which variable to update, and the second contains the new
	* value. Both are truncated to shorts in non-local SMP.
	*/
	@Override
	public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue) {
	}

	@Override
	public void func_231152_a_(Minecraft p_231152_1_, int p_231152_2_, int p_231152_3_) {
		String s = this.nameField.getText();
		this.func_231158_b_(p_231152_1_, p_231152_2_, p_231152_3_);
		this.nameField.setText(s);
	}

	@Override
	public boolean func_231046_a_(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
		if (p_231046_1_ == 256) {
			this.field_230706_i_.player.closeScreen();
		}

		return !this.nameField.func_231046_a_(p_231046_1_, p_231046_2_, p_231046_3_) && !this.nameField.canWrite() ? super.func_231046_a_(p_231046_1_, p_231046_2_, p_231046_3_) : true;
	}

	private void renameResponder(String input) {
		if (!input.isEmpty()) {
			String s = input;
			Slot slot = this.container.getSlot(0);
			if (slot != null && slot.getHasStack() && !slot.getStack().hasDisplayName() && input.equals(slot.getStack().getDisplayName().getString())) {
				s = "";
			}

			this.container.updateItemName(s);
			EnigmaticLegacy.packetInstance.sendToServer(new PacketInkwellField(s));
		}
	}

	@Override
	protected void func_230451_b_(MatrixStack matrixStack, int x, int y) {
		this.field_230712_o_.func_238422_b_(matrixStack, this.field_230704_d_, 52F, 13F, 4210752);
		//this.field_230712_o_.func_238422_b_(matrixStack, this.playerInventory.getDisplayName(), JsonConfigHandler.getFloat("playerInvX"), JsonConfigHandler.getFloat("playerInvY"), 4210752);

		RenderSystem.disableBlend();
		/*
		int i = this.container.getMaximumCost();
		if (i > 0) {
			int j = 8453920;
			boolean flag = true;
			String s = I18n.format("container.repair.cost", i);
			if (i >= 40 && !this.field_230706_i_.player.abilities.isCreativeMode) {
				s = I18n.format("container.repair.expensive");
				j = 16736352;
			} else if (!this.container.getSlot(2).getHasStack()) {
				flag = false;
			} else if (!this.container.getSlot(2).canTakeStack(this.playerInventory.player)) {
				j = 16736352;
			}

			if (flag) {
				int k = this.xSize - 8 - this.field_230712_o_.getStringWidth(s) - 2;
				int l = 69;
				AbstractGui.func_238467_a_(p_230451_1_, k - 2, 67, this.xSize - 8, 79, 1325400064);
				this.field_230712_o_.func_238405_a_(p_230451_1_, s, k, 69.0F, j);
			}

		}
		*/
	}

	public void func_230452_b_(MatrixStack p_230452_1_, int p_230452_2_, int p_230452_3_, float p_230452_4_) {
		this.nameField.func_230430_a_(p_230452_1_, p_230452_2_, p_230452_3_, p_230452_4_);
	}

	/**
	* Sends the contents of an inventory slot to the client-side Container. This doesn't have to match the actual
	* contents of that slot.
	*/
	@Override
	public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
		if (slotInd == 0) {
			this.nameField.setText(stack.isEmpty() ? "" : stack.getDisplayName().getString());
			this.nameField.setEnabled(!stack.isEmpty());
			this.func_231035_a_(this.nameField);
		}

	}
}
