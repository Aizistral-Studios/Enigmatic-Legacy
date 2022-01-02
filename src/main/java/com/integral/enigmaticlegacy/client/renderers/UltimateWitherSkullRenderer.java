package com.integral.enigmaticlegacy.client.renderers;

import com.integral.enigmaticlegacy.entities.UltimateWitherSkullEntity;
import com.integral.etherium.client.ShieldAuraLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.GenericHeadModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class UltimateWitherSkullRenderer extends EntityRenderer<UltimateWitherSkullEntity> {
	private static final ResourceLocation INVULNERABLE_WITHER_TEXTURES = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
	private static final ResourceLocation WITHER_TEXTURES = new ResourceLocation("textures/entity/wither/wither.png");
	private final GenericHeadModel skeletonHeadModel = new GenericHeadModel();

	public UltimateWitherSkullRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
	}
	
	@Override
	protected int getBlockLightLevel(UltimateWitherSkullEntity entityIn, BlockPos partialTicks) {
		return 15;
	}
	
	@Override
	public void render(UltimateWitherSkullEntity entityIn, float entityYaw, float partialTicks, PoseStack PoseStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		PoseStackIn.pushPose();

		float inflate = entityIn.isSkullInvulnerable() ? 1.4F : 1.0F;

		PoseStackIn.scale(-inflate, -inflate, inflate);

		float f = MathHelper.rotlerp(entityIn.yRotO, entityIn.yRot, partialTicks);
		float f1 = MathHelper.lerp(partialTicks, entityIn.xRotO, entityIn.xRot);
		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.skeletonHeadModel.renderType(this.getTextureLocation(entityIn)));
		this.skeletonHeadModel.setupAnim(0.0F, f, f1);
		this.skeletonHeadModel.renderToBuffer(PoseStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		PoseStackIn.popPose();

		if (entityIn.isSkullInvulnerable()) {

			float fullTicks = entityIn.tickCount + partialTicks;
			this.renderShield(PoseStackIn, packedLightIn, fullTicks, inflate + 0.1F, bufferIn);
			this.renderShield(PoseStackIn, packedLightIn, fullTicks, inflate + 0.2F, bufferIn);

		}

		super.render(entityIn, entityYaw, partialTicks, PoseStackIn, bufferIn, packedLightIn);
	}

	private void renderShield(PoseStack matrix, int light, float fullTicks, float scale, IRenderTypeBuffer bufferIn) {
		matrix.pushPose();
		matrix.scale(-scale, -scale, scale);

		IVertexBuilder ivertexbuilder1 = bufferIn.getBuffer(RenderType.energySwirl(ShieldAuraLayer.getTextureLocation(), ShieldAuraLayer.xOffset(fullTicks), fullTicks * 0.01F));
		this.skeletonHeadModel.renderToBuffer(matrix, ivertexbuilder1, light, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);

		matrix.popPose();
	}

	/**
	* Returns the location of an entity's texture.
	*/
	@Override
	public ResourceLocation getTextureLocation(UltimateWitherSkullEntity entity) {
		//return entity.isSkullInvulnerable() ? UltimateWitherSkullRenderer.INVULNERABLE_WITHER_TEXTURES : UltimateWitherSkullRenderer.WITHER_TEXTURES;
		return UltimateWitherSkullRenderer.WITHER_TEXTURES;
	}
}