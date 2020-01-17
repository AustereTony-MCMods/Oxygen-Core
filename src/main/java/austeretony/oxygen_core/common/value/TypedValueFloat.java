package austeretony.oxygen_core.common.value;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import austeretony.oxygen_core.common.EnumValueType;
import austeretony.oxygen_core.common.util.StreamUtils;
import io.netty.buffer.ByteBuf;

public class TypedValueFloat implements TypedValue<Float> {

    protected float value;

    public TypedValueFloat(float initial) {
        this.value = initial;
    }

    @Override
    public EnumValueType getType() {
        return EnumValueType.FLOAT;
    }

    @Override
    public Float getValue() {
        return this.value;
    }

    @Override
    public void init(Float value) {
        this.value = value.floatValue();
    }

    @Override
    public void fromString(String str) {
        this.value = Float.parseFloat(str);
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    @Override
    public void fromJson(JsonElement valueElement) {
        this.value = valueElement.getAsFloat();
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
        this.value = StreamUtils.readFloat(bis);
    }

    @Override
    public void write(ByteBuf buffer) {
        buffer.writeFloat(this.value);
    }

    @Override
    public void read(ByteBuf buffer) {
        this.value = buffer.readFloat();
    } 
}
