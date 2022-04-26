package com.integral.enigmaticlegacy.packets.clients;

import java.util.Random;
import java.util.function.Supplier;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.items.AstralBreaker;

import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;


public class PacketUpdateCompass {
	private int x, y, z;
	private boolean noValid;

	public PacketUpdateCompass(int x, int y, int z, boolean noValid) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.noValid = noValid;
	}

	public static void encode(PacketUpdateCompass msg, FriendlyByteBuf buf) {
		buf.writeInt(msg.x);
		buf.writeInt(msg.y);
		buf.writeInt(msg.z);
		buf.writeBoolean(msg.noValid);
	}

	public static PacketUpdateCompass decode(FriendlyByteBuf buf) {
		return new PacketUpdateCompass(buf.readInt(), buf.readInt(), buf.readInt(), buf.readBoolean());
	}

	public static void handle(PacketUpdateCompass msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			EnigmaticLegacy.soulCompass.setNearestCrystal(msg.noValid ? null : new BlockPos(msg.x, msg.y, msg.z));
		});

		ctx.get().setPacketHandled(true);
	}

}

