package austeretony.oxygen_core.common.util.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;

public class HexValue implements TypedValue<Integer> {

    protected int value;

    public HexValue() {}

    public HexValue(int initial) {
        value = initial;
    }

    @Override
    public ValueType getType() {
        return ValueType.HEX;
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
        value = (int) Long.parseLong(str, 16);
    }

    @Override
    public String toString() {
        return Integer.toHexString(value);
    }

    @Override
    public void fromJson(JsonElement valueElement) {
        this.value = (int) Long.parseLong(valueElement.getAsString(), 16);
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(toString());
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
        return new HexValue(value);
    }
}
