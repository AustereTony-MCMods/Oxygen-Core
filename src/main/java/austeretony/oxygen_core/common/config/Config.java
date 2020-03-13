package austeretony.oxygen_core.common.config;

import java.util.List;

import com.google.gson.JsonObject;

import io.netty.buffer.ByteBuf;

public interface Config {

    List<ConfigValue> values();

    String getDomain();

    String getVersion();

    String getExternalPath();

    void getValues(List<ConfigValue> values);

    boolean load(JsonObject configObject);

    void save(JsonObject configObject);

    void write(ByteBuf buffer);

    void read(ByteBuf buffer);
}
