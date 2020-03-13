package austeretony.oxygen_core.common.config;

import java.util.List;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.api.config.AbstractConfig;
import austeretony.oxygen_core.common.main.OxygenMain;

public class OxygenConfig extends AbstractConfig {

    public static final ConfigValue
    CLIENT_REGION_ID = ConfigValueUtils.getValue("client", "region_id", ""),
    CLIENT_DATA_SAVE_PERIOD_SECONDS = ConfigValueUtils.getValue("client", "data_save_peroid_seconds", 60),
    ENABLE_NOTIFICATIONS_KEY = ConfigValueUtils.getValue("client", "enable_notifications_key", true),
    ENABLE_ACCEPT_KEY = ConfigValueUtils.getValue("client", "enable_accept_key", true),
    ENABLE_REJECT_KEY = ConfigValueUtils.getValue("client", "enable_reject_key", true),
    ENABLE_INTERACTION_KEY = ConfigValueUtils.getValue("client", "enable_interaction_key", true),
    NOTIFICATIONS_MENU_KEY = ConfigValueUtils.getValue("client", "notifications_menu_key", 49),
    SETTINGS_MENU_KEY = ConfigValueUtils.getValue("client", "settings_menu_key", 30),
    PRIVILEGES_MENU_KEY = ConfigValueUtils.getValue("client", "privileges_menu_key", 19),
    ACCEPT_KEY = ConfigValueUtils.getValue("client", "accept_key", 19),
    REJECT_KEY = ConfigValueUtils.getValue("client", "reject_key", 45),
    INTERACTION_KEY = ConfigValueUtils.getValue("client", "interaction_key", 33),
    DATE_TIME_FORMATTER_PATTERN = ConfigValueUtils.getValue("client", "date_time_formatter_pattern", "d MM yyyy"),

    SERVER_REGION_ID = ConfigValueUtils.getValue("server", "region_id", ""),
    IO_THREADS_AMOUNT = ConfigValueUtils.getValue("server", "io_threads_amount", 2),
    ROUTINE_THREADS_AMOUNT = ConfigValueUtils.getValue("server", "routine_threads_amount", 2),
    SCHEDULER_THREADS_AMOUNT = ConfigValueUtils.getValue("server", "scheduler_threads_amount", 2),
    SERVER_DATA_SAVE_PERIOD_SECONDS = ConfigValueUtils.getValue("server", "data_save_period_seconds", 120),
    SYNC_CONFIGS = ConfigValueUtils.getValue("server", "sync_configs", true),
    ENABLE_PRIVILEGES = ConfigValueUtils.getValue("server", "enable_privileges", true, true),
    ENABLE_PVP_MANAGER = ConfigValueUtils.getValue("server", "enable_pvp_manager", false),
    SYNC_ENTITIES_HEALTH = ConfigValueUtils.getValue("server", "sync_entities_health", true),
    SYNC_ENTITIES_ABSORPTION_VALUE = ConfigValueUtils.getValue("server", "sync_entities_absorption_value", true),
    SYNC_ENTITIES_ARMOR_VALUE = ConfigValueUtils.getValue("server", "sync_entities_armor_value", true),
    SYNC_ENTITIES_ACTIVE_EFFECTS = ConfigValueUtils.getValue("server", "sync_entities_active_effects", true),
    ENABLE_ECMASCRIPT_ADAPTER = ConfigValueUtils.getValue("server", "enable_ecmascript_adapter", false),
    ADVANCED_LOGGING = ConfigValueUtils.getValue("server", "advanced_logging", true);

    @Override
    public String getDomain() {
        return OxygenMain.MODID;
    }

    @Override
    public String getVersion() {
        return OxygenMain.VERSION_CUSTOM;
    }

    @Override
    public String getExternalPath() {
        return CommonReference.getGameFolder() + "/config/oxygen/core.json";
    }

    @Override
    public void getValues(List<ConfigValue> values) {
        values.add(CLIENT_REGION_ID);
        values.add(CLIENT_DATA_SAVE_PERIOD_SECONDS);
        values.add(ENABLE_NOTIFICATIONS_KEY);
        values.add(ENABLE_ACCEPT_KEY);
        values.add(ENABLE_REJECT_KEY);
        values.add(ENABLE_INTERACTION_KEY);
        values.add(NOTIFICATIONS_MENU_KEY);
        values.add(SETTINGS_MENU_KEY);
        values.add(PRIVILEGES_MENU_KEY);
        values.add(ACCEPT_KEY);
        values.add(REJECT_KEY);
        values.add(INTERACTION_KEY);
        values.add(DATE_TIME_FORMATTER_PATTERN);

        values.add(SERVER_REGION_ID);
        values.add(IO_THREADS_AMOUNT);
        values.add(ROUTINE_THREADS_AMOUNT);
        values.add(SCHEDULER_THREADS_AMOUNT);
        values.add(SERVER_DATA_SAVE_PERIOD_SECONDS);
        values.add(SYNC_CONFIGS);
        values.add(ENABLE_PRIVILEGES);
        values.add(ENABLE_PVP_MANAGER);
        values.add(SYNC_ENTITIES_HEALTH);
        values.add(SYNC_ENTITIES_ARMOR_VALUE);
        values.add(SYNC_ENTITIES_ABSORPTION_VALUE);
        values.add(SYNC_ENTITIES_ACTIVE_EFFECTS);
        values.add(ENABLE_ECMASCRIPT_ADAPTER);
        values.add(ADVANCED_LOGGING);
    }
}
