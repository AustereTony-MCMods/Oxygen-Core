package austeretony.oxygen_core.client.gui.privileges.roles.callback;

import org.lwjgl.input.Keyboard;

import austeretony.alternateui.screen.callback.AbstractGUICallback;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.elements.OxygenButton;
import austeretony.oxygen_core.client.gui.elements.OxygenCallbackBackgroundFiller;
import austeretony.oxygen_core.client.gui.elements.OxygenNumberField;
import austeretony.oxygen_core.client.gui.elements.OxygenTextField;
import austeretony.oxygen_core.client.gui.elements.OxygenTextFormattingColorPicker;
import austeretony.oxygen_core.client.gui.elements.OxygenTextLabel;
import austeretony.oxygen_core.client.gui.privileges.PrivilegesScreen;
import austeretony.oxygen_core.client.gui.privileges.RolesSection;
import austeretony.oxygen_core.common.config.PrivilegesConfig;
import austeretony.oxygen_core.common.privilege.RoleImpl;
import net.minecraft.util.text.TextFormatting;

public class RoleCreationCallback extends AbstractGUICallback {

    private final PrivilegesScreen screen;

    private final RolesSection section;

    private OxygenTextLabel chatFormattingLabel;

    private OxygenTextField roleNameField, prefixField;

    private OxygenNumberField roleIdField;

    private OxygenTextFormattingColorPicker roleNameColorPicker, prefixColorPicker, usernameColorPicker, chatColorPicker;

    private OxygenButton confirmButton, cancelButton;

    public RoleCreationCallback(PrivilegesScreen screen, RolesSection section, int width, int height) {
        super(screen, section, width, height);
        this.screen = screen;
        this.section = section;
    }

