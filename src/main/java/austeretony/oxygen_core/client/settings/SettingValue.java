package austeretony.oxygen_core.client.settings;

import austeretony.oxygen_core.client.gui.base.common.WidgetGroup;
import austeretony.oxygen_core.common.util.value.TypedValue;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;
import java.util.function.Function;

public interface SettingValue<T extends TypedValue, V> {

    String getId();

    SettingType getType();

    String getDomain();

    String getModuleName();

    String getCategory();

    T get();

    V getDefault();

    void fromJson(JsonObject jsonObject);

    void reset();

    @Nonnull
    Function<SettingValue, WidgetGroup> getWidgetSupplier();

    default boolean asBoolean() {
        return get().asBoolean();
    }

    default int asInt() {
        return get().asInt();
    }

    default long asLong() {
        return get().asLong();
    }

    default float asFloat() {
        return get().asFloat();
    }

    default double asDouble() {
        return get().asDouble();
    }

    default String asString() {
        return get().asString();
    }
}
