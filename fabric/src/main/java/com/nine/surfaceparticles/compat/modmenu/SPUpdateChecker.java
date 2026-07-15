package com.nine.surfaceparticles.compat.modmenu;

import com.nine.surfaceparticles.api.meta.update.SPUpdateResult;
import com.nine.surfaceparticles.api.meta.update.SPUpdateService;
import com.terraformersmc.modmenu.api.UpdateChecker;

public class SPUpdateChecker implements UpdateChecker {

    @Override
    public SPUpdateInfo checkForUpdates() {
        SPUpdateResult result = SPUpdateService.await();
        return SPUpdateInfo.from(result);
    }
}
