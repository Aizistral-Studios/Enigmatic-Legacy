package com.integral.enigmaticlegacy.proxy;

import java.util.HashMap;
import java.util.UUID;

import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.objects.TransientPlayerData;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class CommonProxy {

	protected final HashMap<PlayerEntity, TransientPlayerData> commonTransientPlayerData;

	public CommonProxy() {
		this.commonTransientPlayerData = new HashMap<>();
	}

	public HashMap<PlayerEntity, TransientPlayerData> getTransientPlayerData(boolean clientOnly) {
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
		return serverPlayer.getServerWorld().func_234923_W_().equals(this.getOverworldKey()) || serverPlayer.getServerWorld().func_234923_W_().equals(this.getNetherKey()) || serverPlayer.getServerWorld().func_234923_W_().equals(this.getEndKey());
	}

	public boolean isInDimension(PlayerEntity player, RegistryKey<World> world) {
		ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
		return serverPlayer.getServerWorld().func_234923_W_().equals(world);
	}

	public World getCentralWorld() {
		return SuperpositionHandler.getOverworld();
	}

	public RegistryKey<World> getOverworldKey() {
		return World.field_234918_g_;
	}

	public RegistryKey<World> getNetherKey() {
		return World.field_234919_h_;
	}

	public RegistryKey<World> getEndKey() {
		return World.field_234920_i_;
	}

	public UseAction getVisualBlockAction() {
		return UseAction.BOW;
	}

	public PlayerEntity getPlayer(UUID playerID) {
		if (ServerLifecycleHooks.getCurrentServer() != null)
			return ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(playerID);
		else
			return null;
	}

	public void pushRevelationToast(ItemStack renderedStack, int xp, int knowledge) {
		// NO-OP
	}

}
