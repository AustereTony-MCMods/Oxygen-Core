package austeretony.oxygen_core.client.gui.privileges.roles;

import austeretony.oxygen_core.client.gui.OxygenGUIUtils;
import austeretony.oxygen_core.client.gui.elements.OxygenBackgroundFiller;

public class RolesBackgroundFiller extends OxygenBackgroundFiller {

    public RolesBackgroundFiller(int xPosition, int yPosition, int width, int height) {             
        super(xPosition, yPosition, width, height);
    }

    @Override
    public void drawBackground() {
        drawRect(0, 0, this.getWidth(), this.getHeight(), this.getEnabledBackgroundColor());      

        //title underline
        OxygenGUIUtils.drawRect(4.0D, 14.0D, this.getWidth() - 4.0D, 14.4D, this.getDisabledBackgroundColor());

        //panel underline
        OxygenGUIUtils.drawRect(4.0D, this.getHeight() - 14.0D, this.getWidth() - 4.0D, this.getHeight() - 14.4D, this.getDisabledBackgroundColor());
    }
}
