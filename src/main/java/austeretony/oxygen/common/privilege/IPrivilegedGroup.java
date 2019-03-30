package austeretony.oxygen.common.privilege;

import java.util.Collection;

import com.google.gson.JsonObject;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TextFormatting;

public interface IPrivilegedGroup {

    long getGroupId();

    String getGroupName();

    String getTitle();

    String getPrefix();

    String getSuffix();

    TextFormatting getNicknameColor();

    TextFormatting getPrefixColor();

    TextFormatting getSuffixColor();

    TextFormatting getChatColor();

    Collection<IPrivilege> getPrivileges();

    boolean hasPrivilege(String privilegeName);

    IPrivilege getPrivilege(String privilegeName);

    void addPrivilege(IPrivilege privilege);

    void removePrivilege(String privilegeName);

    JsonObject serialize();

    void write(PacketBuffer buffer);
}
