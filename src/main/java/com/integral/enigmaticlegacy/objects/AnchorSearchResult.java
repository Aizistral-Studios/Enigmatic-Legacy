package com.integral.enigmaticlegacy.objects;

import java.util.Optional;

import com.integral.enigmaticlegacy.blocks.BlockEndAnchor;
import com.integral.enigmaticlegacy.items.EndAnchor;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public record AnchorSearchResult(Optional<Vec3> location, boolean found, boolean spentCharge) {
	// NO-OP
}
