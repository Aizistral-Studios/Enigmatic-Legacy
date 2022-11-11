package com.aizistral.enigmaticlegacy.packets.server;

import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.gui.containers.LoreInscriberContainer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet for updating stored anvil field for targeted player on server side.
 * @author Integral
 */

public class PacketInkwellField {

	private String field;

	  public PacketInkwellField(String field) {
	    this.field = field;
	  }

	  public static void encode(PacketInkwellField msg, FriendlyByteBuf buf) {
	     buf.writeUtf(msg.field);
	  }

	  public static PacketInkwellField decode(FriendlyByteBuf buf) {
	    return new PacketInkwellField(buf.readUtf(128));
	 }

	  public static void handle(PacketInkwellField msg, Supplier<NetworkEvent.Context> ctx) {

		    ctx.get().enqueueWork(() -> {
		      ServerPlayer playerServ = ctx.get().getSender();

		      if (playerServ.containerMenu instanceof LoreInscriberContainer) {
		    	  LoreInscriberContainer container = (LoreInscriberContainer) playerServ.containerMenu;
		    	  container.updateItemName(msg.field);
		      }

		    });
		    ctx.get().setPacketHandled(true);
	  }

}

