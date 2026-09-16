package com.nine.surfaceparticles.core.emit;

import com.nine.surfaceparticles.config.SPConfig;
import com.nine.surfaceparticles.core.surface.Surface;
import com.nine.surfaceparticles.core.surface.SurfaceResolver;
import com.nine.surfaceparticles.core.tuning.ParticleTuning;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public final class FootParticleEmitter {

    private FootParticleEmitter() {
    }

    public static boolean emitSprintParticle(Entity entity) {
        if (!(entity.level() instanceof ClientLevel level)) {
            return false;
        }
        if (!SPConfig.General.ENABLE_SPRINT_PARTICLES.get()) {
            return false;
        }

        Surface surface = SurfaceResolver.resolve(entity);
        if (surface == null || !canSpawnFrom(surface.state())) {
            return true;
        }

        BlockPos entityPos = entity.blockPosition();
        BlockPos surfacePos = surface.pos();
        double width = entity.getBbWidth();
        double x = entity.getX() + (entity.getRandom().nextDouble() - 0.5D) * width;
        double z = entity.getZ() + (entity.getRandom().nextDouble() - 0.5D) * width;
        if (entityPos.getX() != surfacePos.getX()) {
            x = Mth.clamp(x, surfacePos.getX(), surfacePos.getX() + 1.0D);
        }
        if (entityPos.getZ() != surfacePos.getZ()) {
            z = Mth.clamp(z, surfacePos.getZ(), surfacePos.getZ() + 1.0D);
        }
        double y = Math.max(entity.getY(), surface.topY()) + ParticleTuning.SPRINT_Y_OFFSET;

        Vec3 movement = entity.getDeltaMovement();
        Minecraft.getInstance().particleEngine.add(new SurfaceParticle(
                level, x, y, z,
                movement.x * -ParticleTuning.SPRINT_BACK_VELOCITY,
                ParticleTuning.SPRINT_UP_VELOCITY,
                movement.z * -ParticleTuning.SPRINT_BACK_VELOCITY,
                surface
        ));
        return true;
    }

    private static boolean canSpawnFrom(BlockState state) {
        return !state.isAir() && !state.is(Blocks.MOVING_PISTON) && state.shouldSpawnTerrainParticles();
    }
}
