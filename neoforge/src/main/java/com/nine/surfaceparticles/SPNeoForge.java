package com.nine.surfaceparticles;

import com.nine.surfaceparticles.ui.screen.SPConfigScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = SPCommon.MODID, dist = Dist.CLIENT)
public class SPNeoForge {

    public SPNeoForge(
            IEventBus modEventBus,
            ModContainer container
    ) {
        SPCommon.init();

        bindConfigScreen(container);
    }

    private void bindConfigScreen(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class,
                (modContainer, parent) -> new SPConfigScreen(parent));
    }

}
