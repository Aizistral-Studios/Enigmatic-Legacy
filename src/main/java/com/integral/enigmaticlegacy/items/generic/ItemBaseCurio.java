package com.integral.enigmaticlegacy.items.generic;

import java.util.Map;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.IItemCurio;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.type.capability.ICurio;

import net.minecraft.world.item.Item.Properties;
import top.theillusivec4.curios.api.type.capability.ICurio.DropRule;
import top.theillusivec4.curios.api.type.capability.ICurio.RenderHelper;

public abstract class ItemBaseCurio extends ItemBase implements IItemCurio, Vanishable {

	public ItemBaseCurio() {
		this(getDefaultProperties());
	}

	public ItemBaseCurio(Properties props) {
		super(props);
	}

	@Override
	public void onEquip(String identifier, int index, LivingEntity entityLivingBase, ItemStack stack) {
		// Insert existential void here
	}

	@Override
	public void onUnequip(String identifier, int index, LivingEntity entityLivingBase, ItemStack stack) {
		// Insert existential void here
	}

	@Override
	public void curioTick(String identifier, int index, LivingEntity entityLivingBase, ItemStack stack) {
		// Insert existential void here
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level worldIn, Player playerIn) {
		// Insert existential void here
	}

	@Override
	public boolean canRightClickEquip(ItemStack stack) {
		return true;
	}

	@Override
	public boolean canEquip(String identifier, LivingEntity living, ItemStack stack) {
		return !SuperpositionHandler.hasCurio(living, this);
	}

	@Override
	public boolean canUnequip(String identifier, LivingEntity living, ItemStack stack) {
		return true;
	}

	@Override
	public DropRule getDropRule(LivingEntity livingEntity, ItemStack stack) {
		return DropRule.DEFAULT;
	}

	public static Properties getDefaultProperties() {
		Properties props = new Item.Properties();

		props.tab(EnigmaticLegacy.enigmaticTab);
		props.stacksTo(1);
		props.rarity(Rarity.COMMON);

		return props;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		Map<Enchantment, Integer> list = EnchantmentHelper.getEnchantments(book);

		if (list.size() == 1 && list.containsKey(Enchantments.BINDING_CURSE))
			return true;
		else
			return super.isBookEnchantable(stack, book);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public abstract boolean canRender(String identifier, int index, LivingEntity living, ItemStack stack);

	@Override
	@OnlyIn(Dist.CLIENT)
	public void render(String identifier, int index, PoseStack PoseStack, IRenderTypeBuffer renderTypeBuffer,
			int light, LivingEntity living, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, ItemStack stack) {

		if (this.canRender(identifier, index, living, stack))
			return;

		BipedModel<LivingEntity> model = this.getModel();
		model.setupAnim(living, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		model.prepareMobModel(living, limbSwing, limbSwingAmount, partialTicks);
		RenderHelper.followBodyRotations(living, model);
		IVertexBuilder vertexBuilder = ItemRenderer.getFoilBuffer(renderTypeBuffer, model.renderType(this.getTexture()),
				false, false);
		model.renderToBuffer(PoseStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
	}

	@Nullable
	@OnlyIn(Dist.CLIENT)
	protected BipedModel<LivingEntity> getModel() {
		return null;
	}

	@Nullable
	@OnlyIn(Dist.CLIENT)
	protected ResourceLocation getTexture() {
		return null;
	}
}
