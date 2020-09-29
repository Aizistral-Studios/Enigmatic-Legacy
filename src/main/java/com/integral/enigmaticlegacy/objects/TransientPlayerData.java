package com.integral.enigmaticlegacy.objects;

import java.util.HashMap;
import java.util.UUID;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.packets.clients.PacketRecallParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketSyncTransientData;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class TransientPlayerData {

	public static TransientPlayerData get(PlayerEntity player) {
		boolean clientOnly = player.world.isRemote;

		if (EnigmaticLegacy.proxy.getTransientPlayerData(clientOnly).containsKey(player))
			return EnigmaticLegacy.proxy.getTransientPlayerData(clientOnly).get(player);
		else {
			TransientPlayerData data = new TransientPlayerData(player);
			EnigmaticLegacy.proxy.getTransientPlayerData(clientOnly).put(player, data);

			return data;
		}
	}

	public static boolean set(PlayerEntity player, TransientPlayerData data) {
		boolean clientOnly = player.world.isRemote;

		if (data != null) {
			EnigmaticLegacy.proxy.getTransientPlayerData(clientOnly).put(player, data);
			return true;
		} else
			return false;
	}

	public void syncToClients(float blockRadius) {
		PlayerEntity player = this.getPlayer();
		EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getPosX(), player.getPosY(), player.getPosZ(), blockRadius, player.world.func_234923_W_())), new PacketSyncTransientData(this));
	}

	public void syncToPlayer() {
		if (this.getPlayer() instanceof ServerPlayerEntity) {
			ServerPlayerEntity serverPlayer = (ServerPlayerEntity) this.getPlayer();

			EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new PacketSyncTransientData(this));
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void syncToServer() {
		// NO-OP
	}

	private final PlayerEntity player;
	private int fireImmunityTimer;
	private int fireImmunityTimerCap;
	private int spellstoneCooldown;
	private int fireImmunityTimerLast;

	public boolean needsSync = false;

	public TransientPlayerData(PlayerEntity thePlayer) {
		this.player = thePlayer;
		this.fireImmunityTimer = 0;
		this.spellstoneCooldown = 0;
		this.fireImmunityTimerLast = 0;
		this.fireImmunityTimerCap = 300*100 + this.getFireDiff();
	}

	public int getFireDiff() {
		return 20*100;
	}

	public int getFireImmunityTimerLast() {
		return this.fireImmunityTimerLast;
	}

	public void setFireImmunityTimerLast(int fireImmunityTimerLast) {
		this.fireImmunityTimerLast = fireImmunityTimerLast;
	}

	public int getFireImmunityTimerCap() {
		return this.fireImmunityTimerCap;
	}

	@OnlyIn(Dist.CLIENT)
	public float getFireImmunityFraction(float partialTick) {
		float difference = (float)this.fireImmunityTimer - (float)this.fireImmunityTimerLast;
		difference *= 0;

		double coolFiller;

		if (ConfigHandler.TRAITOR_BAR.getValue()) {
			coolFiller = SuperpositionHandler.parabolicFunction(0, this.fireImmunityTimerCap - this.getFireDiff(), (double)this.fireImmunityTimerLast+(double)difference);
		} else {
			coolFiller = SuperpositionHandler.flippedParabolicFunction(0, this.fireImmunityTimerCap - this.getFireDiff(), (double)this.fireImmunityTimerLast+(double)difference);
		}

		return (float)coolFiller;
	}

	public int getFireImmunityTimer() {
		return this.fireImmunityTimer;
	}

	public int getSpellstoneCooldown() {
		return this.spellstoneCooldown;
	}

	public void setFireImmunityTimer(int fireImmunityTimer) {
		int newValue = Math.min(Math.max(fireImmunityTimer, 0), this.fireImmunityTimerCap);

		if (newValue != this.fireImmunityTimer) {
			this.needsSync = true;
		}

		this.fireImmunityTimerLast = fireImmunityTimer;
		this.fireImmunityTimer = newValue;
	}

	public void setSpellstoneCooldown(int spellstoneCooldown) {
		int newValue = Math.max(spellstoneCooldown, 0);

		if (this.spellstoneCooldown != newValue) {
			this.needsSync = true;

			for (Item spellstone : EnigmaticLegacy.spellstoneList) {
				this.player.getCooldownTracker().setCooldown(spellstone, this.spellstoneCooldown);
			}
		}
	}

	public PlayerEntity getPlayer() {
		return this.player;
	}

	public static PacketBuffer encode(TransientPlayerData data, PacketBuffer buf) {
		buf.writeUniqueId(data.player.getUniqueID());
		buf.writeInt(data.spellstoneCooldown);
		buf.writeInt(data.fireImmunityTimer);
		buf.writeInt(data.fireImmunityTimerCap);
		buf.writeInt(data.fireImmunityTimerLast);

		return buf;
	}

	public static TransientPlayerData decode(PacketBuffer buf) {
		UUID playerID = buf.readUniqueId();
		int spellstoneCooldown = buf.readInt();
		int fireImmunityTimer = buf.readInt();
		int fireImmunityTimerCap = buf.readInt();
		int fireImmunityTimerLast = buf.readInt();

		PlayerEntity player = EnigmaticLegacy.proxy.getPlayer(playerID);

		if (player != null) {
			TransientPlayerData data = new TransientPlayerData(player);
			data.spellstoneCooldown = spellstoneCooldown;
			data.fireImmunityTimer = fireImmunityTimer;
			data.fireImmunityTimerCap = fireImmunityTimerCap;
			data.fireImmunityTimerLast = fireImmunityTimerLast;

			return data;
		}

		return null;
	}

}
