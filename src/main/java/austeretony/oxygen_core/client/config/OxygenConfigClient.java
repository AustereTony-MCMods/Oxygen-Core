package austeretony.oxygen_core.client.config;

import java.util.List;

import austeretony.oxygen_core.common.EnumValueType;
import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.api.config.AbstractConfigHolder;
import austeretony.oxygen_core.common.api.config.ConfigValueImpl;
import austeretony.oxygen_core.common.config.ConfigValue;
import austeretony.oxygen_core.common.main.OxygenMain;

public class OxygenConfigClient extends AbstractConfigHolder {

    public static final ConfigValue 
    IO_THREADS_AMOUNT = new ConfigValueImpl(EnumValueType.INT, "setup", "io_threads_amount"),
    NETWORK_THREADS_AMOUNT = new ConfigValueImpl(EnumValueType.INT, "setup", "network_threads_amount"),
    ROUTINE_THREADS_AMOUNT = new ConfigValueImpl(EnumValueType.INT, "setup", "routine_threads_amount"),
    SCHEDULER_THREADS_AMOUNT = new ConfigValueImpl(EnumValueType.INT, "setup", "scheduler_threads_amount"),
    CLIENT_SETTINGS_SAVE_DELAY_MINUTES = new ConfigValueImpl(EnumValueType.INT, "setup", "client_settings_save_delay_minutes"),

    INTERACT_WITH_RMB = new ConfigValueImpl(EnumValueType.BOOLEAN, "main", "interact_with_rmb");

    @Override
    public String getDomain() {
        return OxygenMain.MODID + ":client";
    }

    @Override
    public String getVersion() {
        return OxygenMain.VERSION_CUSTOM;
    }

    @Override
    public String getExternalPath() {
        return CommonReference.getGameFolder() + "/config/oxygen/oxygen_client.json";
    }

    @Override
    public String getInternalPath() {
        return "assets/oxygen_core/oxygen_client.json";
    }

    @Override
    public void getValues(List<ConfigValue> values) {
        values.add(IO_THREADS_AMOUNT);
        values.add(NETWORK_THREADS_AMOUNT);
        values.add(ROUTINE_THREADS_AMOUNT);
        values.add(SCHEDULER_THREADS_AMOUNT);
        values.add(CLIENT_SETTINGS_SAVE_DELAY_MINUTES);

        values.add(INTERACT_WITH_RMB);
    }

    @Override
    public boolean sync() {
        return false;
    }
}
