package austeretony.oxygen_core.common.settings;

import austeretony.oxygen_core.common.EnumValueType;
import austeretony.oxygen_core.common.value.TypedValueBoolean;
import austeretony.oxygen_core.common.value.TypedValueFloat;
import austeretony.oxygen_core.common.value.TypedValueHex;
import austeretony.oxygen_core.common.value.TypedValueInteger;
import austeretony.oxygen_core.common.value.TypedValueLong;
import austeretony.oxygen_core.common.value.TypedValueString;

public class SettingValueUtils {

    public static SettingValue getValue(EnumValueType type, String key, String baseValue) {
        switch (type) {
        case BOOLEAN:
            return new SettingValueImpl(new TypedValueBoolean(Boolean.parseBoolean(baseValue)), key, baseValue);
        case INT:
            return new SettingValueImpl(new TypedValueInteger(Integer.parseInt(baseValue)), key, baseValue);
        case LONG:
            return new SettingValueImpl(new TypedValueLong(Long.parseLong(baseValue)), key, baseValue);
        case FLOAT:
            return new SettingValueImpl(new TypedValueFloat(Float.parseFloat(baseValue)), key, baseValue);
        case STRING:
            return new SettingValueImpl(new TypedValueString(baseValue), key, baseValue);
        case HEX:        
            return new SettingValueImpl(new TypedValueHex((int) Long.parseLong(baseValue, 16)), key, baseValue);
        default:
            return null;
        }
    }
}
