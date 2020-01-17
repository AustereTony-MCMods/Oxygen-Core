package austeretony.oxygen_core.common.value;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import austeretony.oxygen_core.common.EnumValueType;
import austeretony.oxygen_core.common.util.StreamUtils;
import io.netty.buffer.ByteBuf;

public class TypedValueBoolean implements TypedValue<Boolean> {

    protected boolean value;

    public TypedValueBoolean(boolean initial) {
        this.value = initial;
    }

    @Override
    public EnumValueType getType() {
        return EnumValueType.BOOLEAN;
    }

    @Override
    public Boolean getValue() {
        return this.value;
    }

    @Override
    public void init(Boolean value) {
        this.value = value.booleanValue();
    }

    @Override
    public void fromString(String str) {
        this.value = Boolean.parseBoolean(str);
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    @Override
    public void fromJson(JsonElement valueElement) {
        this.value = valueElement.getAsBoolean();
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(this.value);
    }

    @Override
    public void write(BufferedOutputStream bos) throws IOException {
        StreamUtils.write(this.value, bos);
    }

    @Override
    public void read(BufferedInputStream bis) throws IOException {
        this.value = StreamUtils.readBoolean(bis);
    }

    @Override
    public void write(ByteBuf buffer) {
        buffer.writeBoolean(this.value);
    }

    @Override
    public void read(ByteBuf buffer) {
        this.value = buffer.readBoolean();
    }
}
