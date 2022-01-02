package com.integral.etherium.client;

import com.integral.etherium.items.EtheriumArmor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAction;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShieldAuraLayer extends LayerRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
	private static final ResourceLocation WITHER_ARMOR = new ResourceLocation("enigmaticlegacy", "textures/models/misc/ultimate_wither_armor.png");
	private final PlayerModel<AbstractClientPlayer> witherModel = new PlayerModel<AbstractClientPlayer>(0.5F, false);

	public ShieldAuraLayer(IEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
		super(renderer);
	}

	@Override
	public void render(PoseStack PoseStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
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
				this.witherModel.bipedHeadwear.showModel = false;
				this.witherModel.bipedBodyWear.showModel = false;
				this.witherModel.bipedLeftLegwear.showModel = false;
				this.witherModel.bipedRightLegwear.showModel = false;
				this.witherModel.bipedLeftArmwear.showModel = false;
				this.witherModel.bipedRightArmwear.showModel = false;
				 */

				this.witherModel.crouching = entitylivingbaseIn.isCrouching();
				BipedModel.ArmPose bipedmodel$armpose = this.func_217766_a(entitylivingbaseIn, itemstack, itemstack1, Hand.MAIN_HAND);
				BipedModel.ArmPose bipedmodel$armpose1 = this.func_217766_a(entitylivingbaseIn, itemstack, itemstack1, Hand.OFF_HAND);
				if (entitylivingbaseIn.getMainArm() == HandSide.RIGHT) {
					this.witherModel.rightArmPose = bipedmodel$armpose;
					this.witherModel.leftArmPose = bipedmodel$armpose1;
				} else {
					this.witherModel.rightArmPose = bipedmodel$armpose1;
					this.witherModel.leftArmPose = bipedmodel$armpose;
				}
			}

			this.getParentModel().copyPropertiesTo(entitymodel);
			IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.energySwirl(ShieldAuraLayer.getTextureLocation(), ShieldAuraLayer.xOffset(f), f * 0.01F));
			entitymodel.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			entitymodel.renderToBuffer(PoseStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
		}

	}

	private BipedModel.ArmPose func_217766_a(AbstractClientPlayer playerIn, ItemStack itemStackMain, ItemStack itemStackOff, Hand handIn) {
		BipedModel.ArmPose bipedmodel$armpose = BipedModel.ArmPose.EMPTY;
		ItemStack itemstack = handIn == Hand.MAIN_HAND ? itemStackMain : itemStackOff;
		if (!itemstack.isEmpty()) {
			bipedmodel$armpose = BipedModel.ArmPose.ITEM;
			if (playerIn.getUseItemRemainingTicks() > 0) {
				UseAction useaction = itemstack.getUseAnimation();
				if (useaction == UseAction.BLOCK) {
					bipedmodel$armpose = BipedModel.ArmPose.BLOCK;
				} else if (useaction == UseAction.BOW) {
					bipedmodel$armpose = BipedModel.ArmPose.BOW_AND_ARROW;
				} else if (useaction == UseAction.SPEAR) {
					bipedmodel$armpose = BipedModel.ArmPose.THROW_SPEAR;
				} else if (useaction == UseAction.CROSSBOW && handIn == playerIn.getUsedItemHand()) {
					bipedmodel$armpose = BipedModel.ArmPose.CROSSBOW_CHARGE;
				}
			} else {
				boolean flag3 = itemStackMain.getItem() == Items.CROSSBOW;
				boolean flag = CrossbowItem.isCharged(itemStackMain);
				boolean flag1 = itemStackOff.getItem() == Items.CROSSBOW;
				boolean flag2 = CrossbowItem.isCharged(itemStackOff);
				if (flag3 && flag) {
					bipedmodel$armpose = BipedModel.ArmPose.CROSSBOW_HOLD;
				}

				if (flag1 && flag2 && itemStackMain.getItem().getUseAnimation(itemStackMain) == UseAction.NONE) {
					bipedmodel$armpose = BipedModel.ArmPose.CROSSBOW_HOLD;
				}
			}
		}

		return bipedmodel$armpose;
	}

	public static float xOffset(float p_225634_1_) {
		return MathHelper.cos(p_225634_1_ * 0.02F) * 2.0F;
	}

	public static ResourceLocation getTextureLocation() {
		return ShieldAuraLayer.WITHER_ARMOR;
	}

	protected PlayerModel<AbstractClientPlayer> model() {
		return this.witherModel;
	}
}