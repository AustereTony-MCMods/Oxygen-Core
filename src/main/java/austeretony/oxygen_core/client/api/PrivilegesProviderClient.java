package austeretony.oxygen_core.client.api;

import java.util.Set;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.privilege.RoleDataClient;
import austeretony.oxygen_core.common.privilege.Privilege;
import austeretony.oxygen_core.common.privilege.Role;
import austeretony.oxygen_core.common.value.TypedValueBoolean;
import austeretony.oxygen_core.common.value.TypedValueFloat;
import austeretony.oxygen_core.common.value.TypedValueInteger;
import austeretony.oxygen_core.common.value.TypedValueLong;
import austeretony.oxygen_core.common.value.TypedValueString;

public class PrivilegesProviderClient {

    public static RoleDataClient getRoleData(int roleId) {
        return OxygenManagerClient.instance().getPrivilegesManager().getRoleData(roleId);
    }

    public static Set<Integer> getPlayerRolesIds() {
        return OxygenManagerClient.instance().getPrivilegesManager().getPlayerRolesIds();
    }

    public static Role getPriorityPlayerRole() {
        return OxygenManagerClient.instance().getPrivilegesManager().getPriorityPlayerRole();
    }

    public static Privilege getPriorityPlayerPrivilege(int privilegeId) {
        return OxygenManagerClient.instance().getPrivilegesManager().getPriorityPlayerPrivilege(privilegeId);
    }

    public static boolean getAsBoolean(int privilegeId, boolean defaultValue) {
        Privilege<TypedValueBoolean> privilege;
        if ((privilege = getPriorityPlayerPrivilege(privilegeId)) != null)
            return privilege.get().getValue();
        return defaultValue;
    }

    public static int getAsInt(int privilegeId, int defaultValue) {
        Privilege<TypedValueInteger> privilege;
        if ((privilege = getPriorityPlayerPrivilege(privilegeId)) != null)
            return privilege.get().getValue();
        return defaultValue;
    }

    public static long getAsLong(int privilegeId, long defaultValue) {
        Privilege<TypedValueLong> privilege;
        if ((privilege = getPriorityPlayerPrivilege(privilegeId)) != null)
            return privilege.get().getValue();
        return defaultValue;
    }

    public static float getAsFloat(int privilegeId, float defaultValue) {
        Privilege<TypedValueFloat> privilege;
        if ((privilege = getPriorityPlayerPrivilege(privilegeId)) != null)
            return privilege.get().getValue();
        return defaultValue;
    }

    public static String getAsString(int privilegeId, String defaultValue) {
        Privilege<TypedValueString> privilege;
        if ((privilege = getPriorityPlayerPrivilege(privilegeId)) != null)
            return privilege.get().getValue();
        return defaultValue;
    }
}
