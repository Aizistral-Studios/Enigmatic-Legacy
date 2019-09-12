package com.integral.enigmaticlegacy.proxy.renderers;

import java.util.Random;

import com.integral.enigmaticlegacy.entities.PermanentItemEntity;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Copy of default ItemRenderer for PermanentItemEntity.
 * @author Integral
 */

@SuppressWarnings({"deprecation", "unused"})
@OnlyIn(Dist.CLIENT)
public class PermanentItemRenderer extends EntityRenderer<PermanentItemEntity> {
   private final net.minecraft.client.renderer.ItemRenderer itemRenderer;
   private final Random random = new Random();

   public PermanentItemRenderer(EntityRendererManager renderManagerIn, net.minecraft.client.renderer.ItemRenderer p_i46167_2_) {
      super(renderManagerIn);
      this.itemRenderer = p_i46167_2_;
      this.shadowSize = 0.15F;
      this.shadowOpaque = 0.75F;
   }

private int transformModelCount(PermanentItemEntity itemIn, double p_177077_2_, double p_177077_4_, double p_177077_6_, float p_177077_8_, IBakedModel p_177077_9_) {
      ItemStack itemstack = itemIn.getItem();
      Item item = itemstack.getItem();
      if (item == null) {
         return 0;
      } else {
         boolean flag = p_177077_9_.isGui3d();
         int i = this.getModelCount(itemstack);
         float f = 0.25F;
         float f1 = shouldBob() ? MathHelper.sin(((float)itemIn.getAge() + p_177077_8_) / 10.0F + itemIn.hoverStart) * 0.1F + 0.1F : 0;
         float f2 = p_177077_9_.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.getY();
         GlStateManager.translatef((float)p_177077_2_, (float)p_177077_4_ + f1 + 0.25F * f2, (float)p_177077_6_);
         if (flag || this.renderManager.options != null) {
            float f3 = (((float)itemIn.getAge() + p_177077_8_) / 20.0F + itemIn.hoverStart) * (180F / (float)Math.PI);
            GlStateManager.rotatef(f3, 0.0F, 1.0F, 0.0F);
         }

         GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
         return i;
      }
   }

   protected int getModelCount(ItemStack stack) {
      int i = 1;
      if (stack.getCount() > 48) {
         i = 5;
      } else if (stack.getCount() > 32) {
         i = 4;
      } else if (stack.getCount() > 16) {
         i = 3;
      } else if (stack.getCount() > 1) {
         i = 2;
      }

      return i;
   }

   public void doRender(PermanentItemEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
      ItemStack itemstack = entity.getItem();
      int i = itemstack.isEmpty() ? 187 : Item.getIdFromItem(itemstack.getItem()) + itemstack.getDamage();
      this.random.setSeed((long)i);
      boolean flag = false;
      if (this.bindEntityTexture(entity)) {
         this.renderManager.textureManager.getTexture(this.getEntityTexture(entity)).setBlurMipmap(false, false);
         flag = true;
      }

      GlStateManager.enableRescaleNormal();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.enableBlend();
      RenderHelper.enableStandardItemLighting();
      GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      GlStateManager.pushMatrix();
      IBakedModel ibakedmodel = this.itemRenderer.getItemModelWithOverrides(itemstack, entity.world, (LivingEntity)null);
      int j = this.transformModelCount(entity, x, y, z, partialTicks, ibakedmodel);
      boolean flag1 = ibakedmodel.isGui3d();
      if (!flag1) {
         float f3 = -0.0F * (float)(j - 1) * 0.5F;
         float f4 = -0.0F * (float)(j - 1) * 0.5F;
         float f5 = -0.09375F * (float)(j - 1) * 0.5F;
         GlStateManager.translatef(f3, f4, f5);
      }

      if (this.renderOutlines) {
         GlStateManager.enableColorMaterial();
         GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
      }

      for(int k = 0; k < j; ++k) {
         if (flag1) {
            GlStateManager.pushMatrix();
            if (k > 0) {
               float f7 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
               float f9 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
               float f6 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
               GlStateManager.translatef(shouldSpreadItems() ? f7 : 0, shouldSpreadItems() ? f9 : 0, f6);
            }

            IBakedModel transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND, false);
            this.itemRenderer.renderItem(itemstack, transformedModel);
            GlStateManager.popMatrix();
         } else {
            GlStateManager.pushMatrix();
            if (k > 0) {
               float f8 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
               float f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
               GlStateManager.translatef(f8, f10, 0.0F);
            }

            IBakedModel transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND, false);
            this.itemRenderer.renderItem(itemstack, transformedModel);
            GlStateManager.popMatrix();
            GlStateManager.translatef(0.0F, 0.0F, 0.09375F);
         }
      }

      if (this.renderOutlines) {
         GlStateManager.tearDownSolidRenderingTextureCombine();
         GlStateManager.disableColorMaterial();
      }

      GlStateManager.popMatrix();
      GlStateManager.disableRescaleNormal();
      GlStateManager.disableBlend();
      this.bindEntityTexture(entity);
      if (flag) {
         this.renderManager.textureManager.getTexture(this.getEntityTexture(entity)).restoreLastBlurMipmap();
      }

      super.doRender(entity, x, y, z, entityYaw, partialTicks);
   }

   protected ResourceLocation getEntityTexture(PermanentItemEntity entity) {
      return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
   }
   
   /*==================================== FORGE START ===========================================*/

   /**
    * @return If items should spread out when rendered in 3D
    */
   public boolean shouldSpreadItems() {
      return true;
   }

   /**
    * @return If items should have a bob effect
    */
   public boolean shouldBob() {
      return true;
   }
   /*==================================== FORGE END =============================================*/
}