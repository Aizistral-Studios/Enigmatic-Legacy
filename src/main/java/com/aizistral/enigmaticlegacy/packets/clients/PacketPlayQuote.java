package com.aizistral.enigmaticlegacy.packets.clients;

import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.client.Quote;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class PacketPlayQuote {
	private Quote quote;
	private int delay;

	public PacketPlayQuote(Quote quote, int delay) {
		this.quote = quote;
		this.delay = delay;
	}

	public static void encode(PacketPlayQuote msg, FriendlyByteBuf buf) {
		buf.writeInt(msg.quote.getID());
		buf.writeInt(msg.delay);
	}

	public static PacketPlayQuote decode(FriendlyByteBuf buf) {
		return new PacketPlayQuote(Quote.getByID(buf.readInt()), buf.readInt());
	}

	public static void handle(PacketPlayQuote msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			msg.quote.play(msg.delay);
		});
		ctx.get().setPacketHandled(true);
	}

}

