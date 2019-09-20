package austeretony.oxygen_core.client.api;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.common.privilege.PrivilegedGroup;

public class PrivilegeProviderClient {

    public static PrivilegedGroup getPlayerGroup(){
        return OxygenManagerClient.instance().getPrivilegesManager().getPrivilegedGroup();
    }

    public static boolean getValue(String privilegeName, boolean defaultValue) {
        PrivilegedGroup group = OxygenManagerClient.instance().getPrivilegesManager().getPrivilegedGroup();
        if (group.hasPrivilege(privilegeName))
            return group.getPrivilege(privilegeName).getBooleanValue();
        return defaultValue;
    }

    public static int getValue(String privilegeName, int defaultValue) {
        PrivilegedGroup group = OxygenManagerClient.instance().getPrivilegesManager().getPrivilegedGroup();
        if (group.hasPrivilege(privilegeName))
            return group.getPrivilege(privilegeName).getIntValue();
        return defaultValue;
    }

    public static long getValue(String privilegeName, long defaultValue) {
        PrivilegedGroup group = OxygenManagerClient.instance().getPrivilegesManager().getPrivilegedGroup();
        if (group.hasPrivilege(privilegeName))
            return group.getPrivilege(privilegeName).getLongValue();
        return defaultValue;
    }

    public static float getValue(String privilegeName, float defaultValue) {
        PrivilegedGroup group = OxygenManagerClient.instance().getPrivilegesManager().getPrivilegedGroup();
        if (group.hasPrivilege(privilegeName))
            return group.getPrivilege(privilegeName).getFloatValue();
        return defaultValue;
    }

    public static String getValue(String privilegeName, String defaultValue) {
        PrivilegedGroup group = OxygenManagerClient.instance().getPrivilegesManager().getPrivilegedGroup();
        if (group.hasPrivilege(privilegeName))
            return group.getPrivilege(privilegeName).getStringValue();
        return defaultValue;
    }
}
