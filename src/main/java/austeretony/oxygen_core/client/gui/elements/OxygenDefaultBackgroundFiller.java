package austeretony.oxygen_core.client.gui.elements;

import austeretony.oxygen_core.client.gui.OxygenGUIUtils;

public class OxygenDefaultBackgroundFiller extends OxygenBackgroundFiller {

    public OxygenDefaultBackgroundFiller(int xPosition, int yPosition, int width, int height) {             
        super(xPosition, yPosition, width, height);
    }

    @Override
    public void drawBackground() {
        drawRect(0, 0, this.getWidth(), this.getHeight(), this.getEnabledBackgroundColor());      

        //title underline
        OxygenGUIUtils.drawRect(4.0D, 14.0D, this.getWidth() - 4.0D, 14.4D, this.getDisabledBackgroundColor());
    }
}
