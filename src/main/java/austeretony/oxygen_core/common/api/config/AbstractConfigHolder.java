package austeretony.oxygen_core.common.api.config;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import austeretony.oxygen_core.common.config.ConfigHolder;
import austeretony.oxygen_core.common.config.ConfigValue;
import austeretony.oxygen_core.common.main.OxygenMain;
import io.netty.buffer.ByteBuf;

public abstract class AbstractConfigHolder implements ConfigHolder {

    private final List<ConfigValue> values = new ArrayList<>(10);

    public AbstractConfigHolder() {
        this.getValues(this.values);
    }

    @Override
    public List<ConfigValue> values() {
        return this.values;
    }

    @Override
    public void init(JsonObject configObject) {
        for (ConfigValue value : this.values)
            value.init(configObject);
    }

    @Override
    public void write(ByteBuf buffer) {
        if (this.sync())
            for (ConfigValue value : this.values)
                value.write(buffer);  
    }

    @Override
    public void read(ByteBuf buffer) {
        if (this.sync()) {
            for (ConfigValue value : this.values)
                value.read(buffer);   
            OxygenMain.LOGGER.info("Synchronized config for <{}>.", this.getDomain());
        }
    }
}
