package austeretony.oxygen_core.common.value;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import austeretony.oxygen_core.common.EnumValueType;
import austeretony.oxygen_core.common.util.StreamUtils;
import io.netty.buffer.ByteBuf;

public class TypedValueLong implements TypedValue<Long> {

    protected long value;

    public TypedValueLong(long initial) {
        this.value = initial;
    }

    @Override
    public EnumValueType getType() {
        return EnumValueType.LONG;
    }

    @Override
    public Long getValue() {
        return this.value;
    }

    @Override
    public void init(Long value) {
        this.value = value.longValue();
    }

    @Override
    public void fromString(String str) {
        this.value = Long.parseLong(str);
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    @Override
    public void fromJson(JsonElement valueElement) {
        this.value = valueElement.getAsLong();
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
        this.value = StreamUtils.readLong(bis);
    }

    @Override
    public void write(ByteBuf buffer) {
        buffer.writeLong(this.value);
    }

    @Override
    public void read(ByteBuf buffer) {
        this.value = buffer.readLong();
    } 
}
