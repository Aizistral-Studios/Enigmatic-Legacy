package com.integral.etherium.proxy;

import java.util.Map;

import com.integral.etherium.client.ShieldAuraLayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void initAuxiliaryRender() {
		Map<String, EntityRenderer<? extends Player>> skinMap = Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap();

		PlayerRenderer renderSteve;
		PlayerRenderer renderAlex;

		renderSteve = (PlayerRenderer) skinMap.get("default");
		renderAlex = (PlayerRenderer) skinMap.get("slim");

		renderSteve.addLayer(new ShieldAuraLayer(renderSteve, Minecraft.getInstance().getEntityModels()));
		renderAlex.addLayer(new ShieldAuraLayer(renderAlex, Minecraft.getInstance().getEntityModels()));
	}
	
	@Override
	public void loadComplete(FMLLoadCompleteEvent event) {
		this.initAuxiliaryRender();
	}
	
}
