package com.integral.enigmaticlegacy.packets.clients;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
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
	private double motionX, motionY, motionZ, posX, posY, posZ;
	private float rotationYaw;
	private float rotationPitch;

	public PacketForceArrowRotations(int entityID, float rotationYaw, float rotationPitch, double vecX, double vecY, double vecZ, double posX, double posY, double posZ) {
		this.entityID = entityID;
		this.rotationYaw = rotationYaw;
		this.rotationPitch = rotationPitch;
		this.motionX = vecX;
		this.motionY = vecY;
		this.motionZ = vecZ;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}

	public static void encode(PacketForceArrowRotations msg, PacketBuffer buf) {
		buf.writeInt(msg.entityID);
		buf.writeFloat(msg.rotationYaw);
		buf.writeFloat(msg.rotationPitch);
		buf.writeDouble(msg.motionX);
		buf.writeDouble(msg.motionY);
		buf.writeDouble(msg.motionZ);
		buf.writeDouble(msg.posX);
		buf.writeDouble(msg.posY);
		buf.writeDouble(msg.posZ);
	}

	public static PacketForceArrowRotations decode(PacketBuffer buf) {
		return new PacketForceArrowRotations(buf.readInt(), buf.readFloat(), buf.readFloat(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble());
	}

	public static void handle(PacketForceArrowRotations msg, Supplier<NetworkEvent.Context> ctx) {

		ctx.get().enqueueWork(() -> {
			ClientWorld theWorld = Minecraft.getInstance().level;
			Entity arrow = theWorld.getEntity(msg.entityID);

			if (arrow != null) {
				arrow.addTag("enigmaticlegacy.redirected");

				arrow.teleportTo(msg.posX, msg.posY, msg.posZ);
				arrow.setDeltaMovement(msg.motionX, msg.motionY, msg.motionZ);
				arrow.yRot = msg.rotationYaw;
				arrow.yRotO = msg.rotationYaw;
				arrow.xRot = msg.rotationPitch;
				arrow.xRotO = msg.rotationPitch;
			}

		});
		ctx.get().setPacketHandled(true);
	}

}
