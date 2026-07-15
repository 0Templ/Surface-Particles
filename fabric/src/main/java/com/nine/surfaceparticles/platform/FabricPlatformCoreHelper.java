package com.nine.surfaceparticles.platform;

import com.nine.surfaceparticles.SPCommon;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class FabricPlatformCoreHelper implements IPlatformCoreHelper {

    @Override
    public Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public boolean inDevEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public String modVersion() {
        return FabricLoader.getInstance()
                .getModContainer(SPCommon.MODID)
                .map(mod -> mod.getMetadata().getVersion().getFriendlyString())
                .orElse("0.0.0");
    }

    @Override
    public String currentLoader() {
        return "fabric";
    }

    @Override
    public boolean isModLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }

}
