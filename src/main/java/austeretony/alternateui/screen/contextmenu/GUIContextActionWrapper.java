package austeretony.alternateui.screen.contextmenu;

import austeretony.alternateui.screen.core.GUISimpleElement;

/**
 * Context action wrapper for context menu.
 * 
 * @author AustereTony
 */
public class GUIContextActionWrapper extends GUISimpleElement<GUIContextActionWrapper> {

    public final AbstractContextAction action;

    public GUIContextActionWrapper(AbstractContextAction action) {
        this.action = action;
    }
}
