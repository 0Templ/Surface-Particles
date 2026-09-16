package com.nine.surfaceparticles.config.option;

import com.nine.surfaceparticles.config.ConfigSpec;

public enum ConfigFlag implements ConfigOption {

    HIDE_CONSTRAINTS {
        @Override
        public void apply(ConfigSpec spec) {
            spec.hideConstraints = true;
        }
    }
}
