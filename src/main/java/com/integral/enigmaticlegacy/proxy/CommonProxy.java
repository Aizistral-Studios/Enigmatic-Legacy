package com.integral.enigmaticlegacy.proxy;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.MapMaker;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.objects.TransientPlayerData;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class CommonProxy {

	protected final Map<PlayerEntity, TransientPlayerData> commonTransientPlayerData;

	public CommonProxy() {
		this.commonTransientPlayerData = new WeakHashMap<>();
	}

	public void clearTransientData() {
		this.commonTransientPlayerData.clear();
	}

	public Map<PlayerEntity, TransientPlayerData> getTransientPlayerData(boolean clientOnly) {
		return this.commonTransientPlayerData;
	}

	public void handleItemPickup(int pickuper_id, int item_id) {
		// Insert existential void here
	}

	public void loadComplete(FMLLoadCompleteEvent event) {
		// Insert existential void here
	}

	public void initAuxiliaryRender() {
		// Insert existential void here
	}

	public boolean isInVanillaDimension(PlayerEntity player) {
		ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
		return serverPlayer.getLevel().dimension().equals(this.getOverworldKey()) || serverPlayer.getLevel().dimension().equals(this.getNetherKey()) || serverPlayer.getLevel().dimension().equals(this.getEndKey());
	}

	public boolean isInDimension(PlayerEntity player, RegistryKey<World> world) {
		ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
		return serverPlayer.getLevel().dimension().equals(world);
	}

	public World getCentralWorld() {
		return SuperpositionHandler.getOverworld();
	}

	public RegistryKey<World> getOverworldKey() {
		return World.OVERWORLD;
	}

	public RegistryKey<World> getNetherKey() {
		return World.NETHER;
	}

	public RegistryKey<World> getEndKey() {
		return World.END;
	}

	public UseAction getVisualBlockAction() {
		return UseAction.BOW;
	}

	public PlayerEntity getPlayer(UUID playerID) {
		if (ServerLifecycleHooks.getCurrentServer() != null)
			return ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(playerID);
		else
			return null;
	}

	public PlayerEntity getClientPlayer() {
		return null;
	}

	public void pushRevelationToast(ItemStack renderedStack, int xp, int knowledge) {
		// NO-OP
	}

	public void initEntityRendering() {
		// NO-OP
	}

	public void spawnBonemealParticles(World world, BlockPos pos, int data) {
		// NO-OP
	}

}
