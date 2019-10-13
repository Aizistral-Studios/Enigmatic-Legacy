package com.integral.enigmaticlegacy.helpers;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
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

   public IToast.Visibility draw(ToastGui toastGui, long delta) {
	   
         toastGui.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(EnigmaticLegacy.MODID, "textures/gui/enigmatic_toasts.png"));
         GlStateManager.color3f(1.0F, 1.0F, 1.0F);
         toastGui.blit(0, 0, 0, 0, 160, 43);
         toastGui.getMinecraft().fontRenderer.drawString(I18n.format("enigmaticlegacy.toasts.slotUnlocked.title", I18n.format("enigmaticlegacy.curiotype." + identifier)), 7.0F, 7.0F, -11534256);
         toastGui.getMinecraft().fontRenderer.drawString(I18n.format("enigmaticlegacy.toasts.slotUnlocked.text1"), 30.0F, 18.0F, -16777216);
         toastGui.getMinecraft().fontRenderer.drawString(I18n.format("enigmaticlegacy.toasts.slotUnlocked.text2"), 30.0F, 28.0F, -16777216);
         RenderHelper.enableGUIStandardItemLighting();
         GlStateManager.pushMatrix();
         GlStateManager.scalef(1.6F, 1.6F, 1.0F);
         GlStateManager.popMatrix();
         toastGui.getMinecraft().getItemRenderer().renderItemAndEffectIntoGUI((LivingEntity)null, drawnStack, 8, 18);
         return delta - this.firstDrawTime >= 5000L ? IToast.Visibility.HIDE : IToast.Visibility.SHOW;
      
   }

   
}