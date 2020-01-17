package austeretony.oxygen_core.client.gui.menu;

import org.lwjgl.input.Keyboard;

import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.gui.settings.SettingsScreen;
import austeretony.oxygen_core.client.settings.EnumCoreClientSetting;

public class SettingsMenuEntry implements OxygenMenuEntry {

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public String getLocalizedName() {
        return ClientReference.localize("oxygen_core.gui.settings.title");
    }

    @Override
    public int getKeyCode() {
        return Keyboard.KEY_A;
    }

    @Override
    public boolean isValid() {
        return EnumCoreClientSetting.ADD_SETTINGS_MENU.get().asBoolean();
    }

    @Override
    public void open() {
        ClientReference.displayGuiScreen(new SettingsScreen());
    }
}
