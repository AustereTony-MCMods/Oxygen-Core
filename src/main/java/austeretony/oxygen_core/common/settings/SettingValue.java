package austeretony.oxygen_core.common.settings;

import com.google.gson.JsonObject;

import austeretony.oxygen_core.common.value.TypedValue;

public interface SettingValue<T extends TypedValue> {

    String getKey();

    String getBaseValue();

    String getUserValue();

    T get();

    void setValue(String value);

    void load(JsonObject jsonObject);

    void save(JsonObject jsonObject);

    void reset();

    default boolean asBoolean() {
        return (Boolean) this.get().getValue();
    }

    default int asInt() {
        return (Integer) this.get().getValue();
    }

    default long asLong() {
        return (Long) this.get().getValue();
    }

    default float asFloat() {
        return (Float) this.get().getValue();
    }

    default String asString() {
        return (String) this.get().getValue();
    }
}
