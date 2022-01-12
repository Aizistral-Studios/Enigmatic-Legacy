package com.integral.enigmaticlegacy.objects;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Special type of Toast that alerts player when they unlock new Curio slot.
 * @author Integral
 */

@OnlyIn(Dist.CLIENT)
public class SlotUnlockedToast implements Toast {
	private long firstDrawTime;
	private ItemStack drawnStack;
	private String identifier;

	public SlotUnlockedToast(ItemStack stack, String id) {
		this.drawnStack = stack;
		this.identifier = id;
	}

	@Override
	public Toast.Visibility render(PoseStack PoseStack, ToastComponent toastGui, long delta) {
		toastGui.getMinecraft().getTextureManager().bindForSetup(new ResourceLocation(EnigmaticLegacy.MODID, "textures/gui/enigmatic_toasts.png"));
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		toastGui.blit(PoseStack, 0, 0, 0, 0, 160, 43);
		toastGui.getMinecraft().font.draw(PoseStack, I18n.get("enigmaticlegacy.toasts.slotUnlocked.title", I18n.get("enigmaticlegacy.curiotype." + this.identifier)), 7.0F, 7.0F, -11534256);
		toastGui.getMinecraft().font.draw(PoseStack, I18n.get("enigmaticlegacy.toasts.slotUnlocked.text1"), 30.0F, 18.0F, -16777216);
		toastGui.getMinecraft().font.draw(PoseStack, I18n.get("enigmaticlegacy.toasts.slotUnlocked.text2"), 30.0F, 28.0F, -16777216);

		//RenderHelper.turnBackOn();
		//GlStateManager._pushMatrix();
		//GlStateManager._scalef(1.6F, 1.6F, 1.0F);
		//GlStateManager._popMatrix();

		toastGui.getMinecraft().getItemRenderer().renderAndDecorateItem(this.drawnStack, 8, 18);
		return delta - this.firstDrawTime >= 5000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;

	}

}