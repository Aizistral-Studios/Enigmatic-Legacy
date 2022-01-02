package com.integral.enigmaticlegacy.packets.server;

import java.util.function.Supplier;

import com.integral.enigmaticlegacy.api.items.ISpellstone;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.world.entity.player.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Packet for triggering active ability of the spellstone.
 * @author Integral
 */

public class PacketSpellstoneKey {

	private boolean pressed;

	  public PacketSpellstoneKey(boolean pressed) {
	    this.pressed = pressed;

	  }

	  public static void encode(PacketSpellstoneKey msg, PacketBuffer buf) {
	     buf.writeBoolean(msg.pressed);
	  }

	  public static PacketSpellstoneKey decode(PacketBuffer buf) {
	    return new PacketSpellstoneKey(buf.readBoolean());
	 }

	  public static void handle(PacketSpellstoneKey msg, Supplier<NetworkEvent.Context> ctx) {

		    ctx.get().enqueueWork(() -> {
		      ServerPlayer playerServ = ctx.get().getSender();
		      
		      if (SuperpositionHandler.hasSpellstone(playerServ)) {
		    	  ItemStack spellstone = SuperpositionHandler.getSpellstone(playerServ);
		    	  ISpellstone function = (ISpellstone) spellstone.getItem();
		    	  
		    	  function.triggerActiveAbility(playerServ.level, playerServ, spellstone);
		      }

		    });
		    ctx.get().setPacketHandled(true);
	  }

}
