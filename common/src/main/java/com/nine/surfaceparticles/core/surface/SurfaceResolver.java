package com.nine.surfaceparticles.core.surface;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class SurfaceResolver {

    private SurfaceResolver() {
    }

    public static Surface resolve(Entity entity) {
        Level level = entity.level();

        BlockPos feetPos = entity.blockPosition();
        BlockState feetState = level.getBlockState(feetPos);
        if (isGroundCover(feetState, level, feetPos)) {
            return new Surface(feetPos, feetState, feetPos.getY() + shapeTop(feetState, level, feetPos));
        }

        BlockPos onPos = entity.getOnPosLegacy();
        BlockState onState = level.getBlockState(onPos);
        if (onState.getRenderShape() == RenderShape.INVISIBLE) {
            return null;
        }
        return new Surface(onPos, onState, onPos.getY() + shapeTop(onState, level, onPos));
    }

    private static boolean isGroundCover(BlockState state, Level level, BlockPos pos) {
        if (state.isAir() || state.getRenderShape() == RenderShape.INVISIBLE) {
            return false;
        }
        if (state.is(BlockTags.SNOW)) {
            return true;
        }
        return !state.getCollisionShape(level, pos).isEmpty();
    }

    private static double shapeTop(BlockState state, Level level, BlockPos pos) {
        VoxelShape shape = state.getShape(level, pos);
        return shape.isEmpty() ? 1.0D : shape.max(Direction.Axis.Y);
    }
}
