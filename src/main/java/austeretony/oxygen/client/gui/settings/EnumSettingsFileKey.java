package austeretony.oxygen.client.gui.settings;

public enum EnumSettingsFileKey {

    SETTINGS_PROFILE("profile"),
    PROFILE_NAME("name"),
    PROFILES("profiles"),
    COLOR_SECTION("color"),
    SCALE_SECTION("scale"),
    OTHER_SECTION("other"),

    //Colors

    BASE_BACKGROUND_COLOR("base_background"),
    ADDITIONAL_BACKGROUND_COLOR("additional_background"),
    PANEL_BACKGROUND_COLOR("panel_background"),

    ENABLED_BUTTON_COLOR("enabled_button"),
    DISABLED_BUTTON_COLOR("disabled_button"),
    HOVERED_BUTTON_COLOR("hovered_button"),

    ENABLED_ELEMENT_COLOR("enabled_element"),
    DISABLED_ELEMENT_COLOR("disabled_element"),
    HOVERED_ELEMENT_COLOR("hovered_element"),

    ENABLED_CONTEXT_ACTION_COLOR("enabled_context_action"),
    DISABLED_CONTEXT_ACTION_COLOR("disabled_context_action"),
    HOVERED_CONTEXT_ACTION_COLOR("hovered_context_action"),
    
    ENABLED_SLIDER_COLOR("enabled_slider"),
    DISABLED_SLIDER_COLOR("disabled_slider"),
    HOVERED_SLIDER_COLOR("hovered_slider"),

    ENABLED_TEXT_COLOR("enabled_text"),
    DISABLED_TEXT_COLOR("disabled_text"),
    HOVERED_TEXT_COLOR("hovered_text"),

    ENABLED_TEXT_DARK_COLOR("enabled_text_dark"),
    DISABLED_TEXT_DARK_COLOR("disabled_text_dark"),
    HOVERED_TEXT_DARK_COLOR("hovered_text_dark"),
    
    ENABLED_TEXTFIELD_COLOR("enabled_textfield"),
    DISABLED_TEXTFIELD_COLOR("disabled_textfield"),
    HOVERED_TEXTFIELD_COLOR("hovered_textfield"),

    BASE_OVERLAY_TEXT_COLOR("base_overlay_text"),
    ADDITIONAL_OVERLAY_TEXT_COLOR("additional_overlay_text"),

    TOOLTIP_BACKGROUND_COLOR("tooltip_background"),
    TOOLTIP_TEXT_COLOR("tooltip_text"),

    //Scale

    TITLE_SCALE("title"),
    BUTTON_TEXT_SCALE("button_text"),
    TOOLTIP_SCALE("tooltip"),
    TEXT_SCALE("text"),
    SUB_TEXT_SCALE("sub_text"),
    PANEL_TEXT_SCALE("panel_text"),
    DROP_DOWN_LIST_SCALE("drop_down_list"),
    CONTEXT_MENU_SCALE("context_menu"),
    OVERLAY_SCALE("overlay"),

    //Other

    USE_TEXTURES_FLAG("use_textures"),

    TEXTURE_OFFSET_X("texture_offset_x"),
    TEXTURE_OFFSET_Y("texture_offset_y"),

    DROP_DOWN_LIST_WIDTH("drop_down_list_width"),
    CONTEXT_MENU_WIDTH("context_menu_width");

    public final String key;

    EnumSettingsFileKey(String key) {
        this.key = key;
    }
}
