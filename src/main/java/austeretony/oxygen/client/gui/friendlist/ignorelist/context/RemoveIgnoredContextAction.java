package austeretony.oxygen.client.gui.friendlist.ignorelist.context;

import austeretony.alternateui.screen.contextmenu.AbstractContextAction;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.oxygen.client.gui.friendlist.IgnoreListGUISection;
import net.minecraft.client.resources.I18n;

public class RemoveIgnoredContextAction extends AbstractContextAction {

    private IgnoreListGUISection section;

    public RemoveIgnoredContextAction(IgnoreListGUISection section) {
        this.section = section;
    }

    @Override
    protected String getName(GUIBaseElement currElement) {
        return I18n.format("oxygen.gui.action.remove");
    }

    @Override
    public boolean isValid(GUIBaseElement currElement) {
        return true;
    }

    @Override
    public void execute(GUIBaseElement currElement) {
        this.section.openRemoveCallback();
    }
}
