package austeretony.oxygen_core.client.gui.privileges.management.privileges.context;

import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.gui.elements.OxygenContextMenu.OxygenContextMenuAction;
import austeretony.oxygen_core.client.gui.privileges.management.DefaultPrivilegesSection;

public class RemovePrivilegeContextAction implements OxygenContextMenuAction {

    @Override
    public String getLocalizedName(GUIBaseElement currElement) {
        return ClientReference.localize("oxygen_core.gui.privileges.management.removePrivilege");
    }

    @Override
    public boolean isValid(GUIBaseElement currElement) {
        return true;
    }

    @Override
    public void execute(GUIBaseElement currElement) {
        ((DefaultPrivilegesSection) currElement.getScreen().getWorkspace().getCurrentSection()).openRemoveDefaultPrivilegeCallback();
    }
}
