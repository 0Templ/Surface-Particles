package com.nine.surfaceparticles.config;

import com.nine.surfaceparticles.config.option.ConfigComment;
import com.nine.surfaceparticles.config.option.ConfigRange;
import com.nine.surfaceparticles.config.option.ConfigSection;
import com.nine.surfaceparticles.config.option.ConfigSide;

public class ConfigSpec {

    public ConfigSide side = ConfigSide.COMMON;

    public ConfigComment comment = ConfigComment.EMPTY;
    public ConfigSection section = ConfigSection.NONE;
    public boolean hideConstraints = false;

    public ConfigRange<? extends Number> range = null;
}
