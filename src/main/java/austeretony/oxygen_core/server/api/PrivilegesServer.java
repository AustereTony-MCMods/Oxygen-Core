package austeretony.oxygen_core.server.api;

import austeretony.oxygen_core.server.OxygenManagerServer;

import java.util.UUID;

public class PrivilegesServer {

    public static boolean getBoolean(UUID playerUUID, int privilegeId, boolean defaultValue) {
        return OxygenManagerServer.instance().getPrivilegesManager().getBoolean(playerUUID, privilegeId, defaultValue);
    }

    public static int getInt(UUID playerUUID, int privilegeId, int defaultValue) {
        return OxygenManagerServer.instance().getPrivilegesManager().getInt(playerUUID, privilegeId, defaultValue);
    }

    public static long getLong(UUID playerUUID, int privilegeId, long defaultValue) {
        return OxygenManagerServer.instance().getPrivilegesManager().getLong(playerUUID, privilegeId, defaultValue);
    }

    public static float getFloat(UUID playerUUID, int privilegeId, float defaultValue) {
        return OxygenManagerServer.instance().getPrivilegesManager().getFloat(playerUUID, privilegeId, defaultValue);
    }

    public static double getDouble(UUID playerUUID, int privilegeId, double defaultValue) {
        return OxygenManagerServer.instance().getPrivilegesManager().getDouble(playerUUID, privilegeId, defaultValue);
    }

    public static String getString(UUID playerUUID, int privilegeId, String defaultValue) {
        return OxygenManagerServer.instance().getPrivilegesManager().getString(playerUUID, privilegeId, defaultValue);
    }
}
