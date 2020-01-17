package austeretony.oxygen_core.client.gui.privileges.players.context;

import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.gui.elements.OxygenContextMenu.OxygenContextMenuAction;
import austeretony.oxygen_core.client.gui.privileges.PlayersSection;

public class RemoveRoleContextAction implements OxygenContextMenuAction {

    @Override
    public String getLocalizedName(GUIBaseElement currElement) {
        return ClientReference.localize("oxygen_core.gui.privileges.removeRoleFromPlayer");
    }

    @Override
    public boolean isValid(GUIBaseElement currElement) {
        return true;
    }

    @Override
    public void execute(GUIBaseElement currElement) {
        ((PlayersSection) currElement.getScreen().getWorkspace().getCurrentSection()).openRemoveRoleCallback();
    }
}
