package com.integral.enigmaticlegacy.proxy;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.client.renderers.PermanentItemRenderer;
import com.integral.enigmaticlegacy.client.renderers.ShieldAuraLayer;
import com.integral.enigmaticlegacy.client.renderers.UltimateWitherSkullRenderer;
import com.integral.enigmaticlegacy.entities.EnigmaticPotionEntity;
import com.integral.enigmaticlegacy.entities.PermanentItemEntity;
import com.integral.enigmaticlegacy.entities.UltimateWitherSkullEntity;
import com.integral.enigmaticlegacy.objects.RevelationTomeToast;
import com.integral.enigmaticlegacy.objects.TransientPlayerData;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.client.particle.ItemPickupParticle;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

public class ClientProxy extends CommonProxy {

	private static final Random random = new Random();
	protected final HashMap<PlayerEntity, TransientPlayerData> clientTransientPlayerData;

	public ClientProxy() {
		super();
		this.clientTransientPlayerData = new HashMap<>();
	}

	@Override
	public HashMap<PlayerEntity, TransientPlayerData> getTransientPlayerData(boolean clientOnly) {
		if (clientOnly)
			return this.clientTransientPlayerData;
		else
			return this.commonTransientPlayerData;
	}

	@Override
	public void handleItemPickup(int pickuper_id, int item_id) {
		try {
			Entity pickuper = Minecraft.getInstance().world.getEntityByID(pickuper_id);
			Entity entity = Minecraft.getInstance().world.getEntityByID(item_id);

			Minecraft.getInstance().particles.addEffect(new ItemPickupParticle(Minecraft.getInstance().getRenderManager(), Minecraft.getInstance().getRenderTypeBuffers(), Minecraft.getInstance().world, pickuper, entity));
			Minecraft.getInstance().world.playSound(entity.getPosX(), entity.getPosY(), entity.getPosZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, (ClientProxy.random.nextFloat() - ClientProxy.random.nextFloat()) * 1.4F + 2.0F, false);
		} catch (Exception ex) {
			Exception log = new Exception("Unknown error when rendering permanent item pickup", ex);
			EnigmaticLegacy.enigmaticLogger.catching(log);
		}
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
	public void initEntityRendering() {
		RenderingRegistry.registerEntityRenderingHandler(PermanentItemEntity.TYPE, renderManager -> new PermanentItemRenderer(renderManager, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(EnigmaticPotionEntity.TYPE, renderManager -> new SpriteRenderer<>(renderManager, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(UltimateWitherSkullEntity.TYPE, UltimateWitherSkullRenderer::new);
	}

	@Override
	public void loadComplete(FMLLoadCompleteEvent event) {
		this.initAuxiliaryRender();
	}

	@Override
	public boolean isInVanillaDimension(PlayerEntity player) {
		return player.world.getDimensionKey().equals(this.getOverworldKey()) || player.world.getDimensionKey().equals(this.getNetherKey()) || player.world.getDimensionKey().equals(this.getEndKey());
	}

	@Override
	public boolean isInDimension(PlayerEntity player, RegistryKey<World> world) {
		return player.world.getDimensionKey().equals(world);
	}

	@Override
	public World getCentralWorld() {
		return Minecraft.getInstance().world;
	}

	@Override
	public UseAction getVisualBlockAction() {
		return UseAction.BLOCK;
	}

	@Override
	public PlayerEntity getPlayer(UUID playerID) {
		if (Minecraft.getInstance().world != null)
			return Minecraft.getInstance().world.getPlayerByUuid(playerID);
		else
			return null;
	}

	@Override
	public void pushRevelationToast(ItemStack renderedStack, int xp, int knowledge) {
		ToastGui gui = Minecraft.getInstance().getToastGui();
		gui.add(new RevelationTomeToast(renderedStack, xp, knowledge));
	}

}
