package austeretony.oxygen_core.client.settings;

import austeretony.oxygen_core.client.api.OxygenClient;
import austeretony.oxygen_core.client.gui.base.Alignment;
import austeretony.oxygen_core.client.settings.gui.SettingWidgets;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.util.value.ValueType;

public final class CoreSettings {

    /**
     * COMMON SETTINGS
     */

    //misc

    public static final SettingValue
    ENABLE_SOUND_EFFECTS = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.COMMON, "Core", "misc",
            ValueType.BOOLEAN, "enable_sound_effects", true, SettingWidgets.checkBox()),
    ENABLE_RARITY_COLORS_GUI_DISPLAY = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.COMMON, "Core", "misc",
            ValueType.BOOLEAN, "enable_rarity_colors_gui_display", true, SettingWidgets.checkBox()),
    ENABLE_DURABILITY_BARS_GUI_DISPLAY = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.COMMON, "Core", "misc",
            ValueType.BOOLEAN, "enable_durability_bars_gui_display", true, SettingWidgets.checkBox()),
    ENABLE_STATUS_MESSAGES = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.COMMON, "Core", "misc",
            ValueType.BOOLEAN, "enable_status_messages", true, SettingWidgets.checkBox()),

    //oxygen menu

    ADD_SETTINGS_SCREEN_TO_OXYGEN_MENU = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.COMMON, "Core", "oxygen_menu",
            ValueType.BOOLEAN, "add_settings_screen", true, SettingWidgets.checkBox()),

    /**
     * INTERFACE SETTINGS
     */

    //alignment

    ALIGNMENT_SETTINGS_SCREEN = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "alignment",
            ValueType.STRING, "settings_screen_alignment", Alignment.CENTER.toString(), SettingWidgets.screenAlignmentList()),

    //color

    COLOR_SCREEN_BACKGROUND_FILL = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_background_fill",
            ValueType.HEX, "color_screen_background_fill", 0x00000000, SettingWidgets.colorPicker()),
    COLOR_CALLBACK_BACKGROUND_FILL = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_background_fill",
            ValueType.HEX, "color_callback_background_fill", 0x68101010, SettingWidgets.colorPicker()),

    COLOR_BACKGROUND_BASE = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_background_screen",
            ValueType.HEX, "color_background_base", 0xdc101010, SettingWidgets.colorPicker()),
    COLOR_BACKGROUND_ADDITIONAL = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_background_screen",
            ValueType.HEX, "color_background_additional", 0xff404040, SettingWidgets.colorPicker()),

    COLOR_ELEMENT_ENABLED = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_element",
            ValueType.HEX, "color_element_enabled", 0x96080808, SettingWidgets.colorPicker()),
    COLOR_ELEMENT_DISABLED = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_element",
            ValueType.HEX, "color_element_disabled", 0x96040404, SettingWidgets.colorPicker()),
    COLOR_ELEMENT_MOUSE_OVER = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_element",
            ValueType.HEX, "color_element_mouse_over", 0x96303030, SettingWidgets.colorPicker()),

    COLOR_BUTTON_ENABLED = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_button",
            ValueType.HEX, "color_button_enabled", 0xff404040, SettingWidgets.colorPicker()),
    COLOR_BUTTON_DISABLED = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_button",
            ValueType.HEX, "color_button_disabled", 0xff202020, SettingWidgets.colorPicker()),
    COLOR_BUTTON_MOUSE_OVER = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_button",
            ValueType.HEX, "color_button_mouse_over", 0xff606060, SettingWidgets.colorPicker()),

    COLOR_SLIDER_ENABLED = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_slider",
            ValueType.HEX, "color_slider_enabled", 0xff505050, SettingWidgets.colorPicker()),
    COLOR_SLIDER_DISABLED = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_slider",
            ValueType.HEX, "color_slider_disabled", 0xff404040, SettingWidgets.colorPicker()),
    COLOR_SLIDER_MOUSE_OVER = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_slider",
            ValueType.HEX, "color_slider_mouse_over", 0xff656565, SettingWidgets.colorPicker()),

    COLOR_TEXT_BASE_ENABLED = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_text_base",
            ValueType.HEX, "color_text_base_enabled", 0xffcccccc, SettingWidgets.colorPicker()),
    COLOR_TEXT_BASE_DISABLED = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_text_base",
            ValueType.HEX, "color_text_base_disabled", 0xff999999, SettingWidgets.colorPicker()),
    COLOR_TEXT_BASE_MOUSE_OVER = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_text_base",
            ValueType.HEX, "color_text_base_mouse_over", 0xffe5e5e5, SettingWidgets.colorPicker()),

    COLOR_TEXT_ADDITIONAL_ENABLED = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_text_bonus",
            ValueType.HEX, "color_text_additional_enabled", 0xffa5a5a5, SettingWidgets.colorPicker()),
    COLOR_TEXT_ADDITIONAL_DISABLED = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_text_bonus",
            ValueType.HEX, "color_text_additional_disabled", 0xff7f7f7f, SettingWidgets.colorPicker()),
    COLOR_TEXT_ADDITIONAL_MOUSE_OVER = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_text_bonus",
            ValueType.HEX, "color_text_additional_mouse_over", 0xffbfbfbf, SettingWidgets.colorPicker()),

    COLOR_TEXT_FIELD_ENABLED = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_text_field",
            ValueType.HEX, "color_text_field_enabled", 0x96454545, SettingWidgets.colorPicker()),
    COLOR_TEXT_FIELD_DISABLED = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_text_field",
            ValueType.HEX, "color_text_field_disabled", 0x96353535, SettingWidgets.colorPicker()),
    COLOR_TEXT_FIELD_MOUSE_OVER = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_text_field",
            ValueType.HEX, "color_text_field_mouse_over", 0x96525252, SettingWidgets.colorPicker()),

    COLOR_OVERLAY_TEXT_BASE = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_overlay_text",
            ValueType.HEX, "color_overlay_text_base", 0xffaaaaaa, SettingWidgets.colorPicker()),
    COLOR_OVERLAY_TEXT_ADDITIONAL = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_overlay_text",
            ValueType.HEX, "color_overlay_text_additional", 0xffeeeeee, SettingWidgets.colorPicker()),

    COLOR_TOOLTIP_TEXT_BASE = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_tooltip_text",
            ValueType.HEX, "color_tooltip_text_base", 0xdc141414, SettingWidgets.colorPicker()),
    COLOR_TOOLTIP_TEXT_ADDITIONAL = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_tooltip_text",
            ValueType.HEX, "color_tooltip_text_additional", 0xffeeeeee, SettingWidgets.colorPicker()),

    COLOR_ELEMENT_ACTIVE = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_special",
            ValueType.HEX, "color_element_active", 0x3200e51a, SettingWidgets.colorPicker()),
    COLOR_TEXT_ACTIVE = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_special",
            ValueType.HEX, "color_text_active", 0xff00e51a, SettingWidgets.colorPicker()),

    COLOR_ELEMENT_INACTIVE = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_special",
            ValueType.HEX, "color_element_inactive", 0x32cc0000, SettingWidgets.colorPicker()),
    COLOR_TEXT_INACTIVE = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_special",
            ValueType.HEX, "color_text_inactive", 0xffcc0000, SettingWidgets.colorPicker()),

    COLOR_ELEMENT_SPECIAL = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_special",
            ValueType.HEX, "color_element_special", 0x3200e5e5, SettingWidgets.colorPicker()),
    COLOR_TEXT_SPECIAL = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "color_special",
            ValueType.HEX, "color_text_special", 0xff00e5e5, SettingWidgets.colorPicker()),

    //scale

    SCALE_TEXT_BASE = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "scale_text",
            ValueType.FLOAT, "scale_text_base", .65F, SettingWidgets.textScaleScrollBar()),
    SCALE_TEXT_ADDITIONAL = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "scale_text",
            ValueType.FLOAT, "scale_text_additional", .55F, SettingWidgets.textScaleScrollBar()),
    SCALE_TEXT_TITLE = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "scale_text",
            ValueType.FLOAT, "scale_text_title", .82F, SettingWidgets.textScaleScrollBar()),
    SCALE_TEXT_BUTTON = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "scale_text",
            ValueType.FLOAT, "scale_text_button", .62F, SettingWidgets.textScaleScrollBar()),
    SCALE_TEXT_PANEL = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "scale_text",
            ValueType.FLOAT, "scale_text_panel", .6F, SettingWidgets.textScaleScrollBar()),
    SCALE_TEXT_TOOLTIP = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "scale_text",
            ValueType.FLOAT, "scale_text_tooltip", .58F, SettingWidgets.textScaleScrollBar()),
    SCALE_TEXT_OVERLAY = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "scale_text",
            ValueType.FLOAT, "scale_text_overlay", .6F, SettingWidgets.textScaleScrollBar()),

    SCALE_OVERLAY = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "scale_misc",
            ValueType.FLOAT, "scale_overlay", 1F, SettingWidgets.floatNormalizedPercentSelector()),

    //misc

    ENABLE_VERTICAL_GRADIENT = OxygenClient.registerSetting(OxygenMain.MOD_ID, SettingType.INTERFACE, "Core", "misc",
            ValueType.BOOLEAN, "enable_vertical_gradient", true, SettingWidgets.checkBox());

    private CoreSettings() {}

    public static void register() {}
}
