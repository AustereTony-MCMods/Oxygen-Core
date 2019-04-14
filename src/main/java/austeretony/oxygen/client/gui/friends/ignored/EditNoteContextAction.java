package austeretony.oxygen.client.gui.friends.ignored;

import austeretony.alternateui.screen.contextmenu.AbstractContextAction;
import austeretony.oxygen.client.gui.friends.IgnoredGUISection;
import net.minecraft.client.resources.I18n;

public class EditNoteContextAction extends AbstractContextAction {

    private IgnoredGUISection section;

    public EditNoteContextAction(IgnoredGUISection section) {
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
