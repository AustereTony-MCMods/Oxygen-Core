package austeretony.alternateui.screen.contextmenu;

import austeretony.alternateui.screen.core.GUISimpleElement;

/**
 * Context action wrapper for context menu.
 * 
 * @author AustereTony
 */
public class GUIContextAction extends GUISimpleElement<GUIContextAction> {

    public final AbstractContextAction action;

    public GUIContextAction(AbstractContextAction action) {
        this.action = action;
    }
}
