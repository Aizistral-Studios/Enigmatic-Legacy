package com.integral.enigmaticlegacy.packets.server;

import java.util.function.Supplier;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.ISpellstone;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.items.AngelBlessing;
import com.integral.enigmaticlegacy.items.EnigmaticItem;
import com.integral.enigmaticlegacy.items.EyeOfNebula;
import com.integral.enigmaticlegacy.items.GolemHeart;
import com.integral.enigmaticlegacy.items.MagmaHeart;
import com.integral.enigmaticlegacy.items.OceanStone;
import com.integral.enigmaticlegacy.items.VoidPearl;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
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
		      ServerPlayerEntity playerServ = ctx.get().getSender();

		      System.out.println("Handling packet");
		      
		      if (SuperpositionHandler.hasSpellstone(playerServ)) {
		    	  ItemStack spellstone = SuperpositionHandler.getSpellstone(playerServ);
		    	  ISpellstone function = (ISpellstone) spellstone.getItem();
		    	  
		    	  System.out.println("Triggering ability of: " + spellstone.getItem().getName());
		    	  
		    	  function.triggerActiveAbility(playerServ.world, playerServ, spellstone);
		      }

		    });
		    ctx.get().setPacketHandled(true);
	  }

}
