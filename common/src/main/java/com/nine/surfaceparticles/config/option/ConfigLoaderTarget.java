package com.nine.surfaceparticles.config.option;

import com.nine.surfaceparticles.config.ConfigSpec;
import com.nine.surfaceparticles.config.LoaderTarget;

public record ConfigLoaderTarget(LoaderTarget target) implements ConfigOption {

    public static ConfigLoaderTarget of(LoaderTarget target) {
        return new ConfigLoaderTarget(target);
    }

    @Override
    public void apply(ConfigSpec spec) {
        spec.target = this.target != null ? this.target : LoaderTarget.COMMON;
    }
}
