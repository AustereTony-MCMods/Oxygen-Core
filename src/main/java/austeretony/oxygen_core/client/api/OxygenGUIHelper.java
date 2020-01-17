package austeretony.oxygen_core.client.api;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.gui.elements.OxygenContextMenu.OxygenContextMenuAction;
import austeretony.oxygen_core.client.gui.menu.OxygenMenuEntry;
import austeretony.oxygen_core.client.gui.overlay.Overlay;

public class OxygenGUIHelper {

    public static void registerScreenId(int screenId) {
        OxygenManagerClient.instance().getGUIManager().registerScreenId(screenId);
    }

    public static void registerOverlay(Overlay overlay) {
        OxygenManagerClient.instance().getGUIManager().registerOverlay(overlay);
    }

    public static void registerContextAction(int screenId, OxygenContextMenuAction action) {
        OxygenManagerClient.instance().getGUIManager().registerContextAction(screenId, action);
    }

    public static boolean isOxygenMenuEnabled() {
        return OxygenMenuHelper.isOxygenMenuEnabled();
    }

    public static void registerOxygenMenuEntry(OxygenMenuEntry entry) {
        OxygenMenuHelper.registerOxygenMenuEntry(entry);
    }
}
