package austeretony.oxygen_core.client.settings;

import austeretony.oxygen_core.client.gui.base.common.WidgetGroup;
import austeretony.oxygen_core.common.util.value.TypedValue;
import austeretony.oxygen_core.common.util.value.ValueType;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class SettingValueImpl<T> implements SettingValue<TypedValue<T>, T> {

    protected final TypedValue<T> value;
    protected T defaultValue;
    private final SettingType type;
    private final String id, domain, moduleName, category;
    @Nonnull
    private final Function<SettingValue, WidgetGroup> widgetSupplier;

    public SettingValueImpl(String domain, SettingType type, String moduleName, String category,
                            ValueType valueType, String id, T defaultValue,
                            @Nonnull Function<SettingValue, WidgetGroup> widgetSupplier) {
        this.value = ValueType.createValue(valueType);
        this.value.setValue(defaultValue);
        this.defaultValue = defaultValue;
        this.id = id;
        this.domain = domain;
        this.type = type;
        this.moduleName = moduleName;
        this.category = category;
        this.widgetSupplier = widgetSupplier;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public SettingType getType() {
        return type;
    }

    @Override
    public String getDomain() {
        return domain;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public TypedValue<T> get() {
        return value;
    }

    @Override
    public T getDefault() {
        return defaultValue;
    }

    @Override
    public void fromJson(JsonObject jsonObject) {
        JsonObject domainObject;
        if (jsonObject.has(getDomain())) {
            domainObject = jsonObject.get(getDomain()).getAsJsonObject();
            if (domainObject.has(getId()))
                value.fromJson(domainObject.get(getId()));
            else
                domainObject.add(getId(), value.toJson());
        } else {
            domainObject = new JsonObject();
            domainObject.add(getId(), value.toJson());
            jsonObject.add(getDomain(), domainObject);
        }
    }

    @Override
    public void reset() {
        value.setValue(defaultValue);
    }

    @Override
    @Nonnull
    public Function<SettingValue, WidgetGroup> getWidgetSupplier() {
        return widgetSupplier;
    }
}
