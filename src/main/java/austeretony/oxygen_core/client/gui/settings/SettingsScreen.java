package austeretony.oxygen_core.client.gui.settings;

import java.util.Set;
import java.util.TreeSet;

import austeretony.alternateui.screen.core.AbstractGUIScreen;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.core.GUIWorkspace;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen_core.client.gui.menu.OxygenMenuEntry;
import austeretony.oxygen_core.client.gui.menu.SettingsMenuEntry;
import austeretony.oxygen_core.client.settings.gui.EnumCoreGUISetting;

public class SettingsScreen extends AbstractGUIScreen {

    public static final OxygenMenuEntry SETTINGS_MENU_ENTRY = new SettingsMenuEntry();

    public static final Set<ElementsContainer> CONTAINERS = new TreeSet<>((e1, e2)->e1.getLocalizedName().compareTo(e2.getLocalizedName()));

    protected CommonSettingsSection commonSettingsSection;

    protected GUISettingsSection guiSettingsSection;

    @Override
    protected GUIWorkspace initWorkspace() {
        EnumGUIAlignment alignment = EnumGUIAlignment.CENTER;
        switch (EnumCoreGUISetting.SETTINGS_MENU_ALIGNMENT.get().asInt()) {
        case - 1: 
            alignment = EnumGUIAlignment.LEFT;
            break;
        case 0:
            alignment = EnumGUIAlignment.CENTER;
            break;
        case 1:
            alignment = EnumGUIAlignment.RIGHT;
            break;    
        default:
            alignment = EnumGUIAlignment.CENTER;
            break;
        }
        return new GUIWorkspace(this, 200, 228).setAlignment(alignment, 0, 0);
    }

    @Override
    protected void initSections() {
        this.getWorkspace().initSection(this.commonSettingsSection = (CommonSettingsSection) new CommonSettingsSection(this).enable());        
        this.getWorkspace().initSection(this.guiSettingsSection = (GUISettingsSection) new GUISettingsSection(this).enable());        
    }

    @Override
    protected AbstractGUISection getDefaultSection() {
        return this.commonSettingsSection;
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element) {}

    @Override
    protected boolean doesGUIPauseGame() {
        return false;
    }

    public CommonSettingsSection getCommonSettingsSection() {
        return this.commonSettingsSection;
    }

    public GUISettingsSection getGUISettingsSection() {
        return this.guiSettingsSection;
    }

    public static void registerSettingsContainer(ElementsContainer container) {
        CONTAINERS.add(container);
    }
}
