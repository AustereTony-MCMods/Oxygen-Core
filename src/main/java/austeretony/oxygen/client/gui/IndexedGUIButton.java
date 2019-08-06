package austeretony.oxygen.client.gui;

import austeretony.alternateui.screen.button.GUIButton;

public class IndexedGUIButton<T> extends GUIButton {

    public final T index;

    public IndexedGUIButton(T index) {
        this.index = index;
    }
}
