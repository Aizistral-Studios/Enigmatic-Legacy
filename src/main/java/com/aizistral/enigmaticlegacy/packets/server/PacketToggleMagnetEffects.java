package com.aizistral.enigmaticlegacy.packets.server;

import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.objects.TransientPlayerData;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet for toggling magnet rings effects.
 * @author Integral
 */

public class PacketToggleMagnetEffects {

	public PacketToggleMagnetEffects() {
		// NO-OP
	}

	public static void encode(PacketToggleMagnetEffects msg, FriendlyByteBuf buf) {
		// NO-OP
	}

	public static PacketToggleMagnetEffects decode(FriendlyByteBuf buf) {
		return new PacketToggleMagnetEffects();
	}

	public static void handle(PacketToggleMagnetEffects msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayer playerServ = ctx.get().getSender();
			TransientPlayerData data = TransientPlayerData.get(playerServ);

			data.setDisabledMagnetRingEffects(!data.getDisabledMagnetRingEffects());
			data.syncToAllClients();
		});

		ctx.get().setPacketHandled(true);
	}

}