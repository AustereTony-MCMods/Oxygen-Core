package austeretony.oxygen_core.common.util.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagLong;

public class LongValue implements TypedValue<Long> {

    protected long value;

    public LongValue() {}

    public LongValue(long initial) {
        value = initial;
    }

    @Override
    public ValueType getType() {
        return ValueType.LONG;
    }

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public void setValue(Long value) {
        this.value = value;
    }

    @Override
    public void fromString(String str) {
        value = Long.parseLong(str);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public void fromJson(JsonElement valueElement) {
        value = valueElement.getAsLong();
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(value);
    }

    @Override
    public NBTBase toNBT() {
        return new NBTTagLong(value);
    }

    @Override
    public void fromNBT(NBTBase nbtBase) {
        value = ((NBTTagLong) nbtBase).getLong();
    }

    @Override
    public void write(ByteBuf buffer) {
        buffer.writeLong(value);
    }

    @Override
    public void read(ByteBuf buffer) {
        value = buffer.readLong();
    }

    @Override
    public TypedValue<Long> copy() {
        return new LongValue(value);
    }
}
