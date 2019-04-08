package austeretony.oxygen.common.config;

import java.util.Queue;

import austeretony.oxygen.common.api.config.AbstractConfigHolder;
import austeretony.oxygen.common.api.config.ConfigValue;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.reference.CommonReference;

public class OxygenConfig extends AbstractConfigHolder {

    public static final ConfigValue
    SYNC_CONFIGS = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "sync_configs"),
    SYNC_PRIVILEGES = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "sync_privileges"),
    ENABLE_CUSTOM_LOCALIZATION = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "enable_custom_localization"),
    ENABLE_TELEMETRY = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "enable_telemetry"),
    ENABLE_PRIVILEGES = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "enable_privileges");

    @Override
    public String getModId() {
        return OxygenMain.MODID;
    }

    @Override
    public String getExternalPath() {
        return CommonReference.getGameFolder() + "/config/oxygen/oxygen.json";
    }

    @Override
    public String getInternalPath() {
        return "assets/oxygen/oxygen.json";
    }

    @Override
    public void getValues(Queue<ConfigValue> values) {
        values.add(SYNC_CONFIGS);
        values.add(SYNC_PRIVILEGES);
        values.add(ENABLE_CUSTOM_LOCALIZATION);
        values.add(ENABLE_TELEMETRY);
        values.add(ENABLE_PRIVILEGES);
    }

    @Override
    public boolean sync() {
        return true;
    }
}
