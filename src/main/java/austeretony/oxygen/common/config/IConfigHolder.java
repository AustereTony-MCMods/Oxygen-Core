package austeretony.oxygen.common.config;

import java.util.List;

import com.google.gson.JsonObject;

import austeretony.oxygen.common.api.config.ConfigValue;
import net.minecraft.network.PacketBuffer;

public interface IConfigHolder {

    List<ConfigValue> values();

    String getModId();

    String getVersion();

    String getExternalPath();

    String getInternalPath();

    void getValues(List<ConfigValue> values);

    void init(JsonObject configObject);

    boolean sync();

    void write(PacketBuffer buffer);

    void read(PacketBuffer buffer);
}
