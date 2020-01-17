package austeretony.oxygen_core.server.api;

import java.util.Set;
import java.util.UUID;

import austeretony.oxygen_core.common.config.OxygenConfig;
import austeretony.oxygen_core.common.privilege.Privilege;
import austeretony.oxygen_core.common.privilege.Role;
import austeretony.oxygen_core.common.value.TypedValueBoolean;
import austeretony.oxygen_core.common.value.TypedValueFloat;
import austeretony.oxygen_core.common.value.TypedValueInteger;
import austeretony.oxygen_core.common.value.TypedValueLong;
import austeretony.oxygen_core.common.value.TypedValueString;
import austeretony.oxygen_core.server.OxygenManagerServer;

public class PrivilegesProviderServer {

    public static Role getRole(int roleId) {
        return OxygenManagerServer.instance().getPrivilegesManager().getRole(roleId);
    }

    public static Set<Integer> getPlayerRolesIds(UUID playerUUID) {
        return OxygenManagerServer.instance().getPrivilegesManager().getPlayerRolesIds(playerUUID);
    }

    public static Role getChatFormattingPlayerRole(UUID playerUUID) {
        return OxygenManagerServer.instance().getPrivilegesManager().getChatFormattingPlayerRole(playerUUID);
    }

    public static Role getPriorityPlayerRole(UUID playerUUID) {
        return OxygenManagerServer.instance().getPrivilegesManager().getPriorityPlayerRole(playerUUID);
    }

    public static Privilege getPriorityPlayerPrivilege(UUID playerUUID, int privilegeId) {
        return OxygenManagerServer.instance().getPrivilegesManager().getPriorityPlayerPrivilege(playerUUID, privilegeId);
    }

    public static boolean getAsBoolean(UUID playerUUID, int privilegeId, boolean defaultValue) {
        if (OxygenConfig.ENABLE_PRIVILEGES.asBoolean()) { 
            Privilege<TypedValueBoolean> privilege;
            if ((privilege = getPriorityPlayerPrivilege(playerUUID, privilegeId)) != null)
                return privilege.get().getValue();
        }
        return defaultValue;
    }

    public static int getAsInt(UUID playerUUID, int privilegeId, int defaultValue) {
        if (OxygenConfig.ENABLE_PRIVILEGES.asBoolean()) { 
            Privilege<TypedValueInteger> privilege;
            if ((privilege = getPriorityPlayerPrivilege(playerUUID, privilegeId)) != null)
                return privilege.get().getValue();
        }
        return defaultValue;
    }

    public static long getAsLong(UUID playerUUID, int privilegeId, long defaultValue) {
        if (OxygenConfig.ENABLE_PRIVILEGES.asBoolean()) { 
            Privilege<TypedValueLong> privilege;
            if ((privilege = getPriorityPlayerPrivilege(playerUUID, privilegeId)) != null)
                return privilege.get().getValue();
        }
        return defaultValue;
    }

    public static float getAsFloat(UUID playerUUID, int privilegeId, float defaultValue) {
        if (OxygenConfig.ENABLE_PRIVILEGES.asBoolean()) { 
            Privilege<TypedValueFloat> privilege;
            if ((privilege = getPriorityPlayerPrivilege(playerUUID, privilegeId)) != null)
                return privilege.get().getValue();
        }
        return defaultValue;
    }

    public static String getAsString(UUID playerUUID, int privilegeId, String defaultValue) {
        if (OxygenConfig.ENABLE_PRIVILEGES.asBoolean()) { 
            Privilege<TypedValueString> privilege;
            if ((privilege = getPriorityPlayerPrivilege(playerUUID, privilegeId)) != null)
                return privilege.get().getValue();
        }
        return defaultValue;
    }
}
