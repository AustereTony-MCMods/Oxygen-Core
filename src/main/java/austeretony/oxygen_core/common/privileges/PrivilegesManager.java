package austeretony.oxygen_core.common.privileges;

import javax.annotation.Nullable;
import java.util.UUID;

public class PrivilegesManager {

    private @Nullable PrivilegeProvider provider;

    public void registerProvider(PrivilegeProvider provider) {
        this.provider = provider;
    }

    public boolean getBoolean(UUID playerUUID, int privilegeId, boolean defaultValue) {
        if (provider != null) {
            return provider.getBoolean(playerUUID, privilegeId, defaultValue);
        }
        return defaultValue;
    }

    public int getInt(UUID playerUUID, int privilegeId, int defaultValue) {
        if (provider != null) {
            return provider.getInt(playerUUID, privilegeId, defaultValue);
        }
        return defaultValue;
    }

    public long getLong(UUID playerUUID, int privilegeId, long defaultValue) {
        if (provider != null) {
            return provider.getLong(playerUUID, privilegeId, defaultValue);
        }
        return defaultValue;
    }

    public float getFloat(UUID playerUUID, int privilegeId, float defaultValue) {
        if (provider != null) {
            return provider.getFloat(playerUUID, privilegeId, defaultValue);
        }
        return defaultValue;
    }

    public double getDouble(UUID playerUUID, int privilegeId, double defaultValue) {
        if (provider != null) {
            return provider.getDouble(playerUUID, privilegeId, defaultValue);
        }
        return defaultValue;
    }

    public String getString(UUID playerUUID, int privilegeId, String defaultValue) {
        if (provider != null) {
            return provider.getString(playerUUID, privilegeId, defaultValue);
        }
        return defaultValue;
    }
}
