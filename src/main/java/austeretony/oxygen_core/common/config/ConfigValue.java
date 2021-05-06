package austeretony.oxygen_core.common.config;

import austeretony.oxygen_core.common.util.value.TypedValue;
import com.google.gson.JsonObject;

import io.netty.buffer.ByteBuf;

public interface ConfigValue<T extends TypedValue> {

    String getCategory();

    String getKey();

    T get();

    boolean init(JsonObject jsonObject);

    void save(JsonObject jsonObject);

    boolean needSync();

    void write(ByteBuf buffer);

    void read(ByteBuf buffer);

    default boolean asBoolean() {
        return get().asBoolean();
    }

    default int asInt() {
        return get().asInt();
    }

    default long asLong() {
        return get().asLong();
    }

    default float asFloat() {
        return get().asFloat();
    }

    default double asDouble() {
        return get().asDouble();
    }

    default String asString() {
        return get().asString();
    }
}
