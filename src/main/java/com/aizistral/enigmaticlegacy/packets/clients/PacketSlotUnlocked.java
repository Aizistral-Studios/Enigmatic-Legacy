package com.aizistral.enigmaticlegacy.packets.clients;
import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.handlers.EnigmaticEventHandler;
import com.aizistral.enigmaticlegacy.objects.SlotUnlockedToast;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet for displaying Curio slot unlock notification.
 * @author Integral
 */

public class PacketSlotUnlocked {
	
	private String type;

	  public PacketSlotUnlocked(String type) {
		  this.type = type;
	  }

	  public static void encode(PacketSlotUnlocked msg, FriendlyByteBuf buf) {
		  buf.writeUtf(msg.type);
	  }

	  public static PacketSlotUnlocked decode(FriendlyByteBuf buf) {
	    return new PacketSlotUnlocked(buf.readUtf());
	 }

	  public static void handle(PacketSlotUnlocked msg, Supplier<NetworkEvent.Context> ctx) {

		    ctx.get().enqueueWork(() -> {
		      LocalPlayer player = Minecraft.getInstance().player;
		      ItemStack stack;
		      
		      if (msg.type.equals("ring"))
		    	  stack = new ItemStack(EnigmaticItems.IRON_RING);
		      else if (msg.type.equals("scroll"))
		    	  stack = new ItemStack(EnigmaticItems.THICC_SCROLL);
		      else if (msg.type.equals("spellstone"))
		    	  stack = new ItemStack(EnigmaticItems.VOID_PEARL);
		      else
		    	  stack = new ItemStack(EnigmaticItems.IRON_RING);
		      
		      Toast toast = new SlotUnlockedToast(stack, msg.type);
		      EnigmaticEventHandler.SCHEDULED_TOASTS.add(toast);
		      EnigmaticEventHandler.DEFERRED_TOASTS.put(player, 5);
		      
		    });
		    ctx.get().setPacketHandled(true);
	  }

}

