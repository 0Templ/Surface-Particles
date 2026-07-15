package com.nine.surfaceparticles;

import com.nine.surfaceparticles.api.meta.update.SPUpdateService;
import com.nine.surfaceparticles.config.SPConfig;
import com.nine.surfaceparticles.platform.Platform;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SPCommon {

    public static final String MODID = "surfaceparticles";

    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static void init() {
        Platform.init();
        SPConfig.init();
        SPUpdateService.startAsync();
    }

}
