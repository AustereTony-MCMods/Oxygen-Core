package austeretony.oxygen_core.common.privilege;

import com.google.gson.JsonObject;

import austeretony.oxygen_core.common.EnumValueType;
import io.netty.buffer.ByteBuf;

public interface Privilege {

    String getName();

    EnumValueType getType();

    boolean getBooleanValue();

    int getIntValue();

    long getLongValue();

    float getFloatValue();

    String getStringValue();

    JsonObject serialize();

    void write(ByteBuf buffer);
}
