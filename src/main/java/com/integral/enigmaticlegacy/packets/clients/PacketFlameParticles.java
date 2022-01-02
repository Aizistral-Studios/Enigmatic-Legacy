package com.integral.enigmaticlegacy.packets.clients;

import java.util.Random;
import java.util.function.Supplier;

import com.integral.enigmaticlegacy.items.AstralBreaker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.ParticleStatus;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Packet for bursting out a bunch of flame breath particles.
 * @author Integral
 */

public class PacketFlameParticles {

	public static final Random random = new Random();

	private double x;
	private double y;
	private double z;
	private int num;
	private boolean check;

	public PacketFlameParticles(double x, double y, double z, int number, boolean checkSettings) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.num = number;
		this.check = checkSettings;
	}

	public static void encode(PacketFlameParticles msg, PacketBuffer buf) {
		buf.writeDouble(msg.x);
		buf.writeDouble(msg.y);
		buf.writeDouble(msg.z);
		buf.writeInt(msg.num);
		buf.writeBoolean(msg.check);
	}

	public static PacketFlameParticles decode(PacketBuffer buf) {
		return new PacketFlameParticles(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readInt(), buf.readBoolean());
	}

	public static void handle(PacketFlameParticles msg, Supplier<NetworkEvent.Context> ctx) {
		if (AstralBreaker.flameParticlesToggle.getValue()) {
			ctx.get().enqueueWork(() -> {
				ClientPlayerEntity player = Minecraft.getInstance().player;

				int amount = msg.num;
				float modifier = 1.0F;

				if (msg.check) {
					if (Minecraft.getInstance().options.particles == ParticleStatus.MINIMAL) {
						modifier = 0.20F;
					} else if (Minecraft.getInstance().options.particles == ParticleStatus.DECREASED) {
						modifier = 0.35F;
					} else {
						modifier = 0.65F;
					}
				}

				amount *= modifier;

				for (int counter = 0; counter <= amount; counter++) {
					player.level.addParticle(ParticleTypes.FLAME, true, msg.x + (Math.random() - 0.5), msg.y + (Math.random() - 0.5), msg.z + (Math.random() - 0.5), (Math.random() - 0.5D) * 0.1D, (Math.random() - 0.5D) * 0.1D, (Math.random() - 0.5D) * 0.1D);
				}

			});

			ctx.get().setPacketHandled(true);
		}
	}
}
