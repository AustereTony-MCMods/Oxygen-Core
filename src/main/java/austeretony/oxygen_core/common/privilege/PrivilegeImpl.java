package austeretony.oxygen_core.common.privilege;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import austeretony.oxygen_core.common.EnumValueType;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;

public class PrivilegeImpl implements Privilege {

    public final String name;

    public final EnumValueType type;

    private boolean booleanValue;

    private int intValue;

    private long longValue;

    private float floatValue;

    private String stringValue;

    private PrivilegeImpl(String name, EnumValueType type) {
        this.name = name;
        this.type = type;
    }

    public PrivilegeImpl(String name, boolean value) {
        this(name, EnumValueType.BOOLEAN);
        this.booleanValue = value;
    }

    public PrivilegeImpl(String name, int value) {
        this(name, EnumValueType.INT);
        this.intValue = value;
    }

    public PrivilegeImpl(String name, long value) {
        this(name, EnumValueType.LONG);
        this.longValue = value;
    }

    public PrivilegeImpl(String name, float value) {
        this(name, EnumValueType.FLOAT);
        this.floatValue = value;
    }

    public PrivilegeImpl(String name, String value) {
        this(name, EnumValueType.STRING);
        this.stringValue = value;
    }

    @Override
    public String getName() {
        return this.name;
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

    @Override
    public JsonObject serialize() {
        JsonObject privilegeObject = new JsonObject();
        privilegeObject.add(EnumPrivilegeFileKey.NAME.name, new JsonPrimitive(this.getName()));
        privilegeObject.add(EnumPrivilegeFileKey.TYPE.name, new JsonPrimitive(this.getType().ordinal()));
        switch (this.type) {
        case BOOLEAN:
            privilegeObject.add(EnumPrivilegeFileKey.VALUE.name, new JsonPrimitive(this.getBooleanValue()));
            break;
        case INT:
            privilegeObject.add(EnumPrivilegeFileKey.VALUE.name, new JsonPrimitive(this.getIntValue()));
            break;
        case LONG:
            privilegeObject.add(EnumPrivilegeFileKey.VALUE.name, new JsonPrimitive(this.getLongValue()));
            break;
        case FLOAT:
            privilegeObject.add(EnumPrivilegeFileKey.VALUE.name, new JsonPrimitive(this.getFloatValue()));
            break;
        case STRING:
            privilegeObject.add(EnumPrivilegeFileKey.VALUE.name, new JsonPrimitive(this.getStringValue()));
            break;
        }
        return privilegeObject;
    }

    public static PrivilegeImpl deserialize(JsonObject jsonObject) {
        EnumValueType type = EnumValueType.values()[jsonObject.get(EnumPrivilegeFileKey.TYPE.name).getAsInt()];
        String name = jsonObject.get(EnumPrivilegeFileKey.NAME.name).getAsString();
        switch (type) {
        case BOOLEAN:
            return new PrivilegeImpl(name, jsonObject.get(EnumPrivilegeFileKey.VALUE.name).getAsBoolean());
        case INT:
            return new PrivilegeImpl(name, jsonObject.get(EnumPrivilegeFileKey.VALUE.name).getAsInt());
        case LONG:
            return new PrivilegeImpl(name, jsonObject.get(EnumPrivilegeFileKey.VALUE.name).getAsLong());
        case FLOAT:
            return new PrivilegeImpl(name, jsonObject.get(EnumPrivilegeFileKey.VALUE.name).getAsFloat());
        case STRING:
            return new PrivilegeImpl(name, jsonObject.get(EnumPrivilegeFileKey.VALUE.name).getAsString());
        }
        return null;
    }

    @Override
    public void write(ByteBuf buffer) {
        ByteBufUtils.writeString(this.getName(), buffer);
        buffer.writeByte(this.getType().ordinal());
        switch (this.getType()) {
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

    public static PrivilegeImpl read(ByteBuf buffer) {
        String name = ByteBufUtils.readString(buffer);
        switch (EnumValueType.values()[buffer.readByte()]) {
        case BOOLEAN:
            return new PrivilegeImpl(name, buffer.readBoolean());
        case INT:
            return new PrivilegeImpl(name, buffer.readInt());
        case LONG:
            return new PrivilegeImpl(name, buffer.readLong());
        case FLOAT:
            return new PrivilegeImpl(name, buffer.readFloat());
        case STRING:
            return new PrivilegeImpl(name, ByteBufUtils.readString(buffer));
        }
        return null;
    }
}
