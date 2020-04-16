package com.integral.enigmaticlegacy.proxy.renderers;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.entities.EnigmaticPotionEntity;
import com.integral.enigmaticlegacy.entities.PermanentItemEntity;
import com.integral.enigmaticlegacy.entities.UltimateWitherSkullEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

/**
 * Separate handler for registering entities' models.
 * @author Integral
 */

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = EnigmaticLegacy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModelHandler {

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent evt) {

		RenderingRegistry.registerEntityRenderingHandler(PermanentItemEntity.TYPE, renderManager -> new PermanentItemRenderer(renderManager, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(EnigmaticPotionEntity.TYPE, renderManager -> new SpriteRenderer<>(renderManager, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(UltimateWitherSkullEntity.TYPE, UltimateWitherSkullRenderer::new);

	}

}
