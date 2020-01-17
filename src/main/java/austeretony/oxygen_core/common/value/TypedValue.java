package austeretony.oxygen_core.common.value;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

import com.google.gson.JsonElement;

import austeretony.oxygen_core.common.EnumValueType;
import io.netty.buffer.ByteBuf;

public interface TypedValue<T> {

    EnumValueType getType();

    T getValue();

    void init(T value);

    void fromString(String str);

    void fromJson(JsonElement valueElement);

    JsonElement toJson();

    void write(BufferedOutputStream bos) throws IOException;

    void read(BufferedInputStream bis) throws IOException;

    void write(ByteBuf buffer);

    void read(ByteBuf buffer);
}
