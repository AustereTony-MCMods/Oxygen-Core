package austeretony.oxygen_core.client.gui.menu;

import org.lwjgl.input.Keyboard;

import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.gui.privileges.PrivilegesScreen;
import austeretony.oxygen_core.client.settings.EnumCoreClientSetting;

public class PrivilegesMenuEntry implements OxygenMenuEntry {

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public String getLocalizedName() {
        return ClientReference.localize("oxygen_core.gui.privileges.title");
    }

    @Override
    public int getKeyCode() {
        return Keyboard.KEY_R;
    }

    @Override
    public boolean isValid() {
        return EnumCoreClientSetting.ADD_PRIVILEGES_MENU.get().asBoolean();
    }

    @Override
    public void open() {
        ClientReference.displayGuiScreen(new PrivilegesScreen());
    }
}
