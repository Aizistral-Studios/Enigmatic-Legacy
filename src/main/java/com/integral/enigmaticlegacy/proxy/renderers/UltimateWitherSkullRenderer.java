package com.integral.enigmaticlegacy.proxy.renderers;

import com.integral.enigmaticlegacy.entities.UltimateWitherSkullEntity;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.GenericHeadModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UltimateWitherSkullRenderer extends EntityRenderer<UltimateWitherSkullEntity> {
   private static final ResourceLocation INVULNERABLE_WITHER_TEXTURES = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
   private static final ResourceLocation WITHER_TEXTURES = new ResourceLocation("textures/entity/wither/wither.png");
   private final GenericHeadModel skeletonHeadModel = new GenericHeadModel();
   private final GenericHeadModel skeletonHeadModelBig = new GenericHeadModel();

   public UltimateWitherSkullRenderer(EntityRendererManager renderManagerIn) {
      super(renderManagerIn);
   }

   private float getRenderYaw(float p_82400_1_, float p_82400_2_, float p_82400_3_) {
      float f;
      for(f = p_82400_2_ - p_82400_1_; f < -180.0F; f += 360.0F) {
         ;
      }

      while(f >= 180.0F) {
         f -= 360.0F;
      }

      return p_82400_1_ + p_82400_3_ * f;
   }

   public void doRender(UltimateWitherSkullEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
      GlStateManager.pushMatrix();
      GlStateManager.disableCull();
      float f = this.getRenderYaw(entity.prevRotationYaw, entity.rotationYaw, partialTicks);
      float f1 = MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch);
      GlStateManager.translatef((float)x, (float)y, (float)z);
      GlStateManager.enableRescaleNormal();
      GlStateManager.scalef(-1.0F, -1.0F, 1.0F);
      GlStateManager.enableAlphaTest();
      this.bindEntityTexture(entity);
      if (this.renderOutlines) {
         GlStateManager.enableColorMaterial();
         GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
      }
      
      if (!entity.isSkullInvulnerable()) {
    	  this.skeletonHeadModel.func_217104_a(0.0F, 0.0F, 0.0F, f, f1, 0.0625F);
      } else {
    	  this.skeletonHeadModelBig.func_217104_a(0.0F, 0.0F, 0.0F, f, f1, 0.0825F);
      }
      
      if (this.renderOutlines) {
         GlStateManager.tearDownSolidRenderingTextureCombine();
         GlStateManager.disableColorMaterial();
      }

      GlStateManager.popMatrix();
      super.doRender(entity, x, y, z, entityYaw, partialTicks);
   }

   protected ResourceLocation getEntityTexture(UltimateWitherSkullEntity entity) {
      return entity.isSkullInvulnerable() ? WITHER_TEXTURES : WITHER_TEXTURES;
   }
}