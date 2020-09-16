package com.integral.enigmaticlegacy.packets.server;

import java.util.function.Supplier;

import com.integral.enigmaticlegacy.handlers.EnigmaticEventHandler;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Packet for updating stored anvil field for targeted player on server side.
 * @author Integral
 */

public class PacketAnvilField {

	private String field;

	public PacketAnvilField(String field) {
		this.field = field;
	}

	public static void encode(PacketAnvilField msg, PacketBuffer buf) {
		buf.writeString(msg.field);
	}

	public static PacketAnvilField decode(PacketBuffer buf) {
		return new PacketAnvilField(buf.readString(128));
	}

	public static void handle(PacketAnvilField msg, Supplier<NetworkEvent.Context> ctx) {

		ctx.get().enqueueWork(() -> {
			ServerPlayerEntity playerServ = ctx.get().getSender();

			//EnigmaticEventHandler.anvilFields.put(playerServ, msg.field);

		});
		ctx.get().setPacketHandled(true);
	}

}
