package austeretony.oxygen.common.api;

import java.util.Set;

import austeretony.alternateui.screen.contextmenu.AbstractContextAction;
import austeretony.oxygen.client.OxygenManagerClient;

public class OxygenGUIHelper {

    public static void registerScreenId(int screenId) {
        OxygenManagerClient.instance().getGUIManager().registerScreenId(screenId);
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

    public static boolean isDataRecieved(int screenId) {
        return OxygenManagerClient.instance().getGUIManager().isDataRecieved(screenId);
    }

    public static void dataRecieved(int screenId) {
        OxygenManagerClient.instance().getGUIManager().dataRecieved(screenId);
    }

    public static void resetDataRecieved(int screenId) {
        OxygenManagerClient.instance().getGUIManager().resetDataRecieved(screenId);
    }

    public static void registerContextAction(int screenId, AbstractContextAction action) {
        OxygenManagerClient.instance().getGUIManager().registerContextAction(screenId, action);
    }

    public static Set<AbstractContextAction> getContextActions(int screenId) {
        return OxygenManagerClient.instance().getGUIManager().getContextActions(screenId);
    }
}
