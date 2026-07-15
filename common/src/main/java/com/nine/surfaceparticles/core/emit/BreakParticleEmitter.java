package com.nine.surfaceparticles.core.emit;

import com.nine.surfaceparticles.config.SPConfig;
import com.nine.surfaceparticles.core.texture.SurfaceTexture;
import com.nine.surfaceparticles.core.texture.SurfaceTextures;
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
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;


public final class BreakParticleEmitter {

    private static final double RIM_HEIGHT = 0.25D;

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
            int countX = Math.max(2, Mth.ceil(sizeX / 0.25D));
            int countY = Math.max(2, Mth.ceil(sizeY / 0.25D));
            int countZ = Math.max(2, Mth.ceil(sizeZ / 0.25D));

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
                                state, pos, texture
                        ));
                    }
                }
            }
        });
    }

    /**
     * Sides with a tinted overlay (grass block pattern) split in two: the top
     * row of the face is the overlay rim and shows the biome-tinted top
     * texture, the rest shows the plain particle texture — the untinted base
     * sprite has a default-colored rim baked in that would clash with the
     * biome tint.
     */
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
        double xp = x + random.nextDouble() * (bounds.maxX - bounds.minX - 0.2D) + 0.1D + bounds.minX;
        double yp = y + random.nextDouble() * (bounds.maxY - bounds.minY - 0.2D) + 0.1D + bounds.minY;
        double zp = z + random.nextDouble() * (bounds.maxZ - bounds.minZ - 0.2D) + 0.1D + bounds.minZ;
        switch (direction) {
            case DOWN -> yp = y + bounds.minY - 0.1D;
            case UP -> yp = y + bounds.maxY + 0.1D;
            case NORTH -> zp = z + bounds.minZ - 0.1D;
            case SOUTH -> zp = z + bounds.maxZ + 0.1D;
            case WEST -> xp = x + bounds.minX - 0.1D;
            case EAST -> xp = x + bounds.maxX + 0.1D;
        }

        SurfaceTextures.FaceTexture info = SurfaceTextures.faceInfo(state, level, pos, direction);
        SurfaceTexture texture = info.texture();
        if (info.tintedOverlay() && direction.getAxis().isHorizontal()) {
            // Same rim split as the destroy cloud: overlay rim occupies
            // roughly the top quarter of the face.
            boolean rim = yp >= y + bounds.maxY - RIM_HEIGHT;
            texture = rim
                    ? SurfaceTextures.face(state, level, pos, Direction.UP)
                    : SurfaceTextures.particle(state, level, pos);
        }

        SurfaceParticle particle = new SurfaceParticle(
                level, xp, yp, zp, 0.0D, 0.0D, 0.0D,
                state, pos, texture
        );
        particle.setPower(0.2F);
        particle.scale(0.6F);
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
