package com.integral.enigmaticlegacy.client.fx;

import com.integral.enigmaticlegacy.entities.PermanentItemEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderTypeBuffers;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PermanentItemPickupParticle extends Particle {
	private final RenderTypeBuffers renderTypeBuffers;
	private final Entity item;
	private final Entity target;
	private int particleAge;
	private final EntityRendererManager renderManager;

	public PermanentItemPickupParticle(EntityRendererManager entityRenderManager, RenderTypeBuffers buffers, ClientWorld world, Entity item, Entity target) {
		this(entityRenderManager, buffers, world, item, target, item.getMotion());
	}

	private PermanentItemPickupParticle(EntityRendererManager entityRenderManager, RenderTypeBuffers buffers, ClientWorld world, Entity item, Entity target, Vector3d motionVector) {
		super(world, item.getPosX(), item.getPosY(), item.getPosZ(), motionVector.x, motionVector.y, motionVector.z);
		this.renderTypeBuffers = buffers;
		this.item = this.func_239181_a_(item);
		this.target = target;
		this.renderManager = entityRenderManager;
	}

	private Entity func_239181_a_(Entity entity) {
		return !(entity instanceof PermanentItemEntity) ? entity : ((PermanentItemEntity)entity).func_234273_t_();
	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.CUSTOM;
	}

	@Override
	public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {
		try {
			float f = (this.particleAge + partialTicks) / 3.0F;
			f = f * f;
			double d0 = MathHelper.lerp(partialTicks, this.target.lastTickPosX, this.target.getPosX());
			double d1 = MathHelper.lerp(partialTicks, this.target.lastTickPosY, this.target.getPosY()) + 0.5D;
			double d2 = MathHelper.lerp(partialTicks, this.target.lastTickPosZ, this.target.getPosZ());
			double d3 = MathHelper.lerp(f, this.item.getPosX(), d0);
			double d4 = MathHelper.lerp(f, this.item.getPosY(), d1);
			double d5 = MathHelper.lerp(f, this.item.getPosZ(), d2);
			IRenderTypeBuffer.Impl ibuffer = this.renderTypeBuffers.getBufferSource();
			Vector3d vector3d = renderInfo.getProjectedView();
			this.renderManager.renderEntityStatic(this.item, d3 - vector3d.getX(), d4 - vector3d.getY(), d5 - vector3d.getZ(), this.item.rotationYaw, partialTicks, new MatrixStack(), ibuffer, this.renderManager.getPackedLight(this.item, partialTicks));
			ibuffer.finish();
		} catch (Throwable ex) {
			// NO-OP
		}
	}

	@Override
	public void tick() {
		++this.particleAge;
		if (this.particleAge == 3) {
			this.setExpired();
		}

	}
}
