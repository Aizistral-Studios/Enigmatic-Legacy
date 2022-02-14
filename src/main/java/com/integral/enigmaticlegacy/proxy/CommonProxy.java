package com.integral.enigmaticlegacy.proxy;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.MapMaker;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.objects.TransientPlayerData;

import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

public class CommonProxy {
	protected final Map<Player, TransientPlayerData> commonTransientPlayerData;
	protected final Map<UUID, Tuple<Integer, Integer>> cursesTimeCache;

	public CommonProxy() {
		this.commonTransientPlayerData = new WeakHashMap<>();
		this.cursesTimeCache = new WeakHashMap<>();
	}

	public void clearTransientData() {
		this.commonTransientPlayerData.clear();
		this.cursesTimeCache.clear();
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
		return serverPlayer.getLevel().dimension().equals(this.getOverworldKey()) || serverPlayer.getLevel().dimension().equals(this.getNetherKey()) || serverPlayer.getLevel().dimension().equals(this.getEndKey());
	}

	public boolean isInDimension(Player player, ResourceKey<Level> world) {
		ServerPlayer serverPlayer = (ServerPlayer) player;
		return serverPlayer.getLevel().dimension().equals(world);
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

	public int getStats(Player player, ResourceLocation stat) {
		if (player instanceof ServerPlayer playerMP)
			return playerMP.getStats().getValue(Stats.CUSTOM.get(stat));
		else
			return 0;
	}

	public void cacheStats(UUID playerID, int timeWithoutCurses, int timeWithCurses) {
		this.cursesTimeCache.put(playerID, new Tuple<Integer, Integer>(timeWithoutCurses, timeWithCurses));
	}

	public int getTimeWithoutCurses(Player player) {
		if (this.cursesTimeCache.containsKey(player.getUUID()))
			return this.cursesTimeCache.get(player.getUUID()).getA();
		else
			return 0;
	}

	public int getTimeWithCurses(Player player) {
		if (this.cursesTimeCache.containsKey(player.getUUID()))
			return this.cursesTimeCache.get(player.getUUID()).getB();
		else
			return 0;
	}

}
