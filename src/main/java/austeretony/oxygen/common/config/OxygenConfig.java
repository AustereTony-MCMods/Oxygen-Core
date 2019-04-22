package austeretony.oxygen.common.config;

import java.util.Queue;

import austeretony.oxygen.common.api.config.AbstractConfigHolder;
import austeretony.oxygen.common.api.config.ConfigValue;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.OxygenMain;

public class OxygenConfig extends AbstractConfigHolder {

    public static final ConfigValue
    SYNC_CONFIGS = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "sync_configs"),
    SYNC_PRIVILEGES = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "sync_privileges"),
    ENABLE_TELEMETRY = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "enable_telemetry"),
    ENABLE_PRIVILEGES = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "enable_privileges"),
    ENABLE_CURRENCY = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "enable_currency"),
    REPLACE_TAB_OVERLAY = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "replace_tab_overlay"),

    ENABLE_FRIENDS_LIST = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "friends", "enable_friends"),
    FRIEND_REQUEST_EXPIRE_TIME = new ConfigValue(ConfigValue.EnumValueType.INT, "friends", "friend_request_expire_time_seconds"),
    MAX_FRIENDS = new ConfigValue(ConfigValue.EnumValueType.INT, "friends", "max_friends"),
    MAX_IGNORED = new ConfigValue(ConfigValue.EnumValueType.INT, "friends", "max_ignored");

    @Override
    public String getModId() {
        return OxygenMain.MODID;
    }

    @Override
    public String getVersion() {
        return OxygenMain.VERSION_CUSTOM;
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
        values.add(ENABLE_TELEMETRY);
        values.add(ENABLE_PRIVILEGES);
        values.add(ENABLE_CURRENCY);
        values.add(REPLACE_TAB_OVERLAY);

        values.add(ENABLE_FRIENDS_LIST);
        values.add(FRIEND_REQUEST_EXPIRE_TIME);
        values.add(MAX_FRIENDS);
        values.add(MAX_IGNORED);
    }

    @Override
    public boolean sync() {
        return true;
    }
}
