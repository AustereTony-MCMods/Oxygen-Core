package austeretony.oxygen.common.privilege;

import java.util.Collection;

import com.google.gson.JsonObject;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TextFormatting;

public interface IPrivilegedGroup {

    long getId();

    String getName();

    String getPrefix();

    String getSuffix();

    TextFormatting getUsernameColor();

    TextFormatting getPrefixColor();

    TextFormatting getSuffixColor();

    TextFormatting getChatColor();

    Collection<IPrivilege> getPrivileges();

    boolean hasPrivilege(String privilegeName);

    IPrivilege getPrivilege(String privilegeName);

    void addPrivilege(IPrivilege privilege, boolean save);

    void addPrivileges(boolean save, IPrivilege... privileges);

    void removePrivilege(String privilegeName, boolean save);

    JsonObject serialize();

    void write(PacketBuffer buffer);
}
