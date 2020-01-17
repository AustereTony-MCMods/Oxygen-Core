package austeretony.oxygen_core.common.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import austeretony.oxygen_core.common.value.TypedValue;
import io.netty.buffer.ByteBuf;

public class ConfigValueImpl<T extends TypedValue> implements ConfigValue<T> {

    protected final T value;

    public final String category, key;

    public final boolean sync;

    public ConfigValueImpl(T value, String category, String key, boolean sync) {
        this.value = value;
        this.category = category;
        this.key = key;
        this.sync = sync;
    }

    @Override
    public String getCategory() {
        return this.category;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public T get() {
        return this.value;
    }

    private JsonElement getValue(JsonObject jsonObject) {
        return jsonObject.get(this.category).getAsJsonObject().get(this.key);
    }

    public boolean exist(JsonObject jsonObject) {
        return jsonObject.has(this.category) && jsonObject.get(this.category).getAsJsonObject().has(this.key);
    }

    @Override
    public void init(JsonObject jsonObject) {
        if (!this.exist(jsonObject))
            this.save(jsonObject);
        else                
            this.value.fromJson(this.getValue(jsonObject));
    }

    @Override
    public void save(JsonObject jsonObject) {
        if (!jsonObject.has(this.category))
            jsonObject.add(this.category, new JsonObject());
        jsonObject.get(this.category).getAsJsonObject().add(this.key, this.value.toJson());
    }

    @Override
    public boolean needSync() {
        return this.sync;
    }

    @Override
    public void write(ByteBuf buffer) {
        this.value.write(buffer);
    }

    @Override
    public void read(ByteBuf buffer) {
        this.value.read(buffer);
    }
}
