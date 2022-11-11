package com.aizistral.enigmaticlegacy.packets.clients;

import java.util.UUID;
import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class PacketSyncPlayTime {
	private UUID playerID;
	private int timeWithCurses, timeWithoutCurses;

	public PacketSyncPlayTime(UUID playerID, int timeWithCurses, int timeWithoutCurses) {
		this.playerID = playerID;
		this.timeWithCurses = timeWithCurses;
		this.timeWithoutCurses =  timeWithoutCurses;
	}

	public static void encode(PacketSyncPlayTime msg, FriendlyByteBuf buf) {
		buf.writeUUID(msg.playerID);
		buf.writeInt(msg.timeWithCurses);
		buf.writeInt(msg.timeWithoutCurses);
	}

	public static PacketSyncPlayTime decode(FriendlyByteBuf buf) {
		return new PacketSyncPlayTime(buf.readUUID(), buf.readInt(), buf.readInt());
	}

	public static void handle(PacketSyncPlayTime msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			EnigmaticLegacy.PROXY.cacheStats(msg.playerID, msg.timeWithoutCurses, msg.timeWithCurses);
		});

		ctx.get().setPacketHandled(true);
	}
}

