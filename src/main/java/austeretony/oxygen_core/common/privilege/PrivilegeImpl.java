package austeretony.oxygen_core.common.privilege;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import austeretony.oxygen_core.common.value.TypedValue;
import io.netty.buffer.ByteBuf;

public class PrivilegeImpl<T extends TypedValue> implements Privilege<T> {

    protected final T value;

    protected final int id;

    public PrivilegeImpl(T value, int id) {
        this.value = value;
        this.id = id;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public T get() {
        return this.value;
    }

    @Override
    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(EnumPrivilegeFileKey.ID.name, new JsonPrimitive(this.getId()));
        jsonObject.add(EnumPrivilegeFileKey.TYPE.name, new JsonPrimitive(this.value.getType().ordinal()));
        jsonObject.add(EnumPrivilegeFileKey.VALUE.name, this.value.toJson());
        return jsonObject;
    }

    @Override
    public void write(ByteBuf buffer) {
        buffer.writeShort(this.id);
        buffer.writeByte(this.value.getType().ordinal());
        this.value.write(buffer);
    }
}
