package austeretony.oxygen_core.common.privilege;

import java.util.Collection;

import com.google.gson.JsonObject;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.text.TextFormatting;

public interface Role {

    int getId();

    String getName();

    String getPrefix();

    TextFormatting getNameColor();

    TextFormatting getUsernameColor();

    TextFormatting getPrefixColor();

    TextFormatting getChatColor();

    Collection<Privilege> getPrivileges();

    Privilege getPrivilege(int privilegeId);

    void addPrivilege(Privilege privilege);

    void addPrivileges(Privilege... privileges);

    Privilege removePrivilege(int privilegeId);

    JsonObject serialize();

    void write(ByteBuf buffer);
}
