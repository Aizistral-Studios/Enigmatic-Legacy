package com.aizistral.enigmaticlegacy.packets.clients;

import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet for bursting out particles for UltimateWitherSkull.
 * @author Integral
 */

public class PacketWitherParticles {

	private double x;
	private double y;
	private double z;
	private int num;
	private boolean check;
	private int mode;

	public PacketWitherParticles(double x, double y, double z, int number, boolean checkSettings) {
		this(x, y, z, number, checkSettings, 0);
	}

	public PacketWitherParticles(double x, double y, double z, int number, boolean checkSettings, int mode) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.num = number;
		this.check = checkSettings;
		this.mode = mode;
	}

	public static void encode(PacketWitherParticles msg, FriendlyByteBuf buf) {
		buf.writeDouble(msg.x);
		buf.writeDouble(msg.y);
		buf.writeDouble(msg.z);
		buf.writeInt(msg.num);
		buf.writeBoolean(msg.check);
		buf.writeInt(msg.mode);
	}

	public static PacketWitherParticles decode(FriendlyByteBuf buf) {
		return new PacketWitherParticles(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readInt(), buf.readBoolean(), buf.readInt());
	}

	public static void handle(PacketWitherParticles msg, Supplier<NetworkEvent.Context> ctx) {

		ctx.get().enqueueWork(() -> {

			LocalPlayer player = Minecraft.getInstance().player;

			int amount = msg.num;

			if (msg.check) {
				amount *= SuperpositionHandler.getParticleMultiplier();
			}

			if (msg.mode == 0) {
				for (int counter = 0; counter <= amount; counter++) {
					player.level().addParticle(ParticleTypes.LARGE_SMOKE, true, msg.x, msg.y, msg.z, (Math.random() - 0.5D) * 0.2D, (Math.random() - 0.5D) * 0.2D, (Math.random() - 0.5D) * 0.2D);
				}
			} else if (msg.mode == 1) {
				for (int counter = 0; counter <= amount; counter++) {
					player.level().addParticle(ParticleTypes.LARGE_SMOKE, true, msg.x + (Math.random()-0.5D), msg.y, msg.z + (Math.random()-0.5D), 0.0D, 0.0D, 0.0D);
				}
			}

		});
		ctx.get().setPacketHandled(true);
	}

}
