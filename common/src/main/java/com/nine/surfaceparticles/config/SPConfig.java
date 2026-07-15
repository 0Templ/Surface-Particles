package com.nine.surfaceparticles.config;

import com.nine.surfaceparticles.config.option.ConfigComment;
import com.nine.surfaceparticles.config.option.ConfigSection;
import com.nine.surfaceparticles.config.option.ConfigSide;

public class SPConfig {

    public static void init() {
    }

    public static final class General {

        public static final ConfigValue<Boolean> ENABLE_SPRINT_PARTICLES =
                ConfigImpl.register(
                        "enable_sprint_particles",
                        true,
                        ConfigSection.GENERAL,
                        ConfigSide.CLIENT,
                        ConfigComment.of("Master switch for the sprint particle fix.")
                                .line("When disabled, vanilla sprint particles are left untouched.")
                );

        public static final ConfigValue<Boolean> ENABLE_HIT_PARTICLES =
                ConfigImpl.register(
                        "enable_hit_particles",
                        true,
                        ConfigSection.GENERAL,
                        ConfigSide.CLIENT,
                        ConfigComment.of("Fix for the crack particles spawned while a block is being mined.")
                                .line("The particle takes the texture of the face being hit instead of the")
                                .line("model's single particle texture.")
                                .line("When disabled, vanilla hit particles are left untouched.")
                );

        public static final ConfigValue<Boolean> ENABLE_DESTROY_PARTICLES =
                ConfigImpl.register(
                        "enable_destroy_particles",
                        true,
                        ConfigSection.GENERAL,
                        ConfigSide.CLIENT,
                        ConfigComment.of("Fix for the particle cloud spawned when a block breaks.")
                                .line("Outer particles take the texture of the block face they sit on;")
                                .line("inner ones keep the vanilla particle texture.")
                                .line("When disabled, vanilla destroy particles are left untouched.")
                );

    }
}
