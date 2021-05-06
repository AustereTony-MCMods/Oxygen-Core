package austeretony.oxygen_core.client.gui.menu;

import austeretony.oxygen_core.client.settings.CoreSettings;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.config.CoreConfig;
import austeretony.oxygen_core.common.main.OxygenMain;
import net.minecraft.util.ResourceLocation;

public class SettingsScreenMenuEntry implements OxygenMenuEntry {

    private static final ResourceLocation ICON = new ResourceLocation(OxygenMain.MOD_ID,
            "textures/gui/menu/settings.png");

    @Override
    public int getScreenId() {
        return OxygenMain.SCREEN_ID_SETTINGS;
    }

    @Override
    public String getDisplayName() {
        return MinecraftClient.localize("oxygen_core.gui.settings.title");
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public ResourceLocation getIconTexture() {
        return ICON;
    }

    @Override
    public int getKeyCode() {
        return CoreConfig.SETTINGS_SCREEN_KEY_ID.asInt();
    }

    @Override
    public boolean isValid() {
        return CoreSettings.ADD_SETTINGS_SCREEN_TO_OXYGEN_MENU.asBoolean();
    }
}
