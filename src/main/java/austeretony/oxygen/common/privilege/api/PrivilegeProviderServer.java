package austeretony.oxygen.common.privilege.api;

import java.util.UUID;

import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.privilege.IPrivilegedGroup;
import austeretony.oxygen.common.privilege.PrivilegeManagerServer;
import net.minecraft.entity.player.EntityPlayer;

public class PrivilegeProviderServer {

    public static boolean groupExist(String groupName){
        return PrivilegeManagerServer.instance().groupExist(groupName);
    }

    public static IPrivilegedGroup getGroup(String groupName){
        return PrivilegeManagerServer.instance().getGroup(groupName);
    }

    public static IPrivilegedGroup getDefaultGroup(){
        return getGroup(PrivilegedGroup.DEFAULT_GROUP.getGroupName());
    }

    public static IPrivilegedGroup getPlayerGroup(UUID playerUUID){
        return PrivilegeManagerServer.instance().getPlayerPrivilegedGroup(playerUUID);
    }

    public static void addGroup(IPrivilegedGroup group) {
        PrivilegeManagerServer.instance().addGroup(group);
    }

    public static void removeGroup(String groupName) {
        PrivilegeManagerServer.instance().removeGroup(groupName);
    }

    public static void promotePlayer(EntityPlayer player, String groupName) {
        PrivilegeManagerServer.instance().promotePlayer(OxygenHelperServer.uuid(player), groupName);
    }

    public static void resetPlayerGroup(EntityPlayer player) {
        PrivilegeManagerServer.instance().promotePlayer(OxygenHelperServer.uuid(player), PrivilegedGroup.DEFAULT_GROUP.getGroupName());
    }

    public static void addPrivilege(String groupName, String privilegeName) {
        PrivilegeManagerServer.instance().getGroup(groupName).addPrivilege(new Privilege(privilegeName));
    }

    public static void addPrivilege(String groupName, String privilegeName, int value) {
        PrivilegeManagerServer.instance().getGroup(groupName).addPrivilege(new Privilege(privilegeName, value));
    }

    public static void removePrivilege(String groupName, String privilegeName) {
        PrivilegeManagerServer.instance().getGroup(groupName).removePrivilege(privilegeName);
    }

    public static boolean getPrivilegeValue(UUID playerUUID, String privilegeName, boolean defaultValue) {
        if (OxygenConfig.PRIVILEGES.getBooleanValue() && PrivilegeManagerServer.instance().getPlayerPrivilegedGroup(playerUUID).hasPrivilege(privilegeName))
            return true;
        return defaultValue;
    }

    public static int getPrivilegeValue(UUID playerUUID, String privilegeName, int defaultValue) {
        if (OxygenConfig.PRIVILEGES.getBooleanValue() && PrivilegeManagerServer.instance().getPlayerPrivilegedGroup(playerUUID).hasPrivilege(privilegeName))
            return PrivilegeManagerServer.instance().getPlayerPrivilegedGroup(playerUUID).getPrivilege(privilegeName).getValue();
        return defaultValue;
    }
}
