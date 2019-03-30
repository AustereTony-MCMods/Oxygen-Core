package austeretony.oxygen.common.config;

import java.util.Queue;

import austeretony.oxygen.common.api.config.AbstractConfigHolder;
import austeretony.oxygen.common.api.config.ConfigValue;
import austeretony.oxygen.common.main.OxygenMain;

public class OxygenConfig extends AbstractConfigHolder {

    public static final ConfigValue
    SYNC_CONFIGS = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "sync_configs"),
    SYNC_PRIVILEGES = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "sync_privileges"),
    CUSTOM_LOCALIZATION = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "enable_custom_localization"),
    TELEMETRY = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "enable_telemetry"),
    PRIVILEGES = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "enable_privileges");

    @Override
    public String getModId() {
        return OxygenMain.MODID;
    }

    @Override
    public void getValues(Queue<ConfigValue> values) {
        values.add(SYNC_CONFIGS);
        values.add(SYNC_PRIVILEGES);
        values.add(CUSTOM_LOCALIZATION);
        values.add(TELEMETRY);
        values.add(PRIVILEGES);
    }

    @Override
    public boolean sync() {
        return true;
    }
}
