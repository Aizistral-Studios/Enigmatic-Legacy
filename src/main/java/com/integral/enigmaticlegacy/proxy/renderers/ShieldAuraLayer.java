package com.integral.enigmaticlegacy.proxy.renderers;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.items.EtheriumArmor;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShieldAuraLayer extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {
   private static final ResourceLocation WITHER_ARMOR = new ResourceLocation(EnigmaticLegacy.MODID, "textures/models/misc/ultimate_wither_armor.png");
   private final PlayerModel<AbstractClientPlayerEntity> witherModel = new PlayerModel<AbstractClientPlayerEntity>(0.5F, false);
   private boolean first;
   
   public ShieldAuraLayer(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> p_i50915_1_, boolean first) {
      super(p_i50915_1_);
      
      this.first = first;
   }
   
   private BipedModel.ArmPose func_217766_a(AbstractClientPlayerEntity p_217766_1_, ItemStack p_217766_2_, ItemStack p_217766_3_, Hand p_217766_4_) {
	      BipedModel.ArmPose bipedmodel$armpose = BipedModel.ArmPose.EMPTY;
	      ItemStack itemstack = p_217766_4_ == Hand.MAIN_HAND ? p_217766_2_ : p_217766_3_;
	      if (!itemstack.isEmpty()) {
	         bipedmodel$armpose = BipedModel.ArmPose.ITEM;
	         if (p_217766_1_.getItemInUseCount() > 0) {
	            UseAction useaction = itemstack.getUseAction();
	            if (useaction == UseAction.BLOCK) {
	               bipedmodel$armpose = BipedModel.ArmPose.BLOCK;
	            } else if (useaction == UseAction.BOW) {
	               bipedmodel$armpose = BipedModel.ArmPose.BOW_AND_ARROW;
	            } else if (useaction == UseAction.SPEAR) {
	               bipedmodel$armpose = BipedModel.ArmPose.THROW_SPEAR;
	            } else if (useaction == UseAction.CROSSBOW && p_217766_4_ == p_217766_1_.getActiveHand()) {
	               bipedmodel$armpose = BipedModel.ArmPose.CROSSBOW_CHARGE;
	            }
	         } else {
	            boolean flag3 = p_217766_2_.getItem() == Items.CROSSBOW;
	            boolean flag = CrossbowItem.isCharged(p_217766_2_);
	            boolean flag1 = p_217766_3_.getItem() == Items.CROSSBOW;
	            boolean flag2 = CrossbowItem.isCharged(p_217766_3_);
	            if (flag3 && flag) {
	               bipedmodel$armpose = BipedModel.ArmPose.CROSSBOW_HOLD;
	            }

	            if (flag1 && flag2 && p_217766_2_.getItem().getUseAction(p_217766_2_) == UseAction.NONE) {
	               bipedmodel$armpose = BipedModel.ArmPose.CROSSBOW_HOLD;
	            }
	         }
	      }

	      return bipedmodel$armpose;
	   }

   public void render(AbstractClientPlayerEntity entityIn, float p_212842_2_, float p_212842_3_, float p_212842_4_, float p_212842_5_, float p_212842_6_, float p_212842_7_, float p_212842_8_) {
	   if (EtheriumArmor.hasShield(entityIn)) {
         GlStateManager.depthMask(!entityIn.isInvisible());
         this.bindTexture(WITHER_ARMOR);
         GlStateManager.matrixMode(5890);
         GlStateManager.loadIdentity();
         
         float f;
         
         if (!Minecraft.getInstance().isGamePaused())
         if (entityIn.getDisplayName().equals(Minecraft.getInstance().player.getDisplayName()))
        	 p_212842_4_ = Minecraft.getInstance().getRenderPartialTicks();
         
         if (this.first)
        	 f = (float)entityIn.ticksExisted + p_212842_4_;
         else 
        	 f = - ((float)entityIn.ticksExisted + p_212842_4_);
         
         float f1 = MathHelper.cos(f * 0.02F) * 2.0F;
         float f2 = f * 0.01F;
         GlStateManager.translatef(f1, f2, 0.0F);
         GlStateManager.matrixMode(5888);
         GlStateManager.enableBlend();
         float f3 = 0.5F;
         GlStateManager.color4f(0.5F, 0.5F, 0.5F, 1.0F);
         GlStateManager.disableLighting();
         GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
         this.witherModel.setLivingAnimations(entityIn, p_212842_2_, p_212842_3_, p_212842_4_);
         
         
         if (entityIn.isSpectator()) {
        	 witherModel.setVisible(false);
        	 witherModel.bipedHead.showModel = true;
        	 witherModel.bipedHeadwear.showModel = true;
          } else {
             ItemStack itemstack = entityIn.getHeldItemMainhand();
             ItemStack itemstack1 = entityIn.getHeldItemOffhand();
             witherModel.setVisible(true);
             witherModel.bipedHeadwear.showModel = entityIn.isWearing(PlayerModelPart.HAT);
             witherModel.bipedBodyWear.showModel = entityIn.isWearing(PlayerModelPart.JACKET);
             witherModel.bipedLeftLegwear.showModel = entityIn.isWearing(PlayerModelPart.LEFT_PANTS_LEG);
             witherModel.bipedRightLegwear.showModel = entityIn.isWearing(PlayerModelPart.RIGHT_PANTS_LEG);
             witherModel.bipedLeftArmwear.showModel = entityIn.isWearing(PlayerModelPart.LEFT_SLEEVE);
             witherModel.bipedRightArmwear.showModel = entityIn.isWearing(PlayerModelPart.RIGHT_SLEEVE);
             witherModel.isSneak = entityIn.shouldRenderSneaking();
             BipedModel.ArmPose bipedmodel$armpose = this.func_217766_a(entityIn, itemstack, itemstack1, Hand.MAIN_HAND);
             BipedModel.ArmPose bipedmodel$armpose1 = this.func_217766_a(entityIn, itemstack, itemstack1, Hand.OFF_HAND);
             if (entityIn.getPrimaryHand() == HandSide.RIGHT) {
            	 witherModel.rightArmPose = bipedmodel$armpose;
            	 witherModel.leftArmPose = bipedmodel$armpose1;
             } else {
            	 witherModel.rightArmPose = bipedmodel$armpose1;
            	 witherModel.leftArmPose = bipedmodel$armpose;
             }
          }
         
         this.getEntityModel().setModelAttributes(this.witherModel);
         GameRenderer gamerenderer = Minecraft.getInstance().gameRenderer;
         gamerenderer.setupFogColor(true);
         this.witherModel.render(entityIn, p_212842_2_, p_212842_3_, p_212842_5_, p_212842_6_, p_212842_7_, p_212842_8_);
         gamerenderer.setupFogColor(false);
         GlStateManager.matrixMode(5890);
         GlStateManager.loadIdentity();
         GlStateManager.matrixMode(5888);
         GlStateManager.enableLighting();
         GlStateManager.disableBlend();
         GlStateManager.depthMask(true);
      }
   }

   public boolean shouldCombineTextures() {
      return false;
   }
}