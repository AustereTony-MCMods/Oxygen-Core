package austeretony.oxygen_core.common.api.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import austeretony.oxygen_core.common.EnumValueType;
import austeretony.oxygen_core.common.config.ConfigValue;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;

public class ConfigValueImpl implements ConfigValue {

    public final EnumValueType type;

    public final String category, key;

    private boolean booleanValue;

    private int intValue;

    private long longValue;

    private float floatValue;

    private String stringValue;

    public ConfigValueImpl(EnumValueType type, String category, String key) {
        this.type = type;
        this.category = category;
        this.key = key;
    }

    @Override
    public EnumValueType getType() {
        return this.type;
    }

    @Override
    public String getCategory() {
        return this.category;
    }

    @Override
    public String getKey() {
        return this.key;
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
    public long getLongValue() {
        return this.longValue;
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
        return jsonObject.get(this.category).getAsJsonObject().get(this.key);
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
        case LONG:
            this.longValue = this.getValue(jsonObject).getAsLong();
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
    public void write(ByteBuf buffer) {
        switch (this.type) {
        case BOOLEAN:
            buffer.writeBoolean(this.getBooleanValue());
            break;
        case INT:
            buffer.writeInt(this.getIntValue());
            break;
        case LONG:
            buffer.writeLong(this.getLongValue());
            break;
        case FLOAT:
            buffer.writeFloat(this.getFloatValue());
            break;
        case STRING:
            ByteBufUtils.writeString(this.getStringValue(), buffer);
            break;
        }
    }

    @Override
    public void read(ByteBuf buffer) {
        switch (this.type) {
        case BOOLEAN:
            this.booleanValue = buffer.readBoolean();
            break;
        case INT:
            this.intValue = buffer.readInt();
            break;
        case LONG:
            this.longValue = buffer.readLong();
            break;
        case FLOAT:
            this.floatValue = buffer.readFloat();
            break;
        case STRING:
            this.stringValue = ByteBufUtils.readString(buffer);
            break;
        }
    }
}
