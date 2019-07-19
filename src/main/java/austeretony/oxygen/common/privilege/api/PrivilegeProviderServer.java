package austeretony.oxygen.common.privilege.api;

import java.util.UUID;

import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.privilege.IPrivilege;
import austeretony.oxygen.common.privilege.IPrivilegedGroup;
import austeretony.oxygen.common.privilege.PrivilegeManagerServer;
import net.minecraft.entity.player.EntityPlayer;

public class PrivilegeProviderServer {

    public static boolean groupExist(String groupName){
        return OxygenManagerServer.instance().getPrivilegeManager().groupExist(groupName);
    }

    public static IPrivilegedGroup getGroup(String groupName){
        return OxygenManagerServer.instance().getPrivilegeManager().getGroup(groupName);
    }

    public static IPrivilegedGroup getDefaultGroup(){
        return getGroup(PrivilegedGroup.DEFAULT_GROUP.getName());
    }

    public static IPrivilegedGroup getPlayerGroup(UUID playerUUID){
        return OxygenManagerServer.instance().getPrivilegeManager().getPlayerPrivilegedGroup(playerUUID);
    }

    public static void addGroup(IPrivilegedGroup group, boolean save) {
        OxygenManagerServer.instance().getPrivilegeManager().addGroup(group, save);
    }

    public static void removeGroup(String groupName) {
        OxygenManagerServer.instance().getPrivilegeManager().removeGroup(groupName);
    }

    public static void promotePlayer(UUID playerUUID, String groupName) {
        OxygenManagerServer.instance().getPrivilegeManager().promotePlayer(playerUUID, groupName);
    }

    public static void promotePlayer(EntityPlayer player, String groupName) {
        promotePlayer(CommonReference.getPersistentUUID(player), groupName);
    }

    public static void resetPlayerGroup(UUID playerUUID) {
        promotePlayer(playerUUID, PrivilegedGroup.DEFAULT_GROUP.getName());
    }

    public static void resetPlayerGroup(EntityPlayer player) {
        promotePlayer(CommonReference.getPersistentUUID(player), PrivilegedGroup.DEFAULT_GROUP.getName());
    }

    public static void addPrivilege(String groupName, IPrivilege privilege, boolean save) {
        OxygenManagerServer.instance().getPrivilegeManager().getGroup(groupName).addPrivilege(privilege, save);
    }

    public static void addPrivileges(String groupName, boolean save, IPrivilege... privileges) {
        OxygenManagerServer.instance().getPrivilegeManager().getGroup(groupName).addPrivileges(save, privileges);
    }

    public static void removePrivilege(String groupName, String privilegeName, boolean save) {
        OxygenManagerServer.instance().getPrivilegeManager().getGroup(groupName).removePrivilege(privilegeName, save);
    }

    public static boolean getPrivilegeValue(UUID playerUUID, String privilegeName, boolean defaultValue) {
        if (OxygenConfig.ENABLE_PRIVILEGES.getBooleanValue()) { 
            IPrivilegedGroup group = OxygenManagerServer.instance().getPrivilegeManager().getPlayerPrivilegedGroup(playerUUID);
            if (group.hasPrivilege(privilegeName))
                return group.getPrivilege(privilegeName).getValue() == 1 ? true : false;
        }
        return defaultValue;
    }

    public static int getPrivilegeValue(UUID playerUUID, String privilegeName, int defaultValue) {
        if (OxygenConfig.ENABLE_PRIVILEGES.getBooleanValue()) { 
            IPrivilegedGroup group = OxygenManagerServer.instance().getPrivilegeManager().getPlayerPrivilegedGroup(playerUUID);
            if (group.hasPrivilege(privilegeName))
                return group.getPrivilege(privilegeName).getValue();
        }
        return defaultValue;
    }

    public static void registerPrivilege(String privilegeName, String modName) {
        PrivilegeManagerServer.PRIVILEGES_REGISTRY.put(privilegeName, modName);
    }

    public static boolean privilegeExist(String privilegeName) {
        return PrivilegeManagerServer.PRIVILEGES_REGISTRY.containsKey(privilegeName);
    }
}
