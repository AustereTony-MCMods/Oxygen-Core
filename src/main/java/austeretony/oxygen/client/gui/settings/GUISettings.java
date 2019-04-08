package austeretony.oxygen.client.gui.settings;

public class GUISettings {

    private static GUISettings instance;

    private int 
    baseGUIBackgroundColor, 
    additionalGUIBackgroundColor, 
    enabledElementColor, 
    disabledElementColor, 
    hoveredElementColor,
    enabledTextColor, 
    disabledTextColor, 
    hoveredTextColor,
    enabledTextColorDark, 
    disabledTextColorDark, 
    hoveredTextColorDark;

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

        this.enabledElementColor = 0xDC131313;
        this.disabledElementColor = 0xDC101010;
        this.hoveredElementColor = 0xDC303030;

        this.enabledTextColor = 0xFFB2B2B2;
        this.disabledTextColor = 0xFF8C8C8C;
        this.hoveredTextColor = 0xFFD1D1D1;

        this.enabledTextColorDark = 0xFF888888;
        this.disabledTextColorDark = 0xFF555555;
        this.hoveredTextColorDark = 0xFFAAAAAA;

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
}
