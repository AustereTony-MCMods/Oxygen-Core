package austeretony.oxygen.common.config;

import java.util.Queue;

import com.google.gson.JsonObject;

import austeretony.oxygen.common.api.config.ConfigValue;
import net.minecraft.network.PacketBuffer;

public interface IConfigHolder {

    Queue<ConfigValue> values();
    
    String getModId();

    void getValues(Queue<ConfigValue> values);

    void init(JsonObject configObject);

    boolean sync();

    void write(PacketBuffer buffer);

    void read(PacketBuffer buffer);
}
