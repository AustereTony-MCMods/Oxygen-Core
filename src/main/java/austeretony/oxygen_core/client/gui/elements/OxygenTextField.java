package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.text.GUITextField;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;

public class OxygenTextField extends GUITextField {

    private InputListener inputListener;

    public OxygenTextField(int xPosition, int yPosition, int width, int maxStringLength, String displayText) {
        super(xPosition, yPosition, width, 8, maxStringLength);
        this.setTextScale(EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat());
        this.setTextDynamicColor(EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_HOVERED_COLOR.get().asInt());
        this.setDynamicBackgroundColor(EnumBaseGUISetting.TEXTFIELD_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXTFIELD_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXTFIELD_HOVERED_COLOR.get().asInt()); 
        this.setDisplayText(displayText);
        this.cancelDraggedElementLogic(true);
        this.enableDynamicBackground();
    }

    public void setInputListener(InputListener inputListener) {
        this.inputListener = inputListener;
    }

    @Override
    public boolean keyTyped(char keyChar, int keyCode) {
        boolean flag = super.keyTyped(keyChar, keyCode);
        if (flag && this.inputListener != null)
            this.inputListener.keyTyped(keyChar, keyCode);
        return flag;
    }

    public static interface InputListener {

        void keyTyped(char keyChar, int keyCode);
    }
}
