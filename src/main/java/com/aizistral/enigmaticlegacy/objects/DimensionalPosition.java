package com.aizistral.enigmaticlegacy.objects;

import net.minecraft.world.level.Level;

public class DimensionalPosition {

	public double posX;
	public double posY;
	public double posZ;
	public Level world;

	public DimensionalPosition(double x, double y, double z, Level world) {
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

	public Level getWorld() {
		return this.world;
	}
}
