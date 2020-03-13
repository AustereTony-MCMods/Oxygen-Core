package austeretony.oxygen_core.client.gui.settings.gui.callback;

import org.lwjgl.input.Keyboard;

import austeretony.alternateui.screen.callback.AbstractGUICallback;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.elements.OxygenCallbackBackgroundFiller;
import austeretony.oxygen_core.client.gui.elements.OxygenKeyButton;
import austeretony.oxygen_core.client.gui.elements.OxygenTextLabel;
import austeretony.oxygen_core.client.gui.settings.GUISettingsSection;
import austeretony.oxygen_core.client.gui.settings.SettingsScreen;

public class SetKeyCallback extends AbstractGUICallback {

    private final SettingsScreen screen;

    private final GUISettingsSection section;

    private OxygenKeyButton confirmButton, cancelButton;

    public SetKeyCallback(SettingsScreen screen, GUISettingsSection section, int width, int height) {
        super(screen, section, width, height);
        this.screen = screen;
        this.section = section;
    }

    @Override
    public void init() {
        this.enableDefaultBackground(EnumBaseGUISetting.FILL_CALLBACK_COLOR.get().asInt());
        this.addElement(new OxygenCallbackBackgroundFiller(0, 0, this.getWidth(), this.getHeight()));
        this.addElement(new OxygenTextLabel(4, 5, ClientReference.localize("oxygen_core.gui.settings.callback.setKey"), EnumBaseGUISetting.TEXT_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        this.addElement(this.confirmButton = new OxygenKeyButton(15, this.getHeight() - 10, ClientReference.localize("oxygen_core.gui.confirm"), Keyboard.KEY_R, this::confirm).disable());
        this.addElement(this.cancelButton = new OxygenKeyButton(this.getWidth() - 55, this.getHeight() - 10, ClientReference.localize("oxygen_core.gui.cancel"), Keyboard.KEY_X, this::close));
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element, int mouseButton) {}

    private void confirm() {
        /*this.scaleButton.scaleSetting.setValue(this.newScaleStr);
        this.scaleButton.setDisplayText(this.scaleButton.scaleSetting.getUserValue());*/
        OxygenManagerClient.instance().getClientSettingManager().changed();
        this.confirmButton.disable();
        this.close();
    }

    /*public void open(ScaleButton button) {
        this.scaleButton = button;
        this.open();
    }*/
}
