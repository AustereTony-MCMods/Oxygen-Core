package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.button.GUIButton;

public class OxygenWrapperPanelEntry<T> extends GUIButton {

    protected final T wrapped;

    public OxygenWrapperPanelEntry(T index) {
        this.wrapped = index;
    }

    public T getWrapped() {
        return this.wrapped;
    }
}
