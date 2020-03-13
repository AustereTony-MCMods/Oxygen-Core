package austeretony.oxygen_core.client.gui.privileges.management.roles.callback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.input.Keyboard;

import austeretony.alternateui.screen.callback.AbstractGUICallback;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.elements.OxygenCallbackBackgroundFiller;
import austeretony.oxygen_core.client.gui.elements.OxygenKeyButton;
import austeretony.oxygen_core.client.gui.elements.OxygenNumberField;
import austeretony.oxygen_core.client.gui.elements.OxygenScrollablePanel;
import austeretony.oxygen_core.client.gui.elements.OxygenTextField;
import austeretony.oxygen_core.client.gui.elements.OxygenTextLabel;
import austeretony.oxygen_core.client.gui.privileges.management.PrivilegesManagementScreen;
import austeretony.oxygen_core.client.gui.privileges.management.RolesSection;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.privilege.PrivilegeRegistry;
import austeretony.oxygen_core.common.privilege.PrivilegeRegistry.PrivilegeRegistryEntry;
import austeretony.oxygen_core.common.util.MathUtils;

public class AddPrivilegeCallback extends AbstractGUICallback {

    private final PrivilegesManagementScreen screen;

    private final RolesSection section;

    private OxygenNumberField privilegeIdField;

    private OxygenTextField valueField;

    private OxygenScrollablePanel privilegesPanel;

    private OxygenKeyButton confirmButton, cancelButton;

    //cache

    private PrivilegePanelEntry currPrivilegeEntry;

    private String privilegeValue;

    public AddPrivilegeCallback(PrivilegesManagementScreen screen, RolesSection section, int width, int height) {
        super(screen, section, width, height);
        this.screen = screen;
        this.section = section;
    }

    @Override
    public void init() {
        this.enableDefaultBackground(EnumBaseGUISetting.FILL_CALLBACK_COLOR.get().asInt());
        this.addElement(new OxygenCallbackBackgroundFiller(0, 0, this.getWidth(), this.getHeight()));
        this.addElement(new OxygenTextLabel(4, 12, ClientReference.localize("oxygen_core.gui.privileges.management.callback.addPrivilege"), EnumBaseGUISetting.TEXT_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        this.addElement(new OxygenTextLabel(6, 23, ClientReference.localize("oxygen_core.gui.privileges.privileges"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() + 0.05F, EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));
        this.addElement(this.privilegesPanel = new OxygenScrollablePanel(this.screen, 6, 26, this.getWidth() - 15, 10, 1, 1400, 7, EnumBaseGUISetting.TEXT_PANEL_SCALE.get().asFloat(), false));
        this.loadPrivileges();

        this.privilegesPanel.<PrivilegePanelEntry>setElementClickListener((previous, clicked, mouseX, mouseY, mouseButton)->{
            if (previous != null)
                previous.setToggled(false);
            clicked.toggle();
            this.currPrivilegeEntry = clicked;

            this.confirmButton.setEnabled(!this.valueField.getTypedText().isEmpty());
        });

        this.addElement(new OxygenTextLabel(6, 112, ClientReference.localize("oxygen_core.gui.privileges.management.callback.privilegeId"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));
        this.addElement(this.privilegeIdField = new OxygenNumberField(6, 113, 50, "", Short.MAX_VALUE, false, 0, true));
        this.privilegeIdField.setInputListener((keyChar, keyCode)->this.confirmButton.setEnabled(!this.valueField.getTypedText().isEmpty()));

        this.addElement(new OxygenTextLabel(6, 131, ClientReference.localize("oxygen_core.gui.privileges.management.callback.value"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));
        this.addElement(this.valueField = new OxygenTextField(6, 132, 50, 10, ""));
        this.valueField.setInputListener((keyChar, keyCode)->this.validateInputValue());

        this.addElement(this.confirmButton = new OxygenKeyButton(15, this.getHeight() - 10, ClientReference.localize("oxygen_core.gui.confirm"), Keyboard.KEY_R, this::confirm).disable());
        this.addElement(this.cancelButton = new OxygenKeyButton(this.getWidth() - 55, this.getHeight() - 10, ClientReference.localize("oxygen_core.gui.cancel"), Keyboard.KEY_X, this::close));
    }

    private void loadPrivileges() {
        List<PrivilegeRegistryEntry> entries = new ArrayList<>(PrivilegeRegistry.getRegisteredPrivileges());

        Collections.sort(entries, (e1, e2)->e1.id - e2.id);

        this.privilegesPanel.reset();
        for (PrivilegeRegistryEntry entry : entries)
            this.privilegesPanel.addEntry(new PrivilegePanelEntry(entry));

        this.privilegesPanel.getScroller().reset();
        this.privilegesPanel.getScroller().updateRowsAmount(MathUtils.clamp(entries.size(), 7, 1400));
    }

    private void validateInputValue() {
        PrivilegeRegistryEntry entry = this.currPrivilegeEntry != null ? this.currPrivilegeEntry.getWrapped() : PrivilegeRegistry.getRegistryEntry((int) this.privilegeIdField.getTypedNumberAsLong());
        if (entry != null) {
            this.privilegeValue = this.valueField.getTypedText();
            boolean valid = false;
            switch (entry.type) {            
            case INT:
                try {
                    int value = Integer.parseInt(this.privilegeValue);
                    valid = true;
                } catch (NumberFormatException exception) {
                    OxygenMain.LOGGER.error("[Core] Wrong privilege integer value!");
                }
                break;
            case FLOAT:
                try {
                    float value = Float.parseFloat(this.privilegeValue);
                    valid = true;
                } catch (NumberFormatException exception) {
                    OxygenMain.LOGGER.error("[Core] Wrong privilege float value!");
                }
                break;
            case LONG:
                try {
                    long value = Long.parseLong(this.privilegeValue);
                    valid = true;
                } catch (NumberFormatException exception) {
                    OxygenMain.LOGGER.error("[Core] Wrong privilege long value!");
                }
                break;
            default:
                valid = true;
                break;
            }
            this.confirmButton.setEnabled(valid);
        }
    }

    @Override
    protected void onOpen() {
        if (this.currPrivilegeEntry != null)
            this.currPrivilegeEntry.setToggled(false);
        this.currPrivilegeEntry = null;
        this.privilegeIdField.reset();
        this.valueField.reset();

        this.confirmButton.disable();
    }

    private void confirm() {
        if (!this.valueField.isDragged()) {
            OxygenManagerClient.instance().getPrivilegesManager().addPrivilegeSynced(
                    this.section.getCurrentRoleEntry().getWrapped().getId(), 
                    this.privilegeIdField.getTypedText().isEmpty() ? this.currPrivilegeEntry.getWrapped().id : (int) this.privilegeIdField.getTypedNumberAsLong(), 
                            this.privilegeValue);
            this.confirmButton.disable();
            this.close();
        }
    }

    @Override
    public void close() {
        if (!this.valueField.isDragged())
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
