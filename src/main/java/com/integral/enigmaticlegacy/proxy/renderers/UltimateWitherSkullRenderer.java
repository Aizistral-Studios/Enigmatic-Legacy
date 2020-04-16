package com.integral.enigmaticlegacy.proxy.renderers;

import com.integral.enigmaticlegacy.entities.UltimateWitherSkullEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.GenericHeadModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
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
	protected int getBlockLight(UltimateWitherSkullEntity entityIn, float partialTicks) {
		return 15;
	}

	@Override
	public void render(UltimateWitherSkullEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();

		if (entityIn.isSkullInvulnerable())
			matrixStackIn.scale(-1.4F, -1.4F, 1.4F);
		else
			matrixStackIn.scale(-1.0F, -1.0F, 1.0F);

		float f = MathHelper.rotLerp(entityIn.prevRotationYaw, entityIn.rotationYaw, partialTicks);
		float f1 = MathHelper.lerp(partialTicks, entityIn.prevRotationPitch, entityIn.rotationPitch);
		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.skeletonHeadModel.getRenderType(this.getEntityTexture(entityIn)));
		this.skeletonHeadModel.func_225603_a_(0.0F, f, f1);
		this.skeletonHeadModel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

		matrixStackIn.pop();

		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	/**
	* Returns the location of an entity's texture.
	*/
	@Override
	public ResourceLocation getEntityTexture(UltimateWitherSkullEntity entity) {
		//return entity.isSkullInvulnerable() ? UltimateWitherSkullRenderer.INVULNERABLE_WITHER_TEXTURES : UltimateWitherSkullRenderer.WITHER_TEXTURES;
		return UltimateWitherSkullRenderer.WITHER_TEXTURES;
	}
}