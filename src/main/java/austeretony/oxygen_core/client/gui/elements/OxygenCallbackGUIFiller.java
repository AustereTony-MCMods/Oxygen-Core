package austeretony.oxygen_core.client.gui.elements;

import austeretony.oxygen_core.client.gui.settings.GUISettings;

public class OxygenCallbackGUIFiller extends BackgroundGUIFiller {

    public OxygenCallbackGUIFiller(int xPosition, int yPosition, int width, int height) {             
        super(xPosition, yPosition, width, height);
        this.setDynamicBackgroundColor(GUISettings.get().getBaseGUIBackgroundColor(), GUISettings.get().getAdditionalGUIBackgroundColor(), 0);
    }

    @Override
    public void drawBackground() {
        //main background  
        drawRect(0, 0, this.getWidth(), this.getHeight(), this.getEnabledBackgroundColor());      

        //title underline
        CustomRectUtils.drawRect(4.0D, 13.0D, this.getWidth() - 4.0D, 13.4D, this.getDisabledBackgroundColor());

        //frame
        CustomRectUtils.drawRect(0.0D, 0.0D, 0.4D, this.getHeight(), this.getDisabledBackgroundColor());
        CustomRectUtils.drawRect(this.getWidth() - 0.4D, 0.0D, this.getWidth(), this.getHeight(), this.getDisabledBackgroundColor());
        CustomRectUtils.drawRect(0.0D, 0.0D, this.getWidth(), 0.4D, this.getDisabledBackgroundColor());
        CustomRectUtils.drawRect(0.0D, this.getHeight() - 0.4D, this.getWidth(), this.getHeight(), this.getDisabledBackgroundColor());
    }
}