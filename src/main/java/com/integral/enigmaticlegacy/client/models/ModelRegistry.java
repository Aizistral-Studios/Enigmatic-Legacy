package com.integral.enigmaticlegacy.client.models;

import com.integral.enigmaticlegacy.client.renderers.PermanentItemRenderer;
import com.integral.enigmaticlegacy.client.renderers.UltimateWitherSkullRenderer;
import com.integral.enigmaticlegacy.entities.EnigmaticPotionEntity;
import com.integral.enigmaticlegacy.entities.PermanentItemEntity;
import com.integral.enigmaticlegacy.entities.UltimateWitherSkullEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

/**
 * Separate handler for registering entities' models.
 * @author Integral
 */

@OnlyIn(Dist.CLIENT)
public final class ModelRegistry {

	public static void registerModels() {

		RenderingRegistry.registerEntityRenderingHandler(PermanentItemEntity.TYPE, renderManager -> new PermanentItemRenderer(renderManager, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(EnigmaticPotionEntity.TYPE, renderManager -> new SpriteRenderer<>(renderManager, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(UltimateWitherSkullEntity.TYPE, UltimateWitherSkullRenderer::new);

	}

}
