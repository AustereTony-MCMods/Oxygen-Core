package austeretony.oxygen_core.client.gui.settings;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class GUISettings {

    private static GUISettings instance;

    private final GUISettingsLoader loader;

    private GUISettingsProfile defaultProfile, currentProfile;

    private final Map<String, GUISettingsProfile> profiles = new HashMap<>(5);

    private GUISettings() {
        this.loader = new GUISettingsLoader(this);
    }

    public static void create() {
        if (instance == null) {
            instance = new GUISettings();
            instance.init();
        }
    }

    public static GUISettings get() {
        return instance;
    }

    private void init() {       
        this.loader.loadSettings();
    }

    public void reload() {
        this.loader.loadSettings();
    }

    public void save() {
        //TODO
    }

    public GUISettingsProfile getDefaultProfile() {
        return this.defaultProfile;
    }

    public GUISettingsProfile getCurrentProfile() {
        return this.currentProfile;
    }

    public void setCurrentProfile(GUISettingsProfile profile) {
        this.currentProfile = profile;
    }

    public Collection<GUISettingsProfile> getProfiles() {
        return this.profiles.values();
    }

    public GUISettingsProfile getProfile(String name) {
        return this.profiles.get(name);
    }

    public void addProfile(GUISettingsProfile profile) {
        this.profiles.put(profile.name, profile);
    }

    public void removeProfile(String name) {
        this.profiles.remove(name);
    }

    public void setBaseGUIBackgroundColor(int colorHex) {
        this.currentProfile.baseGUIBackgroundColor = colorHex;
    }

    public int getBaseGUIBackgroundColor() {
        return this.currentProfile.baseGUIBackgroundColor;
    }

    public void setAdditionalGUIBackgroundColor(int colorHex) {
        this.currentProfile.additionalGUIBackgroundColor = colorHex;
    }

    public int getAdditionalGUIBackgroundColor() {
        return this.currentProfile.additionalGUIBackgroundColor;
    }

    public void setEnabledButtonColor(int colorHex) {
        this.currentProfile.enabledButtonColor = colorHex;
    }

    public int getEnabledButtonColor() {
        return this.currentProfile.enabledButtonColor;
    }

    public void setDisabledButtonColor(int colorHex) {
        this.currentProfile.disabledButtonColor = colorHex;
    }

    public int getDisabledButtonColor() {
        return this.currentProfile.disabledButtonColor;
    }

    public void setHoveredButtonColor(int colorHex) {
        this.currentProfile.hoveredButtonColor = colorHex;
    }

    public int getHoveredButtonColor() {
        return this.currentProfile.hoveredButtonColor;
    }

    public void setEnabledElementColor(int colorHex) {
        this.currentProfile.enabledElementColor = colorHex;
    }

    public int getEnabledElementColor() {
        return this.currentProfile.enabledElementColor;
    }

    public void setDisabledElementColor(int colorHex) {
        this.currentProfile.disabledElementColor = colorHex;
    }

    public int getDisabledElementColor() {
        return this.currentProfile.disabledElementColor;
    }

    public void setHoveredElementColor(int colorHex) {
        this.currentProfile.hoveredElementColor = colorHex;
    }

    public int getHoveredElementColor() {
        return this.currentProfile.hoveredElementColor;
    }

    public void setEnabledSliderColor(int colorHex) {
        this.currentProfile.enabledSliderColor = colorHex;
    }

    public int getEnabledSliderColor() {
        return this.currentProfile.enabledSliderColor;
    }

    public void setDisabledSliderColor(int colorHex) {
        this.currentProfile.disabledSliderColor = colorHex;
    }

    public int getDisabledSliderColor() {
        return this.currentProfile.disabledSliderColor;
    }

    public void setHoveredSliderColor(int colorHex) {
        this.currentProfile.hoveredSliderColor = colorHex;
    }

    public int getHoveredSliderColor() {
        return this.currentProfile.hoveredSliderColor;
    }

    public void setEnabledTextColor(int colorHex) {
        this.currentProfile.enabledTextColor = colorHex;
    }

    public int getEnabledTextColor() {
        return this.currentProfile.enabledTextColor;
    }

    public void setDisabledTextColor(int colorHex) {
        this.currentProfile.disabledTextColor = colorHex;
    }

    public int getDisabledTextColor() {
        return this.currentProfile.disabledTextColor;
    }

    public void setHoveredTextColor(int colorHex) {
        this.currentProfile.hoveredTextColor = colorHex;
    }

    public int getHoveredTextColor() {
        return this.currentProfile.hoveredTextColor;
    }

    public void setEnabledTextColorDark(int colorHex) {
        this.currentProfile.enabledTextColorDark = colorHex;
    }

    public int getEnabledTextColorDark() {
        return this.currentProfile.enabledTextColorDark;
    }

    public void setDisabledTextColorDark(int colorHex) {
        this.currentProfile.disabledTextColorDark = colorHex;
    }

    public int getDisabledTextColorDark() {
        return this.currentProfile.disabledTextColorDark;
    }

    public void setHoveredTextColorDark(int colorHex) {
        this.currentProfile.hoveredTextColorDark = colorHex;
    }

    public int getHoveredTextColorDark() {
        return this.currentProfile.hoveredTextColorDark;
    }

    public void setEnabledTextFieldColor(int colorHex) {
        this.currentProfile.enabledTextFieldColor = colorHex;
    }

    public int getEnabledTextFieldColor() {
        return this.currentProfile.enabledTextFieldColor;
    }

    public void setDisabledTextFieldColor(int colorHex) {
        this.currentProfile.disabledTextFieldColor = colorHex;
    }

    public int getDisabledTextFieldColor() {
        return this.currentProfile.disabledTextFieldColor;
    }

    public void setHoveredTextFieldColor(int colorHex) {
        this.currentProfile.hoveredTextFieldColor = colorHex;
    }

    public int getHoveredTextFieldColor() {
        return this.currentProfile.hoveredTextFieldColor;
    }

    public void setBaseOverlayTextColor(int colorHex) {
        this.currentProfile.baseOverlayTextColor = colorHex;
    }

    public int getBaseOverlayTextColor() {
        return this.currentProfile.baseOverlayTextColor;
    }

    public void setAdditionalOverlayTextColor(int colorHex) {
        this.currentProfile.additionalOverlayTextColor = colorHex;
    }

    public int getAdditionalOverlayTextColor() {
        return this.currentProfile.additionalOverlayTextColor;
    }

    public int getTooltipBackgroundColor() {
        return this.currentProfile.tooltipBackgroundColor;
    }

    public void setTooltipBackgroundColor(int colorHex) {
        this.currentProfile.tooltipBackgroundColor = colorHex;
    }

    public int getTooltipTextColor() {
        return this.currentProfile.tooltipTextColor;
    }

    public void setTooltipTextColor(int colorHex) {
        this.currentProfile.tooltipTextColor = colorHex;
    }

    //TODO

    public int getStatusElementColor() {
        return this.currentProfile.statusElementColor;
    }

    public void setStatusElementColor(int colorHex) {
        this.currentProfile.statusElementColor = colorHex;
    }

    public int getStatusElementColorTransparent() {
        return this.currentProfile.statusElementColorTransparent;
    }

    public void setStatusElementColorTransparent(int colorHex) {
        this.currentProfile.statusElementColorTransparent = colorHex;
    }

    public int getActiveElementColor() {
        return this.currentProfile.activeElementColor;
    }

    public void setActiveElementColor(int colorHex) {
        this.currentProfile.activeElementColor = colorHex;
    }

    public int getActiveElementColorTransparent() {
        return this.currentProfile.activeElementColorTransparent;
    }

    public void setActiveElementColorTransparent(int colorHex) {
        this.currentProfile.activeElementColorTransparent = colorHex;
    }

    public int getInactiveElementColor() {
        return this.currentProfile.inactiveElementColor;
    }

    public void setInactiveElementColor(int colorHex) {
        this.currentProfile.inactiveElementColor = colorHex;
    }

    public int getInactiveElementColorTransparent() {
        return this.currentProfile.inactiveElementColorTransparent;
    }

    public void setInactiveElementColorTransparent(int colorHex) {
        this.currentProfile.inactiveElementColorTransparent = colorHex;
    }

    public float getTitleScale() {
        return this.currentProfile.titleScale;
    }

    public void setTitleScale(float scaleFactor) {
        this.currentProfile.titleScale = scaleFactor;
    }

    public float getButtonTextScale() {
        return this.currentProfile.buttonTextScale;
    }

    public void setButtonTextScale(float scaleFactor) {
        this.currentProfile.buttonTextScale = scaleFactor;
    }

    public float getTooltipScale() {
        return this.currentProfile.tooltipScale;
    }

    public void setTooltipScale(float scaleFactor) {
        this.currentProfile.tooltipScale = scaleFactor;
    }

    public float getTextScale() {
        return this.currentProfile.textScale;
    }

    public void setTextScale(float scaleFactor) {
        this.currentProfile.textScale = scaleFactor;
    }

    public float getSubTextScale() {
        return this.currentProfile.subTextScale;
    }

    public void setSubTextScale(float scaleFactor) {
        this.currentProfile.subTextScale = scaleFactor;
    }

    public float getPanelTextScale() {
        return this.currentProfile.panelTextScale;
    }

    public void setPanelTextScale(float scaleFactor) {
        this.currentProfile.panelTextScale = scaleFactor;
    }

    public float getDropDownListScale() {
        return this.currentProfile.dropDownListScale;
    }

    public void setDropDownListScale(float scaleFactor) {
        this.currentProfile.dropDownListScale = scaleFactor;
    }

    public float getContextMenuScale() {
        return this.currentProfile.contextMenuScale;
    }

    public void setContextMenuScale(float scaleFactor) {
        this.currentProfile.contextMenuScale = scaleFactor;
    }

    public float getOverlayScale() {
        return this.currentProfile.overlayScale;
    }

    public void setOverlayScale(float scaleFactor) {
        this.currentProfile.overlayScale = scaleFactor;
    }

    public int getDropDownListWidth() {
        return this.currentProfile.dropDownListWidth;
    }

    public void setDropDownListWidth(int width) {
        this.currentProfile.dropDownListWidth = width;
    }

    public int getContextMenuWidth() {
        return this.currentProfile.contextMenuWidth;
    }

    public void setContextMenuWidth(int width) {
        this.currentProfile.contextMenuWidth = width;
    }
}
