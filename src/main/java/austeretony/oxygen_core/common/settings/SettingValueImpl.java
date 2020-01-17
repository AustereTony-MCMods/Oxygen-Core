package austeretony.oxygen_core.common.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import austeretony.oxygen_core.common.value.TypedValue;

public class SettingValueImpl<T extends TypedValue> implements SettingValue<T> {

    protected final T value;

    protected final String key, baseValue;

    protected String userValue;

    public SettingValueImpl(T value, String key, String baseValue) {
        this.value = value;
        this.key = key;
        this.baseValue = baseValue;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public String getBaseValue() {
        return this.baseValue;
    }

    @Override
    public String getUserValue() {
        return this.userValue;
    }

    @Override
    public T get() {
        return this.value;
    }

    @Override
    public void setValue(String value) {
        this.userValue = value;
        this.value.fromString(this.userValue == null ? this.baseValue : this.userValue);
    }

    public boolean exist(JsonObject jsonObject) {
        return jsonObject.has(this.key);
    }

    private JsonElement getValue(JsonObject jsonObject) {
        return jsonObject.get(this.key);
    }

    @Override
    public void load(JsonObject jsonObject) {
        if (!this.exist(jsonObject))
            this.save(jsonObject);
        else {
            this.value.fromJson(this.getValue(jsonObject));
            this.userValue = this.value.toString();
        }
    }

    @Override
    public void save(JsonObject jsonObject) {
        if (this.userValue == null)
            this.userValue = this.baseValue;
        jsonObject.add(this.key, this.value.toJson());
    }

    @Override
    public void reset() {
        this.value.fromString(this.userValue = this.baseValue);
    }
}
