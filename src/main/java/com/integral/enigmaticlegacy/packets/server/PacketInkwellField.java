package com.integral.enigmaticlegacy.packets.server;

import java.util.function.Supplier;

import com.integral.enigmaticlegacy.gui.containers.LoreInscriberContainer;

import net.minecraft.world.entity.player.ServerPlayer;
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
	     buf.writeUtf(msg.field);
	  }

	  public static PacketInkwellField decode(PacketBuffer buf) {
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

