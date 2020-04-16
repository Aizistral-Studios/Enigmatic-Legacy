package com.integral.enigmaticlegacy.proxy;

import java.util.Map;
import java.util.Random;

import com.integral.enigmaticlegacy.proxy.renderers.ShieldAuraLayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ItemPickupParticle;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

public class ClientProxy extends CommonProxy {

	private static final Random random = new Random();

	@Override
	public void handleItemPickup(int pickuper_id, int item_id) {
		Entity pickuper = Minecraft.getInstance().world.getEntityByID(pickuper_id);
		Entity entity = Minecraft.getInstance().world.getEntityByID(item_id);

		Minecraft.getInstance().particles.addEffect(new ItemPickupParticle(Minecraft.getInstance().getRenderManager(), Minecraft.getInstance().getRenderTypeBuffers(), Minecraft.getInstance().world, pickuper, entity));
		Minecraft.getInstance().world.playSound(entity.getPosX(), entity.getPosY(), entity.getPosZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, (ClientProxy.random.nextFloat() - ClientProxy.random.nextFloat()) * 1.4F + 2.0F, false);
	}

	@Override
	public void initAuxiliaryRender() {
		Map<String, PlayerRenderer> skinMap = Minecraft.getInstance().getRenderManager().getSkinMap();

		PlayerRenderer renderSteve;
		PlayerRenderer renderAlex;

		renderSteve = skinMap.get("default");
		renderAlex = skinMap.get("slim");

		renderSteve.addLayer(new ShieldAuraLayer(renderSteve));
		renderAlex.addLayer(new ShieldAuraLayer(renderAlex));

		//render.addLayer(new ShieldAuraLayer(render, false));
	}

	@Override
	public void loadComplete(FMLLoadCompleteEvent event) {
		this.initAuxiliaryRender();
	}

}
