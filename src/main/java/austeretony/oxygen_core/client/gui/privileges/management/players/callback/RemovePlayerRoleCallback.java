package austeretony.oxygen_core.client.gui.privileges.management.players.callback;

import org.lwjgl.input.Keyboard;

import austeretony.alternateui.screen.callback.AbstractGUICallback;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.elements.OxygenCallbackBackgroundFiller;
import austeretony.oxygen_core.client.gui.elements.OxygenKeyButton;
import austeretony.oxygen_core.client.gui.elements.OxygenScrollablePanel;
import austeretony.oxygen_core.client.gui.elements.OxygenTextLabel;
import austeretony.oxygen_core.client.gui.privileges.management.PlayersSection;
import austeretony.oxygen_core.client.gui.privileges.management.PrivilegesManagementScreen;
import austeretony.oxygen_core.client.gui.privileges.management.players.PlayerRolePanelEntry;
import austeretony.oxygen_core.common.PlayerSharedData;
import austeretony.oxygen_core.common.main.OxygenMain;

public class RemovePlayerRoleCallback extends AbstractGUICallback {

    private final PrivilegesManagementScreen screen;

    private final PlayersSection section;

    private OxygenScrollablePanel rolesPanel;

    private OxygenKeyButton closeButton;

    public RemovePlayerRoleCallback(PrivilegesManagementScreen screen, PlayersSection section, int width, int height) {
        super(screen, section, width, height);
        this.screen = screen;
        this.section = section;
    }

    @Override
    public void init() {
        this.enableDefaultBackground(EnumBaseGUISetting.FILL_CALLBACK_COLOR.get().asInt());
        this.addElement(new OxygenCallbackBackgroundFiller(0, 0, this.getWidth(), this.getHeight()));
        this.addElement(new OxygenTextLabel(4, 12, ClientReference.localize("oxygen_core.gui.privileges.management.callback.removePlayerRole"), EnumBaseGUISetting.TEXT_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        this.addElement(new OxygenTextLabel(6, 23, ClientReference.localize("oxygen_core.gui.privileges.roles"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() + 0.05F, EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));
        this.addElement(this.rolesPanel = new OxygenScrollablePanel(this.screen, 6, 26, this.getWidth() - 15, 10, 1, OxygenMain.MAX_ROLES_PER_PLAYER, 5, EnumBaseGUISetting.TEXT_PANEL_SCALE.get().asFloat(), false));

        this.addElement(this.closeButton = new OxygenKeyButton(this.getWidth() - 55, this.getHeight() - 10, ClientReference.localize("oxygen_core.gui.close"), Keyboard.KEY_X, this::close));
    }

    private void loadPlayerRoles() {
        this.rolesPanel.reset();

        PlayerSharedData sharedData = OxygenManagerClient.instance().getPrivilegesContainer().getServerPlayerData(this.section.getCurrentPlayerEntry().getWrapped());
        int roleId;
        for (int i = 0; i < OxygenMain.MAX_ROLES_PER_PLAYER; i++) {
            roleId = sharedData.getByte(i + OxygenMain.ROLES_SHARED_DATA_STARTING_INDEX);
            if (roleId != OxygenMain.DEFAULT_ROLE_INDEX)
                this.rolesPanel.addEntry(new PlayerRolePanelEntry(OxygenManagerClient.instance().getPrivilegesContainer().getServerRole(roleId), this.section.getCurrentPlayerEntry().getWrapped()));
        }
    }

    @Override
    protected void onOpen() {
        this.loadPlayerRoles();
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element, int mouseButton) {
        if (mouseButton == 0)
            if (element == this.closeButton)
                this.close();
    }
}
