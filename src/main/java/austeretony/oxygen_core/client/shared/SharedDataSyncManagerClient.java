package austeretony.oxygen_core.client.shared;

import java.util.HashMap;
import java.util.Map;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.server.SPRequestSharedDataSync;

public class SharedDataSyncManagerClient {

    private final Map<Integer, SharedDataSyncListener> handlers = new HashMap<>(10);

    public void registerSyncListener(int id, SharedDataSyncListener listener) {
        this.handlers.put(id, listener);
    }

    public void syncSharedData(int id) {
        OxygenMain.network().sendToServer(new SPRequestSharedDataSync(id));
    }

    public void sharedDataReceived(int id) {
        if (this.handlers.containsKey(id))
            this.handlers.get(id).synced();
    }

    public static interface SharedDataSyncListener {

        void synced();
    }
}
