package austeretony.oxygen.client.gui.friendlist.ignored;

import austeretony.alternateui.screen.contextmenu.AbstractContextAction;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.oxygen.client.gui.friendlist.IgnoreListGUISection;
import net.minecraft.client.resources.I18n;

public class EditNoteContextAction extends AbstractContextAction {

    private IgnoreListGUISection section;

    public EditNoteContextAction(IgnoreListGUISection section) {
        this.section = section;
    }

    @Override
    protected String getName(GUIBaseElement currElement) {
        return I18n.format("oxygen.gui.action.editNote");
    }

    @Override
    public boolean isValid(GUIBaseElement currElement) {
        return true;
    }

    @Override
    public void execute(GUIBaseElement currElement) {
        this.section.openEditNoteCallback();
    }
}
