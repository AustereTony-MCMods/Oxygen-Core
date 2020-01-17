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
import austeretony.oxygen_core.client.gui.settings.gui.ScaleButton;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.util.MathUtils;

public class SetScaleCallback extends AbstractGUICallback {

    private final SettingsScreen screen;

    private final GUISettingsSection section;

    private OxygenTextLabel scaleLabel;

    private OxygenNumberField scaleField;

    private OxygenTexturedButton applyButton, resetButton;

    private OxygenButton confirmButton, cancelButton;

    //cache

    private ScaleButton scaleButton;

    private String newScaleStr;

    public SetScaleCallback(SettingsScreen screen, GUISettingsSection section, int width, int height) {
        super(screen, section, width, height);
        this.screen = screen;
        this.section = section;
    }

    @Override   
    public void init() {
        this.enableDefaultBackground(EnumBaseGUISetting.FILL_CALLBACK_COLOR.get().asInt());
        this.addElement(new OxygenCallbackBackgroundFiller(0, 0, this.getWidth(), this.getHeight()));
        this.addElement(new OxygenTextLabel(4, 12, ClientReference.localize("oxygen_core.gui.settings.callback.setScale"), EnumBaseGUISetting.TEXT_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));
        this.addElement(new OxygenTextLabel(6, 23, ClientReference.localize("oxygen_core.gui.settings.elementScale"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        this.addElement(this.scaleLabel = new OxygenTextLabel(6, 33, ClientReference.localize("oxygen_core.gui.settings.scale"), 1.0F, EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));
        this.addElement(this.scaleField = new OxygenNumberField(30, 25, 55, "", 2L, true, 2, true));

        this.addElement(this.applyButton = new OxygenTexturedButton(88, 27, 5, 5, OxygenGUITextures.CHECK_ICONS, 5, 5, ClientReference.localize("oxygen_core.gui.apply")));         
        this.applyButton.setClickListener((mouseX, mouseY, mouseButton)->this.applyColor());

        this.addElement(this.resetButton = new OxygenTexturedButton(96, 27, 5, 5, OxygenGUITextures.CROSS_ICONS, 5, 5, ClientReference.localize("oxygen_core.gui.reset")));         
        this.resetButton.setClickListener((mouseX, mouseY, mouseButton)->this.resetColor());

        this.addElement(this.confirmButton = new OxygenButton(15, this.getHeight() - 12, 40, 10, ClientReference.localize("oxygen_core.gui.confirm")).disable());
        this.confirmButton.setKeyPressListener(Keyboard.KEY_R, ()->this.confirm());

        this.addElement(this.cancelButton = new OxygenButton(this.getWidth() - 55, this.getHeight() - 12, 40, 10, ClientReference.localize("oxygen_core.gui.cancel")));
        this.cancelButton.setKeyPressListener(Keyboard.KEY_X, ()->this.close());
    }

    private void applyColor() {
        this.newScaleStr = this.scaleField.getTypedText();
        float scale = this.scaleButton.scaleSetting.asFloat();
        try {
            scale = Float.parseFloat(this.newScaleStr);
        } catch (NumberFormatException exception) {
            OxygenMain.LOGGER.error("Invalid scale value!", exception);
            this.setWarning(true);
        }   

        this.setWarning(false);
        this.scaleLabel.setTextScale(MathUtils.clamp(scale, 0.2F, 1.6F));
        this.newScaleStr = String.valueOf(this.scaleLabel.getTextScale());
        this.confirmButton.enable();
    }

    private void setWarning(boolean flag) {
        if (flag) {
            this.scaleLabel.setDisplayText("!");
            this.scaleLabel.setEnabledTextColor(EnumBaseGUISetting.INACTIVE_TEXT_COLOR.get().asInt());
            this.scaleLabel.setScale(1.0F);
        } else {
            this.scaleLabel.setDisplayText(ClientReference.localize("oxygen_core.gui.settings.scale"));
            this.scaleLabel.setEnabledTextColor(EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt());
        }
    }

    private void resetColor() {
        this.newScaleStr = this.scaleButton.scaleSetting.getUserValue();
        this.scaleField.setText(this.scaleButton.scaleSetting.getUserValue());
        this.scaleLabel.setTextScale(this.scaleButton.scaleSetting.asFloat());
        this.confirmButton.enable();
    }

    @Override
    protected void onOpen() {
        this.scaleLabel.setTextScale(this.scaleButton.scaleSetting.asFloat());
        this.scaleField.setText(this.scaleButton.scaleSetting.getUserValue());
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
        this.scaleButton.scaleSetting.setValue(this.newScaleStr);
        this.scaleButton.setDisplayText(this.scaleButton.scaleSetting.getUserValue());
        OxygenManagerClient.instance().getClientSettingManager().changed();
        this.confirmButton.disable();
        this.close();
    }

    public void open(ScaleButton button) {
        this.scaleButton = button;
        this.open();
    }
}
