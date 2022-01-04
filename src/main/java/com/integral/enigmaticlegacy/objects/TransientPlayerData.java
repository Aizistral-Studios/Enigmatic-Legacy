package com.integral.enigmaticlegacy.objects;

import java.lang.ref.WeakReference;
import java.util.UUID;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.items.ForbiddenFruit;
import com.integral.enigmaticlegacy.items.MagmaHeart;
import com.integral.enigmaticlegacy.packets.clients.PacketSyncTransientData;

import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

public class TransientPlayerData {

	// TODO Wrap variables into param objects

	public static TransientPlayerData get(Player player) {
		boolean clientOnly = player.level.isClientSide;

		if (EnigmaticLegacy.proxy.getTransientPlayerData(clientOnly).containsKey(player))
			return EnigmaticLegacy.proxy.getTransientPlayerData(clientOnly).get(player);
		else {
			TransientPlayerData data = new TransientPlayerData(player);
			EnigmaticLegacy.proxy.getTransientPlayerData(clientOnly).put(player, data);

			return data;
		}
	}

	public static boolean set(Player player, TransientPlayerData data) {
		boolean clientOnly = player.level.isClientSide;

		if (data != null) {
			EnigmaticLegacy.proxy.getTransientPlayerData(clientOnly).put(player, data);
			return true;
		} else
			return false;
	}

	public void syncToClients(float blockRadius) {
		Player player = this.getPlayer();
		EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getX(), player.getY(), player.getZ(), blockRadius, player.level.dimension())), new PacketSyncTransientData(this));
	}

	public void syncToPlayer() {
		if (this.getPlayer() instanceof ServerPlayer) {
			EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) this.getPlayer()), new PacketSyncTransientData(this));
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void syncToServer() {
		// NO-OP
	}

	private final WeakReference<Player> player;
	private int fireImmunityTimer;
	private int fireImmunityTimerCap;
	public int spellstoneCooldown;
	private int fireImmunityTimerLast;
	private Boolean consumedForbiddenFruit;

	public boolean needsSync = false;

	public TransientPlayerData(Player thePlayer) {
		this.player = new WeakReference<>(thePlayer);
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

		if (MagmaHeart.traitorBar.getValue()) {
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
			this.spellstoneCooldown = newValue;

			for (Item spellstone : EnigmaticLegacy.spellstoneList) {
				this.player.get().getCooldowns().addCooldown(spellstone, this.spellstoneCooldown);
			}
		}
	}

	public Boolean getConsumedForbiddenFruit() {
		return this.consumedForbiddenFruit != null ? this.consumedForbiddenFruit : (this.consumedForbiddenFruit = SuperpositionHandler.getPersistentBoolean(this.player.get(), ForbiddenFruit.consumedFruitTag, false));
	}

	public void setConsumedForbiddenFruit(Boolean consumedForbiddenFruit){
		this.consumedForbiddenFruit = consumedForbiddenFruit;
	}

	public Player getPlayer() {
		return this.player.get();
	}

	public static FriendlyByteBuf encode(TransientPlayerData data, FriendlyByteBuf buf) {
		buf.writeUUID(data.player.get().getUUID());
		buf.writeInt(data.spellstoneCooldown);
		buf.writeInt(data.fireImmunityTimer);
		buf.writeInt(data.fireImmunityTimerCap);
		buf.writeInt(data.fireImmunityTimerLast);
		buf.writeBoolean(data.getConsumedForbiddenFruit());

		return buf;
	}

	public static TransientPlayerData decode(FriendlyByteBuf buf) {
		UUID playerID = buf.readUUID();
		int spellstoneCooldown = buf.readInt();
		int fireImmunityTimer = buf.readInt();
		int fireImmunityTimerCap = buf.readInt();
		int fireImmunityTimerLast = buf.readInt();
		boolean consumedForbiddenFruit = buf.readBoolean();

		Player player = EnigmaticLegacy.proxy.getPlayer(playerID);

		if (player != null) {
			TransientPlayerData data = new TransientPlayerData(player);
			data.spellstoneCooldown = spellstoneCooldown;
			data.fireImmunityTimer = fireImmunityTimer;
			data.fireImmunityTimerCap = fireImmunityTimerCap;
			data.fireImmunityTimerLast = fireImmunityTimerLast;
			data.consumedForbiddenFruit = consumedForbiddenFruit;

			return data;
		}

		return null;
	}

}
