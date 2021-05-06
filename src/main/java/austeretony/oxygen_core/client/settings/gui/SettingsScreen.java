package austeretony.oxygen_core.client.settings.gui;

import austeretony.oxygen_core.client.gui.base.Alignment;
import austeretony.oxygen_core.client.gui.base.core.OxygenScreen;
import austeretony.oxygen_core.client.gui.base.core.Section;
import austeretony.oxygen_core.client.gui.base.core.Workspace;
import austeretony.oxygen_core.client.gui.menu.OxygenMenuEntry;
import austeretony.oxygen_core.client.gui.menu.SettingsScreenMenuEntry;
import austeretony.oxygen_core.client.settings.CoreSettings;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.main.OxygenMain;

public class SettingsScreen extends OxygenScreen {

    public static final OxygenMenuEntry SETTINGS_SCREEN_ENTRY = new SettingsScreenMenuEntry();
    public static final String MAIN_MODULE_NAME = "Core";

    private Section commonSection;

    @Override
    public int getScreenId() {
        return OxygenMain.SCREEN_ID_SETTINGS;
    }

    @Override
    public Workspace createWorkspace() {
        Workspace workspace = new Workspace(this, 200, 215);
        workspace.setAlignment(Alignment.valueOf(CoreSettings.ALIGNMENT_SETTINGS_SCREEN.asString()), 0, 0);
        return workspace;
    }

    @Override
    public void addSections() {
        getWorkspace().addSection(commonSection = new CommonSettingsSection(this));
        getWorkspace().addSection(new InterfaceSettingsSection(this));
    }

    @Override
    public Section getDefaultSection() {
        return commonSection;
    }

    public static void open() {
        MinecraftClient.displayGuiScreen(new SettingsScreen());
    }
}

