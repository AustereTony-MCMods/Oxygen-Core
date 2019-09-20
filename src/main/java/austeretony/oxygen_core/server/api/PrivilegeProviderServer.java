package austeretony.oxygen_core.server.api;

import java.util.UUID;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.privilege.Privilege;
import austeretony.oxygen_core.common.privilege.PrivilegedGroup;
import austeretony.oxygen_core.common.privilege.PrivilegedGroupImpl;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.config.OxygenConfigServer;
import net.minecraft.entity.player.EntityPlayer;

public class PrivilegeProviderServer {

    public static boolean groupExist(String groupName){
        return OxygenManagerServer.instance().getPrivilegesManager().groupExist(groupName);
    }

    public static PrivilegedGroup getGroup(String groupName){
        return OxygenManagerServer.instance().getPrivilegesManager().getGroup(groupName);
    }

    public static PrivilegedGroup getDefaultGroup(){
        return getGroup(PrivilegedGroupImpl.DEFAULT_GROUP.getName());
    }

    public static PrivilegedGroup getPlayerGroup(UUID playerUUID){
        return OxygenManagerServer.instance().getPrivilegesManager().getPlayerPrivilegedGroup(playerUUID);
    }

    public static void addGroup(PrivilegedGroup group, boolean save) {
        OxygenManagerServer.instance().getPrivilegesManager().addGroup(group, save);
    }

    public static void removeGroup(String groupName) {
        OxygenManagerServer.instance().getPrivilegesManager().removeGroup(groupName);
    }

    public static void promotePlayer(UUID playerUUID, String groupName) {
        OxygenManagerServer.instance().getPrivilegesManager().promotePlayer(playerUUID, groupName);
    }

    public static void promotePlayer(EntityPlayer player, String groupName) {
        promotePlayer(CommonReference.getPersistentUUID(player), groupName);
    }

    public static void resetPlayerGroup(UUID playerUUID) {
        promotePlayer(playerUUID, PrivilegedGroupImpl.DEFAULT_GROUP.getName());
    }

    public static void resetPlayerGroup(EntityPlayer player) {
        promotePlayer(CommonReference.getPersistentUUID(player), PrivilegedGroupImpl.DEFAULT_GROUP.getName());
    }

    public static void addPrivilege(String groupName, Privilege privilege, boolean save) {
        OxygenManagerServer.instance().getPrivilegesManager().getGroup(groupName).addPrivilege(privilege, save);
    }

    public static void addPrivileges(String groupName, boolean save, Privilege... privileges) {
        OxygenManagerServer.instance().getPrivilegesManager().getGroup(groupName).addPrivileges(save, privileges);
    }

    public static void removePrivilege(String groupName, String privilegeName, boolean save) {
        OxygenManagerServer.instance().getPrivilegesManager().getGroup(groupName).removePrivilege(privilegeName, save);
    }

    public static boolean getValue(UUID playerUUID, String privilegeName, boolean defaultValue) {
        if (OxygenConfigServer.ENABLE_PRIVILEGES.getBooleanValue()) { 
            PrivilegedGroup group = OxygenManagerServer.instance().getPrivilegesManager().getPlayerPrivilegedGroup(playerUUID);
            if (group.hasPrivilege(privilegeName))
                return group.getPrivilege(privilegeName).getBooleanValue();
        }
        return defaultValue;
    }

    public static int getValue(UUID playerUUID, String privilegeName, int defaultValue) {
        if (OxygenConfigServer.ENABLE_PRIVILEGES.getBooleanValue()) { 
            PrivilegedGroup group = OxygenManagerServer.instance().getPrivilegesManager().getPlayerPrivilegedGroup(playerUUID);
            if (group.hasPrivilege(privilegeName))
                return group.getPrivilege(privilegeName).getIntValue();
        }
        return defaultValue;
    }

    public static long getValue(UUID playerUUID, String privilegeName, long defaultValue) {
        if (OxygenConfigServer.ENABLE_PRIVILEGES.getBooleanValue()) { 
            PrivilegedGroup group = OxygenManagerServer.instance().getPrivilegesManager().getPlayerPrivilegedGroup(playerUUID);
            if (group.hasPrivilege(privilegeName))
                return group.getPrivilege(privilegeName).getLongValue();
        }
        return defaultValue;
    }

    public static float getValue(UUID playerUUID, String privilegeName, float defaultValue) {
        if (OxygenConfigServer.ENABLE_PRIVILEGES.getBooleanValue()) { 
            PrivilegedGroup group = OxygenManagerServer.instance().getPrivilegesManager().getPlayerPrivilegedGroup(playerUUID);
            if (group.hasPrivilege(privilegeName))
                return group.getPrivilege(privilegeName).getFloatValue();
        }
        return defaultValue;
    }

    public static String getValue(UUID playerUUID, String privilegeName, String defaultValue) {
        if (OxygenConfigServer.ENABLE_PRIVILEGES.getBooleanValue()) { 
            PrivilegedGroup group = OxygenManagerServer.instance().getPrivilegesManager().getPlayerPrivilegedGroup(playerUUID);
            if (group.hasPrivilege(privilegeName))
                return group.getPrivilege(privilegeName).getStringValue();
        }
        return defaultValue;
    }
}
