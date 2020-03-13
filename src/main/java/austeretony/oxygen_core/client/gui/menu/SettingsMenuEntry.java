package austeretony.oxygen_core.client.gui.menu;

import austeretony.oxygen_core.client.OxygenGUIManager;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.settings.EnumCoreClientSetting;
import austeretony.oxygen_core.common.config.OxygenConfig;
import austeretony.oxygen_core.common.main.OxygenMain;

public class SettingsMenuEntry implements OxygenMenuEntry {

    @Override
    public int getId() {
        return OxygenMain.SETTINGS_SCREEN_ID;
    }

    @Override
    public String getLocalizedName() {
        return ClientReference.localize("oxygen_core.gui.settings.title");
    }

    @Override
    public int getKeyCode() {
        return OxygenConfig.SETTINGS_MENU_KEY.asInt();
    }

    @Override
    public boolean isValid() {
        return EnumCoreClientSetting.ADD_SETTINGS_MENU.get().asBoolean();
    }

    @Override
    public void open() {
        OxygenGUIManager.openSettingsMenu();
    }
}
