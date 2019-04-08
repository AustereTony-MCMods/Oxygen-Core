package austeretony.oxygen.common.privilege;

import com.google.gson.JsonObject;

import net.minecraft.network.PacketBuffer;

public interface IPrivilege {

    String getName();

    int getValue();

    JsonObject serialize();

    void write(PacketBuffer buffer);
}
