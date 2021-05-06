package austeretony.oxygen_core.common.config;

import austeretony.oxygen_core.common.main.OxygenMain;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class CoreConfig extends AbstractConfig {

    public static final ConfigValue
    //common
            LOGGING_LEVEL = ConfigValueUtils.getInt("common", "logging_level", 1),
            PERSISTENT_DATA_SAVE_PERIOD_SECONDS = ConfigValueUtils.getInt("server", "persistent_data_save_period_seconds", 120),
    //client
            CLIENT_REGION_ID = ConfigValueUtils.getString("client", "region_id", ""),
            DATE_TIME_FORMATTER_PATTERN = ConfigValueUtils.getString("client", "date_time_formatter_pattern",
                    "d MM yyyy HH:mm"),
            SETTINGS_SCREEN_KEY_ID = ConfigValueUtils.getInt("client", "settings_screen_key_id", Keyboard.KEY_S),
    //server
            SERVER_REGION_ID = ConfigValueUtils.getString("server", "region_id", ""),
            ENABLE_PVP_MANAGER = ConfigValueUtils.getBoolean("server", "enable_pvp_manager", false),
            CURRENCY_DB_THREADS = ConfigValueUtils.getInt("server", "currency_db_threads", 2);

    @Override
    public String getDomain() {
        return OxygenMain.MOD_ID;
    }

    @Override
    public String getVersion() {
        return OxygenMain.VERSION_CUSTOM;
    }

    @Override
    public String getFileName() {
        return "core.json";
    }

    @Override
    public void getValues(List<ConfigValue> values) {
        values.add(LOGGING_LEVEL);
        values.add(PERSISTENT_DATA_SAVE_PERIOD_SECONDS);

        values.add(CLIENT_REGION_ID);
        values.add(DATE_TIME_FORMATTER_PATTERN);
        values.add(SETTINGS_SCREEN_KEY_ID);

        values.add(SERVER_REGION_ID);
        values.add(ENABLE_PVP_MANAGER);
        values.add(CURRENCY_DB_THREADS);
    }
}
