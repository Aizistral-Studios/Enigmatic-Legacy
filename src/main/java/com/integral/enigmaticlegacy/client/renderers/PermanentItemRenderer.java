package com.integral.enigmaticlegacy.client.renderers;

import java.util.Random;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.IPermanentCrystal;
import com.integral.enigmaticlegacy.entities.PermanentItemEntity;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Copy of default ItemRenderer for PermanentItemEntity.
 * @author Integral
 */

@OnlyIn(Dist.CLIENT)
public class PermanentItemRenderer extends EntityRenderer<PermanentItemEntity> {
	private final net.minecraft.client.renderer.ItemRenderer itemRenderer;
	private final Random random = new Random();

	public PermanentItemRenderer(EntityRendererManager renderManagerIn, net.minecraft.client.renderer.ItemRenderer itemRendererIn) {
		super(renderManagerIn);
		this.itemRenderer = itemRendererIn;
		this.shadowSize = 0.15F;
		this.shadowOpaque = 0.75F;
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

	@SuppressWarnings({ "deprecation", "unused" })
	@Override
	public void render(PermanentItemEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		if (!Minecraft.getInstance().player.isAlive() && Math.sqrt(entityIn.getDistanceSq(Minecraft.getInstance().player.getPosX(), Minecraft.getInstance().player.getPosYEye(), Minecraft.getInstance().player.getPosZ())) <= 1.0)
			return;

		matrixStackIn.push();
		ItemStack itemstack = entityIn.getItem();

		if (itemstack.getItem() instanceof IPermanentCrystal) {
			matrixStackIn.scale(1.25f, 1.25f, 1.25f);
			matrixStackIn.translate(0, -0.1125d, 0);
		}

		int i = itemstack.isEmpty() ? 187 : Item.getIdFromItem(itemstack.getItem()) + itemstack.getDamage();
		this.random.setSeed(i);
		IBakedModel ibakedmodel = this.itemRenderer.getItemModelWithOverrides(itemstack, entityIn.world, (LivingEntity) null);
		boolean flag = ibakedmodel.isGui3d();
		int j = this.getModelCount(itemstack);
		float f = 0.25F;
		float f1 = MathHelper.sin((entityIn.getAge() + partialTicks) / 10.0F + entityIn.hoverStart) * 0.1F + 0.1F;
		float f2 = this.shouldBob() ? ibakedmodel.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.getY() : 0;
		matrixStackIn.translate(0.0D, f1 + 0.25F * f2, 0.0D);
		float f3 = entityIn.func_234272_a_(partialTicks);
		matrixStackIn.rotate(Vector3f.YP.rotation(f3));
		if (!flag) {
			float f7 = -0.0F * (j - 1) * 0.5F;
			float f8 = -0.0F * (j - 1) * 0.5F;
			float f9 = -0.09375F * (j - 1) * 0.5F;
			matrixStackIn.translate(f7, f8, f9);
		}

		for (int k = 0; k < j; ++k) {
			matrixStackIn.push();
			if (k > 0) {
				if (flag) {
					float f11 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
					float f13 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
					float f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
					matrixStackIn.translate(this.shouldSpreadItems() ? f11 : 0, this.shouldSpreadItems() ? f13 : 0, this.shouldSpreadItems() ? f10 : 0);
				} else {
					float f12 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
					float f14 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
					matrixStackIn.translate(this.shouldSpreadItems() ? f12 : 0, this.shouldSpreadItems() ? f14 : 0, 0.0D);
				}
			}

			this.itemRenderer.renderItem(itemstack, ItemCameraTransforms.TransformType.GROUND, false, matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, ibakedmodel);
			matrixStackIn.pop();
			if (!flag) {
				matrixStackIn.translate(0.0, 0.0, 0.09375F);
			}
		}

		matrixStackIn.pop();
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	/**
	* Returns the location of an entity's texture.
	*/
	@SuppressWarnings("deprecation")
	@Override
	public ResourceLocation getEntityTexture(PermanentItemEntity entity) {
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