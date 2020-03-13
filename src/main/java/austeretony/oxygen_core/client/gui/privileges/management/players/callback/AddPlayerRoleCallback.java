package austeretony.oxygen_core.client.gui.privileges.management.players.callback;

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
import austeretony.oxygen_core.client.gui.elements.OxygenPanelEntry;
import austeretony.oxygen_core.client.gui.elements.OxygenScrollablePanel;
import austeretony.oxygen_core.client.gui.elements.OxygenTextLabel;
import austeretony.oxygen_core.client.gui.privileges.management.PlayersSection;
import austeretony.oxygen_core.client.gui.privileges.management.PrivilegesManagementScreen;
import austeretony.oxygen_core.common.privilege.Role;
import austeretony.oxygen_core.common.util.MathUtils;

public class AddPlayerRoleCallback extends AbstractGUICallback {

    private final PrivilegesManagementScreen screen;

    private final PlayersSection section;

    private OxygenScrollablePanel rolesPanel;

    private OxygenKeyButton confirmButton, cancelButton;

    //cache

    private OxygenPanelEntry<Integer> currRoleEntry;

    public AddPlayerRoleCallback(PrivilegesManagementScreen screen, PlayersSection section, int width, int height) {
        super(screen, section, width, height);
        this.screen = screen;
        this.section = section;
    }

    @Override
    public void init() {
        this.enableDefaultBackground(EnumBaseGUISetting.FILL_CALLBACK_COLOR.get().asInt());
        this.addElement(new OxygenCallbackBackgroundFiller(0, 0, this.getWidth(), this.getHeight()));
        this.addElement(new OxygenTextLabel(4, 12, ClientReference.localize("oxygen_core.gui.privileges.management.callback.addPlayerRole"), EnumBaseGUISetting.TEXT_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        this.addElement(this.rolesPanel = new OxygenScrollablePanel(this.screen, 6, 16, this.getWidth() - 15, 10, 1, 140, 7, EnumBaseGUISetting.TEXT_PANEL_SCALE.get().asFloat(), false));
        this.loadRoles();

        this.rolesPanel.<OxygenPanelEntry<Integer>>setElementClickListener((previous, clicked, mouseX, mouseY, mouseButton)->{
            if (previous != null)
                previous.setToggled(false);
            clicked.toggle();
            this.currRoleEntry = clicked;
            this.confirmButton.enable();
        });

        this.addElement(this.confirmButton = new OxygenKeyButton(15, this.getHeight() - 10, ClientReference.localize("oxygen_core.gui.confirm"), Keyboard.KEY_R, this::confirm).disable());
        this.addElement(this.cancelButton = new OxygenKeyButton(this.getWidth() - 55, this.getHeight() - 10, ClientReference.localize("oxygen_core.gui.cancel"), Keyboard.KEY_X, this::close));
    }

    private void loadRoles() {
        List<Role> roles = new ArrayList<>(OxygenManagerClient.instance().getPrivilegesContainer().getServerRoles());

        Collections.sort(roles, (r1, r2)->r2.getId() - r1.getId());

        this.rolesPanel.reset();

        for (Role role : roles)
            this.rolesPanel.addEntry(new OxygenPanelEntry<Integer>(role.getId(), String.format("%s [%s]", role.getNameColor() + role.getName(), role.getId()), true));

        this.rolesPanel.getScroller().reset();
        this.rolesPanel.getScroller().updateRowsAmount(MathUtils.clamp(roles.size(), 7, 140));
    }

    private void confirm() {
        OxygenManagerClient.instance().getPrivilegesManager().addRoleToPlayerSynced(this.section.getCurrentPlayerEntry().getWrapped(), this.currRoleEntry.getWrapped());
        this.currRoleEntry.setToggled(false);
        this.confirmButton.disable();
        this.close();
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
