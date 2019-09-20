package austeretony.oxygen_core.client.gui.notifications;

import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.gui.menu.AbstractMenuEntry;

public class NotificationsMenuEntry extends AbstractMenuEntry {

    @Override
    public String getName() {
        return "oxygen.gui.notifications.title";
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void open() {
        ClientReference.displayGuiScreen(new NotificationsGUIScreen());
    }
}