package austeretony.oxygen.common.privilege.api;

import java.util.UUID;

import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.privilege.IPrivilegedGroup;
import austeretony.oxygen.common.privilege.PrivilegeManagerServer;
import austeretony.oxygen.common.reference.CommonReference;
import net.minecraft.entity.player.EntityPlayer;

public class PrivilegeProviderServer {

    public static boolean groupExist(String groupName){
        return PrivilegeManagerServer.instance().groupExist(groupName);
    }

    public static IPrivilegedGroup getGroup(String groupName){
        return PrivilegeManagerServer.instance().getGroup(groupName);
    }

    public static IPrivilegedGroup getDefaultGroup(){
        return getGroup(PrivilegedGroup.DEFAULT_GROUP.getName());
    }

    public static IPrivilegedGroup getPlayerGroup(UUID playerUUID){
        return PrivilegeManagerServer.instance().getPlayerPrivilegedGroup(playerUUID);
    }

    public static void addGroup(IPrivilegedGroup group, boolean save) {
        PrivilegeManagerServer.instance().addGroup(group, save);
    }

    public static void removeGroup(String groupName) {
        PrivilegeManagerServer.instance().removeGroup(groupName);
    }

    public static void promotePlayer(UUID playerUUID, String groupName) {
        PrivilegeManagerServer.instance().promotePlayer(playerUUID, groupName);
    }

    public static void promotePlayer(EntityPlayer player, String groupName) {
        promotePlayer(CommonReference.uuid(player), groupName);
    }

    public static void resetPlayerGroup(UUID playerUUID) {
        promotePlayer(playerUUID, PrivilegedGroup.DEFAULT_GROUP.getName());
    }

    public static void resetPlayerGroup(EntityPlayer player) {
        promotePlayer(CommonReference.uuid(player), PrivilegedGroup.DEFAULT_GROUP.getName());
    }

    public static void addPrivilege(String groupName, String privilegeName, boolean save) {
        PrivilegeManagerServer.instance().getGroup(groupName).addPrivilege(new Privilege(privilegeName), save);
    }

    public static void addPrivilege(String groupName, String privilegeName, int value, boolean save) {
        PrivilegeManagerServer.instance().getGroup(groupName).addPrivilege(new Privilege(privilegeName, value), save);
    }

    public static void removePrivilege(String groupName, String privilegeName, boolean save) {
        PrivilegeManagerServer.instance().getGroup(groupName).removePrivilege(privilegeName, save);
    }

    public static boolean getPrivilegeValue(UUID playerUUID, String privilegeName, boolean defaultValue) {
        if (OxygenConfig.ENABLE_PRIVILEGES.getBooleanValue() && PrivilegeManagerServer.instance().getPlayerPrivilegedGroup(playerUUID).hasPrivilege(privilegeName))
            return true;
        return defaultValue;
    }

    public static int getPrivilegeValue(UUID playerUUID, String privilegeName, int defaultValue) {
        if (OxygenConfig.ENABLE_PRIVILEGES.getBooleanValue() && PrivilegeManagerServer.instance().getPlayerPrivilegedGroup(playerUUID).hasPrivilege(privilegeName))
            return PrivilegeManagerServer.instance().getPlayerPrivilegedGroup(playerUUID).getPrivilege(privilegeName).getValue();
        return defaultValue;
    }

    public static void registerPrivilege(String privilegeName, String modName) {
        PrivilegeManagerServer.PRIVILEGES_REGISTRY.put(privilegeName, modName);
    }

    public static boolean privilegeExist(String privilegeName) {
        return PrivilegeManagerServer.PRIVILEGES_REGISTRY.containsKey(privilegeName);
    }
}
