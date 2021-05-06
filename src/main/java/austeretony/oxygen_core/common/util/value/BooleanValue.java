package austeretony.oxygen_core.common.util.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;

public class BooleanValue implements TypedValue<Boolean> {

    protected boolean value;

    public BooleanValue() {}

    public BooleanValue(boolean initial) {
        value = initial;
    }

    @Override
    public ValueType getType() {
        return ValueType.BOOLEAN;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public void fromString(String str) {
        value = Boolean.parseBoolean(str);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public void fromJson(JsonElement valueElement) {
        value = valueElement.getAsBoolean();
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(value);
    }

    @Override
    public NBTBase toNBT() {
        return new NBTTagByte((byte) (value ? 1 : 0));
    }

    @Override
    public void fromNBT(NBTBase nbtBase) {
        value = ((NBTTagByte) nbtBase).getByte() == 1;
    }

    @Override
    public void write(ByteBuf buffer) {
        buffer.writeBoolean(value);
    }

    @Override
    public void read(ByteBuf buffer) {
        value = buffer.readBoolean();
    }

    @Override
    public TypedValue<Boolean> copy() {
        return new BooleanValue(value);
    }
}
