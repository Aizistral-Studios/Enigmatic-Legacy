package com.aizistral.enigmaticlegacy.objects;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;

public class RevelationTomeToast implements Toast {
	private final ItemStack tome;
	private final int xpPoints;
	private final int revelationPoints;

	public RevelationTomeToast(ItemStack tome, int xpPoints, int revelationPoints) {
		this.tome = tome;
		this.xpPoints = xpPoints;
		this.revelationPoints = revelationPoints;
	}

	@Nonnull
	@Override
	@SuppressWarnings("resource")
	public Visibility render(GuiGraphics graphics, ToastComponent toastGui, long delta) {
		Minecraft mc = Minecraft.getInstance();
		RenderSystem.setShaderTexture(0, TEXTURE);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		graphics.blit(TEXTURE, 0, 0, 0, 32, 160, 32);

		Font font = toastGui.getMinecraft().font;

		graphics.drawString(font, I18n.get("enigmaticlegacy.toasts.revelationTome.title", this.xpPoints), 30, 7, -11534256);
		graphics.drawString(font, I18n.get("enigmaticlegacy.toasts.revelationTome.text", this.revelationPoints), 30, 17, -16777216);
		graphics.renderFakeItem(this.tome, 8, 8);

		return delta >= 5000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
	}

}
