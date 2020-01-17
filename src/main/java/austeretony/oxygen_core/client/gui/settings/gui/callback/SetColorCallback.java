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
import austeretony.oxygen_core.client.gui.elements.OxygenTextField;
import austeretony.oxygen_core.client.gui.elements.OxygenTextLabel;
import austeretony.oxygen_core.client.gui.elements.OxygenTexturedButton;
import austeretony.oxygen_core.client.gui.settings.GUISettingsSection;
import austeretony.oxygen_core.client.gui.settings.SettingsScreen;
import austeretony.oxygen_core.client.gui.settings.gui.ColorButton;
import austeretony.oxygen_core.client.gui.settings.gui.ColorFrame;
import austeretony.oxygen_core.common.main.OxygenMain;

public class SetColorCallback extends AbstractGUICallback {

    private final SettingsScreen screen;

    private final GUISettingsSection section;

    private OxygenTextField colorARGBField;

    private OxygenTexturedButton applyButton, resetButton;

    private OxygenButton confirmButton, cancelButton;

    private ColorFrame colorFrame;

    //cache

    private ColorButton colorButton;

    private String newColorStr;

    public SetColorCallback(SettingsScreen screen, GUISettingsSection section, int width, int height) {
        super(screen, section, width, height);
        this.screen = screen;
        this.section = section;
    }

    @Override   
    public void init() {
        this.enableDefaultBackground(EnumBaseGUISetting.FILL_CALLBACK_COLOR.get().asInt());
        this.addElement(new OxygenCallbackBackgroundFiller(0, 0, this.getWidth(), this.getHeight()));
        this.addElement(new OxygenTextLabel(4, 11, ClientReference.localize("oxygen_core.gui.settings.callback.setColor"), EnumBaseGUISetting.TEXT_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));
        this.addElement(new OxygenTextLabel(6, 23, ClientReference.localize("oxygen_core.gui.settings.argbColor"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        this.addElement(this.colorFrame = new ColorFrame(6, 25, 0xFFFFFFFF));
        this.addElement(this.colorARGBField = new OxygenTextField(18, 25, 55, 8, ""));

        this.addElement(this.applyButton = new OxygenTexturedButton(78, 27, 5, 5, OxygenGUITextures.CHECK_ICONS, 5, 5, ClientReference.localize("oxygen_core.gui.apply")));         
        this.applyButton.setClickListener((mouseX, mouseY, mouseButton)->this.applyColor());

        this.addElement(this.resetButton = new OxygenTexturedButton(86, 27, 5, 5, OxygenGUITextures.CROSS_ICONS, 5, 5, ClientReference.localize("oxygen_core.gui.reset")));         
        this.resetButton.setClickListener((mouseX, mouseY, mouseButton)->this.resetColor());

        this.addElement(this.confirmButton = new OxygenButton(15, this.getHeight() - 12, 40, 10, ClientReference.localize("oxygen_core.gui.confirm")).disable());
        this.confirmButton.setKeyPressListener(Keyboard.KEY_R, ()->this.confirm());

        this.addElement(this.cancelButton = new OxygenButton(this.getWidth() - 55, this.getHeight() - 12, 40, 10, ClientReference.localize("oxygen_core.gui.cancel")));
        this.cancelButton.setKeyPressListener(Keyboard.KEY_X, ()->this.close());
    }

    private void applyColor() {
        this.newColorStr = this.colorARGBField.getTypedText();
        try {
            this.colorFrame.setWarning(false);
            this.colorFrame.setColor((int) Long.parseLong(this.newColorStr, 16));
            this.confirmButton.enable();

        } catch (NumberFormatException exception) {
            OxygenMain.LOGGER.error("Invalid color value!", exception);
            this.colorFrame.setWarning(true);
        }
    }

    private void resetColor() {
        this.newColorStr = this.colorButton.colorSetting.getUserValue();
        this.colorARGBField.setText(this.colorButton.colorSetting.getUserValue());
        this.colorFrame.setColor(this.colorButton.colorSetting.asInt());
        this.confirmButton.enable();
    }

    @Override
    protected void onOpen() {
        this.colorFrame.setColor(this.colorButton.colorSetting.asInt());
        this.colorARGBField.setText(this.colorButton.colorSetting.getUserValue());
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
        this.colorButton.colorSetting.setValue(this.newColorStr);
        this.colorButton.setButtonColor(this.colorButton.colorSetting.asInt());
        OxygenManagerClient.instance().getClientSettingManager().changed();
        this.confirmButton.disable();
        this.close();
    }

    public void open(ColorButton button) {
        this.colorButton = button;
        this.open();
    }
}
