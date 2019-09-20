package austeretony.oxygen_core.client.gui;

import austeretony.alternateui.screen.list.GUIDropDownElement;

public class IndexedGUIDropDownElement<T> extends GUIDropDownElement {

    public final T index;

    public IndexedGUIDropDownElement(T index) {
        super();
        this.index = index;
    }
}
