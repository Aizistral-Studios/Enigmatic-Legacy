package com.integral.enigmaticlegacy.packets.server;

import java.util.function.Supplier;

import com.integral.enigmaticlegacy.gui.containers.LoreInscriberContainer;
import com.integral.enigmaticlegacy.handlers.EnigmaticEventHandler;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Packet for updating stored anvil field for targeted player on server side.
 * @author Integral
 */

public class PacketInkwellField {

	private String field;

	  public PacketInkwellField(String field) {
	    this.field = field;
	  }

	  public static void encode(PacketInkwellField msg, PacketBuffer buf) {
	     buf.writeString(msg.field);
	  }

	  public static PacketInkwellField decode(PacketBuffer buf) {
	    return new PacketInkwellField(buf.readString(128));
	 }

	  public static void handle(PacketInkwellField msg, Supplier<NetworkEvent.Context> ctx) {

		    ctx.get().enqueueWork(() -> {
		      ServerPlayerEntity playerServ = ctx.get().getSender();

		      if (playerServ.openContainer instanceof LoreInscriberContainer) {
		    	  LoreInscriberContainer container = (LoreInscriberContainer) playerServ.openContainer;
		    	  container.updateItemName(msg.field);
		      }

		    });
		    ctx.get().setPacketHandled(true);
	  }

}

