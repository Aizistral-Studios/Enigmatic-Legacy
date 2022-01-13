package com.integral.etherium.proxy;

import java.util.Map;

import com.integral.etherium.client.ShieldAuraLayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

public class ClientProxy extends CommonProxy {
	
	@SubscribeEvent
	public static void addLayers(EntityRenderersEvent.AddLayers evt) {
		addPlayerLayer(evt, "default");
		addPlayerLayer(evt, "slim");
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private static void addPlayerLayer(EntityRenderersEvent.AddLayers evt, String skin) {
		EntityRenderer<? extends Player> renderer = evt.getSkin(skin);

		if (renderer instanceof LivingEntityRenderer livingRenderer) {
			livingRenderer.addLayer(new ShieldAuraLayer(livingRenderer, Minecraft.getInstance().getEntityModels()));
		}
	}
	
	@Override
	public void loadComplete(FMLLoadCompleteEvent event) {
		this.initAuxiliaryRender();
	}
	
}
