package com.nine.surfaceparticles.core.tuning;

import net.minecraft.core.Direction;

public final class ParticleTuning {

    private ParticleTuning() {
    }

    public static final double SPRINT_BACK_VELOCITY = 4.0D;

    public static final double SPRINT_UP_VELOCITY = 1.5D;

    public static final double SPRINT_Y_OFFSET = 0.1D;

    public static final double FEET_SNAP_BELOW = 0.12D;

    public static final double FEET_SNAP_ABOVE = 0.35D;

    public static final double COVER_FOOTPRINT_INSET = 0.01D;

    public static final double COVER_STAND_THROUGH_SLOP = 0.05D;

    public static final double BREAK_GRID_STEP = 0.25D;

    public static final int BREAK_MIN_CELLS = 2;

    public static final float CRACK_POWER = 0.2F;

    public static final float CRACK_SCALE = 0.6F;

    public static final double CRACK_FACE_OFFSET = 0.1D;

    public static final double CRACK_RANDOM_INSET = 0.1D;

    public static final float SHADE_UP = 1.0F;
    public static final float SHADE_HORIZONTAL_NS = 0.8F;
    public static final float SHADE_HORIZONTAL_EW = 0.6F;
    public static final float SHADE_DOWN = 0.5F;

    public static final float DUST_DIM = 0.6F;

    public static final float SHADE_CORE = 1.0F;

    public static float shadeFor(Direction direction) {
        if (direction == null) {
            return SHADE_CORE;
        }
        return switch (direction) {
            case UP -> SHADE_UP;
            case DOWN -> SHADE_DOWN;
            case NORTH, SOUTH -> SHADE_HORIZONTAL_NS;
            case EAST, WEST -> SHADE_HORIZONTAL_EW;
        };
    }
}
