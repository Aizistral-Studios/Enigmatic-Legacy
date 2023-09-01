package com.aizistral.enigmaticlegacy.proxy;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.objects.TransientPlayerData;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

public class CommonProxy {
	protected final Map<Player, TransientPlayerData> commonTransientPlayerData;

	public CommonProxy() {
		this.commonTransientPlayerData = new WeakHashMap<>();
	}

	public void displayPermadeathScreen() {
		// NO-OP
	}

	public void clearTransientData() {
		this.commonTransientPlayerData.clear();
	}

	public Map<Player, TransientPlayerData> getTransientPlayerData(boolean clientOnly) {
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

	public boolean isInVanillaDimension(Player player) {
		ServerPlayer serverPlayer = (ServerPlayer) player;
		return serverPlayer.level().dimension().equals(this.getOverworldKey()) || serverPlayer.level().dimension().equals(this.getNetherKey()) || serverPlayer.level().dimension().equals(this.getEndKey());
	}

	public boolean isInDimension(Player player, ResourceKey<Level> world) {
		ServerPlayer serverPlayer = (ServerPlayer) player;
		return serverPlayer.level().dimension().equals(world);
	}

	public Level getCentralWorld() {
		return SuperpositionHandler.getOverworld();
	}

	public ResourceKey<Level> getOverworldKey() {
		return Level.OVERWORLD;
	}

	public ResourceKey<Level> getNetherKey() {
		return Level.NETHER;
	}

	public ResourceKey<Level> getEndKey() {
		return Level.END;
	}

	public UseAnim getVisualBlockAction() {
		return UseAnim.BOW;
	}

	public Player getPlayer(UUID playerID) {
		if (ServerLifecycleHooks.getCurrentServer() != null)
			return ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(playerID);
		else
			return null;
	}

	public Player getClientPlayer() {
		return null;
	}

	public String getClientUsername() {
		return null;
	}

	public void pushRevelationToast(ItemStack renderedStack, int xp, int knowledge) {
		// NO-OP
	}

	public void initEntityRendering() {
		// NO-OP
	}

	public void spawnBonemealParticles(Level world, BlockPos pos, int data) {
		// NO-OP
	}

	public void updateInfinitumCounters() {
		// NO-OP
	}

	public void displayReviveAnimation(int entityID, int reviveType) {
		// NO-OP
	}

}
