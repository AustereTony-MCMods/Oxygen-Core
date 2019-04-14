package austeretony.alternateui.screen.contextmenu;

import austeretony.alternateui.screen.core.GUIBaseElement;

/**
 * @author AustereTony
 */
public abstract class AbstractContextAction {

    private GUIBaseElement element;

    public void setElement(GUIBaseElement element) {
        this.element = element;
    }

    public GUIBaseElement getElement() {
        return this.element;
    }

    protected abstract String getName();

    protected abstract boolean isValid();

    protected abstract void execute();
}
