package austeretony.oxygen_core.client.gui.settings;

import austeretony.alternateui.screen.framework.GUIElementsFramework;
import austeretony.oxygen_core.client.gui.settings.gui.callback.SetColorCallback;
import austeretony.oxygen_core.client.gui.settings.gui.callback.SetKeyCallback;
import austeretony.oxygen_core.client.gui.settings.gui.callback.SetOffsetCallback;
import austeretony.oxygen_core.client.gui.settings.gui.callback.SetScaleCallback;

public interface ElementsContainer {

    String getLocalizedName();

    boolean hasCommonSettings();

    boolean hasGUISettings();

    void addCommon(GUIElementsFramework framework);

    void addGUI(GUIElementsFramework framework);

    void resetCommon();

    void resetGUI();

    void initSetColorCallback(SetColorCallback callback);

    void initSetScaleCallback(SetScaleCallback callback);

    void initSetOffsetCallback(SetOffsetCallback callback);

    void initSetKeyCallback(SetKeyCallback callback);
}