    @Override
    public void init() {
        this.enableDefaultBackground(EnumBaseGUISetting.FILL_CALLBACK_COLOR.get().asInt());
        this.addElement(new OxygenCallbackBackgroundFiller(0, 0, this.getWidth(), this.getHeight()));
        this.addElement(new OxygenTextLabel(4, 12, ClientReference.localize("oxygen_core.gui.privileges.callback.roleCreation"), EnumBaseGUISetting.TEXT_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        this.addElement(new OxygenTextLabel(6, 23, ClientReference.localize("oxygen_core.gui.privileges.roleName"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));
        this.addElement(this.roleNameField = new OxygenTextField(6, 25, 80, RoleImpl.ROLE_NAME_MAX_LENGTH, ""));
        this.roleNameField.setInputListener((keyChar, keyCode)->this.confirmButton.setEnabled(!this.roleNameField.getTypedText().isEmpty() && !this.roleIdField.getTypedText().isEmpty()));

        this.addElement(new OxygenTextLabel(6, 42, ClientReference.localize("oxygen_core.gui.privileges.roleId"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));
        this.addElement(this.roleIdField = new OxygenNumberField(6, 44, 30, "", 127, false, 0, true));
        this.roleIdField.setInputListener((keyChar, keyCode)->this.confirmButton.setEnabled(!this.roleNameField.getTypedText().isEmpty() && !this.roleIdField.getTypedText().isEmpty()));

        this.addElement(new OxygenTextLabel(6, 61, ClientReference.localize("oxygen_core.gui.privileges.prefix"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));
        this.addElement(this.prefixField = new OxygenTextField(6, 63, 80, RoleImpl.PREFIX_MAX_LENGTH, ""));

        //role name color
        this.addElement(new OxygenTextLabel(6, 81, ClientReference.localize("oxygen_core.gui.privileges.roleNameColor"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));
        this.addElement(this.roleNameColorPicker = new OxygenTextFormattingColorPicker(6, 83));

        //prefix color
        this.addElement(new OxygenTextLabel(6, 99, ClientReference.localize("oxygen_core.gui.privileges.prefixColor"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));
        this.addElement(this.prefixColorPicker = new OxygenTextFormattingColorPicker(6, 101));
        this.prefixColorPicker.setColorPickListener((index)->this.updateChatFormatting());

        //username color
        this.addElement(new OxygenTextLabel(6, 117, ClientReference.localize("oxygen_core.gui.privileges.usernameColor"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));
        this.addElement(this.usernameColorPicker = new OxygenTextFormattingColorPicker(6, 119));
        this.usernameColorPicker.setColorPickListener((index)->this.updateChatFormatting());

        //chat color
        this.addElement(new OxygenTextLabel(6, 135, ClientReference.localize("oxygen_core.gui.privileges.chatColor"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));
        this.addElement(this.chatColorPicker = new OxygenTextFormattingColorPicker(6, 137));
        this.chatColorPicker.setColorPickListener((index)->this.updateChatFormatting());

        //formatting example
        this.addElement(new OxygenTextLabel(6, 154, ClientReference.localize("oxygen_core.gui.privileges.chatFormatting"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() + 0.05F, EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));
        this.addElement(this.chatFormattingLabel = new OxygenTextLabel(6, 162, "", EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        this.addElement(this.confirmButton = new OxygenButton(15, this.getHeight() - 12, 40, 10, ClientReference.localize("oxygen_core.gui.confirm")).disable());
        this.confirmButton.setKeyPressListener(Keyboard.KEY_R, ()->this.confirm());

        this.addElement(this.cancelButton = new OxygenButton(this.getWidth() - 55, this.getHeight() - 12, 40, 10, ClientReference.localize("oxygen_core.gui.cancel")));
        this.cancelButton.setKeyPressListener(Keyboard.KEY_X, ()->this.close());
    }

    private void updateChatFormatting() {     
        if (PrivilegesConfig.ENABLE_FORMATTED_CHAT.asBoolean()) {
            if (PrivilegesConfig.ENABLE_CUSTOM_FORMATTED_CHAT.asBoolean()) {
                String prefix = null, username, result;

                TextFormatting defColor = TextFormatting.values()[PrivilegesConfig.DEFAULT_CHAT_COLOR.asInt()];

                if (!this.prefixField.getTypedText().isEmpty())
                    prefix = TextFormatting.values()[this.prefixColorPicker.getPickedColorIndex()] + this.prefixField.getTypedText() + TextFormatting.RESET;

                username = TextFormatting.values()[this.usernameColorPicker.getPickedColorIndex()] + ClientReference.getClientPlayer().getName() + TextFormatting.RESET;

                result = !this.prefixField.getTypedText().isEmpty() ? PrivilegesConfig.FORMATTED_CHAT_PREFIX_PATTERN.asString() : PrivilegesConfig.FORMATTED_CHAT_PATTERN.asString();
                if (prefix != null)
                    result = defColor + result.replace("@prefix", prefix + defColor);
                result = result.replace("@username", username + defColor);

                result += TextFormatting.values()[this.chatColorPicker.getPickedColorIndex()] + ClientReference.localize("oxygen_core.gui.privileges.testMessage");

                this.chatFormattingLabel.setDisplayText(result);
            } else {
                StringBuilder formattedUsername = new StringBuilder();

                if (!this.prefixField.getTypedText().isEmpty()) {
                    formattedUsername.append(TextFormatting.values()[this.prefixColorPicker.getPickedColorIndex()]);
                    formattedUsername.append("[");
                    formattedUsername.append(this.prefixField.getTypedText());
                    formattedUsername.append("]");
                    formattedUsername.append(TextFormatting.RESET);
                }

                formattedUsername.append(TextFormatting.values()[this.usernameColorPicker.getPickedColorIndex()]);
                formattedUsername.append(ClientReference.getClientPlayer().getName());
                formattedUsername.append(TextFormatting.RESET);

                this.chatFormattingLabel.setDisplayText(ClientReference.localize("chat.type.text", formattedUsername.toString(), TextFormatting.values()[this.chatColorPicker.getPickedColorIndex()] + ClientReference.localize("oxygen_core.gui.privileges.testMessage")));
            }
        } else
            this.chatFormattingLabel.setDisplayText(ClientReference.localize("chat.type.text", ClientReference.getClientPlayer().getName(), ClientReference.localize("oxygen_core.gui.privileges.testMessage")));
    }

    @Override
    protected void onOpen() {
        this.roleNameField.reset();
        this.roleIdField.reset();
        this.prefixField.reset();
        this.roleNameColorPicker.setPickedColorIndex(TextFormatting.GRAY.ordinal());
        this.prefixColorPicker.setPickedColorIndex(TextFormatting.GRAY.ordinal());
        this.usernameColorPicker.setPickedColorIndex(TextFormatting.GRAY.ordinal());
        this.chatColorPicker.setPickedColorIndex(TextFormatting.GRAY.ordinal());
        this.updateChatFormatting();
    }

    private void confirm() {
        if (!this.roleNameField.isDragged()
                && !this.roleIdField.isDragged()
                && !this.prefixField.isDragged()) {
            OxygenManagerClient.instance().getPrivilegesManager().createRoleSynced(
                    (int) this.roleIdField.getTypedNumberAsLong(), 
                    this.roleNameField.getTypedText(), 
                    this.prefixField.getTypedText(), 
                    this.roleNameColorPicker.getPickedColorIndex(), 
                    this.prefixColorPicker.getPickedColorIndex(), 
                    this.usernameColorPicker.getPickedColorIndex(), 
                    this.chatColorPicker.getPickedColorIndex());
            this.confirmButton.disable();
            this.close();
        }
    }

    @Override
    public void close() {
        if (!this.roleNameField.isDragged()
                && !this.roleIdField.isDragged()
                && !this.prefixField.isDragged())
            super.close();
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
}
