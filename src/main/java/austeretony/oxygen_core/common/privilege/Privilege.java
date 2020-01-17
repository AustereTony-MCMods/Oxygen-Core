package austeretony.oxygen_core.common.privilege;

import com.google.gson.JsonObject;

import austeretony.oxygen_core.common.value.TypedValue;
import io.netty.buffer.ByteBuf;

public interface Privilege<T extends TypedValue> {

    int getId();

    T get();

    JsonObject toJson();

    void write(ByteBuf buffer);
}
