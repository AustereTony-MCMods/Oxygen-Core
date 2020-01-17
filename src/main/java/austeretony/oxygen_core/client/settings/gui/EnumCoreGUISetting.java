package austeretony.oxygen_core.client.settings.gui;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.common.EnumValueType;
import austeretony.oxygen_core.common.settings.SettingValue;
import austeretony.oxygen_core.common.settings.SettingValueUtils;

public enum EnumCoreGUISetting {

    //Alignment

    NOTIFICATIONS_MENU_ALIGNMENT("alignment_notifications_menu", EnumValueType.INT, String.valueOf(0)), 
    SETTINGS_MENU_ALIGNMENT("alignment_settings_menu", EnumValueType.INT, String.valueOf(0)),
    PRIVILEGES_MENU_ALIGNMENT("alignment_privileges_menu", EnumValueType.INT, String.valueOf(0)),

    //Offset

    REQUEST_OVERLAY_OFFSET("offset_request_overlay", EnumValueType.INT, String.valueOf(25));

    private final String key, baseValue;

    private final EnumValueType type;

    private SettingValue value;

    EnumCoreGUISetting(String key, EnumValueType type, String baseValue) {
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
        for (EnumCoreGUISetting setting : EnumCoreGUISetting.values())
            OxygenManagerClient.instance().getClientSettingManager().register(SettingValueUtils.getValue(setting.type, setting.key, setting.baseValue));
    }
}
