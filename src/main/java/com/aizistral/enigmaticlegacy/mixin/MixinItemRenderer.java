package com.aizistral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;

import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/tags/TagKey;)Z", ordinal = 0), require = 1)
	private boolean redirectCompassCheck(ItemStack stack, TagKey<Item> item) {
		return stack.is(EnigmaticItems.THE_INFINITUM) || stack.is(item);
	}

}
