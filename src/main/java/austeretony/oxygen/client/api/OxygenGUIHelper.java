package austeretony.oxygen.client.api;

import java.util.Set;

import austeretony.alternateui.screen.contextmenu.AbstractContextAction;
import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.gui.AbstractMenuEntry;
import austeretony.oxygen.client.gui.OxygenMenuManager;
import austeretony.oxygen.client.gui.overlay.IOverlay;
import austeretony.oxygen.client.sync.gui.api.IGUIHandlerClient;

public class OxygenGUIHelper {

    public static void registerScreenId(int screenId) {
        OxygenManagerClient.instance().getGUIManager().registerScreenId(screenId);
    }

    public static void registerSharedDataListenerScreen(int screenId, IGUIHandlerClient guiHandler) {
        OxygenManagerClient.instance().getGUIManager().registerSharedDataListenerScreen(screenId, guiHandler);
    }

    public static void registerOverlay(IOverlay overlay) {
        OxygenManagerClient.instance().getGUIManager().registerOverlay(overlay);
    }

    public static void openSharedDataListenerScreen(int screenId) {
        OxygenManagerClient.instance().getGUIManager().openSharedDataListenerScreenSynced(screenId);
    }

    public static boolean isNeedSync(int screenId) {
        return OxygenManagerClient.instance().getGUIManager().isNeedSync(screenId);
    }

    public static void needSync(int screenId) {
        OxygenManagerClient.instance().getGUIManager().needSync(screenId);
    }

    public static void resetNeedSync(int screenId) {
        OxygenManagerClient.instance().getGUIManager().resetNeedSync(screenId);
    }

    public static boolean isScreenInitialized(int screenId) {
        return OxygenManagerClient.instance().getGUIManager().isScreenInitialized(screenId);
    }

    public static void screenInitialized(int screenId) {
        OxygenManagerClient.instance().getGUIManager().screenInitialized(screenId);
    }

    public static void resetScreenInitialized(int screenId) {
        OxygenManagerClient.instance().getGUIManager().resetScreenInitialized(screenId);
    }

    public static boolean isDataReceived(int screenId) {
        return OxygenManagerClient.instance().getGUIManager().isDataReceived(screenId);
    }

    public static void dataReceived(int screenId) {
        OxygenManagerClient.instance().getGUIManager().dataReceived(screenId);
    }

    public static void resetDataReceived(int screenId) {
        OxygenManagerClient.instance().getGUIManager().resetDataReceived(screenId);
    }

    public static void registerContextAction(int screenId, AbstractContextAction action) {
        OxygenManagerClient.instance().getGUIManager().registerContextAction(screenId, action);
    }

    public static Set<AbstractContextAction> getContextActions(int screenId) {
        return OxygenManagerClient.instance().getGUIManager().getContextActions(screenId);
    }

    public static boolean isOxygenMenuEnabled() {
        return OxygenMenuManager.isOxygenMenuEnabled();
    }

    public static void registerOxygenMenuEntry(AbstractMenuEntry entry) {
        OxygenMenuManager.registerOxygenMenuEntry(entry);
    }
}
