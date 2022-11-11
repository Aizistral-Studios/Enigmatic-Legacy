package com.aizistral.enigmaticlegacy.packets.clients;

import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.helpers.PatchouliHelper;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

public class PacketSetEntryState {
	private boolean isRead;
	private String entryId;

	public PacketSetEntryState(boolean read, String id) {
		this.isRead = read;
		this.entryId = id;
	}

	public static void encode(PacketSetEntryState msg, FriendlyByteBuf buf) {
		buf.writeBoolean(msg.isRead);
		buf.writeUtf(msg.entryId);
	}

	public static PacketSetEntryState decode(FriendlyByteBuf buf) {
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

