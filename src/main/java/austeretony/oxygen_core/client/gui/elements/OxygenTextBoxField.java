package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.text.GUITextBoxField;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;

public class OxygenTextBoxField extends GUITextBoxField {

    private InputListener inputListener;

    public OxygenTextBoxField(int xPosition, int yPosition, int width, int height, int maxStringLength) {
        super(xPosition, yPosition, width, height, maxStringLength);
        this.setTextScale(EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat());
        this.setTextDynamicColor(EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_HOVERED_COLOR.get().asInt());
        this.setDynamicBackgroundColor(EnumBaseGUISetting.TEXTFIELD_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXTFIELD_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXTFIELD_HOVERED_COLOR.get().asInt());
        this.setLineOffset(2);
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
            this.inputListener.onInput(keyChar, keyCode);
        return flag;
    }

    public static interface InputListener {

        void onInput(char keyChar, int keyCode);
    }
}
