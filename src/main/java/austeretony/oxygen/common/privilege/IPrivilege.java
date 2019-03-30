package austeretony.oxygen.common.privilege;

import com.google.gson.JsonObject;

import net.minecraft.network.PacketBuffer;

public interface IPrivilege {

    String getPrivilegeName();

    int getValue();

    JsonObject serealize();

    void write(PacketBuffer buffer);
}
