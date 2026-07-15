package com.nine.surfaceparticles;

import com.nine.surfaceparticles.ui.screen.SPConfigScreen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(SPCommon.MODID)
public class SPForge {

    public SPForge() {
        SPCommon.init();

        if (FMLEnvironment.dist.isClient()) {
            bindConfigScreen();
        }
    }

    private void bindConfigScreen() {
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (minecraft, parent) -> new SPConfigScreen(parent)
                )
        );
    }

}
