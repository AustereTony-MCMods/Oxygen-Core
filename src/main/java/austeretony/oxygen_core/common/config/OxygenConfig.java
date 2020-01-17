package austeretony.oxygen_core.common.config;

import java.util.List;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.api.config.AbstractConfig;
import austeretony.oxygen_core.common.main.OxygenMain;

public class OxygenConfig extends AbstractConfig {

    public static final ConfigValue
    CLIENT_DATA_SAVE_PERIOD_SECONDS = ConfigValueUtils.getValue("client", "data_save_peroid_seconds", 60),
    ENABLE_NOTIFICATIONS_KEY = ConfigValueUtils.getValue("client", "enable_notifications_key", true),
    ENABLE_ACCEPT_KEY = ConfigValueUtils.getValue("client", "enable_accept_key", true),
    ENABLE_REJECT_KEY = ConfigValueUtils.getValue("client", "enable_reject_key", true),
    ENABLE_INTERACTION_KEY = ConfigValueUtils.getValue("client", "enable_interaction_key", true),
    DATE_FORMAT_PATTERN = ConfigValueUtils.getValue("client", "date_format_pattern", "d MM yyyy"),

    IO_THREADS_AMOUNT = ConfigValueUtils.getValue("server", "io_threads_amount", 2),
    ROUTINE_THREADS_AMOUNT = ConfigValueUtils.getValue("server", "routine_threads_amount", 2),
    SCHEDULER_THREADS_AMOUNT = ConfigValueUtils.getValue("server", "scheduler_threads_amount", 2),
    SERVER_DATA_SAVE_PERIOD_SECONDS = ConfigValueUtils.getValue("server", "data_save_period_seconds", 120),
    SYNC_CONFIGS = ConfigValueUtils.getValue("server", "sync_configs", true),
    ENABLE_PRIVILEGES = ConfigValueUtils.getValue("server", "enable_privileges", true, true),
    SYNC_ENTITIES_HEALTH = ConfigValueUtils.getValue("server", "sync_entities_health", true),
    SYNC_ENTITIES_ABSORPTION_VALUE = ConfigValueUtils.getValue("server", "sync_entities_absorption_value", true),
    SYNC_ENTITIES_ARMOR_VALUE = ConfigValueUtils.getValue("server", "sync_entities_armor_value", true),
    SYNC_ENTITIES_ACTIVE_EFFECTS = ConfigValueUtils.getValue("server", "sync_entities_active_effects", true);

    @Override
    public String getDomain() {
        return OxygenMain.MODID;
    }

    @Override
    public String getExternalPath() {
        return CommonReference.getGameFolder() + "/config/oxygen/oxygen.json";
    }

    @Override
    public void getValues(List<ConfigValue> values) {
        values.add(CLIENT_DATA_SAVE_PERIOD_SECONDS);
        values.add(ENABLE_NOTIFICATIONS_KEY);
        values.add(ENABLE_ACCEPT_KEY);
        values.add(ENABLE_REJECT_KEY);
        values.add(ENABLE_INTERACTION_KEY);
        values.add(DATE_FORMAT_PATTERN);

        values.add(IO_THREADS_AMOUNT);
        values.add(ROUTINE_THREADS_AMOUNT);
        values.add(SCHEDULER_THREADS_AMOUNT);
        values.add(SERVER_DATA_SAVE_PERIOD_SECONDS);
        values.add(SYNC_CONFIGS);
        values.add(ENABLE_PRIVILEGES);
        values.add(SYNC_ENTITIES_HEALTH);
        values.add(SYNC_ENTITIES_ARMOR_VALUE);
        values.add(SYNC_ENTITIES_ABSORPTION_VALUE);
        values.add(SYNC_ENTITIES_ACTIVE_EFFECTS);
    }
}
