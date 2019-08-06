package austeretony.oxygen.client.gui.notifications;

import austeretony.oxygen.client.gui.BackgroundGUIFiller;
import austeretony.oxygen.client.gui.settings.GUISettings;

public class NotificationsGUIFiller extends BackgroundGUIFiller {

    public NotificationsGUIFiller(int xPosition, int yPosition, int width, int height) {             
        super(xPosition, yPosition, width, height, NotificationsGUIScreen.NOTIFICATIONS_MENU_BACKGROUND);
    }

    @Override
    public void drawDefaultBackground() {
        drawRect(- 1, - 1, this.getWidth() + 1, this.getHeight() + 1, GUISettings.instance().getBaseGUIBackgroundColor());//main background
        drawRect(0, 0, this.getWidth(), 13, GUISettings.instance().getAdditionalGUIBackgroundColor());//title background
        drawRect(0, 14, this.getWidth() - 3, this.getHeight(), GUISettings.instance().getPanelGUIBackgroundColor());//panel background
        drawRect(this.getWidth() - 2, 14, this.getWidth(), this.getHeight(), GUISettings.instance().getAdditionalGUIBackgroundColor());//slider background
    }
}
