package com.integral.enigmaticlegacy.proxy.renderers;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
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

	public ShieldAuraLayer(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> renderer) {
		super(renderer);
	}

	@Override
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

		if (EnigmaticLegacy.etheriumChestplate.hasShield(entitylivingbaseIn)) {
			float f;

			if (!Minecraft.getInstance().isGamePaused())
				if (entitylivingbaseIn.getDisplayName().equals(Minecraft.getInstance().player.getDisplayName()))
					partialTicks = Minecraft.getInstance().getRenderPartialTicks();

			f = entitylivingbaseIn.ticksExisted + partialTicks;

			PlayerModel<AbstractClientPlayerEntity> entitymodel = this.func_225635_b_();
			entitymodel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);

			if (entitylivingbaseIn.isSpectator()) {
				this.witherModel.setVisible(false);
				this.witherModel.bipedHead.showModel = true;
				this.witherModel.bipedHeadwear.showModel = true;
			} else {
				ItemStack itemstack = entitylivingbaseIn.getHeldItemMainhand();
				ItemStack itemstack1 = entitylivingbaseIn.getHeldItemOffhand();
				this.witherModel.setVisible(true);

				/*
				this.witherModel.bipedHeadwear.showModel = false;
				this.witherModel.bipedBodyWear.showModel = false;
				this.witherModel.bipedLeftLegwear.showModel = false;
				this.witherModel.bipedRightLegwear.showModel = false;
				this.witherModel.bipedLeftArmwear.showModel = false;
				this.witherModel.bipedRightArmwear.showModel = false;
				*/

				this.witherModel.isSneak = entitylivingbaseIn.isShiftKeyDown();
				BipedModel.ArmPose bipedmodel$armpose = this.func_217766_a(entitylivingbaseIn, itemstack, itemstack1, Hand.MAIN_HAND);
				BipedModel.ArmPose bipedmodel$armpose1 = this.func_217766_a(entitylivingbaseIn, itemstack, itemstack1, Hand.OFF_HAND);
				if (entitylivingbaseIn.getPrimaryHand() == HandSide.RIGHT) {
					this.witherModel.rightArmPose = bipedmodel$armpose;
					this.witherModel.leftArmPose = bipedmodel$armpose1;
				} else {
					this.witherModel.rightArmPose = bipedmodel$armpose1;
					this.witherModel.leftArmPose = bipedmodel$armpose;
				}
			}

			this.getEntityModel().copyModelAttributesTo(entitymodel);
			IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEnergySwirl(ShieldAuraLayer.func_225633_a_(), ShieldAuraLayer.func_225634_a_(f), f * 0.01F));
			entitymodel.setRotationAngles(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			entitymodel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
		}

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

	protected static float func_225634_a_(float p_225634_1_) {
		return MathHelper.cos(p_225634_1_ * 0.02F) * 2.0F;
	}

	protected static ResourceLocation func_225633_a_() {
		return ShieldAuraLayer.WITHER_ARMOR;
	}

	protected PlayerModel<AbstractClientPlayerEntity> func_225635_b_() {
		return this.witherModel;
	}
}