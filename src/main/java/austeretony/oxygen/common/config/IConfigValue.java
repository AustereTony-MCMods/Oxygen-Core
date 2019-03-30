package austeretony.oxygen.common.config;

import com.google.gson.JsonObject;

import austeretony.oxygen.common.api.config.ConfigValue.EnumValueType;
import net.minecraft.network.PacketBuffer;

public interface IConfigValue {

    EnumValueType getType();

    boolean getBooleanValue();

    int getIntValue();

    float getFloatValue();

    String getStringValue();

    void init(JsonObject jsonObject);

    void write(PacketBuffer buffer);

    void read(PacketBuffer buffer);
}
