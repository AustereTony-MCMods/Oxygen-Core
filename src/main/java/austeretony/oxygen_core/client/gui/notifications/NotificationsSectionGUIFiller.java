package austeretony.oxygen_core.client.gui.notifications;

import austeretony.oxygen_core.client.gui.elements.BackgroundGUIFiller;
import austeretony.oxygen_core.client.gui.elements.CustomRectUtils;
import austeretony.oxygen_core.client.gui.settings.GUISettings;

public class NotificationsSectionGUIFiller extends BackgroundGUIFiller {

    public NotificationsSectionGUIFiller(int xPosition, int yPosition, int width, int height) {             
        super(xPosition, yPosition, width, height);
    }
    
    @Override
    public void drawBackground() {
        //main background  
        drawRect(0, 0, this.getWidth(), this.getHeight(), this.getEnabledBackgroundColor());      

        //title underline
        CustomRectUtils.drawRect(4.0D, 14.0D, this.getWidth() - 4.0D, 14.4D, this.getDisabledBackgroundColor());
    }
}