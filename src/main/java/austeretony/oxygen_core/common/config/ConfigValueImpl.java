package austeretony.oxygen_core.common.config;

import austeretony.oxygen_core.common.util.value.TypedValue;
import austeretony.oxygen_core.common.util.value.ValueType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.netty.buffer.ByteBuf;

public class ConfigValueImpl<T> implements ConfigValue<TypedValue<T>> {

    protected final TypedValue<T>  value;
    public final String category, key;
    public final boolean sync;

    public ConfigValueImpl(ValueType type, T def, String category, String key, boolean sync) {
        value = ValueType.createValue(type);
        value.setValue(def);
        this.category = category;
        this.key = key;
        this.sync = sync;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public TypedValue<T> get() {
        return value;
    }

    private JsonElement getValue(JsonObject jsonObject) {
        return jsonObject.get(category).getAsJsonObject().get(key);
    }

    public boolean exist(JsonObject jsonObject) {
        return jsonObject.has(category) && jsonObject.get(category).getAsJsonObject().has(key);
    }

    @Override
    public boolean init(JsonObject jsonObject) {
        boolean created = false;
        if (!exist(jsonObject)) {
            save(jsonObject);
            created = true;
        } else
            value.fromJson(getValue(jsonObject));
        return created;
    }

    @Override
    public void save(JsonObject jsonObject) {
        if (!jsonObject.has(category))
            jsonObject.add(category, new JsonObject());
        jsonObject.get(category).getAsJsonObject().add(key, value.toJson());
    }

    @Override
    public boolean needSync() {
        return sync;
    }

    @Override
    public void write(ByteBuf buffer) {
        value.write(buffer);
    }

    @Override
    public void read(ByteBuf buffer) {
        value.read(buffer);
    }
}
