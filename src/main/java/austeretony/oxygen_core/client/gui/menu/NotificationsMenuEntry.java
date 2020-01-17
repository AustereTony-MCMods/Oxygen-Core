package austeretony.oxygen_core.client.gui.menu;

import org.lwjgl.input.Keyboard;

import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.gui.notifications.NotificationsScreen;
import austeretony.oxygen_core.client.settings.EnumCoreClientSetting;

public class NotificationsMenuEntry implements OxygenMenuEntry {

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getLocalizedName() {
        return ClientReference.localize("oxygen_core.gui.notifications.title");
    }

    @Override
    public int getKeyCode() {
        return Keyboard.KEY_N;
    }

    @Override
    public boolean isValid() {
        return EnumCoreClientSetting.ADD_NOTIFICATIONS_MENU.get().asBoolean();
    }

    @Override
    public void open() {
        ClientReference.displayGuiScreen(new NotificationsScreen());
    }
}
