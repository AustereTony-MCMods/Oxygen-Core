package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.text.GUITextField;
import austeretony.oxygen_core.client.gui.settings.GUISettings;

public class OxygenGUITextField extends GUITextField {

    private InputListener inputListener;

    public OxygenGUITextField(int xPosition, int yPosition, int width, int height, int maxStringLength, String displayText, int lineOffset, boolean numberField, long maxNumber) {
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
        this.setDisplayText(displayText);
        this.setLineOffset(lineOffset);
        this.cancelDraggedElementLogic(true);
        if (numberField)
            this.enableNumberFieldMode(maxNumber == - 1 ? Integer.MAX_VALUE : maxNumber);
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