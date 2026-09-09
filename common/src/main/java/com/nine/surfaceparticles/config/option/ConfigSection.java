package com.nine.surfaceparticles.config.option;

import com.nine.surfaceparticles.config.ConfigSpec;

public enum ConfigSection implements ConfigOption {

    NONE(""),
    GENERAL("general"),

    ;

    public final String key;
    public final ConfigSection parent;
    ConfigSection(String key) {
        this(null, key);
    }

    ConfigSection(
            ConfigSection parent,
            String key
    ) {
        this.parent = parent;
        this.key = key;
    }

    @Override
    public void apply(ConfigSpec spec) {
        spec.section = this;
    }

    public String getFullKey() {
        if (parent == null) {
            return key;
        }
        String parentKey = parent.getFullKey();
        if (parentKey.isBlank()) {
            return key;
        }
        return key.isBlank() ? parentKey : parentKey + "." + key;
    }

    public String getKey() {
        return key;
    }

    public String getDisplayName() {
        return key;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public ConfigSection getParent() {
        return parent;
    }
}
