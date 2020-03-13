package austeretony.oxygen_core.client.gui.privileges.management.roles.context;

import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.gui.elements.OxygenContextMenu.OxygenContextMenuAction;
import austeretony.oxygen_core.client.gui.privileges.management.RolesSection;
import austeretony.oxygen_core.common.main.OxygenMain;

public class EditRoleContextAction implements OxygenContextMenuAction {

    @Override
    public String getLocalizedName(GUIBaseElement currElement) {
        return ClientReference.localize("oxygen_core.gui.privileges.management.edit");
    }

    @Override
    public boolean isValid(GUIBaseElement currElement) {
        return ((RolesSection) currElement.getScreen().getWorkspace().getCurrentSection()).getCurrentRoleEntry().getWrapped().getId() != OxygenMain.OPERATOR_ROLE_ID;
    }

    @Override
    public void execute(GUIBaseElement currElement) {
        ((RolesSection) currElement.getScreen().getWorkspace().getCurrentSection()).openEditRoleCallback();
    }
}
