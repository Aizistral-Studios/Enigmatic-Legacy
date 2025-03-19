package com.aizistral.enigmaticlegacy.packets.clients;

import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.objects.TransientPlayerData;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class PacketSyncTransientData {
	private TransientPlayerData playerData;

	public PacketSyncTransientData(TransientPlayerData data) {
		this.playerData = data;
	}

	public static void encode(PacketSyncTransientData msg, FriendlyByteBuf buf) {
		TransientPlayerData.encode(msg.playerData, buf);
	}

	public static PacketSyncTransientData decode(FriendlyByteBuf buf) {
		TransientPlayerData data = TransientPlayerData.decode(buf);
		return new PacketSyncTransientData(data);
	}

	public static void handle(PacketSyncTransientData msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {

			// Syncying to server must have additional checks to prevent exploits.
			// In current implementation it's just not possible, since no need yet exists.

			if (msg.playerData != null)
				if (msg.playerData.getPlayer().level().isClientSide) {
					TransientPlayerData.set(msg.playerData.getPlayer(), msg.playerData);
				}
		});

		ctx.get().setPacketHandled(true);
	}

}
