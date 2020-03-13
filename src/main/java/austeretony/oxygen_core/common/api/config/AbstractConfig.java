package austeretony.oxygen_core.common.api.config;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import austeretony.oxygen_core.common.config.Config;
import austeretony.oxygen_core.common.config.ConfigValue;
import austeretony.oxygen_core.common.main.OxygenMain;
import io.netty.buffer.ByteBuf;

public abstract class AbstractConfig implements Config {

    private final List<ConfigValue> values = new ArrayList<>();

    public AbstractConfig() {
        this.getValues(this.values);
    }

    @Override
    public List<ConfigValue> values() {
        return this.values;
    }

    @Override
    public boolean load(JsonObject configObject) {
        boolean updated = false;
        for (ConfigValue value : this.values)
            if (value.init(configObject) && updated == false)
                updated = true;
        return updated;
    }

    @Override
    public void save(JsonObject configObject) {
        for (ConfigValue value : this.values)
            value.save(configObject);
    }

    @Override
    public void write(ByteBuf buffer) {
        for (ConfigValue value : this.values)
            if (value.needSync())
                value.write(buffer);  
    }

    @Override
    public void read(ByteBuf buffer) {
        for (ConfigValue value : this.values)
            if (value.needSync())
                value.read(buffer);   
        OxygenMain.LOGGER.info("[Core] Synchronized config for <{}>.", this.getDomain());
    }
}
