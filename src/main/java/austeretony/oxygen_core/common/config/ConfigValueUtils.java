package austeretony.oxygen_core.common.config;

import austeretony.oxygen_core.common.value.TypedValueBoolean;
import austeretony.oxygen_core.common.value.TypedValueFloat;
import austeretony.oxygen_core.common.value.TypedValueInteger;
import austeretony.oxygen_core.common.value.TypedValueLong;
import austeretony.oxygen_core.common.value.TypedValueString;

public class ConfigValueUtils {

    public static ConfigValue getValue(String category, String key, boolean baseValue, boolean needSync) {
        return new ConfigValueImpl(new TypedValueBoolean(baseValue), category, key, needSync);
    }

    public static ConfigValue getValue(String category, String key, boolean baseValue) {
        return new ConfigValueImpl(new TypedValueBoolean(baseValue), category, key, false);
    }

    public static ConfigValue getValue(String category, String key, int baseValue, boolean needSync) {
        return new ConfigValueImpl(new TypedValueInteger(baseValue), category, key, needSync);
    }

    public static ConfigValue getValue(String category, String key, int baseValue) {
        return new ConfigValueImpl(new TypedValueInteger(baseValue), category, key, false);
    }

    public static ConfigValue getValue(String category, String key, long baseValue, boolean needSync) {
        return new ConfigValueImpl(new TypedValueLong(baseValue), category, key, needSync);
    }

    public static ConfigValue getValue(String category, String key, long baseValue) {
        return new ConfigValueImpl(new TypedValueLong(baseValue), category, key, false);
    }

    public static ConfigValue getValue(String category, String key, float baseValue, boolean needSync) {
        return new ConfigValueImpl(new TypedValueFloat(baseValue), category, key, needSync);
    }

    public static ConfigValue getValue(String category, String key, float baseValue) {
        return new ConfigValueImpl(new TypedValueFloat(baseValue), category, key, false);
    }

    public static ConfigValue getValue(String category, String key, String baseValue, boolean needSync) {
        return new ConfigValueImpl(new TypedValueString(baseValue), category, key, needSync);
    }

    public static ConfigValue getValue(String category, String key, String baseValue) {
        return new ConfigValueImpl(new TypedValueString(baseValue), category, key, false);
    }
}
