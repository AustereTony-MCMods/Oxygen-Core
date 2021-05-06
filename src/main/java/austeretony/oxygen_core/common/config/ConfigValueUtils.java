package austeretony.oxygen_core.common.config;

import austeretony.oxygen_core.common.util.value.*;

public class ConfigValueUtils {

    public static ConfigValue getBoolean(String category, String key, boolean baseValue, boolean needSync) {
        return new ConfigValueImpl(ValueType.BOOLEAN, baseValue, category, key, needSync);
    }

    public static ConfigValue getBoolean(String category, String key, boolean baseValue) {
        return new ConfigValueImpl(ValueType.BOOLEAN, baseValue, category, key, false);
    }

    public static ConfigValue getByte(String category, String key, int baseValue, boolean needSync) {
        return new ConfigValueImpl(ValueType.BYTE, baseValue, category, key, needSync);
    }

    public static ConfigValue getByte(String category, String key, int baseValue) {
        return new ConfigValueImpl(ValueType.BYTE, baseValue, category, key, false);
    }

    public static ConfigValue getShort(String category, String key, int baseValue, boolean needSync) {
        return new ConfigValueImpl(ValueType.SHORT, baseValue, category, key, needSync);
    }

    public static ConfigValue getShort(String category, String key, int baseValue) {
        return new ConfigValueImpl(ValueType.SHORT, baseValue, category, key, false);
    }

    public static ConfigValue getInt(String category, String key, int baseValue, boolean needSync) {
        return new ConfigValueImpl(ValueType.INTEGER, baseValue, category, key, needSync);
    }

    public static ConfigValue getInt(String category, String key, int baseValue) {
        return new ConfigValueImpl(ValueType.INTEGER, baseValue, category, key, false);
    }

    public static ConfigValue getLong(String category, String key, long baseValue, boolean needSync) {
        return new ConfigValueImpl(ValueType.LONG, baseValue, category, key, needSync);
    }

    public static ConfigValue getLong(String category, String key, long baseValue) {
        return new ConfigValueImpl(ValueType.LONG, baseValue, category, key, false);
    }

    public static ConfigValue getFloat(String category, String key, float baseValue, boolean needSync) {
        return new ConfigValueImpl(ValueType.FLOAT, baseValue, category, key, needSync);
    }

    public static ConfigValue getFloat(String category, String key, float baseValue) {
        return new ConfigValueImpl(ValueType.FLOAT, baseValue, category, key, false);
    }

    public static ConfigValue getDouble(String category, String key, double baseValue, boolean needSync) {
        return new ConfigValueImpl(ValueType.DOUBLE, baseValue, category, key, needSync);
    }

    public static ConfigValue getDouble(String category, String key, double baseValue) {
        return new ConfigValueImpl(ValueType.DOUBLE, baseValue, category, key, false);
    }

    public static ConfigValue getString(String category, String key, String baseValue, boolean needSync) {
        return new ConfigValueImpl(ValueType.STRING, baseValue, category, key, needSync);
    }

    public static ConfigValue getString(String category, String key, String baseValue) {
        return new ConfigValueImpl(ValueType.STRING, baseValue, category, key, false);
    }
}
