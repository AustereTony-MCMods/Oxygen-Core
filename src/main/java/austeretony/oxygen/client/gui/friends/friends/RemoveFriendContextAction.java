package austeretony.oxygen.client.gui.friends.friends;

import austeretony.alternateui.screen.contextmenu.AbstractContextAction;
import austeretony.oxygen.client.gui.friends.FriendsGUISection;
import net.minecraft.client.resources.I18n;

public class RemoveFriendContextAction extends AbstractContextAction {

    private FriendsGUISection section;

    public RemoveFriendContextAction(FriendsGUISection section) {
        this.section = section;
    }

    @Override
    protected String getName() {
        return I18n.format("oxygen.gui.action.remove");
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void execute() {
        this.section.openRemoveCallback();
    }
}
