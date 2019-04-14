package austeretony.oxygen.client.gui.friends.friends;

import austeretony.alternateui.screen.contextmenu.AbstractContextAction;
import austeretony.oxygen.client.gui.friends.FriendsGUISection;
import net.minecraft.client.resources.I18n;

public class EditNoteContextAction extends AbstractContextAction {

    private FriendsGUISection section;

    public EditNoteContextAction(FriendsGUISection section) {
        this.section = section;
    }

    @Override
    protected String getName() {
        return I18n.format("oxygen.gui.action.editNote");
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void execute() {
        this.section.openEditNoteCallback();
    }
}
