package com.integral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

	@Redirect(method = "render(Lnet/minecraft/world/item/ItemStack;"
			+ "Lnet/minecraft/client/renderer/block/model/ItemTransforms$TransformType;"
			+ "ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;"
			+ "IILnet/minecraft/client/resources/model/BakedModel;)V", at = @At(value = "INVOKE", target =
			"Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 3), require = 1)
	private boolean redirectCompassCheck(ItemStack stack, Item item) {
		boolean result = stack.is(EnigmaticLegacy.theInfinitum) || stack.is(item);
		return result;
	}

}
