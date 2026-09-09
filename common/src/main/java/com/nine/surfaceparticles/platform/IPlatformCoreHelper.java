package com.nine.surfaceparticles.platform;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

import java.nio.file.Path;

public interface IPlatformCoreHelper {

    Path getConfigPath();

    boolean inDevEnvironment();

    String modVersion();

    String currentLoader();

    boolean isModLoaded(String id);

    default boolean handleDestroyEffects(BlockState state, ClientLevel level, BlockPos pos, ParticleEngine engine) {
        return false;
    }

    default boolean handleHitEffects(BlockState state, ClientLevel level, BlockPos pos, Direction direction, HitResult hit, ParticleEngine engine) {
        return false;
    }

}
