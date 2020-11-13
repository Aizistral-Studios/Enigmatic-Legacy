package com.integral.enigmaticlegacy.packets.clients;

import java.util.function.Supplier;

import com.integral.enigmaticlegacy.objects.TransientPlayerData;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketSyncTransientData {

	private TransientPlayerData playerData;

	public PacketSyncTransientData(TransientPlayerData data) {
		this.playerData = data;
	}

	public static void encode(PacketSyncTransientData msg, PacketBuffer buf) {
		TransientPlayerData.encode(msg.playerData, buf);
	}

	public static PacketSyncTransientData decode(PacketBuffer buf) {
		TransientPlayerData data = TransientPlayerData.decode(buf);
		return new PacketSyncTransientData(data);
	}

	public static void handle(PacketSyncTransientData msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {

			// Syncying to server must have additional checks to prevent exploits.
			// In current implementation it's just not possible, since no need yet exists.

			if (msg.playerData.getPlayer().world.isRemote) {
				TransientPlayerData.set(msg.playerData.getPlayer(), msg.playerData);
			}
		});

		ctx.get().setPacketHandled(true);
	}

}
