package austeretony.oxygen_core.common.privilege;

import java.util.Collection;

import com.google.gson.JsonObject;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.text.TextFormatting;

public interface PrivilegedGroup {

    long getId();

    String getName();

    String getPrefix();

    String getSuffix();

    TextFormatting getUsernameColor();

    TextFormatting getPrefixColor();

    TextFormatting getSuffixColor();

    TextFormatting getChatColor();

    Collection<Privilege> getPrivileges();

    boolean hasPrivilege(String privilegeName);

    Privilege getPrivilege(String privilegeName);

    void addPrivilege(Privilege privilege, boolean save);

    void addPrivileges(boolean save, Privilege... privileges);

    void removePrivilege(String privilegeName, boolean save);

    JsonObject serialize();

    void write(ByteBuf buffer);
}
