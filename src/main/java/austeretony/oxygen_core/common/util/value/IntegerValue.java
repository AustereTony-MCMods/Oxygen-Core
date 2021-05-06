package austeretony.oxygen_core.common.util.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;

public class IntegerValue implements TypedValue<Integer> {

    protected int value;

    public IntegerValue() {}

    public IntegerValue(int initial) {
        value = initial;
    }

    @Override
    public ValueType getType() {
        return ValueType.INTEGER;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public void fromString(String str) {
        value = Integer.parseInt(str);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public void fromJson(JsonElement valueElement) {
        value = valueElement.getAsInt();
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(value);
    }

    @Override
    public NBTBase toNBT() {
        return new NBTTagInt(value);
    }

    @Override
    public void fromNBT(NBTBase nbtBase) {
        value = ((NBTTagInt) nbtBase).getInt();
    }

    @Override
    public void write(ByteBuf buffer) {
        buffer.writeInt(value);
    }

    @Override
    public void read(ByteBuf buffer) {
        value = buffer.readInt();
    }

    @Override
    public TypedValue<Integer> copy() {
        return new IntegerValue(value);
    }
}
