package com.integral.enigmaticlegacy.packets.server;

import java.util.function.Supplier;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SOpenWindowPacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Packet for opening Ender Chest inventory to a player.
 * @author Integral
 */

public class PacketEnderRingKey {
	
	private boolean pressed;

	  public PacketEnderRingKey(boolean pressed) {
	    this.pressed = pressed;
	  }

	  public static void encode(PacketEnderRingKey msg, PacketBuffer buf) {
	     buf.writeBoolean(msg.pressed);
	  }

	  public static PacketEnderRingKey decode(PacketBuffer buf) {
	    return new PacketEnderRingKey(buf.readBoolean());
	 }
	  
	  public static void handle(PacketEnderRingKey msg, Supplier<NetworkEvent.Context> ctx) {

		    ctx.get().enqueueWork(() -> {
		      ServerPlayerEntity playerServ = ctx.get().getSender();
		      
		      	 if (playerServ.openContainer.windowId == 0)
		      	 if (SuperpositionHandler.hasCurio(playerServ, EnigmaticLegacy.enderRing)) {
				 ChestContainer container = ChestContainer.createGeneric9X3(playerServ.currentWindowId+1, playerServ.inventory, playerServ.getInventoryEnderChest());
				 
				 playerServ.currentWindowId = container.windowId;
				 playerServ.connection.sendPacket(new SOpenWindowPacket(container.windowId, container.getType(), new TranslationTextComponent("container.enderchest")));
		         container.addListener(playerServ);
		         playerServ.openContainer = container;
		         net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open(playerServ, playerServ.openContainer));
		      
		         playerServ.world.playSound(null, playerServ.getPosition(), SoundEvents.BLOCK_ENDER_CHEST_OPEN, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random()*0.2)));
		         
		      	 }
		    });
		    ctx.get().setPacketHandled(true);
	  }

}
