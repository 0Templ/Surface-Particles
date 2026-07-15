package com.nine.surfaceparticles.core.surface;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;


public record Surface(BlockPos pos, BlockState state, double topY) {
}
