package austeretony.oxygen_core.common.util.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagShort;

public class ShortValue implements TypedValue<Integer> {

    protected int value;

    public ShortValue() {}

    public ShortValue(int initial) {
        value = initial;
    }

    @Override
    public ValueType getType() {
        return ValueType.SHORT;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public void setValue(Integer value) {
        this.value = value.shortValue();
    }

    @Override
    public void fromString(String str) {
        value = Short.parseShort(str);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public void fromJson(JsonElement valueElement) {
        value = valueElement.getAsShort();
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(value);
    }

    @Override
    public NBTBase toNBT() {
        return new NBTTagShort((short) value);
    }

    @Override
    public void fromNBT(NBTBase nbtBase) {
        value = ((NBTTagShort) nbtBase).getShort();
    }

    @Override
    public void write(ByteBuf buffer) {
        buffer.writeShort(value);
    }

    @Override
    public void read(ByteBuf buffer) {
        value = buffer.readShort();
    }

    @Override
    public TypedValue<Integer> copy() {
        return new ShortValue(value);
    }
}
