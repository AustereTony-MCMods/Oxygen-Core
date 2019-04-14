package austeretony.oxygen.common.privilege.api;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.privilege.IPrivilegedGroup;

public class PrivilegeProviderClient {

    public static IPrivilegedGroup getPlayerGroup(){
        return OxygenManagerClient.instance().getPrivilegeManager().getPrivilegedGroup();
    }

    public static boolean getPrivilegeValue(String privilegeName, boolean defaultValue) {
        if (OxygenConfig.ENABLE_PRIVILEGES.getBooleanValue() && OxygenManagerClient.instance().getPrivilegeManager().getPrivilegedGroup().hasPrivilege(privilegeName))
            return true;
        return defaultValue;
    }

    public static int getPrivilegeValue(String privilegeName, int defaultValue) {
        if (OxygenConfig.ENABLE_PRIVILEGES.getBooleanValue() && OxygenManagerClient.instance().getPrivilegeManager().getPrivilegedGroup().hasPrivilege(privilegeName))
            return OxygenManagerClient.instance().getPrivilegeManager().getPrivilegedGroup().getPrivilege(privilegeName).getValue();
        return defaultValue;
    }
}
