package austeretony.oxygen_core.client.gui.settings.gui.callback;

import org.lwjgl.input.Keyboard;

import austeretony.alternateui.screen.callback.AbstractGUICallback;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.OxygenGUITextures;
import austeretony.oxygen_core.client.gui.elements.OxygenButton;
import austeretony.oxygen_core.client.gui.elements.OxygenCallbackBackgroundFiller;
import austeretony.oxygen_core.client.gui.elements.OxygenNumberField;
import austeretony.oxygen_core.client.gui.elements.OxygenTextLabel;
import austeretony.oxygen_core.client.gui.elements.OxygenTexturedButton;
import austeretony.oxygen_core.client.gui.settings.GUISettingsSection;
import austeretony.oxygen_core.client.gui.settings.SettingsScreen;
import austeretony.oxygen_core.client.gui.settings.gui.OffsetButton;

public class SetOffsetCallback extends AbstractGUICallback {

    private final SettingsScreen screen;

    private final GUISettingsSection section;

    private OxygenNumberField offsetField;

    private OxygenTexturedButton applyButton, resetButton;

    private OxygenButton confirmButton, cancelButton;

    //cache

    private OffsetButton offsetButton;

    private String newOffsetStr;

    public SetOffsetCallback(SettingsScreen screen, GUISettingsSection section, int width, int height) {
        super(screen, section, width, height);
        this.screen = screen;
        this.section = section;
    }

    @Override
    public void init() {
        this.enableDefaultBackground(EnumBaseGUISetting.FILL_CALLBACK_COLOR.get().asInt());
        this.addElement(new OxygenCallbackBackgroundFiller(0, 0, this.getWidth(), this.getHeight()));
        this.addElement(new OxygenTextLabel(4, 12, ClientReference.localize("oxygen_core.gui.settings.callback.setOffset"), EnumBaseGUISetting.TEXT_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));
        this.addElement(new OxygenTextLabel(6, 23, ClientReference.localize("oxygen_core.gui.settings.offset"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        this.addElement(this.offsetField = new OxygenNumberField(6, 25, 30, "", 200L, false, 0, false));

        this.addElement(this.applyButton = new OxygenTexturedButton(40, 27, 5, 5, OxygenGUITextures.CHECK_ICONS, 5, 5, ClientReference.localize("oxygen_core.gui.apply")));         
        this.applyButton.setClickListener((mouseX, mouseY, mouseButton)->this.applyOffset());
        this.addElement(this.resetButton = new OxygenTexturedButton(48, 27, 5, 5, OxygenGUITextures.CROSS_ICONS, 5, 5, ClientReference.localize("oxygen_core.gui.reset")));         
        this.resetButton.setClickListener((mouseX, mouseY, mouseButton)->this.resetOffset());

        this.addElement(this.confirmButton = new OxygenButton(15, this.getHeight() - 12, 40, 10, ClientReference.localize("oxygen_core.gui.confirm")).disable());
        this.confirmButton.setKeyPressListener(Keyboard.KEY_R, ()->this.confirm());

        this.addElement(this.cancelButton = new OxygenButton(this.getWidth() - 55, this.getHeight() - 12, 40, 10, ClientReference.localize("oxygen_core.gui.cancel")));
        this.cancelButton.setKeyPressListener(Keyboard.KEY_X, ()->this.close());
    }

    private void applyOffset() {
        this.newOffsetStr = this.offsetField.getTypedText();
        this.confirmButton.enable();
    }

    private void resetOffset() {
        this.newOffsetStr = this.offsetButton.offsetSetting.getUserValue();
        this.offsetField.setText(this.offsetButton.offsetSetting.getUserValue());
        this.confirmButton.enable();
    }

    @Override
    protected void onOpen() {
        this.offsetField.setText(this.offsetButton.offsetSetting.getUserValue());
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element, int mouseButton) {
        if (mouseButton == 0) { 
            if (element == this.cancelButton)
                this.close();
            else if (element == this.confirmButton)
                this.confirm();
        }
    }

    private void confirm() {
        this.offsetButton.offsetSetting.setValue(this.newOffsetStr);
        this.offsetButton.setDisplayText(this.offsetButton.offsetSetting.getUserValue());
        OxygenManagerClient.instance().getClientSettingManager().changed();
        this.confirmButton.disable();
        this.close();
    }

    public void open(OffsetButton button) {
        this.offsetButton = button;
        this.open();
    }
}
