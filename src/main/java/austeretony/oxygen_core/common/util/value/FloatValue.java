package austeretony.oxygen_core.common.util.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagFloat;

public class FloatValue implements TypedValue<Float> {

    protected float value;

    public FloatValue() {}

    public FloatValue(float initial) {
        value = initial;
    }

    @Override
    public ValueType getType() {
        return ValueType.FLOAT;
    }

    @Override
    public Float getValue() {
        return value;
    }

    @Override
    public void setValue(Float value) {
        this.value = value;
    }

    @Override
    public void fromString(String str) {
        value = Float.parseFloat(str);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public void fromJson(JsonElement valueElement) {
        value = valueElement.getAsFloat();
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(value);
    }

    @Override
    public NBTBase toNBT() {
        return new NBTTagFloat(value);
    }

    @Override
    public void fromNBT(NBTBase nbtBase) {
        value = ((NBTTagFloat) nbtBase).getFloat();
    }

    @Override
    public void write(ByteBuf buffer) {
        buffer.writeFloat(value);
    }

    @Override
    public void read(ByteBuf buffer) {
        value = buffer.readFloat();
    }

    @Override
    public TypedValue<Float> copy() {
        return new FloatValue(value);
    }
}
