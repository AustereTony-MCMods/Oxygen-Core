package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.text.GUITextBoxField;
import austeretony.oxygen_core.client.gui.settings.GUISettings;

public class OxygenGUITextBoxField extends GUITextBoxField {

    private InputListener inputListener;

    public OxygenGUITextBoxField(int xPosition, int yPosition, int width, int height, int maxStringLength) {
        super(xPosition, yPosition, width, height, maxStringLength);
        this.setTextScale(GUISettings.get().getSubTextScale());
        this.setTextDynamicColor(
                GUISettings.get().getEnabledTextColor(), 
                GUISettings.get().getDisabledTextColor(), 
                GUISettings.get().getHoveredTextColor());
        this.enableDynamicBackground(
                GUISettings.get().getEnabledTextFieldColor(), 
                GUISettings.get().getDisabledTextFieldColor(), 
                GUISettings.get().getHoveredTextFieldColor());
        this.setLineOffset(2);
        this.cancelDraggedElementLogic(true);
    }

    public void setInputListener(InputListener inputListener) {
        this.inputListener = inputListener;
    }

    @Override
    public boolean keyTyped(char keyChar, int keyCode) {
        boolean flag = super.keyTyped(keyChar, keyCode);
        if (flag && this.inputListener != null)
            this.inputListener.onInput(keyChar, keyCode);
        return flag;
    }

    public static interface InputListener {

        void onInput(char keyChar, int keyCode);
    }
}
