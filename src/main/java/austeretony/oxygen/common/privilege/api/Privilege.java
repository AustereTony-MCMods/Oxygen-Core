package austeretony.oxygen.common.privilege.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import austeretony.oxygen.common.privilege.IPrivilege;
import austeretony.oxygen.common.privilege.io.EnumPrivilegeFilesKeys;
import austeretony.oxygen.common.util.OxygenUtils;
import austeretony.oxygen.common.util.PacketBufferUtils;
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

    @Override
    public String getPrivilegeName() {
        return this.name;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public JsonObject serealize() {
        JsonObject privilegeObject = new JsonObject();
        privilegeObject.add(OxygenUtils.keyFromEnum(EnumPrivilegeFilesKeys.NAME), new JsonPrimitive(this.getPrivilegeName()));
        privilegeObject.add(OxygenUtils.keyFromEnum(EnumPrivilegeFilesKeys.VALUE), new JsonPrimitive(this.getValue()));
        return privilegeObject;
    }

    public static Privilege deserialize(JsonObject jsonObject) {
        return new Privilege(
                jsonObject.get(OxygenUtils.keyFromEnum(EnumPrivilegeFilesKeys.NAME)).getAsString(),
                jsonObject.get(OxygenUtils.keyFromEnum(EnumPrivilegeFilesKeys.VALUE)).getAsInt());        
    }

    @Override
    public void write(PacketBuffer buffer) {
        PacketBufferUtils.writeString(this.getPrivilegeName(), buffer);
        buffer.writeInt(this.getValue());
    }

    public static Privilege read(PacketBuffer buffer) {
        return new Privilege(PacketBufferUtils.readString(buffer), buffer.readInt());
    }
}
