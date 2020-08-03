package com.integral.enigmaticlegacy.objects;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
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
	public IToast.Visibility func_230444_a_(MatrixStack matrixStack, ToastGui toastGui, long delta) {

		toastGui.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(EnigmaticLegacy.MODID, "textures/gui/enigmatic_toasts.png"));
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		toastGui.func_238474_b_(matrixStack, 0, 0, 0, 0, 160, 43);
		toastGui.getMinecraft().fontRenderer.func_238421_b_(matrixStack, I18n.format("enigmaticlegacy.toasts.slotUnlocked.title", I18n.format("enigmaticlegacy.curiotype." + this.identifier)), 7.0F, 7.0F, -11534256);
		toastGui.getMinecraft().fontRenderer.func_238421_b_(matrixStack, I18n.format("enigmaticlegacy.toasts.slotUnlocked.text1"), 30.0F, 18.0F, -16777216);
		toastGui.getMinecraft().fontRenderer.func_238421_b_(matrixStack, I18n.format("enigmaticlegacy.toasts.slotUnlocked.text2"), 30.0F, 28.0F, -16777216);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.pushMatrix();
		GlStateManager.scalef(1.6F, 1.6F, 1.0F);
		GlStateManager.popMatrix();
		toastGui.getMinecraft().getItemRenderer().renderItemAndEffectIntoGUI((LivingEntity) null, this.drawnStack, 8, 18);
		return delta - this.firstDrawTime >= 5000L ? IToast.Visibility.HIDE : IToast.Visibility.SHOW;

	}

}