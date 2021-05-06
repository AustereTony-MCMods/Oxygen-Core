package austeretony.oxygen_core.common.util.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;

public class ByteValue implements TypedValue<Integer> {

    protected int value;

    public ByteValue() {}

    public ByteValue(int initial) {
        value = initial;
    }

    @Override
    public ValueType getType() {
        return ValueType.BYTE;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public void setValue(Integer value) {
        this.value = value.byteValue();
    }

    @Override
    public void fromString(String str) {
        value = Byte.parseByte(str);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public void fromJson(JsonElement valueElement) {
        value = valueElement.getAsByte();
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(value);
    }

    @Override
    public NBTBase toNBT() {
        return new NBTTagByte((byte) value);
    }

    @Override
    public void fromNBT(NBTBase nbtBase) {
        value = ((NBTTagByte) nbtBase).getByte();
    }

    @Override
    public void write(ByteBuf buffer) {
        buffer.writeByte(value);
    }

    @Override
    public void read(ByteBuf buffer) {
        value = buffer.readByte();
    }

    @Override
    public TypedValue<Integer> copy() {
        return new ByteValue(value);
    }
}
