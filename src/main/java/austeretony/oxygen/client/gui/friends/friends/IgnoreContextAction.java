package austeretony.oxygen.client.gui.friends.friends;

import austeretony.alternateui.screen.contextmenu.AbstractContextAction;
import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.gui.friends.FriendsGUISection;
import austeretony.oxygen.common.config.OxygenConfig;
import net.minecraft.client.resources.I18n;

public class IgnoreContextAction extends AbstractContextAction {

    private FriendsGUISection section;

    public IgnoreContextAction(FriendsGUISection section) {
        this.section = section;
    }

    @Override
    protected String getName() {
        return I18n.format("oxygen.gui.action.ignore");
    }

    @Override
    public boolean isValid() {
        return OxygenManagerClient.instance().getPlayerData().getIgnoredAmount() < OxygenConfig.MAX_IGNORED.getIntValue();
    }

    @Override
    public void execute() {
        this.section.openIgnoreFriendCallback();
    }
}
