package com.integral.enigmaticlegacy.objects;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.LivingEntity;
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
	public Visibility render(PoseStack ms, ToastComponent toastGui, long delta) {
		Minecraft mc = Minecraft.getInstance();
		mc.getTextureManager().bindForSetup(TEXTURE);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		toastGui.blit(ms, 0, 0, 0, 32, 160, 32);

		toastGui.getMinecraft().font.draw(ms, I18n.get("enigmaticlegacy.toasts.revelationTome.title", this.xpPoints), 30, 7, -11534256);
		toastGui.getMinecraft().font.draw(ms, I18n.get("enigmaticlegacy.toasts.revelationTome.text", this.revelationPoints), 30, 17, -16777216);

		toastGui.getMinecraft().getItemRenderer().renderAndDecorateItem(this.tome, 8, 8);

		return delta >= 5000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
	}

}
