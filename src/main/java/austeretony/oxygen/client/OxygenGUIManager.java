package austeretony.oxygen.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.alternateui.screen.contextmenu.AbstractContextAction;
import austeretony.oxygen.client.core.api.ClientReference;
import austeretony.oxygen.client.gui.overlay.IOverlay;
import austeretony.oxygen.client.sync.gui.api.IGUIHandlerClient;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.network.server.SPOpenOxygenScreen;
import austeretony.oxygen.common.sync.gui.EnumScreenType;
import austeretony.oxygen.util.LinkedHashSetWrapper;

public class OxygenGUIManager {

    //used to insure displayed data in GUIs is actual (synchronized before display)
    private final Map<Integer, Boolean>
    needSync = new ConcurrentHashMap<Integer, Boolean>(10),//GUIs which rely on some server data
    initializedGUIs = new ConcurrentHashMap<Integer, Boolean>(10),//fully initialized GUIs
    receivedData = new ConcurrentHashMap<Integer, Boolean>(10);//GUIs data synchronized for

    //a way for other modules to add their own context actions for any menus (screenId is used)
    private final Map<Integer, LinkedHashSetWrapper<AbstractContextAction>> contextActionsRegistry = new HashMap<Integer, LinkedHashSetWrapper<AbstractContextAction>>(10);

    private final Map<Integer, IGUIHandlerClient> guiHandlers = new HashMap<Integer, IGUIHandlerClient>(5);

    private final Set<IOverlay> overlays = new HashSet<IOverlay>(5);

    public void registerScreenId(int screenId) {
        this.needSync.put(screenId, false);
        this.initializedGUIs.put(screenId, false);
        this.receivedData.put(screenId, false);
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

    public boolean isDataReceived(int screenId) {
        return this.receivedData.get(screenId);
    }

    public void dataReceived(int screenId) {
        this.receivedData.put(screenId, true);
    }

    public void resetDataReceived(int screenId) {
        this.receivedData.put(screenId, false);
    }

    public void registerContextAction(int screenId, AbstractContextAction action) {
        if (!this.contextActionsRegistry.containsKey(screenId))
            this.registerScreenId(screenId);
        this.contextActionsRegistry.get(screenId).add(action);
    }

    public Set<AbstractContextAction> getContextActions(int screenId) {
        return this.contextActionsRegistry.get(screenId).set;
    }

    public void registerSharedDataListenerScreen(int screenId, IGUIHandlerClient guiHandler) {
        this.guiHandlers.put(screenId, guiHandler);
    }

    public void updateSharedDataListenersDataState(boolean flag) {
        for (int screenId : this.guiHandlers.keySet())
            this.receivedData.put(screenId, flag);
    }

    public IGUIHandlerClient getGuiHandlerForScreen(int screenId) {
        return this.guiHandlers.get(screenId);
    }

    public void openSharedDataListenerScreenSynced(int screenId) {
        if (ClientReference.getCurrentScreen() != null)
            ClientReference.displayGuiScreen(null);
        this.needSync(screenId);
        OxygenMain.network().sendToServer(new SPOpenOxygenScreen(EnumScreenType.SHARED_DATA_LISTENER_SCREEN, screenId));
    }

    public void openSharedDataListenerScreenDelegated(int screenId) {
        ClientReference.delegateToClientThread(()->this.getGuiHandlerForScreen(screenId).open());
    }

    public void registerOverlay(IOverlay overlay) {
        this.overlays.add(overlay);
    }

    public Set<IOverlay> getOverlays() {
        return this.overlays;
    }
}