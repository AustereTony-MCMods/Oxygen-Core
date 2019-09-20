package austeretony.oxygen_core.server.config;

import java.util.List;

import austeretony.oxygen_core.common.EnumValueType;
import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.api.config.AbstractConfigHolder;
import austeretony.oxygen_core.common.api.config.ConfigValueImpl;
import austeretony.oxygen_core.common.config.ConfigValue;
import austeretony.oxygen_core.common.main.OxygenMain;

public class OxygenConfigServer extends AbstractConfigHolder {

    public static final ConfigValue
    IO_THREADS_AMOUNT = new ConfigValueImpl(EnumValueType.INT, "setup", "io_threads_amount"),
    NETWORK_THREADS_AMOUNT = new ConfigValueImpl(EnumValueType.INT, "setup", "network_threads_amount"),
    ROUTINE_THREADS_AMOUNT = new ConfigValueImpl(EnumValueType.INT, "setup", "routine_threads_amount"),
    SCHEDULER_THREADS_AMOUNT = new ConfigValueImpl(EnumValueType.INT, "setup", "scheduler_threads_amount"),
    SHARED_DATA_SAVE_DELAY_MINUTES = new ConfigValueImpl(EnumValueType.INT, "setup", "shared_data_save_delay_minutes"),
    PLAYERS_DATA_SAVE_DELAY_MINUTES = new ConfigValueImpl(EnumValueType.INT, "setup", "players_data_save_delay_minutes"),

    SYNC_CONFIGS = new ConfigValueImpl(EnumValueType.BOOLEAN, "main", "sync_configs"),
    ENABLE_PRIVILEGES = new ConfigValueImpl(EnumValueType.BOOLEAN, "main", "enable_privileges"),
    SYNC_PRIVILEGES = new ConfigValueImpl(EnumValueType.BOOLEAN, "main", "sync_privileges");

    @Override
    public String getDomain() {
        return OxygenMain.MODID + ":server";
    }

    @Override
    public String getVersion() {
        return OxygenMain.VERSION_CUSTOM;
    }

    @Override
    public String getExternalPath() {
        return CommonReference.getGameFolder() + "/config/oxygen/oxygen_server.json";
    }

    @Override
    public String getInternalPath() {
        return "assets/oxygen_core/oxygen_server.json";
    }

    @Override
    public void getValues(List<ConfigValue> values) {
        values.add(IO_THREADS_AMOUNT);
        values.add(NETWORK_THREADS_AMOUNT);
        values.add(ROUTINE_THREADS_AMOUNT);
        values.add(SCHEDULER_THREADS_AMOUNT);
        values.add(SHARED_DATA_SAVE_DELAY_MINUTES);
        values.add(PLAYERS_DATA_SAVE_DELAY_MINUTES);

        values.add(SYNC_CONFIGS);
        values.add(SYNC_PRIVILEGES);
        values.add(ENABLE_PRIVILEGES);
    }

    @Override
    public boolean sync() {
        return false;
    }
}