package com.nine.surfaceparticles.core.emit;

import com.nine.surfaceparticles.platform.Platform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public final class BreakParticleHandler {

    private BreakParticleHandler() {
    }

    public static boolean handleDestroy(ClientLevel level, BlockPos pos, BlockState state) {
        if (!BreakParticleEmitter.destroyEnabled()) {
            return false;
        }
        if (state.isAir() || !state.shouldSpawnTerrainParticles()) {
            return false;
        }
        if (Platform.CORE.handleDestroyEffects(state, level, pos, Minecraft.getInstance().particleEngine)) {
            return true;
        }
        BreakParticleEmitter.destroyEffects(level, pos, state);
        return true;
    }

    public static boolean handleHit(ClientLevel level, BlockPos pos, Direction direction, BlockState state) {
        if (!BreakParticleEmitter.hitEnabled()) {
            return false;
        }
        if (state.getRenderShape() == RenderShape.INVISIBLE || !state.shouldSpawnTerrainParticles()) {
            return false;
        }
        BreakParticleEmitter.crackEffect(level, pos, direction);
        return true;
    }
}
