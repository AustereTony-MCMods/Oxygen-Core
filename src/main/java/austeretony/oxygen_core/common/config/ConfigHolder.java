package austeretony.oxygen_core.common.config;

import java.util.List;

import com.google.gson.JsonObject;

import austeretony.oxygen_core.common.api.config.ConfigValueImpl;
import io.netty.buffer.ByteBuf;

public interface ConfigHolder {

    List<ConfigValue> values();

    String getDomain();

    String getVersion();

    String getExternalPath();

    String getInternalPath();

    void getValues(List<ConfigValue> values);

    void init(JsonObject configObject);

    boolean sync();

    void write(ByteBuf buffer);

    void read(ByteBuf buffer);
}