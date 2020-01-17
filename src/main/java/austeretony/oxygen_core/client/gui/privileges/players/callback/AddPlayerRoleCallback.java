package austeretony.oxygen_core.client.gui.privileges.players.callback;

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
import austeretony.oxygen_core.client.gui.elements.OxygenButton;
import austeretony.oxygen_core.client.gui.elements.OxygenCallbackBackgroundFiller;
import austeretony.oxygen_core.client.gui.elements.OxygenPanelEntry;
import austeretony.oxygen_core.client.gui.elements.OxygenScrollablePanel;
import austeretony.oxygen_core.client.gui.elements.OxygenTextLabel;
import austeretony.oxygen_core.client.gui.privileges.PlayersSection;
import austeretony.oxygen_core.client.gui.privileges.PrivilegesScreen;
import austeretony.oxygen_core.common.privilege.Role;
import austeretony.oxygen_core.common.util.MathUtils;

public class AddPlayerRoleCallback extends AbstractGUICallback {

    private final PrivilegesScreen screen;

    private final PlayersSection section;

    private OxygenScrollablePanel rolesPanel;

    private OxygenButton confirmButton, cancelButton;

    //cache

    private OxygenPanelEntry<Integer> currRoleEntry;

    public AddPlayerRoleCallback(PrivilegesScreen screen, PlayersSection section, int width, int height) {
        super(screen, section, width, height);
        this.screen = screen;
        this.section = section;
    }

    @Override
    public void init() {
        this.enableDefaultBackground(EnumBaseGUISetting.FILL_CALLBACK_COLOR.get().asInt());
        this.addElement(new OxygenCallbackBackgroundFiller(0, 0, this.getWidth(), this.getHeight()));
        this.addElement(new OxygenTextLabel(4, 12, ClientReference.localize("oxygen_core.gui.privileges.callback.addPlayerRole"), EnumBaseGUISetting.TEXT_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        this.addElement(new OxygenTextLabel(6, 23, ClientReference.localize("oxygen_core.gui.privileges.roles"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() + 0.05F, EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));
        this.addElement(this.rolesPanel = new OxygenScrollablePanel(this.screen, 6, 26, this.getWidth() - 15, 10, 1, 126, 7, EnumBaseGUISetting.TEXT_PANEL_SCALE.get().asFloat(), false));
        this.loadRoles();

        this.rolesPanel.<OxygenPanelEntry<Integer>>setClickListener((previous, clicked, mouseX, mouseY, mouseButton)->{
            if (previous != null)
                previous.setToggled(false);
            clicked.toggle();
            this.currRoleEntry = clicked;
            this.confirmButton.enable();
        });

        this.addElement(this.confirmButton = new OxygenButton(15, this.getHeight() - 12, 40, 10, ClientReference.localize("oxygen_core.gui.confirm")).disable());
        this.confirmButton.setKeyPressListener(Keyboard.KEY_R, ()->this.confirm());

        this.addElement(this.cancelButton = new OxygenButton(this.getWidth() - 55, this.getHeight() - 12, 40, 10, ClientReference.localize("oxygen_core.gui.cancel")));
        this.cancelButton.setKeyPressListener(Keyboard.KEY_X, ()->this.close());
    }

    private void loadRoles() {
        List<Role> roles = new ArrayList<>(OxygenManagerClient.instance().getPrivilegesManager().getRoles());

        Collections.sort(roles, (r1, r2)->r2.getId() - r1.getId());

        this.rolesPanel.reset();

        for (Role role : roles)
            this.rolesPanel.addEntry(new OxygenPanelEntry<Integer>(role.getId(), String.format("%s [%s]", role.getNameColor() + role.getName(), role.getId()), true));

        this.rolesPanel.getScroller().reset();
        this.rolesPanel.getScroller().updateRowsAmount(MathUtils.clamp(roles.size(), 7, 126));
    }

    private void confirm() {
        OxygenManagerClient.instance().getPrivilegesManager().addRoleToPlayerSynced(this.section.getCurrentPlayerUUID(), this.currRoleEntry.index);
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
