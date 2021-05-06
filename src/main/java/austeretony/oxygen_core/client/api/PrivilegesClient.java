package austeretony.oxygen_core.client.api;

import austeretony.oxygen_core.client.OxygenManagerClient;

public class PrivilegesClient {

    public static boolean getBoolean(int privilegeId, boolean defaultValue) {
        return OxygenManagerClient.instance().getPrivilegesManager().getBoolean(OxygenClient.getClientPlayerUUID(),
                privilegeId, defaultValue);
    }

    public static int getInt(int privilegeId, int defaultValue) {
        return OxygenManagerClient.instance().getPrivilegesManager().getInt(OxygenClient.getClientPlayerUUID(),
                privilegeId, defaultValue);
    }

    public static long getLong(int privilegeId, long defaultValue) {
        return OxygenManagerClient.instance().getPrivilegesManager().getLong(OxygenClient.getClientPlayerUUID(),
                privilegeId, defaultValue);
    }

    public static float getFloat(int privilegeId, float defaultValue) {
        return OxygenManagerClient.instance().getPrivilegesManager().getFloat(OxygenClient.getClientPlayerUUID(),
                privilegeId, defaultValue);
    }

    public static double getDouble(int privilegeId, double defaultValue) {
        return OxygenManagerClient.instance().getPrivilegesManager().getDouble(OxygenClient.getClientPlayerUUID(),
                privilegeId, defaultValue);
    }

    public static String getString(int privilegeId, String defaultValue) {
        return OxygenManagerClient.instance().getPrivilegesManager().getString(OxygenClient.getClientPlayerUUID(),
                privilegeId, defaultValue);
    }
}
