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

    public void setTextureOffsetX(int value) {
        this.textureOffsetX = value;
    }

    public int getTextureOffsetX() {
        return this.textureOffsetX;
    }

    public void setTextureOffsetY(int value) {
        this.textureOffsetY = value;
    }

    public int getTextureOffsetY() {
        return this.textureOffsetY;
    }

    public void setBaseGUIBackgroundColor(int colorHex) {
        this.baseGUIBackgroundColor = colorHex;
    }

    public int getBaseGUIBackgroundColor() {
        return this.baseGUIBackgroundColor;
    }

    public void setAdditionalGUIBackgroundColor(int colorHex) {
        this.additionalGUIBackgroundColor = colorHex;
    }

    public int getAdditionalGUIBackgroundColor() {
        return this.additionalGUIBackgroundColor;
    }

    public void setPanelGUIBackgroundColor(int colorHex) {
        this.panelGUIBackgroundColor = colorHex;
    }

    public int getPanelGUIBackgroundColor() {
        return this.panelGUIBackgroundColor;
    }

    public void setEnabledButtonColor(int colorHex) {
        this.enabledButtonColor = colorHex;
    }

    public int getEnabledButtonColor() {
        return this.enabledButtonColor;
    }

    public void setDisabledButtonColor(int colorHex) {
        this.disabledButtonColor = colorHex;
    }

    public int getDisabledButtonColor() {
        return this.disabledButtonColor;
    }

    public void setHoveredButtonColor(int colorHex) {
        this.hoveredButtonColor = colorHex;
    }

    public int getHoveredButtonColor() {
        return this.hoveredButtonColor;
    }

    public void setEnabledElementColor(int colorHex) {
        this.enabledElementColor = colorHex;
    }

    public int getEnabledElementColor() {
        return this.enabledElementColor;
    }

    public void setDisabledElementColor(int colorHex) {
        this.disabledElementColor = colorHex;
    }

    public int getDisabledElementColor() {
        return this.disabledElementColor;
    }

    public void setHoveredElementColor(int colorHex) {
        this.hoveredElementColor = colorHex;
    }

    public int getHoveredElementColor() {
        return this.hoveredElementColor;
    }

    public void setEnabledContextActionColor(int colorHex) {
        this.enabledContextActionColor = colorHex;
    }

    public int getEnabledContextActionColor() {
        return this.enabledContextActionColor;
    }

    public void setDisabledContextActionColor(int colorHex) {
        this.disabledContextActionColor = colorHex;
    }

    public int getDisabledContextActionColor() {
        return this.disabledContextActionColor;
    }

    public void setHoveredContextActionColor(int colorHex) {
        this.hoveredContextActionColor = colorHex;
    }

    public int getHoveredContextActionColor() {
        return this.hoveredContextActionColor;
    }

    public void setEnabledSliderColor(int colorHex) {
        this.enabledSliderColor = colorHex;
    }

    public int getEnabledSliderColor() {
        return this.enabledSliderColor;
    }

    public void setDisabledSliderColor(int colorHex) {
        this.disabledSliderColor = colorHex;
    }

    public int getDisabledSliderColor() {
        return this.disabledSliderColor;
    }

    public void setHoveredSliderColor(int colorHex) {
        this.hoveredSliderColor = colorHex;
    }

    public int getHoveredSliderColor() {
        return this.hoveredSliderColor;
    }

    public void setEnabledTextColor(int colorHex) {
        this.enabledTextColor = colorHex;
    }

    public int getEnabledTextColor() {
        return this.enabledTextColor;
    }

    public void setDisabledTextColor(int colorHex) {
        this.disabledTextColor = colorHex;
    }

    public int getDisabledTextColor() {
        return this.disabledTextColor;
    }

    public void setHoveredTextColor(int colorHex) {
        this.hoveredTextColor = colorHex;
    }

    public int getHoveredTextColor() {
        return this.hoveredTextColor;
    }

    public void setEnabledTextColorDark(int colorHex) {
        this.enabledTextColorDark = colorHex;
    }

    public int getEnabledTextColorDark() {
        return this.enabledTextColorDark;
    }

    public void setDisabledTextColorDark(int colorHex) {
        this.disabledTextColorDark = colorHex;
    }

    public int getDisabledTextColorDark() {
        return this.disabledTextColorDark;
    }

    public void setHoveredTextColorDark(int colorHex) {
        this.hoveredTextColorDark = colorHex;
    }

    public int getHoveredTextColorDark() {
        return this.hoveredTextColorDark;
    }

    public void setBaseOverlayTextColor(int colorHex) {
        this.baseOverlayTextColor = colorHex;
    }

    public int getBaseOverlayTextColor() {
        return this.baseOverlayTextColor;
    }

    public void setAdditionalOverlayTextColor(int colorHex) {
        this.additionalOverlayTextColor = colorHex;
    }

    public int getAdditionalOverlayTextColor() {
        return this.additionalOverlayTextColor;
    }

    public int getTooltipBackgroundColor() {
        return this.tooltipBackgroundColor;
    }

    public void setTooltipBackgroundColor(int colorHex) {
        this.tooltipBackgroundColor = colorHex;
    }

    public int getTooltipTextColor() {
        return this.tooltipTextColor;
    }

    public void setTooltipTextColor(int colorHex) {
        this.tooltipTextColor = colorHex;
    }

    public float getTitleScale() {
        return this.titleScale;
    }

    public void setTitleScale(float scaleFactor) {
        this.titleScale = scaleFactor;
    }

    public float getButtonTextScale() {
        return this.buttonTextScale;
    }

    public void setButtonTextScale(float scaleFactor) {
        this.buttonTextScale = scaleFactor;
    }

    public float getTooltipScale() {
        return this.tooltipScale;
    }

    public void setTooltipScale(float scaleFactor) {
        this.tooltipScale = scaleFactor;
    }

    public float getTextScale() {
        return this.textScale;
    }

    public void setTextScale(float scaleFactor) {
        this.textScale = scaleFactor;
    }

    public float getSubTextScale() {
        return this.subTextScale;
    }

    public void setSubTextScale(float scaleFactor) {
        this.subTextScale = scaleFactor;
    }

    public float getPanelTextScale() {
        return this.panelTextScale;
    }

    public void setPanelTextScale(float scaleFactor) {
        this.panelTextScale = scaleFactor;
    }

    public float getDropDownListScale() {
        return this.dropDownListScale;
    }

    public void setDropDownListScale(float scaleFactor) {
        this.dropDownListScale = scaleFactor;
    }

    public float getContextMenuScale() {
        return this.contextMenuScale;
    }

    public void setContextMenuScale(float scaleFactor) {
        this.contextMenuScale = scaleFactor;
    }

    public float getOverlayScale() {
        return this.overlayScale;
    }

    public void setOverlayScale(float scaleFactor) {
        this.overlayScale = scaleFactor;
    }

    public int getDropDownListWidth() {
        return this.dropDownListWidth;
    }

    public void setDropDownListWidth(int width) {
        this.dropDownListWidth = width;
    }

    public int getContextMenuWidth() {
        return this.contextMenuWidth;
    }

    public void setContextMenuWidth(int width) {
        this.contextMenuWidth = width;
    }

    public JsonObject serealize() {
        JsonObject object = new JsonObject();

        object.add(EnumSettingKeys.PROFILE_NAME.key, new JsonPrimitive(this.name));

        JsonObject colorSection = new JsonObject();

        colorSection.add(EnumSettingKeys.BASE_BACKGROUND_COLOR.key, new JsonPrimitive(this.baseGUIBackgroundColor));
        colorSection.add(EnumSettingKeys.ADDITIONAL_BACKGROUND_COLOR.key, new JsonPrimitive(this.additionalGUIBackgroundColor));
        colorSection.add(EnumSettingKeys.PANEL_BACKGROUND_COLOR.key, new JsonPrimitive(this.panelGUIBackgroundColor));

        colorSection.add(EnumSettingKeys.ENABLED_BUTTON_COLOR.key, new JsonPrimitive(this.enabledButtonColor));
        colorSection.add(EnumSettingKeys.DISABLED_BUTTON_COLOR.key, new JsonPrimitive(this.disabledButtonColor));
        colorSection.add(EnumSettingKeys.HOVERED_BUTTON_COLOR.key, new JsonPrimitive(this.hoveredButtonColor));

        colorSection.add(EnumSettingKeys.ENABLED_ELEMENT_COLOR.key, new JsonPrimitive(this.enabledElementColor));
        colorSection.add(EnumSettingKeys.DISABLED_ELEMENT_COLOR.key, new JsonPrimitive(this.disabledElementColor));
        colorSection.add(EnumSettingKeys.HOVERED_ELEMENT_COLOR.key, new JsonPrimitive(this.hoveredElementColor));

        colorSection.add(EnumSettingKeys.ENABLED_CONTEXT_ACTION_COLOR.key, new JsonPrimitive(this.enabledContextActionColor));
        colorSection.add(EnumSettingKeys.DISABLED_CONTEXT_ACTION_COLOR.key, new JsonPrimitive(this.disabledContextActionColor));
        colorSection.add(EnumSettingKeys.HOVERED_CONTEXT_ACTION_COLOR.key, new JsonPrimitive(this.hoveredContextActionColor));

        colorSection.add(EnumSettingKeys.ENABLED_SLIDER_COLOR.key, new JsonPrimitive(this.enabledSliderColor));
        colorSection.add(EnumSettingKeys.DISABLED_SLIDER_COLOR.key, new JsonPrimitive(this.disabledSliderColor));
        colorSection.add(EnumSettingKeys.HOVERED_SLIDER_COLOR.key, new JsonPrimitive(this.hoveredSliderColor));

        colorSection.add(EnumSettingKeys.ENABLED_TEXT_COLOR.key, new JsonPrimitive(this.enabledTextColor));
        colorSection.add(EnumSettingKeys.DISABLED_TEXT_COLOR.key, new JsonPrimitive(this.disabledTextColor));
        colorSection.add(EnumSettingKeys.HOVERED_TEXT_COLOR.key, new JsonPrimitive(this.hoveredTextColor));

        colorSection.add(EnumSettingKeys.ENABLED_TEXT_DARK_COLOR.key, new JsonPrimitive(this.enabledTextColorDark));
        colorSection.add(EnumSettingKeys.DISABLED_TEXT_DARK_COLOR.key, new JsonPrimitive(this.disabledTextColorDark));
        colorSection.add(EnumSettingKeys.HOVERED_TEXT_DARK_COLOR.key, new JsonPrimitive(this.hoveredTextColorDark));

        colorSection.add(EnumSettingKeys.BASE_OVERLAY_TEXT_COLOR.key, new JsonPrimitive(this.baseOverlayTextColor));
        colorSection.add(EnumSettingKeys.ADDITIONAL_OVERLAY_TEXT_COLOR.key, new JsonPrimitive(this.additionalOverlayTextColor));

        colorSection.add(EnumSettingKeys.TOOLTIP_BACKGROUND_COLOR.key, new JsonPrimitive(this.tooltipBackgroundColor));
        colorSection.add(EnumSettingKeys.TOOLTIP_TEXT_COLOR.key, new JsonPrimitive(this.tooltipTextColor));

        JsonObject scaleSection = new JsonObject();

        scaleSection.add(EnumSettingKeys.TITLE_SCALE.key, new JsonPrimitive(this.titleScale));
        scaleSection.add(EnumSettingKeys.BUTTON_TEXT_SCALE.key, new JsonPrimitive(this.buttonTextScale));
        scaleSection.add(EnumSettingKeys.TOOLTIP_SCALE.key, new JsonPrimitive(this.tooltipScale));
        scaleSection.add(EnumSettingKeys.TEXT_SCALE.key, new JsonPrimitive(this.textScale));
        scaleSection.add(EnumSettingKeys.SUB_TEXT_SCALE.key, new JsonPrimitive(this.subTextScale));
        scaleSection.add(EnumSettingKeys.PANEL_TEXT_SCALE.key, new JsonPrimitive(this.panelTextScale));
        scaleSection.add(EnumSettingKeys.DROP_DOWN_LIST_SCALE.key, new JsonPrimitive(this.dropDownListScale));
        scaleSection.add(EnumSettingKeys.CONTEXT_MENU_SCALE.key, new JsonPrimitive(this.contextMenuScale));
        scaleSection.add(EnumSettingKeys.OVERLAY_SCALE.key, new JsonPrimitive(this.overlayScale));

        JsonObject otherSection = new JsonObject();

        otherSection.add(EnumSettingKeys.TEXTURE_OFFSET_X.key, new JsonPrimitive(this.textureOffsetX));
        otherSection.add(EnumSettingKeys.TEXTURE_OFFSET_Y.key, new JsonPrimitive(this.textureOffsetY));

        otherSection.add(EnumSettingKeys.DROP_DOWN_LIST_WIDTH.key, new JsonPrimitive(this.dropDownListWidth));
        otherSection.add(EnumSettingKeys.CONTEXT_MENU_WIDTH.key, new JsonPrimitive(this.contextMenuWidth));

        object.add(EnumSettingKeys.COLOR_SECTION.key, colorSection);
        object.add(EnumSettingKeys.SCALE_SECTION.key, scaleSection);
        object.add(EnumSettingKeys.OTHER_SECTION.key, otherSection);

        return object;
    }

    public static GUISettingsProfile deserialize(JsonObject object) {
        GUISettingsProfile profie = new GUISettingsProfile(object.get(EnumSettingKeys.PROFILE_NAME.key).getAsString());

        JsonObject section = object.get(EnumSettingKeys.COLOR_SECTION.key).getAsJsonObject();

        profie.baseGUIBackgroundColor = (int) Long.parseLong(section.get(EnumSettingKeys.BASE_BACKGROUND_COLOR.key).getAsString(), 16);
        profie.additionalGUIBackgroundColor = (int) Long.parseLong(section.get(EnumSettingKeys.ADDITIONAL_BACKGROUND_COLOR.key).getAsString(), 16);
        profie.panelGUIBackgroundColor = (int) Long.parseLong(section.get(EnumSettingKeys.PANEL_BACKGROUND_COLOR.key).getAsString(), 16);

        profie.enabledButtonColor = (int) Long.parseLong(section.get(EnumSettingKeys.ENABLED_BUTTON_COLOR.key).getAsString(), 16);
        profie.disabledButtonColor = (int) Long.parseLong(section.get(EnumSettingKeys.DISABLED_BUTTON_COLOR.key).getAsString(), 16);
        profie.hoveredButtonColor = (int) Long.parseLong(section.get(EnumSettingKeys.HOVERED_BUTTON_COLOR.key).getAsString(), 16);

        profie.enabledElementColor = (int) Long.parseLong(section.get(EnumSettingKeys.ENABLED_ELEMENT_COLOR.key).getAsString(), 16);
        profie.disabledElementColor = (int) Long.parseLong(section.get(EnumSettingKeys.DISABLED_ELEMENT_COLOR.key).getAsString(), 16);
        profie.hoveredElementColor = (int) Long.parseLong(section.get(EnumSettingKeys.HOVERED_ELEMENT_COLOR.key).getAsString(), 16);

        profie.enabledContextActionColor = (int) Long.parseLong(section.get(EnumSettingKeys.ENABLED_CONTEXT_ACTION_COLOR.key).getAsString(), 16);
        profie.disabledContextActionColor = (int) Long.parseLong(section.get(EnumSettingKeys.DISABLED_CONTEXT_ACTION_COLOR.key).getAsString(), 16);
        profie.hoveredContextActionColor = (int) Long.parseLong(section.get(EnumSettingKeys.HOVERED_CONTEXT_ACTION_COLOR.key).getAsString(), 16);

        profie.enabledSliderColor = (int) Long.parseLong(section.get(EnumSettingKeys.ENABLED_SLIDER_COLOR.key).getAsString(), 16);
        profie.disabledSliderColor = (int) Long.parseLong(section.get(EnumSettingKeys.DISABLED_SLIDER_COLOR.key).getAsString(), 16);
        profie.hoveredSliderColor = (int) Long.parseLong(section.get(EnumSettingKeys.HOVERED_SLIDER_COLOR.key).getAsString(), 16);

        profie.enabledTextColor = (int) Long.parseLong(section.get(EnumSettingKeys.ENABLED_TEXT_COLOR.key).getAsString(), 16);
        profie.disabledTextColor = (int) Long.parseLong(section.get(EnumSettingKeys.DISABLED_TEXT_COLOR.key).getAsString(), 16);
        profie.hoveredTextColor = (int) Long.parseLong(section.get(EnumSettingKeys.HOVERED_TEXT_COLOR.key).getAsString(), 16);

        profie.enabledTextColorDark = (int) Long.parseLong(section.get(EnumSettingKeys.ENABLED_TEXT_DARK_COLOR.key).getAsString(), 16);
        profie.disabledTextColorDark = (int) Long.parseLong(section.get(EnumSettingKeys.DISABLED_TEXT_DARK_COLOR.key).getAsString(), 16);
        profie.hoveredTextColorDark = (int) Long.parseLong(section.get(EnumSettingKeys.HOVERED_TEXT_DARK_COLOR.key).getAsString(), 16);

        profie.baseOverlayTextColor = (int) Long.parseLong(section.get(EnumSettingKeys.BASE_OVERLAY_TEXT_COLOR.key).getAsString(), 16);
        profie.additionalOverlayTextColor = (int) Long.parseLong(section.get(EnumSettingKeys.ADDITIONAL_OVERLAY_TEXT_COLOR.key).getAsString(), 16);

        profie.tooltipBackgroundColor = (int) Long.parseLong(section.get(EnumSettingKeys.TOOLTIP_BACKGROUND_COLOR.key).getAsString(), 16);
        profie.tooltipTextColor = (int) Long.parseLong(section.get(EnumSettingKeys.TOOLTIP_TEXT_COLOR.key).getAsString(), 16);

        section = object.get(EnumSettingKeys.SCALE_SECTION.key).getAsJsonObject();

        profie.titleScale =  section.get(EnumSettingKeys.TITLE_SCALE.key).getAsFloat();
        profie.buttonTextScale =  section.get(EnumSettingKeys.BUTTON_TEXT_SCALE.key).getAsFloat();
        profie.tooltipScale =  section.get(EnumSettingKeys.TOOLTIP_SCALE.key).getAsFloat();
        profie.textScale =  section.get(EnumSettingKeys.TEXT_SCALE.key).getAsFloat();
        profie.subTextScale =  section.get(EnumSettingKeys.SUB_TEXT_SCALE.key).getAsFloat();
        profie.panelTextScale =  section.get(EnumSettingKeys.PANEL_TEXT_SCALE.key).getAsFloat();
        profie.dropDownListScale =  section.get(EnumSettingKeys.DROP_DOWN_LIST_SCALE.key).getAsFloat();
        profie.contextMenuScale =  section.get(EnumSettingKeys.CONTEXT_MENU_SCALE.key).getAsFloat();
        profie.overlayScale =  section.get(EnumSettingKeys.OVERLAY_SCALE.key).getAsFloat();

        section = object.get(EnumSettingKeys.OTHER_SECTION.key).getAsJsonObject();

        profie.textureOffsetX = section.get(EnumSettingKeys.TEXTURE_OFFSET_X.key).getAsInt();
        profie.textureOffsetY = section.get(EnumSettingKeys.TEXTURE_OFFSET_Y.key).getAsInt();

        profie.dropDownListWidth = section.get(EnumSettingKeys.DROP_DOWN_LIST_WIDTH.key).getAsInt();
        profie.contextMenuWidth =  section.get(EnumSettingKeys.CONTEXT_MENU_WIDTH.key).getAsInt();

        return profie;
    }
}
