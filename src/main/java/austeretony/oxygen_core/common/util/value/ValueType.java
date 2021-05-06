package austeretony.oxygen_core.common.util.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public enum ValueType {

    BOOLEAN("boolean"),
    BYTE("byte"),
    SHORT("short"),
    INTEGER("integer"),
    LONG("long"),
    FLOAT("float"),
    DOUBLE("double"),
    STRING("string"),
    HEX("hex"),

    CUSTOM("custom");

    private final String id;

    ValueType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static ValueType getById(String id) {
        for (ValueType type : values()) {
            if (type.getId().equals(id)) {
                return type;
            }
        }
        return null;
    }

    @Nullable
    public static TypedValue createValue(@Nullable ValueType type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case BOOLEAN:
                return new BooleanValue();
            case BYTE:
                return new ByteValue();
            case SHORT:
                return new ShortValue();
            case INTEGER:
                return new IntegerValue();
            case LONG:
                return new LongValue();
            case FLOAT:
                return new FloatValue();
            case DOUBLE:
                return new DoubleValue();
            case STRING:
                return new StringValue();
            case HEX:
                return new HexValue();
        }
        return null;
    }

    @Nullable
    public static <T> TypedValue<T> fromString(ValueType type, String valueStr) {
        TypedValue<T> value = createValue(type);
        if (value == null) {
            return null;
        }

        if (type == ValueType.BOOLEAN) {
            if (!valueStr.equals("true") && !valueStr.equals("false")) {
                return null;
            }
        }
        value.fromString(valueStr);

        return value;
    }

    public static <T> JsonObject toJson(@Nonnull TypedValue<T> value) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(value.getType().getId(), value.toJson());
        return jsonObject;
    }

    @Nullable
    public static <T> TypedValue<T> fromJson(JsonObject jsonObject) {
        Map.Entry<String, JsonElement> entry = jsonObject.entrySet().iterator().next();
        ValueType type = getById(entry.getKey());

        TypedValue<T> value = createValue(type);
        if (type != null) {
            value.fromJson(entry.getValue());
        }
        return value;
    }

    public static <T> NBTBase toNBT(TypedValue<T> value) {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setTag(value.getType().getId(), value.toNBT());
        return tagCompound;
    }

    @Nullable
    public static <T> TypedValue<T> fromNBT(NBTBase nbtBase) {
        NBTTagCompound tagCompound = (NBTTagCompound) nbtBase;
        String id = tagCompound.getKeySet().iterator().next();
        ValueType type = getById(id);

        TypedValue<T> value = createValue(type);
        if (type != null) {
            value.fromNBT(tagCompound.getTag(id));
        }
        return value;
    }

    public static <T> void write(@Nonnull TypedValue<T> value, ByteBuf buffer) {
        buffer.writeByte(value.getType().ordinal());
        value.write(buffer);
    }

    @Nullable
    public static <T> TypedValue<T> read(ByteBuf buffer) {
        ValueType type = null;
        try {
            type = ValueType.values()[buffer.readByte()];
        } catch (ArrayIndexOutOfBoundsException ignored) {}

        TypedValue<T> value = createValue(type);
        if (value != null) {
            value.read(buffer);
        }
        return value;
    }
}
