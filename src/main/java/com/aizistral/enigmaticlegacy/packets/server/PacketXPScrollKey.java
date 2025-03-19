package com.aizistral.enigmaticlegacy.packets.server;

import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet for handing the keybind of Scroll of Ageless Wisdom.
 * @author Integral
 */

public class PacketXPScrollKey {

	private boolean pressed;

	public PacketXPScrollKey(boolean pressed) {
		this.pressed = pressed;
	}

	public static void encode(PacketXPScrollKey msg, FriendlyByteBuf buf) {
		buf.writeBoolean(msg.pressed);
	}

	public static PacketXPScrollKey decode(FriendlyByteBuf buf) {
		return new PacketXPScrollKey(buf.readBoolean());
	}

	public static void handle(PacketXPScrollKey msg, Supplier<NetworkEvent.Context> ctx) {

		ctx.get().enqueueWork(() -> {
			ServerPlayer playerServ = ctx.get().getSender();

			if (SuperpositionHandler.hasCurio(playerServ, EnigmaticItems.XP_SCROLL)) {
				ItemStack scroll = SuperpositionHandler.getCurioStack(playerServ, EnigmaticItems.XP_SCROLL);
				EnigmaticItems.XP_SCROLL.trigger(playerServ.level(), scroll, playerServ, InteractionHand.MAIN_HAND, false);
			}

		});
		ctx.get().setPacketHandled(true);
	}

}

