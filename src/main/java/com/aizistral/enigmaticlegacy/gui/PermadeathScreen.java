package com.aizistral.enigmaticlegacy.gui;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PermadeathScreen extends Screen {
	public static PermadeathScreen active = null;
	private final Component reason;
	private MultiLineLabel message = MultiLineLabel.EMPTY;
	private final Screen parent;
	private int textHeight;

	public PermadeathScreen(Screen parent, Component title, Component reason) {
		super(title);
		this.parent = parent;
		this.reason = reason;
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}

	@Override
	protected void init() {
		this.clearWidgets();

		this.message = MultiLineLabel.create(this.font, ((MutableComponent)this.reason).withStyle(ChatFormatting.WHITE), this.width - 50);
		this.textHeight = this.message.getLineCount() * 9;
		this.addRenderableWidget(new Button(this.width / 2 - 100, Math.min(this.height / 2 + this.textHeight / 2 + 9, this.height - 30), 200, 20,
				Component.translatable("gui.enigmaticlegacy.toWorldList"), (p_96002_) -> {
					active = null;
					this.minecraft.setScreen(this.parent);
				}));
	}

	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTick) {
		this.init();

		this.renderBackground(stack);
		drawCenteredString(stack, this.font, this.title, this.width / 2, this.height / 2 - this.textHeight / 2 - 9 * 2, 11184810);
		this.message.renderCentered(stack, this.width / 2, this.height / 2 - this.textHeight / 2);
		super.render(stack, mouseX, mouseY, partialTick);
	}
}
