package com.integral.enigmaticlegacy.proxy;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.MapMaker;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.client.fx.PermanentItemPickupParticle;
import com.integral.enigmaticlegacy.client.renderers.PermanentItemRenderer;
import com.integral.enigmaticlegacy.client.renderers.UltimateWitherSkullRenderer;
import com.integral.enigmaticlegacy.entities.EnigmaticPotionEntity;
import com.integral.enigmaticlegacy.entities.PermanentItemEntity;
import com.integral.enigmaticlegacy.entities.UltimateWitherSkullEntity;
import com.integral.enigmaticlegacy.objects.RevelationTomeToast;
import com.integral.enigmaticlegacy.objects.TransientPlayerData;
import com.integral.etherium.client.ShieldAuraLayer;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.particle.ItemPickupParticle;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

public class ClientProxy extends CommonProxy {
	private static final Random random = new Random();
	protected final Map<Player, TransientPlayerData> clientTransientPlayerData;

	public ClientProxy() {
		super();
		this.clientTransientPlayerData = new WeakHashMap<>();
	}

	@Override
	public void clearTransientData() {
		super.clearTransientData();
		this.clientTransientPlayerData.clear();
	}

	@Override
	public Map<Player, TransientPlayerData> getTransientPlayerData(boolean clientOnly) {
		if (clientOnly)
			return this.clientTransientPlayerData;
		else
			return this.commonTransientPlayerData;
	}

	@Override
	public void handleItemPickup(int pickuper_id, int item_id) {
		try {
			Entity pickuper = Minecraft.getInstance().level.getEntity(pickuper_id);
			Entity entity = Minecraft.getInstance().level.getEntity(item_id);

			// TODO Verify fix... someday

			Minecraft.getInstance().particleEngine.add(new PermanentItemPickupParticle(Minecraft.getInstance().getEntityRenderDispatcher(), Minecraft.getInstance().renderBuffers(), Minecraft.getInstance().level, pickuper, entity));
			Minecraft.getInstance().level.playLocalSound(pickuper.getX(), pickuper.getY(), pickuper.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, (ClientProxy.random.nextFloat() - ClientProxy.random.nextFloat()) * 1.4F + 2.0F, false);
		} catch (Throwable ex) {
			Exception log = new Exception("Unknown error when rendering permanent item pickup", ex);
			EnigmaticLegacy.logger.catching(log);
		}
	}

	@SubscribeEvent
	public void addLayers(EntityRenderersEvent.AddLayers evt) {
		this.addPlayerLayer(evt, "default");
		this.addPlayerLayer(evt, "slim");
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private void addPlayerLayer(EntityRenderersEvent.AddLayers evt, String skin) {
		EntityRenderer<? extends Player> renderer = evt.getSkin(skin);

		if (renderer instanceof LivingEntityRenderer livingRenderer) {
			livingRenderer.addLayer(new ShieldAuraLayer(livingRenderer, Minecraft.getInstance().getEntityModels()));
		}
	}

	@Override
	public void initEntityRendering() {
		EntityRenderers.register(PermanentItemEntity.TYPE, renderManager -> new PermanentItemRenderer(renderManager, Minecraft.getInstance().getItemRenderer()));
		EntityRenderers.register(EnigmaticPotionEntity.TYPE, ThrownItemRenderer::new);
		EntityRenderers.register(UltimateWitherSkullEntity.TYPE, UltimateWitherSkullRenderer::new);
	}

	@Override
	public void loadComplete(FMLLoadCompleteEvent event) {
		this.initAuxiliaryRender();
	}

	@Override
	public boolean isInVanillaDimension(Player player) {
		return player.level.dimension().equals(this.getOverworldKey()) || player.level.dimension().equals(this.getNetherKey()) || player.level.dimension().equals(this.getEndKey());
	}

	@Override
	public boolean isInDimension(Player player, ResourceKey<Level> world) {
		return player.level.dimension().equals(world);
	}

	@Override
	public Level getCentralWorld() {
		return Minecraft.getInstance().level;
	}

	@Override
	public UseAnim getVisualBlockAction() {
		return UseAnim.BLOCK;
	}

	@Override
	public Player getPlayer(UUID playerID) {
		if (Minecraft.getInstance().level != null)
			return Minecraft.getInstance().level.getPlayerByUUID(playerID);
		else
			return null;
	}

	@Override
	public void pushRevelationToast(ItemStack renderedStack, int xp, int knowledge) {
		ToastComponent gui = Minecraft.getInstance().getToasts();
		gui.addToast(new RevelationTomeToast(renderedStack, xp, knowledge));
	}

	@Override
	@SuppressWarnings("deprecation")
	public void spawnBonemealParticles(Level world, BlockPos pos, int data) {
		if (data == 0) {
			data = 15;
		}

		BlockState blockstate = world.getBlockState(pos);
		if (!blockstate.isAir()) {
			double d0 = 0.5D;
			double d1;
			if (blockstate.is(Blocks.WATER)) {
				data *= 3;
				d1 = 1.0D;
				d0 = 3.0D;
			} else if (blockstate.isSolidRender(world, pos)) {
				pos = pos.above();
				data *= 3;
				d0 = 3.0D;
				d1 = 1.0D;
			} else {
				d1 = blockstate.getShape(world, pos).max(Direction.Axis.Y);
			}

			world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);

			for(int i = 0; i < data; ++i) {
				double d2 = random.nextGaussian() * 0.02D;
				double d3 = random.nextGaussian() * 0.02D;
				double d4 = random.nextGaussian() * 0.02D;
				double d5 = 0.5D - d0;
				double d6 = pos.getX() + d5 + random.nextDouble() * d0 * 2.0D;
				double d7 = pos.getY() + random.nextDouble() * d1;
				double d8 = pos.getZ() + d5 + random.nextDouble() * d0 * 2.0D;

				world.addParticle(ParticleTypes.HAPPY_VILLAGER, d6, d7, d8, d2, d3, d4);
			}

		}

	}

	@Override
	public Player getClientPlayer() {
		return Minecraft.getInstance().player;
	}

}
