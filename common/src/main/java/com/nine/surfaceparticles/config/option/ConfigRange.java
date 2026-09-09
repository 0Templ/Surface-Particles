package com.nine.surfaceparticles.config.option;

import com.nine.surfaceparticles.config.ConfigSpec;
import net.minecraft.util.Mth;

public record ConfigRange<T extends Number & Comparable<T>>(T min, T max) implements ConfigOption {

    public T clamp(T value) {
        if (value == null) {
            return null;
        }
        if (min != null && value.compareTo(min) < 0) return min;
        if (max != null && value.compareTo(max) > 0) return max;
        return value;
    }

    public Object clampValue(Object input) {
        if (input == null || (min == null && max == null)) {
            return input;
        }
        if (input instanceof Integer intValue) {
            return Mth.clamp(
                    intValue,
                    min != null ? min.intValue() : Integer.MIN_VALUE,
                    max != null ? max.intValue() : Integer.MAX_VALUE
            );
        }
        if (input instanceof Long longValue) {
            return Mth.clamp(
                    longValue,
                    min != null ? min.longValue() : Long.MIN_VALUE,
                    max != null ? max.longValue() : Long.MAX_VALUE
            );
        }
        if (input instanceof Double doubleValue) {
            return Mth.clamp(
                    doubleValue,
                    min != null ? min.doubleValue() : -Double.MAX_VALUE,
                    max != null ? max.doubleValue() : Double.MAX_VALUE
            );
        }
        if (input instanceof Float floatValue) {
            return Mth.clamp(
                    floatValue,
                    min != null ? min.floatValue() : -Float.MAX_VALUE,
                    max != null ? max.floatValue() : Float.MAX_VALUE
            );
        }
        if (input instanceof Short shortValue) {
            return (short) Mth.clamp(
                    shortValue,
                    min != null ? min.shortValue() : Short.MIN_VALUE,
                    max != null ? max.shortValue() : Short.MAX_VALUE
            );
        }
        if (input instanceof Byte byteValue) {
            return (byte) Mth.clamp(
                    byteValue,
                    min != null ? min.byteValue() : Byte.MIN_VALUE,
                    max != null ? max.byteValue() : Byte.MAX_VALUE
            );
        }
        return input;
    }

    @Override
    public void apply(ConfigSpec spec) {
        spec.range = this;
    }
}
