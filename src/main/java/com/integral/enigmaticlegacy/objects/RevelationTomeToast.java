package com.integral.enigmaticlegacy.objects;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class RevelationTomeToast implements IToast {

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
	public Visibility func_230444_a_(MatrixStack ms, ToastGui toastGui, long delta) {
		Minecraft mc = Minecraft.getInstance();
		mc.getTextureManager().bindTexture(TEXTURE_TOASTS);
		RenderSystem.color3f(1.0F, 1.0F, 1.0F);
		toastGui.blit(ms, 0, 0, 0, 32, 160, 32);

		toastGui.getMinecraft().fontRenderer.drawString(ms, I18n.format("enigmaticlegacy.toasts.revelationTome.title", this.xpPoints), 30, 7, -11534256);
		toastGui.getMinecraft().fontRenderer.drawString(ms, I18n.format("enigmaticlegacy.toasts.revelationTome.text", this.revelationPoints), 30, 17, -16777216);

		toastGui.getMinecraft().getItemRenderer().renderItemAndEffectIntoGUI((LivingEntity) null, this.tome, 8, 8);

		return delta >= 5000L ? IToast.Visibility.HIDE : IToast.Visibility.SHOW;
	}

}
