package com.integral.enigmaticlegacy.packets.clients;

import java.util.function.Supplier;

import com.integral.enigmaticlegacy.helpers.PatchouliHelper;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketSetEntryState {
	private boolean isRead;
	private String entryId;

	public PacketSetEntryState(boolean read, String id) {
		this.isRead = read;
		this.entryId = id;
	}

	public static void encode(PacketSetEntryState msg, PacketBuffer buf) {
		buf.writeBoolean(msg.isRead);
		buf.writeUtf(msg.entryId);
	}

	public static PacketSetEntryState decode(PacketBuffer buf) {
		return new PacketSetEntryState(buf.readBoolean(), buf.readUtf());
	}

	public static void handle(PacketSetEntryState msg, Supplier<NetworkEvent.Context> ctx) {

		ctx.get().enqueueWork(() -> {

			if (msg.isRead) {
				PatchouliHelper.markEntryRead(new ResourceLocation(msg.entryId));
			} else
				PatchouliHelper.markEntryUnread(new ResourceLocation(msg.entryId));

		});
		ctx.get().setPacketHandled(true);
	}

}

