package austeretony.oxygen_core.client.settings;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.common.EnumValueType;
import austeretony.oxygen_core.common.settings.SettingValue;
import austeretony.oxygen_core.common.settings.SettingValueUtils;

public enum EnumCoreClientSetting {

    //Misc

    HIDE_REQUESTS_OVERLAY("misc_hide_requests_overlay", EnumValueType.BOOLEAN, String.valueOf(false)),

    //Oxygen Menu

    ADD_NOTIFICATIONS_MENU("menu_add_notifications", EnumValueType.BOOLEAN, String.valueOf(true)),
    ADD_SETTINGS_MENU("menu_add_settings", EnumValueType.BOOLEAN, String.valueOf(true)),
    ADD_PRIVILEGES_MENU("menu_add_privileges", EnumValueType.BOOLEAN, String.valueOf(false));

    private final String key, baseValue;

    private final EnumValueType type;

    private SettingValue value;

    EnumCoreClientSetting(String key, EnumValueType type, String baseValue) {
        this.key = key;
        this.type = type;
        this.baseValue = baseValue;
    }

    public SettingValue get() {
        if (this.value == null)
            this.value = OxygenManagerClient.instance().getClientSettingManager().getSettingValue(this.key);
        return this.value;
    }

    public static void register() {
        for (EnumCoreClientSetting setting : values())
            OxygenManagerClient.instance().getClientSettingManager().register(SettingValueUtils.getValue(setting.type, setting.key, setting.baseValue));
    }
}
