package com.nine.surfaceparticles.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.nine.surfaceparticles.SPCommon;
import com.nine.surfaceparticles.config.option.ConfigComment;
import com.nine.surfaceparticles.config.option.ConfigOption;
import com.nine.surfaceparticles.config.option.ConfigRange;
import com.nine.surfaceparticles.config.option.ConfigSide;
import com.nine.surfaceparticles.platform.Platform;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ConfigImpl {

    private static final File CLIENT_FILE = getConfigPath("surfaceparticles-client");
    public static final CommentedFileConfig CLIENT = CommentedFileConfig.builder(CLIENT_FILE)
            .autosave()
            .sync()
            .preserveInsertionOrder()
            .build();
    private static final List<ConfigValue<?>> REGISTERED_VALUES = new ArrayList<>();

    static {
        load();
    }

    private static void load() {
        CLIENT.load();
    }

    public static <T> ConfigValue<T> register(
            String key,
            T defaultValue,
            ConfigOption... options
    ) {
        return register(key, defaultValue, tryGetClass(defaultValue), tryGetListElementClass(defaultValue), options);
    }

    private static <T> ConfigValue<T> register(
            String key,
            T defaultValue,
            Class<T> clazz,
            Class<?> elementClass,
            ConfigOption... options
    ) {
        ConfigSpec spec = new ConfigSpec();
        for (var option : options) {
            option.apply(spec);
        }

        ConfigValue<T> value = new ConfigValue<>(
                CLIENT,
                key,
                defaultValue,
                spec.range,
                spec.section,
                spec.side,
                clazz,
                elementClass
        );

        if (CLIENT.get(value.path()) == null) {
            CLIENT.set(value.path(), value.serializeUnchecked(value.defaultValue()));
        }

        clampStored(value);

        String comment = buildComment(spec.comment, spec.range, spec.hideConstraints, clazz);
        if (!comment.isBlank()) {
            CLIENT.setComment(value.path(), comment);
        }
        normalizeTomlSpacing(spec.side);
        REGISTERED_VALUES.add(value);

        return value;
    }

    public static List<ConfigValue<?>> registeredValues() {
        return List.copyOf(REGISTERED_VALUES);
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> tryGetClass(T defaultValue) {
        if (defaultValue == null) {
            return (Class<T>) Object.class;
        }
        return (Class<T>) defaultValue.getClass();
    }

    private static Class<?> tryGetListElementClass(Object defaultValue) {
        if (defaultValue instanceof List<?> list && !list.isEmpty()) {
            return list.get(0).getClass();
        }
        return null;
    }

    private static String buildComment(
            ConfigComment comment,
            ConfigRange<?> range,
            boolean hideConstraints,
            Class<?> clazz
    ) {
        List<String> lines = new ArrayList<>();
        if (comment != null && comment.value() != null && !comment.value().isBlank()) {
            lines.add(comment.value());
        }

        if (range != null && !hideConstraints) {
            lines.add("Range: " + range.min() + " .. " + range.max());
        }

        if (clazz != null && clazz.isEnum()) {
            Object[] values = clazz.getEnumConstants();
            String[] names = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                names[i] = ((Enum<?>) values[i]).name();
            }
            lines.add("Available values: " + String.join(", ", names));
        }

        return String.join("\n", lines);
    }

    private static void clampStored(ConfigValue<?> value) {
        Object current = CLIENT.get(value.path());
        Object fixed = value.normalizeUnchecked(current);
        if (fixed != null && !fixed.equals(current)) {
            CLIENT.set(value.path(), value.serializeUnchecked(fixed));
        }
    }

    private static File getConfigPath(String id) {
        var dir = Platform.CORE.getConfigPath().resolve("surfaceparticles");
        var df = dir.toFile();
        if (!df.exists() && !df.mkdirs()) {
            SPCommon.LOGGER.warn("Could not create config directory at {}", df.getAbsolutePath());
        }
        return new File(df, id + ".toml");
    }

    private static void normalizeTomlSpacing(ConfigSide side) {
        File file = CLIENT_FILE;

        if (!file.exists()) {
            return;
        }

        try {
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            String lineSeparator = content.contains("\r\n") ? "\r\n" : "\n";
            String[] lines = content.split("\\R", -1);
            boolean changed = false;

            for (int i = 0; i < lines.length; i++) {
                if (lines[i].trim().equals("#")) {
                    lines[i] = "";
                    changed = true;
                }
            }

            if (changed) {
                Files.writeString(file.toPath(), String.join(lineSeparator, lines), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            SPCommon.LOGGER.warn("Failed to normalize: {}", file.getName(), e);
        }
    }
}
