package austeretony.oxygen_core.client.gui.elements;

import austeretony.oxygen_core.client.gui.OxygenGUIUtils;

public class OxygenDefaultBackgroundWithButtonsFiller extends OxygenBackgroundFiller {

    public OxygenDefaultBackgroundWithButtonsFiller(int xPosition, int yPosition, int width, int height) {             
        super(xPosition, yPosition, width, height);
    }

    @Override
    public void drawBackground() {
        drawRect(0, 0, this.getWidth(), this.getHeight(), this.getEnabledBackgroundColor());      

        //title underline
        OxygenGUIUtils.drawRect(4.0D, 14.0D, this.getWidth() - 4.0D, 14.4D, this.getDisabledBackgroundColor());

        //buttons background
        OxygenGUIUtils.drawRect(- this.screen.guiLeft, this.getHeight() + this.screen.guiTop - 10, this.mc.displayWidth, this.mc.displayHeight, this.getEnabledBackgroundColor());
    }
}
