package austeretony.oxygen.common.privilege.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import austeretony.oxygen.common.privilege.IPrivilege;
import austeretony.oxygen.common.privilege.io.EnumPrivilegeFileKey;
import austeretony.oxygen.util.OxygenUtils;
import austeretony.oxygen.util.PacketBufferUtils;
import net.minecraft.network.PacketBuffer;

public class Privilege implements IPrivilege {

    public final String name;

    private int value;

    public Privilege(String name) {
        this.name = name;
    }

    public Privilege(String name, int value) {
        this(name);
        this.value = value;
    }

    public Privilege(String name, boolean flag) {
        this(name);
        this.value = flag ? 1 : 0;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public JsonObject serialize() {
        JsonObject privilegeObject = new JsonObject();
        privilegeObject.add(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.NAME), new JsonPrimitive(this.getName()));
        privilegeObject.add(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.VALUE), new JsonPrimitive(this.getValue()));
        return privilegeObject;
    }

    public static Privilege deserialize(JsonObject jsonObject) {
        return new Privilege(
                jsonObject.get(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.NAME)).getAsString(),
                jsonObject.get(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.VALUE)).getAsInt());        
    }

    @Override
    public void write(PacketBuffer buffer) {
        PacketBufferUtils.writeString(this.getName(), buffer);
        buffer.writeInt(this.getValue());
    }

    public static Privilege read(PacketBuffer buffer) {
        return new Privilege(PacketBufferUtils.readString(buffer), buffer.readInt());
    }
}
