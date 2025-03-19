package com.aizistral.enigmaticlegacy.objects;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Special type of Toast that alerts player when they unlock new Curio slot.
 * @author Integral
 */

@OnlyIn(Dist.CLIENT)
public class SlotUnlockedToast implements Toast {
	private static final ResourceLocation TEXTURE = new ResourceLocation(EnigmaticLegacy.MODID, "textures/gui/enigmatic_toasts.png");
	private long firstDrawTime;
	private ItemStack drawnStack;
	private String identifier;

	public SlotUnlockedToast(ItemStack stack, String id) {
		this.drawnStack = stack;
		this.identifier = id;
	}

	@Override
	public Toast.Visibility render(GuiGraphics graphics, ToastComponent toastGui, long delta) {
		RenderSystem.setShaderTexture(0, TEXTURE);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		Font font = toastGui.getMinecraft().font;

		graphics.blit(TEXTURE, 0, 0, 0, 0, 160, 43);
		graphics.drawString(font, I18n.get("enigmaticlegacy.toasts.slotUnlocked.title",
				I18n.get("enigmaticlegacy.curiotype." + this.identifier)), 7, 7, -11534256);
		graphics.drawString(font, I18n.get("enigmaticlegacy.toasts.slotUnlocked.text1"), 30, 18, -16777216);
		graphics.drawString(font, I18n.get("enigmaticlegacy.toasts.slotUnlocked.text2"), 30, 28, -16777216);

		graphics.renderFakeItem(this.drawnStack, 8, 18);

		//RenderHelper.turnBackOn();
		//GlStateManager._pushMatrix();
		//GlStateManager._scalef(1.6F, 1.6F, 1.0F);
		//GlStateManager._popMatrix();
		return delta - this.firstDrawTime >= 5000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;

	}

}