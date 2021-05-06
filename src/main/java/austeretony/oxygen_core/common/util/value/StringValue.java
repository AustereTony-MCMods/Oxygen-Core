package austeretony.oxygen_core.common.util.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import austeretony.oxygen_core.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;

public class StringValue implements TypedValue<String> {

    protected String value;

    public StringValue() {
        value = "";
    }

    public StringValue(String initial) {
        value = initial;
    }

    @Override
    public ValueType getType() {
        return ValueType.STRING;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void fromString(String str) {
        value = str;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public void fromJson(JsonElement valueElement) {
        value = valueElement.getAsString();
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(value);
    }

    @Override
    public NBTBase toNBT() {
        return new NBTTagString(value);
    }

    @Override
    public void fromNBT(NBTBase nbtBase) {
        value = ((NBTTagString) nbtBase).getString();
    }

    @Override
    public void write(ByteBuf buffer) {
        ByteBufUtils.writeString(value, buffer);
    }

    @Override
    public void read(ByteBuf buffer) {
        value = ByteBufUtils.readString(buffer);
    }

    @Override
    public TypedValue<String> copy() {
        return new StringValue(value);
    }
}
