package com.integral.enigmaticlegacy.client.renderers;

import com.integral.enigmaticlegacy.entities.UltimateWitherSkullEntity;
import com.integral.etherium.client.ShieldAuraLayer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.GenericHeadModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
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
	protected int getBlockLight(UltimateWitherSkullEntity entityIn, BlockPos partialTicks) {
		return 15;
	}
	
	@Override
	public void render(UltimateWitherSkullEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();

		float inflate = entityIn.isSkullInvulnerable() ? 1.4F : 1.0F;

		matrixStackIn.scale(-inflate, -inflate, inflate);

		float f = MathHelper.rotLerp(entityIn.prevRotationYaw, entityIn.rotationYaw, partialTicks);
		float f1 = MathHelper.lerp(partialTicks, entityIn.prevRotationPitch, entityIn.rotationPitch);
		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.skeletonHeadModel.getRenderType(this.getEntityTexture(entityIn)));
		this.skeletonHeadModel.func_225603_a_(0.0F, f, f1);
		this.skeletonHeadModel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		matrixStackIn.pop();

		if (entityIn.isSkullInvulnerable()) {

			float fullTicks = entityIn.ticksExisted + partialTicks;
			this.renderShield(matrixStackIn, packedLightIn, fullTicks, inflate + 0.1F, bufferIn);
			this.renderShield(matrixStackIn, packedLightIn, fullTicks, inflate + 0.2F, bufferIn);

		}

		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	private void renderShield(MatrixStack matrix, int light, float fullTicks, float scale, IRenderTypeBuffer bufferIn) {
		matrix.push();
		matrix.scale(-scale, -scale, scale);

		IVertexBuilder ivertexbuilder1 = bufferIn.getBuffer(RenderType.getEnergySwirl(ShieldAuraLayer.func_225633_a_(), ShieldAuraLayer.func_225634_a_(fullTicks), fullTicks * 0.01F));
		this.skeletonHeadModel.render(matrix, ivertexbuilder1, light, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);

		matrix.pop();
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