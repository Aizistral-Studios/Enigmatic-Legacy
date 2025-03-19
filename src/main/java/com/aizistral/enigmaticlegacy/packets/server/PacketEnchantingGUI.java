package com.aizistral.enigmaticlegacy.packets.server;

import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet for opening Ender Chest inventory to a player.
 * @author Integral
 */

public class PacketEnchantingGUI {

	public PacketEnchantingGUI() {
		// NO-OP
	}

	public static void encode(PacketEnchantingGUI msg, FriendlyByteBuf buf) {
		// NO-OP
	}

	public static PacketEnchantingGUI decode(FriendlyByteBuf buf) {
		return new PacketEnchantingGUI();
	}

	public static void handle(PacketEnchantingGUI msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			SuperpositionHandler.grantAdvancement(ctx.get().getSender(), new ResourceLocation(EnigmaticLegacy.MODID, "book/enchantments/generic"));
		});
		ctx.get().setPacketHandled(true);
	}

}
