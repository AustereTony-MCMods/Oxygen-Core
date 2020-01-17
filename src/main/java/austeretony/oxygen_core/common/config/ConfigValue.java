package austeretony.oxygen_core.common.config;

import com.google.gson.JsonObject;

import austeretony.oxygen_core.common.value.TypedValue;
import io.netty.buffer.ByteBuf;

public interface ConfigValue<T extends TypedValue> {

    String getCategory();

    String getKey();

    T get();

    void init(JsonObject jsonObject);

    void save(JsonObject jsonObject);

    boolean needSync();

    void write(ByteBuf buffer);

    void read(ByteBuf buffer);

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
