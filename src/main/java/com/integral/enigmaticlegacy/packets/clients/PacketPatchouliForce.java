package com.integral.enigmaticlegacy.packets.clients;

import java.util.Random;
import java.util.function.Supplier;

import com.integral.enigmaticlegacy.items.AstralBreaker;

import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import vazkii.patchouli.client.base.PersistentData;

public class PacketPatchouliForce {

	public PacketPatchouliForce() {
		// NO-OP
	}

	public static void encode(PacketPatchouliForce msg, FriendlyByteBuf buf) {
		// NO-OP
	}

	public static PacketPatchouliForce decode(FriendlyByteBuf buf) {
		return new PacketPatchouliForce();
	}

	public static void handle(PacketPatchouliForce msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PersistentData.data.bookGuiScale = 4;
		});

		ctx.get().setPacketHandled(true);
	}
}
