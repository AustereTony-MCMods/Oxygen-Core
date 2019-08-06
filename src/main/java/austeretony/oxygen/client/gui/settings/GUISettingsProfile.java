package austeretony.oxygen.client.gui.settings;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class GUISettingsProfile {

    public final String name;

    protected int 
    baseGUIBackgroundColor, 
    additionalGUIBackgroundColor, 
    panelGUIBackgroundColor,
    enabledButtonColor, 
    disabledButtonColor, 
    hoveredButtonColor,
    enabledElementColor, 
    disabledElementColor, 
    hoveredElementColor,
    enabledContextActionColor, 
    disabledContextActionColor, 
    hoveredContextActionColor,
    enabledSliderColor, 
    disabledSliderColor, 
    hoveredSliderColor,
    enabledTextColor, 
    disabledTextColor, 
    hoveredTextColor,
    enabledTextColorDark, 
    disabledTextColorDark, 
    hoveredTextColorDark,
    enabledTextFieldColor, 
    disabledTextFieldColor, 
    hoveredTextFieldColor,
    baseOverlayTextColor,
    additionalOverlayTextColor,
    tooltipBackgroundColor,
    tooltipTextColor,

    dropDownListWidth,
    contextMenuWidth,

    textureOffsetX,
    textureOffsetY;

    protected float 
    titleScale,
    buttonTextScale,
    tooltipScale,
    textScale,
    subTextScale,
    panelTextScale,
    dropDownListScale,
    contextMenuScale,
    overlayScale;

    public GUISettingsProfile(String name) {
        this.name = name;
    }

    public JsonObject serealize() {
        JsonObject object = new JsonObject();

        object.add(EnumSettingsFileKey.PROFILE_NAME.key, new JsonPrimitive(this.name));

        JsonObject colorSection = new JsonObject();

        colorSection.add(EnumSettingsFileKey.BASE_BACKGROUND_COLOR.key, new JsonPrimitive(this.baseGUIBackgroundColor));
        colorSection.add(EnumSettingsFileKey.ADDITIONAL_BACKGROUND_COLOR.key, new JsonPrimitive(this.additionalGUIBackgroundColor));
        colorSection.add(EnumSettingsFileKey.PANEL_BACKGROUND_COLOR.key, new JsonPrimitive(this.panelGUIBackgroundColor));

        colorSection.add(EnumSettingsFileKey.ENABLED_BUTTON_COLOR.key, new JsonPrimitive(this.enabledButtonColor));
        colorSection.add(EnumSettingsFileKey.DISABLED_BUTTON_COLOR.key, new JsonPrimitive(this.disabledButtonColor));
        colorSection.add(EnumSettingsFileKey.HOVERED_BUTTON_COLOR.key, new JsonPrimitive(this.hoveredButtonColor));

        colorSection.add(EnumSettingsFileKey.ENABLED_ELEMENT_COLOR.key, new JsonPrimitive(this.enabledElementColor));
        colorSection.add(EnumSettingsFileKey.DISABLED_ELEMENT_COLOR.key, new JsonPrimitive(this.disabledElementColor));
        colorSection.add(EnumSettingsFileKey.HOVERED_ELEMENT_COLOR.key, new JsonPrimitive(this.hoveredElementColor));

        colorSection.add(EnumSettingsFileKey.ENABLED_CONTEXT_ACTION_COLOR.key, new JsonPrimitive(this.enabledContextActionColor));
        colorSection.add(EnumSettingsFileKey.DISABLED_CONTEXT_ACTION_COLOR.key, new JsonPrimitive(this.disabledContextActionColor));
        colorSection.add(EnumSettingsFileKey.HOVERED_CONTEXT_ACTION_COLOR.key, new JsonPrimitive(this.hoveredContextActionColor));

        colorSection.add(EnumSettingsFileKey.ENABLED_SLIDER_COLOR.key, new JsonPrimitive(this.enabledSliderColor));
        colorSection.add(EnumSettingsFileKey.DISABLED_SLIDER_COLOR.key, new JsonPrimitive(this.disabledSliderColor));
        colorSection.add(EnumSettingsFileKey.HOVERED_SLIDER_COLOR.key, new JsonPrimitive(this.hoveredSliderColor));

        colorSection.add(EnumSettingsFileKey.ENABLED_TEXT_COLOR.key, new JsonPrimitive(this.enabledTextColor));
        colorSection.add(EnumSettingsFileKey.DISABLED_TEXT_COLOR.key, new JsonPrimitive(this.disabledTextColor));
        colorSection.add(EnumSettingsFileKey.HOVERED_TEXT_COLOR.key, new JsonPrimitive(this.hoveredTextColor));

        colorSection.add(EnumSettingsFileKey.ENABLED_TEXT_DARK_COLOR.key, new JsonPrimitive(this.enabledTextColorDark));
        colorSection.add(EnumSettingsFileKey.DISABLED_TEXT_DARK_COLOR.key, new JsonPrimitive(this.disabledTextColorDark));
        colorSection.add(EnumSettingsFileKey.HOVERED_TEXT_DARK_COLOR.key, new JsonPrimitive(this.hoveredTextColorDark));

        colorSection.add(EnumSettingsFileKey.ENABLED_TEXTFIELD_COLOR.key, new JsonPrimitive(this.enabledTextFieldColor));
        colorSection.add(EnumSettingsFileKey.DISABLED_TEXTFIELD_COLOR.key, new JsonPrimitive(this.disabledTextFieldColor));
        colorSection.add(EnumSettingsFileKey.HOVERED_TEXTFIELD_COLOR.key, new JsonPrimitive(this.hoveredTextFieldColor));

        colorSection.add(EnumSettingsFileKey.BASE_OVERLAY_TEXT_COLOR.key, new JsonPrimitive(this.baseOverlayTextColor));
        colorSection.add(EnumSettingsFileKey.ADDITIONAL_OVERLAY_TEXT_COLOR.key, new JsonPrimitive(this.additionalOverlayTextColor));

        colorSection.add(EnumSettingsFileKey.TOOLTIP_BACKGROUND_COLOR.key, new JsonPrimitive(this.tooltipBackgroundColor));
        colorSection.add(EnumSettingsFileKey.TOOLTIP_TEXT_COLOR.key, new JsonPrimitive(this.tooltipTextColor));

        JsonObject scaleSection = new JsonObject();

        scaleSection.add(EnumSettingsFileKey.TITLE_SCALE.key, new JsonPrimitive(this.titleScale));
        scaleSection.add(EnumSettingsFileKey.BUTTON_TEXT_SCALE.key, new JsonPrimitive(this.buttonTextScale));
        scaleSection.add(EnumSettingsFileKey.TOOLTIP_SCALE.key, new JsonPrimitive(this.tooltipScale));
        scaleSection.add(EnumSettingsFileKey.TEXT_SCALE.key, new JsonPrimitive(this.textScale));
        scaleSection.add(EnumSettingsFileKey.SUB_TEXT_SCALE.key, new JsonPrimitive(this.subTextScale));
        scaleSection.add(EnumSettingsFileKey.PANEL_TEXT_SCALE.key, new JsonPrimitive(this.panelTextScale));
        scaleSection.add(EnumSettingsFileKey.DROP_DOWN_LIST_SCALE.key, new JsonPrimitive(this.dropDownListScale));
        scaleSection.add(EnumSettingsFileKey.CONTEXT_MENU_SCALE.key, new JsonPrimitive(this.contextMenuScale));
        scaleSection.add(EnumSettingsFileKey.OVERLAY_SCALE.key, new JsonPrimitive(this.overlayScale));

        JsonObject otherSection = new JsonObject();

        otherSection.add(EnumSettingsFileKey.TEXTURE_OFFSET_X.key, new JsonPrimitive(this.textureOffsetX));
        otherSection.add(EnumSettingsFileKey.TEXTURE_OFFSET_Y.key, new JsonPrimitive(this.textureOffsetY));

        otherSection.add(EnumSettingsFileKey.DROP_DOWN_LIST_WIDTH.key, new JsonPrimitive(this.dropDownListWidth));
        otherSection.add(EnumSettingsFileKey.CONTEXT_MENU_WIDTH.key, new JsonPrimitive(this.contextMenuWidth));

        object.add(EnumSettingsFileKey.COLOR_SECTION.key, colorSection);
        object.add(EnumSettingsFileKey.SCALE_SECTION.key, scaleSection);
        object.add(EnumSettingsFileKey.OTHER_SECTION.key, otherSection);

        return object;
    }

    public static GUISettingsProfile deserialize(JsonObject object) {
        GUISettingsProfile profie = new GUISettingsProfile(object.get(EnumSettingsFileKey.PROFILE_NAME.key).getAsString());

        JsonObject section = object.get(EnumSettingsFileKey.COLOR_SECTION.key).getAsJsonObject();

        profie.baseGUIBackgroundColor = (int) Long.parseLong(section.get(EnumSettingsFileKey.BASE_BACKGROUND_COLOR.key).getAsString(), 16);
        profie.additionalGUIBackgroundColor = (int) Long.parseLong(section.get(EnumSettingsFileKey.ADDITIONAL_BACKGROUND_COLOR.key).getAsString(), 16);
        profie.panelGUIBackgroundColor = (int) Long.parseLong(section.get(EnumSettingsFileKey.PANEL_BACKGROUND_COLOR.key).getAsString(), 16);

        profie.enabledButtonColor = (int) Long.parseLong(section.get(EnumSettingsFileKey.ENABLED_BUTTON_COLOR.key).getAsString(), 16);
        profie.disabledButtonColor = (int) Long.parseLong(section.get(EnumSettingsFileKey.DISABLED_BUTTON_COLOR.key).getAsString(), 16);
        profie.hoveredButtonColor = (int) Long.parseLong(section.get(EnumSettingsFileKey.HOVERED_BUTTON_COLOR.key).getAsString(), 16);

        profie.enabledElementColor = (int) Long.parseLong(section.get(EnumSettingsFileKey.ENABLED_ELEMENT_COLOR.key).getAsString(), 16);
        profie.disabledElementColor = (int) Long.parseLong(section.get(EnumSettingsFileKey.DISABLED_ELEMENT_COLOR.key).getAsString(), 16);
        profie.hoveredElementColor = (int) Long.parseLong(section.get(EnumSettingsFileKey.HOVERED_ELEMENT_COLOR.key).getAsString(), 16);

        profie.enabledContextActionColor = (int) Long.parseLong(section.get(EnumSettingsFileKey.ENABLED_CONTEXT_ACTION_COLOR.key).getAsString(), 16);
        profie.disabledContextActionColor = (int) Long.parseLong(section.get(EnumSettingsFileKey.DISABLED_CONTEXT_ACTION_COLOR.key).getAsString(), 16);
        profie.hoveredContextActionColor = (int) Long.parseLong(section.get(EnumSettingsFileKey.HOVERED_CONTEXT_ACTION_COLOR.key).getAsString(), 16);

        profie.enabledSliderColor = (int) Long.parseLong(section.get(EnumSettingsFileKey.ENABLED_SLIDER_COLOR.key).getAsString(), 16);
        profie.disabledSliderColor = (int) Long.parseLong(section.get(EnumSettingsFileKey.DISABLED_SLIDER_COLOR.key).getAsString(), 16);
        profie.hoveredSliderColor = (int) Long.parseLong(section.get(EnumSettingsFileKey.HOVERED_SLIDER_COLOR.key).getAsString(), 16);

        profie.enabledTextColor = (int) Long.parseLong(section.get(EnumSettingsFileKey.ENABLED_TEXT_COLOR.key).getAsString(), 16);
        profie.disabledTextColor = (int) Long.parseLong(section.get(EnumSettingsFileKey.DISABLED_TEXT_COLOR.key).getAsString(), 16);
        profie.hoveredTextColor = (int) Long.parseLong(section.get(EnumSettingsFileKey.HOVERED_TEXT_COLOR.key).getAsString(), 16);

        profie.enabledTextColorDark = (int) Long.parseLong(section.get(EnumSettingsFileKey.ENABLED_TEXT_DARK_COLOR.key).getAsString(), 16);
        profie.disabledTextColorDark = (int) Long.parseLong(section.get(EnumSettingsFileKey.DISABLED_TEXT_DARK_COLOR.key).getAsString(), 16);
        profie.hoveredTextColorDark = (int) Long.parseLong(section.get(EnumSettingsFileKey.HOVERED_TEXT_DARK_COLOR.key).getAsString(), 16);

        profie.enabledTextFieldColor = (int) Long.parseLong(section.get(EnumSettingsFileKey.ENABLED_TEXTFIELD_COLOR.key).getAsString(), 16);
        profie.disabledTextFieldColor = (int) Long.parseLong(section.get(EnumSettingsFileKey.DISABLED_TEXTFIELD_COLOR.key).getAsString(), 16);
        profie.hoveredTextFieldColor = (int) Long.parseLong(section.get(EnumSettingsFileKey.HOVERED_TEXTFIELD_COLOR.key).getAsString(), 16);

        profie.baseOverlayTextColor = (int) Long.parseLong(section.get(EnumSettingsFileKey.BASE_OVERLAY_TEXT_COLOR.key).getAsString(), 16);
        profie.additionalOverlayTextColor = (int) Long.parseLong(section.get(EnumSettingsFileKey.ADDITIONAL_OVERLAY_TEXT_COLOR.key).getAsString(), 16);

        profie.tooltipBackgroundColor = (int) Long.parseLong(section.get(EnumSettingsFileKey.TOOLTIP_BACKGROUND_COLOR.key).getAsString(), 16);
        profie.tooltipTextColor = (int) Long.parseLong(section.get(EnumSettingsFileKey.TOOLTIP_TEXT_COLOR.key).getAsString(), 16);

        section = object.get(EnumSettingsFileKey.SCALE_SECTION.key).getAsJsonObject();

        profie.titleScale =  section.get(EnumSettingsFileKey.TITLE_SCALE.key).getAsFloat();
        profie.buttonTextScale =  section.get(EnumSettingsFileKey.BUTTON_TEXT_SCALE.key).getAsFloat();
        profie.tooltipScale =  section.get(EnumSettingsFileKey.TOOLTIP_SCALE.key).getAsFloat();
        profie.textScale =  section.get(EnumSettingsFileKey.TEXT_SCALE.key).getAsFloat();
        profie.subTextScale =  section.get(EnumSettingsFileKey.SUB_TEXT_SCALE.key).getAsFloat();
        profie.panelTextScale =  section.get(EnumSettingsFileKey.PANEL_TEXT_SCALE.key).getAsFloat();
        profie.dropDownListScale =  section.get(EnumSettingsFileKey.DROP_DOWN_LIST_SCALE.key).getAsFloat();
        profie.contextMenuScale =  section.get(EnumSettingsFileKey.CONTEXT_MENU_SCALE.key).getAsFloat();
        profie.overlayScale =  section.get(EnumSettingsFileKey.OVERLAY_SCALE.key).getAsFloat();

        section = object.get(EnumSettingsFileKey.OTHER_SECTION.key).getAsJsonObject();

        profie.textureOffsetX = section.get(EnumSettingsFileKey.TEXTURE_OFFSET_X.key).getAsInt();
        profie.textureOffsetY = section.get(EnumSettingsFileKey.TEXTURE_OFFSET_Y.key).getAsInt();

        profie.dropDownListWidth = section.get(EnumSettingsFileKey.DROP_DOWN_LIST_WIDTH.key).getAsInt();
        profie.contextMenuWidth =  section.get(EnumSettingsFileKey.CONTEXT_MENU_WIDTH.key).getAsInt();

        return profie;
    }
}
