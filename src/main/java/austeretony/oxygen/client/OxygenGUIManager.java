package austeretony.oxygen.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.alternateui.screen.contextmenu.AbstractContextAction;
import austeretony.oxygen.util.LinkedHashSetWrapper;

public class OxygenGUIManager {

    //used to insure displayed data in GUIs is actual (synchronized before display)
    private final Map<Integer, Boolean>
    needSync = new ConcurrentHashMap<Integer, Boolean>(10),//GUIs which rely on some server data
    initializedGUIs = new ConcurrentHashMap<Integer, Boolean>(10),//fully initialized GUIs
    recievedData = new ConcurrentHashMap<Integer, Boolean>(10);//GUIs data synchronized for

    //a way for other modules to add their own context actions for any menus (screenId is used)
    private final Map<Integer, LinkedHashSetWrapper<AbstractContextAction>> contextActionsRegistry = new HashMap<Integer, LinkedHashSetWrapper<AbstractContextAction>>(10);

    private final Set<Integer> sharedDataListeners = new HashSet<Integer>(10);

    public void registerScreenId(int screenId) {
        this.needSync.put(screenId, false);
        this.initializedGUIs.put(screenId, false);
        this.recievedData.put(screenId, false);
        if (!this.contextActionsRegistry.containsKey(screenId))
            this.contextActionsRegistry.put(screenId, new LinkedHashSetWrapper<AbstractContextAction>());
    }

    public boolean isNeedSync(int screenId) {
        return this.needSync.get(screenId);
    }

    public void needSync(int screenId) {
        this.needSync.put(screenId, true);
    }

    public void resetNeedSync(int screenId) {
        this.needSync.put(screenId, false);
    }

    public boolean isScreenInitialized(int screenId) {
        return this.initializedGUIs.get(screenId);
    }

    public void screenInitialized(int screenId) {
        this.initializedGUIs.put(screenId, true);
    }

    public void resetScreenInitialized(int screenId) {
        this.initializedGUIs.put(screenId, false);
    }

    public boolean isDataRecieved(int screenId) {
        return this.recievedData.get(screenId);
    }

    public void dataRecieved(int screenId) {
        this.recievedData.put(screenId, true);
    }

    public void resetDataRecieved(int screenId) {
        this.recievedData.put(screenId, false);
    }

    public void registerContextAction(int screenId, AbstractContextAction action) {
        if (!this.contextActionsRegistry.containsKey(screenId))
            this.registerScreenId(screenId);
        this.contextActionsRegistry.get(screenId).add(action);
    }

    public Set<AbstractContextAction> getContextActions(int screenId) {
        return this.contextActionsRegistry.get(screenId).set;
    }

    public void registerSharedDataListenerScreen(int screenId) {
        this.sharedDataListeners.add(screenId);
    }

    public void updateSharedDataListenersDataState(boolean flag) {
        for (int screenId : this.sharedDataListeners)
            this.recievedData.put(screenId, flag);
    }
}