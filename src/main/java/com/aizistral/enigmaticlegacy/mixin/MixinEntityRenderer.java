package com.aizistral.enigmaticlegacy.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {
	@Shadow @Final
	protected EntityRenderDispatcher entityRenderDispatcher;

	@Shadow
	public abstract Font getFont();

	@Inject(method = "renderNameTag", at = @At("HEAD"), cancellable = true)
	private void onRenderNameTag(Entity entity, Component name, PoseStack stack, MultiBufferSource buffer, int packedLight, CallbackInfo info) {
		if (entity instanceof Player player) {
			ItemStack insignia = SuperpositionHandler.getCurioStack(player, EnigmaticItems.INSIGNIA);

			if (insignia != null && !insignia.isEmpty()) {
				Optional<Component> customName = EnigmaticItems.INSIGNIA.getCustomName(insignia);

				if (customName.isPresent()) {
					info.cancel();
					SuperpositionHandler.renderInsigniaNameplate(entity, customName.get(), stack, buffer,
							packedLight, this.entityRenderDispatcher, this.getFont());
				}
			}
		}
	}

}
