package com.aizistral.enigmaticlegacy.packets.clients;
import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet for playing respective sound and animation on client side,
 * whenever item is picked up by player.
 * @author Integral
 */

public class PacketHandleItemPickup {
	private int item_id;
	private int pickuper_id;

	public PacketHandleItemPickup(int itemID, int pickuperID) {
		this.item_id = itemID;
		this.pickuper_id = pickuperID;
	}

	public static void encode(PacketHandleItemPickup msg, FriendlyByteBuf buf) {
		buf.writeInt(msg.item_id);
		buf.writeInt(msg.pickuper_id);
	}

	public static PacketHandleItemPickup decode(FriendlyByteBuf buf) {
		return new PacketHandleItemPickup(buf.readInt(), buf.readInt());
	}

	public static void handle(PacketHandleItemPickup msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			EnigmaticLegacy.PROXY.handleItemPickup(msg.pickuper_id, msg.item_id);
		});

		ctx.get().setPacketHandled(true);
	}

}

