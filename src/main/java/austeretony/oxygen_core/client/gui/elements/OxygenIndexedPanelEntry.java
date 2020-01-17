package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.button.GUIButton;

public class OxygenIndexedPanelEntry<T> extends GUIButton {

    public final T index;

    public OxygenIndexedPanelEntry(T index) {
        this.index = index;
    }
}
