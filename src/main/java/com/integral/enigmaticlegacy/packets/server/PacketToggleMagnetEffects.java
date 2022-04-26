package com.integral.enigmaticlegacy.packets.server;

import java.util.function.Supplier;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.objects.TransientPlayerData;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;

/**
 * Packet for toggling magnet rings effects.
 * @author Integral
 */

public class PacketToggleMagnetEffects {

	public PacketToggleMagnetEffects() {
		// NO-OP
	}

	public static void encode(PacketToggleMagnetEffects msg, FriendlyByteBuf buf) {
		// NO-OP
	}

	public static PacketToggleMagnetEffects decode(FriendlyByteBuf buf) {
		return new PacketToggleMagnetEffects();
	}

	public static void handle(PacketToggleMagnetEffects msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayer playerServ = ctx.get().getSender();
			TransientPlayerData data = TransientPlayerData.get(playerServ);

			data.setDisabledMagnetRingEffects(!data.getDisabledMagnetRingEffects());
			data.syncToAllClients();
		});

		ctx.get().setPacketHandled(true);
	}

}