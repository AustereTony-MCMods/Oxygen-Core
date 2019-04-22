package austeretony.oxygen.client.gui.playerlist;

import java.util.UUID;

import austeretony.alternateui.screen.contextmenu.AbstractContextAction;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.gui.PlayerGUIButton;
import austeretony.oxygen.common.api.OxygenHelperClient;
import net.minecraft.client.resources.I18n;

public class AddToFriendsContextAction extends AbstractContextAction {

    @Override
    protected String getName(GUIBaseElement currElement) {
        return I18n.format("oxygen.gui.action.addToFriends");
    }

    @Override
    protected boolean isValid(GUIBaseElement currElement) {
        UUID targetUUID = ((PlayerGUIButton) currElement).playerUUID;
        return !targetUUID.equals(OxygenHelperClient.getPlayerUUID()) 
                && !OxygenHelperClient.getPlayerData().haveFriendListEntryForUUID(targetUUID);
    }

    @Override
    protected void execute(GUIBaseElement currElement) {
        UUID targetUUID = ((PlayerGUIButton) currElement).playerUUID;
        OxygenManagerClient.instance().getFriendListManager().sendFriendRequestSynced(targetUUID);
    }
}
