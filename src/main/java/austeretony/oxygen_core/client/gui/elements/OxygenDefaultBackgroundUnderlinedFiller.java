package austeretony.oxygen_core.client.gui.elements;

import austeretony.oxygen_core.client.gui.OxygenGUIUtils;
import austeretony.oxygen_core.client.gui.elements.OxygenBackgroundFiller;

public class OxygenDefaultBackgroundUnderlinedFiller extends OxygenBackgroundFiller {

    public OxygenDefaultBackgroundUnderlinedFiller(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
    }

    @Override
    public void drawBackground() {
        drawRect(0, 0, this.getWidth(), this.getHeight(), this.getEnabledBackgroundColor());      

        //title underline
        OxygenGUIUtils.drawRect(4.0D, 14.0D, this.getWidth() - 4.0D, 14.4D, this.getDisabledBackgroundColor());

        //panel underline
        OxygenGUIUtils.drawRect(4.0D, this.getHeight() - 12.6D, this.getWidth() - 4.0D, this.getHeight() - 13.0D, this.getDisabledBackgroundColor());
    }
}
