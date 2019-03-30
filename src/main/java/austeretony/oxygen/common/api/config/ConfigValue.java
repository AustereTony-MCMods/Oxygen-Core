package austeretony.oxygen.common.api.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import austeretony.oxygen.common.config.IConfigValue;
import austeretony.oxygen.common.util.PacketBufferUtils;
import net.minecraft.network.PacketBuffer;

public class ConfigValue implements IConfigValue {

    public final EnumValueType type;

    public final String configSection, configKey;

    private boolean booleanValue;

    private int intValue;

    private float floatValue;

    private String stringValue;

    public ConfigValue(EnumValueType type, String configSection, String configKey) {
        this.type = type;
        this.configSection = configSection;
        this.configKey = configKey;
    }

    @Override
    public EnumValueType getType() {
        return this.type;
    }

    @Override
    public boolean getBooleanValue() {
        return this.booleanValue;
    }

    @Override
    public int getIntValue() {
        return this.intValue;
    }

    @Override
    public float getFloatValue() {
        return this.floatValue;
    }

    @Override
    public String getStringValue() {
        return this.stringValue;
    }

    private JsonElement getValue(JsonObject jsonObject) {
        return jsonObject.get(this.configSection).getAsJsonObject().get(this.configKey);
    }

    @Override
    public void init(JsonObject jsonObject) {
        switch (this.type) {
        case BOOLEAN:
            this.booleanValue = this.getValue(jsonObject).getAsBoolean();
            break;
        case INT:
            this.intValue = this.getValue(jsonObject).getAsInt();
            break;
        case FLOAT:
            this.floatValue = this.getValue(jsonObject).getAsFloat();
            break;
        case STRING:
            this.stringValue = this.getValue(jsonObject).getAsString();
            break;
        }
    }

    @Override
    public void write(PacketBuffer buffer) {
        switch (this.type) {
        case BOOLEAN:
            buffer.writeBoolean(this.getBooleanValue());
            break;
        case INT:
            buffer.writeInt(this.getIntValue());
            break;
        case FLOAT:
            buffer.writeFloat(this.getFloatValue());
            break;
        case STRING:
            PacketBufferUtils.writeString(this.getStringValue(), buffer);
            break;
        }
    }

    @Override
    public void read(PacketBuffer buffer) {
        switch (this.type) {
        case BOOLEAN:
            this.booleanValue = buffer.readBoolean();
            break;
        case INT:
            this.intValue = buffer.readInt();
            break;
        case FLOAT:
            this.floatValue = buffer.readFloat();
            break;
        case STRING:
            this.stringValue = PacketBufferUtils.readString(buffer);
            break;
        }
    }

    public enum EnumValueType {

        BOOLEAN,
        INT,
        FLOAT,
        STRING
    }
}
