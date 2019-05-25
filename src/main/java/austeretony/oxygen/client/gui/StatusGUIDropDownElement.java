package austeretony.oxygen.client.gui;

import austeretony.alternateui.screen.list.GUIDropDownElement;
import austeretony.oxygen.common.main.OxygenPlayerData;

public class StatusGUIDropDownElement extends GUIDropDownElement {

    public final OxygenPlayerData.EnumActivityStatus status;

    public StatusGUIDropDownElement(OxygenPlayerData.EnumActivityStatus status) {
        super();
        this.status = status;
    }
}
