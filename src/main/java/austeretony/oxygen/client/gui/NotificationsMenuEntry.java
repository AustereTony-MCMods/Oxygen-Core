package austeretony.oxygen.client.gui;

import austeretony.oxygen.client.core.api.ClientReference;
import austeretony.oxygen.client.gui.notifications.NotificationsGUIScreen;
import net.minecraft.util.ResourceLocation;

public class NotificationsMenuEntry extends AbstractMenuEntry {

    @Override
    public String getName() {
        return "oxygen.gui.notifications.title";
    }

    @Override
    public ResourceLocation getIcon() {
        //TODO
        return null;
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