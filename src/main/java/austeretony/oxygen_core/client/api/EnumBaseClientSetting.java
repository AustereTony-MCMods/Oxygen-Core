package austeretony.oxygen_core.client.api;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.common.EnumValueType;
import austeretony.oxygen_core.common.settings.SettingValue;
import austeretony.oxygen_core.common.settings.SettingValueUtils;

public enum EnumBaseClientSetting {

    //Misc

    INTERACT_WITH_RMB("misc_interact_with_rmb", EnumValueType.BOOLEAN, String.valueOf(false)),
    ENABLE_SOUND_EFFECTS("misc_enable_sound_effects", EnumValueType.BOOLEAN, String.valueOf(true)),
    ENABLE_RARITY_COLORS("misc_enable_rarity_colors", EnumValueType.BOOLEAN, String.valueOf(true)),
    ENABLE_ITEMS_DURABILITY_BAR("misc_enable_items_durability_bar", EnumValueType.BOOLEAN, String.valueOf(true)),
    ENABLE_STATUS_MESSAGES("misc_enable_status_messages", EnumValueType.BOOLEAN, String.valueOf(true));

    private final String key, baseValue;

    private final EnumValueType type;

    private SettingValue value;

    EnumBaseClientSetting(String key, EnumValueType type, String baseValue) {
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
        for (EnumBaseClientSetting setting : values())
            OxygenManagerClient.instance().getClientSettingManager().register(SettingValueUtils.getValue(setting.type, setting.key, setting.baseValue));
    }
}
