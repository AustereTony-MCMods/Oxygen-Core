package austeretony.oxygen_core.common.value;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import austeretony.oxygen_core.common.EnumValueType;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import austeretony.oxygen_core.common.util.StreamUtils;
import io.netty.buffer.ByteBuf;

public class TypedValueString implements TypedValue<String> {

    protected String value;

    public TypedValueString(String initial) {
        this.value = initial;
    }

    @Override
    public EnumValueType getType() {
        return EnumValueType.STRING;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public void init(String value) {
        this.value = value;
    }

    @Override
    public void fromString(String str) {
        this.value = str;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public void fromJson(JsonElement valueElement) {
        this.value = valueElement.getAsString();
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
        this.value = StreamUtils.readString(bis);
    }

    @Override
    public void write(ByteBuf buffer) {
        ByteBufUtils.writeString(this.value, buffer);
    }

    @Override
    public void read(ByteBuf buffer) {
        this.value = ByteBufUtils.readString(buffer);
    } 
}
