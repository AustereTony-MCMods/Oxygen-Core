package austeretony.oxygen_core.common.config;

import com.google.gson.JsonObject;

import austeretony.oxygen_core.common.EnumValueType;
import io.netty.buffer.ByteBuf;

public interface ConfigValue {

    EnumValueType getType();

    String getCategory();

    String getKey();

    boolean getBooleanValue();

    int getIntValue();

    long getLongValue();

    float getFloatValue();

    String getStringValue();

    void init(JsonObject jsonObject);

    void write(ByteBuf buffer);

    void read(ByteBuf buffer);
}
