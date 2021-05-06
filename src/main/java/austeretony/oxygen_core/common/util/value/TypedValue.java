package austeretony.oxygen_core.common.util.value;

import com.google.gson.JsonElement;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;

public interface TypedValue<T> {

    ValueType getType();

    T getValue();

    void setValue(T value);

    void fromString(String str);

    void fromJson(JsonElement valueElement);

    JsonElement toJson();

    NBTBase toNBT();

    void fromNBT(NBTBase nbtBase);

    void write(ByteBuf buffer);

    void read(ByteBuf buffer);

    TypedValue<T> copy();

    default boolean asBoolean() {
        return (Boolean) getValue();
    }

    default int asInt() {
        return (Integer) getValue();
    }

    default long asLong() {
        return (Long) getValue();
    }

    default float asFloat() {
        return (Float) getValue();
    }

    default double asDouble() {
        return (Double) getValue();
    }

    default String asString() {
        return (String) getValue();
    }
}
