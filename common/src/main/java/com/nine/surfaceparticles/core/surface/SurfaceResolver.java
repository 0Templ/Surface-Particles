package com.nine.surfaceparticles.core.surface;

import com.nine.surfaceparticles.core.tuning.ParticleTuning;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class SurfaceResolver {

    private SurfaceResolver() {
    }

    public static Surface resolve(Entity entity) {
        Level level = entity.level();

        BlockPos feetPos = entity.blockPosition();
        BlockState feetState = level.getBlockState(feetPos);
        if (isSurfaceAtFeet(feetState, level, feetPos, entity.getY())) {
            return new Surface(feetPos, feetState, feetPos.getY() + shapeTop(feetState, level, feetPos));
        }

        BlockPos onPos = entity.getOnPosLegacy();
        BlockState onState = level.getBlockState(onPos);
        if (onState.getRenderShape() == RenderShape.INVISIBLE) {
            return null;
        }
        return new Surface(onPos, onState, onPos.getY() + shapeTop(onState, level, onPos));
    }

    private static boolean isSurfaceAtFeet(BlockState state, Level level, BlockPos pos, double feetY) {
        if (state.isAir() || state.getRenderShape() == RenderShape.INVISIBLE) {
            return false;
        }
        if (!state.getFluidState().isEmpty()) {
            return false;
        }
        VoxelShape shape = state.getShape(level, pos);
        if (shape.isEmpty()) {
            return false;
        }
        double top = pos.getY() + shape.max(Direction.Axis.Y);
        if (feetY >= top - ParticleTuning.FEET_SNAP_BELOW
                && feetY <= top + ParticleTuning.FEET_SNAP_ABOVE) {
            return true;
        }
        return isFlatCover(shape)
                && feetY >= pos.getY() - ParticleTuning.COVER_STAND_THROUGH_SLOP
                && feetY <= top + ParticleTuning.FEET_SNAP_ABOVE;
    }

    private static boolean isFlatCover(VoxelShape shape) {
        AABB bounds = shape.bounds();
        double inset = ParticleTuning.COVER_FOOTPRINT_INSET;
        return bounds.minX <= inset
                && bounds.minZ <= inset
                && bounds.maxX >= 1.0D - inset
                && bounds.maxZ >= 1.0D - inset
                && bounds.minY <= inset;
    }

    private static double shapeTop(BlockState state, Level level, BlockPos pos) {
        VoxelShape shape = state.getShape(level, pos);
        return shape.isEmpty() ? 1.0D : shape.max(Direction.Axis.Y);
    }
}
