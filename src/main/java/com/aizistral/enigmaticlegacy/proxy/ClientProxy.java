package com.aizistral.enigmaticlegacy.proxy;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.WeakHashMap;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.client.QuoteHandler;
import com.aizistral.enigmaticlegacy.client.fx.PermanentItemPickupParticle;
import com.aizistral.enigmaticlegacy.client.renderers.EndAnchorRenderer;
import com.aizistral.enigmaticlegacy.client.renderers.EnigmaticElytraLayer;
import com.aizistral.enigmaticlegacy.client.renderers.PermanentItemRenderer;
import com.aizistral.enigmaticlegacy.client.renderers.UltimateWitherSkullRenderer;
import com.aizistral.enigmaticlegacy.gui.PermadeathScreen;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.objects.RevelationTomeToast;
import com.aizistral.enigmaticlegacy.objects.TransientPlayerData;
import com.aizistral.enigmaticlegacy.registries.EnigmaticEntities;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.aizistral.enigmaticlegacy.registries.EnigmaticTiles;
import com.aizistral.etherium.client.ShieldAuraLayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientProxy extends CommonProxy {
	private static final Random RANDOM = new Random();
	protected final Map<Player, TransientPlayerData> clientTransientPlayerData;
	protected final List<InfinitumCounterEntry> theInfinitumHoldTicks;

	public ClientProxy() {
		super();
		this.clientTransientPlayerData = new WeakHashMap<>();
		this.theInfinitumHoldTicks = new ArrayList<>();

		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(this::onClientSetup);

		MinecraftForge.EVENT_BUS.register(QuoteHandler.INSTANCE);
	}

	@Override
	public void displayPermadeathScreen() {
		if (Minecraft.getInstance().level != null) {
			boolean local = Minecraft.getInstance().isLocalServer();
			Minecraft.getInstance().level.disconnect();

			if (local) {
				Minecraft.getInstance().clearLevel(new GenericDirtMessageScreen(Component.translatable("menu.savingLevel")));
			} else {
				Minecraft.getInstance().clearLevel();
			}
		}

		PermadeathScreen permadeath = new PermadeathScreen(new SelectWorldScreen(new TitleScreen()),
				Component.translatable("gui.enigmaticlegacy.permadeathTitle"),
				Component.translatable("message.enigmaticlegacy.permadeath"));
		PermadeathScreen.active = permadeath;
		Minecraft.getInstance().setScreen(permadeath);
	}

	@Override
	public void clearTransientData() {
		super.clearTransientData();
		this.clientTransientPlayerData.clear();
		this.theInfinitumHoldTicks.clear();
	}

	@Override
	public Map<Player, TransientPlayerData> getTransientPlayerData(boolean clientOnly) {
		if (clientOnly)
			return this.clientTransientPlayerData;
		else
			return this.commonTransientPlayerData;
	}

	@Override
	public void displayReviveAnimation(int entityID, int reviveType) {
		Player player = this.getClientPlayer();
		Entity entity = player.level().getEntity(entityID);

		if (entity != null) {
			Item item = reviveType == 0 ? EnigmaticItems.COSMIC_SCROLL : EnigmaticItems.THE_CUBE;
			int i = 40;
			Minecraft.getInstance().particleEngine.createTrackingEmitter(entity, ParticleTypes.TOTEM_OF_UNDYING, 30);

			int amount = 50;

			for (int counter = 0; counter <= amount; counter++) {
				player.level().addParticle(ParticleTypes.FLAME, true, entity.getX() + (Math.random() - 0.5), entity.getY() + (entity.getBbHeight()/2) + (Math.random() - 0.5), entity.getZ() + (Math.random() - 0.5), (Math.random() - 0.5D) * 0.2, (Math.random() - 0.5D) * 0.2, (Math.random() - 0.5D) * 0.2);
			}

			player.level().playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, entity.getSoundSource(), 1.0F, 1.0F, false);

			if (entity == player) {
				ItemStack stack = SuperpositionHandler.getCurioStack(player, item);

				if (stack == null) {
					stack = new ItemStack(item, 1);
				}

				Minecraft.getInstance().gameRenderer.displayItemActivation(stack);
			}
		}
	}

	@Override
	public void handleItemPickup(int pickuper_id, int item_id) {
		try {
			Entity pickuper = Minecraft.getInstance().level.getEntity(pickuper_id);
			Entity entity = Minecraft.getInstance().level.getEntity(item_id);

			// TODO Verify fix... someday

			Minecraft.getInstance().particleEngine.add(new PermanentItemPickupParticle(Minecraft.getInstance().getEntityRenderDispatcher(), Minecraft.getInstance().renderBuffers(), Minecraft.getInstance().level, pickuper, entity));
			Minecraft.getInstance().level.playLocalSound(pickuper.getX(), pickuper.getY(), pickuper.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, (ClientProxy.RANDOM.nextFloat() - ClientProxy.RANDOM.nextFloat()) * 1.4F + 2.0F, false);
		} catch (Throwable ex) {
			Exception log = new Exception("Unknown error when rendering permanent item pickup", ex);
			EnigmaticLegacy.LOGGER.catching(log);
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void addLayers(EntityRenderersEvent.AddLayers evt) {
		this.addPlayerLayer(evt, "default");
		this.addPlayerLayer(evt, "slim");
		this.addEntityLayer(evt, EntityType.ARMOR_STAND);
	}

	@OnlyIn(Dist.CLIENT)
	@SuppressWarnings({"rawtypes", "unchecked"})
	private void addPlayerLayer(EntityRenderersEvent.AddLayers evt, String skin) {
		EntityRenderer<? extends Player> renderer = evt.getSkin(skin);

		if (renderer instanceof LivingEntityRenderer livingRenderer) {
			livingRenderer.addLayer(new EnigmaticElytraLayer(livingRenderer, evt.getEntityModels()));
			livingRenderer.addLayer(new ShieldAuraLayer(livingRenderer, evt.getEntityModels()));
		}
	}

	@OnlyIn(Dist.CLIENT)
	private <T extends LivingEntity, M extends HumanoidModel<T>, R extends LivingEntityRenderer<T, M>> void addEntityLayer(EntityRenderersEvent.AddLayers evt, EntityType<? extends T> entityType) {
		R renderer = evt.getRenderer(entityType);

		if (renderer != null) {
			renderer.addLayer(new EnigmaticElytraLayer<>(renderer, evt.getEntityModels()));
		}
	}

	private void registerBlockingProperty(Item item) {
		ItemProperties.register(item, new ResourceLocation("blocking"),
				(stack, world, entity, seed) -> entity != null && entity.isUsingItem()
						&& entity.getUseItem() == stack ? 1 : 0);
	}

	@OnlyIn(Dist.CLIENT)
	public void onClientSetup(FMLClientSetupEvent event) {
		this.registerBlockingProperty(EnigmaticItems.INFERNAL_SHIELD);
		this.registerBlockingProperty(EnigmaticItems.ELDRITCH_PAN);

		ItemProperties.register(EnigmaticItems.THE_INFINITUM, new ResourceLocation(EnigmaticLegacy.MODID, "the_infinitum_open"), (stack, world, entity, seed) -> {
			if (entity instanceof Player player) {
				for (InfinitumCounterEntry entry : this.theInfinitumHoldTicks) {
					if (entry.getPlayer() == player && entry.getStack() == stack)
						return entry.animValue;
				}

				ItemStack mainhand = player.getMainHandItem();
				ItemStack offhand = player.getOffhandItem();

				if (mainhand == stack || offhand == stack)
					if (SuperpositionHandler.isTheWorthyOne(player)) {
						this.theInfinitumHoldTicks.add(new InfinitumCounterEntry(player, stack));
					}
			}

			return 0;
		});

		BlockEntityRenderers.register(EnigmaticTiles.END_ANCHOR, EndAnchorRenderer::new);
	}

	@Override
	public void updateInfinitumCounters() {
		for (InfinitumCounterEntry entry : new ArrayList<>(this.theInfinitumHoldTicks)) {
			if (entry.getPlayer() == null || entry.getStack() == null) {
				this.theInfinitumHoldTicks.remove(entry);
				continue;
			}

			ItemStack stack = entry.getStack();
			Player player = entry.getPlayer();
			int holdTicks = entry.counter;
			ItemStack mainhand = player.getMainHandItem();
			ItemStack offhand = player.getOffhandItem();

			if (mainhand == stack || offhand == stack)
				if (SuperpositionHandler.isTheWorthyOne(player)) {
					if (entry.counter > 5) {
						entry.animValue = 1F;
					} else if (entry.counter == 5) {
						entry.animValue = 0.5F;
					} else {
						entry.animValue = 0F;
					}

					entry.counter++;
					continue;
				}

			if (entry.counter <= 0) {
				this.theInfinitumHoldTicks.remove(entry);
			} else {
				if (entry.counter > 5) {
					entry.counter = 5;
				}

				if (entry.counter > 1) {
					entry.animValue = 1F;
				} else if (entry.counter == 1) {
					entry.animValue = 0.5F;
				} else {
					entry.animValue = 0F;
				}

				entry.counter--;

			}
		}
	}

	@Override
	public void initEntityRendering() {
		EntityRenderers.register(EnigmaticEntities.PERMANENT_ITEM_ENTITY, renderManager -> new PermanentItemRenderer(renderManager, Minecraft.getInstance().getItemRenderer()));
		EntityRenderers.register(EnigmaticEntities.ENIGMATIC_POTION, ThrownItemRenderer::new);
		EntityRenderers.register(EnigmaticEntities.ULTIMATE_WITHER_SKULL, UltimateWitherSkullRenderer::new);
	}

	@Override
	public void loadComplete(FMLLoadCompleteEvent event) {
		this.initAuxiliaryRender();
	}

	@Override
	public boolean isInVanillaDimension(Player player) {
		return player.level().dimension().equals(this.getOverworldKey()) || player.level().dimension().equals(this.getNetherKey()) || player.level().dimension().equals(this.getEndKey());
	}

	@Override
	public boolean isInDimension(Player player, ResourceKey<Level> world) {
		return player.level().dimension().equals(world);
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
				double d2 = RANDOM.nextGaussian() * 0.02D;
				double d3 = RANDOM.nextGaussian() * 0.02D;
				double d4 = RANDOM.nextGaussian() * 0.02D;
				double d5 = 0.5D - d0;
				double d6 = pos.getX() + d5 + RANDOM.nextDouble() * d0 * 2.0D;
				double d7 = pos.getY() + RANDOM.nextDouble() * d1;
				double d8 = pos.getZ() + d5 + RANDOM.nextDouble() * d0 * 2.0D;

				world.addParticle(ParticleTypes.HAPPY_VILLAGER, d6, d7, d8, d2, d3, d4);
			}

		}
	}

	@Override
	public Player getClientPlayer() {
		return Minecraft.getInstance().player;
	}

	@Override
	public String getClientUsername() {
		return Minecraft.getInstance().getUser().getName();
	}

	private static class InfinitumCounterEntry {
		private final WeakReference<ItemStack> stack;
		private final WeakReference<Player> player;
		private int counter = 0;
		private float animValue = 0F;

		public InfinitumCounterEntry(Player player, ItemStack stack) {
			this.player = new WeakReference<>(player);
			this.stack = new WeakReference<>(stack);
		}

		public Player getPlayer() {
			return this.player.get();
		}

		public ItemStack getStack() {
			return this.stack.get();
		}
	}

}
