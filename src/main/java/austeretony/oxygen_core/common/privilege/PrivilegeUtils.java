package austeretony.oxygen_core.common.privilege;

import com.google.gson.JsonObject;

import austeretony.oxygen_core.common.EnumValueType;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import austeretony.oxygen_core.common.value.TypedValueBoolean;
import austeretony.oxygen_core.common.value.TypedValueFloat;
import austeretony.oxygen_core.common.value.TypedValueInteger;
import austeretony.oxygen_core.common.value.TypedValueLong;
import austeretony.oxygen_core.common.value.TypedValueString;
import io.netty.buffer.ByteBuf;

public class PrivilegeUtils {

    public static Privilege getPrivilege(int id, boolean value) {
        return new PrivilegeImpl(new TypedValueBoolean(value), id);
    }

    public static Privilege getPrivilege(int id, int value) {
        return new PrivilegeImpl(new TypedValueInteger(value), id);
    }

    public static Privilege getPrivilege(int id, long value) {
        return new PrivilegeImpl(new TypedValueLong(value), id);
    }

    public static Privilege getPrivilege(int id, float value) {
        return new PrivilegeImpl(new TypedValueFloat(value), id);
    }

    public static Privilege getPrivilege(int id, String value) {
        return new PrivilegeImpl(new TypedValueString(value), id);
    }

    public static Privilege deserialize(JsonObject jsonObject) {
        EnumValueType type = EnumValueType.values()[jsonObject.get(EnumPrivilegeFileKey.TYPE.name).getAsInt()];
        int id = jsonObject.get(EnumPrivilegeFileKey.ID.name).getAsInt();
        switch (type) {
        case BOOLEAN:
            return new PrivilegeImpl(new TypedValueBoolean(jsonObject.get(EnumPrivilegeFileKey.VALUE.name).getAsBoolean()), id);
        case INT:
            return new PrivilegeImpl(new TypedValueInteger(jsonObject.get(EnumPrivilegeFileKey.VALUE.name).getAsInt()), id);
        case LONG:
            return new PrivilegeImpl(new TypedValueLong(jsonObject.get(EnumPrivilegeFileKey.VALUE.name).getAsLong()), id);
        case FLOAT:
            return new PrivilegeImpl(new TypedValueFloat(jsonObject.get(EnumPrivilegeFileKey.VALUE.name).getAsFloat()), id);
        case STRING:
            return new PrivilegeImpl(new TypedValueString(jsonObject.get(EnumPrivilegeFileKey.VALUE.name).getAsString()), id);
        default:
            break;
        }
        return null;
    }

    public static Privilege read(ByteBuf buffer) {
        int id = buffer.readShort();
        switch (EnumValueType.values()[buffer.readByte()]) {
        case BOOLEAN:
            return new PrivilegeImpl(new TypedValueBoolean(buffer.readBoolean()), id);
        case INT:
            return new PrivilegeImpl(new TypedValueInteger(buffer.readInt()), id);
        case LONG:
            return new PrivilegeImpl(new TypedValueLong(buffer.readLong()), id);
        case FLOAT:
            return new PrivilegeImpl(new TypedValueFloat(buffer.readFloat()), id);
        case STRING:
            return new PrivilegeImpl(new TypedValueString(ByteBufUtils.readString(buffer)), id);
        default:
            break;
        }
        return null;
    }
}
