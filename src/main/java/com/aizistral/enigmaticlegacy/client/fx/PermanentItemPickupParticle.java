package com.aizistral.enigmaticlegacy.client.fx;

import com.aizistral.enigmaticlegacy.entities.PermanentItemEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PermanentItemPickupParticle extends Particle {
	private final RenderBuffers renderTypeBuffers;
	private final Entity item;
	private final Entity target;
	private int particleAge;
	private final EntityRenderDispatcher renderManager;

	public PermanentItemPickupParticle(EntityRenderDispatcher entityRenderManager, RenderBuffers buffers, ClientLevel world, Entity item, Entity target) {
		this(entityRenderManager, buffers, world, item, target, item.getDeltaMovement());
	}

	private PermanentItemPickupParticle(EntityRenderDispatcher entityRenderManager, RenderBuffers buffers, ClientLevel world, Entity item, Entity target, Vec3 motionVector) {
		super(world, item.getX(), item.getY(), item.getZ(), motionVector.x, motionVector.y, motionVector.z);
		this.renderTypeBuffers = buffers;
		this.item = this.getSafeCopy(item);
		this.target = target;
		this.renderManager = entityRenderManager;
	}

	private Entity getSafeCopy(Entity entity) {
		return !(entity instanceof PermanentItemEntity) ? entity : ((PermanentItemEntity)entity).copy();
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.CUSTOM;
	}

	@Override
	public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
		try {
			float f = (this.particleAge + partialTicks) / 3.0F;
			f = f * f;
			double d0 = Mth.lerp(partialTicks, this.target.xOld, this.target.getX());
			double d1 = Mth.lerp(partialTicks, this.target.yOld, this.target.getY()) + 0.5D;
			double d2 = Mth.lerp(partialTicks, this.target.zOld, this.target.getZ());
			double d3 = Mth.lerp(f, this.item.getX(), d0);
			double d4 = Mth.lerp(f, this.item.getY(), d1);
			double d5 = Mth.lerp(f, this.item.getZ(), d2);
			MultiBufferSource.BufferSource ibuffer = this.renderTypeBuffers.bufferSource();
			Vec3 vector3d = renderInfo.getPosition();
			this.renderManager.render(this.item, d3 - vector3d.x(), d4 - vector3d.y(), d5 - vector3d.z(), this.item.getYRot(), partialTicks, new PoseStack(), ibuffer, this.renderManager.getPackedLightCoords(this.item, partialTicks));
			ibuffer.endBatch();
		} catch (Throwable ex) {
			// NO-OP
		}
	}

	@Override
	public void tick() {
		++this.particleAge;
		if (this.particleAge == 3) {
			this.remove();
		}

	}
}
