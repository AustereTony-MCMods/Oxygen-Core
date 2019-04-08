package austeretony.oxygen.common.privilege.api;

import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.privilege.IPrivilegedGroup;
import austeretony.oxygen.common.privilege.PrivilegeManagerClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PrivilegeProviderClient {

    public static IPrivilegedGroup getPlayerGroup(){
        return PrivilegeManagerClient.instance().getPrivilegedGroup();
    }

    public static boolean getPrivilegeValue(String privilegeName, boolean defaultValue) {
        if (OxygenConfig.ENABLE_PRIVILEGES.getBooleanValue() && PrivilegeManagerClient.instance().getPrivilegedGroup().hasPrivilege(privilegeName))
            return true;
        return defaultValue;
    }

    public static int getPrivilegeValue(String privilegeName, int defaultValue) {
        if (OxygenConfig.ENABLE_PRIVILEGES.getBooleanValue() && PrivilegeManagerClient.instance().getPrivilegedGroup().hasPrivilege(privilegeName))
            return PrivilegeManagerClient.instance().getPrivilegedGroup().getPrivilege(privilegeName).getValue();
        return defaultValue;
    }
}
