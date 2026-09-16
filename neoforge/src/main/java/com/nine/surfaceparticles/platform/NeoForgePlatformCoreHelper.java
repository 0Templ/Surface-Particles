package com.nine.surfaceparticles.platform;

import com.nine.surfaceparticles.SPCommon;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;

import java.nio.file.Path;

public class NeoForgePlatformCoreHelper implements IPlatformCoreHelper {

    @Override
    public Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public boolean inDevEnvironment() {
        return !FMLLoader.getCurrent().isProduction();
    }

    @Override
    public String modVersion() {
        return ModList.get()
                .getModContainerById(SPCommon.MODID)
                .map(container -> container.getModInfo().getVersion().toString())
                .orElse("0.0.0");
    }

    @Override
    public String currentLoader() {
        return "neoforge";
    }

    @Override
    public boolean isModLoaded(String id) {
        return ModList.get().isLoaded(id);
    }

    @Override
    public boolean handleDestroyEffects(BlockState state, ClientLevel level, BlockPos pos, ParticleEngine engine) {
        return IClientBlockExtensions.of(state).addDestroyEffects(state, level, pos, engine);
    }

}
