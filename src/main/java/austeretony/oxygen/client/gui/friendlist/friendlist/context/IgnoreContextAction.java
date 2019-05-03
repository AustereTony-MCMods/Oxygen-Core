package austeretony.oxygen.client.gui.friendlist.friendlist.context;

import austeretony.alternateui.screen.contextmenu.AbstractContextAction;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.gui.friendlist.FriendListGUISection;
import austeretony.oxygen.common.config.OxygenConfig;
import net.minecraft.client.resources.I18n;

public class IgnoreContextAction extends AbstractContextAction {

    private FriendListGUISection section;

    public IgnoreContextAction(FriendListGUISection section) {
        this.section = section;
    }

    @Override
    protected String getName(GUIBaseElement currElement) {
        return I18n.format("oxygen.gui.action.ignore");
    }

    @Override
    public boolean isValid(GUIBaseElement currElement) {
        return OxygenManagerClient.instance().getPlayerData().getIgnoredAmount() < OxygenConfig.MAX_IGNORED.getIntValue();
    }

    @Override
    public void execute(GUIBaseElement currElement) {
        this.section.openIgnoreFriendCallback();
    }
}
