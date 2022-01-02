package com.integral.enigmaticlegacy.packets.server;

import java.util.function.Supplier;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.items.XPScroll;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Packet for handing the keybind of Scroll of Ageless Wisdom.
 * @author Integral
 */

public class PacketXPScrollKey {
	
	private boolean pressed;

	  public PacketXPScrollKey(boolean pressed) {
	    this.pressed = pressed;
	  }

	  public static void encode(PacketXPScrollKey msg, PacketBuffer buf) {
	     buf.writeBoolean(msg.pressed);
	  }

	  public static PacketXPScrollKey decode(PacketBuffer buf) {
	    return new PacketXPScrollKey(buf.readBoolean());
	 }
	  
	  public static void handle(PacketXPScrollKey msg, Supplier<NetworkEvent.Context> ctx) {

		    ctx.get().enqueueWork(() -> {
		      ServerPlayerEntity playerServ = ctx.get().getSender();
				
		      	 if (SuperpositionHandler.hasCurio(playerServ, EnigmaticLegacy.xpScroll)) {
		      		 ItemStack scroll = SuperpositionHandler.getCurioStack(playerServ, EnigmaticLegacy.xpScroll);
		      		 ((XPScroll)EnigmaticLegacy.xpScroll).trigger(playerServ.level, scroll, playerServ, Hand.MAIN_HAND, false);
		      	 }
		      	 
		    });
		    ctx.get().setPacketHandled(true);
	  }

}

