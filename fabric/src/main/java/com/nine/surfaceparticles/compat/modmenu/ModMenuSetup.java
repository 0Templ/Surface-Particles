package com.nine.surfaceparticles.compat.modmenu;

import com.nine.surfaceparticles.ui.screen.SPConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuSetup implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return SPConfigScreen::new;
    }

}
