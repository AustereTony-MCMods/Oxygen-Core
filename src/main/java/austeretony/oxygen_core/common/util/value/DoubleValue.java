package austeretony.oxygen_core.common.util.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagDouble;

public class DoubleValue implements TypedValue<Double> {

    protected double value;

    public DoubleValue() {}

    public DoubleValue(double initial) {
        value = initial;
    }

    @Override
    public ValueType getType() {
        return ValueType.DOUBLE;
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public void fromString(String str) {
        value = Double.parseDouble(str);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public void fromJson(JsonElement valueElement) {
        value = valueElement.getAsDouble();
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(value);
    }

    @Override
    public NBTBase toNBT() {
        return new NBTTagDouble(value);
    }

    @Override
    public void fromNBT(NBTBase nbtBase) {
        value = ((NBTTagDouble) nbtBase).getDouble();
    }

    @Override
    public void write(ByteBuf buffer) {
        buffer.writeDouble(value);
    }

    @Override
    public void read(ByteBuf buffer) {
        value = buffer.readDouble();
    }

    @Override
    public TypedValue<Double> copy() {
        return new DoubleValue(value);
    }
}
