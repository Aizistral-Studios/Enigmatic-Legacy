package com.integral.enigmaticlegacy.packets.clients;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Packet for setting arrows's rotations on client side.
 * @author Integral
 */

public class PacketForceArrowRotations {

	private int entityID;
	private double motionX, motionY, motionZ;
	private float rotationYaw;
	private float rotationPitch;

	public PacketForceArrowRotations(int entityID, float rotationYaw, float rotationPitch, double x, double y, double z) {
		this.rotationYaw = rotationYaw;
		this.rotationPitch = rotationPitch;
		this.motionX = x;
		this.motionY = y;
		this.motionZ = x;
	}

	public static void encode(PacketForceArrowRotations msg, PacketBuffer buf) {
		buf.writeInt(msg.entityID);
		buf.writeFloat(msg.rotationYaw);
		buf.writeFloat(msg.rotationPitch);
		buf.writeDouble(msg.motionX);
		buf.writeDouble(msg.motionY);
		buf.writeDouble(msg.motionZ);
	}

	public static PacketForceArrowRotations decode(PacketBuffer buf) {
		return new PacketForceArrowRotations(buf.readInt(), buf.readFloat(), buf.readFloat(), buf.readDouble(), buf.readDouble(), buf.readDouble());
	}

	public static void handle(PacketForceArrowRotations msg, Supplier<NetworkEvent.Context> ctx) {

		ctx.get().enqueueWork(() -> {
			ClientWorld theWorld = Minecraft.getInstance().world;
			Entity arrow = theWorld.getEntityByID(msg.entityID);

			if (arrow != null) {
				arrow.setMotion(msg.motionX, msg.motionY, msg.motionZ);
				arrow.rotationYaw = msg.rotationYaw;
				arrow.prevRotationYaw = msg.rotationYaw;
				arrow.rotationPitch = msg.rotationPitch;
				arrow.prevRotationPitch = msg.rotationPitch;
			}

		});
		ctx.get().setPacketHandled(true);
	}

}
