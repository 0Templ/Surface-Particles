package com.nine.surfaceparticles.core.emit;

import com.nine.surfaceparticles.config.SPConfig;
import com.nine.surfaceparticles.core.texture.SurfaceTexture;
import com.nine.surfaceparticles.core.texture.SurfaceTextures;
import com.nine.surfaceparticles.core.tuning.ParticleTuning;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;

public final class BreakParticleEmitter {

    private BreakParticleEmitter() {
    }

    public static boolean hitEnabled() {
        return SPConfig.General.ENABLE_HIT_PARTICLES.get();
    }

    public static boolean destroyEnabled() {
        return SPConfig.General.ENABLE_DESTROY_PARTICLES.get();
    }

    public static void destroyEffects(ClientLevel level, BlockPos pos, BlockState state) {
        if (state.isAir()) {
            return;
        }

        Map<Direction, SurfaceTextures.FaceTexture> faces = new EnumMap<>(Direction.class);
        SurfaceTexture inside = SurfaceTextures.particle(state, level, pos);
        ParticleEngine engine = Minecraft.getInstance().particleEngine;

        VoxelShape shape = state.getShape(level, pos);
        shape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
            double sizeX = Math.min(1.0D, x2 - x1);
            double sizeY = Math.min(1.0D, y2 - y1);
            double sizeZ = Math.min(1.0D, z2 - z1);
            int countX = Math.max(ParticleTuning.BREAK_MIN_CELLS, Mth.ceil(sizeX / ParticleTuning.BREAK_GRID_STEP));
            int countY = Math.max(ParticleTuning.BREAK_MIN_CELLS, Mth.ceil(sizeY / ParticleTuning.BREAK_GRID_STEP));
            int countZ = Math.max(ParticleTuning.BREAK_MIN_CELLS, Mth.ceil(sizeZ / ParticleTuning.BREAK_GRID_STEP));

            for (int xi = 0; xi < countX; xi++) {
                for (int yi = 0; yi < countY; yi++) {
                    for (int zi = 0; zi < countZ; zi++) {
                        double relX = (xi + 0.5D) / countX;
                        double relY = (yi + 0.5D) / countY;
                        double relZ = (zi + 0.5D) / countZ;
                        double x = relX * sizeX + x1;
                        double y = relY * sizeY + y1;
                        double z = relZ * sizeZ + z1;

                        Direction face = cellFace(xi, countX, yi, countY, zi, countZ);
                        SurfaceTexture texture;
                        if (face == null) {
                            texture = inside;
                        } else {
                            SurfaceTextures.FaceTexture info = faces.computeIfAbsent(
                                    face, f -> SurfaceTextures.faceInfo(state, level, pos, f));
                            texture = pickFaceTexture(info, face, yi == countY - 1, faces, state, level, pos, inside);
                        }

                        engine.add(new SurfaceParticle(
                                level,
                                pos.getX() + x, pos.getY() + y, pos.getZ() + z,
                                relX - 0.5D, relY - 0.5D, relZ - 0.5D,
                                state, pos, texture, face
                        ));
                    }
                }
            }
        });
    }

    private static SurfaceTexture pickFaceTexture(
            SurfaceTextures.FaceTexture info,
            Direction face,
            boolean topRow,
            Map<Direction, SurfaceTextures.FaceTexture> faces,
            BlockState state,
            ClientLevel level,
            BlockPos pos,
            SurfaceTexture inside
    ) {
        if (!info.tintedOverlay() || !face.getAxis().isHorizontal()) {
            return info.texture();
        }
        if (topRow) {
            return faces.computeIfAbsent(Direction.UP, f -> SurfaceTextures.faceInfo(state, level, pos, f)).texture();
        }
        return inside;
    }

    public static void crackEffect(ClientLevel level, BlockPos pos, Direction direction) {
        crackEffect(level, pos, direction, null);
    }

    public static void crackEffect(ClientLevel level, BlockPos pos, Direction direction, Vec3 hitLocation) {
        BlockState state = level.getBlockState(pos);
        if (state.getRenderShape() == RenderShape.INVISIBLE) {
            return;
        }
        VoxelShape shape = state.getShape(level, pos);
        if (shape.isEmpty()) {
            return;
        }

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        AABB bounds = shape.bounds();
        RandomSource random = level.getRandom();
        double inset = ParticleTuning.CRACK_RANDOM_INSET;
        double xp;
        double yp;
        double zp;
        if (hitLocation != null) {
            xp = Mth.clamp(hitLocation.x, x + bounds.minX, x + bounds.maxX);
            yp = Mth.clamp(hitLocation.y, y + bounds.minY, y + bounds.maxY);
            zp = Mth.clamp(hitLocation.z, z + bounds.minZ, z + bounds.maxZ);
        } else {
            xp = x + random.nextDouble() * (bounds.maxX - bounds.minX - inset * 2.0D) + inset + bounds.minX;
            yp = y + random.nextDouble() * (bounds.maxY - bounds.minY - inset * 2.0D) + inset + bounds.minY;
            zp = z + random.nextDouble() * (bounds.maxZ - bounds.minZ - inset * 2.0D) + inset + bounds.minZ;
        }
        double offset = ParticleTuning.CRACK_FACE_OFFSET;
        switch (direction) {
            case DOWN -> yp = y + bounds.minY - offset;
            case UP -> yp = y + bounds.maxY + offset;
            case NORTH -> zp = z + bounds.minZ - offset;
            case SOUTH -> zp = z + bounds.maxZ + offset;
            case WEST -> xp = x + bounds.minX - offset;
            case EAST -> xp = x + bounds.maxX + offset;
        }

        SurfaceTextures.FaceTexture info = SurfaceTextures.faceInfo(state, level, pos, direction);
        SurfaceTexture texture = info.texture();
        if (info.tintedOverlay() && direction.getAxis().isHorizontal()) {
            boolean rim = yp >= y + bounds.maxY - ParticleTuning.BREAK_GRID_STEP;
            texture = rim
                    ? SurfaceTextures.face(state, level, pos, Direction.UP)
                    : SurfaceTextures.particle(state, level, pos);
        }

        SurfaceParticle particle = new SurfaceParticle(
                level, xp, yp, zp, 0.0D, 0.0D, 0.0D,
                state, pos, texture, direction
        );
        particle.setPower(ParticleTuning.CRACK_POWER);
        particle.scale(ParticleTuning.CRACK_SCALE);
        Minecraft.getInstance().particleEngine.add(particle);
    }

    private static Direction cellFace(int xi, int countX, int yi, int countY, int zi, int countZ) {
        double vx = (xi + 0.5D) / countX - 0.5D;
        double vy = (yi + 0.5D) / countY - 0.5D;
        double vz = (zi + 0.5D) / countZ - 0.5D;
        double ax = Math.abs(vx);
        double ay = Math.abs(vy);
        double az = Math.abs(vz);

        if (ay >= ax && ay >= az) {
            boolean up = vy > 0.0D;
            boolean outer = up ? yi == countY - 1 : yi == 0;
            return outer ? (up ? Direction.UP : Direction.DOWN) : null;
        }
        if (ax >= az) {
            boolean east = vx > 0.0D;
            boolean outer = east ? xi == countX - 1 : xi == 0;
            return outer ? (east ? Direction.EAST : Direction.WEST) : null;
        }
        boolean south = vz > 0.0D;
        boolean outer = south ? zi == countZ - 1 : zi == 0;
        return outer ? (south ? Direction.SOUTH : Direction.NORTH) : null;
    }
}
