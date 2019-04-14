package austeretony.oxygen.client.gui.settings;

public class GUISettings {

    private static GUISettings instance;

    private int 
    baseGUIBackgroundColor, 
    additionalGUIBackgroundColor, 
    enabledButtonColor, 
    disabledButtonColor, 
    hoveredButtonColor,
    enabledElementColor, 
    disabledElementColor, 
    hoveredElementColor,
    enabledContextActionColor, 
    disabledContextActionColor, 
    hoveredContextActionColor,
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
    contextMenuWidth;

    private float 
    titleScale,
    buttonTextScale,
    tooltipScale,
    textScale,
    subTextScale,
    dropDownListScale,
    contextMenuScale;

    private GUISettings() {}

    public static void create() {
        if (instance == null) {
            instance = new GUISettings();
            instance.init();
        }
    }

    public static GUISettings instance() {
        return instance;
    }

    private void init() {
        this.baseGUIBackgroundColor = 0xDC202020;
        this.additionalGUIBackgroundColor = 0xDC131313;

        this.enabledButtonColor = 0xFF404040;
        this.disabledButtonColor = 0xFF202020;
        this.hoveredButtonColor = 0xFF606060;

        this.enabledElementColor = 0xDC131313;
        this.disabledElementColor = 0xDC101010;
        this.hoveredElementColor = 0xDC303030;

        this.enabledContextActionColor = 0xFF282828;
        this.disabledContextActionColor = 0xFF222222;
        this.hoveredContextActionColor = 0xFF404040;

        this.enabledTextColor = 0xFFCCCCCC;
        this.disabledTextColor = 0xFF999999;
        this.hoveredTextColor = 0xFFE5E5E5;

        this.enabledTextColorDark = 0xFFA5A5A5;
        this.disabledTextColorDark = 0xFF7F7F7F;
        this.hoveredTextColorDark = 0xFFBFBFBF;

        this.baseOverlayTextColor = 0xFFAAAAAA;
        this.additionalOverlayTextColor = 0xFFEEEEEE;

        this.tooltipBackgroundColor = 0xFF282828;
        this.tooltipTextColor = 0xFFEEEEEE;

        this.contextMenuWidth = 55;

        this.titleScale = 0.9F;
        this.buttonTextScale = 0.7F;
        this.tooltipScale = 0.7F;
        this.textScale = 0.7F;
        this.subTextScale = 0.6F;
        this.dropDownListScale = 0.9F;
        this.contextMenuScale = 0.9F;

        //TODO implement GUIs customization for clients (GUI or config)
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

    public int getContextMenuWidth() {
        return this.contextMenuWidth;
    }

    public void setContextMenuWidth(int colorHex) {
        this.contextMenuWidth = colorHex;
    }
}
