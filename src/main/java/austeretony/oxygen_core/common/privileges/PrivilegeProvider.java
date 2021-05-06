package austeretony.oxygen_core.common.privileges;

import java.util.UUID;

public interface PrivilegeProvider {

    boolean getBoolean(UUID playerUUID, int privilegeId, boolean defaultValue);

    int getInt(UUID playerUUID, int privilegeId, int defaultValue);

    long getLong(UUID playerUUID, int privilegeId, long defaultValue);

    float getFloat(UUID playerUUID, int privilegeId, float defaultValue);

    double getDouble(UUID playerUUID, int privilegeId, double defaultValue);

    String getString(UUID playerUUID, int privilegeId, String defaultValue);
}
