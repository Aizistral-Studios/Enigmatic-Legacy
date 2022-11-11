package com.aizistral.enigmaticlegacy.objects;

import java.util.Optional;

import net.minecraft.world.phys.Vec3;

public record AnchorSearchResult(Optional<Vec3> location, boolean found, boolean spentCharge) {
	// NO-OP
}
