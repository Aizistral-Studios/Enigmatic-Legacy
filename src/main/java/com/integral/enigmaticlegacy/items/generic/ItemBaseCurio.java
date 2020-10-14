package com.integral.enigmaticlegacy.items.generic;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.type.capability.ICurio;

public abstract class ItemBaseCurio extends ItemBase implements ICurio {

	public ItemBaseCurio() {
		this(ItemBaseCurio.getDefaultProperties());
	}

	public ItemBaseCurio(Properties props) {
		super(props);
	}

	@Override
	public void onEquip(String identifier, int index, LivingEntity entityLivingBase) {
		// Insert existential void here
	}

	@Override
	public void onUnequip(String identifier, int index, LivingEntity entityLivingBase) {
		// Insert existential void here
	}

	@Override
	public void curioTick(String identifier, int index, LivingEntity entityLivingBase) {
		// Insert existential void here
	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
		// Insert existential void here
	}

	@Override
	public boolean canRightClickEquip() {
		return true;
	}

	@Override
	public boolean canEquip(String identifier, LivingEntity living) {
		if (SuperpositionHandler.hasCurio(living, this))
			return false;
		else
			return true;

	}

	@Override
	public boolean canUnequip(String identifier, LivingEntity living) {
		return true;
	}

	@Override
	public DropRule getDropRule(LivingEntity livingEntity) {
		return DropRule.DEFAULT;
	}

	public static Properties getDefaultProperties() {
		Properties props = new Item.Properties();

		props.group(EnigmaticLegacy.enigmaticTab);
		props.maxStackSize(1);
		props.rarity(Rarity.COMMON);

		return props;
	}

	@Override
    @OnlyIn(Dist.CLIENT)
    public abstract boolean canRender(String identifier, int index, LivingEntity living);

	@Override
    @OnlyIn(Dist.CLIENT)
    public void render(String identifier, int index, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity living, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (this.canRender(identifier, index, living))
			return;

		BipedModel<LivingEntity> model = this.getModel();
        model.setRotationAngles(living, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        model.setLivingAnimations(living, limbSwing, limbSwingAmount, partialTicks);
        RenderHelper.followBodyRotations(living, model);
        IVertexBuilder vertexBuilder = ItemRenderer.getBuffer(renderTypeBuffer, model.getRenderType(this.getTexture()), false, false);
        model.render(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    protected BipedModel<LivingEntity> getModel() {
		return null;
	}

    @OnlyIn(Dist.CLIENT)
    @Nullable
    protected ResourceLocation getTexture() {
    	return null;
    }
}
