package com.aizistral.enigmaticlegacy.packets.clients;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import vazkii.patchouli.client.base.PersistentData;

public class PacketPatchouliForce {

	public PacketPatchouliForce() {
		// NO-OP
	}

	public static void encode(PacketPatchouliForce msg, FriendlyByteBuf buf) {
		// NO-OP
	}

	public static PacketPatchouliForce decode(FriendlyByteBuf buf) {
		return new PacketPatchouliForce();
	}

	public static void handle(PacketPatchouliForce msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PersistentData.data.bookGuiScale = 4;
		});

		ctx.get().setPacketHandled(true);
	}
}
