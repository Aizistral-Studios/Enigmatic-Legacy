package com.aizistral.etherium.client;

import com.aizistral.etherium.items.EtheriumArmor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShieldAuraLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
	private static final ResourceLocation WITHER_ARMOR = new ResourceLocation("enigmaticlegacy", "textures/models/misc/ultimate_wither_armor.png");
	private final PlayerModel<AbstractClientPlayer> witherModel;

	public ShieldAuraLayer(LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer, EntityModelSet modelSet) {
		super(renderer);
		this.witherModel = new PlayerModel<AbstractClientPlayer>(modelSet.bakeLayer(ModelLayers.PLAYER), false);
	}

	@Override
	public void render(PoseStack poseStackIn, MultiBufferSource bufferIn, int packedLightIn, AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (EtheriumArmor.hasShield(entitylivingbaseIn)) {
			float f;

			if (!Minecraft.getInstance().isPaused())
				if (entitylivingbaseIn.getDisplayName().equals(Minecraft.getInstance().player.getDisplayName())) {
					partialTicks = Minecraft.getInstance().getFrameTime();
				}

			f = entitylivingbaseIn.tickCount + partialTicks;

			PlayerModel<AbstractClientPlayer> entitymodel = this.model();
			entitymodel.prepareMobModel(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);

			if (entitylivingbaseIn.isSpectator()) {
				this.witherModel.setAllVisible(false);
				this.witherModel.head.visible = true;
				this.witherModel.hat.visible = true;
			} else {
				ItemStack itemstack = entitylivingbaseIn.getMainHandItem();
				ItemStack itemstack1 = entitylivingbaseIn.getOffhandItem();
				this.witherModel.setAllVisible(true);

				/*
				this.witherModel.hat.visible = false;
				this.witherModel.jacket.visible = false;
				this.witherModel.leftPants.visible = false;
				this.witherModel.rightPants.visible = false;
				this.witherModel.rightSleeve.visible = false;
				this.witherModel.leftSleeve.visible = false;
				 */

				this.witherModel.crouching = entitylivingbaseIn.isCrouching();
				HumanoidModel.ArmPose bipedmodel$armpose = this.func_217766_a(entitylivingbaseIn, itemstack, itemstack1, InteractionHand.MAIN_HAND);
				HumanoidModel.ArmPose bipedmodel$armpose1 = this.func_217766_a(entitylivingbaseIn, itemstack, itemstack1, InteractionHand.OFF_HAND);
				if (entitylivingbaseIn.getMainArm() == HumanoidArm.RIGHT) {
					this.witherModel.rightArmPose = bipedmodel$armpose;
					this.witherModel.leftArmPose = bipedmodel$armpose1;
				} else {
					this.witherModel.rightArmPose = bipedmodel$armpose1;
					this.witherModel.leftArmPose = bipedmodel$armpose;
				}
			}

			float scale = 1.05F;

			poseStackIn.pushPose();
			poseStackIn.scale(scale, scale, scale);
			poseStackIn.translate(0, -(scale-1F)/2F, 0);

			this.getParentModel().copyPropertiesTo(entitymodel);
			VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.energySwirl(ShieldAuraLayer.getTextureLocation(), ShieldAuraLayer.xOffset(f), f * 0.01F));
			entitymodel.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			entitymodel.renderToBuffer(poseStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
			poseStackIn.popPose();
		}

	}

	private HumanoidModel.ArmPose func_217766_a(AbstractClientPlayer playerIn, ItemStack itemStackMain, ItemStack itemStackOff, InteractionHand handIn) {
		HumanoidModel.ArmPose bipedmodel$armpose = HumanoidModel.ArmPose.EMPTY;
		ItemStack itemstack = handIn == InteractionHand.MAIN_HAND ? itemStackMain : itemStackOff;
		if (!itemstack.isEmpty()) {
			bipedmodel$armpose = HumanoidModel.ArmPose.ITEM;
			if (playerIn.getUseItemRemainingTicks() > 0) {
				UseAnim useaction = itemstack.getUseAnimation();
				if (useaction == UseAnim.BLOCK) {
					bipedmodel$armpose = HumanoidModel.ArmPose.BLOCK;
				} else if (useaction == UseAnim.BOW) {
					bipedmodel$armpose = HumanoidModel.ArmPose.BOW_AND_ARROW;
				} else if (useaction == UseAnim.SPEAR) {
					bipedmodel$armpose = HumanoidModel.ArmPose.THROW_SPEAR;
				} else if (useaction == UseAnim.CROSSBOW && handIn == playerIn.getUsedItemHand()) {
					bipedmodel$armpose = HumanoidModel.ArmPose.CROSSBOW_CHARGE;
				}
			} else {
				boolean flag3 = itemStackMain.getItem() == Items.CROSSBOW;
				boolean flag = CrossbowItem.isCharged(itemStackMain);
				boolean flag1 = itemStackOff.getItem() == Items.CROSSBOW;
				boolean flag2 = CrossbowItem.isCharged(itemStackOff);
				if (flag3 && flag) {
					bipedmodel$armpose = HumanoidModel.ArmPose.CROSSBOW_HOLD;
				}

				if (flag1 && flag2 && itemStackMain.getItem().getUseAnimation(itemStackMain) == UseAnim.NONE) {
					bipedmodel$armpose = HumanoidModel.ArmPose.CROSSBOW_HOLD;
				}
			}
		}

		return bipedmodel$armpose;
	}

	public static float xOffset(float p_225634_1_) {
		return Mth.cos(p_225634_1_ * 0.02F) * 2.0F;
	}

	public static ResourceLocation getTextureLocation() {
		return ShieldAuraLayer.WITHER_ARMOR;
	}

	protected PlayerModel<AbstractClientPlayer> model() {
		return this.witherModel;
	}
}