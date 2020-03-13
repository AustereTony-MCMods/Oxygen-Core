package austeretony.oxygen_core.client.gui.menu;

import austeretony.oxygen_core.client.OxygenGUIManager;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.settings.EnumCoreClientSetting;
import austeretony.oxygen_core.common.config.OxygenConfig;
import austeretony.oxygen_core.common.main.OxygenMain;

public class PrivilegesMenuEntry implements OxygenMenuEntry {

    @Override
    public int getId() {
        return OxygenMain.PRIVILEGES_SCREEN_ID;
    }

    @Override
    public String getLocalizedName() {
        return ClientReference.localize("oxygen_core.gui.privileges.privileges");
    }

    @Override
    public int getKeyCode() {
        return OxygenConfig.PRIVILEGES_MENU_KEY.asInt();
    }

    @Override
    public boolean isValid() {
        return EnumCoreClientSetting.ADD_PRIVILEGES_MENU.get().asBoolean();
    }

    @Override
    public void open() {
        OxygenGUIManager.openPrivilegesMenu();
    }
}
