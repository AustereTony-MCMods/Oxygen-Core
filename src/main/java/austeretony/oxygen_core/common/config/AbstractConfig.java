package austeretony.oxygen_core.common.config;

import austeretony.oxygen_core.common.main.OxygenMain;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractConfig implements Config {

    private final List<ConfigValue> values = new ArrayList<>();

    public AbstractConfig() {
        getValues(values);
    }

    @Override
    public List<ConfigValue> values() {
        return values;
    }

    @Override
    public boolean load(JsonObject configObject) {
        boolean updated = false;
        for (ConfigValue value : values) {
            if (value.init(configObject) && !updated) {
                updated = true;
            }
        }
        return updated;
    }

    @Override
    public void save(JsonObject configObject) {
        for (ConfigValue value : values) {
            value.save(configObject);
        }
    }

    @Override
    public void write(ByteBuf buffer) {
        for (ConfigValue value : values) {
            if (value.needSync()) {
                value.write(buffer);
            }
        }
    }

    @Override
    public void read(ByteBuf buffer) {
        for (ConfigValue value : values) {
            if (value.needSync()) {
                value.read(buffer);
            }
        }
        OxygenMain.logInfo(1, "[Core] Synchronized config for <{}>.", getDomain());
    }
}
