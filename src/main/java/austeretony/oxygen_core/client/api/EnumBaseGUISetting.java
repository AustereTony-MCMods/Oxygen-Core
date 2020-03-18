package austeretony.oxygen_core.client.api;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.common.EnumValueType;
import austeretony.oxygen_core.common.settings.SettingValue;
import austeretony.oxygen_core.common.settings.SettingValueUtils;

public enum EnumBaseGUISetting {

    //Color

    FILL_SCREEN_COLOR("color_fill_screen", EnumValueType.HEX, Integer.toHexString(0x00000000)),
    FILL_CALLBACK_COLOR("color_fill_callback", EnumValueType.HEX, Integer.toHexString(0x68101010)),

    BACKGROUND_BASE_COLOR("color_background_base", EnumValueType.HEX, Integer.toHexString(0xdc101010)),
    BACKGROUND_ADDITIONAL_COLOR("color_background_additional", EnumValueType.HEX, Integer.toHexString(0xff404040)),

    BUTTON_ENABLED_COLOR("color_button_enabled", EnumValueType.HEX, Integer.toHexString(0xff404040)),
    BUTTON_DISABLED_COLOR("color_button_disabled", EnumValueType.HEX, Integer.toHexString(0xff202020)),
    BUTTON_HOVERED_COLOR("color_button_hovered", EnumValueType.HEX, Integer.toHexString(0xff606060)),

    ELEMENT_ENABLED_COLOR("color_element_enabled", EnumValueType.HEX, Integer.toHexString(0x96080808)),
    ELEMENT_DISABLED_COLOR("color_element_disabled", EnumValueType.HEX, Integer.toHexString(0x96040404)),
    ELEMENT_HOVERED_COLOR("color_element_hovered", EnumValueType.HEX, Integer.toHexString(0x96303030)),

    SLIDER_ENABLED_COLOR("color_slider_enabled", EnumValueType.HEX, Integer.toHexString(0xff303030)),
    SLIDER_DISABLED_COLOR("color_slider_disabled", EnumValueType.HEX, Integer.toHexString(0xff202020)),
    SLIDER_HOVERED_COLOR("color_slider_hovered", EnumValueType.HEX, Integer.toHexString(0xff454545)),

    TEXT_ENABLED_COLOR("color_text_enabled", EnumValueType.HEX, Integer.toHexString(0xffcccccc)),
    TEXT_DISABLED_COLOR("color_text_disabled", EnumValueType.HEX, Integer.toHexString(0xff999999)),
    TEXT_HOVERED_COLOR("color_text_hovered", EnumValueType.HEX, Integer.toHexString(0xffe5e5e5)),

    TEXT_DARK_ENABLED_COLOR("color_text_dark_enabled", EnumValueType.HEX, Integer.toHexString(0xffa5a5a5)),
    TEXT_DARK_DISABLED_COLOR("color_text_dark_disabled", EnumValueType.HEX, Integer.toHexString(0xff7f7f7f)),
    TEXT_DARK_HOVERED_COLOR("color_text_dark_hovered", EnumValueType.HEX, Integer.toHexString(0xffbfbfbf)),

    TEXTFIELD_ENABLED_COLOR("color_textfield_enabled", EnumValueType.HEX, Integer.toHexString(0x96454545)),
    TEXTFIELD_DISABLED_COLOR("color_textfield_disabled", EnumValueType.HEX, Integer.toHexString(0x96353535)),
    TEXTFIELD_HOVERED_COLOR("color_textfield_hovered", EnumValueType.HEX, Integer.toHexString(0x96525252)),

    OVERLAY_TEXT_BASE_COLOR("color_overlay_text_base", EnumValueType.HEX, Integer.toHexString(0xffaaaaaa)),
    OVERLAY_TEXT_ADDITIONAL_COLOR("color_overlay_text_additional", EnumValueType.HEX, Integer.toHexString(0xffeeeeee)),

    TOOLTIP_BACKGROUND_COLOR("color_tooltip_background", EnumValueType.HEX, Integer.toHexString(0xdc141414)),
    TOOLTIP_TEXT_COLOR("color_tooltip_text", EnumValueType.HEX, Integer.toHexString(0xffeeeeee)),

    ACTIVE_TEXT_COLOR("color_active_text", EnumValueType.HEX, Integer.toHexString(0xff00e51a)),
    ACTIVE_ELEMENT_COLOR("color_active_element", EnumValueType.HEX, Integer.toHexString(0x3200e51a)),

    INACTIVE_TEXT_COLOR("color_inactive_text", EnumValueType.HEX, Integer.toHexString(0xffcc0000)),
    INACTIVE_ELEMENT_COLOR("color_inactive_element", EnumValueType.HEX, Integer.toHexString(0x32cc0000)),

    STATUS_TEXT_COLOR("color_status_text", EnumValueType.HEX, Integer.toHexString(0xff00e5e5)),
    STATUS_ELEMENT_COLOR("color_status_element", EnumValueType.HEX, Integer.toHexString(0x3200e5e5)),

    //Scale

    TEXT_TITLE_SCALE("scale_text_title", EnumValueType.FLOAT, String.valueOf(0.85F)),
    TEXT_BUTTON_SCALE("scale_text_button", EnumValueType.FLOAT, String.valueOf(0.65F)),
    TEXT_TOOLTIP_SCALE("scale_text_tooltip", EnumValueType.FLOAT, String.valueOf(0.6F)),
    TEXT_SCALE("scale_text", EnumValueType.FLOAT, String.valueOf(0.7F)),
    TEXT_SUB_SCALE("text_sub", EnumValueType.FLOAT, String.valueOf(0.6F)),
    TEXT_PANEL_SCALE("scale_text_panel", EnumValueType.FLOAT, String.valueOf(0.6F)),

    OVERLAY_SCALE("scale_overlay", EnumValueType.FLOAT, String.valueOf(0.8F)),

    //Other

    VERTICAL_GRADIENT("misc_background_vertical_gradient", EnumValueType.BOOLEAN, String.valueOf(false));

    private final String key, baseValue;

    private final EnumValueType type;

    private SettingValue value;

    EnumBaseGUISetting(String key, EnumValueType type, String baseValue) {
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
        for (EnumBaseGUISetting setting : values())
            OxygenManagerClient.instance().getClientSettingManager().register(SettingValueUtils.getValue(setting.type, setting.key, setting.baseValue));
    }
}
