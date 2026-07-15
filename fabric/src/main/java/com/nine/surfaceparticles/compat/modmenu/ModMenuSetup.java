package com.nine.surfaceparticles.compat.modmenu;

import com.nine.surfaceparticles.SPCommon;
import com.nine.surfaceparticles.ui.screen.SPConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.api.UpdateChecker;

import java.util.Map;

public class ModMenuSetup implements ModMenuApi {

    @Override
    public Map<String, UpdateChecker> getProvidedUpdateCheckers() {
        return Map.of(
                SPCommon.MODID, new SPUpdateChecker()
        );
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return SPConfigScreen::new;
    }

}
