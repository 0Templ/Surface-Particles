package com.nine.surfaceparticles.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.nine.surfaceparticles.SPCommon;
import com.nine.surfaceparticles.config.option.ConfigRange;
import com.nine.surfaceparticles.config.option.ConfigSection;
import com.nine.surfaceparticles.config.option.ConfigSide;

public class ConfigValue<T> {

    public final String key;
    public final Class<T> clazz;
    public final ConfigSide side;
    private final CommentedFileConfig config;
    private final T defaultValue;
    private final ConfigSection section;
    private final Class<?> elementClass;
    private final ConfigRange<?> range;
    private volatile boolean mismatchWarned;

    public ConfigValue(
            CommentedFileConfig config,
            String key,
            T defaultValue,
            ConfigRange<?> range,
            ConfigSection section,
            ConfigSide side,
            Class<T> clazz,
            Class<?> elementClass
    ) {
        this.config = config;
        this.key = key;
        this.defaultValue = defaultValue;
        this.range = range;
        this.section = section;
        this.side = side;
        this.clazz = clazz;
        this.elementClass = elementClass;
    }

    public ConfigValue(
            String key,
            T defaultValue,
            ConfigRange<?> range,
            ConfigSection section,
            ConfigSide side,
            Class<T> clazz,
            Class<?> elementClass
    ) {
        this(null, key, defaultValue, range, section, side, clazz, elementClass);
    }

    public Class<T> clazz() {
        return clazz;
    }

    public String path() {
        String sectionKey = section.getFullKey();
        return sectionKey.isBlank() ? key : sectionKey + "." + key;
    }

    public T defaultValue() {
        return defaultValue;
    }

    public T get() {
        if (config == null) {
            return defaultValue;
        }

        Object value = config.get(path());
        if (value == null) {
            return defaultValue;
        }

        return this.cast(value);
    }

    @SuppressWarnings("unchecked")
    public T cast(Object value) {
        if (value == null) {
            return null;
        }

        try {
            if (value instanceof Number number) {
                if (clazz == Double.class) {
                    return (T) Double.valueOf(number.doubleValue());
                }
                if (clazz == Float.class) {
                    return (T) Float.valueOf(number.floatValue());
                }
                if (clazz == Integer.class) {
                    return (T) Integer.valueOf(number.intValue());
                }
                if (clazz == Long.class) {
                    return (T) Long.valueOf(number.longValue());
                }
            }

            if (clazz != null && clazz.isEnum() && value instanceof String str) {
                try {
                    return (T) clazz.getMethod("fromId", String.class).invoke(null, str);
                } catch (ReflectiveOperationException | ClassCastException ignored) {
                }
                return (T) Enum.valueOf((Class<Enum>) clazz, str);
            }

            if (clazz == Boolean.class && value instanceof Boolean boolValue) {
                return (T) boolValue;
            }

            return (T) value;
        } catch (RuntimeException e) {
            if (!mismatchWarned) {
                mismatchWarned = true;
                SPCommon.LOGGER.warn("Invalid value for config '{}', using default '{}': {}",
                        path(), defaultValue, value);
            } else {
                SPCommon.LOGGER.debug("Invalid value for config '{}', using default: {}", path(), value);
            }
            return defaultValue;
        }
    }

    public void set(T value) {
        if (config == null) {
            return;
        }

        Object toSet = this.normalize(value);
        config.set(path(), serialize(toSet));
    }

    public T normalize(T value) {
        return this.cast(tryClamp(value));
    }

    public boolean isAvailable() {
        return config != null;
    }

    public Number min() {
        return range != null ? range.min() : null;
    }

    public Number max() {
        return range != null ? range.max() : null;
    }

    public Class<?> elementClass() {
        return elementClass;
    }

    CommentedFileConfig config() {
        return this.config;
    }

    Object serializeNormalized(T value) {
        return serialize(this.normalize(value));
    }

    Object serializeUnchecked(Object value) {
        return serialize(value);
    }

    @SuppressWarnings("unchecked")
    Object normalizeUnchecked(Object value) {
        return normalize(cast(value));
    }

    private Object serialize(Object value) {
        if (value == null) {
            return null;
        }
        if (clazz != null && clazz.isEnum()) {
            return ((Enum<?>) value).name();
        }
        return value;
    }

    private Object tryClamp(Object input) {
        if (input == null || range == null) {
            return input;
        }
        return range.clampValue(input);
    }
}
