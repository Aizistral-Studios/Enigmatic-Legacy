package com.integral.enigmaticlegacy.objects;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Special type of Toast that alerts player when they unlock new Curio slot.
 * @author Integral
 */

@OnlyIn(Dist.CLIENT)
public class SlotUnlockedToast implements IToast {
	private long firstDrawTime;
	private ItemStack drawnStack;
	private String identifier;

	public SlotUnlockedToast(ItemStack stack, String id) {
		this.drawnStack = stack;
		this.identifier = id;
	}

	@Override
	public IToast.Visibility render(MatrixStack matrixStack, ToastGui toastGui, long delta) {
		toastGui.getMinecraft().getTextureManager().bind(new ResourceLocation(EnigmaticLegacy.MODID, "textures/gui/enigmatic_toasts.png"));
		GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);

		toastGui.blit(matrixStack, 0, 0, 0, 0, 160, 43);
		toastGui.getMinecraft().font.draw(matrixStack, I18n.get("enigmaticlegacy.toasts.slotUnlocked.title", I18n.get("enigmaticlegacy.curiotype." + this.identifier)), 7.0F, 7.0F, -11534256);
		toastGui.getMinecraft().font.draw(matrixStack, I18n.get("enigmaticlegacy.toasts.slotUnlocked.text1"), 30.0F, 18.0F, -16777216);
		toastGui.getMinecraft().font.draw(matrixStack, I18n.get("enigmaticlegacy.toasts.slotUnlocked.text2"), 30.0F, 28.0F, -16777216);
		RenderHelper.turnBackOn();
		GlStateManager._pushMatrix();
		GlStateManager._scalef(1.6F, 1.6F, 1.0F);
		GlStateManager._popMatrix();

		toastGui.getMinecraft().getItemRenderer().renderAndDecorateItem((LivingEntity) null, this.drawnStack, 8, 18);
		return delta - this.firstDrawTime >= 5000L ? IToast.Visibility.HIDE : IToast.Visibility.SHOW;

	}

}