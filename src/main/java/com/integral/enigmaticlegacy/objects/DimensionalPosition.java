package com.integral.enigmaticlegacy.objects;

import net.minecraft.world.World;

public class DimensionalPosition {

	public double posX;
	public double posY;
	public double posZ;
	public World world;

	public DimensionalPosition(double x, double y, double z, World world) {
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.world = world;
	}

	public double getPosX() {
		return this.posX;
	}

	public double getPosY() {
		return this.posY;
	}

	public double getPosZ() {
		return this.posZ;
	}

	public World getWorld() {
		return this.world;
	}
}
