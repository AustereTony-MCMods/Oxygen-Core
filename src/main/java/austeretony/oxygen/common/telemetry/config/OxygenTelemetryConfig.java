package austeretony.oxygen.common.telemetry.config;

import java.util.Queue;

import austeretony.oxygen.common.api.config.AbstractConfigHolder;
import austeretony.oxygen.common.api.config.ConfigValue;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.reference.CommonReference;

public class OxygenTelemetryConfig extends AbstractConfigHolder {

    public static final ConfigValue
    COLLECT_DATA = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "collect_data"),
    LOG_FILE_CHANGING_INTERVAL = new ConfigValue(ConfigValue.EnumValueType.INT, "main", "log_file_changing_interval_hours"),
    CACHE_FILE_VOLUME = new ConfigValue(ConfigValue.EnumValueType.INT, "main", "cache_file_volume_seconds"),
    CACHE_FILES_AMOUNT = new ConfigValue(ConfigValue.EnumValueType.INT, "main", "cache_files_amount"),
    RECORDING_INTERVAL = new ConfigValue(ConfigValue.EnumValueType.INT, "main", "recording_interval_seconds");

    @Override
    public String getModId() {
        return OxygenMain.MODID + ":telemetry";
    }

    @Override
    public String getExternalPath() {
        return CommonReference.getGameFolder() + "/config/oxygen/telemetry/telemetry.json";
    }

    @Override
    public String getInternalPath() {
        return "assets/oxygen/telemetry.json";
    }

    @Override
    public void getValues(Queue<ConfigValue> values) {
        values.add(COLLECT_DATA);        
        values.add(LOG_FILE_CHANGING_INTERVAL);        
        values.add(CACHE_FILE_VOLUME);        
        values.add(CACHE_FILES_AMOUNT);
        values.add(RECORDING_INTERVAL);
    }

    @Override
    public boolean sync() {
        return false;
    }
}
