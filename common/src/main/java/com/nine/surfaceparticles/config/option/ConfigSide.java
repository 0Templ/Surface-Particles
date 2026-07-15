package com.nine.surfaceparticles.config.option;

import com.nine.surfaceparticles.config.ConfigSpec;

public enum ConfigSide implements ConfigOption {

    CLIENT,

    COMMON;

    @Override
    public void apply(ConfigSpec spec) {
        spec.side = this;
    }
}
