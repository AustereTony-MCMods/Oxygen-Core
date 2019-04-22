package austeretony.alternateui.screen.contextmenu;

import austeretony.alternateui.screen.core.GUIBaseElement;

/**
 * @author AustereTony
 */
public abstract class AbstractContextAction {

    protected abstract String getName(GUIBaseElement currElement);

    protected abstract boolean isValid(GUIBaseElement currElement);

    protected abstract void execute(GUIBaseElement currElement);
}
