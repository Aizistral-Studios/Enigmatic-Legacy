package com.integral.enigmaticlegacy.packets.clients;

import java.util.Random;
import java.util.function.Supplier;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.items.AstralBreaker;

import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class PacketPermadeath {

	public PacketPermadeath() {
		// NO-OP
	}

	public static void encode(PacketPermadeath msg, FriendlyByteBuf buf) {
		// NO-OP
	}

	public static PacketPermadeath decode(FriendlyByteBuf buf) {
		return new PacketPermadeath();
	}

	public static void handle(PacketPermadeath msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> EnigmaticLegacy.proxy.displayPermadeathScreen());
		ctx.get().setPacketHandled(true);
	}
}

