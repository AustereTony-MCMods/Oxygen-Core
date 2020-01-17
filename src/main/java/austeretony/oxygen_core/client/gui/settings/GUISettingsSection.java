package austeretony.oxygen_core.client.gui.settings;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import austeretony.alternateui.screen.callback.AbstractGUICallback;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.framework.GUIElementsFramework;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.elements.OxygenButton;
import austeretony.oxygen_core.client.gui.elements.OxygenPanelEntry;
import austeretony.oxygen_core.client.gui.elements.OxygenScrollablePanel;
import austeretony.oxygen_core.client.gui.elements.OxygenSectionSwitcher;
import austeretony.oxygen_core.client.gui.elements.OxygenTextLabel;
import austeretony.oxygen_core.client.gui.settings.gui.callback.SetColorCallback;
import austeretony.oxygen_core.client.gui.settings.gui.callback.SetKeyCallback;
import austeretony.oxygen_core.client.gui.settings.gui.callback.SetOffsetCallback;
import austeretony.oxygen_core.client.gui.settings.gui.callback.SetScaleCallback;
import austeretony.oxygen_core.common.util.MathUtils;

public class GUISettingsSection extends AbstractGUISection {

    private SettingsScreen screen; 

    private OxygenScrollablePanel sectionsPanel;

    private OxygenButton resetButton;

    private List<GUIElementsFramework> frameworks = new ArrayList<>(10);

    private AbstractGUICallback setColorCallback, setScaleCallback, setOffsetCallback, setKeyCallback;

    //cache

    private int currIndex = - 1;

    public GUISettingsSection(SettingsScreen screen) {
        super(screen);
        this.screen = screen;
        this.setDisplayText(ClientReference.localize("oxygen_core.gui.settings.gui"));
    }

    @Override
    public void init() {
        this.addElement(new SettingsSectionBackgroundFiller(0, 0, this.getWidth(), this.getHeight()));
        this.addElement(new OxygenTextLabel(4, 12, ClientReference.localize("oxygen_core.gui.settings.title"), EnumBaseGUISetting.TEXT_TITLE_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        this.addElement(this.sectionsPanel = new OxygenScrollablePanel(this.screen, 6, 16, 60, 10, 1, MathUtils.greaterOfTwo(10, SettingsScreen.CONTAINERS.size()), 10, EnumBaseGUISetting.TEXT_SCALE.get().asFloat(), false));   

        this.sectionsPanel.<OxygenPanelEntry<Integer>>setClickListener((previous, clicked, mouseX, mouseY, mouseButton)->{
            if (previous != clicked) {
                if (previous != null)
                    previous.setToggled(false);
                clicked.toggle();                    
                this.currIndex = clicked.index;
                this.showSettings(previous == null ? - 1 : previous.index, clicked.index);
                this.resetButton.enable();
            }
        });

        this.setColorCallback = new SetColorCallback(this.screen, this, 140, 50);
        this.setScaleCallback = new SetScaleCallback(this.screen, this, 140, 50);
        this.setOffsetCallback = new SetOffsetCallback(this.screen, this, 140, 50);
        this.setKeyCallback = new SetKeyCallback(this.screen, this, 140, 50);

        GUIElementsFramework framework;
        int i = 0;
        for (ElementsContainer container : SettingsScreen.CONTAINERS) {
            if (!container.hasGUISettings()) continue;
            this.sectionsPanel.addEntry(new OxygenPanelEntry(i++, container.getLocalizedName(), false));

            framework = new GUIElementsFramework(this.getScreen(), 68, 16, 126, 196);
            this.addElement(framework);
            this.frameworks.add(framework);
            container.addGUI(framework);
            container.initSetColorCallback((SetColorCallback) this.setColorCallback);
            container.initSetScaleCallback((SetScaleCallback) this.setScaleCallback);
            container.initSetOffsetCallback((SetOffsetCallback) this.setOffsetCallback);
            container.initSetKeyCallback((SetKeyCallback) this.setKeyCallback);
            framework.disableFull();
        }

        this.addElement(this.resetButton = new OxygenButton(4, this.getHeight() - 12, 40, 10, ClientReference.localize("oxygen_core.gui.reset")).disable());     

        this.resetButton.setClickListener((mouseX, mouseY, mouseButton)->this.resetSettings());
        this.resetButton.setKeyPressListener(Keyboard.KEY_X, ()->this.resetSettings());

        this.addElement(new OxygenSectionSwitcher(this.getWidth() - 4, 4, this, this.screen.getCommonSettingsSection()));
    }

    private void showSettings(int prevIndex, int currIndex) {
        if (prevIndex != - 1)
            this.frameworks.get(prevIndex).disableFull();
        this.frameworks.get(currIndex).enableFull();
    }

    private void resetSettings() {
        int i = 0;
        for (ElementsContainer container : SettingsScreen.CONTAINERS)
            if (i++ == this.currIndex)
                container.resetGUI();
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element, int mouseButton) {}
}
